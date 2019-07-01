package th.co.thiensurat.fragments.credit.Import;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.fragments.credit.Audit.CheckCustomersDetailsFragment;
import th.co.thiensurat.fragments.credit.Person;

public class ImportAuditMapFragment extends BHFragment implements OnMapReadyCallback,
            ClusterManager.OnClusterClickListener<Person>,
            ClusterManager.OnClusterInfoWindowClickListener<Person>,
            ClusterManager.OnClusterItemClickListener<Person>,
            ClusterManager.OnClusterItemInfoWindowClickListener<Person>{

    private ClusterManager<Person> mClusterManager;

    @InjectView
    private TextView txtCountSaleAudit;

    private List<AssignInfo> result;
    private MapFragment mapFragment;

    @Override
    protected int titleID() {
        return R.string.title_import_credit_data;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_sale_audit_map;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        /*** [START] :: MapFragment ***/
        /*mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }*/

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }

        // R.id.map is a FrameLayout, not a Fragment
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        /*** [END] :: MapFragment ***/
    }

    private class PersonRenderer extends DefaultClusterRenderer<Person> {


        public PersonRenderer(GoogleMap getMap) {
            super(activity.getApplicationContext(), getMap, mClusterManager);

        }

        @Override
        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(person.profilePhoto)).title(person.name);
        }

    }

    private void setUpClusterer(GoogleMap getMap) {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Person>(activity, getMap);
        mClusterManager.setRenderer(new PersonRenderer(getMap));

        //getMap.setOnCameraChangeListener(mClusterManager);
        getMap.setOnCameraIdleListener(mClusterManager);

        getMap.setOnMarkerClickListener(mClusterManager);
        getMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        // Add cluster items (markers) to the cluster manager.
        addItems(getMap);
    }

    private void addItems(GoogleMap getMap) {

        // Set some lat/lng coordinates to start with.
        double lat;
        double lng;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();//setCenter
        for(AssignInfo info : result) {

            // Add ten cluster items in close proximity, for purposes of this example.
            lat = info.Latitude;
            lng = info.Longitude;

            Bitmap orgMarker = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
            Bitmap bgMarker = Bitmap.createBitmap(orgMarker.getWidth(), orgMarker.getHeight(), Bitmap.Config.ARGB_8888);
            //bgMarker.setHasAlpha(true);

            Canvas cv = new Canvas(bgMarker);
            cv.drawColor(Color.RED);

            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setAntiAlias(true);
            p.setTypeface(Typeface.DEFAULT);
            p.setTextSize(orgMarker.getWidth() / 3);
            p.setTextAlign(Paint.Align.CENTER);
            cv.drawText(String.valueOf(info.Order), (orgMarker.getWidth() / 2), orgMarker.getHeight() / 2, p);//set number

            cv.drawBitmap(bgMarker, 0, 0, p);

            //bgMarker = Bitmap.createScaledBitmap(bgMarker, orgMarker.getWidth(), orgMarker.getHeight(), true);

            Bitmap newMarker = Bitmap.createBitmap(orgMarker.getWidth(), orgMarker.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas mCanvas = new Canvas(newMarker);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mCanvas.drawBitmap(bgMarker, 0, 0, null);
            mCanvas.drawBitmap(orgMarker, 0, 0, paint);
            paint.setXfermode(null);

            LatLng offsetItem = new LatLng(lat, lng);
            mClusterManager.addItem(new Person(offsetItem, info.CustomerFullName, newMarker, info.RefNo));

            builder.include(offsetItem);//setCenter

        }

        //+setCenter
        LatLngBounds bounds = builder.build();

        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        /*** [START] :: Fixed - [BHPROJ-0025-743] :: [Android-ตรวจสอบลูกค้า] หน้าแสดง Map ให้ zoom-out ออกมาหน่อย เนื่องจากเปิดเข้ามาดูแผนที่แล้ว มีการ zoom-in เยอะเกินไปทำให้ทุกครั้งลูกค้าต้อง zoom-out เองตลอด  ***/
        //getMap.animateCamera(cu);
        getMap.moveCamera(cu);

        if(getMap.getCameraPosition().zoom > 18 ){
            getMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        }
        /*** [END] :: Fixed - [BHPROJ-0025-743] :: [Android-ตรวจสอบลูกค้า] หน้าแสดง Map ให้ zoom-out ออกมาหน่อย เนื่องจากเปิดเข้ามาดูแผนที่แล้ว มีการ zoom-in เยอะเกินไปทำให้ทุกครั้งลูกค้าต้อง zoom-out เองตลอด  ***/
        //-setCenter
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID){
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap getMap) {

       result = new AssignController().getSaleAuditByAssigneeEmpID(BHPreference.organizationCode(), BHPreference.employeeID(), AddressInfo.AddressType.AddressPayment.toString());

        if(result != null && result.size() > 0) {
            txtCountSaleAudit.setText(String.format("ลูกค้าที่ต้องตรวจสอบจำนวน %d คน", result.size()));
            setUpClusterer(getMap);
        }else{
            txtCountSaleAudit.setText(String.format("ลูกค้าที่ต้องตรวจสอบจำนวน %d คน", 0));
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        /*String firstName = cluster.getItems().iterator().next().name;
        showMessage( " (including " + firstName + ")");*/

        final List<AssignInfo> listItems = new ArrayList<AssignInfo>();
        for(Person info : cluster.getItems()){
            AssignInfo newPerson = new AssignInfo();
            newPerson.CustomerFullName = info.name;
            newPerson.profilePhoto = info.profilePhoto;
            newPerson.Latitude = info.mPosition.latitude;
            newPerson.Longitude = info.mPosition.longitude;
            listItems.add(newPerson);

        }

        final Dialog dialog = new Dialog(activity);
        dialog.setTitle("Make Your Selection");
        dialog.setContentView(R.layout.map_dialog);
        ListView list = (ListView) dialog.findViewById(R.id.listView1);
        BHArrayAdapter<AssignInfo> adapter = new BHArrayAdapter<AssignInfo>(activity, R.layout.map_dialog_list, listItems){
            class ViewHolder {
                public ImageView img, navigate;
                public TextView name;
            }

            @Override
            public void onViewItem(final int position, View view, Object holder, final AssignInfo info) {
                // TODO Auto-generated method stub
                ViewHolder vh = (ViewHolder) holder;
                vh.name.setText(info.CustomerFullName);
                vh.img.setImageBitmap(info.profilePhoto);
                vh.navigate.setVisibility(View.GONE);
            }
        };
        list.setAdapter(adapter);
        dialog.show();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String CustomerFullName = listItems.get(position).CustomerFullName;
                String latitude = String.valueOf(listItems.get(position).Latitude);
                String longitude= String.valueOf(listItems.get(position).Longitude);


                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String data = String.format("geo:%s,%s?q=%s,%s(%s)", latitude, longitude,
                        latitude, longitude, CustomerFullName);
                intent.setData(Uri.parse(data));
                startActivity(intent);
            }

        });
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Person> personCluster) {
    }

    @Override
    public boolean onClusterItemClick(Person person) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Person person) {

    }
}
