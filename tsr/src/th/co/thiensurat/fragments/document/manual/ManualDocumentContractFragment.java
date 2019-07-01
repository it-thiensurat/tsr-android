package th.co.thiensurat.fragments.document.manual;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.ManualDocumentInfo;
import th.co.thiensurat.data.info.ManualDocumentWithdrawalInfo;

public class ManualDocumentContractFragment extends BHFragment {

    @InjectView
    TextView txtDocNo;
    @InjectView
    EditText etBookNo, etRunningNo, etNote;

    public static class Data extends BHParcelable {
        public String DocumentRef;
        public String DocumentNo;
        public String DocumentType;

    }

    private Data dataManualDocument;

    private ManualDocumentInfo manualDoc;
    private DocumentHistoryInfo manualDocHist;
    private ManualDocumentInfo manualDocByManualDocTypeID;

    private ManualDocumentWithdrawalInfo docWithdrawal;


    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_manual_document_contract;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_manual_document;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub

        return new int[]{R.string.button_back, R.string.button_save_manual_contract};

    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        super.onProcessButtonClicked(buttonID);
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;

            case R.string.button_save_manual_contract:
//			int num ;
//			num = Integer.parseInt(etRunningNo.getText().toString());
//			if(num >= docWithdrawal.StartNo && num <= docWithdrawal.EndNo ){
//				SaveManualDocument();
//			}
                if (etRunningNo.getText().toString().equals("") || etBookNo.getText().toString().equals("")) {
                    String title = "กรุณาตรวจสอบข้อมูล";
                    String message = "กรุณาใส่ข้อมูลให้ครบ";
                    showNoticeDialogBox(title, message);
                } else {

                    SaveManualDocument();
                }


            default:
                break;
        }
    }

    private void showNoticeDialogBox(String title, String message) {
        // TODO Auto-generated method stub
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity);
        setupAlert.setTitle(title);
        setupAlert.setMessage(message);
        setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        setupAlert.show();
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (dataManualDocument == null)
            dataManualDocument = getData();

        txtDocNo.setText(dataManualDocument.DocumentNo);


        new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub

                manualDoc = getManualDocumentContractByDocumentNumber(dataManualDocument.DocumentRef);

                if (manualDoc == null) {
                    docWithdrawal = getManualDocumentWithdrawalByType(DocumentHistoryController.DocumentType.Contract.toString());

                    if(docWithdrawal != null) {
                        manualDocByManualDocTypeID = getManualDocumentByManualDocTypeID(DocumentHistoryController.DocumentType.Contract.toString());
                        if (manualDocByManualDocTypeID != null) {
                            docWithdrawal.ManualRunningNo = manualDocByManualDocTypeID.ManualRunningNo + 1;
                        } else {
                            docWithdrawal.ManualRunningNo = docWithdrawal.StartNo;
                        }
                    }
                }

            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (manualDoc != null) {

                    etBookNo.setText(manualDoc.ManualVolumeNo);
                    etRunningNo.setText("" + manualDoc.ManualRunningNo);
                    etNote.setText(manualDoc.Note);
                } else {
                    if (docWithdrawal != null) {
                        etBookNo.setText(docWithdrawal.VolumeNo);
                        //int runing = docWithdrawal.ManualRunningNo + 1;
                        //etRunningNo.setText(""+runing);
                        etRunningNo.setText(String.valueOf(docWithdrawal.ManualRunningNo));
                        etNote.setText("");
                    } else {
                        showDialog("แจ้งเตือน", "ไม่พบการเบิกเอกสาร");
                        showLastView();
                    }

                }

            }
        }.start();
    }


    private void SaveManualDocument() {
        new BackgroundProcess(activity) {

            ManualDocumentInfo manualDocumentInfo = new ManualDocumentInfo();
            DocumentHistoryInfo docHist = new DocumentHistoryInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub


                manualDocumentInfo.DocumentID = DatabaseHelper.getUUID();
                manualDocumentInfo.DocumentType = DocumentHistoryController.DocumentType.ManualDocument.toString();
                manualDocumentInfo.DocumentNumber = dataManualDocument.DocumentRef;
                manualDocumentInfo.ManualDocTypeID = DocumentHistoryController.DocumentType.Contract.toString();
                manualDocumentInfo.ManualVolumeNo = etBookNo.getText().toString();
                manualDocumentInfo.ManualRunningNo = Long.parseLong(etRunningNo.getText().toString());
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


                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = new Date();
                docHist.DocumentType = DocumentHistoryController.DocumentType.ManualDocument.toString();
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


            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub


                if (manualDoc != null) {

                    manualDoc.DocumentType = manualDoc.DocumentType;
                    manualDoc.DocumentNumber = manualDoc.DocumentNumber;
                    manualDoc.ManualDocTypeID = manualDoc.ManualDocTypeID;
                    manualDoc.ManualVolumeNo = etBookNo.getText().toString();
                    manualDoc.ManualRunningNo = Long.parseLong(etRunningNo.getText().toString());
                    manualDoc.Note = etNote.getText().toString();
                    manualDoc.TeamCode = BHPreference.teamCode();
                    manualDoc.SubTeamCode = BHPreference.selectTeamCodeOrSubTeamCode();
                    manualDoc.EmpID = BHPreference.employeeID();
                    manualDoc.UpdateBy = BHPreference.employeeID();
                    manualDoc.UpdateDate = new Date();
                    manualDoc.isActive = true;

                    updateManualDocument(manualDoc);

                    manualDocHist = getDocumentHistoryByDocumentNumber(manualDoc.DocumentID, DocumentHistoryController.DocumentType.ManualDocument.toString());

                    manualDocHist.DocumentNumber = manualDoc.DocumentID;
                    manualDocHist.DatePrint = new Date();
                    manualDocHist.LastUpdateDate = new Date();
                    manualDocHist.LastUpdateBy = BHPreference.employeeID();

                    updateDocumentHistory(manualDocHist, true);
                } else {

                    addManualDocument(manualDocumentInfo);
                    addDocumentHistory(docHist, true);


                }

//				manualDocHist = getDocumentHistoryByDocumentNumber(manualDoc.DocumentID, DocumentHistoryController.DocumentType.ManualDocument.toString());
//
//				if (manualDocHist != null) {
//					
//					manualDocHist.DocumentNumber = manualDoc.DocumentID;
//					manualDocHist.DatePrint = new Date();
//					manualDocHist.LastUpdateDate = new Date();
//					manualDocHist.LastUpdateBy = BHPreference.employeeID();
//					
//					updateDocumentHistory(manualDocHist, true);
//
//				} else {
//
//					
//					addDocumentHistory(docHist, true);
//
//				}


            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                super.after();
                showLastView();
            }
        }.start();
    }
//	private void bindManualDocument() {
//		
//		
//		
//		
//		
//		
//
//		BHArrayAdapter<ManualDocumentInfo> adapter = new BHArrayAdapter<ManualDocumentInfo>(activity, R.layout.list_manual_document_list_item, manualDocList) {
//
//			class ViewHolder {
//				public TextView tvManualDocumentBookRunningNo;
//				public TextView tvDocumentNo;
//			}
//
//			@Override
//			protected void onViewItem(int position, View view, Object holder, ManualDocumentInfo info) {
//				// TODO Auto-generated method stub
//				try {
//					ViewHolder vh = (ViewHolder) holder;
////					String ManualRunningNo = "0";
////					if (info.ManualRunningNo != null && !info.ManualRunningNo.equals(""))
////						ManualRunningNo = BHUtilities.trim(info.ManualRunningNo);
////					Integer ManualRunningNo_Integer = Integer.parseInt(ManualRunningNo);
//					String ManualDocumentVolumeNo = String.format("%s", BHUtilities.trim(info.ManualVolumeNo));
//					vh.tvManualDocumentBookRunningNo.setText(ManualDocumentVolumeNo);
//					vh.tvDocumentNo.setText(BHUtilities.trim(info.DocumentNumber));
//
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//			}
//		};
//
//		lvManualDocumentList.setAdapter(adapter);

    // lvManualDocumentList.setOnItemClickListener(new OnItemClickListener()
    // {
    //
    // @Override
    // public void onItemClick(AdapterView<?> parent, View view, int
    // position, long id) {
    // // TODO Auto-generated method stub
    //
    // final SalePaymentPeriodInfo loss = lossList.get(position - 1);
    //
    //
    // (new BackgroundProcess(activity) {
    //
    //
    // ContractInfo result;
    //
    // @Override
    // protected void before() {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // protected void calling() {
    // // TODO Auto-generated method stub
    // result = getContractBySerialNo(BHPreference.organizationCode(),
    // loss.ProductSerialNumber, ContractStatus.NORMAL.toString());
    // }
    //
    // @Override
    // protected void after() { // TODO
    // // Auto-generated
    // // method stub
    // if (result != null) {
    // dataDetail = new LossDetailFragment.Data();
    // dataDetail.Serialnumber = result.ProductSerialNumber;
    // dataDetail.Name = result.ProductName;
    //
    // LossDetailFragment fmLossDetail =
    // BHFragment.newInstance(LossDetailFragment.class, dataDetail);
    //
    // showNextView(fmLossDetail);
    //
    // }
    //
    // }
    //
    // }).start();
    //
    // }
    //
    // });

}


