package th.co.thiensurat.fragments.report;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;

public class ReportSummaryCreditSaleAuditFragment extends BHFragment {

    @InjectView private WebView webView;

    @Override
    protected int titleID() {
        return R.string.menu_report_credit_saleaudit;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_credit_saleaudit;
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
        setWebView();
    }

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
        webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_credit_saleaudit) + "");

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


}
