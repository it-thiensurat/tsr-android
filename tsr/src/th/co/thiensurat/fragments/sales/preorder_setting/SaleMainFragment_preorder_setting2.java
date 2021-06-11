package th.co.thiensurat.fragments.sales.preorder_setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;


import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;

import th.co.thiensurat.activities.SurveyActivity_preorder;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.PrinterController;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseManager;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.credit.credit.CreditListFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.SaleMoreDetailAddress;
import th.co.thiensurat.fragments.sales.SaleScanEmployeesFragment;
import th.co.thiensurat.fragments.sales.preorder.SaleFirstPaymentChoiceFragment_preorder;
import th.co.thiensurat.fragments.sales.preorder.models.Get_data_api3;
import th.co.thiensurat.fragments.share.BarcodeScanFragment;
import th.co.thiensurat.retrofit.api.Service;

import static java.lang.String.valueOf;
import static th.co.thiensurat.business.controller.TSRController.getAddress;
import static th.co.thiensurat.business.controller.TSRController.getContractByRefNoForSendDocuments;
import static th.co.thiensurat.retrofit.api.client.BASE_URL;
import static th.co.thiensurat.retrofit.api.client.DRINKO_BASE_UAT_URL;
import static th.co.thiensurat.retrofit.api.client.DRINKO_BASE_URL;
import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class SaleMainFragment_preorder_setting2 extends BHFragment {
    @InjectView
    private EditText edtSearch;
    @InjectView
    private Button btnSearch,btnRefresh;
    @InjectView
    private LinearLayout li_s;
    @InjectView
    private TextView textViewFinish,textcancle;
    @InjectView
    public static ListView listViewFinish;
    public static List<ContractInfo> contractList = null;

    //public static SaleMainFinishedFragment_preorder_setting2.ContractAdapter contractAdapter;
    ContractAdapter contractAdapter;
    ProgressDialog dialog;
    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());
    public  boolean isContractDetails =  false;

    SaleMainFragment_preorder_setting saleMainFragment_preorder_setting;

   public static JsonArrayRequest jsonArrayRequest;
    public static RequestQueue requestQueue;

    private List<SalePaymentPeriodInfo> paymentPeriodOutput;
    private ContractInfo contract = null;
    private AddressInfo addressIDCard = null;
    private AddressInfo addressInstall = null;

    private static final int REQUEST_QR_SCAN = 2468;
    private static final int REQUEST_QR_SCAN2 = 2469;

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
        //updateAssignForPostpone_test();
        BHPreference.setProcessType(ProcessType.Sale.toString());

        li_s.setVisibility(View.VISIBLE);

       // load_data_contact_online_preoder();

        GetContractStatusFinish();
//        getDivisionId(BHPreference.employeeID());

        Log.e("Empid", BHPreference.employeeID());
        Log.e("Departid", BHPreference.departmentCode());

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

    EditText input = null;
    EditText input2 = null;
    String serailCheckScan = "";
    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public class ContractAdapter extends BHArrayAdapter<ContractInfo> {
        public ContractAdapter(Context context, int resource, List<ContractInfo> objects) {
            super(context, resource, objects);
        }

        public class ViewHolder {
            public TextView textViewContractnumber;
            public TextView textViewName;
            public TextView textViewStatus,textViewStatus2,textcancle;
            public ImageView imageDelete;
            public ImageView imageNext;
        }

        int gg=0;
        @Override
        protected void onViewItem(final int position, View view, Object holder, final ContractInfo info) {
            // TODO Auto-generated method stub
            ContractAdapter.ViewHolder vh = (ContractAdapter.ViewHolder) holder;
            gg++;
           // Log.e("position",position+","+gg);
            if(gg>148){
                Log.e("info.CONTNO",info.CONTNO);
                Log.e("info.svcontno",info.svcontno+"");
            }

            vh.textcancle.setVisibility(View.VISIBLE);
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

             // Log.e("ffg",contractList.get(position).SPECCODE+"");
            try {
                if(info.SPECCODE.equals("1")){
                    vh.textcancle.setText("พิมพ์ใบยกเลิกจอง");
                    vh.textcancle.setBackgroundColor(0xff0089d3);

                } else {
                    vh.textcancle.setText("ยกเลิกใบจอง");
                    vh.textcancle.setBackgroundColor(0xffFF0000);
                }

            } catch (Exception ex){
                vh.textcancle.setText("ยกเลิกใบจอง");
                vh.textcancle.setBackgroundColor(0xffFF0000);
            }

            Log.e("info.SPECCODE",info.SPECCODE+"");
            String fs=info.SPECCODE+"";
            vh.textcancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("info", String.valueOf(info));
                    getDivisionId(BHPreference.employeeID());
                    if(fs.equals("1")){
                        AlertDialog.Builder setupAlert;
                        setupAlert = new AlertDialog.Builder(activity)
                                .setTitle("พิมพ์ใบยกเลิกจอง")
                                .setMessage("ต้องการ พิมพ์ใบยกเลิกจอง \nหมายเลข "  + info.CONTNO  +"\n"+info.CustomerFullName+ "     ใช่หรือไม่")
                                .setCancelable(false);
                        setupAlert = setupAlert.setPositiveButton("ใช่ ฉันต้องการพิมพ์ใบยกเลิกจองนี้", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                try {
//                                    paymentPeriodOutput = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(info.RefNo);
                                    contract = getContractByRefNoForSendDocuments(BHPreference.organizationCode(), info.RefNo);
                                    addressIDCard = getAddress(info.RefNo, AddressInfo.AddressType.AddressIDCard);
                                    addressInstall = getAddress(info.RefNo, AddressInfo.AddressType.AddressInstall);

                                    Log.e("Payment output", String.valueOf(paymentPeriodOutput));
                                    for (SalePaymentPeriodInfo item : paymentPeriodOutput) {
                                        contract.NextPaymentAmount = item.NetAmount;
                                    }
                                } catch (Exception ex){
                                    Log.e("Print preorder slip", ex.getLocalizedMessage());
                                }
                                printDocument();
                            }
                        }).setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        setupAlert.show();
                    } else {
                        /**
                         *
                         * Edit by Teerayut Klinsanga
                         *
                         * Created date 27/04/2021
                         *
                         */
                        input = new EditText(activity);
                        input2 = new EditText(activity);
                        final TextView textView = new TextView(activity);
                        final ImageButton imgButton1 = new ImageButton(activity);
                        final ImageButton imgButton2 = new ImageButton(activity);
                        LinearLayout layout = new LinearLayout(activity);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setPadding(10, 5, 10, 5);

                        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        lp1.weight = Float.parseFloat("0.7");

                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp2.weight = Float.parseFloat("0.3");

                        LinearLayout subLayout = new LinearLayout(activity);
                        subLayout.setOrientation(LinearLayout.HORIZONTAL);
                        subLayout.setWeightSum(1);

                        input.setLayoutParams(lp1);
                        input.setHint("S/N สินค้ายกเลิกคืน 1");
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        subLayout.addView(input);

                        imgButton1.setLayoutParams(lp2);
                        imgButton1.setImageDrawable(activity.getResources().getDrawable(R.drawable.scan));
                        imgButton1.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
                        imgButton1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                serailCheckScan = info.ProductSerialNumber;
                                new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

                                    @Override
                                    public void onSuccess(BHPermissions bhPermissions) {
                                        Intent intent = new IntentIntegrator(activity).createScanIntent();
                                        startActivityForResult(intent, REQUEST_QR_SCAN);
                                    }

                                    @Override
                                    public void onNotSuccess(BHPermissions bhPermissions) {
                                        bhPermissions.openAppSettings(getActivity());
                                    }

                                    @Override
                                    public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
                                        bhPermissions.showMessage(getActivity(), permissionType);
                                    }

                                }, BHPermissions.PermissionType.CAMERA);
                            }
                        });
                        subLayout.addView(imgButton1);

                        layout.addView(subLayout);

                        LinearLayout subLayout2 = new LinearLayout(activity);
                        subLayout.setOrientation(LinearLayout.HORIZONTAL);
                        subLayout.setWeightSum(1);

                        input2.setLayoutParams(lp1);
                        input2.setHint("S/N สินค้ายกเลิกคืน 2");
                        input2.setInputType(InputType.TYPE_CLASS_TEXT);
                        subLayout2.addView(input2);

                        imgButton2.setLayoutParams(lp2);
                        imgButton2.setImageDrawable(activity.getResources().getDrawable(R.drawable.scan));
                        imgButton2.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
                        imgButton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

                                    @Override
                                    public void onSuccess(BHPermissions bhPermissions) {
                                        Intent intent = new IntentIntegrator(activity).createScanIntent();
                                        startActivityForResult(intent, REQUEST_QR_SCAN2);
                                    }

                                    @Override
                                    public void onNotSuccess(BHPermissions bhPermissions) {
                                        bhPermissions.openAppSettings(getActivity());
                                    }

                                    @Override
                                    public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
                                        bhPermissions.showMessage(getActivity(), permissionType);
                                    }

                                }, BHPermissions.PermissionType.CAMERA);
                            }
                        });
                        subLayout2.addView(imgButton2);

                        layout.addView(subLayout2);

                        LinearLayout.LayoutParams lpText = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        textView.setLayoutParams(lpText);
                        textView.setTextSize(20);
                        textView.setPadding(10, 10, 10, 10);
                        textView.setMaxLines(3);
                        textView.setText("ต้องการยกเลิกใบจอง \nหมายเลข "  + info.CONTNO  +"\n"+info.CustomerFullName+ " ใช่หรือไม่");
                        layout.addView(textView);

                        /**
                         * END
                         */

                        AlertDialog.Builder setupAlert;
                        setupAlert = new AlertDialog.Builder(activity)
                                .setView(layout)
                                .setTitle("ยกเลิกใบจอง")
                                .setMessage(null)
                                .setCancelable(false);

//                        setupAlert = new AlertDialog.Builder(activity)
//                                .setTitle("ยกเลิกใบจอง")
//                                .setMessage("ต้องการยกเลิกใบจอง \nหมายเลข "  + info.CONTNO  +"\n"+info.CustomerFullName+ " ใช่หรือไม่")
//                                .setCancelable(false);

                        setupAlert = setupAlert.setPositiveButton("ใช่ ฉันต้องการยกเลิกใบจองนี้", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                if (input.getText().toString().equals("")) {
                                    AlertDialog.Builder waringAlert = new AlertDialog.Builder(activity)
                                        .setTitle("คำเตือน!")
                                        .setMessage("กรุณาพิมพ์หรือสแกนหมายเลขผลิตภัณฑ์")
                                        .setCancelable(false);
                                    waringAlert.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    waringAlert.show();
                                } else {
                                    doVoidContract(info.RefNo, info.CONTNO, info.ProductSerialNumber);
                                    update_contract_for_cancal_preorder(info.RefNo);
                                    updateAssignForPostpone(info.RefNo);
                                    checkHasSurvey(info.RefNo);
                                }
                            }
                        }).setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        setupAlert.show();
                    }
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

                  //  Log.e("ffg",contractList.get(position).SPECCODE+"");

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
                                  Log.e("ffg",contractList.get(position).SPECCODE+"");
                                  String gf=contractList.get(position).SPECCODE+"";
                                  if(gf.equals("1")){
                                      new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                              .setTitleText("ใบจองถูกยกเลิกแล้ว!")
                                              .setContentText("ไม่สามารถขายสินค้าได้")
                                              .show();
                                  } else {
                                      Log.e("aaa4",cont.CONTNO);
                                      BHApplication.getInstance().getPrefManager().setPreferrence("getContractReferenceNo", contractList.get(position).CONTNO);
                                      BHApplication.getInstance().getPrefManager().setPreferrence("code_name", cont.SaleTeamCode);
                                      BHApplication.getInstance().getPrefManager().setPreferrence("name_recommend",cont.SaleEmployeeCode);
                                      BHApplication.getInstance().getPrefManager().setPreferrence("name_CustomerID",cont.RefNo);

                                      // showNextView(new SaleScanEmployeesFragment_preorder_setting());
                                      // select_page_s=1;
                                      BHPreference.setRefNo("");
                                      // ใช้งานจริงเปิด Method BarcodeScan แล้วปิด บรรทัดที่ 113-119
                                      saleMainFragment_preorder_setting = new SaleMainFragment_preorder_setting();
                                      saleMainFragment_preorder_setting.BarcodeScan();
                                  }
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

    private SQLiteDatabase database = null;

    public void updateAssignForPostpone_test() {

        String sql = "update Contract set [SPECCODE] = ? ";
        //  String sql = "UPDATE Assign SET [Order] = ? WHERE AssignID = ?";
        executeNonQuery2(sql, new String[]{"0"});
    }

    public void updateAssignForPostpone(String Refno) {

        String sql = "update Contract set [SPECCODE] = ? WHERE Refno =?";
        //  String sql = "UPDATE Assign SET [Order] = ? WHERE AssignID = ?";
        executeNonQuery2(sql, new String[]{"1", Refno});
    }


    protected void executeNonQuery2(String sql, String[] args) {
        openDatabase2();
        try {
            if (args == null) {
                database.execSQL(sql);
            } else {
                database.execSQL(sql, args);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeDatabase2();
        }
    }
    private void openDatabase2() {
        database = DatabaseManager.getInstance().openDatabase();
    }
    private void closeDatabase2() {
        closeDatabase2(false);
    }
    private void closeDatabase2(boolean force) {

        Log.e("555","6666");


        String Search=edtSearch.getText().toString();
        GetContractStatusFinish2(Search);


        if(force)
            DatabaseManager.getInstance().forceCloseDatabase();
        else
            DatabaseManager.getInstance().closeDatabase();
    }


    private void doVoidContract(final String RefNo, final String ContractNo, final String ProductSerialNumber){
        // ทำการยกเลิกสัญญา
        (new BackgroundProcess(activity) {
            ProductStockInfo ps = null;

            @Override
            protected void calling() {
                // TODO Auto-generated method stub

                TSRController.voidContract(RefNo, ContractNo,BHPreference.employeeID(), true);
                ps = getProductStock(ProductSerialNumber, ProductStockController.ProductStockStatus.SOLD);
                if (ps !=null) {
                    ps.ProductSerialNumber = ProductSerialNumber;
                    ps.OrganizationCode = BHPreference.organizationCode();
                    ps.Status = ProductStockController.ProductStockStatus.CHECKED.toString();
                    ps.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.teamCode();
                    ps.ScanDate = new Date();
                    updateProductStock(ps,true);
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated
                // method stub
//                AlertDialog.Builder VoidNoti;
//
//                VoidNoti = new AlertDialog.Builder(activity)
//                        .setTitle("ยกเลิกใบจอง")
//                        .setMessage("ระบบดำเนินการยกเลิกใบจอง "   + " เรียบร้อยแล้ว")
//                        .setCancelable(false);
//                VoidNoti = VoidNoti.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                      //  showLastView();
//                    }
//                });
//                VoidNoti.show();
                /**
                 * Edit by Teerayut Klinsanga
                 *
                 * Create date 24/05/2021
                 */
                onVoid(RefNo, BHPreference.employeeID(), divisionid, "4678", "ยกเลิกใบจอง", "06");

                /**
                 * End
                 */
            }
        }).start();
    }

    public void update_contract_for_cancal_preorder(String Refno){
        Log.e("Refno",Refno);
        jsonArrayRequest = new JsonArrayRequest("https://tssm.thiensurat.co.th/assanee_UAT/assanee/bighead_api_new/update_contract_for_cancal_preorder.php"+"?Refno="+Refno,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

            /*            try {
                            new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("บันทึกข้อมูล!")
                                    .setContentText("เรียบร้อย!")
                                    .show();
                        }
                        catch (Exception EX){

                        }*/
                        // showDialog("บันทึกข้อมูล","เรียบร้อย");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //showDialog("บันทึกข้อมูล","เรียบร้อย");
    /*                    try {
                            new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("บันทึกข้อมูล!")
                                    .setContentText("เรียบร้อย!")
                                    .show();
                        }
                        catch (Exception EX){

                        }*/
                    }
                });
        try {
            try {
                requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(jsonArrayRequest);

                int MY_SOCKET_TIMEOUT_MS=10000;

                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            } catch (RuntimeException ex){

            }
        } catch (OutOfMemoryError EX){

        }
    }

    private void printDocument() {
        new PrinterController(activity).printNewImageContract_preorder2(contract, addressIDCard, addressInstall);
    }

    private void checkHasSurvey(String refno) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.check_save_data(refno);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    //  Log.e("Survey contno", contract.RefNo);
                    Log.e("Survey contno",refno);

                    Gson gson=new Gson();
                    try {
                        JSONObject jsonObject=new JSONObject(gson.toJson(response.body()));
                        //JSONArray array = jsonObject.getJSONArray("data");
                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data(jsonObject.getJSONArray("data"));
                        // layoutSurvey.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data","22");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("onFailure question:",t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Exception question",e.getLocalizedMessage());
        }
    }

    public  void JSON_PARSE_DATA_AFTER_WEBCALL_load_data(JSONArray array) {
        if(array.length()==0){

        } else {
            for (int i = 0; i < array.length(); i++) {
                final Get_data_api3 GetDataAdapter2 = new Get_data_api3();
                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);

                    String FirstPeriodPayBy=json.getString("FirstPeriodPayBy");
                    String FirstPeriodPayAmount=json.getString("FirstPeriodPayAmount");
                    String ContractBy=json.getString("ContractBy");
                    String WaterInfo=json.getString("WaterInfo");
                    String WaterProblem=json.getString("WaterProblem");
                    String WaterProblemMore=json.getString("WaterProblemMore");
                    String ShippingBy=json.getString("ShippingBy");
                    String ShippingDate=json.getString("ShippingDate");
                    String ShippingTo=json.getString("ShippingTo");
                    String TelnoCus=json.getString("TelnoCus");
                    String InstallDetails=json.getString("InstallDetails");

                    BHApplication.getInstance().getPrefManager().setPreferrence("FirstPeriodPayBy", FirstPeriodPayBy);
                    BHApplication.getInstance().getPrefManager().setPreferrence("FirstPeriodPayAmount", FirstPeriodPayAmount);
                    BHApplication.getInstance().getPrefManager().setPreferrence("ContractBy", ContractBy);
                    BHApplication.getInstance().getPrefManager().setPreferrence("WaterInfo", WaterInfo);
                    BHApplication.getInstance().getPrefManager().setPreferrence("WaterProblem", WaterProblem);
                    BHApplication.getInstance().getPrefManager().setPreferrence("WaterProblemMore", WaterProblemMore);
                    BHApplication.getInstance().getPrefManager().setPreferrence("ShippingBy", ShippingBy);
                    BHApplication.getInstance().getPrefManager().setPreferrence("ShippingDate", ShippingDate);
                    BHApplication.getInstance().getPrefManager().setPreferrence("ShippingTo", ShippingTo);
                    BHApplication.getInstance().getPrefManager().setPreferrence("TelnoCus", TelnoCus);
                    BHApplication.getInstance().getPrefManager().setPreferrence("InstallDetails", InstallDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     *
     * Edit by Teerayut Klinsanga
     *
     * Created date 27/04/2021
     *
     */
    String barcode1, barcode2, divisionid;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_QR_SCAN) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showMessage("ยังไม่ได้ทำการสแกน กรุณาทำการสแกนอีกครั้ง");
            } else if (resultCode == Activity.RESULT_OK) {
                barcode1 = intent.getStringExtra(Intents.Scan.RESULT);
                if (barcode1.equals(serailCheckScan)) {
                    AlertDialog.Builder VoidNoti = new AlertDialog.Builder(activity)
                            .setTitle("คำเตือน")
                            .setMessage("หมายเลขเครื่องไม่ตรงกับรายการที่ต้องการยกเลิก\nกรุณาสแกนใหม่อีกครั้ง")
                            .setCancelable(false);
                    VoidNoti = VoidNoti.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            input.setText("");
                        }
                    });
                    VoidNoti.show();
                } else {
                    input.setText(barcode1);
                }

                Log.e("Barcode 1", barcode1);
            }
        } else if (requestCode == REQUEST_QR_SCAN2) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showMessage("ยังไม่ได้ทำการสแกน กรุณาทำการสแกนอีกครั้ง");
            } else if (resultCode == Activity.RESULT_OK) {
                barcode2 = intent.getStringExtra(Intents.Scan.RESULT);
                input2.setText(barcode2);
                Log.e("Barcode 2", barcode2);
            }
        }
    }

    public void getDivisionId(String empid) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GIS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = null;
            if (BHGeneral.SERVICE_MODE.toString() == "UAT") {
                call = request.getDivisionIdUAT(empid);
            } else {

            }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    //  Log.e("Survey contno", contract.RefNo);

                    Gson gson=new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        divisionid = jsonObject.getString("data");
//                        divisionid = jsonArray.getJSONObject(0).getString("data");
                        Log.e("DivisionId", divisionid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data",e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("onFailure DivisionId:",t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Exception DivisionId",e.getLocalizedMessage());
        }
    }

    public void onVoid(String contno, String empid, String departid, String problemid, String detail, String channel) {
        try {
            Call call = null;
            Service request = null;
            Retrofit retrofit = null;
            if (BHGeneral.SERVICE_MODE.toString() == "UAT") {

                List<MultipartBody.Part> parts = new ArrayList<>();
                parts.add(MultipartBody.Part.createFormData("ProblemID[]", problemid));
                parts.add(MultipartBody.Part.createFormData("ProblemDetail[]", detail));
                RequestBody refnoBody = RequestBody.create(MediaType.parse("text/plain"), contno);
                RequestBody empBody = RequestBody.create(MediaType.parse("text/plain"), empid);
                RequestBody departBody = RequestBody.create(MediaType.parse("text/plain"), departid);
                RequestBody channelBody = RequestBody.create(MediaType.parse("text/plain"), channel);


                retrofit = new Retrofit.Builder()
                        .baseUrl(DRINKO_BASE_UAT_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                request = retrofit.create(Service.class);
                call = request.openTicketUAT(refnoBody, empBody, departBody, parts, channelBody);
//                call = request.openTicketUAT(contno, empid, departid, problemid, detail, channel);
            } else {
                retrofit = new Retrofit.Builder()
                        .baseUrl(DRINKO_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                request = retrofit.create(Service.class);
            }


            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson=new Gson();
                    try {
                        Log.e("JSON body", String.valueOf(response.body()));
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        Log.e("Void", String.valueOf(jsonObject));
                        if (jsonObject.getString("status").equals("SUCCESS")) {
                            AlertDialog.Builder waringAlert = new AlertDialog.Builder(activity)
                                    .setTitle("ยกเลิกใบจอง!")
                                    .setMessage("ดำเนินการยกเลิกใบจองเรียบร้อย\n" + "ใบงานเลขที่ " + jsonObject.getString("InformID"))
                                    .setCancelable(false);
                            waringAlert.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            waringAlert.show();
                        } else {
                            AlertDialog.Builder waringAlert = new AlertDialog.Builder(activity)
                                    .setTitle("คำเตือน!")
                                    .setMessage("พบข้อผิดพลาดในการเปิดใบงาน")
                                    .setCancelable(false);
                            waringAlert.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            waringAlert.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data",e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("onFailure onVoid:",t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Exception onVoid",e.getLocalizedMessage());
        }
    }

    /**
     * END
     */
}
