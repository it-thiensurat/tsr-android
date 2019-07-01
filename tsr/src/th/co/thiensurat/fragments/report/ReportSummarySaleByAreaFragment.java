package th.co.thiensurat.fragments.report;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.FortnightController;
import th.co.thiensurat.data.info.FortnightInfo;

public class ReportSummarySaleByAreaFragment extends BHFragment {
//    @InjectView
//    private Spinner spnFortnight;
//
//    @InjectView
//    private TabPageIndicator tabReportType;
//
//    @InjectView
//    private ViewPager vpReportType;
//
//    private ReportSummarySaleByAreaMapFragment mapFragment;
//    private ReportSummarySaleByAreaListFragment listFragment;
//    private String fortnightID;

    @Override
    protected int titleID() {
        return R.string.menu_report_zone;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_sale_by_area;
    }

    @Override
    protected int[] processButtons() {
        /*** [START] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
//        return new int[]{R.string.button_back};
        return null;
        /*** [END] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        //bindControls();


        setWebView();


    }
    @InjectView
    private WebView webView;

    private  void setWebView()
    {
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                view.loadUrl(urlNewString);
                BHLoading.show(getActivity());

                return true;
            }

            Runnable rRun;
            Handler handler = new Handler();;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE

                BHLoading.show(getActivity());
                rRun=new Runnable() {
                    @Override
                    public void run() {
                        showMessage(getResources().getString(R.string.mobile_report_error));
                        BHLoading.close();
                    }
                };
                int delay = Integer.parseInt(getActivity().getResources().getString(R.string.mobile_report_delay));
                handler.postDelayed(rRun, delay);

            }

            @Override
            public void onPageFinished(WebView view, String url) {

                handler.removeCallbacks(rRun);
                showMessage(getResources().getString(R.string.mobile_report_success));
                BHLoading.close();
            }


        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_zone) + "");

    }

    /*** [START] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
    /*
    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
            default:
                showLastView();
                break;
        }
    }
    */
    /*** [END] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/

    /*
    private void bindControls() {
        final String[] PAGE_TITLE = {"แผนที่", "ตามพื้นที่"};
        mapFragment = BHFragment.newInstance(ReportSummarySaleByAreaMapFragment.class);
        listFragment = BHFragment.newInstance(ReportSummarySaleByAreaListFragment.class);

        List<FortnightInfo> fortnightInfoList = new FortnightController().getAllFortnight(BHPreference.organizationCode());
        Collections.sort(fortnightInfoList, new Comparator<FortnightInfo>() {
            @Override
            public int compare(FortnightInfo lhs, FortnightInfo rhs) {
                return lhs.StartDate.before(rhs.StartDate) ? 1 : -1;
            }
        });

        BHSpinnerAdapter<FortnightInfo> fortnightAdapter = new BHSpinnerAdapter<FortnightInfo>(activity, fortnightInfoList) {
            @Override
            protected void setupView(TextView tv, FortnightInfo item) {
                tv.setText(String.format("%d/%s (%s - %s)", item.FortnightNumber, BHUtilities.dateFormat(item.StartDate, "yyyy"), BHUtilities.dateFormat(item.StartDate), BHUtilities.dateFormat(item.EndDate)));
            }
        };
        spnFortnight.setAdapter(fortnightAdapter);
        spnFortnight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FortnightInfo info = (FortnightInfo) parent.getItemAtPosition(position);
                fortnightID = info.FortnightID;
                int index = vpReportType.getCurrentItem();
                mapFragment.refreshData(index == 0, fortnightID);
                listFragment.refreshData(index == 1, fortnightID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vpReportType.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public CharSequence getPageTitle(int position) {
                // TODO Auto-generated method stub
                return PAGE_TITLE[position];
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return PAGE_TITLE.length;
            }

            @Override
            public Fragment getItem(int position) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        return mapFragment;

                    case 1:
                        return listFragment;

                    default:
                        return null;
                }
            }
        });

        tabReportType.setViewPager(vpReportType);
        tabReportType.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int current;

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                current = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
//                Log.i("VIEWPAGER", "STATE " + state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mapFragment.refreshData(current == 0, fortnightID);
                    listFragment.refreshData(current == 1, fortnightID);
                }
            }
        });

    }
    */


}
