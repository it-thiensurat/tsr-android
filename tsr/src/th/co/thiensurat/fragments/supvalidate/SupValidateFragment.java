package th.co.thiensurat.fragments.supvalidate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.adapter.SupValidateAdapter;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.CustomerStatusInfo;
import th.co.thiensurat.data.info.SupvalidateInfo;
import th.co.thiensurat.data.info.SupvalidateItemInfo;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;


public class SupValidateFragment extends BHFragment implements SupValidateAdapter.ItemClickListener {

    private SupvalidateInfo supvalidateInfo;
    private List<SupvalidateInfo> supvalidateInfos;

    private SupvalidateItemInfo supvalidateItemInfo;
    private List<SupvalidateItemInfo> supvalidateItemInfoList;

    private SupValidateAdapter supValidateAdapter;

    @InjectView
    private RecyclerView recyclerview;
    @InjectView
    private LinearLayout layout_msg;
    @InjectView
    private LinearLayout layout_list;
    @InjectView
    private TextView txtMsg;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_sup_validate;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        recyclerview.setLayoutManager(new LinearLayoutManager(activity));
        recyclerview.setHasFixedSize(true);

        loadData();
    }

    @Override
    protected int titleID() {
        return R.string.caption_sup_validate;
    }

    private void loadData() {
        try {
            BHLoading.show(activity);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GIS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.getImageValidate(BHPreference.employeeID());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson=new Gson();
                    try {
                        supvalidateInfos = new ArrayList<>();
//                        supvalidateItemInfoList = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
//                        Log.e("jsonObject", String.valueOf(jsonObject));
                        if (jsonObject.getString("status").equals("SUCCESS")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Log.e("jsonArray", String.valueOf(jsonArray));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                supvalidateInfo = new SupvalidateInfo();
                                supvalidateInfo.setContno(object.getString("CONTNO"));
                                supvalidateInfo.setEffdate(object.getString("EFFDATE"));
                                supvalidateInfo.setCustomername(object.getString("FirstName") + " " + object.getString("LastName"));
                                supvalidateInfo.setProductname(object.getString("ProductName"));
                                supvalidateInfo.setProductmodel(object.getString("ProductModel"));
                                supvalidateInfo.setProductserial(object.getString("ProductSerialNumber"));
                                supvalidateInfo.setStatusComment(object.getString("comment"));
                                supvalidateInfo.setStatusId(object.getInt("ApproveStatus"));

                                supvalidateItemInfoList = new ArrayList<>();
                                JSONArray array = object.getJSONArray("image");
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject obj = array.getJSONObject(j);
                                    supvalidateItemInfo = new SupvalidateItemInfo();
                                    supvalidateItemInfo.setImageUrl(obj.getString("ImageName"));
                                    supvalidateItemInfo.setImageType(obj.getString("ImageTypeCode"));
                                    supvalidateItemInfo.setImageComment(obj.getString("ProblemNameV3"));
                                    supvalidateItemInfoList.add(supvalidateItemInfo);
                                }

                                supvalidateInfo.setSupvalidateItemInfoList(supvalidateItemInfoList);
                                supvalidateInfos.add(supvalidateInfo);
                            }

                            setToAdapter(supvalidateInfos);
                        } else {
                            layout_list.setVisibility(View.GONE);
                            layout_msg.setVisibility(View.VISIBLE);
                            txtMsg.setText(jsonObject.getString("message"));
                            BHLoading.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data",e.getLocalizedMessage());
                        BHLoading.close();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data","2");
                    BHLoading.close();
                }
            });

        } catch (Exception e) {
            Log.e("data",e.getLocalizedMessage());
            BHLoading.close();
        }
    }

    private void setToAdapter(List<SupvalidateInfo> supvalidateInfos1) {
        supValidateAdapter = new SupValidateAdapter(activity, supvalidateInfos1);
        recyclerview.setAdapter(supValidateAdapter);
        supValidateAdapter.setClickListener(this);
        supValidateAdapter.notifyDataSetChanged();
        layout_list.setVisibility(View.VISIBLE);
        layout_msg.setVisibility(View.GONE);
        BHLoading.close();
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}