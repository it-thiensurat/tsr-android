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
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.fragments.sales.sales_quotation.models.CustomerAPModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.QuotationWaitModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class QuotaionWaitFragment extends BHPagerFragment {

    @InjectView
    private TextView textViewUnfinished;
    @InjectView
    private ListView listViewUnfinish;

    private List<CustomerAPModel> customerList;
    private List<get_product_sale_q> productSaleQsList;
    private List<QuotationWaitModel> quotationWaitModelList;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_sales_quotation;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_sale_main_unfinished;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        customerList = new ArrayList<>();
        productSaleQsList = new ArrayList<>();
        quotationWaitModelList = new ArrayList<>();
        getQuotationWaitList();
    }

    private void getQuotationWaitList() {
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
                call = request.getQuotationWaitUAT(BHPreference.employeeID());
            }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson=new Gson();
                    try {
                        Log.e("Response", String.valueOf(response));
                        Log.e("JSON body", String.valueOf(response.body()));
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
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
                        Log.e("Get quotation", String.valueOf(jsonObject));
                        BHLoading.close();
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

    private void setUI() {
        textViewUnfinished.setVisibility(View.INVISIBLE);
        BHArrayAdapter<QuotationWaitModel> customerAPModelBHArrayAdapter = new BHArrayAdapter<QuotationWaitModel>(activity, R.layout.list_main_status, quotationWaitModelList) {
            @Override
            protected void onViewItem(int position, View view, Object holder, QuotationWaitModel info) {
                ViewHolder vh = (ViewHolder) holder;
                vh.textViewContractnumber.setText("เลขที่ใบเสนอราคา  :  " + BHUtilities.trim(info.getQuotationId()));
                vh.textViewName.setText          ("ชื่อลูกค้า          :  "+ BHUtilities.trim(info.getCustomerAPModelList().get(position).getCustomerName()));
                vh.textViewStatus.setText        ("สถานะ           :  " + BHUtilities.trim(info.getQuotationStatusText()));
                vh.textViewStatus2.setVisibility(View.VISIBLE);
                vh.textViewStatus2.setText       ("Comment         :  " + BHUtilities.trim(info.getQuotationComment()));
                vh.imageDelete.setVisibility(View.GONE);
                if (info.getQuotationStatus() == 3) {
                    vh.textViewContractnumber.setTextColor(activity.getResources().getColor(R.color.bg_body_white));
                    vh.textViewName.setTextColor(activity.getResources().getColor(R.color.bg_body_white));
                    vh.textViewStatus.setTextColor(activity.getResources().getColor(R.color.bg_body_white));
                    vh.textViewStatus2.setTextColor(activity.getResources().getColor(R.color.bg_body_white));
                    vh.aaa.setBackgroundColor(activity.getResources().getColor(R.color.bg_alert));
                } else {
                    vh.textViewContractnumber.setTextColor(activity.getResources().getColor(R.color.status_bar_black));
                    vh.textViewName.setTextColor(activity.getResources().getColor(R.color.status_bar_black));
                    vh.textViewStatus.setTextColor(activity.getResources().getColor(R.color.status_bar_black));
                    vh.textViewStatus2.setTextColor(activity.getResources().getColor(R.color.status_bar_black));
                    vh.aaa.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
                }
            }

            class ViewHolder {
                public TextView textViewContractnumber;
                public TextView textViewName;
                public TextView textViewStatus;
                public ImageView imageDelete, imageNext;
                public LinearLayout aaa;
                public TextView textViewStatus2;
            }
        };

        listViewUnfinish.setAdapter(customerAPModelBHArrayAdapter);
        listViewUnfinish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                QuotationWaitModel quotationWaitModel = quotationWaitModelList.get(position);
                Log.e("edit model", String.valueOf(quotationWaitModel));
                if (quotationWaitModel.getQuotationStatus() == 3) {
                    List<CustomerAPModel> customerAPModelList = new ArrayList<>();
                    customerAPModelList = quotationWaitModel.getCustomerAPModelList();
                    New2SaleCustomerAddressCardFragment_sale_Q.Data data = new New2SaleCustomerAddressCardFragment_sale_Q.Data();
                    data.actionType = "edit";
                    data.customerAPModelList = customerAPModelList;
                    data.quotationWaitModelList.add(quotationWaitModel);
                    New2SaleCustomerAddressCardFragment_sale_Q unfinish = BHFragment.newInstance(New2SaleCustomerAddressCardFragment_sale_Q.class, data);
                    showNextView(unfinish);
                }
            }
        });
    }
}
