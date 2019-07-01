package th.co.thiensurat.fragments.contracts.change;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.controller.ComplainController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.ManualDocumentController;
import th.co.thiensurat.data.controller.SaleAuditController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ComplainInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.ManualDocumentInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.SaleAuditInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.document.manual.ManualDocumentDetailFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.sales.SalePayFragment;

public class ChangeContractResultFragment extends BHFragment {

    // *** [Start] Debug-Value *** //
    // private static final String TSR_COMMITTEE_NAME =
    // "นายวิรัช  วงศ์นิรันดร์";
    // private static final String LOGIN_ORGANIZATION_CODE = "0";
    // private static final String LOGIN_TEAM_CODE = "PAK23";
    // private static final String LOGIN_TEAM_NAME = "PAK23";
    // private static final String LOGIN_EMPID = "PAK1070";
    // private static final String LOGIN_SALECODE = "SCPAK1070";
    // private static final String LOGIN_SALELEADER_EMPID = "PAK0687";
    // private static final String LOGIN_SALELEADER_EMPNAME =
    // "นายประยงค์  ทิพย์ชาติ";
    // *** [End] Debug-Value *** //

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());

    public static class Data extends BHParcelable {

        public String selectedCauseName;
        public ContractInfo newContract;
        public List<SalePaymentPeriodInfo> newSPPList; // SalePaymentPeriod
        // public String newRefNo;
        public AddressInfo newAddressIDCard;
        public AddressInfo newAddressInstall;
        public AddressInfo newAddressPayment;

        // For Save
        public String changeContractType;
        public ChangeContractInfo chgContractRequest;
        public ChangeContractInfo chgContractApprove;
        public ChangeContractInfo chgContractAction;
        public AssignInfo assign;
        public ContractInfo oldContract;
        // public AddressInfo addressIDCard;
        // public AddressInfo addressInstall;
        // public AddressInfo addressPayment;

        // public ChangeContractInfo chgCont;

        public DebtorCustomerInfo tmpCustomer;

        public List<PaymentInfo> newPayment;

        public ContractImageInfo newContractImage;

    }

    @InjectView private TextView txtEFFDate;
    @InjectView private TextView txtContNo;
    @InjectView private TextView txtContNo_Old;
    @InjectView private TextView txtCustomerFullName;
    @InjectView private TextView txtIDCard;
    @InjectView private TextView txtAddressIDCard;
    @InjectView private TextView txtAddressInstall;
    @InjectView private TextView txtProduct;
    @InjectView private TextView txtSerialNo;

    @InjectView private LinearLayout linearLayoutSaleAmount;
    @InjectView private TextView txtSaleAmount;

    @InjectView private LinearLayout linearLayoutSaleDiscount;
    @InjectView private TextView txtSaleDiscount;

    @InjectView private LinearLayout linearLayoutSaleNetAmount;
    @InjectView private TextView txtSaleNetAmount;

    @InjectView private LinearLayout linearLayoutFirstPaymentAmount;
    @InjectView private TextView txtFirstPaymentAmount;

    @InjectView private LinearLayout linearLayoutTwoPaymentAmount;
    @InjectView private TextView txtTwoPaymentAmount;

    @InjectView private LinearLayout linearLayoutNextPaymentAmount;
    @InjectView private TextView txtNextPaymentAmountLabel;
    @InjectView private TextView txtNextPaymentAmountValue;

    @InjectView private TextView txtCCCause, lblCustomerFullName;
    @InjectView private TextView txtCCDate;

    private Data dataContract;
    private ContractInfo contract;
    private List<SalePaymentPeriodInfo> sppList = null;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_change_contract_result;
    }

    @Override
    protected int titleID() {
        return R.string.title_change_contract;
    }

    @Override
    protected int[] processButtons() {
        //return new int[]{R.string.button_back, R.string.button_save_manual_contract};
        if(dataContract == null){
            dataContract = getData();
        }

        int[] buttons;

        if(dataContract.chgContractApprove == null && dataContract.chgContractAction == null){
            buttons = new int[]{R.string.button_back, R.string.button_save};
        } else {
            buttons = new int[]{R.string.button_back, R.string.button_save_manual_contract};
        }
        return buttons;
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            /*case R.string.button_save:
                validateManualDocument(dataContract.newContract);
                // showMessage("Under construction!");
                break;*/
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_save_manual_contract:
                // Check Manual Document [Contract] ? EditMode or AddMode
                ManualDocumentDetailFragment.Data data1 = new ManualDocumentDetailFragment.Data();
                data1.DocumentNumber = dataContract.newContract.RefNo;
                data1.DocumentNo = dataContract.newContract.CONTNO;
                data1.processType = SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract;
                data1.DocumentType = DocumentHistoryController.DocumentType.Contract.toString();

                ManualDocumentDetailFragment fmManualDocContract = BHFragment.newInstance(ManualDocumentDetailFragment.class, data1);
                showNextView(fmManualDocContract);
                break;
            case R.string.button_save:
                final String title = "คำเตือน";
                final String message = "ยืนยันการเปลี่ยนสัญญา";
                new AlertDialog.Builder(activity)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                save();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }).show();

                break;
            default:
                break;
        }
    }

    /*public void validateManualDocument(ContractInfo newContract) {
        List<ManualDocumentInfo> manualDocument = new ManualDocumentController().getManualDocumentForChangeContract(newContract.RefNo
                , DocumentHistoryController.DocumentType.ManualDocument.toString(), DocumentHistoryController.DocumentType.Contract.toString());
        if (manualDocument != null && manualDocument.size() > 0) {
            final String title = "คำเตือน";
            final String message = "ยืนยันการเปลี่ยนสัญญา";
            new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            save();
                        }
                    })
                    .setNegativeButton("ใม่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).show();
        } else {
            String title = "กรุณาตรวจสอบข้อมูล";
            String message = "กรุณาบันทึกใบสัญญามือ";
            BHUtilities.alertDialog(activity, title, message).show();
        }
    }*/


    public void save() {
        (new BackgroundProcess(activity) {
            List<String> positionCode = Arrays.asList(BHPreference.PositionCode().split(","));

            @Override
            protected void calling() {
                if (dataContract.newAddressIDCard != null) {
                    if (dataContract.newAddressIDCard.AddressDetail == null || (dataContract.newAddressIDCard.AddressDetail != null && dataContract.newAddressIDCard.AddressDetail.equals(""))) {
                        dataContract.newAddressIDCard.AddressDetail = "-";
                    }
                    if (dataContract.newAddressIDCard.AddressDetail2 == null || (dataContract.newAddressIDCard.AddressDetail2 != null && dataContract.newAddressIDCard.AddressDetail2.equals(""))) {
                        dataContract.newAddressIDCard.AddressDetail2 = "-";
                    }
                    if (dataContract.newAddressIDCard.AddressDetail3 == null || (dataContract.newAddressIDCard.AddressDetail3 != null && dataContract.newAddressIDCard.AddressDetail3.equals(""))) {
                        dataContract.newAddressIDCard.AddressDetail3 = "-";
                    }
                    if (dataContract.newAddressIDCard.AddressDetail4 == null || (dataContract.newAddressIDCard.AddressDetail4 != null && dataContract.newAddressIDCard.AddressDetail4.equals(""))) {
                        dataContract.newAddressIDCard.AddressDetail4 = "-";
                    }
                    saveAddress(dataContract.newAddressIDCard);
                }
                if (dataContract.newAddressInstall != null) {
                    if (dataContract.newAddressInstall.AddressDetail == null || (dataContract.newAddressInstall.AddressDetail != null && dataContract.newAddressInstall.AddressDetail.equals(""))) {
                        dataContract.newAddressInstall.AddressDetail = "-";
                    }
                    if (dataContract.newAddressInstall.AddressDetail2 == null || (dataContract.newAddressInstall.AddressDetail2 != null && dataContract.newAddressInstall.AddressDetail2.equals(""))) {
                        dataContract.newAddressInstall.AddressDetail2 = "-";
                    }
                    if (dataContract.newAddressInstall.AddressDetail3 == null || (dataContract.newAddressInstall.AddressDetail3 != null && dataContract.newAddressInstall.AddressDetail3.equals(""))) {
                        dataContract.newAddressInstall.AddressDetail3 = "-";
                    }
                    if (dataContract.newAddressInstall.AddressDetail4 == null || (dataContract.newAddressInstall.AddressDetail4 != null && dataContract.newAddressInstall.AddressDetail4.equals(""))) {
                        dataContract.newAddressInstall.AddressDetail4 = "-";
                    }
                    saveAddress(dataContract.newAddressInstall);
                }
                if (dataContract.newAddressPayment != null) {
                    saveAddress(dataContract.newAddressPayment);
                }

                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                dataContract.chgContractRequest.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                addRequestChangeContract(dataContract.chgContractRequest);

                if (dataContract.chgContractApprove != null) {
                    dataContract.chgContractApprove.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                    approveChangeContract(dataContract.chgContractApprove, dataContract.assign);
                }

                /*** [START] :: Fixed - [BHPROJ-0024-3070] :: [Android-บันทึกข้อมูลสัญญา] แก้ไขในส่วนของการบันทึกข้อมูลสัญญาใน Field ContractReferenceNo ให้ถูกต้อง (กรณีหัวหน้าทีมขายเปลี่ยนสัญญา) ***/
                List<ManualDocumentInfo> manualDocumentContract = new ManualDocumentController().getManualDocumentByDocumentNumber(dataContract.newContract.RefNo);
                if (manualDocumentContract != null && manualDocumentContract.size() > 0) {
                    dataContract.newContract.ContractReferenceNo = String.valueOf(manualDocumentContract.get(0).ManualRunningNo);
                }
                /*** [END] :: Fixed - [BHPROJ-0024-3070] :: [Android-บันทึกข้อมูลสัญญา] แก้ไขในส่วนของการบันทึกข้อมูลสัญญาใน Field ContractReferenceNo ให้ถูกต้อง ***/

                if (dataContract.changeContractType.equals("01") || dataContract.changeContractType.equals("02") || dataContract.changeContractType.equals("04")) {
                    // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                    if(dataContract.chgContractAction != null){
                        dataContract.chgContractAction.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                        dataContract.chgContractAction.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                    }
                    actionChangeContractWithSalePaymentPeriod(dataContract.chgContractAction, dataContract.newContract, dataContract.newSPPList);
                } else if (dataContract.changeContractType.equals("03")) {
                    //actionChangeContract(dataContract.chgContractAction, dataContract.newContract);
                    // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                    if(dataContract.chgContractAction != null){
                        dataContract.chgContractAction.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                        dataContract.chgContractAction.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                    }
                    actionChangeContractWithSalePaymentPeriod(dataContract.chgContractAction, dataContract.newContract, dataContract.newSPPList);
                    //saveDebtorCustomer(dataContract.tmpCustomer);
                    saveCustomerData(dataContract.tmpCustomer, true);
                }

                if (dataContract.newPayment != null) {
                    for (PaymentInfo info : dataContract.newPayment) {
                        TSRController.addPaymentByChangeContract(info, true);
                    }
                }

                if(isCredit) {
                    //Add SaleAudit(ถ้ามี)
                    SaleAuditInfo oldSaleAudit = new SaleAuditController().getSaleAuditByRefNo(dataContract.oldContract.RefNo);
                    String empId = BHPreference.employeeID();
                    Date newDate = new Date();
                    if(oldSaleAudit != null) {

                        SaleAuditInfo newSaleAudit = oldSaleAudit;
                        newSaleAudit.SaleAuditID = DatabaseHelper.getUUID();
                        newSaleAudit.RefNo = dataContract.newContract.RefNo;
                        newSaleAudit.CreateDate = newDate;
                        newSaleAudit.CreateBy = empId;
                        newSaleAudit.LastUpdateDate = newDate;
                        newSaleAudit.LastUpdateBy = empId;
                        // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                        newSaleAudit.SaleAuditEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                        //ตรวจสอบว่าผ่านการ Audit หรือยัง?
                        if(oldSaleAudit.IsPassSaleAudit || (oldSaleAudit.ComplainID == null || oldSaleAudit.ComplainID.equals(""))){
                            //Add SaleAudit
                            TSRController.addSaleAudit(newSaleAudit, true);
                        } else {
                            //ถ้ายังไม่ผ่านการ Audit ต้องมาตรวจอีกว่ามีการแจ้งปัญหาอยู่หรือไม่?
                            //ถ้ามีให้ทำการสร้าง Complain ขึ้นมาด้วย พร้อมกับ Assign งานแจ้งปัญหา และ SaleAudit
                            ComplainInfo oldComplain = new ComplainController().getComplainByComplainID(oldSaleAudit.ComplainID);
                            if(oldComplain != null){
                                //Add Complain
                                ComplainInfo newComplain = oldComplain;
                                newComplain.ComplainID = DatabaseHelper.getUUID();
                                newComplain.RefNo = dataContract.newContract.RefNo;
                                newComplain.ReferenceID = newSaleAudit.SaleAuditID;
                                newComplain.CreateDate = newDate;
                                newComplain.CreateBy = empId;
                                newComplain.LastUpdateDate = newDate;
                                newComplain.LastUpdateBy = empId;

                                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                                newComplain.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                                TSRController.addComplainStatusREQUEST(newComplain, true);

                                TSRController.updateComplainStatusAPPROVED(newComplain, true);

                                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                                newComplain.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                                TSRController.updateComplainStatusCOMPLETED(newComplain, true);

                                //Add Assign For Complain
                                List<AssignInfo> oldAssignFroComplain = new AssignController().getAssignByRefNoByTaskTypeByReferenceID(dataContract.oldContract.RefNo, AssignController.AssignTaskType.Complain.toString(), oldSaleAudit.ComplainID);
                                if(oldAssignFroComplain != null && oldAssignFroComplain.size() > 0){
                                    AssignInfo newAssignFroComplain = oldAssignFroComplain.get(0);
                                    newAssignFroComplain.AssignID = DatabaseHelper.getUUID();
                                    newAssignFroComplain.RefNo = dataContract.newContract.RefNo;
                                    newAssignFroComplain.ReferenceID = newComplain.ComplainID;
                                    newAssignFroComplain.CreateDate = newDate;
                                    newAssignFroComplain.CreateBy = empId;
                                    newAssignFroComplain.LastUpdateDate = newDate;
                                    newAssignFroComplain.LastUpdateBy = empId;
                                    // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                                    newSaleAudit.SaleAuditEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                                    TSRController.addAssign(newAssignFroComplain, true);
                                }

                                //Add SaleAudit
                                newSaleAudit.ComplainID = newComplain.ComplainID;
                                TSRController.addSaleAudit(newSaleAudit, true);
                            }
                        }

                        //Add Assign For SaleAudit(ถ้ามี)
                        List<AssignInfo> oldAssignFroSaleAudit = new AssignController().getAssignByRefNoByTaskTypeByReferenceID(dataContract.oldContract.RefNo, AssignController.AssignTaskType.SaleAudit.toString(), newSaleAudit.SaleAuditID);
                        if (oldAssignFroSaleAudit != null && oldAssignFroSaleAudit.size() > 0) {
                            AssignInfo newAssignFroSaleAudit = oldAssignFroSaleAudit.get(0);
                            newAssignFroSaleAudit.AssignID = DatabaseHelper.getUUID();
                            newAssignFroSaleAudit.RefNo = dataContract.newContract.RefNo;
                            newAssignFroSaleAudit.ReferenceID = newSaleAudit.SaleAuditID;
                            newAssignFroSaleAudit.CreateDate = newDate;
                            newAssignFroSaleAudit.CreateBy = empId;
                            newAssignFroSaleAudit.LastUpdateDate = newDate;
                            newAssignFroSaleAudit.LastUpdateBy = empId;

                            TSRController.addAssign(newAssignFroSaleAudit, true);
                        }

                        List<SalePaymentPeriodInfo> oldSppList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(dataContract.oldContract.RefNo);
                        List<SalePaymentPeriodInfo> newSppList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(dataContract.newContract.RefNo);
                        //ถ้าผ่านการ Audit ถึงจะมีการ Assign เก็บเงินงวดต่อไป
                        if(oldSaleAudit.IsPassSaleAudit){
                            boolean checkAssignFroNextPayment = true;

                            //Assign เก็บเงินงวดต่อไป ต้อง Check ก่อนว่า สัญญาเดิม มีการ Assign ให้ครบทุก ๆ งวดที่ยังไม่ Complete หรือเปล่า?
                            AssignInfo  newAssignFroNextPayment = new AssignInfo();
                            if (oldSppList != null && oldSppList.size() > 0) {
                                for (SalePaymentPeriodInfo info : oldSppList) {
                                    if(!info.PaymentComplete){
                                        List<AssignInfo> oldAssignFroNextPayment = new AssignController().getAssignByRefNoByTaskTypeByReferenceID(dataContract.oldContract.RefNo, AssignController.AssignTaskType.SalePaymentPeriod.toString(), info.SalePaymentPeriodID);
                                        if(oldAssignFroNextPayment != null && oldAssignFroNextPayment.size() > 0){
                                            newAssignFroNextPayment = oldAssignFroNextPayment.get(0);
                                        } else {
                                            checkAssignFroNextPayment = false;
                                        }
                                    }
                                }
                            }

                            //ถ้ามีการ Assign งานเก็บเงินงวดต่อไปครบทุกงวดขอสัญญาเก่า = true จะ Assign งานเก็บเงินงวดต่อไปทุกงวดขอสัญญาใหม่
                            if(checkAssignFroNextPayment){
                                if(newSppList != null && newSppList.size() > 0){
                                    for(SalePaymentPeriodInfo info : newSppList){
                                        if(!info.PaymentComplete){
                                            AssignInfo assignInfo = new AssignInfo();
                                            assignInfo.AssignID = DatabaseHelper.getUUID();
                                            assignInfo.TaskType = AssignController.AssignTaskType.SalePaymentPeriod.toString();
                                            assignInfo.OrganizationCode = dataContract.newContract.OrganizationCode;
                                            assignInfo.RefNo = dataContract.newContract.RefNo;
                                            assignInfo.AssigneeEmpID = newAssignFroNextPayment.AssigneeEmpID;
                                            assignInfo.AssigneeTeamCode = newAssignFroNextPayment.AssigneeTeamCode;
                                            assignInfo.Order = 0;
                                            assignInfo.OrderExpect = 0;
                                            assignInfo.ReferenceID = info.SalePaymentPeriodID;
                                            assignInfo.CreateDate = newDate;
                                            assignInfo.CreateBy = empId;
                                            assignInfo.LastUpdateDate = newDate;
                                            assignInfo.LastUpdateBy = empId;

                                            TSRController.addAssign(assignInfo, true);
                                        }
                                    }
                                }
                            }
                        }

                        //หาวันที่ PaymentDueDate && PaymentAppointmentDate ของสัญญาเก่าที่ยังไม่ Complete งวดต่ำสุด
                        Date paymentAppointmentDate = null; //PaymentDueDate && PaymentAppointmentDate จะเท่ากัน
                        int monthPlusPlus = 0;
                        int startPaymentPeriodNumber = 0; ////งวดแรกที่ยังไม่ Complete ของสัญญาใหม่
                        if (oldSppList != null && oldSppList.size() > 0) {
                            for (SalePaymentPeriodInfo info : oldSppList) {
                                if(!info.PaymentComplete && paymentAppointmentDate == null){
                                    paymentAppointmentDate = info.PaymentAppointmentDate;
                                }
                            }
                        }

                        //ทำการปรับวันที่ PaymentDueDate && PaymentAppointmentDate ของสัญญาใหม่
                        if(newSppList != null && newSppList.size() > 0) {
                            //กำหนดวัน PaymentDueDate && PaymentAppointmentDate ของสัญญาใหม่ที่ยังไม่ Complete
                            for (SalePaymentPeriodInfo info : newSppList) {
                                if (!info.PaymentComplete) {

                                    if(startPaymentPeriodNumber == 0 ) {
                                        startPaymentPeriodNumber = info.PaymentPeriodNumber - 1;
                                    }

                                    Calendar c = Calendar.getInstance();
                                    c.setTime(paymentAppointmentDate);
                                    c.add(Calendar.MONTH, monthPlusPlus);
                                    monthPlusPlus++;

                                    info.PaymentDueDate = c.getTime();
                                    info.PaymentAppointmentDate = c.getTime();
                                    info.LastUpdateDate = newDate;
                                    info.LastUpdateBy = empId;
                                    TSRController.updateSalePaymentPeriod(info, true);
                                }
                            }

                            //กำหนดวัน PaymentDueDate && PaymentAppointmentDate ของสัญญาใหม่ที่ Complete แล้ว
                            for (SalePaymentPeriodInfo info : newSppList) {
                                if (info.PaymentComplete) {
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(paymentAppointmentDate);
                                    c.add(Calendar.MONTH, -startPaymentPeriodNumber);
                                    startPaymentPeriodNumber--;

                                    info.PaymentDueDate = c.getTime();
                                    info.PaymentAppointmentDate = c.getTime();
                                    info.LastUpdateDate = newDate;
                                    info.LastUpdateBy = empId;
                                    TSRController.updateSalePaymentPeriod(info, true);
                                }
                            }
                        }
                    }
                } else {
                    SalePaymentPeriodInfo period1Old = new SalePaymentPeriodController().getSalePaymentPeriodInfoByRefNoAndPaymentPeriodNumber(dataContract.oldContract.RefNo, 1);
                    List<AssignInfo> assignSalePaymentPeriod = new AssignController().getAssignByRefNoByTaskTypeByReferenceID(dataContract.oldContract.RefNo
                            , AssignController.AssignTaskType.SalePaymentPeriod.toString(), period1Old.SalePaymentPeriodID);
                    if (assignSalePaymentPeriod != null && assignSalePaymentPeriod.size() > 0 && dataContract.newContract != null) {
                        SalePaymentPeriodInfo period1 = new SalePaymentPeriodController().getSalePaymentPeriodInfoByRefNoAndPaymentPeriodNumber(dataContract.newContract.RefNo, 1);

                        AssignInfo assignInfo = new AssignInfo();
                        assignInfo.AssignID = DatabaseHelper.getUUID();
                        assignInfo.TaskType = AssignController.AssignTaskType.SalePaymentPeriod.toString();
                        assignInfo.OrganizationCode = dataContract.newContract.OrganizationCode;
                        assignInfo.RefNo = dataContract.newContract.RefNo;
                        assignInfo.AssigneeEmpID = BHPreference.employeeID();
                        assignInfo.AssigneeTeamCode = BHPreference.teamCode();
                        assignInfo.Order = 0;
                        assignInfo.OrderExpect = 1;
                        assignInfo.ReferenceID = period1.SalePaymentPeriodID;
                        assignInfo.CreateBy = BHPreference.employeeID();
                        assignInfo.CreateDate = new Date();
                        assignInfo.LastUpdateBy = BHPreference.employeeID();
                        assignInfo.LastUpdateDate = new Date();

                        TSRController.addAssign(assignInfo, true);
                    }
                }
//						else if (dataContract.changeContractType.equals("04")) {
//							actionChangeContract(dataContract.chgContractAction, dataContract.newContract);
//						}



                //updateContract(dataContract.oldContract, true);
                updateContract(dataContract.oldContract, true, true);

//						DocumentHistoryInfo docHist = new DocumentHistoryInfo();
//						docHist.PrintHistoryID = DatabaseHelper.getUUID();
//						docHist.OrganizationCode = BHPreference.organizationCode();
//						docHist.DatePrint = new Date();
//						docHist.DocumentType = DocumentType.ChangeContract.toString();
//						docHist.DocumentNumber = contract.RefNo;
//						addDocumentHistory(docHist,false);

                List<ContractImageInfo> oldContractImages = new ContractImageController().getContractImage(dataContract.oldContract.RefNo);
                if (oldContractImages != null && oldContractImages.size() > 0) {
                    for (ContractImageInfo oldContractImage : oldContractImages) {
                        if (!dataContract.changeContractType.equals("03")
                                || (dataContract.changeContractType.equals("03") && !oldContractImage.ImageTypeCode.equals(ContractImageController.ImageType.CUSTOMER.toString()))) {
                            // new ContractImage
                            ContractImageInfo newContractImage = new ContractImageInfo();
                            newContractImage.ImageID = DatabaseHelper.getUUID();
                            newContractImage.RefNo = dataContract.newContract.RefNo;
                            newContractImage.ImageName = newContractImage.ImageID + ".jpg";
                            newContractImage.ImageTypeCode = oldContractImage.ImageTypeCode;
                            newContractImage.SyncedDate = new Date();

                            /*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
                            /*// copy contractImage old -> new
                            copyImage(oldContractImage, newContractImage);
                            // add ContractImage to SERVER & LOCAL-DB
                            AddContractImage(newContractImage);*/

                            if (IsImageFile(oldContractImage)) {
                                // copy contractImage old -> new
                                copyImage(oldContractImage, newContractImage);
                                // add ContractImage to SERVER & LOCAL-DB
                                AddContractImage(newContractImage);
                            } else {
                                AddContractImageAndCopyImageForChangeContract(oldContractImage, newContractImage);
                            }
                            /*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

                        }
                    }

                    // if cause = 03
                    if (dataContract.changeContractType.equals("03")) {
                        if (dataContract.newContractImage != null) {
                            //add new customer image
                            ContractImageInfo newContractImage = dataContract.newContractImage;
                            AddContractImage(dataContract.newContractImage);
                        } else {
                            // copy old -> new
                            ContractImageInfo oldContractImage = new ContractImageController().getContractImage(dataContract.oldContract.RefNo, ContractImageController.ImageType.CUSTOMER.toString());
                            if (oldContractImage != null) {
                                ContractImageInfo newContractImage = new ContractImageInfo();
                                newContractImage.ImageID = DatabaseHelper.getUUID();
                                newContractImage.RefNo = dataContract.newContract.RefNo;
                                newContractImage.ImageName = newContractImage.ImageID + ".jpg";
                                newContractImage.ImageTypeCode = oldContractImage.ImageTypeCode;
                                newContractImage.SyncedDate = new Date();

                                /*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
                                /*// copy contractImage old -> new
                                copyImage(oldContractImage, newContractImage);
                                // add ContractImage to SERVER & LOCAL-DB
                                AddContractImage(newContractImage);*/

                                if (IsImageFile(oldContractImage)) {
                                    // copy contractImage old -> new
                                    copyImage(oldContractImage, newContractImage);
                                    // add ContractImage to SERVER & LOCAL-DB
                                    AddContractImage(newContractImage);
                                } else {
                                    AddContractImageAndCopyImageForChangeContract(oldContractImage, newContractImage);
                                }
                                /*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

                            }
                        }
                    }
                }

                if (positionCode.contains("SaleLeader")) {
                    // FIX BHPRJ00301-3788 เมื่อมีการบันทึกสัญญามือของสัญญาใหม่แล้ว จะต้องนำส่งใบสัญญามือของสัญญาเก่าด้วย
                    List<ManualDocumentInfo> manualDocumentList = new ManualDocumentController().getManualDocumentForChangeContract(dataContract.oldContract.RefNo
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

                   /* docHist.PrintHistoryID = DatabaseHelper.getUUID();
                    docHist.PrintOrder = 2;

                    addDocumentHistory(docHist, true);*/
                }
            }

            @Override
            protected void after() {

                if (positionCode.contains("SaleLeader")) {
                    ChangeContractPrintFragment.Data data1 = new ChangeContractPrintFragment.Data();
                    data1.selectedCauseName = dataContract.selectedCauseName;
                    data1.newContract = contract;
                    data1.newSPPList = sppList;
                    data1.newAddressInstall = dataContract.newAddressInstall;
                    data1.newAddressIDCard = dataContract.newAddressIDCard;
                    ChangeContractPrintFragment fmCCPrint = BHFragment.newInstance(ChangeContractPrintFragment.class, data1);
                    showNextView(fmCCPrint);
                } else {//if (positionCode.contains("SubTeamLeader") || isCredit) {
                    activity.showView(new ChangeContractMainFragment());
                }
            }
        }).start();
    }

    public void copyImage(ContractImageInfo oldContractImage, ContractImageInfo newContractImage) {
        File oldFolder = new File(String.format("%s/%s/%s/%s.jpg",
                BHStorage.getFolder(BHStorage.FolderType.Picture), oldContractImage.RefNo,
                oldContractImage.ImageTypeCode, oldContractImage.ImageID));
        File newFolder = new File(String.format("%s/%s/%s/",
                BHStorage.getFolder(BHStorage.FolderType.Picture),
                newContractImage.RefNo, newContractImage.ImageTypeCode));
        // Check old file is exists
        if (oldFolder.exists()) {
            if (!newFolder.exists()) {
                // Create folder picture ContractImage
                newFolder.mkdirs();
            }
            try {
                // Copy file to destination
                File newFile = new File(newFolder, newContractImage.ImageName);
                TSRController.copyFileUsingStream(oldFolder, newFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean IsImageFile(ContractImageInfo info) {
        try {
            File folder = new File(String.format("%s/%s/%s/%s.jpg",
                    BHStorage.getFolder(BHStorage.FolderType.Picture), info.RefNo,
                    info.ImageTypeCode, info.ImageID));
            // Check old file is exists
            return folder.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    private void AddContractImage(final ContractImageInfo newContractImage) {
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                TSRController.addContractImage(newContractImage, true);
            }
        }).start();
    }

    private void AddContractImageAndCopyImageForChangeContract(final ContractImageInfo oldContractImage, final ContractImageInfo newContractImage) {
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                TSRController.addContractImage(newContractImage, true);
                TSRController.copyImageForChangeContract(oldContractImage, newContractImage);
            }
        }).start();
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        if(dataContract == null){
            dataContract = getData();
        }

        if (dataContract.tmpCustomer != null) {
            dataContract.newContract.CustomerID = dataContract.tmpCustomer.CustomerID;
            dataContract.newContract.CustomerName = dataContract.tmpCustomer.CustomerName;
            dataContract.newContract.CustomerFullName = dataContract.tmpCustomer.CustomerFullName();
            dataContract.newContract.IDCard = dataContract.tmpCustomer.IDCard;
        }

        // Check เคยบันทึกสัญญามือแล้วหรือไม่
        List<ManualDocumentInfo> manualDocumentContract = new ManualDocumentController().getManualDocumentByDocumentNumber(dataContract.newContract.RefNo);
        if (manualDocumentContract != null && manualDocumentContract.size() > 0) {
            save();
        } else {
            contract = dataContract.newContract;

            sppList = dataContract.newSPPList;
            if (contract == null) {
                final String title = "กรุณาตรวจสอบสินค้า";
                String message = "ไม่พบข้อมูลสัญญาซื้อขาย!";
                Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                setupAlert.show();
                // showMessage("ไม่พบข้อมูลสัญญาซื้อขาย!");
            } else {
                bindContract();
                setWidgetsEventListener();
            }
        }


    }

    private void bindContract() {
        String titleLblCustomerFullName = contract.MODE > 1 ? "" + getResources().getString(R.string.change_contract_customer_fullname) : ""
                + getResources().getString(R.string.change_contract_customer_fullname_cash);
        lblCustomerFullName.setText(titleLblCustomerFullName);

        txtEFFDate.setText(BHUtilities.dateFormat(contract.EFFDATE));
        txtContNo.setText(BHUtilities.trim(contract.CONTNO));
        txtContNo_Old.setText(BHUtilities.trim(this.dataContract.oldContract.CONTNO));
        if (this.dataContract.tmpCustomer == null)
            //txtCustomerFullName.setText(BHUtilities.trim(contract.CustomerFullName));
            txtCustomerFullName.setText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));
        else
            txtCustomerFullName.setText(BHUtilities.trim(this.dataContract.tmpCustomer.CustomerFullName()));
        txtIDCard.setText(BHUtilities.trim(contract.IDCard));

        if (dataContract.newAddressIDCard != null)
            txtAddressIDCard.setText(BHUtilities.trim(dataContract.newAddressIDCard.Address()));
        if (dataContract.newAddressInstall != null)
            txtAddressInstall.setText(BHUtilities.trim(dataContract.newAddressInstall.Address()));
        // bindAddressIDCard(contract.RefNo);
        // bindAddressInstall(contract.RefNo);

        txtProduct.setText(BHUtilities.trim(contract.ProductName));
        txtSerialNo.setText(BHUtilities.trim(contract.ProductSerialNumber));

        txtSaleAmount.setText(BHUtilities.numericFormat(contract.SALES));
        txtSaleDiscount.setText(BHUtilities.numericFormat(contract.TradeInDiscount));
        txtSaleNetAmount.setText(BHUtilities.numericFormat(contract.TotalPrice));

        if (contract.TradeInDiscount == 0) {
            linearLayoutSaleAmount.setVisibility(View.GONE);
            linearLayoutSaleDiscount.setVisibility(View.GONE);
        }

        if (sppList != null) {
            if (sppList.size() > 0) {
                txtFirstPaymentAmount.setText(BHUtilities.numericFormat(sppList.get(0).NetAmount));

                if(sppList.size() != 1) {
                    if (sppList.size() == 2) {
                        linearLayoutNextPaymentAmount.setVisibility(View.GONE);
                        txtTwoPaymentAmount.setText(BHUtilities.numericFormat(sppList.get(1).NetAmount));
                    } else {
                        if (sppList.get(1).NetAmount == sppList.get(2).NetAmount) {
                            linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
                            txtNextPaymentAmountLabel.setText("งวดที่ 2 ถึง " + BHUtilities.numericFormat(sppList.size()) + " ต้องชำระงวดละ");
                            txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(sppList.get(1).NetAmount));
                        } else {
                            if (sppList.size() == 3) {
                                txtTwoPaymentAmount.setText(BHUtilities.numericFormat(sppList.get(1).NetAmount));
                                txtNextPaymentAmountLabel.setText("งวดที่ 3 ที่ต้องชำระงวดละ");
                                txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(sppList.get(2).NetAmount));
                            } else {
                                txtTwoPaymentAmount.setText(BHUtilities.numericFormat(sppList.get(1).NetAmount));
                                txtNextPaymentAmountLabel.setText("งวดที่ 3 ถึง " + BHUtilities.numericFormat(sppList.size()) + " ต้องชำระงวดละ");
                                txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(sppList.get(2).NetAmount));
                            }
                        }
                    }
                } else {
                    linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
                    linearLayoutNextPaymentAmount.setVisibility(View.GONE);
                }
            } else {
                linearLayoutFirstPaymentAmount.setVisibility(View.GONE);
                linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
                linearLayoutNextPaymentAmount.setVisibility(View.GONE);
            }
        } else {
            linearLayoutFirstPaymentAmount.setVisibility(View.GONE);
            linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
            linearLayoutNextPaymentAmount.setVisibility(View.GONE);

            final String title = "กรุณาตรวจสอบสินค้า";
            String message = "ไม่พบข้อมูลงวดการชำระเงิน!";
            Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            setupAlert.show();
            // showMessage("ไม่พบข้อมูลงวดการชำระเงิน!");
        }

        txtCCCause.setText(BHUtilities.trim(dataContract.selectedCauseName));
        //txtCCDate.setText(BHUtilities.dateFormat(new Date()));
        txtCCDate.setText(BHUtilities.dateFormat(dataContract.chgContractRequest.RequestDate));

    }

    private void setWidgetsEventListener() {
        // Event listener of TextView
        linearLayoutSaleDiscount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SalePayFragment fmCCTradeInBrand = BHFragment.newInstance(SalePayFragment.class);
                showNextView(fmCCTradeInBrand);
            }
        });

    }

}