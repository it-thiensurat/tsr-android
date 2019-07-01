package th.co.thiensurat.fragments.products.change;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import th.co.thiensurat.data.controller.ChangeContractController;
import th.co.thiensurat.data.controller.ChangeProductController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.LogScanProductSerialController;
import th.co.thiensurat.data.info.ChangeProductInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.LogScanProductSerialInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.fragments.share.BarcodeScanFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment.ScanCallBack;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.data.info.ProblemInfo.ProblemType;

import static th.co.thiensurat.business.controller.TSRController.getContractByProductSerialNumberByStatus;

public class ChangeProductCustomerListFragment extends BHFragment {

    @InjectView private ListView lvContractList;
    @InjectView private Button btnSearch;
    @InjectView private EditText edtProductSerialNumber;

    private List<ChangeProductInfo> changeProductList;

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());

    private List<ProblemInfo> problemList = TSRController.getProblemByProblemType(BHPreference.organizationCode(), ProblemType.ChangeProduct.toString());

    private ContractAdapter changeProductAdapter;

    private AlertDialog alertDialog;

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_change_product_customer_list;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        int[] buttons;
        if (isCredit) {
            buttons = new int[]{R.string.button_update_list};
        } else {
            buttons = new int[]{};
        }
        return buttons;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_change_product;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        int layoutListHeader;
        int layoutListAdapter;

        if (isCredit) {
            layoutListHeader = R.layout.list_contract_status_header;
            layoutListAdapter = R.layout.list_contract_status_item;
        } else {
            layoutListHeader = R.layout.list_contract_header;
            layoutListAdapter = R.layout.list_contract_item;
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(layoutListHeader, lvContractList, false);
        lvContractList.addHeaderView(header, null, false);

        if (changeProductList == null) {
            changeProductList = new ArrayList<ChangeProductInfo>();
        }

        changeProductAdapter = new ContractAdapter(activity, layoutListAdapter, changeProductList);
        lvContractList.setAdapter(changeProductAdapter);

        lvContractList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(alertDialog == null){
                    final ChangeProductInfo changeProduct = (ChangeProductInfo) lvContractList.getItemAtPosition(i);

                    if (isCredit) {
                        //-- (A) ฝ่ายเก็บเงิน --> ทำ REQUEST หรือ ACTION
                        //-- Fixed - Search From Server ของ ChangeProduct ไม่แสดงผล เนื่องจากสถานะของการเปลี่ยนสัญญามีชื่อ Field เดียวกับสถานะของสัญญา ทำให้สับสนได้
                        if (changeProduct.Status == null) {
                            //-- (A-1) REQUEST --> ขึ้น Dialog ระบุสาเหตุที่ขอ+รายละเอียดที่ขอ
                            displayAlertDialog(changeProduct.RefNo, 0, "");
                        } else if (changeProduct.Status.equals(ChangeProductController.ChangeProductStatus.APPROVED.toString())) {
                            //-- (A-2) ACTION
                            BHPreference.setRefNo(changeProduct.RefNo);
                            if(changeProduct.IsMigrate){
                                dialogBoxIsMigrate(changeProduct);
                            } else {
                                ChangeProductDetailFragment.imageID = "";
                                scanBarcode(changeProduct);
                            }
                        }
                    } else {
                        //-- (B) ฝ่ายขาย --> ทำ 3 step รวดเดียว คือ REQUEST + APPROVED + ACTION
                        if(changeProduct.IsMigrate){
                            dialogBoxIsMigrate(changeProduct);
                        } else {
                            ChangeProductDetailFragment.imageID = "";
                            BHPreference.setRefNo(changeProduct.RefNo);
                            scanBarcode(changeProduct);
                        }
                    }
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (edtProductSerialNumber.getText().toString().trim().isEmpty()) {
                    BHUtilities.alertDialog(activity, "คำเตือน", "กรุณากรอกข้อมูลที่ต้องการค้นหา").show();
                } else {
                    getContractForSearch(edtProductSerialNumber.getText().toString());
                }
            }
        });

        getContractForSearch(null);
    }

    private void dialogBoxIsMigrate(final ChangeProductInfo info){
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle(getResources().getString(titleID()))
                .setMessage("ตรวจพบเป็นข้อมูลที่มาจากการ Migrate ต้องการสแกนสินค้าหรือไม่")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_scan), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        saveLogScanProductSerial(true, info);

                        ChangeProductDetailFragment.imageID = "";
                        BHPreference.setRefNo(info.RefNo);
                        scanBarcode(info);

                        alertDialog = null;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_not_scan), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if(info.ProductSerialNumber != null){
                            ContractInfo result = getContractByProductSerialNumberByStatus(info.OrganizationCode, info.ProductSerialNumber);

                            if(result != null){
                                saveLogScanProductSerial(false, info);

                                ChangeProductCheckFragment.Data dataCheck = new ChangeProductCheckFragment.Data();
                                dataCheck.OldProductSerialNumber = result.ProductSerialNumber;
                                dataCheck.OldProductID = result.ProductID;
                                dataCheck.OldProductName = result.ProductName;
                                dataCheck.statusPage = false;

                                ChangeProductCheckFragment fmProductCheck = BHFragment.newInstance(ChangeProductCheckFragment.class, dataCheck);
                                showNextView(fmProductCheck);
                            }  else {
                                String title = (getResources().getString(titleID()));;
                                String message = "ไม่พบข้อมูลสินค้า";
                                showNoticeDialogBox(title, message);
                            }
                        } else {
                            String title = (getResources().getString(titleID()));
                            String message = "ไม่พบรหัสสินค้า";
                            showNoticeDialogBox(title, message);
                        }

                        alertDialog = null;
                    }
                });
        alertDialog = setupAlert.show();
    }

    private void saveLogScanProductSerial (boolean isScanProductSerial, ChangeProductInfo info){
        LogScanProductSerialInfo logInfo = new LogScanProductSerialInfo();

        logInfo.LogScanProductSerialID = DatabaseHelper.getUUID();
        logInfo.OrganizationCode = BHPreference.organizationCode();
        logInfo.TaskType = LogScanProductSerialController.LogScanProductSerialTaskType.ChangeProduct.toString();		// ประเภทของการร้องขอ (ขออนุมัติถอดเครื่อง=Impound, ขออนุมัติเปลี่ยนเครื่อง=ChangeProduct, ขออนุมัติเปลี่ยนสัญญา=ChangeContract)
        logInfo.RequestID = info.ChangeProductID;	                    // GUID การร้องขอ (ขออนุมัติถอดเครื่องใช้-ImpoundProductID, ขออนุมัติเปลี่ยนเครื่องใช้-ChangeProductID, ขออนุมัติเปลี่ยนสัญญาใช้-ChangeContractID)
        logInfo.IsScanProductSerial = isScanProductSerial;		        // บอกว่ามีการ  Scan Product Serial Number หรือไม่
        logInfo.RefNo = info.RefNo;
        logInfo.ProductSerialNumber = info.ProductSerialNumber;		    // หมายเลขเครื่อง
        logInfo.Status = info.Status == null ? ChangeProductController.ChangeProductStatus.COMPLETED.toString() : ChangeProductController.ChangeProductStatus.APPROVED.toString();		// สถานะคำร้อง (REQUEST=คำร้องขอรออนุมัติ, APPROVED=คำร้องขอที่ถูกอนุมัติแล้วแต่รอดำเนินการ, COMPLETED=คำร้องขอที่ดำเนินการเรียบร้อยแล้ว)
        logInfo.CreateDate = new Date();
        logInfo.CreateBy = BHPreference.employeeID();

        TSRController.addLogScanProductSerial(logInfo, true);
    }


    private void getContractForSearch(final String Search) {
        (new BackgroundProcess(activity) {
            List<ChangeProductInfo> list;

            @Override
            protected void before() {
                    changeProductList.clear();
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                if (isCredit) {
                    /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                    changeProductList = TSRController.getChangeProductListForCredit(BHPreference.organizationCode(), ContractStatus.NORMAL.toString(), "");
                    //changeProductList = TSRController.getChangeProductListForCredit(BHPreference.organizationCode(), Search, null, BHPreference.employeeID());
                    list = TSRController.getChangeProductListForCredit(BHPreference.organizationCode(), Search, null, BHPreference.employeeID());


                } else {
                    /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                    changeProductList = TSRController.getChangeProductList(BHPreference.organizationCode(), BHPreference.teamCode(), ContractStatus.NORMAL.toString(), "");
                    list = TSRController.getChangeProductList(BHPreference.organizationCode(), BHPreference.teamCode(), Search, null);
                }

                if(list != null){
                    changeProductList.addAll(list);
                }
            }

            @Override
            protected void after() {
                lvContractList.clearChoices();
                changeProductAdapter.notifyDataSetChanged();

                if(changeProductList == null || changeProductList.size() == 0){
                    BHUtilities.alertDialog(activity, "คำเตือน", "ไม่พบข้อมูล").show();
                }
            }

        }).start();
    }

    private void scanBarcode(final ChangeProductInfo changeProduct) {
        BarcodeScanFragment fm = BHFragment.newInstance(
                BarcodeScanFragment.class, new ScanCallBack() {

                    @Override
                    public void onResult(BHParcelable data) {
                        // TODO Auto-generated method stub

                        final BarcodeScanFragment.Result barcodeResult = (BarcodeScanFragment.Result) data;
                        (new BackgroundProcess(activity) {
                            ContractInfo result = null;
                            ContractInfo cont = null;

                            @Override
                            protected void calling() {
                                // TODO Auto-generated method stub
                                /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                                cont = TSRController.getContractByRefNo(changeProduct.OrganizationCode, changeProduct.RefNo);
                                if (cont == null) {
                                    TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), changeProduct.RefNo);
                                    cont = TSRController.getContractByRefNo(changeProduct.OrganizationCode, changeProduct.RefNo);
                                }
                                /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/

                                /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                                result = getContractByProductSerialNumberByStatus(changeProduct.OrganizationCode, barcodeResult.barcode, ContractStatus.NORMAL.toString());
                                if (barcodeResult.barcode.equals(cont.ProductSerialNumber)) {
                                    result = getContractByProductSerialNumberByStatus(changeProduct.OrganizationCode, barcodeResult.barcode);
                                }
                            }

                            @Override
                            protected void after() {
                                if (result != null) {

                                    ChangeProductCheckFragment.Data dataCheck = new ChangeProductCheckFragment.Data();
                                    dataCheck.OldProductSerialNumber = result.ProductSerialNumber;
                                    dataCheck.OldProductID = result.ProductID;
                                    dataCheck.OldProductName = result.ProductName;
                                    dataCheck.statusPage = false;

                                    ChangeProductCheckFragment fmProductCheck = BHFragment.newInstance(ChangeProductCheckFragment.class, dataCheck);
                                    showNextView(fmProductCheck);

                                } else {
                                    final String title = "ChangeProduct";
                                    String message = "สินค้าไม่ถูกต้อง";
                                    showNoticeDialogBox(title, message);
                                }
                            }
                        }).start();

                    }

                    @Override
                    public String onNextClick() {
                        return changeProduct.ProductSerialNumber;
                    }

                });
        fm.setTitle(R.string.title_change_product);
        fm.setViewTitle(R.string.title_scan_old_product);
        showNextView(fm);
    }

    private void showNoticeDialogBox(final String title, final String message) {
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity);
        setupAlert.setTitle(title);
        setupAlert.setMessage(message);
        setupAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // ??
                    }
                });
        setupAlert.show();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_update_list:
                syncEditContractRelated();
                break;
            default:
                break;
        }
    }

    private class SynchronizeReceiver extends BroadcastReceiver {
        private SynchronizeReceiver instance;

        private ProgressDialog dialog;
        private SynchronizeService.SynchronizeResult result;
        private boolean isProcessing;

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
            // TODO Auto-generated method stub

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
//                        showNextView(new ChangeProductCustomerListFragment());
                        activity.showView(new ChangeProductCustomerListFragment());
                    }

                    stop();
                }

            }
        }

    }

    private void syncEditContractRelated() {
        TransactionService.stopService(activity);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
        request.master.syncRequestChangeProductRelated = true;

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);
    }

    private void displayAlertDialog(final String RefNo,final int ProblemPosition,final String ProblemDetail) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.change_product_dialog, null);
        final Spinner spnProblem = (Spinner) alertLayout.findViewById(R.id.spnProblem);
        final EditText editTextProblemDetail = (EditText) alertLayout.findViewById(R.id.editTextProblemDetail);

        bindProblem(spnProblem);

        if(ProblemPosition != 0){
            spnProblem.setSelection(ProblemPosition);
        }

        if (!ProblemDetail.equals("")) {
            editTextProblemDetail.setText(ProblemDetail);
        }

        Builder setupAlert;
        setupAlert = new Builder(activity)
                .setTitle("กรุณาใส่ข้อมูลการร้องขอเปลี่ยนเครื่อง")
                .setView(alertLayout)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String problemDetail = editTextProblemDetail.getText().toString();

                        if (spnProblem.getSelectedItemPosition() == 0) {
                            dialog.cancel();
                            alertDialog(RefNo, "กรุณาเลือกสาเหตุการเปลี่ยนเครื่อง", spnProblem.getSelectedItemPosition(), problemDetail);
                        } else {
                            if (problemList.get(spnProblem.getSelectedItemPosition() - 1).ProblemName.trim().equals("อื่น ๆ") && problemDetail.toString().trim().equals("")) {
                                dialog.cancel();
                                alertDialog(RefNo, "กรณีเลือกสาเหตุขอเปลี่ยนเครื่อง อื่นๆ กรุณากรอกที่ช่อง  รายละเอียด", spnProblem.getSelectedItemPosition(), problemDetail);
                            } else {
                                saveChangeProductStatusREQUEST(RefNo, spnProblem.getSelectedItemPosition(), problemDetail);
                            }
                        }

                        alertDialog = null;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        alertDialog = null;
                    }
                });
        alertDialog = setupAlert.show();
    }

    private void bindProblem(Spinner spnProblem) {
        List<String> problem = new ArrayList<String>();
        problem.add("");
        for (ProblemInfo item : problemList) {
            problem.add(String.format(item.ProblemName));
        }
        BHSpinnerAdapter<String> arrayProblem = new BHSpinnerAdapter<String>(activity, problem);

        spnProblem.setAdapter(arrayProblem);
    }

    private void alertDialog(final String RefNo, String Message, final int ProblemPosition, final String ProblemDetail) {
        AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle("แจ้งเตือน")
                .setMessage(Message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        displayAlertDialog(RefNo, ProblemPosition, ProblemDetail);
                    }
                });
        setupAlert.show();
    }

    private void saveChangeProductStatusREQUEST(final String RefNo,final int ProblemPosition, final String ProblemDetail) {

        (new BackgroundProcess(activity) {
            ChangeProductInfo changeProduct = new ChangeProductInfo();
            Date newDate = new Date();

            @Override
            protected void before() {
                changeProduct.ChangeProductID = DatabaseHelper.getUUID();
                changeProduct.OrganizationCode = BHPreference.organizationCode();
                changeProduct.RefNo = RefNo;
                //changeProduct.OldProductSerialNumber = dataProduct.OldProductSerialNumber;
                //changeProduct.NewProductSerialNumber = dataProduct.NewProductSerialNumber;
                changeProduct.Status = ChangeProductController.ChangeProductStatus.REQUEST.toString();
                changeProduct.RequestProblemID = problemList.get(ProblemPosition - 1).ProblemID;
                changeProduct.RequestDetail = ProblemDetail;
                changeProduct.RequestDate = newDate;
                changeProduct.RequestBy = BHPreference.employeeID();
                changeProduct.RequestTeamCode = BHPreference.teamCode();
                //changeProduct.ApprovedDate = changeProduct.RequestDate;
                //changeProduct.ApproveDetail = changeProduct.RequestDetail;
                //changeProduct.ApprovedBy = changeProduct.RequestBy;
                //changeProduct.ResultProblemID = changeProduct.RequestProblemID;
                //changeProduct.ResultDetail = changeProduct.RequestDetail;
                //changeProduct.EffectiveDate = changeProduct.RequestDate;
                //changeProduct.EffectiveBy = changeProduct.RequestBy;
                //changeProduct.ChangeProductPaperID = TSRController.getAutoGenerateDocumentID(TSRController.DocumentGenType.ChangeProduct.toString(), BHPreference.SubTeamCode(), BHPreference.saleCode());
                changeProduct.CreateDate = newDate;
                changeProduct.CreateBy = BHPreference.employeeID();
                changeProduct.LastUpdateDate = newDate;
                changeProduct.LastUpdateBy = BHPreference.employeeID();

                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                changeProduct.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();

            }

            @Override
            protected void calling() {
                TSRController.addChangeProduct(changeProduct, true);
                /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                List<ChangeProductInfo> list;

                if (isCredit) {
                    /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                    changeProductList = TSRController.getChangeProductListForCredit(BHPreference.organizationCode(), ContractStatus.NORMAL.toString(), "");
                    //changeProductList = TSRController.getChangeProductListForCredit(BHPreference.organizationCode(), Search, null, BHPreference.employeeID());
                    list = TSRController.getChangeProductListForCredit(BHPreference.organizationCode(), null, changeProduct.RefNo, BHPreference.employeeID());


                } else {
                    /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                    changeProductList = TSRController.getChangeProductList(BHPreference.organizationCode(), BHPreference.teamCode(), ContractStatus.NORMAL.toString(), "");
                    list = TSRController.getChangeProductList(BHPreference.organizationCode(), BHPreference.teamCode(), null, changeProduct.RefNo);
                }

                if(list == null){
                    TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), changeProduct.RefNo);
                }
                /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
            }

            @Override
            protected void after() {
                activity.showView(new ChangeProductCustomerListFragment());
            }
        }).start();
    }
}
