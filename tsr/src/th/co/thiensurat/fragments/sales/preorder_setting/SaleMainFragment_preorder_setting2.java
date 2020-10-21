package th.co.thiensurat.fragments.sales.preorder_setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;


import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;

import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseManager;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.SaleMoreDetailAddress;
import th.co.thiensurat.fragments.sales.SaleScanEmployeesFragment;
import th.co.thiensurat.fragments.sales.preorder.SaleFirstPaymentChoiceFragment_preorder;
import th.co.thiensurat.retrofit.api.Service;

import static java.lang.String.valueOf;
import static th.co.thiensurat.retrofit.api.client.BASE_URL;

public class SaleMainFragment_preorder_setting2 extends BHFragment {
    @InjectView
    private EditText edtSearch;
    @InjectView
    private Button btnSearch,btnRefresh;
    @InjectView
    private LinearLayout li_s;
    @InjectView
    private TextView textViewFinish;
    @InjectView
    public static ListView listViewFinish;
    public static List<ContractInfo> contractList = null;

    //public static SaleMainFinishedFragment_preorder_setting2.ContractAdapter contractAdapter;
    ContractAdapter contractAdapter;
    ProgressDialog dialog;
    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());
    public  boolean isContractDetails =  false;





    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back};
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_main_finished;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_sales;
    }

/*    @Override
    public String fragmentTag() {
        // TODO Auto-generated method stub
        //return SaleMoreDetailAddress.FRAGMENT_SALE_MOREDETAIL;
    }*/

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        BHPreference.setProcessType(ProcessType.Sale.toString());

        li_s.setVisibility(View.VISIBLE);

       // load_data_contact_online_preoder();

        GetContractStatusFinish();


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Search=edtSearch.getText().toString();
                GetContractStatusFinish2(Search);
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetContractStatusFinish();bindContractList();
            }
        });

    }






    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }



    private void GetContractStatusFinish() {
        // TODO Auto-generated method stub
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                try {
                    if(isCredit){
                        //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป (Comment ตัวนี้ไปใช้ getContractStatusFinishForCreditBySearch แทน)
                        //contractList = TSRController.getContractStatusFinishForCredit(BHPreference.organizationCode(), ContractStatusName.COMPLETED.toString());
                        contractList = TSRController.getContractStatusFinishForCreditBySearch_ContractInfo_preorder(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), "%%");
                        //Log.e("user",contractList.toString());
                        Log.e("1111","1111");
                    } else {
                        if (BHPreference.IsSaleForCRD()) {
                            if (isContractDetails) {
                                contractList = TSRController.getContractStatusFinishForCreditBySearch_ContractInfo_preorder(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), "%%");
                                Log.e("1111","2222");
                            } else {
                              //  contractList = TSRController.getContractStatusFinishForCRD_ContractInfo_preorder(BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), BHPreference.employeeID());
                                contractList = TSRController.getContractStatusFinish_ContractInfo_preorder_SETTING();

                                Log.e("1111","3333");
                            }
                        } else {
                        //    contractList = TSRController.getContractStatusFinish_ContractInfo_preorder_SETTING(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString());
                            contractList = TSRController.getContractStatusFinish_ContractInfo_preorder_SETTING();

                            Log.e("1111","4444");
                        }
                    }
                    //Log.e("TEST_SPEED","1111");
                    Log.e("contractList_SI",contractList.size()+"");
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            @Override
            protected void after() {
                // TODO Auto-generated method stub
                try {
                    if (contractList != null) {
                        //Log.e("TEST_SPEED","222222");
                        if (isConnectingToInternet()) {
                            String SOU = BHPreference.sourceSystem();

                            if(SOU.equals("Credit")){
                                dialog = ProgressDialog.show(activity, "", "Loading...", true);
                                //new PaymentController().deletePaymentByRefNo(BHPreference.RefNo());
                                //load_data();

                                Log.e("sssss","ssss");
                            } else {
                                Log.e("sssss","ssss2");
                                bindContractList();
                                listViewFinish.setVisibility(View.VISIBLE);
                            }

                        } else {
                            bindContractList();
                        }
                    } else {
                        listViewFinish.setVisibility(View.GONE);
                        textViewFinish.setVisibility(View.VISIBLE);
                        if(isCredit){
                            textViewFinish.setText("Finished List");
                        } else {
                            textViewFinish.setText("Finished Sales List");
                        }
                    }
                } catch (NullPointerException ex) {
                    listViewFinish.setVisibility(View.GONE);
                    textViewFinish.setVisibility(View.VISIBLE);
                    if(isCredit){
                        textViewFinish.setText("Finished List");
                    } else {
                        textViewFinish.setText("Finished Sales List");
                    }
                }
            }
        }.start();
    }



    private void GetContractStatusFinish2(String S) {
        // TODO Auto-generated method stub
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                try {
                    if(isCredit){
                        //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป (Comment ตัวนี้ไปใช้ getContractStatusFinishForCreditBySearch แทน)
                        //contractList = TSRController.getContractStatusFinishForCredit(BHPreference.organizationCode(), ContractStatusName.COMPLETED.toString());
                        contractList = TSRController.getContractStatusFinishForCreditBySearch_ContractInfo_preorder(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), "%%");
                        //Log.e("user",contractList.toString());
                        Log.e("1111","1111");
                    } else {
                        if (BHPreference.IsSaleForCRD()) {
                            if (isContractDetails) {
                                contractList = TSRController.getContractStatusFinishForCreditBySearch_ContractInfo_preorder(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), "%%");
                                Log.e("1111","2222");
                            } else {
                                //  contractList = TSRController.getContractStatusFinishForCRD_ContractInfo_preorder(BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), BHPreference.employeeID());
                                contractList = TSRController.getContractStatusFinish_ContractInfo_preorder_SETTING_S(S);

                                Log.e("1111","3333");
                            }
                        } else {
                            //    contractList = TSRController.getContractStatusFinish_ContractInfo_preorder_SETTING(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString());
                            contractList = TSRController.getContractStatusFinish_ContractInfo_preorder_SETTING_S(S);

                            Log.e("1111","4444");
                        }
                    }
                    //Log.e("TEST_SPEED","1111");
                    Log.e("contractList_SI",contractList.size()+"");
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            @Override
            protected void after() {
                // TODO Auto-generated method stub
                try {
                    if (contractList != null) {
                        //Log.e("TEST_SPEED","222222");
                        if (isConnectingToInternet()) {
                            String SOU = BHPreference.sourceSystem();

                            if(SOU.equals("Credit")){
                                dialog = ProgressDialog.show(activity, "", "Loading...", true);
                                //new PaymentController().deletePaymentByRefNo(BHPreference.RefNo());
                                //load_data();

                                Log.e("sssss","ssss");
                            } else {
                                Log.e("sssss","ssss2");
                                bindContractList();
                            }

                        } else {
                            bindContractList();
                        }
                    } else {
                        listViewFinish.setVisibility(View.GONE);
                        textViewFinish.setVisibility(View.VISIBLE);
                        if(isCredit){
                            textViewFinish.setText("Finished List");
                        } else {
                            textViewFinish.setText("Finished Sales List");
                        }
                    }
                } catch (NullPointerException ex) {
                    listViewFinish.setVisibility(View.GONE);
                    textViewFinish.setVisibility(View.VISIBLE);
                    if(isCredit){
                        textViewFinish.setText("Finished List");
                    } else {
                        textViewFinish.setText("Finished Sales List");
                    }
                }
            }
        }.start();
    }


    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }






    public static class ContractAdapter extends BHArrayAdapter<ContractInfo> {
        public ContractAdapter(Context context, int resource, List<ContractInfo> objects) {
            super(context, resource, objects);
        }

        public class ViewHolder {
            public TextView textViewContractnumber;
            public TextView textViewName;
            public TextView textViewStatus,textViewStatus2;
            public ImageView imageDelete;
            public ImageView imageNext;
        }

        int gg=0;
        @Override
        protected void onViewItem(final int position, View view, Object holder, final ContractInfo info) {
            // TODO Auto-generated method stub
            ContractAdapter.ViewHolder vh = (ContractAdapter.ViewHolder) holder;
            gg++;
            Log.e("position",position+","+gg);
            if(gg>148){
                Log.e("info.CONTNO",info.CONTNO);
                Log.e("info.svcontno",info.svcontno+"");
            }

            //vh.textViewContractnumber.setText("เลขที่สัญญา  :  "+ info.CONTNO);
            vh.textViewContractnumber.setText("เลขที่ใบจอง  :  "+ info.CONTNO);
            vh.textViewName.setText	         ("ชื่อลูกค้า        :  "+ BHUtilities.trim(info.CustomerFullName) +" "+ BHUtilities.trim(info.CompanyName));
            vh.textViewStatus.setText        ("สถานะ           :  "+ info.StatusName);
            String SOU=	BHPreference.sourceSystem();
            if(SOU.equals("Credit")){
                vh.textViewStatus2.setVisibility(View.VISIBLE);
                String DDD =info.svcontno+"";
                if(DDD.equals("null")){
                    vh.textViewStatus2.setText        ("ชำระเงิน           :  "+ "เครดิต");
                } else {
                    vh.textViewStatus2.setText        ("ชำระเงิน           :  "+ info.svcontno+"");
                }
            } else {
                vh.textViewStatus2.setVisibility(View.GONE);
            }

            vh.imageDelete.setVisibility(View.GONE);

            vh.imageNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }

    private void bindContractList() {
        // TODO Auto-generated method stub
        textViewFinish.setVisibility(View.GONE);
        contractAdapter = new ContractAdapter(activity, R.layout.list_main_status, contractList);
        listViewFinish.setAdapter(contractAdapter);

        listViewFinish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                try {
                   // BHPreference.setRefNo(contractList.get(position).RefNo);
                    //BHPreference.setRefNo(contractList.get(position).RefNo);

                    BHPreference.setProcessType(SaleFirstPaymentChoiceFragment_preorder.ProcessType.ViewCompletedContract.toString());
                    final String refNo = contractList.get(position).RefNo;

                    /*** [START] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/
                    new BackgroundProcess(activity) {
                        ContractInfo cont;
                        @Override
                        protected void calling() {
                            // TODO Auto-generated method stub
                            try {
                                cont = TSRController.getContractContractInfo_preorder(refNo);
                                if(cont == null){
                                    TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), refNo);
                                }

                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }
                        @Override
                        protected void after() {
                            // TODO Auto-generated method stub
                            if (cont != null) {


                                Log.e("aaa4",cont.CONTNO);



                                BHApplication.getInstance().getPrefManager().setPreferrence("getContractReferenceNo", contractList.get(position).CONTNO);
                                BHApplication.getInstance().getPrefManager().setPreferrence("code_name", cont.SaleTeamCode);
                                BHApplication.getInstance().getPrefManager().setPreferrence("name_recommend",cont.SaleEmployeeCode);
                                BHApplication.getInstance().getPrefManager().setPreferrence("name_CustomerID",cont.RefNo);

                                showNextView(new SaleScanEmployeesFragment_preorder_setting());


                            }
                        }
                    }.start();
                    /*** [END] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/

                } catch (NullPointerException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }




}
