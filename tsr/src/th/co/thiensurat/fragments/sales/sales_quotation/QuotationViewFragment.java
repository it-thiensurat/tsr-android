package th.co.thiensurat.fragments.sales.sales_quotation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.fragments.sales.EditContractsMainFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class QuotationViewFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String viewUser;
        public String actionType;
        public String quotationnId;
    }

    private Data data;
    private String POSITIONID = "";

    @InjectView
    private WebView webView;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_quotation_view;
    }

    @Override
    protected int[] processButtons() {
//        return new int[]{R.string.button_end};
        data = getData();
        if (data.viewUser.equals("sup")) {
            return new int[]{R.string.button_reject, R.string.button_edit, R.string.button_approve};
        } else {
            return new int[]{R.string.button_end};
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();
        getEmpPosition();
        BHLoading.show(activity);
        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("https://toss.thiensurat.co.th/ALPINES/ReportPonsabai.aspx?QuotationId=" + data.quotationnId);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                BHLoading.show(activity);
                view.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                BHLoading.show(activity);
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                BHLoading.close();
            }
        });
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_end:
                if (POSITIONID.equals("0006")) {
                    activity.showView(new QuotationSupFragment());
                } else {
                    activity.showView(new SaleMainFragment_sales_quotation());
                }
                break;
            case R.string.button_approve:
                break;
            default:
                break;
        }
    }

    private void getEmpPosition() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GIS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call=null;
            if(BHGeneral.SERVICE_MODE.toString().equals("UAT")){
                call = request.getPositionIdUAT(BHPreference.employeeID());
            }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        Log.e("JSON Object", String.valueOf(jsonObject));
                        if (jsonObject.getString("status").equals("SUCCESS")) {
                            POSITIONID = jsonObject.getString("data");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONException", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("onFailure", t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            Log.e("Exception", e.getLocalizedMessage());
        }
    }

    private EditText input;
    private void Alert(String type) {
        String msg = "";
        if (type.equals("approve")) {
            msg = "คุณต้องการอนุมัติใบเสนอราคาเลขที่ " + data.quotationnId + " \nใช่หรือไม่?";
        } else if (type.equals("edit")) {
            msg = "";
        } else if (type.equals("reject")) {
            msg = "";
        }

        input = new EditText(activity);
        AlertDialog.Builder setupAlert = new AlertDialog.Builder(activity);
        setupAlert.setTitle("คำเตือน");
        setupAlert.setCancelable(false);
        setupAlert.setMessage(msg);
        setupAlert.setNegativeButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                    }
                });
        setupAlert.show();
    }
}