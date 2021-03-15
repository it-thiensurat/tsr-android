package th.co.thiensurat.fragments.customerstatus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.activities.SignatureActivity;
import th.co.thiensurat.adapter.CustomerStatusAdapter;
import th.co.thiensurat.adapter.EmployeeAdapter;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.CustomerStatusInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.data.controller.DocumentController.getAlbumStorageDir;
import static th.co.thiensurat.data.controller.DocumentController.getResizedBitmap;
import static th.co.thiensurat.retrofit.api.client.BASE_URL;
import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class CustomerStatusFragment extends BHFragment implements CustomerStatusAdapter.ItemClickListener {

    private ProgressDialog dialog;
//    protected static MainActivity activity;
    private CustomerStatusAdapter customerStatusAdapter;

    @InjectView
    private EditText editSearch;
    @InjectView
    private Button buttonSearch;
    @InjectView
    private RecyclerView recyclerview;
    @InjectView
    LinearLayout layout_list;
    @InjectView
    LinearLayout layout_msg;
    @InjectView
    TextView txtMsg;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.caption_customer_status;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_customer_status;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog = ProgressDialog.show(getActivity(), "Please wait...", "กำลังโหลดข้อมูลลูกค้า", true, false);
                BHLoading.show(activity);
                onSearch(editSearch.getText().toString());
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private String message = "";
    private JSONArray address;
    public void onSearch(String search) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GIS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.getCustomerStatus(search);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson=new Gson();
                    try {
                        List<AddressInfo> addressInfoList;
                        List<CustomerStatusInfo> customerStatusInfoList;
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        String status = jsonObject.getString("status");
                        message = jsonObject.getString("message");
                        customerStatusInfoList = new ArrayList<>();
                        CustomerStatusInfo customerStatusInfo;
                        addressInfoList = new ArrayList<>();
                        AddressInfo addressInfo;
                        if (status.equals("SUCCESS")) {
                            JSONArray array = jsonObject.getJSONArray("data");
                            layout_msg.setVisibility(View.GONE);
                            layout_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                addressInfo = new AddressInfo();
                                customerStatusInfo = new CustomerStatusInfo();
                                customerStatusInfo.setRefno(object.getString("Refno"));
                                customerStatusInfo.setCONTNO(object.getString("CONTNO"));
                                customerStatusInfo.setIDCard(object.getString("IDCard"));
                                customerStatusInfo.setPrefixName(object.getString("PrefixName"));
                                customerStatusInfo.setCustomerName(object.getString("CustomerName"));
                                customerStatusInfo.setPayLastStatus(object.getString("PayLastStatus"));
                                customerStatusInfo.setCustomerStatus(object.getString("CustomerStatus"));
                                customerStatusInfo.setAccountStatus(object.getString("AccountStatus"));
                                customerStatusInfo.setPayType(object.getString("PayType"));
                                customerStatusInfo.setAllPeriods(object.getString("AllPeriods"));
                                customerStatusInfo.setPayLastPeriod(object.getString("PayLastPeriod"));
                                customerStatusInfo.setTotalPrice(object.getString("TotalPrice"));
                                customerStatusInfo.setProductName(object.getString("ProductName"));
                                customerStatusInfo.setProductModel(object.getString("ProductModel"));
                                customerStatusInfo.setSaleCode(object.getString("SaleCode"));
                                customerStatusInfo.setEffDate(object.getString("EffDate"));
                                customerStatusInfo.setAgingCumulative(object.getString("AgingCumulative"));
                                customerStatusInfo.setAgingContinuous(object.getString("AgingContinuous"));
                                customerStatusInfo.setAgingCumulativeDetail(object.getString("AgingCumulativeDetail"));
                                customerStatusInfo.setStDate(object.getString("StDate"));
                                customerStatusInfo.setCustomerAddress(object.getJSONArray("Address"));
                                customerStatusInfoList.add(customerStatusInfo);
                            }
                            setItemToRecyclerView(customerStatusInfoList);
                        } else {
//                            dialog.dismiss();
                            BHLoading.close();
                            layout_list.setVisibility(View.GONE);
                            layout_msg.setVisibility(View.VISIBLE);
                            txtMsg.setText(message.toString());
                        }
//                        Log.e("Response", String.valueOf(response.body()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data",e.getLocalizedMessage());
                        dialog.dismiss();
                        txtMsg.setText(message.toString());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data","2");
                    dialog.dismiss();
                    txtMsg.setText(message.toString());
                }
            });

        } catch (Exception e) {
            Log.e("data",e.getLocalizedMessage());
            dialog.dismiss();
            txtMsg.setText(message.toString());
        }
    }

    private void setItemToRecyclerView(List<CustomerStatusInfo> customerStatusInfoList) {
        recyclerview.setLayoutManager(new LinearLayoutManager(activity));
        recyclerview.setHasFixedSize(true);
        customerStatusAdapter = new CustomerStatusAdapter(customerStatusInfoList, address);
        recyclerview.setAdapter(customerStatusAdapter);
        customerStatusAdapter.setClickListener(this);
        customerStatusAdapter.notifyDataSetChanged();
        editSearch.setText("");
        BHLoading.close();
    }
}