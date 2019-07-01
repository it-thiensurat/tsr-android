package th.co.thiensurat.fragments.report;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.ReportSaleByAreaController;
import th.co.thiensurat.data.info.ReportSaleByAreaMapInfo;

/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/

public class ReportSummarySaleByAreaMapFragment extends BHPagerFragment {
    @InjectView
    private MapView mapArea;

    private LocationManager locationManager;
    private GoogleMap googleMap;
    private List<ReportSaleByAreaMapInfo> data;
    private boolean isActive;
    private String fortnightID;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_sale_by_area_map;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // Gets the MapView from the XML layout and creates it
        mapArea.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapArea.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                /*** [START] :: Permission ***/
                //googleMap.setMyLocationEnabled(false);

                new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(BHPermissions bhPermissions) {
                        googleMap.setMyLocationEnabled(false);
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
                /*** [END] :: Permission ***/


                // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                try {
                    MapsInitializer.initialize(activity);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ReportSummarySaleByAreaMapFragment.this.googleMap = googleMap;

                // Updates the location and zoom of the MapView
//                googleMap.addMarker(new MarkerOptions().position(new LatLng(13.960885, 100.622214)).title("ตำแหน่งปัจจุบันของลูกค้า").snippet("" + 13.960885 + "    " + 100.622214));
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.960885, 100.622214), 16));
            }
        });

//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
//        googleMap.animateCamera(cameraUpdate);

        //setMapView();
    }

    @Override
    public void onResume() {
        mapArea.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapArea.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapArea.onLowMemory();
    }

    public void refreshData(boolean isActive, String fortnightID) {
        boolean dataChange = !fortnightID.equals(this.fortnightID);
        this.isActive = isActive;
        if (isActive && dataChange) {
            this.fortnightID = fortnightID;
            refreshData();
        }
    }

    private void refreshData() {
        if (googleMap != null) {
            new BackgroundProcess(activity) {
                @Override
                protected void calling() {
                    data = new ReportSaleByAreaController().getMaps(BHPreference.employeeID(), fortnightID);
                }

                @Override
                protected void after() {
                    if (data != null && data.size() > 0) {
                        googleMap.clear();
                        boolean isCenter = false;
                        for (ReportSaleByAreaMapInfo info : data) {
                            LatLng point = new LatLng(info.Latitude, info.Longitude);
                            googleMap.addMarker(new MarkerOptions().position(point).title(info.CustomerName));
                            if (!isCenter) {
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
                            }
                        }

                    }
                }
            }.start();
        }
    }

}
