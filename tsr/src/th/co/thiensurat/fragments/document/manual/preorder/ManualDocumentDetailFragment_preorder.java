package th.co.thiensurat.fragments.document.manual.preorder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ChangeContractController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DocumentHistoryController.DocumentType;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.ManualDocumentController;
import th.co.thiensurat.data.controller.ProblemController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.ManualDocumentInfo;
import th.co.thiensurat.data.info.ManualDocumentWithdrawalInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.ReceiptInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.contracts.change.ChangeContractPrintFragment;
import th.co.thiensurat.fragments.sales.preorder.SaleFirstPaymentChoiceFragment_preorder;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.business.controller.TSRController.getManualDocumentByNumber;
import static th.co.thiensurat.business.controller.TSRController.getManualDocumentWithdrawalByVolume;

public class ManualDocumentDetailFragment_preorder extends BHFragment {

    @InjectView ListView lvManualDocumentList;
    @InjectView TextView txtDocNo, lblDocNo, lblManualBookNo, lblManualRunningNo, lblNote;
    @InjectView EditText etManualBookNo, etManualRunningNo, etNote;
    @InjectView ViewTitle lblTitle;
    @InjectView LinearLayout li1,li2;


    public static class Data extends BHParcelable {
        public String DocumentNumber;
        public String DocumentNo;
        public String DocumentType;
        public SaleFirstPaymentChoiceFragment_preorder.ProcessType processType;
        public ChangeContractInfo changeContractInfo;
    }

    private Data dataManualDocument;

    private List<ManualDocumentInfo> manualDocList = null;
    private DocumentHistoryInfo manualDocHist;

    private ManualDocumentWithdrawalInfo docWithdrawal;
    private boolean flagShowListView;

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());


    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_manual_document_detail;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_manual_document;
    }

    @Override
    protected int[] processButtons() {
        if (dataManualDocument == null)
            dataManualDocument = getData();

        if (dataManualDocument.DocumentType.equals(DocumentType.Contract.toString())
                && dataManualDocument.processType != null
                && (dataManualDocument.processType == SaleFirstPaymentChoiceFragment_preorder.ProcessType.Sale
                || dataManualDocument.processType == SaleFirstPaymentChoiceFragment_preorder.ProcessType.ChangeContract))
            return new int[]{R.string.button_back, R.string.button_contract_confirm_preorder};
        else if (dataManualDocument.DocumentType.equals(DocumentType.Receipt.toString()))
            return new int[]{R.string.button_back, R.string.button_save_manual_receipt};
        else if (dataManualDocument.DocumentType.equals(DocumentType.Contract.toString()))
            return new int[]{R.string.button_back, R.string.button_save_manual_contract};
        else if (dataManualDocument.DocumentType.equals(DocumentType.ChangeProduct.toString()))
            return new int[]{R.string.button_back, R.string.button_save_manual_change_product};
        else if (dataManualDocument.DocumentType.equals(DocumentType.ImpoundProduct.toString()))
            return new int[]{R.string.button_back, R.string.button_save_manual_impound_product};
        else
            return new int[]{R.string.button_back};
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        super.onProcessButtonClicked(buttonID);
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_save_manual_receipt:
                CheckSaveManualDocument();
                break;
            case R.string.button_save_manual_contract:
                CheckSaveManualDocument();
                break;
            case R.string.button_save_manual_change_product:
                CheckSaveManualDocument();
                break;
            case R.string.button_save_manual_impound_product:
                CheckSaveManualDocument();
                break;
            case R.string.button_contract_confirm_preorder:
                CheckSaveManualDocument();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        Log.e("manual","11111");


        li1.setVisibility(View.GONE);
        li2.setVisibility(View.GONE);

        if (dataManualDocument == null)
            dataManualDocument = getData();

        int[] txt = new int[]{R.string.manual_document_detail_title, R.string.manual_document_detail_doc_no, R.string.manual_document_detail_manual_book_no
                , R.string.manual_document_detail_manual_running_no, R.string.manual_document_detail_note};
        if (dataManualDocument.DocumentType.equals(DocumentType.Contract.toString())) {
            flagShowListView = false;
            txt = new int[]{R.string.manual_document_contract_title, R.string.manual_document_contract_doc_no, R.string.manual_document_contract_book_no
                    , R.string.manual_document_contract_running_no, R.string.manual_document_detail_note};
        } else if (dataManualDocument.DocumentType.equals(DocumentType.Receipt.toString())) {
            flagShowListView = true;
            txt = new int[]{R.string.manual_document_detail_title, R.string.manual_document_detail_doc_no, R.string.manual_document_detail_manual_book_no
                    , R.string.manual_document_detail_manual_running_no, R.string.manual_document_detail_note};
        } else if (dataManualDocument.DocumentType.equals(DocumentType.ChangeProduct.toString())) {
        } else if (dataManualDocument.DocumentType.equals(DocumentType.ImpoundProduct.toString())) {
        }

        lblTitle.setText("บันทึกใบจอง");
        lblDocNo.setText("เลขที่ใบจอง");
        lblManualBookNo.setText(getActivity().getResources().getString(txt[2]));
        lblManualRunningNo.setText(getActivity().getResources().getString(txt[3]));
        lblNote.setText(getActivity().getResources().getString(txt[4]));

        if (dataManualDocument.DocumentType.equals(DocumentType.Contract.toString())
                && dataManualDocument.processType != null
                && (dataManualDocument.processType == SaleFirstPaymentChoiceFragment_preorder.ProcessType.Sale)) {

            Log.e("qqq2","1111");
            txtDocNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo"));

        } else {
         //   txtDocNo.setText(dataManualDocument.DocumentNo);
            txtDocNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo"));


            Log.e("qqq","1111");
        }

        LayoutInflater li = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) li.inflate(R.layout.list_manual_document_list_header, lvManualDocumentList, false);
        lvManualDocumentList.addHeaderView(header, null, false);

        if (flagShowListView) {
            lvManualDocumentList.setVisibility(View.VISIBLE);
        } else {
            lvManualDocumentList.setVisibility(View.GONE);
        }

        new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // ดึงค่าข้อมูลการใช้เอกสารมือ (Contract-Receipt) ออกมา
                manualDocList = getManualDocumentByDocumentNumber(dataManualDocument.DocumentNumber);

                if (dataManualDocument.DocumentType.equals(DocumentType.Contract.toString()) && manualDocList != null && manualDocList.size() > 0) {
                    // (A) Edit-Mode (Contract)
                    docWithdrawal = new ManualDocumentWithdrawalInfo();
                    docWithdrawal.ManualRunningNo = manualDocList.get(0).ManualRunningNo;
                    docWithdrawal.VolumeNo = manualDocList.get(0).ManualVolumeNo;
                } else {
                    // (B) Add-Mode (Contract + Receipt)
                    docWithdrawal = getManualDocumentWithdrawalByVolumeNoASC(dataManualDocument.DocumentType, BHPreference.employeeID());
                    if (docWithdrawal != null) {
                        if (docWithdrawal.MaxRunningNo > 0) {
                            /*** [START] :: Fixed - [BHPROJ-0018-3171] :: [Web-Admin-การเบิก-คืนเอกสารมือ] กรณีที่มีการเบิกเอกสารมือ เล่มเก่ามาใช้งานต่อ ตอนทำคืนเอกสาร แสดงเลขที่เริ่มต้นการคืนไม่ถูกต้อง   ***/
                            if(docWithdrawal.StartNo <= docWithdrawal.MaxRunningNo){
                                docWithdrawal.ManualRunningNo = docWithdrawal.MaxRunningNo + 1;
                            } else {
                                docWithdrawal.ManualRunningNo = docWithdrawal.StartNo;
                            }
                            /*** [END] :: Fixed - [BHPROJ-0018-3171] :: [Web-Admin-การเบิก-คืนเอกสารมือ] กรณีที่มีการเบิกเอกสารมือ เล่มเก่ามาใช้งานต่อ ตอนทำคืนเอกสาร แสดงเลขที่เริ่มต้นการคืนไม่ถูกต้อง  ***/
                        } else {
                            docWithdrawal.ManualRunningNo = docWithdrawal.StartNo;
                        }
                    }
                }
            }

            @Override
            protected void after() {
                if (docWithdrawal != null) {
                    if (dataManualDocument.DocumentType.equals(DocumentType.Contract.toString())
                            && dataManualDocument.processType != null
                            && (dataManualDocument.processType == SaleFirstPaymentChoiceFragment_preorder.ProcessType.Sale)) {

                        String asubstring = BHPreference.saleCode().substring(0, 6);

                      //  txtDocNo.setText(asubstring+String.valueOf(docWithdrawal.ManualRunningNo));

                        txtDocNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo"));


                        Log.e("qqq","2222");

                    }
                    else {
                        txtDocNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo"));

                        Log.e("qqq2","2222");
                    }

             //       etManualBookNo.setText(docWithdrawal.VolumeNo);
              //      etManualRunningNo.setText(String.valueOf(docWithdrawal.ManualRunningNo));


                    etManualBookNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo"));
                    etManualRunningNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo"));

                } else {
            /*        showDialog("แจ้งเตือน", "ไม่พบการเบิกเอกสาร");
                    showLastView();
                    return;*/

                    etManualBookNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo"));
                    etManualRunningNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo"));

                }

                if (flagShowListView && dataManualDocument.DocumentType.equals(DocumentType.Receipt.toString()) && manualDocList != null && manualDocList.size() > 0) {
                    bindManualDocument();
                }
            }
        }.start();


    }

    private void CheckSaveManualDocument() {

        beforeSaveManualDocument();
/*        String title = "กรุณาตรวจสอบข้อมูล";
        String message = "";
        if (!etManualBookNo.getText().toString().equals("") && !etManualRunningNo.getText().toString().equals("")) {
            if (dataManualDocument.DocumentType.equals(DocumentType.Contract.toString()) && manualDocList != null && manualDocList.size() > 0) {
                if (Long.parseLong(etManualRunningNo.getText().toString()) == manualDocList.get(0).ManualRunningNo
                        && etManualBookNo.getText().toString().equals(manualDocList.get(0).ManualVolumeNo)) {
                    beforeSaveManualDocument();
                    return;
                }
            }*/
/*            ManualDocumentWithdrawalInfo chkDocWithdrawal = getManualDocumentWithdrawalByVolume(dataManualDocument.DocumentType, etManualBookNo.getText().toString(), BHPreference.employeeID());
            if (chkDocWithdrawal != null) {
                long checked = Long.parseLong(etManualRunningNo.getText().toString());
                if (checked >= chkDocWithdrawal.StartNo && checked <= chkDocWithdrawal.EndNo) {
                    if (checked > chkDocWithdrawal.MaxRunningNo) {
                        beforeSaveManualDocument();
                        return;
                    } else {
                        ManualDocumentInfo docInfo = getManualDocumentByNumber(dataManualDocument.DocumentType, etManualBookNo.getText().toString(), etManualRunningNo.getText().toString());
                        if(docInfo != null) {
                            message = String.format("เอกสารเลขที่ %d ถูกใช้งานแล้ว", checked);
                        } else {
                            beforeSaveManualDocument();
                            return;
                        }
                    }
                } else {
                    message = String.format("สามารถเบิกเอกสารเลขที่ %d-%d ได้เท่านั้น", chkDocWithdrawal.StartNo, chkDocWithdrawal.EndNo);
                }
            } else {
                message = "ไม่พบการเบิกเอกสาร เล่มที่ " + etManualBookNo.getText().toString();
            }
            */



   /*     } else {
            message = "กรุณาใส่ข้อมูลให้ครบ";
        }*/
       // showNoticeDialogBox(title, message);
    }

    public void beforeSaveManualDocument() {
        if (dataManualDocument.DocumentType.equals(DocumentType.Contract.toString())
                && dataManualDocument.processType != null
                && (dataManualDocument.processType == SaleFirstPaymentChoiceFragment_preorder.ProcessType.Sale
                || dataManualDocument.processType == SaleFirstPaymentChoiceFragment_preorder.ProcessType.ChangeContract)) {
            final String title = "คำเตือน";
            final String message = "คุณต้องการบันทึกใบสัญญามือและใบสัญญาใช่หรือไม่";
            AlertDialog.Builder builder = BHUtilities.builderDialog(activity, title, message);
            builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SaveManualDocument();
                }
            });
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        } else {
            SaveManualDocument();
        }
    }

    private void SaveManualDocument() {
        new BackgroundProcess(activity) {

            ManualDocumentInfo manualDocumentInfo = new ManualDocumentInfo();    // ข้อมูลการใช้เอกสารมือ
            DocumentHistoryInfo docHist = new DocumentHistoryInfo();    // ข้อมูลการส่งเอกสาร

            @Override
            protected void calling() {
                if (manualDocList != null && manualDocList.size() > 0 && dataManualDocument.DocumentType.equals(DocumentType.Contract.toString())) {
                    // (A) Edit-Mode (Contract)
                    manualDocumentInfo = manualDocList.get(0);
                    manualDocumentInfo.ManualVolumeNo = etManualBookNo.getText().toString();
                  //  manualDocumentInfo.ManualRunningNo = Long.parseLong(etManualRunningNo.getText().toString());
                    manualDocumentInfo.ManualRunningNo = Long.parseLong("1234");

                    manualDocumentInfo.Note = etNote.getText().toString();
                    manualDocumentInfo.TeamCode = BHPreference.teamCode();
                    manualDocumentInfo.SubTeamCode = BHPreference.selectTeamCodeOrSubTeamCode();
                    manualDocumentInfo.EmpID = BHPreference.employeeID();
                    manualDocumentInfo.UpdateBy = BHPreference.employeeID();
                    manualDocumentInfo.UpdateDate = new Date();
                    manualDocumentInfo.isActive = true;
                    updateManualDocument(manualDocumentInfo);

                    // Fixed for [BHPRJ00301-3764] : Don't send Contract-ManualDocument
                    /*
                    manualDocHist = getDocumentHistoryByDocumentNumber(manualDocumentInfo.DocumentID, DocumentHistoryController.DocumentType.ManualDocument.toString());
                    manualDocHist.DocumentNumber = manualDocumentInfo.DocumentID;
                    manualDocHist.DatePrint = new Date();
                    manualDocHist.LastUpdateDate = new Date();
                    manualDocHist.LastUpdateBy = BHPreference.employeeID();
                	updateDocumentHistory(manualDocHist, true);
                    */

                } else {
                    // (B) Add-Mode (Contract + Receipt)
                    manualDocumentInfo = new ManualDocumentInfo();
                    manualDocumentInfo.DocumentID = DatabaseHelper.getUUID();
                    manualDocumentInfo.DocumentType = DocumentType.ManualDocument.toString();
                    manualDocumentInfo.DocumentNumber = dataManualDocument.DocumentNumber;    // RefNo/ReceiptID
                    manualDocumentInfo.ManualDocTypeID = dataManualDocument.DocumentType;

                    manualDocumentInfo.ManualVolumeNo = etManualBookNo.getText().toString();
            //        manualDocumentInfo.ManualRunningNo = Long.parseLong(etManualRunningNo.getText().toString());
                    manualDocumentInfo.ManualRunningNo = Long.parseLong("1234");

                    manualDocumentInfo.Note = etNote.getText().toString();
                    manualDocumentInfo.TeamCode = BHPreference.teamCode();
                    manualDocumentInfo.SubTeamCode = BHPreference.selectTeamCodeOrSubTeamCode();
                    manualDocumentInfo.EmpID = BHPreference.employeeID();
                    manualDocumentInfo.CreatedBy = BHPreference.employeeID();
                    manualDocumentInfo.CreatedDate = new Date();
                    manualDocumentInfo.UpdateBy = "";
                    manualDocumentInfo.UpdateDate = null;
                    manualDocumentInfo.SyncedDate = new Date();
                    manualDocumentInfo.isActive = true;
                    addManualDocument(manualDocumentInfo);

                    docHist.PrintHistoryID = DatabaseHelper.getUUID();
                    docHist.OrganizationCode = BHPreference.organizationCode();
                    docHist.DatePrint = new Date();
                    docHist.DocumentType = DocumentType.ManualDocument.toString();
                    docHist.DocumentNumber = manualDocumentInfo.DocumentID;
                    docHist.SyncedDate = new Date();
                    docHist.CreateBy = BHPreference.employeeID();
                    docHist.CreateDate = new Date();
                    docHist.LastUpdateDate = null;
                    docHist.LastUpdateBy = "";
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
                    if (manualDocList != null && !dataManualDocument.DocumentType.equals(DocumentType.Contract.toString())) {
                        docHist.PrintOrder = (manualDocList.size() * 2) + 1;
                    }
                    // Fixed for [BHPRJ00301-3764] : Don't send Contract-ManualDocument 
                    if (!dataManualDocument.DocumentType.equals(DocumentType.Contract.toString())) {
                        addDocumentHistory(docHist, true);
                        // Fixed for [BHPRJ00301-3765] : Print Receipt-ManualDocument 1 time (Has a copy) was occur 2 documents  
                        docHist.PrintHistoryID = DatabaseHelper.getUUID();
                        docHist.PrintOrder = docHist.PrintOrder + 1;
                        addDocumentHistory(docHist, true);
                    }

                }
            }

            @Override
            protected void after() {
                if(dataManualDocument.changeContractInfo == null){
                    showLastView();
                } else {
                    updateChangeContract(dataManualDocument.changeContractInfo);
                }

            }
        }.start();
    }

    private void bindManualDocument() {

        BHArrayAdapter<ManualDocumentInfo> adapter = new BHArrayAdapter<ManualDocumentInfo>(activity, R.layout.list_manual_document_list_item, manualDocList) {

            class ViewHolder {
                public TextView tvManualDocumentBookRunningNo;
                public TextView tvDocumentNo;
            }

            @Override
            protected void onViewItem(int position, View view, Object holder, ManualDocumentInfo info) {
                ViewHolder vh = (ViewHolder) holder;
                long ManualRunningNo_Integer = info.ManualRunningNo;
                String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(info.ManualVolumeNo), ManualRunningNo_Integer).replace(' ', '0');
                vh.tvManualDocumentBookRunningNo.setText(ManualDocumentBookRunningNo);
                vh.tvDocumentNo.setText(dataManualDocument.DocumentNo);
            }
        };

        lvManualDocumentList.setAdapter(adapter);
    }


    private void updateChangeContract(final ChangeContractInfo cont) {
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
                    //-- Fixed - [BHPROJ-0016-993] :: เพิ่ม Field เพื่อเก็บค่า RefNo ของข้อมูลที่มาจากระบบเก่าตอนทำ Migrate และยังเก็บค่าเลขที่อ้างอิง/เลขที่เอกสารสัญญามือที่เกิดคู่กับสัญญาฉบับนั้น ๆ ในกรณีที่เป็นสัญญาที่เกิดจากระบบ TSR Application
                    NewContractInfo.ContractReferenceNo = etManualRunningNo.getText().toString();

                    TSRController.updateContractForChangeContrac(OldContractInfo, true);
                    TSRController.updateContractForChangeContrac(NewContractInfo, true);

                    String receiptCode = getAutoGenerateDocumentID(DocumentGenType.Receipt, BHPreference.SubTeamCode(), BHPreference.saleCode());
                    List<ReceiptInfo> receiptInfoList = TSRController.getReceiptByRefNo(cont.NewSaleID);
                    if (receiptInfoList != null) {
                        for (ReceiptInfo info : receiptInfoList) {
                            info.ReceiptCode = receiptCode;
                            info.LastUpdateDate = new Date();
                            info.LastUpdateBy = BHPreference.employeeID();
                            TSRController.updateReceipt(info, true);
                        }
                    }

                    cont.Status = ChangeContractController.ChangeContractStatus.COMPLETED.toString();
                    cont.ResultProblemID = cont.RequestProblemID;
                    cont.ResultDetail = cont.RequestDetail;
                    cont.EffectiveDate = cont.RequestDate;
                    cont.EffectiveBy = BHPreference.employeeID();
//                    cont.ChangeContractPaperID = cont.ChangeContractID;
                    cont.ChangeContractPaperID = getAutoGenerateDocumentID(DocumentGenType.ChangeContract, BHPreference.SubTeamCode(), BHPreference.saleCode());
                    cont.LastUpdateDate = new Date();
                    cont.LastUpdateBy = BHPreference.employeeID();

                    // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                    cont.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                    /*AssignInfo assign = new AssignInfo();
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
                    assign.ReferenceID = cont.ChangeContractID;*/

                    TSRController.actionChangeContract1(cont);

                    List<ManualDocumentInfo> manualDocumentList = new ManualDocumentController().getManualDocumentForChangeContract(cont.OldSaleID
                            , DocumentType.ManualDocument.toString(), DocumentType.Contract.toString());
                    if (manualDocumentList != null && manualDocumentList.size() > 0) {
                        ManualDocumentInfo manualDocumentInfo = manualDocumentList.get(0);
                        DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                        docHist.PrintHistoryID = DatabaseHelper.getUUID();
                        docHist.OrganizationCode = BHPreference.organizationCode();
                        docHist.DatePrint = new Date();
                        docHist.DocumentType = DocumentType.ManualDocument.toString();
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
    }

    public void showNoticeDialogBox(final String title, final String message) {
        AlertDialog.Builder setupAlert;
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

}
