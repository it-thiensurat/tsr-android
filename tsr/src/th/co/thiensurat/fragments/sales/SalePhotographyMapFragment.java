package th.co.thiensurat.fragments.sales;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHStorage.FolderType;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.bighead.utilities.save_image_to_gallery;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AddressController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.ContractImageController.ImageType;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ContractImageInfo;

public class SalePhotographyMapFragment extends BHFragment {

    private static String IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
    public static String FRAGMENT_SALE_PHOTOGRAPHY_MAP = "th.co.thiensurat.fragment.sale.salephotographymap.fragment";
    private GoogleMap mMap;
    private Marker mMarker;
    private LocationManager lm;
    double lat, lng;
    private String imageTypeCode;     //-- Fixed - [BHPROJ-0024-678]
    private static String Parth;
    private static String imagename;
    private boolean isMarker = false;

    /*** [START] Fixed - [BHPROJ-0024-678] ***/
    public static class Data extends BHParcelable {
        public String imageTypeCode;
        public int title;
    }
    private Data data;
    /*** [END] Fixed - [BHPROJ-0024-678] ***/

    @InjectView private RelativeLayout relativeLayout;
    private MapFragment mapFragment;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub

        if(data == null){
            data = getData();
        }
        //return R.string.title_sales;
        return data.title;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_photography_map;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back, R.string.button_add_map};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
        data = getData();

        showMessage("กรุณารอสักครู่ กำลังพาไปยังตำแหน่งปัจจุบัน");
//        imageTypeCode = ImageType.MAP.toString();     //-- Fixed - [BHPROJ-0024-678]
        imageTypeCode = data.imageTypeCode;

        FolderType F = BHStorage.FolderType.Picture;
        Parth = BHStorage.getFolder(F);

        /*mMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapPhoto)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);*/

        /*** [START] :: MapFragment ***/
        /*((MapFragment) getChildFragmentManager().findFragmentById(R.id.mapPhoto)).getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });*/

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            });
        }

        // R.id.map is a FrameLayout, not a Fragment
        getChildFragmentManager().beginTransaction().replace(R.id.mapPhoto, mapFragment).commit();
        /*** [END] :: MapFragment ***/


        activity.setupProcessButtons(this, new int[]{R.string.button_back, R.string.button_type_satellite, R.string.button_add_map});
    }


    LocationListener listener = new LocationListener() {
        public void onLocationChanged(Location loc) {
            if(!isMarker){
                LatLng coordinate = new LatLng(loc.getLatitude(), loc.getLongitude());
                lat = loc.getLatitude();
                lng = loc.getLongitude();

                if (mMarker != null)
                    mMarker.remove();

                mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("ตำแหน่งปัจจุบันของลูกค้า").snippet("" + lat + "    " + lng));

                //mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).draggable(true));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 18));

                isMarker = true;
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    public void onResume() {
        super.onResume();

        lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        /*** [START] :: Permission ***/
        new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

            @Override
            public void onSuccess(BHPermissions bhPermissions) {
                if (isNetwork) {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, listener);
                }

                if (isGPS) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
                }
            }

            @Override
            public void onNotSuccess(BHPermissions bhPermissions) {
                bhPermissions.openAppSettings(getActivity());
            }

            @Override
            public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
                bhPermissions.showMessage(getActivity(), permissionType);
            }

        }, BHPermissions.PermissionType.LOCATION);

        /*if (isNetwork) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, listener);
            *//*Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }*//*
        }

        if (isGPS) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
            *//*Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
                isMarker = true;
            }*//*
        }*/
        /*** [END] :: Permission ***/

    }

    public void onPause() {
        super.onPause();
        lm.removeUpdates(listener);
    }

    /*private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        //BitMapToString(bmOverlay);
        return bmOverlay;
    }*/

    //ไว้ดูรูป
    /*public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }*/


    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        List<Integer> listId = new ArrayList<Integer>();
        switch (buttonID) {
            /*<string name="button_type_normal">NORMAL</string>
        <string name="button_type_satellite">SATELLITE</string>
        <string name="button_type_terrain">TERRAIN</string>
        <string name="button_type_hybrid">HYBRID</string> */

            case R.string.button_type_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                activity.setupProcessButtons(this, new int[]{R.string.button_back, R.string.button_type_satellite, R.string.button_add_map});
                break;

            case R.string.button_type_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                activity.setupProcessButtons(this, new int[]{R.string.button_back, R.string.button_type_terrain, R.string.button_add_map});
                break;

            case R.string.button_type_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                activity.setupProcessButtons(this, new int[]{R.string.button_back, R.string.button_type_hybrid, R.string.button_add_map});
                break;

            case R.string.button_type_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                activity.setupProcessButtons(this, new int[]{R.string.button_back, R.string.button_type_normal, R.string.button_add_map});
                break;

            case R.string.button_back:
                //showLastView(FRAGMENT_SALE_PHOTOGRAPHY_MAP);
                showLastView();
                break;
            case R.string.button_add_map:

                ContractImageInfo contractImageInfo = TSRController.getContractImage(BHPreference.RefNo(), data.imageTypeCode);

                if(contractImageInfo != null){
                    imagename = contractImageInfo.ImageID;
                }else {
                    imagename = DatabaseHelper.getUUID();
                }

                showMessage("บันทึกภาพ");
                /*mMap.snapshot(new SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        // TODO Auto-generated method stub
                        AddContractImage();
                        try {
                            File mediaStorageDir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);
                            if (!mediaStorageDir.exists()) {
                                if (!mediaStorageDir.mkdirs()) {
                                    Log.d(IMAGE_DIRECTORY_NAME, "" + IMAGE_DIRECTORY_NAME + " directory");
                                }
                            }
                            File dir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode + "/" + imagename + ".jpg");
                            FileOutputStream out = new FileOutputStream(dir);

                            //Screenshot RelativeLayout
                            relativeLayout.setDrawingCacheEnabled(true);
                            Bitmap bitMapRelativeLayout = relativeLayout.getDrawingCache();
                            relativeLayout.setDrawingCacheEnabled(false);

                            Bitmap newBitMapRelativeLayout = Bitmap.createBitmap(bitMapRelativeLayout.getWidth(), bitMapRelativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas c = new Canvas(newBitMapRelativeLayout);
                            relativeLayout.draw(c);

                            Bitmap newBitMap = overlay(bitmap, newBitMapRelativeLayout);
                            newBitMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            newBitMap.recycle();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });*/

                mMap.snapshot(new SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        // TODO Auto-generated method stub
                        AddContractImage();
                        try {
                            File mediaStorageDir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);
                            if (!mediaStorageDir.exists()) {
                                if (!mediaStorageDir.mkdirs()) {
                                    Log.d(IMAGE_DIRECTORY_NAME, "" + IMAGE_DIRECTORY_NAME + " directory");
                                }
                            }

                            File dir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode + "/" + imagename + ".jpg");
                            FileOutputStream out = new FileOutputStream(dir);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            bitmap.recycle();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            default:
                break;
        }
    }
    String DD="",NAME_IMAGE="",IMAGE_TYPE="";

    private void AddContractImage() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            ContractImageInfo input = new ContractImageInfo();

            AddressInfo addressInfo = new AddressInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub

                input.ImageID = imagename;
                input.RefNo = BHPreference.RefNo();
                input.ImageName = imagename + ".jpg";
                input.ImageTypeCode = data.imageTypeCode;
                input.SyncedDate = new Date();

                switch (Enum.valueOf(ContractImageController.ImageType.class, data.imageTypeCode)){

                    case MAP:
                        addressInfo = getAddress(BHPreference.RefNo(), AddressInfo.AddressType.AddressInstall);
                        break;
                    case MAPPAYMENT:
                        addressInfo = getAddress(BHPreference.RefNo(), AddressInfo.AddressType.AddressPayment);
                        break;
                }

                /*if(addressInfo != null){
                    addressInfo.Latitude =  mMap.getCameraPosition().target.latitude;
                    addressInfo.Longitude =  mMap.getCameraPosition().target.longitude;
                }*/

                if(addressInfo != null){
                    addressInfo.Latitude =  lat;
                    addressInfo.Longitude =  lng;
                }





                DD="/sdcard/Android/data/"+activity.getApplicationContext().getPackageName()+"/files/pictures/"+IMAGE_DIRECTORY_NAME + "/"+ input.ImageTypeCode+ "/" +input.ImageName;
                NAME_IMAGE=input.ImageName;
                IMAGE_TYPE=input.ImageTypeCode;



                checkPermissions();




            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                addContractImage(input, true);

                if(addressInfo != null){
                    saveAddress(addressInfo);
                }

            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub

            }
        }).start();
    }

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(activity, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                }


                File file21 = new File(DD);

                String filePath = file21.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                save_image_to_gallery.SaveImage(bitmap,NAME_IMAGE,IMAGE_TYPE);
                //  getResizedBiBitmaptmap(bitmap,NAME_IMAGE,IMAGE_TYPE);


                break;
        }
    }




}
