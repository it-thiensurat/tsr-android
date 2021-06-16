package th.co.thiensurat.fragments.sales.sales_quotation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.fragments.sales.sales_quotation.models.CustomerAPModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.QuotationWaitModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;
import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class QuotationSupFragment extends BHFragment {

    @InjectView
    private ViewTitle vwTitle;

    @InjectView
    private TextView textViewUnfinished;
    @InjectView
    private ListView listViewUnfinish;

    private List<CustomerAPModel> customerList;
    private List<get_product_sale_q> productSaleQsList;
    private List<QuotationWaitModel> quotationWaitModelList;

    @Override
    protected int titleID() {
        return R.string.title_sales_quotation;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_sale_main_unfinished;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
//        vwTitle.setText("รายการขออนุมัติเสนอราคา");
        customerList = new ArrayList<>();
        productSaleQsList = new ArrayList<>();
        quotationWaitModelList = new ArrayList<>();
        getQuotation();
    }

    private void getQuotation() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GIS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call=null;
            if (BHGeneral.SERVICE_MODE.toString() == "UAT") {
                call = request.getQuotationForApproveUAT();
            }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        Log.e("JSON Object", String.valueOf(jsonObject));
                        if (jsonObject.getString("status").equals("SUCCESS")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            CustomerAPModel customerAPModel;
                            QuotationWaitModel quotationWaitModel;
                            get_product_sale_q productSaleQ;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                quotationWaitModel = new QuotationWaitModel();
                                quotationWaitModel.setQuotationId(object.getString("APQ_ID"));
                                quotationWaitModel.setQuotationDate(object.getString("APQ_DATE"));
                                quotationWaitModel.setQuotationDiscount(object.getString("APQ_DISCOUNT"));
                                quotationWaitModel.setQuotationProjectName(object.getString("APQ_PROJECTNAME"));
                                quotationWaitModel.setQuotationStatus(object.getInt("APQ_STATUS"));
                                quotationWaitModel.setQuotationStatusText(object.getString("APQ_STATUS_TEXT"));
                                quotationWaitModel.setQuotationComment(object.getString("APQ_COMMENT"));

                                JSONArray cusArray = object.getJSONArray("CUSTOMER_DETAIL");
                                Log.e("CUSTOMER_DETAIL", String.valueOf(cusArray));
                                for (int j = 0; j < cusArray.length(); j++) {
                                    JSONObject objectCus = cusArray.getJSONObject(j);
                                    customerAPModel = new CustomerAPModel();
                                    customerAPModel.setCustomerId(objectCus.getInt("APCUS_ID"));
                                    customerAPModel.setCustomerName(objectCus.getString("APCUS_NAME"));
                                    customerAPModel.setCustomerTax(objectCus.getString("APCUS_IDCARD"));
                                    customerAPModel.setCustomerType(objectCus.getInt("APCUS_TYPE"));
                                    customerAPModel.setCustomerAddr(objectCus.getString("APCUS_ADDR"));
                                    customerAPModel.setCustmerMoo(objectCus.getInt("APCUS_MOO"));
                                    customerAPModel.setCustomerSoi(objectCus.getString("APCUS_SOI"));
                                    customerAPModel.setCustomerRoad(objectCus.getString("APCUS_ROAD"));
                                    customerAPModel.setCustomerProvince(objectCus.getInt("APCUS_PROVINCE_ID"));
                                    customerAPModel.setCustomerDistrict(objectCus.getInt("APCUS_DISTRICT_ID"));
                                    customerAPModel.setCustomerSubdistrict(objectCus.getInt("APCUS_SUBDISTRICT_ID"));
                                    customerAPModel.setCustomerZipcode(objectCus.getString("APCUS_ZIPCODE"));
                                    customerAPModel.setCustomerPhone(objectCus.getString("APCUS_PHONE"));
                                    customerAPModel.setCustomerEmail(objectCus.getString("APCUS_EMAIL"));
                                    customerAPModel.setCustomerContactName(objectCus.getString("APCUS_CONTACT_NAME"));
                                    customerAPModel.setCustomerContactPhone(objectCus.getString("APCUS_CONTACT_PHONE"));
                                    customerAPModel.setCustomerContactEmail(objectCus.getString("APCUS_CONTACT_EMAIL"));
                                    customerList.add(customerAPModel);
                                }

                                JSONArray proArray = object.getJSONArray("PRODUCT_DETAIL");
                                for (int k = 0; k < proArray.length(); k++) {
                                    JSONObject objectPro = proArray.getJSONObject(k);
                                    productSaleQ = new get_product_sale_q();
                                    productSaleQ.setProduct_id(objectPro.getString("APQD_PROD_ID"));
                                    productSaleQ.setProduct_qty(objectPro.getString("APQD_PROD_QTY"));
                                    productSaleQ.setProduct_price(objectPro.getString("APQD_UNIT_PRICE"));

                                    productSaleQsList.add(productSaleQ);
                                }

                                quotationWaitModel.setProductList(productSaleQsList);
                                quotationWaitModel.setCustomerAPModelList(customerList);
                                quotationWaitModelList.add(quotationWaitModel);
                            }

                            setUI();
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

    private void setUI() {
        textViewUnfinished.setVisibility(View.INVISIBLE);
        BHArrayAdapter<QuotationWaitModel> customerAPModelBHArrayAdapter = new BHArrayAdapter<QuotationWaitModel>(activity, R.layout.list_main_status, quotationWaitModelList) {
            @Override
            protected void onViewItem(int position, View view, Object holder, QuotationWaitModel info) {
                ViewHolder vh = (ViewHolder) holder;
                vh.textViewContractnumber.setText("เลขที่ใบเสนอราคา  :  " + BHUtilities.trim(info.getQuotationId()));
                vh.textViewName.setText          ("ชื่อลูกค้า          :  "+ BHUtilities.trim(info.getCustomerAPModelList().get(position).getCustomerName()));
                vh.textViewStatus.setText        ("สถานะ           :  " + BHUtilities.trim(info.getQuotationStatusText()));
                vh.imageDelete.setVisibility(View.GONE);
            }

            class ViewHolder {
                public TextView textViewContractnumber;
                public TextView textViewName;
                public TextView textViewStatus;
                public ImageView imageDelete, imageNext;
                public LinearLayout aaa;
            }
        };

        listViewUnfinish.setAdapter(customerAPModelBHArrayAdapter);
        listViewUnfinish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                QuotationWaitModel quotationWaitModel = quotationWaitModelList.get(position);
                QuotationViewFragment.Data vData = new QuotationViewFragment.Data();
                vData.viewUser = "sup";
                vData.quotationnId = quotationWaitModel.getQuotationId();
                QuotationViewFragment quotationViewFragment = BHFragment.newInstance(QuotationViewFragment.class, vData);
                showNextView(quotationViewFragment);
            }
        });
    }
}
