package th.co.thiensurat.fragments.impound;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.ContractAdapter;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.ImpoundProductController;
import th.co.thiensurat.data.controller.ImpoundProductController.ImpoundProductStatus;
import th.co.thiensurat.data.controller.LogScanProductSerialController;
import th.co.thiensurat.data.controller.ProblemController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.data.info.LogScanProductSerialInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.fragments.complain.ComplainDetailFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment.ScanCallBack;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.service.data.GetContractOfOtherTeamForAvailableImpoundInputInfo;
import th.co.thiensurat.service.data.GetImpoundProductByImpoundProductIDInputInfo;
import th.co.thiensurat.service.data.GetImpoundProductOtherTeamForSearchInputInfo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import static th.co.thiensurat.business.controller.TSRController.getContractProductSerialByStatus;

public class ImpoundProductListOtherTeamFragment extends BHFragment {
    public static String FRAGMENT_IMPOUND_PRODUCT_LIST_TAG = "impound_product_list_tag";
    private static final String IMPOUND_OTHER_LIST_KTY = "IMPOUND_OTHER_LIST_KTY";
    private List<ImpoundProductInfo> impoundProductList;
    //private ImpoundProductInfo selectContract;
    private ContractInfo result;
    private String Date, Time, ProductScan, ImpoundID;
    private ContractAdapter impoundAdapter;

    @InjectView private ListView listCustomer;
    @InjectView private Button btnSerch;
    @InjectView private EditText etSerialNumber;
    @InjectView private TextView textSearch;

    private AlertDialog alertDialog;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_impound_product_list;
    }

    @Override
    public String fragmentTag() {
        return FRAGMENT_IMPOUND_PRODUCT_LIST_TAG;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_update_list, R.string.button_next};
    }

    @Override
    protected int titleID() {
        return R.string.title_remove;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(IMPOUND_OTHER_LIST_KTY)) {
                impoundProductList = savedInstanceState.getParcelableArrayList(IMPOUND_OTHER_LIST_KTY);
            }
        }
        textSearch.setText("ค้นหา");
        btnSerch.setText("ค้นหา");

        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_contract_status_header, listCustomer, false);
        listCustomer.addHeaderView(header, null, false);

        impoundProductList = new ArrayList<ImpoundProductInfo>();
        impoundAdapter = new ContractAdapter(activity, R.layout.list_contract_status_item,
                impoundProductList);
        listCustomer.setAdapter(impoundAdapter);
        listCustomer.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listCustomer.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(alertDialog == null){
                    listCustomer.setItemChecked(position, true);
                    ImpoundProductInfo selectContract = impoundProductList.get(listCustomer.getCheckedItemPosition() - 1);
                    if (selectContract.Status == null) {
                        // insert table impound product : request impound product other team
                        isRequestImpoundProductOtherTeam(position - 1);
                    } else if (selectContract.Status.equals(ImpoundProductStatus.APPROVED.toString())) {

                    }
                }
            }
        });

        bindImpoundProductList(null);

        btnSerch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if(etSerialNumber.getText().toString().trim().isEmpty()) {
                   showNoticeDialogBox("คำเตือน", "กรุณากรอกข้อมูลที่ต้องการค้นหา");
               } else {
                   bindImpoundProductList(etSerialNumber.getText().toString());
               }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(IMPOUND_OTHER_LIST_KTY, (ArrayList<ImpoundProductInfo>) impoundProductList);
        super.onSaveInstanceState(outState);
    }

    private void bindImpoundProductList(final String SearchText) {
        new BackgroundProcess(activity) {
            boolean PaymentComplete = false;
            @Override
            protected void before() {
                impoundProductList.clear();

                if (BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString())) {
                    PaymentComplete = true;
                }

                List<ImpoundProductInfo> impoundProductFilterList;
                if(BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Sale.toString())){
                    impoundProductFilterList = getImpoundProductOtherTeamByStatusRequestOrApproved(BHPreference.organizationCode(), BHPreference.employeeID(), BHPreference.teamCode(), SearchText, PaymentComplete, null);
                }else{
                    impoundProductFilterList = new ImpoundProductController().getImpoundProductOtherTeamByStatusRequestOrApprovedForCredit(BHPreference.organizationCode(), BHPreference.employeeID(), BHPreference.teamCode(), SearchText, PaymentComplete, null);
                }

                if (impoundProductFilterList != null && impoundProductFilterList.size() > 0) {
                    impoundProductList.addAll(impoundProductFilterList);
                }
            }

            @Override
            protected void calling() {
                if (SearchText != null && (impoundProductList == null || impoundProductList.size() == 0)) {
                    GetImpoundProductOtherTeamForSearchInputInfo input = new GetImpoundProductOtherTeamForSearchInputInfo();
                    input.OrganizationCode = BHPreference.organizationCode();
                    input.RequestTeamCode = BHPreference.teamCode();
                    input.SearchText = SearchText;
                    input.PaymentComplete = PaymentComplete;
                    input.EmployeeID = BHPreference.employeeID();

                    List<ImpoundProductInfo> impoundProductFilterList = TSRService.getImpoundProductOtherTeamForSearch(input, false).Info;

                    if (impoundProductFilterList != null && impoundProductFilterList.size() > 0) {
                        impoundProductList.addAll(impoundProductFilterList);
                    }
                }
            }

            @Override
            protected void after() {
                listCustomer.clearChoices();
                impoundAdapter.notifyDataSetChanged();

                if(impoundProductList == null || impoundProductList.size() == 0){
                    showNoticeDialogBox("คำเตือน", "ไม่พบข้อมูล");
                }
            }
        }.start();
    }

    ProblemInfo selectedProblem;
    private void isRequestImpoundProductOtherTeam(final int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.impound_product_dialog, null);

        final TextView txtDetail = (TextView) alertLayout.findViewById(R.id.txtDetail);
        final Spinner spinnerProblem = (Spinner) alertLayout.findViewById(R.id.spinnerProblem);
        final EditText edtNote = (EditText) alertLayout.findViewById(R.id.edtNote);

        final String title = "ยืนยันการส่งคำร้องขอถอดเครื่อง";
        final String message = String.format("สถานะ : %s \nหมายเลขเครื่อง : %s \nสินค้า : %s " +
                        "\nทีมที่ทำสัญญา : %s \nเลขที่สัญญา : %s \nทีมร้องขอถอดเครื่อง : %s ",
                BHUtilities.trim(ImpoundProductStatus.REQUEST.toString()),
                impoundProductList.get(position).ProductSerialNumber, impoundProductList.get(position).ProductName, impoundProductList.get(position).SaleTeamCode
                , impoundProductList.get(position).CONTNO, BHPreference.teamCode());


        txtDetail.setText(message);

        List<ProblemInfo> problemList = TSRController.getProblemByProblemType(BHPreference.organizationCode(), ProblemController.ProblemType.ImpoundProduct.toString());
        if(problemList == null){
            problemList = new ArrayList<>();
        }
        ProblemInfo blank = new ProblemInfo();
        blank.ProblemID = "";
        blank.ProblemName = "";
        problemList.add(0, blank);

        final ProblemAdapter problemAdapter = new ProblemAdapter(activity, R.layout.spinner_item_problem, problemList);
        spinnerProblem.setAdapter(problemAdapter);
        spinnerProblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedProblem = (ProblemInfo) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerProblem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(edtNote);
                return false;
            }
        });

        Builder builder = new Builder(activity);
        builder.setTitle(title);
        builder.setView(alertLayout);
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                hideKeyboard(edtNote);
                dialog.cancel();
                alertDialog = null;
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                hideKeyboard(edtNote);
                if (spinnerProblem.getSelectedItemPosition() == 0) {
                        AlertDialog.Builder notification = BHUtilities.builderDialog(activity, "คำเตือน", "กรุณาเลือกสาเหตุการถอดเครื่อง");
                        notification.setCancelable(false);
                        notification.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isRequestImpoundProductOtherTeam(position);
                            }
                        });
                    notification.show();
                } else {
                    ImpoundProductInfo selectContract = listCustomer.getCheckedItemPosition() != -1 ? impoundProductList.get(listCustomer.getCheckedItemPosition() - 1) : null;
                    if (selectContract != null && selectContract.Status == null && selectedProblem != null && !selectedProblem.ProblemName.equals("")) {
                        addImpoundProduct(selectContract, position, edtNote.getText().toString());
                    }
                }
                alertDialog = null;
            }
        });
        alertDialog = builder.show();
    }

    public void addImpoundProduct(final ImpoundProductInfo selectContract, final int position, final String edtNote){
        (new BackgroundProcess(activity) {
            Date newDate = new Date();

            @Override
            protected void before() {
                impoundProductList.get(position).ImpoundProductID = DatabaseHelper.getUUID();
                impoundProductList.get(position).OrganizationCode = BHPreference.organizationCode();
                impoundProductList.get(position).RefNo = selectContract.RefNo;
                impoundProductList.get(position).Status = ImpoundProductStatus.REQUEST.toString();
                impoundProductList.get(position).RequestProblemID = selectedProblem.ProblemID;
                impoundProductList.get(position).RequestDetail = edtNote;
                impoundProductList.get(position).RequestDate = newDate;
                impoundProductList.get(position).RequestBy = BHPreference.employeeID();//BHPreference.teamCode();
                impoundProductList.get(position).RequestTeamCode = BHPreference.teamCode();
                impoundProductList.get(position).CreateDate = newDate;
                impoundProductList.get(position).CreateBy = BHPreference.employeeID();
                impoundProductList.get(position).LastUpdateDate = newDate;
                impoundProductList.get(position).LastUpdateBy = BHPreference.employeeID();
                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                impoundProductList.get(position).RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
            }

            @Override
            protected void calling() {
                TSRController.addRequestImpoundProductOtherTeam(impoundProductList.get(position), true);


                List<ImpoundProductInfo> impoundProductFilterList;
                if(BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Sale.toString())){
                    impoundProductFilterList = getImpoundProductOtherTeamByStatusRequestOrApproved(BHPreference.organizationCode(), BHPreference.employeeID(), BHPreference.teamCode(), null, false, selectContract.RefNo);
                }else{
                    impoundProductFilterList = new ImpoundProductController().getImpoundProductOtherTeamByStatusRequestOrApprovedForCredit(BHPreference.organizationCode(), BHPreference.employeeID(), BHPreference.teamCode(), null, true, selectContract.RefNo);
                }

                if (impoundProductFilterList == null || impoundProductFilterList.size() == 0) {
                    /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                    TSRController.importContractFromServer(selectContract.OrganizationCode, selectContract.SaleTeamCode, selectContract.RefNo);
                    /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                }
            }

            @Override
            protected void after() {
                impoundAdapter.notifyDataSetChanged();
            }
        }).start();
    }

    public void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public class ProblemAdapter extends BHSpinnerAdapter<ProblemInfo> {
        public ProblemAdapter(Context context, int resource, List<ProblemInfo> objects) {
            super(context, resource, objects);
        }

        protected void setupView(TextView tv, ProblemInfo item) {
            tv.setText(item.ProblemName);
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_next:
                final ImpoundProductInfo selectContract = listCustomer.getCheckedItemPosition() != -1 ? impoundProductList.get(listCustomer.getCheckedItemPosition() - 1) : null;
                if (selectContract != null && selectContract.Status != null) {
                    if (selectContract.Status.equals(ImpoundProductStatus.REQUEST.toString())) {
                        String title = "กรุณาตรวจสถานะ";
                        String message = "สถานะการร้องขอยังไม่ถูกอนุมัติ ";
                        showNoticeDialogBox(title, message);
                    } else if (selectContract.Status.equals(ImpoundProductStatus.APPROVED.toString())) {
                        if(selectContract.IsMigrate){
                            dialogBoxIsMigrate(selectContract);
                        } else {
                            BarcodeCheck();
                        }

                    }
                } else {
                    String title = "กรุณาเลือกรายการ";
                    String message = "ไม่พบการเลือกรายการ";
                    if (selectContract != null){
                        title = "กรุณาตรวจสถานะ";
                        message = "สถานะยังไม่มีการร้องขอ";
                    }
                    showNoticeDialogBox(title, message);
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
//                if (impoundProductList != null && impoundProductList.size() > 0) {
//                    for (int i = 0; i < impoundProductList.size(); i++) {
//                        ImpoundProductInfo im = impoundProductList.get(i);
//                        if (im.Status.equals(ImpoundProductStatus.REQUEST.toString())) {
//                            // STEP_4:Update status approved from Server -> LOCAL DB
//                            refreshStatus(i);
//                        }
//                    }
//                    impoundAdapter.notifyDataSetChanged();
//                }
                break;
            default:
                break;
        }

    }

    private void dialogBoxIsMigrate(final ImpoundProductInfo info){
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle(getResources().getString(titleID()))
                .setMessage("ตรวจพบเป็นข้อมูลที่มาจากการ Migrate ต้องการสแกนสินค้าหรือไม่")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_scan), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        saveLogScanProductSerial(true, info);

                        BarcodeCheck();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_not_scan), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();

                        if(info.ProductSerialNumber != null){
                            ContractInfo result = getContractProductSerialByStatus(info.OrganizationCode, info.ProductSerialNumber);

                            if (result != null) {
                                saveLogScanProductSerial(false, info);

                                ImpoundProductDetailFragment.Data impoundDetail = new ImpoundProductDetailFragment.Data();
                                impoundDetail.Serialnumber = result.ProductSerialNumber;
                                impoundDetail.Name = result.ProductName;
                                impoundDetail.IsImpoundProductOtherTeam = true;
                                impoundDetail.strImpoundProductID = info.ImpoundProductID;
                                ImpoundProductDetailFragment fmProductCheck = BHFragment.newInstance(ImpoundProductDetailFragment.class, impoundDetail);
                                showNextView(fmProductCheck);
                            } else {
                                String title = (getResources().getString(titleID()));;
                                String message = "ไม่พบข้อมูลสินค้า";
                                showNoticeDialogBox(title, message);
                            }
                        } else {
                            String title = (getResources().getString(titleID()));
                            String message = "ไม่พบรหัสสินค้า";
                            showNoticeDialogBox(title, message);
                        }


                    }
                });
        setupAlert.show();
    }

    private void saveLogScanProductSerial (boolean isScanProductSerial, ImpoundProductInfo info){
        LogScanProductSerialInfo logInfo = new LogScanProductSerialInfo();

        logInfo.LogScanProductSerialID = DatabaseHelper.getUUID();
        logInfo.OrganizationCode = BHPreference.organizationCode();
        logInfo.TaskType = LogScanProductSerialController.LogScanProductSerialTaskType.ImpoundProduct.toString();		// ประเภทของการร้องขอ (ขออนุมัติถอดเครื่อง=Impound, ขออนุมัติเปลี่ยนเครื่อง=ChangeProduct, ขออนุมัติเปลี่ยนสัญญา=ChangeContract)
        logInfo.RequestID = info.ImpoundProductID;	                    // GUID การร้องขอ (ขออนุมัติถอดเครื่องใช้-ImpoundProductID, ขออนุมัติเปลี่ยนเครื่องใช้-ChangeProductID, ขออนุมัติเปลี่ยนสัญญาใช้-ChangeContractID)
        logInfo.IsScanProductSerial = isScanProductSerial;		        // บอกว่ามีการ  Scan Product Serial Number หรือไม่
        logInfo.RefNo = info.RefNo;
        logInfo.ProductSerialNumber = info.ProductSerialNumber;		    // หมายเลขเครื่อง
        logInfo.Status = ImpoundProductStatus.APPROVED.toString();		// สถานะคำร้อง (REQUEST=คำร้องขอรออนุมัติ, APPROVED=คำร้องขอที่ถูกอนุมัติแล้วแต่รอดำเนินการ, COMPLETED=คำร้องขอที่ดำเนินการเรียบร้อยแล้ว)
        logInfo.CreateDate = new Date();
        logInfo.CreateBy = BHPreference.employeeID();

        TSRController.addLogScanProductSerial(logInfo, true);
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
        request.master.syncRequestImpoundProductRelated = true;

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
                        bindImpoundProductList(null);
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

    private void BarcodeCheck() {
        BarcodeScanFragment fm = BHFragment.newInstance(BarcodeScanFragment.class, new ScanCallBack() {
            @Override
            public void onResult(BHParcelable data) {
                final BarcodeScanFragment.Result barcodeResult = (BarcodeScanFragment.Result) data;
                final ImpoundProductInfo selectContract = impoundProductList.get(listCustomer.getCheckedItemPosition() - 1);
                if (!barcodeResult.barcode.equals(selectContract.ProductSerialNumber)) {
                    // STEP_5.1:Not Found Product
                    String title = "กรุณาตรวจสอบสินค้า";
                    String message = "รหัสสินค้าไม่ตรงกับรายการที่ท่านเลือก!";
                    showNoticeDialogBox(title, message);
                } else {
                    // STEP_5.1:Found Product
                    (new BackgroundProcess(activity) {
                        @Override
                        protected void calling() {
                            /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                            result = getContractProductSerialByStatus(selectContract.OrganizationCode, selectContract.ProductSerialNumber, ContractStatus.NORMAL.toString());
                            result = getContractProductSerialByStatus(selectContract.OrganizationCode, selectContract.ProductSerialNumber);
                        }

                        @Override
                        protected void after() {
                            if (result != null) {
                                ImpoundProductDetailFragment.Data impoundDetail = new ImpoundProductDetailFragment.Data();
                                impoundDetail.Serialnumber = result.ProductSerialNumber;
                                impoundDetail.Name = result.ProductName;
                                impoundDetail.IsImpoundProductOtherTeam = true;
                                impoundDetail.strImpoundProductID = selectContract.ImpoundProductID;
                                ImpoundProductDetailFragment fmProductCheck = BHFragment.newInstance(ImpoundProductDetailFragment.class, impoundDetail);
                                showNextView(fmProductCheck);
                            } else {
                                String title = "กรุณาตรวจสอบสินค้า";
                                String message = "ไม่พบสินค้ากรุณาทำการสแกนใหม";
                                showNoticeDialogBox(title, message);
                            }

                        }

                    }).start();
                }
            }

            @Override
            public String onNextClick() {
                return ProductScan;
            }

        });
        fm.setTitle(R.string.title_remove);
        fm.setViewTitle(R.string.caption_customer_detail_impound);
        showNextView(fm);

    }

    private void showNoticeDialogBox(String title, String message) {
        Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                .setPositiveButton("ปิด", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        setupAlert.show();
    }
}
