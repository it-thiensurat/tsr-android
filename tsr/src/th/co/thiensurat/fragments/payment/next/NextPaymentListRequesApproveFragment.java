
/********************************************************
 *
 *
 * [Checked@09/09/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/

/*
package th.co.thiensurat.fragments.payment.next;

import java.util.List;

import org.apache.http.impl.conn.ProxySelectorRoutePlanner;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ProblemContractController.ProblemContractStatus;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.data.info.ProblemContractInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetImpoundProductByRequestTeamCodeInputInfo;
import th.co.thiensurat.service.data.GetImpoundProductByRequestTeamCodeOutputInfo;
import th.co.thiensurat.service.data.GetProblemContractByTeamByStatusInputInfo;
import th.co.thiensurat.service.data.GetProblemContractByTeamByStatusOutputInfo;
import th.co.thiensurat.service.data.UpdateProblemContractInputInfo;


public class NextPaymentListRequesApproveFragment extends BHFragment {

	private List<ProblemContractInfo> NextPaymentRequestlist;
	private ProblemContractInfo selectNextPaymentRequest;

	@InjectView ListView listNextPaymentdRequest;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_next_payment_reques_approve;
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
		doBindNextPaymentRequest();
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = activity.getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_next_payment_request_head, listNextPaymentdRequest, false);
		listNextPaymentdRequest.addHeaderView(header, null, false);
	}

	private void doBindNextPaymentRequest() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			GetProblemContractByTeamByStatusInputInfo input = new
                    GetProblemContractByTeamByStatusInputInfo();
			GetProblemContractByTeamByStatusOutputInfo output = new
                    GetProblemContractByTeamByStatusOutputInfo();

			@Override
			protected void before() {
				// TODO Auto-generated method stub
				input.OrganizationCode = BHPreference.organizationCode();
				input.SaleTeamCode = BHPreference.teamCode();
                input.Status= ProblemContractStatus.REQUEST.toString();

			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = TSRService.getProblemContractByTeamByStatus(input, false);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (output.Info != null) {
				NextPaymentRequestlist = output.Info;
				}
				bindNextPaymentRequestList();
			}

		}).start();
	}

	private void bindNextPaymentRequestList() {
		// TODO Auto-generated method stub
		BHArrayAdapter<ProblemContractInfo> adapter = new BHArrayAdapter<ProblemContractInfo>(activity, R.layout.list_next_payment_request, NextPaymentRequestlist) {

			class ViewHolder {
				public TextView txtCONTNO, txtSerialNo, txtTeamRequest, txtEmpName;
			}

			@Override
			protected void onViewItem(int position, View view, Object holder, ProblemContractInfo info) {
				// TODO Auto-generated method stub
				ViewHolder vh = (ViewHolder) holder;
				vh.txtCONTNO.setText(info.CONTNO);
				vh.txtSerialNo.setText(info.ProductSerialNumber);
				vh.txtTeamRequest.setText(info.AssignedEmployee);
				vh.txtEmpName.setText(info.CreateBy);

			}

		};
		listNextPaymentdRequest.setAdapter(adapter);
		listNextPaymentdRequest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				selectNextPaymentRequest = (ProblemContractInfo) listNextPaymentdRequest.getItemAtPosition(position);
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
				output = getContractByRefNo(BHPreference.organizationCode(), selectNextPaymentRequest.RefNo);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (output != null) {
					final String title = "ยืนยันการอนุมัติคำร้องเก็บเงินงวดต่อไป";
					final String message = String.format("เลขที่สัญญา : %s \nหมายเลขเครื่อง : %s \nทีมขออนุมัติเก็บเงินงวดต่อไป : %s",
							selectNextPaymentRequest.CONTNO, selectNextPaymentRequest.ProductSerialNumber, selectNextPaymentRequest.AssignedEmployee);
					Builder setupAlert;
					setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
							.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									(new BackgroundProcess(activity) {

										UpdateProblemContractInputInfo input = new UpdateProblemContractInputInfo();

										@Override
										protected void before() {
											// TODO Auto-generated method stub
											input.ProblemContract = selectNextPaymentRequest.ProblemContract;
											input.RefNo = selectNextPaymentRequest.RefNo;
											input.CONTNO = selectNextPaymentRequest.CONTNO;
											input.SalePaymentPeriodID = selectNextPaymentRequest.SalePaymentPeriodID;
											input.PaymentPeriodNumber = selectNextPaymentRequest.PaymentPeriodNumber;
											input.ReceivedProblemDate = selectNextPaymentRequest.ReceivedProblemDate;
											input.DataSourceSystem = selectNextPaymentRequest.DataSourceSystem;
											input.Status = ProblemContractStatus.APPROVED.toString();
											input.ProblemCode = selectNextPaymentRequest.ProblemCode;
											input.ProbemDetail = selectNextPaymentRequest.ProbemDetail;
											input.AssignedEmployee = selectNextPaymentRequest.AssignedEmployee;
											input.CreateDate = selectNextPaymentRequest.CreateDate;
											input.CreateBy = selectNextPaymentRequest.CreateBy;
											input.LastUpdateDate = selectNextPaymentRequest.LastUpdateDate;
											input.LastUpdateBy = selectNextPaymentRequest.LastUpdateBy;
											input.SyncedDate = selectNextPaymentRequest.SyncedDate;
										}

										@Override
										protected void calling() {
											// TODO Auto-generated method stub
											TSRService.updateProblemContract(input, false);
										}

                                        @Override
                                        protected void after() {
                                            super.after();
                                            onResume();
                                        }
                                    }).start();

								}

							}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									// just do nothing
								}
							});
					setupAlert.show();
				} else {
					final String title = "ยืนยันการอนุมัติคำร้องขอถอดเครื่อง";
					final String message = "ไม่พบสัญญา";
					Builder setupAlert;
					setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
							.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

								}
							});
					setupAlert.show();

				}
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
*/