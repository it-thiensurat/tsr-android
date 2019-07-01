package th.co.thiensurat.fragments.contracts.change;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.ContractAdapter;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ChangeContractController;
import th.co.thiensurat.data.controller.ChangeContractController.ChangeContractStatus;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.LogScanProductSerialController;
import th.co.thiensurat.data.controller.ManualDocumentController;
import th.co.thiensurat.data.controller.ProblemController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.LogScanProductSerialInfo;
import th.co.thiensurat.data.info.ManualDocumentInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.ReceiptInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.document.manual.ManualDocumentDetailFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment.ScanCallBack;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.service.data.GetContractForAvailableChangeContractForCreditInputInfo;
import th.co.thiensurat.service.data.GetContractOfTeamAndSubTeamForAvailableChangeContractForSaleInputInfo;
import th.co.thiensurat.service.data.GetContractOfTeamForAvailableChangeContractForSaleInputInfo;

public class ChangeContractListFragment extends BHFragment implements OnItemClickListener {

    public static String FRAGMENT_CHANGE_CONTRACT_LIST_TAG = "change_contract_list_tag";

    private final String CONTRACT_LIST_KEY = "CONTRACT_LIST";
    private final String TAG = "ChangeContractListFragment";

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());

    @InjectView
    private ListView lvCustomerList;
    @InjectView
    private Button btnSerch;
    @InjectView
    private EditText etSerialNumber;

    private List<ChangeContractInfo> contractList;
    private ContractAdapter changeContractAdapter;
    private BarcodeScanFragment fmBarcode;

    private List<String> positionCode = Arrays.asList(BHPreference.PositionCode().split(","));

    private AlertDialog alertDialog;

    @Override
    public String fragmentTag() {
        // TODO Auto-generated method stub
        return FRAGMENT_CHANGE_CONTRACT_LIST_TAG;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_change_contract_customer_list;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_change_contract;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_update_list};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        /*if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CONTRACT_LIST_KEY)) {
                contractList = savedInstanceState
                        .getParcelableArrayList(CONTRACT_LIST_KEY);
            }
        }*/

        int layoutListHeader;
        int layoutListAdapter;
        int[] buttons;
        if (isCredit) {
            layoutListHeader = R.layout.list_contract_status_header;
            layoutListAdapter = R.layout.list_contract_status_item;
            buttons = new int[]{R.string.button_update_list};
        } else {
            if (positionCode.contains("SaleLeader")) {
                layoutListHeader = R.layout.list_contract_header;
                layoutListAdapter = R.layout.list_contract_item;
                buttons = new int[]{R.string.button_back};
            } else {
                layoutListHeader = R.layout.list_contract_status_header;
                layoutListAdapter = R.layout.list_contract_status_item;
                buttons = new int[]{R.string.button_back, R.string.button_update_list};
            }
        }

        // Set Buttons
        activity.setupProcessButtons(this, buttons);
        // Set List Layout
        LayoutInflater li = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) li.inflate(layoutListHeader, lvCustomerList,
                false);
        lvCustomerList.addHeaderView(header, null, false);

        contractList = new ArrayList<ChangeContractInfo>();
        changeContractAdapter = new ContractAdapter(activity, layoutListAdapter, contractList);
        lvCustomerList.setAdapter(changeContractAdapter);
        lvCustomerList.setOnItemClickListener(this);

        btnSerch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSerialNumber.getText().toString().trim().isEmpty()) {
                    BHUtilities.alertDialog(activity, "คำเตือน", "กรุณากรอกข้อมูลที่ต้องการค้นหา").show();
                } else {
                    getContractList(etSerialNumber.getText().toString());
                }
            }
        });

        // Set data
        getContractList(null);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CONTRACT_LIST_KEY,
                (ArrayList<ChangeContractInfo>) contractList);
    }

    public void getContractList(final String SearchText) {
        new BackgroundProcess(activity) {
            List<ChangeContractInfo> contractListFilter = null;

            @Override
            protected void before() {
                contractList.clear();
            }

            @Override
            protected void calling() {
                if (isCredit) {
                    contractListFilter = new ChangeContractController().getContractAndChangeContractForAvailableForCreditAndSearchText(BHPreference.organizationCode(), SearchText, BHPreference.employeeID(), null);
                } else {
                    if (positionCode.contains("SaleLeader")) { //หัวหน้าทีม
                        contractListFilter = new ChangeContractController().getContractAndChangeContractForAvailableBySaleTeamCodeAndSearchText(
                                BHPreference.organizationCode(), BHPreference.teamCode(), SearchText, null);
                    } else { //หัวหน้าหน่วย
                        contractListFilter = new ChangeContractController().getContractAndChangeContractForAvailableBySaleTeamCodeAndSubTeamCodeAndSearchText(
                                BHPreference.organizationCode(), BHPreference.SubTeamCode(), SearchText, null);
                    }
                }


                // Search Local
                if (contractListFilter != null && contractListFilter.size() > 0) {
                    contractList.addAll(contractListFilter);
                } else if (SearchText != null) {
                    // STEP :Search SERVER

                    List<ContractInfo> contList = null;
                    if (isCredit) {
                        GetContractForAvailableChangeContractForCreditInputInfo contractForCreditInput = new GetContractForAvailableChangeContractForCreditInputInfo();
                        contractForCreditInput.SearchText = SearchText;
                        contractForCreditInput.OrganizationCode = BHPreference.organizationCode();
                        contractForCreditInput.EmployeeID = BHPreference.employeeID();

                        contList = TSRService.GetContractForAvailableChangeContractForCredit(contractForCreditInput, false).Info;
                    } else {
                        if (positionCode.contains("SaleLeader")) { //หัวหน้าทีม
                            GetContractOfTeamForAvailableChangeContractForSaleInputInfo contractInput = new GetContractOfTeamForAvailableChangeContractForSaleInputInfo();
                            contractInput.SearchText = SearchText;
                            contractInput.OrganizationCode = BHPreference.organizationCode();
                            contractInput.SaleTeamCode = BHPreference.teamCode();

                            contList = TSRService.GetContractOfTeamForAvailableChangeContractForSale(contractInput, false).Info;
                        } else { //หัวหน้าหน่วย
                            GetContractOfTeamAndSubTeamForAvailableChangeContractForSaleInputInfo contractInput = new GetContractOfTeamAndSubTeamForAvailableChangeContractForSaleInputInfo();
                            contractInput.SearchText = SearchText;
                            contractInput.OrganizationCode = BHPreference.organizationCode();
                            contractInput.SaleTeamCode = BHPreference.teamCode();
                            contractInput.SaleSubTeamCode = BHPreference.SubTeamCode();

                            contList = TSRService.GetContractOfTeamAndSubTeamForAvailableChangeContractForSale(contractInput, false).Info;
                        }
                    }

                    if (contList != null && contList.size() > 0) {
                        for (ContractInfo cont : contList) {
                            ChangeContractInfo fakeChangeContract = new ChangeContractInfo();
                            fakeChangeContract.RefNo = cont.RefNo;
                            fakeChangeContract.OrganizationCode = cont.OrganizationCode;
                            fakeChangeContract.CONTNO = cont.CONTNO;
                            fakeChangeContract.ProductSerialNumber = cont.ProductSerialNumber;
                            fakeChangeContract.CustomerFullName = cont.CustomerFullName;
                            fakeChangeContract.IsMigrate = cont.IsMigrate;

                            contractList.add(fakeChangeContract);

                            // ADD Import Contract From Server
                            //** Fixed - [BHPRJ00301-4073]
//                                    TSRController.importContractFromServer(cont.OrganizationCode, cont.SaleTeamCode, cont.RefNo, true, true, true);
                            /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
//                                    TSRController.importContractFromServer(cont.OrganizationCode, cont.SaleTeamCode, cont.RefNo);
                            /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                        }
                    }
                }
            }

            @Override
            protected void after() {
                lvCustomerList.clearChoices();
                changeContractAdapter.notifyDataSetChanged();

                if (contractList == null || contractList.size() == 0) {
                    String title = "ChangeContract";
                    String message = "ไม่พบข้อมูล";
                    showNoticeDialogBox(title, message);
                }
            }
        }.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(alertDialog == null) {
            final ChangeContractInfo cont = contractList.get(position - 1);

            if (cont.Status == null) {
                //-- (A) New Request
//            Log.e(TAG, "Change contract");
                /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                (new BackgroundProcess(activity) {
                    List<ChangeContractInfo> contractListFilter = null;

                    @Override
                    protected void calling() {
                        if (isCredit) {
                            contractListFilter = new ChangeContractController().getContractAndChangeContractForAvailableForCreditAndSearchText(BHPreference.organizationCode(), null, BHPreference.employeeID(), cont.RefNo);
                        } else {
                            if (positionCode.contains("SaleLeader")) { //หัวหน้าทีม
                                contractListFilter = new ChangeContractController().getContractAndChangeContractForAvailableBySaleTeamCodeAndSearchText(
                                        BHPreference.organizationCode(), BHPreference.teamCode(), null, cont.RefNo);
                            } else { //หัวหน้าหน่วย
                                contractListFilter = new ChangeContractController().getContractAndChangeContractForAvailableBySaleTeamCodeAndSubTeamCodeAndSearchText(
                                        BHPreference.organizationCode(), BHPreference.SubTeamCode(), null, cont.RefNo);
                            }
                        }

                        if (contractListFilter == null) {
                            TSRController.importContractFromServer(cont.OrganizationCode, null, cont.RefNo);
                        }
                    }

                    @Override
                    protected void after() {

                        if (cont.IsMigrate) {
                            dialogBoxIsMigrate(cont);
                        } else {
                            BarcodeScanFragment fmBarcode = BHFragment.newInstance(BarcodeScanFragment.class, new ScanBarcodeCallBack(cont.OrganizationCode, cont.ProductSerialNumber));
                            fmBarcode.setTitle(R.string.title_change_contract);
                            fmBarcode.setViewTitle(R.string.title_scan_product_serial);
                            showNextView(fmBarcode);
                        }
                    }
                }).start();
                /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
            } else if (cont.Status.equals("APPROVED")) {
                //-- (B) Action

                Builder setupAlert;
                setupAlert = new AlertDialog.Builder(activity)
                        .setTitle("แจ้งเตือน เปลี่ยนสัญญา")
                        .setMessage("ต้องการบันทึกการเปลี่ยนสัญญา")
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //updateChangeContract(cont);

                                ContractInfo OldContractInfo = TSRController.getContractByRefNoForChangeContract(BHPreference.organizationCode(), cont.OldSaleID);
                                ManualDocumentDetailFragment.Data data1 = new ManualDocumentDetailFragment.Data();
                                data1.DocumentNumber = cont.NewSaleID;//dataContract.newContract.RefNo;
                                data1.DocumentNo = OldContractInfo.CONTNO;//dataContract.newContract.CONTNO;
                                data1.processType = SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract;
                                data1.DocumentType = DocumentHistoryController.DocumentType.Contract.toString();
                                data1.changeContractInfo = cont;
                                ManualDocumentDetailFragment fmManualDocContract = BHFragment.newInstance(ManualDocumentDetailFragment.class, data1);
                                showNextView(fmManualDocContract);
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
        }
    }

    private void dialogBoxIsMigrate(final ChangeContractInfo info){
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle(getResources().getString(titleID()))
                .setMessage("ตรวจพบเป็นข้อมูลที่มาจากการ Migrate ต้องการสแกนสินค้าหรือไม่")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_scan), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        saveLogScanProductSerial(true, info);

                        BarcodeScanFragment fmBarcode = BHFragment.newInstance(BarcodeScanFragment.class, new ScanBarcodeCallBack(info.OrganizationCode, info.ProductSerialNumber));
                        fmBarcode.setTitle(R.string.title_change_contract);
                        fmBarcode.setViewTitle(R.string.title_scan_product_serial);
                        showNextView(fmBarcode);
                        alertDialog = null;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_not_scan), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();

                        if (info.ProductSerialNumber != null) {
                            saveLogScanProductSerial(false, info);

                            ChangeContractOldContractDetailFragment.Data data1 = new ChangeContractOldContractDetailFragment.Data();
                            data1.oldRefNo = info.RefNo;
                            ChangeContractOldContractDetailFragment fmOldContract = BHFragment.newInstance(ChangeContractOldContractDetailFragment.class, data1);
                            showNextView(fmOldContract);
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

    private void saveLogScanProductSerial (boolean isScanProductSerial, ChangeContractInfo info){
        LogScanProductSerialInfo logInfo = new LogScanProductSerialInfo();

        logInfo.LogScanProductSerialID = DatabaseHelper.getUUID();
        logInfo.OrganizationCode = BHPreference.organizationCode();
        logInfo.TaskType = LogScanProductSerialController.LogScanProductSerialTaskType.ChangeContract.toString();		// ประเภทของการร้องขอ (ขออนุมัติถอดเครื่อง=Impound, ขออนุมัติเปลี่ยนเครื่อง=ChangeProduct, ขออนุมัติเปลี่ยนสัญญา=ChangeContract)
        logInfo.RequestID = info.ChangeContractID;	                    // GUID การร้องขอ (ขออนุมัติถอดเครื่องใช้-ImpoundProductID, ขออนุมัติเปลี่ยนเครื่องใช้-ChangeProductID, ขออนุมัติเปลี่ยนสัญญาใช้-ChangeContractID)
        logInfo.IsScanProductSerial = isScanProductSerial;		        // บอกว่ามีการ  Scan Product Serial Number หรือไม่
        logInfo.RefNo = info.RefNo;
        logInfo.ProductSerialNumber = info.ProductSerialNumber;		    // หมายเลขเครื่อง
        logInfo.Status = ChangeContractStatus.REQUEST.toString();		// สถานะคำร้อง (REQUEST=คำร้องขอรออนุมัติ, APPROVED=คำร้องขอที่ถูกอนุมัติแล้วแต่รอดำเนินการ, COMPLETED=คำร้องขอที่ดำเนินการเรียบร้อยแล้ว)
        logInfo.CreateDate = new Date();
        logInfo.CreateBy = BHPreference.employeeID();

        TSRController.addLogScanProductSerial(logInfo, true);
    }

    private class ScanBarcodeCallBack extends ScanCallBack {
        private String orgCodeSelected, serialNoSelected;

        ScanBarcodeCallBack(final String orgCodeSelected, final String serialNoSelected) {
            this.orgCodeSelected = orgCodeSelected;
            this.serialNoSelected = serialNoSelected;
        }

        @Override
        public void onResult(BHParcelable data) {
            checkBarcode(orgCodeSelected, serialNoSelected, (BarcodeScanFragment.Result) data);
        }

        @Override
        public String onNextClick() {
            return serialNoSelected;
        }
    }

    public void checkBarcode(final String orgCode, final String serialNo, final BarcodeScanFragment.Result barcodeResult) {
        (new BackgroundProcess(activity) {

            ContractInfo resultContract;

            @Override
            protected void before() {
            }

            @Override
            protected void calling() {
                /*** [START] :: Fixed - [BHPROJ-0024-890] :: [Android-ระบบเปลี่ยนสัญญา] สัญญาที่จ่ายชำระเสร็จสิ้นแล้ว (Status='F') จะไม่สามารถนำมาร้องขอเปลี่ยนสัญญาได้  ***/
                //String Status = TextUtils.join(",", new String[]{("\'" + ContractStatus.NORMAL.toString() + "\'"), ("\'" + ContractStatus.F.toString() + "\'")});
                String Status = TextUtils.join(",", new String[]{("\'" + ContractStatus.NORMAL.toString() + "\'")});
                /*** [END] :: Fixed - [BHPROJ-0024-890] :: [Android-ระบบเปลี่ยนสัญญา] สัญญาที่จ่ายชำระเสร็จสิ้นแล้ว (Status='F') จะไม่สามารถนำมาร้องขอเปลี่ยนสัญญาได้ ***/

                if (isCredit) {
                    resultContract = getContractBySerialNoForCredit(orgCode, barcodeResult.barcode, Status);
                } else {
                    resultContract = getContractBySerialNo(orgCode, barcodeResult.barcode, "");
                }
            }

            @Override
            protected void after() {
                try {
                    if (resultContract != null) {
                        ChangeContractOldContractDetailFragment.Data data1 = new ChangeContractOldContractDetailFragment.Data();
                        data1.oldRefNo = resultContract.RefNo;
                        if (resultContract.ProductSerialNumber
                                .equals(serialNo)) {
                            ChangeContractOldContractDetailFragment fmOldContract = BHFragment.newInstance(
                                    ChangeContractOldContractDetailFragment.class, data1);
                            showNextView(fmOldContract);
                        } else {
                            String title = "กรุณาตรวจสอบสินค้า";
                            String message = "รหัสสินค้าไม่ตรงกับรายการที่ท่านเลือก!";
                            alertDialog(title, message);
                        }
                    } else {
                        String title = "กรุณาตรวจสอบสินค้า";
                        String message = "ไม่พบสินค้า!";
                        alertDialog(title, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void alertDialog(String title, String message) {
        Builder setupAlert = new AlertDialog.Builder(
                activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        getResources().getString(R.string.dialog_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
        setupAlert.show();
    }

    //    public void requestChangeContract(int position) {
//        LayoutInflater inflater = activity.getLayoutInflater();
//        View dialogLayout = inflater.inflate(R.layout.change_contract_custom_dialog, null);
//        final String title = "ยืนยันการส่งคำร้องขอเปลี่ยนสัญญา";
//        final String message = String.format("สถานะ : %s \nหมายเลขเครื่อง : %s \nสินค้า : %s " +
//                        "\nเลขที่สัญญา : %s ",
//                BHUtilities.trim(ImpoundProductController.ImpoundProductStatus.REQUEST.toString()),
//                contractList.get(position).ProductSerialNumber, contractList.get(position).ProductName
//                , contractList.get(position).CONTNO);
////        Button button1 = (Button)dialogLayout.findViewById(R.id.button1);
////        button1.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View v) {
////                Toast.makeText(activity.getApplicationContext()
////                        , "Close dialog", Toast.LENGTH_SHORT);
////                dialog.cancel();
////            }
////        });
////
////        TextView textView1 = (TextView)dialog.findViewById(R.id.textView1);
////        textView1.setText("Custom Dialog");
////        TextView textView2 = (TextView)dialog.findViewById(R.id.textView2);
////        textView2.setText("Try it yourself");
//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
//        dialog.setTitle(title);
//        dialog.setMessage(message);
//        dialog.setView(dialogLayout);
//        dialog.setCancelable(true);
//        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                if (selectContract.Status == null) {
//                    (new BackgroundProcess(activity) {
//                        //  ImpoundProductInfo addimpound = new ImpoundProductInfo();
//
//                        @Override
//                        protected void before() {
//                            // TODO Auto-generated method stub
//                            impoundProductList.get(position).ImpoundProductID = DatabaseHelper.getUUID();
//                            impoundProductList.get(position).OrganizationCode = BHPreference.organizationCode();
//                            impoundProductList.get(position).RefNo = selectContract.RefNo;
//                            impoundProductList.get(position).Status = ImpoundProductController.ImpoundProductStatus.REQUEST.toString();
//                            //impoundProductList.get(position).RequestProblemID = "";
//                            //impoundProductList.get(position).RequestDetail = "";
//                            impoundProductList.get(position).RequestDate = new Date();
//                            impoundProductList.get(position).RequestBy = BHPreference.employeeID();//BHPreference.teamCode();
//                            //impoundProductList.RequestTeamCode = selectContract.SaleTeamCode;
//                            impoundProductList.get(position).RequestTeamCode = BHPreference.teamCode();
//                            //impoundProductList.get(position).ApproveDetail = "";
//                            //impoundProductList.get(position).ApprovedDate = new Date();
//                            //impoundProductList.get(position).ApprovedBy = "";
//                            //impoundProductList.get(position).ResultProblemID = "";
//                            //impoundProductList.get(position).ResultDetail = "";
//                            //impoundProductList.get(position).EffectiveDate = new Date();
//                            //impoundProductList.get(position).EffectiveBy = "";
//                            //impoundProductList.get(position).ImpoundProductPaperID = "";
//                            impoundProductList.get(position).CreateDate = new Date();
//                            impoundProductList.get(position).CreateBy = BHPreference.employeeID();
//                            impoundProductList.get(position).LastUpdateDate = new Date();
//                            impoundProductList.get(position).LastUpdateBy = BHPreference.employeeID();
//                        }
//
//                        @Override
//                        protected void calling() {
//                            // TODO Auto-generated method stub
//                            GetImpoundProductByImpoundProductIDInputInfo input = new GetImpoundProductByImpoundProductIDInputInfo();
//                            input.ImpoundProductID = impoundProductList.get(position).ImpoundProductID;
//                            if (TSRService.getImpoundProductByImpoundProductID(input, false).Info == null) {
//                                Log.e("ImpoundProductListOtherTeamFragment", "TSRService & LOCAL-DB::addRequestImpoundProductOtherTeam");
//                                TSRController.addRequestImpoundProductOtherTeam(impoundProductList.get(position), false);
//                            } else {
//                                Log.e("ImpoundProductListOtherTeamFragment", "LOCAL-DB::addRequestImpoundProductOtherTeam");
//                                new ImpoundProductController().addImpoundProductOtherTeam(impoundProductList.get(position));
    //                            }
//
//                            if (webservice) {
//                                // STEP_3.1:IMPORT CONTRACT, ADDRESS, PRODUCT STOCK
//                                TSRController.importContractFromServer(selectContract.OrganizationCode, selectContract.SaleTeamCode, selectContract.RefNo, true, true, true);
//                            }
//                        }
//
//                        @Override
//                        protected void after() {
//                            impoundAdapter.notifyDataSetChanged();
//                        }
//
//                    }
//                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // just do nothing
//                    }
//                });
//                dialog.show();
//            }
//
//            public void addChangeContract(final int position) {
//                (new BackgroundProcess(activity) {
//
//                    @Override
//                    protected void before() {
//                        contractList.get(position).ChangeContractID = DatabaseHelper.getUUID();
//                        contractList.get(position).OrganizationCode = BHPreference.organizationCode();
//                        //contractList.get(position).RefNo = contractList.get(position).RefNo;
//                        contractList.get(position).Status = ImpoundProductController.ImpoundProductStatus.REQUEST.toString();
//                        contractList.get(position).RequestDate = new Date();
//                        contractList.get(position).RequestBy = BHPreference.employeeID();
//                        contractList.get(position).RequestTeamCode = BHPreference.teamCode();
//                        contractList.get(position).CreateDate = new Date();
//                        contractList.get(position).CreateBy = BHPreference.employeeID();
//                        contractList.get(position).LastUpdateDate = new Date();
//                        contractList.get(position).LastUpdateBy = BHPreference.employeeID();
//                    }
//
//                    @Override
//                    protected void calling() {
//                        Log.e(TAG, "TSRService & LOCAL-DB::addRequestChangeContract");
//                        TSRController.addRequestChangeContract(contractList.get(position));
//                    }
//
//                    @Override
//                    protected void after() {
//                        .notifyDataSetChanged();
//                    }
//                }).start();
//            }
//
    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_next:
                // showNextView();
                break;
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_update_list:
                syncEditContractRelated();
                break;
            default:
                break;
        }

    }

    private class ChangeContractAdapter extends BHArrayAdapter<ChangeContractInfo> {
        public ChangeContractAdapter(Context context, int resource, List<ChangeContractInfo> objects) {
            super(context, resource, objects);
        }

        class ViewHolder {
            public TextView txtEffDate, txtName, txtSerialNo, txtTime, txtAuthorization;
        }

        @Override
        protected void onViewItem(final int position, View view,
                                  Object holder, ChangeContractInfo info) {
            ViewHolder vh = (ViewHolder) holder;
            if (positionCode.contains("SaleLeader")) {
                vh.txtEffDate.setText(BHUtilities.dateFormat(info.InstallDate));
                vh.txtName.setText(String.format("%s %s",
                        BHUtilities.trim(info.CustomerFullName),
                        BHUtilities.trim(info.CompanyName)));

                vh.txtSerialNo.setText(BHUtilities
                        .trim(info.ProductSerialNumber));
            } else {
                String time = new SimpleDateFormat("HH:mm").format(info.InstallDate);
                String Status = null;

                if (info.Status != null) {
                    if (info.Status.equals(ChangeContractStatus.REQUEST.toString())) {
                        Status = "N";
                    } else if (info.Status.equals(ChangeContractStatus.APPROVED.toString())) {
                        Status = "Y";
                    }
                } else {
                    Status = "";
                }
                vh.txtSerialNo.setText(info.ProductSerialNumber);
                vh.txtEffDate.setText(BHUtilities.dateFormat(info.InstallDate, "dd/MM/yy"));
                vh.txtTime.setText(time);
                vh.txtAuthorization.setText(BHUtilities.trim(Status));
            }
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
                        showNextView(new ChangeContractListFragment());
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
        request.master.syncRequestChangeContractRelated = true;

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);
    }

    public void showNoticeDialogBox(final String title, final String message) {
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity);
        setupAlert.setTitle(title);
        setupAlert.setMessage(message);
        setupAlert.setPositiveButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // ??
            }
        });
        setupAlert.show();
    }

    /*private void updateChangeContract(final ChangeContractInfo cont) {
        (new BackgroundProcess(activity) {
            ContractInfo OldContractInfo = null;
            ContractInfo NewContractInfo = null;


            @Override
            protected void calling() {
                OldContractInfo = TSRController.getContractByRefNoForChangeContract(BHPreference.organizationCode(), cont.OldSaleID);
                NewContractInfo = TSRController.getContractByRefNoForChangeContract(BHPreference.organizationCode(), cont.NewSaleID);

                if (OldContractInfo != null && NewContractInfo != null) {
                    OldContractInfo.isActive = false;
                    NewContractInfo.isActive = true;

                    //NewContractInfo.CONTNO = getAutoGenerateDocumentID(TSRController.DocumentGenType.Contract.toString(), BHPreference.SubTeamCode(), BHPreference.saleCode());
                    NewContractInfo.CONTNO = OldContractInfo.CONTNO;
                    OldContractInfo.tocontno = NewContractInfo.CONTNO;
                    OldContractInfo.torefno = NewContractInfo.RefNo;
                    OldContractInfo.todate = cont.RequestDate;

                    OldContractInfo.LastUpdateDate = new Date();
                    OldContractInfo.LastUpdateBy = BHPreference.employeeID();
                    NewContractInfo.LastUpdateDate = new Date();
                    NewContractInfo.LastUpdateBy = BHPreference.employeeID();
                    NewContractInfo.todate = cont.RequestDate;

                    TSRController.updateContractForChangeContrac(OldContractInfo, true);
                    TSRController.updateContractForChangeContrac(NewContractInfo, true);

                    String receiptCode = getAutoGenerateDocumentID(TSRController.DocumentGenType.Receipt.toString(), BHPreference.SubTeamCode(), BHPreference.saleCode());
                    List<ReceiptInfo> receiptInfoList = TSRController.getReceiptByRefNo(cont.NewSaleID);
                    if (receiptInfoList != null) {
                        for (ReceiptInfo info : receiptInfoList) {
                            info.ReceiptCode = receiptCode;
                            info.LastUpdateDate = new Date();
                            info.LastUpdateBy = BHPreference.employeeID();
                            TSRController.updateReceipt(info, true);
                        }
                    }

                    cont.Status = ChangeContractStatus.COMPLETED.toString();
                    cont.ResultProblemID = cont.RequestProblemID;
                    cont.ResultDetail = cont.RequestDetail;
                    cont.EffectiveDate = cont.RequestDate;
                    cont.EffectiveBy = BHPreference.employeeID();
//                    cont.ChangeContractPaperID = cont.ChangeContractID;
                    cont.ChangeContractPaperID = getAutoGenerateDocumentID(TSRController.DocumentGenType.ChangeContract.toString(), BHPreference.SubTeamCode(), BHPreference.saleCode());
                    cont.LastUpdateDate = new Date();
                    cont.LastUpdateBy = BHPreference.employeeID();

                    // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                    cont.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                    *//*AssignInfo assign = new AssignInfo();
                    assign.AssignID = DatabaseHelper.getUUID();
                    assign.TaskType = AssignController.AssignTaskType.ChangeContract.toString();
                    assign.OrganizationCode = BHPreference.organizationCode();
                    assign.RefNo = OldContractInfo.RefNo;
                    assign.AssigneeEmpID = BHPreference.employeeID();
                    assign.AssigneeTeamCode = BHPreference.teamCode();
                    assign.CreateDate = new Date();
                    assign.CreateBy = BHPreference.employeeID();
                    assign.LastUpdateDate = new Date();
                    assign.LastUpdateBy = BHPreference.employeeID();
                    assign.ReferenceID = cont.ChangeContractID;*//*

                    TSRController.actionChangeContract1(cont);

                    List<ManualDocumentInfo> manualDocumentList = new ManualDocumentController().getManualDocumentForChangeContract(cont.OldSaleID
                            , DocumentHistoryController.DocumentType.ManualDocument.toString(), DocumentHistoryController.DocumentType.Contract.toString());
                    if (manualDocumentList != null && manualDocumentList.size() > 0) {
                        ManualDocumentInfo manualDocumentInfo = manualDocumentList.get(0);
                        DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                        docHist.PrintHistoryID = DatabaseHelper.getUUID();
                        docHist.OrganizationCode = BHPreference.organizationCode();
                        docHist.DatePrint = new Date();
                        docHist.DocumentType = DocumentHistoryController.DocumentType.ManualDocument.toString();
                        docHist.DocumentNumber = manualDocumentInfo.DocumentID;
                        docHist.SyncedDate = new Date();
                        docHist.CreateBy = BHPreference.employeeID();
                        docHist.CreateDate = new Date();
                        docHist.LastUpdateDate = new Date();
                        docHist.LastUpdateBy = BHPreference.employeeID();
                        docHist.Selected = false;
                        docHist.Deleted = false;
                        docHist.PrintOrder = 1;
                        docHist.Status = "";
                        docHist.SentDate = null;
                        docHist.SentEmpID = "";
                        docHist.SentSaleCode = "";
                        docHist.SentSubTeamCode = "";
                        docHist.SentTeamCode = "";
                        docHist.ReceivedDate = null;
                        docHist.ReceivedEmpID = "";
                        addDocumentHistory(docHist, true);
                    }


                } else {
                    showDialog("แจ้งเตือน", "ไม่พบข้อมูลของสัญญา");
                }
            }

            @Override
            protected void after() {
                //showNextView(new ChangeContractListFragment());
                if (OldContractInfo != null && NewContractInfo != null) {
                    ContractInfo newContract;
                    if (isCredit) {
                        newContract = getContractByRefNoForCredit(BHPreference.organizationCode(), NewContractInfo.RefNo);
                    } else {
                        newContract = getContractByRefNo(BHPreference.organizationCode(), NewContractInfo.RefNo);
                    }
                    AddressInfo newAddressInstall = getAddress(NewContractInfo.RefNo, AddressInfo.AddressType.AddressInstall);
                    AddressInfo newAddressIDCard = getAddress(NewContractInfo.RefNo, AddressInfo.AddressType.AddressIDCard);

                    ProblemInfo problemInfo = new ProblemController().getProblemByProblemID(cont.RequestProblemID);

                    List<SalePaymentPeriodInfo> sppList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(NewContractInfo.RefNo);

                    ChangeContractPrintFragment.Data data1 = new ChangeContractPrintFragment.Data();
                    data1.selectedCauseName = problemInfo.ProblemName;
                    data1.newContract = newContract;
                    data1.newSPPList = sppList;
                    data1.newAddressInstall = newAddressInstall;
                    data1.newAddressIDCard = newAddressIDCard;
                    ChangeContractPrintFragment fmCCPrint = BHFragment.newInstance(ChangeContractPrintFragment.class, data1);
                    showNextView(fmCCPrint);
                }
            }
        }).start();
    }*/
}

//public class ChangeContractListFragment extends BHFragment {
//
//    private static final String LOGIN_ORGANIZATION_CODE = BHPreference
//            .organizationCode();
//    private static final String LOGIN_TEAM_CODE = BHPreference.teamCode();
//    private static final String CONTRACT_LIST_KEY = "CONTRACT_LIST";
//    protected static String customerid;
//    protected static String refNo;
//
//    @InjectView
//    private ListView lvCustomerList;
//
//    private List<ContractInfo> contractList = null;
//
//    @Override
//    protected int fragmentID() {
//        // TODO Auto-generated method stub
//        return R.layout.fragment_change_contract_customer_list;
//    }
//
//    @Override
//    protected int titleID() {
//        // TODO Auto-generated method stub
//        return R.string.main_menu_document;
//    }
//
//    @Override
//    protected int[] processButtons() {
//        // TODO Auto-generated method stub
//        return new int[] { R.string.button_back };
//    }
//
//    @Override
//    protected void onCreateViewSuccess(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(CONTRACT_LIST_KEY)) {
//                contractList = savedInstanceState
//                        .getParcelableArrayList(CONTRACT_LIST_KEY);
//            }
//        }
//
//        if (contractList == null) {
//
//            new BackgroundProcess(activity) {
//                boolean[] chkPaymentCompleteList;
//
//                // List<ContractInfo> output;
//
//                @Override
//                protected void calling() {
//                    // TODO Auto-generated method stub
//                    try {
//
//                        contractList = TSRController.getContractBySaleTeamCode(
//                                LOGIN_ORGANIZATION_CODE, LOGIN_TEAM_CODE);
//
//                        if (contractList != null) {
//                            chkPaymentCompleteList = new boolean[contractList
//                                    .size()];
//
//                            for (int i = 0; i < contractList.size(); i++) {
//                                chkPaymentCompleteList[i] = true;
//                                List<SalePaymentPeriodInfo> resultSPPList = TSRController
//                                        .getSalePaymentPeriodByRefNo(contractList
//                                                .get(i).RefNo);
//                                if (resultSPPList != null) {
//                                    for (int j = 0; j < resultSPPList.size(); j++) {
//                                        if (resultSPPList.get(j).PaymentComplete == false) {
//                                            chkPaymentCompleteList[i] = false;
//                                            j = resultSPPList.size();
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                protected void after() {
//                    // TODO Auto-generated method stub
//                    if (contractList != null) {
//                        for (int k = contractList.size()-1; k >= 0; k--) {
//                            if (chkPaymentCompleteList[k] == true) {
//                                contractList.remove(k);
//                            }
//                        }
//
//                        if (contractList != null) {
//
//                            // contractList = output;
//                            bindContractList();
//                            setWidgetsEventListener();
//                        }
//                    }
//
//                }
//            }.start();
//        } else {
//            bindContractList();
//            setWidgetsEventListener();
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        // TODO Auto-generated method stub
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList(CONTRACT_LIST_KEY,
//                (ArrayList<ContractInfo>) contractList);
//    }
//
//    private void bindContractList() {
//        LayoutInflater li = activity.getLayoutInflater();
//        ViewGroup header = (ViewGroup) li.inflate(
//                R.layout.impound_product_customer_list_header, lvCustomerList,
//                false);
//        lvCustomerList.addHeaderView(header, null, false);
//
//        BHArrayAdapter<ContractInfo> Contract = new BHArrayAdapter<ContractInfo>(
//                activity, R.layout.list_customer, contractList) {
//
//            class ViewHolder {
//                public TextView txtEffDate;
//                public TextView txtName;
//                public TextView txtSerialNo;
//            }
//
//            @Override
//            protected void onViewItem(final int position, View view,
//                                      Object holder, ContractInfo info) {
//                // TODO Auto-generated method stub
//                ViewHolder vh = (ViewHolder) holder;
//                vh.txtEffDate.setText(BHUtilities.dateFormat(info.InstallDate));
//                // vh.txtName.setText(BHUtilities.trim(info.CustomerFullName));
//                vh.txtName.setText(String.format("%s %s",
//                        BHUtilities.trim(info.CustomerFullName),
//                        BHUtilities.trim(info.CompanyName)));
//
//                vh.txtSerialNo.setText(BHUtilities
//                        .trim(info.ProductSerialNumber));
//            }
//        };
//        lvCustomerList.setAdapter(Contract);
//    }
//
//    private void setWidgetsEventListener() {
//        lvCustomerList.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    final int position, long id) {
//
//                final String orgCodeSelected = ((ContractInfo) lvCustomerList
//                        .getItemAtPosition(position)).OrganizationCode;
//                final String serialNoSelected = ((ContractInfo) lvCustomerList
//                        .getItemAtPosition(position)).ProductSerialNumber;
//
//                customerid = ((ContractInfo) lvCustomerList
//                        .getItemAtPosition(position)).CustomerID;
//                refNo = ((ContractInfo) lvCustomerList
//                        .getItemAtPosition(position)).RefNo;
//
//                BarcodeScanFragment fmBarcode = BHFragment.newInstance(
//                        BarcodeScanFragment.class, new ScanCallBack() {
//
//                            @Override
//                            public void onResult(BHParcelable data) {
//                                // TODO Auto-generated method stub
//                                final BarcodeScanFragment.Result barcodeResult = (BarcodeScanFragment.Result) data;
//                                (new BackgroundProcess(activity) {
//
//                                    // GetContractBySerialNoInputInfo input =
//                                    // new GetContractBySerialNoInputInfo();
//                                    ContractInfo resultContract;
//
//                                    @Override
//                                    protected void before() {
//                                        // TODO Auto-generated method stub
//                                        // input.OrganizationCode =
//                                        // orgCodeSelected;
//                                        // input.ProductSerialNumber =
//                                        // barcodeResult.barcode;
//                                    }
//
//                                    @Override
//                                    protected void calling() {
//                                        // TODO Auto-generated method stub
//                                        resultContract = getContractBySerialNo(
//                                                orgCodeSelected,
//                                                barcodeResult.barcode,
//                                                ContractStatus.NORMAL
//                                                        .toString());
//                                    }
//
//                                    @Override
//                                    protected void after() {
//                                        // TODO Auto-generated method stub
//                                        try {
//                                            if (resultContract != null) {
//
//                                                ChangeContractOldContractDetailFragment.Data data1 = new ChangeContractOldContractDetailFragment.Data();
//                                                data1.oldRefNo = resultContract.RefNo;
//                                                if (resultContract.ProductSerialNumber
//                                                        .equals(serialNoSelected)) {
//                                                    ChangeContractOldContractDetailFragment fmOldContract = BHFragment
//                                                            .newInstance(
//                                                                    ChangeContractOldContractDetailFragment.class,
//                                                                    data1);
//                                                    showNextView(fmOldContract);
//                                                } else {
//                                                    final String title = "กรุณาตรวจสอบสินค้า";
//                                                    String message = "รหัสสินค้าไม่ตรงกับรายการที่ท่านเลือก!";
//                                                    Builder setupAlert = new AlertDialog.Builder(
//                                                            activity)
//                                                            .setTitle(title)
//                                                            .setMessage(message)
//                                                            .setPositiveButton(
//                                                                    "ปิด",
//                                                                    new DialogInterface.OnClickListener() {
//                                                                        @Override
//                                                                        public void onClick(
//                                                                                DialogInterface dialog,
//                                                                                int whichButton) {
//                                                                            // TODO
//                                                                            // Auto-generated
//                                                                            // method
//                                                                            // stub
//                                                                        }
//                                                                    });
//                                                    setupAlert.show();
//                                                    // showMessage("รหัสสินค้าไม่ตรงกับรายการที่ท่านเลือก!");
//                                                }
//                                            } else {
//                                                final String title = "กรุณาตรวจสอบสินค้า";
//                                                String message = "ไม่พบสินค้า!";
//                                                Builder setupAlert = new AlertDialog.Builder(
//                                                        activity)
//                                                        .setTitle(title)
//                                                        .setMessage(message)
//                                                        .setPositiveButton(
//                                                                "ปิด",
//                                                                new DialogInterface.OnClickListener() {
//                                                                    @Override
//                                                                    public void onClick(
//                                                                            DialogInterface dialog,
//                                                                            int whichButton) {
//                                                                        // TODO
//                                                                        // Auto-generated
//                                                                        // method
//                                                                        // stub
//                                                                    }
//                                                                });
//                                                setupAlert.show();
//                                                // showMessage("ไม่พบสินค้า!");
//                                            }
//                                        } catch (Exception e) {
//
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }).start();
//                            }
//
//                            @Override
//                            public String onNextClick() {
//                                return serialNoSelected;
//
//                            }
//                        });
//                fmBarcode.setTitle(R.string.main_menu_document);
//                fmBarcode.setViewTitle(R.string.title_scan_product_serial);
//                showNextView(fmBarcode);
//            }
//        });
//
//    }
//
//    @Override
//    public void onProcessButtonClicked(int buttonID) {
//        // TODO Auto-generated method stub
//        switch (buttonID) {
//            case R.string.button_next:
//                // showNextView();
//                break;
//
//            case R.string.button_back:
//                showLastView();
//                break;
//
//            default:
//                break;
//        }
//
//    }
//
//}
