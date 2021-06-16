package th.co.thiensurat.fragments.sales.sales_quotation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.fragments.sales.sales_quotation.adapter.ProductSelectedAdapter;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;
import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class QuotationDetail extends BHFragment {

    public static class Data extends BHParcelable {
        public JSONObject objectCustomer;
        public List<get_product_sale_q> objectProduct;
        public String discount = "0";
        public String promotionText = "";
        public String projectName = "";
    }

    private Data data;

    @InjectView
    private ViewTitle vwTitle;

    @InjectView
    private RecyclerView recyclerview;

    @InjectView
    private TextView discountPrice, netPrice, vatPrice, sumVatPrice, grandPrice;

    @Override
    protected int fragmentID() {
        return R.layout.quotation_detail;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_save};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        vwTitle.setText("รายละเอียดการเสนอราคา");
        data = getData();

        Log.e("detail(project)", data.projectName);
        Log.e("detail(discount)", data.discount);
        Log.e("detail(customer)", String.valueOf(data.objectCustomer));
        Log.e("detail(product)", String.valueOf(data.objectProduct));
        Log.e("detail(promotion)", data.promotionText);

        setupUI();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_save:
                onSaveData();
                break;
            default:
                break;
        }
    }

    private void setupUI() {
        discountPrice.setText(data.discount);

        recyclerview.setLayoutManager(new LinearLayoutManager(activity));
        recyclerview.setHasFixedSize(true);

        ProductSelectedAdapter productSelectedAdapter = new ProductSelectedAdapter(data.objectProduct);
        recyclerview.setAdapter(productSelectedAdapter);
        productSelectedAdapter.notifyDataSetChanged();

        summary();
    }

    private int qty = 0;
    private double vat = 0;
    private double netValue = 0;
    private double noVatValue = 0;
    private double grandValue = 0;
    private void summary() {
        for (get_product_sale_q productSaleQ : data.objectProduct) {
            qty = Integer.parseInt(productSaleQ.getProduct_qty());
            netValue += (qty * Double.parseDouble(productSaleQ.getProduct_price()));
        }

        netValue = netValue - Integer.parseInt(data.discount);
        vat = (netValue * 7) / 100;
        grandValue = vat + netValue;

        netPrice.setText(BHUtilities.numericFormat(netValue) + " บาท");
        vatPrice.setText(BHUtilities.numericFormat(vat) + " บาท");
        sumVatPrice.setText(BHUtilities.numericFormat(netValue) + " บาท");
        grandPrice.setText(BHUtilities.numericFormat(grandValue) + " บาท");
    }

    private void onSaveData() {
        try {
			Call call = null;
			Service request = null;
			Retrofit retrofit = null;
			BHLoading.show(activity);

			JsonArray datas = new JsonArray();
			for (get_product_sale_q prodcut : data.objectProduct) {
				JsonObject object = new JsonObject();
				object.addProperty("product_id",prodcut.getProduct_id());
				object.addProperty("product_qty", prodcut.getProduct_qty());
                object.addProperty("product_price", prodcut.getProduct_price());
				datas.add(object);
			}

			List<MultipartBody.Part> parts = new ArrayList<>();
			parts.add(MultipartBody.Part.createFormData("product", String.valueOf(datas)));
			RequestBody customBody = RequestBody.create(MediaType.parse("text/plain"), data.objectCustomer.getString("APCUS_ID"));
			RequestBody empBody = RequestBody.create(MediaType.parse("text/plain"), BHPreference.employeeID());
            RequestBody projectBody = RequestBody.create(MediaType.parse("text/plain"), data.projectName);
            RequestBody promotionBody = RequestBody.create(MediaType.parse("text/plain"), data.promotionText);
            RequestBody discountBody = RequestBody.create(MediaType.parse("text/plain"), data.discount);
            RequestBody totalBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(netValue));
            RequestBody grandtotalBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(grandValue));


			if (BHGeneral.SERVICE_MODE.toString() == "UAT") {
				retrofit = new Retrofit.Builder()
						.baseUrl(GIS_BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				request = retrofit.create(Service.class);
				call = request.addProductToQuotationUAT(customBody, empBody, projectBody, promotionBody, discountBody, totalBody, grandtotalBody, parts);
			}

			call.enqueue(new Callback() {
				@Override
				public void onResponse(Call call, retrofit2.Response response) {
					Gson gson=new Gson();
					try {
						Log.e("Response", String.valueOf(response));
						Log.e("JSON body", String.valueOf(response.body()));
						JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
						Log.e("Add quotation", String.valueOf(jsonObject));
						BHLoading.close();
						if (jsonObject.getString("status").equals("SUCCESS")) {
                            String title = "ใบเสนอราคา";
                            String message = jsonObject.getString("message");
                            String quotationId = jsonObject.getString("data");
                            android.app.AlertDialog.Builder setupAlert = new AlertDialog.Builder(activity)
                                    .setTitle("ใบเสนอราคา")
                                    .setCancelable(false)
                                    .setMessage(message)
                                    .setNegativeButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.dismiss();
                                            QuotationViewFragment.Data vData = new QuotationViewFragment.Data();
                                            vData.viewUser = "sale";
                                            vData.quotationnId = quotationId;
                                            QuotationViewFragment quotationViewFragment = BHFragment.newInstance(QuotationViewFragment.class, vData);
                                            showNextView(quotationViewFragment);
                                        }
                                    });
                            setupAlert.show();
                        }
					} catch (JSONException e) {
						e.printStackTrace();
						Log.e("data",e.getLocalizedMessage());
						BHLoading.close();
					}
				}

				@Override
				public void onFailure(Call call, Throwable t) {
					Log.e("onFailure quotation:",t.getLocalizedMessage());
					BHLoading.close();
				}
			});
		} catch (Exception e) {
			Log.e("Exception quotation",e.getLocalizedMessage());
			BHLoading.close();
		}
    }
}
