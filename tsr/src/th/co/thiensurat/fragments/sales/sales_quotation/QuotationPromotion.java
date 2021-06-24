package th.co.thiensurat.fragments.sales.sales_quotation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.json.JSONArray;
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
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.info.DistrictInfo;
import th.co.thiensurat.fragments.sales.SaleUnfinishedFragment;
import th.co.thiensurat.fragments.sales.sales_quotation.models.PromotionModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.QuotationWaitModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;
import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class QuotationPromotion extends BHFragment {

    public static class Data extends BHParcelable {
        public String actionType = "";
        public String projectName = "";
        public JSONObject objectCustomer;
        public List<get_product_sale_q> objectProduct;
        public List<QuotationWaitModel> quotationWaitModelList;
    }

    private Data data;

    private List<PromotionModel> promotionModelList = new ArrayList<>();

    @InjectView
    private ViewTitle vwTitle;

    @InjectView
    private EditText editTextDiscount, editTextPromotion;

    @InjectView
    private Spinner spinnerPromotion;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_sales_quotation;
    }

    @Override
    protected int fragmentID() {
        return R.layout.quotation_promotion;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_detail_quotation};
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_detail_quotation:
                QuotationDetail.Data qdata = new QuotationDetail.Data();
                qdata.discount = editTextDiscount.getText().toString().isEmpty() ? "0" : editTextDiscount.getText().toString();
                qdata.promotionText = editTextPromotion.getText().toString();
                qdata.projectName = data.projectName;
                qdata.objectProduct  = data.objectProduct;
                qdata.objectCustomer = data.objectCustomer;
                QuotationDetail qdetail = BHFragment.newInstance(QuotationDetail.class, qdata);
                showNextView(qdetail);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        vwTitle.setText("ส่วนลดหรือโปรโมชั่น");
        getPromotionText();

        data = getData();

        Log.e("promotion(project)", data.projectName);
        Log.e("promotion(customer)", String.valueOf(data.objectCustomer));
        Log.e("promotion(product)", String.valueOf(data.objectProduct));

        spinnerPromotion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                PromotionModel promotionModel = promotionModelList.get(position);
                editTextPromotion.setText(promotionModel.getPromotionDetail());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (data.actionType.equals("edit")) {
            try {
                editTextDiscount.setText(data.quotationWaitModelList.get(0).getQuotationDiscount());
            } catch (Exception e) {
                Log.e("Exception", e.getLocalizedMessage());
            }
        }
    }

    private void getPromotionText() {
        try {
			Call call = null;
			Service request = null;
			Retrofit retrofit = null;
			BHLoading.show(activity);

			if (BHGeneral.SERVICE_MODE.toString() == "UAT") {
				retrofit = new Retrofit.Builder()
						.baseUrl(GIS_BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				request = retrofit.create(Service.class);
				call = request.getPromotionUAT();
			}

			call.enqueue(new Callback() {
				@Override
				public void onResponse(Call call, retrofit2.Response response) {
					Gson gson=new Gson();
					try {
						Log.e("JSON body", String.valueOf(response.body()));
						JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        bindPromotion(jsonObject.getJSONArray("data"));
						Log.e("Get promotion", String.valueOf(jsonObject));
						BHLoading.close();
					} catch (JSONException e) {
						e.printStackTrace();
						Log.e("data",e.getLocalizedMessage());
						BHLoading.close();
					}
				}

				@Override
				public void onFailure(Call call, Throwable t) {
					Log.e("onFailure onSave:",t.getLocalizedMessage());
					BHLoading.close();
				}
			});
		} catch (Exception e) {
			Log.e("Exception onSave",e.getLocalizedMessage());
			BHLoading.close();
		}
    }

    private void bindPromotion(JSONArray data) {
        Log.e("bindPromotion", String.valueOf(data));
        List<String> promotionList = new ArrayList<String>();
        PromotionModel promotionModel;
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject object = data.getJSONObject(i);
                promotionList.add(object.getString("PromotionName"));
                promotionModel = new PromotionModel();
                promotionModel.setPromotionId(object.getInt("ID"));
                promotionModel.setPromotionName(object.getString("PromotionName"));
                promotionModel.setPromotionDetail(object.getString("PromotionDetail"));
                promotionModelList.add(promotionModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        BHSpinnerAdapter<String> arrayprovince = new BHSpinnerAdapter<String>(activity, promotionList);
        spinnerPromotion.setAdapter(arrayprovince);
    }
}
