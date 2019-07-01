package th.co.thiensurat.fragments.impound;

import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.ImpoundProductController.ImpoundProductStatus;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleContractPrintFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.AddOrUpdateAssignActionImpoundProductInputInfo;
import th.co.thiensurat.service.data.AddOrUpdateAssignActionImpoundProductOutputInfo;
import th.co.thiensurat.service.data.GetContractByRefNoInputInfo;
import th.co.thiensurat.service.data.GetImpoundProductByRequestTeamCodeInputInfo;
import th.co.thiensurat.service.data.GetImpoundProductByRequestTeamCodeOutputInfo;
import th.co.thiensurat.service.data.UpdateImpoundProductInputInfo;
import th.co.thiensurat.service.data.UpdateImpoundProductOutputInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/********************************************************
 *
 *
 * [Checked@09/03/2016] ยังไม่เปิดให้ใช้ Feature นี้ (Feature Approved รายการขออนุมัติถอดเครื่องบน Android)
 *
 *
 *****************************************************/


public class ImpoundProductListRequestApproveFragment extends BHFragment {

	private List<ImpoundProductInfo> impoundRequestlist;
	private ImpoundProductInfo selectimpoundRequest;

	@InjectView
	private ListView listimpoundRequest;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_impound_product_request_approve;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back };
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		doBindImpoundRequest();
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = activity.getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_impound_request_head, listimpoundRequest, false);
		listimpoundRequest.addHeaderView(header, null, false);
	}

	private void doBindImpoundRequest() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			GetImpoundProductByRequestTeamCodeInputInfo input = new GetImpoundProductByRequestTeamCodeInputInfo();
			GetImpoundProductByRequestTeamCodeOutputInfo output = new GetImpoundProductByRequestTeamCodeOutputInfo();

			@Override
			protected void before() {
				// TODO Auto-generated method stub
//				input.OrganizationCode = BHPreference.organizationCode();
//				input.AssigneeTeamCode = BHPreference.teamCode();
                input.OrganizationCode = BHPreference.organizationCode();
                input.RequestTeamCode = BHPreference.teamCode();
                input.EmployeeID = BHPreference.employeeID();
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = TSRService.getImpoundProductByRequestTeamCode(input, false);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (output.Info != null) {
					impoundRequestlist = output.Info;
				}
				bindimpoundRequestList();
			}

		}).start();
	}

	private void bindimpoundRequestList() {
		// TODO Auto-generated method stub
		BHArrayAdapter<ImpoundProductInfo> adapter = new BHArrayAdapter<ImpoundProductInfo>(activity, R.layout.list_impound_request, impoundRequestlist) {

			class ViewHolder {
				public TextView txtCONTNO, txtSerialNo, txtTeamRequest, txtAuthorization;
			}

			@Override
			protected void onViewItem(int position, View view, Object holder, ImpoundProductInfo info) {
				// TODO Auto-generated method stub
				
				String Status = null;

                if (info.Status != null) {
                    if (info.Status.equals(ImpoundProductStatus.REQUEST.toString())) {
                        Status = "N";
                    } else if (info.Status.equals(ImpoundProductStatus.APPROVED.toString())) {
                        Status = "Y";
                    }
                } else {
                    Status = "";
                }
                
				ViewHolder vh = (ViewHolder) holder;
				vh.txtCONTNO.setText(info.CONTNO);
				vh.txtSerialNo.setText(info.ProductSerialNumber);
				//vh.txtTeamRequest.setText(info.RequestBy);
				vh.txtTeamRequest.setText(info.RequestTeamCode);
				vh.txtAuthorization.setText(BHUtilities.trim(Status));

			}

		};
		listimpoundRequest.setAdapter(adapter);
		listimpoundRequest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				//selectimpoundRequest = (ImpoundProductInfo) listimpoundRequest.getItemAtPosition(position);
				selectimpoundRequest = impoundRequestlist.get(position-1);
				ListImpoundRequest();
			}

		});
	}

	private void ListImpoundRequest() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {
			ContractInfo output;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getContractByRefNo(selectimpoundRequest.OrganizationCode, selectimpoundRequest.RefNo);
                if(output == null){
                    GetContractByRefNoInputInfo input = new GetContractByRefNoInputInfo();
                    input.OrganizationCode = selectimpoundRequest.OrganizationCode;
                    input.RefNo = selectimpoundRequest.RefNo;
                    output = TSRService.getContractByRefNo(input, false).Info;
                }
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (output != null) {
					final String title = "ยืนยันการอนุมัติคำร้องขอถอดเครื่อง";
					final String message = String.format("สถานะ : %s \nรหัสพนักงาน : %s \nชื่อพนักงาน : %s \nเลขที่สัญญา : %s \nหมายเลขเครื่อง : %s \nทีมขออนุมัติถอดเครื่อง : %s \nวันเวลาที่ขออนุมัติถอดเครื่อง : %s", selectimpoundRequest.Status,
							selectimpoundRequest.RequestBy, selectimpoundRequest.RequestName, selectimpoundRequest.CONTNO, selectimpoundRequest.ProductSerialNumber, selectimpoundRequest.RequestTeamCode, BHUtilities.dateTimeFormat(selectimpoundRequest.RequestDate));
					Builder setupAlert;
					setupAlert = new Builder(activity).setTitle(title).setMessage(message)
							.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
                                    UpdateStatusImpoundProduct();
								}

							}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									// just do nothing
								}
							}).setNeutralButton(getResources().getString(R.string.dialog_detail), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									// just do nothing
									BHPreference.setProcessType(ProcessType.ViewCompletedContract.toString());
									BHPreference.setRefNo(selectimpoundRequest.RefNo);
									showNextView(new SaleContractPrintFragment());
								}
							});
					setupAlert.show();
				} else {
					final String title = "ยืนยันการอนุมัติคำร้องขอถอดเครื่อง";
					final String message = "ไม่พบสัญญา";
					Builder setupAlert;
					setupAlert = new Builder(activity).setTitle(title).setMessage(message)
							.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

								}
							});
					setupAlert.show();

				}
			}
		}).start();
	}

	private void UpdateStatusImpoundProduct() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			//UpdateImpoundProductInputInfo input = new UpdateImpoundProductInputInfo();
			//UpdateImpoundProductOutputInfo output = new UpdateImpoundProductOutputInfo();

            ImpoundProductInfo impound = new ImpoundProductInfo();
            AssignInfo assignInfo = new AssignInfo();

			@Override
			protected void before() {
				// TODO Auto-generated method stub

				//input.ImpoundProductID = selectimpoundRequest.ImpoundProductID;
				//input.OrganizationCode = selectimpoundRequest.OrganizationCode;
				//input.RefNo = selectimpoundRequest.RefNo;
				//input.Status = ImpoundProductStatus.APPROVED.toString();
				//input.RequestProblemID = selectimpoundRequest.RequestProblemID;
				//input.RequestDetail = selectimpoundRequest.RequestDetail;
				//input.RequestDate = new Date();
				//input.RequestBy = selectimpoundRequest.RequestBy;
				//input.RequestTeamCode = selectimpoundRequest.RequestTeamCode;
				//input.ApproveDetail = selectimpoundRequest.ApproveDetail;
				//input.ApprovedDate = new Date();
				//input.ApprovedBy = BHPreference.teamCode();
                //input.ApprovedBy = BHPreference.employeeID();
				//input.ResultProblemID = selectimpoundRequest.ResultProblemID;
				//input.ResultDetail = selectimpoundRequest.ResultDetail;
				//input.EffectiveDate = new Date();
				//input.EffectiveBy = selectimpoundRequest.EffectiveBy;
				//input.ImpoundProductPaperID = selectimpoundRequest.ImpoundProductPaperID;
				//input.CreateDate = selectimpoundRequest.CreateDate;
				//input.CreateBy = selectimpoundRequest.CreateBy;
				//input.LastUpdateDate = new Date();
				//input.LastUpdateBy = selectimpoundRequest.LastUpdateBy;

//                selectimpoundRequest.Status = ImpoundProductStatus.APPROVED.toString();
//                selectimpoundRequest.ApprovedDate = new Date();
//                selectimpoundRequest.ApprovedBy = BHPreference.teamCode();

                impound.ImpoundProductID = selectimpoundRequest.ImpoundProductID;
                impound.ApproveDetail = "ImpoundProduct";
                impound.ApprovedDate = new Date();
                impound.ApprovedBy = BHPreference.employeeID();
                impound.LastUpdateDate = new Date();
                impound.LastUpdateBy = selectimpoundRequest.LastUpdateBy;

                assignInfo = new AssignInfo();
                assignInfo.AssignID = DatabaseHelper.getUUID();
                assignInfo.TaskType = AssignController.AssignTaskType.ImpoundProduct
                        .toString();
                assignInfo.OrganizationCode = selectimpoundRequest.OrganizationCode;
                assignInfo.RefNo = selectimpoundRequest.RefNo;
                assignInfo.AssigneeEmpID = selectimpoundRequest.RequestBy;//BHPreference.userID();
                assignInfo.AssigneeTeamCode = selectimpoundRequest.RequestTeamCode;
                assignInfo.ReferenceID = selectimpoundRequest.ImpoundProductID;
                assignInfo.CreateBy = BHPreference.employeeID();
                assignInfo.LastUpdateBy = BHPreference.employeeID();
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				//output = TSRService.updateImpoundProduct(input, true);
                approveImpoundProduct(impound, assignInfo);
			}
		}).start();
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

}
