package th.co.thiensurat.fragments.payment.next;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.ContractAdapter;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.PositionController;
import th.co.thiensurat.data.controller.RequestNextPaymentController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.RequestNextPaymentInfo;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.service.data.GetRequestNextPaymentForNewRequestInputInfo;
import th.co.thiensurat.service.data.GetRequestNextPaymentInputInfo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class NextPaymentListFragment extends BHFragment {

    public static final String NEXT_PAYMENT_LIST_FRAGMENT = "NEXT_PAYMENT_LIST_FRAGMENT";
    @InjectView private EditText edtSearch;
    @InjectView private Button btnSearch;
    @InjectView private ListView lvRequestNextPayment;

    private ContractAdapter requestNextPaymentAdapter;
    private List<RequestNextPaymentInfo> requestNextPaymentList;
    //RequestNextPaymentInfo selectedRequestNextPayment;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_next_payment_customer_list;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_update_list, R.string.button_next};
    }

    @Override
    protected int titleID() {
        return R.string.title_payment_next;
    }

    @Override
    public String fragmentTag() {
        return NEXT_PAYMENT_LIST_FRAGMENT;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {



        LayoutInflater inflater = activity.getLayoutInflater();
//        ViewGroup headerListView = (ViewGroup) inflater.inflate(R.layout.list_next_payment_header, lvRequestNextPayment, false);
        ViewGroup headerListView = (ViewGroup) inflater.inflate(R.layout.list_contract_status_header, lvRequestNextPayment, false);
        lvRequestNextPayment.addHeaderView(headerListView, null, false);

        if (requestNextPaymentList == null) {
            requestNextPaymentList = new ArrayList<RequestNextPaymentInfo>();
        }

//        requestNextPaymentAdapter = new RequestNextPaymentAdapter(activity, R.layout.list_next_payment, requestNextPaymentList);
        requestNextPaymentAdapter = new ContractAdapter(activity, R.layout.list_contract_status_item, requestNextPaymentList, true);
        lvRequestNextPayment.setAdapter(requestNextPaymentAdapter);
        lvRequestNextPayment.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        bindRequestNextPaymentList("", false);

        lvRequestNextPayment.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedRequestNextPayment = (RequestNextPaymentInfo) parent.getItemAtPosition(position);
                lvRequestNextPayment.setItemChecked(position, true);
            }
        });

        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bindRequestNextPaymentList(edtSearch.getText().toString(), true);
            }
        });
    }

    private void bindRequestNextPaymentList(final String searchText, final boolean isSearch) {
        (new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (requestNextPaymentList == null) {
                    requestNextPaymentList = new ArrayList<RequestNextPaymentInfo>();
                } else {
                    requestNextPaymentList.clear();
                }
            }

            @Override
            protected void calling() {
                //List<RequestNextPaymentInfo> list = new RequestNextPaymentController().getRequestNextPaymentByIDOrStatusOrSearchText(BHPreference.organizationCode(), searchText, null, null, null);
                List<RequestNextPaymentInfo> list = new RequestNextPaymentController().getNewRequestNextPaymentForNextPaymentListFragment
                        (searchText, BHPreference.organizationCode(), null, BHPreference.employeeID(), BHPreference.teamCode(), BHPreference.sourceSystem(), BHPreference.PositionCode());
                if (list != null && list.size() > 0) {
                    //Local
                    requestNextPaymentList.addAll(list);
                } else {
                    // Server
                    if (isSearch && !searchText.trim().isEmpty()) {
                        GetRequestNextPaymentForNewRequestInputInfo input = new GetRequestNextPaymentForNewRequestInputInfo();
                        input.OrganizationCode = BHPreference.organizationCode();
                        input.SearchText = searchText;

                        /*** [START] :: Fixed - [BHPROJ-0020-980] :: [Web-Admin - เมนูการขออนุมัติเก็บเงินค่างวดที่คาดว่าจะสูญ] หน้ารายละเอียด Time Out ***/
//                        List<RequestNextPaymentInfo> requestNextPaymentListSearch = TSRService.getRequestNextPaymentByIDOrStatusOrSearchText(input, false).Info;
                        List<RequestNextPaymentInfo> requestNextPaymentListSearch = TSRService.getRequestNextPaymentForNewRequest(input, false).Info;
                        /*** [END] :: Fixed - [BHPROJ-0020-980] :: [Web-Admin - เมนูการขออนุมัติเก็บเงินค่างวดที่คาดว่าจะสูญ] หน้ารายละเอียด Time Out ***/

                        if (requestNextPaymentListSearch != null && requestNextPaymentListSearch.size() > 0) {
                            /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                            /*
                            for (RequestNextPaymentInfo request : requestNextPaymentListSearch) {
                                TSRController.importContractFromServer(request.ContractOrganizationCode, null, request.ContractRefNo);
                            }
                            */
                            /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                            requestNextPaymentList.addAll(requestNextPaymentListSearch);
                        }
                    }
                }
            }

            @Override
            protected void after() {
                if (requestNextPaymentAdapter != null) {
                    lvRequestNextPayment.clearChoices();
                    requestNextPaymentAdapter.notifyDataSetChanged();
                }
                if (isSearch && searchText.trim().isEmpty()) {
                    BHUtilities.alertDialog(activity, "คำเตือน", "กรุณากรอกข้อมูลที่ต้องการค้นหา").show();
                } else {
                    if (isSearch && requestNextPaymentList != null && requestNextPaymentList.size() == 0) {
                        BHUtilities.alertDialog(activity, "ผลการค้นหา", "ไม่พบรายการสัญญา").show();
                    }
                }
            }
        }).start();
    }

    /*
    public class RequestNextPaymentAdapter extends BHArrayAdapter<RequestNextPaymentInfo> {

        public RequestNextPaymentAdapter(Context context, int resource, List<RequestNextPaymentInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txtCustomerFullName, txtCONTNO, txtPaymentPeriodNumber, txtOutStandingAmount, txtRequestNextPaymentStatus;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, RequestNextPaymentInfo info) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txtCustomerFullName.setText(info.CustomerFullName);
            viewHolder.txtCustomerFullName.setTextColor(getResources().getColor(getColor(info.HoldSalePaymentPeriod)));
            viewHolder.txtCONTNO.setText(info.CONTNO);
            viewHolder.txtPaymentPeriodNumber.setText(String.valueOf(info.PaymentPeriodNumber));
            viewHolder.txtOutStandingAmount.setText(BHUtilities.numericFormat(info.OutStandingAmount));
            viewHolder.txtRequestNextPaymentStatus.setText(getRequestNextPaymentStatus(info.Status));
            //view.setBackgroundColor(getResources().getColor(getColor(info.HoldSalePaymentPeriod)));
        }

        public int getColor(int HoldSalePaymentPeriod) {
            switch (HoldSalePaymentPeriod) {
                case 0:
                    return R.color.hold_payment_0;
                case 1:
                    return R.color.hold_payment_1;
                case 2:
                    return R.color.hold_payment_2;
                default:
                    return R.color.hold_payment_3;
            }
        }
    }
    */

    public String getRequestNextPaymentStatus(String status) {
        if (status == null) {
            return "";
        } else if (status.equals(RequestNextPaymentController.RequestNextPaymentStatus.REQUEST.toString())) {
            return "N";
        } else if (status.equals(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString())) {
            return "Y";
        } else {
            return "";
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_next:
                /*RequestNextPaymentInfo selectedRequestNextPayment = lvRequestNextPayment.getCheckedItemPosition() != -1 ? requestNextPaymentList.get(lvRequestNextPayment.getCheckedItemPosition() - 1) : null;
                if (selectedRequestNextPayment != null) {
                    if (selectedRequestNextPayment.Status != null && selectedRequestNextPayment.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.REQUEST.toString())) {
                        BHUtilities.alertDialog(activity, "คำเตือน", "สถานะการร้องขอยังไม่ถูกอนุมัติ").show();
                    } else {
                        // approved :: payment , complete reject null :: new , (request not click)
                        // ถ้าเป็นหัวหน้าทีมขายจะสามารถเห็นข้อมูลการ request ของลูกทีมได้ และทำแทนลูกทีมได้
                        String employeeID = BHPreference.employeeID();
//                        if (BHPreference.PositionCode().contains(PositionController.PositionCode.SaleLeader.toString())) {
//                            employeeID = null;
//                        }
                        if (selectedRequestNextPayment.Status == null ||
                                (selectedRequestNextPayment.Status != null
                                        && selectedRequestNextPayment.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString())
                                        && (selectedRequestNextPayment.AssigneeTeamCode != null && selectedRequestNextPayment.AssigneeTeamCode.equals(BHPreference.teamCode()))
                                        && (employeeID == null || (employeeID != null && selectedRequestNextPayment.AssigneeEmpID != null && selectedRequestNextPayment.AssigneeEmpID.equals(employeeID))))) {
                            next(selectedRequestNextPayment);
                        } else {
                            //BHUtilities.alertDialog(activity, "คำเตือน", "สัญญานี้ได้ถูกมอบหมายงานเก็บเงินงวดต่อไปให้กับทีม" + selectedRequestNextPayment.AssigneeTeamCode + " และรหัสพนักงาน " + selectedRequestNextPayment.AssigneeEmpID).show();
                            BHUtilities.alertDialog(activity, "คำเตือน", "สัญญานี้ได้ถูกมอบหมายงานเก็บเงินงวดต่อไปให้กับทีมอื่น").show();
                        }
                    }
                } else {
                    BHUtilities.alertDialog(activity, "คำเตือน", "เลือกรายการ").show();
                }*/

                final RequestNextPaymentInfo selectedRequestNextPayment = lvRequestNextPayment.getCheckedItemPosition() != -1 ? requestNextPaymentList.get(lvRequestNextPayment.getCheckedItemPosition() - 1) : null;
                if (selectedRequestNextPayment != null) {
                    if (selectedRequestNextPayment.Status != null){
                        switch ( RequestNextPaymentController.RequestNextPaymentStatus.valueOf(RequestNextPaymentController.RequestNextPaymentStatus.class, selectedRequestNextPayment.Status)) {
                            case REQUEST:
                                BHUtilities.alertDialog(activity, "คำเตือน", "สถานะการร้องขอยังไม่ถูกอนุมัติ").show();
                                break;
                            case APPROVED:
                                RequestNextPaymentDetailFragment.Data data = new RequestNextPaymentDetailFragment.Data();
                                data.RequestNextPaymentID = selectedRequestNextPayment.RequestNextPaymentID;
                                RequestNextPaymentDetailFragment fragment = BHFragment.newInstance(RequestNextPaymentDetailFragment.class, data);
                                showNextView(fragment);
                                break;
                        }
                    } else {
                        (new BackgroundProcess(activity) {
                            List<RequestNextPaymentInfo> listRequestNextPaymentInfo;

                            @Override
                            protected void before() { }

                            @Override
                            protected void calling() {
                                //listRequestNextPaymentInfo = new RequestNextPaymentController().getNewRequestNextPaymentBySearchTextForCredit(BHPreference.organizationCode(), null, BHPreference.employeeID(), selectedRequestNextPayment.ContractRefNo);
                                listRequestNextPaymentInfo =  new RequestNextPaymentController().getNewRequestNextPaymentForNextPaymentListFragment
                                        (null, BHPreference.organizationCode(), selectedRequestNextPayment.ContractRefNo, BHPreference.employeeID(), BHPreference.teamCode(), BHPreference.sourceSystem(), BHPreference.PositionCode());

                                if(listRequestNextPaymentInfo == null){
                                    TSRController.importContractFromServer(BHPreference.organizationCode(), null, selectedRequestNextPayment.ContractRefNo);
                                }
                            }

                            @Override
                            protected void after() {
                                RequestNextPaymentDetailFragment.Data data = new RequestNextPaymentDetailFragment.Data();
                                data.RefNo = selectedRequestNextPayment.ContractRefNo;
                                RequestNextPaymentDetailFragment fragment = BHFragment.newInstance(RequestNextPaymentDetailFragment.class, data);
                                showNextView(fragment);
                            }
                        }).start();
                    }
                } else {
                    BHUtilities.alertDialog(activity, "คำเตือน", "กนุณาเลือกรายการ").show();
                }
                break;
            case R.string.button_update_list:
                internetDialog = new ProgressDialog(activity);
                internetDialog.setIndeterminate(false);
                internetDialog.setCancelable(false);
                internetDialog.setTitle("Connecting To Internet");
                internetDialog.setMessage("Checking...");
                internetDialog.show();
                // Check Internet
                handler.postDelayed(delayCheckConnectingToInternet, 3000);
                break;
            default:
                break;
        }
    }

    private void next(final RequestNextPaymentInfo selectedRequestNextPayment) {

        /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
        if (selectedRequestNextPayment.Status != null) {
            // (A) Request
            if (selectedRequestNextPayment.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.REQUEST.toString())) {
                String title = "กรุณาตรวจสถานะ";
                String message = "สถานะการร้องขอยังไม่ถูกอนุมัติ ";
                showNoticeDialogBox(title, message);
            } else if (selectedRequestNextPayment.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString()))  {
                RequestNextPaymentDetailFragment.Data data = new RequestNextPaymentDetailFragment.Data();
                data.RequestNextPaymentID = selectedRequestNextPayment.RequestNextPaymentID;
                RequestNextPaymentDetailFragment fragment = BHFragment.newInstance(RequestNextPaymentDetailFragment.class, data);
                showNextView(fragment);
            }
        } else {
            // (B) Approved/Reject/Null :: new
            (new BackgroundProcess(activity) {
                    ContractInfo con;
                    @Override
                    protected void before() { }

                    @Override
                    protected void calling() {
                        con = TSRController.getContractByRefNo(BHPreference.organizationCode(), selectedRequestNextPayment.ContractRefNo);
                        if(con == null){
                            TSRController.importContractFromServer(selectedRequestNextPayment.OrganizationCode, null, selectedRequestNextPayment.ContractRefNo);
                        }
                    }

                    @Override
                protected void after() {
                    RequestNextPaymentDetailFragment.Data data = new RequestNextPaymentDetailFragment.Data();
                        data.RefNo = selectedRequestNextPayment.ContractRefNo;
                        /* FIX [BHPROJ-0026-754] กรณีเป็นการขอเก็บเงินของสัญญาที่เป็นของตัวเองอยู่แล้ว ไม่ต้องขออนุมัติขอเก็บเงินงวดต่อไป ให้สามารถเก็บเงินงวดต่อไปได้เลยไปได้เลย */
                        /* FIX [BHPROJ-0026-840] ลูกค้าต้องการให้ทำการ ขออนุมัติเสมอ ไม่ว่าจะเป็นการขอเก็บเงินของทีมเดียวกันก็ตาม */
//                        if (selectedRequestNextPayment.Status == null && selectedRequestNextPayment.AssigneeDefaultAssigneeEmpID != null && BHPreference.employeeID().equals(selectedRequestNextPayment.AssigneeDefaultAssigneeEmpID)) {
//                            data.AssigneeDefaultAssigneeEmpID = selectedRequestNextPayment.AssigneeDefaultAssigneeEmpID;
//                        }
                    RequestNextPaymentDetailFragment fragment = BHFragment.newInstance(RequestNextPaymentDetailFragment.class, data);
                    showNextView(fragment);
                }
            }).start();
        }
        /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/

    }

    ProgressDialog internetDialog;

    Handler handler = new Handler();

    final Runnable delayCheckConnectingToInternet = new Runnable() {
        public void run() {
            if (isConnectingToInternet()) {
                internetDialog.setTitle("Connecting To Server");
                handler.postDelayed(delayCheckConnectToServer, 3000);
            } else {
                internetDialog.dismiss();
                showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
            }
        }
    };

    final Runnable delayCheckConnectToServer = new Runnable() {
        public void run() {
            checkConnectToServer();
        }
    };

    public void startSynchronize() {
        TransactionService.stopService(activity);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
        request.master.syncRequestNextPaymentRelated = true;

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);

    }

    private class SynchronizeReceiver extends BroadcastReceiver {
        private SynchronizeReceiver instance;
        private SynchronizeService.SynchronizeResult result;
        private boolean isProcessing;
        private ProgressDialog dialog;

        private SynchronizeReceiver() {
            dialog = new ProgressDialog(activity);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setTitle("");
            dialog.setMessage("");

            result = null;
            isProcessing = false;
        }

        public SynchronizeReceiver getInstance() {
            if (instance == null) {
                instance = new SynchronizeReceiver();
                LocalBroadcastManager.getInstance(activity).registerReceiver(instance, new IntentFilter(SynchronizeService.SYNCHRONIZE_BROADCAST_ACTION));
            }
            return instance;
        }

        public void show() {
            if (isProcessing && !dialog.isShowing()) {
                dialog.show();
            }
        }

        private void start() {
            if (!isProcessing) {
                isProcessing = true;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        }

        private void stop() {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(this);
            isProcessing = false;
            dialog = null;
            result = null;
            instance = null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                result = intent.getParcelableExtra(SynchronizeService.SYNCHRONIZE_RESULT_DATA_KEY);
                dialog.setTitle(result.title);
                dialog.setMessage(result.message);
                dialog.setProgress(result.progress);

                if (result.progress >= 100) {
                    dialog.setProgress(100);
                }

                if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED || result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                    dialog.dismiss();
                    if (result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                        showWarningDialog(result.error);
                    } else {
                        // Synchronize syncEditContractRelated Pass
                        Log.e("NextPaymentListFragment", "Synchronize syncEditContractRelated Pass");
                        bindRequestNextPaymentList("", false);
                    }
                    stop();
                }
            }
        }
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private int timeOut = 20 * 1000;

    public void checkConnectToServer() {
        new BackgroundProcess(activity) {
            private boolean isConnected;

            @Override
            protected void calling() {
                HttpGet httpGet = new HttpGet(BHPreference.TSR_SERVICE_URL);
                HttpClient client = new DefaultHttpClient();
                //TimeOut 20s
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, timeOut);
                HttpConnectionParams.setSoTimeout(params, timeOut);

                HttpResponse response = null;
                try {
                    response = client.execute(httpGet);
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {
                        isConnected = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void after() {
                if (internetDialog != null) {
                    internetDialog.dismiss();
                }
                if (isConnected) {
                    startSynchronize();
                } else {
                    showWarningDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได");
                }
            }
        }.start(false);
    }

    private void showNoticeDialogBox(String title, String message) {
        AlertDialog.Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                .setPositiveButton("ปิด", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        setupAlert.show();
    }

}
