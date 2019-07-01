package th.co.thiensurat.fragments.sendmoney;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ChannelController;
import th.co.thiensurat.data.controller.SendMoneyController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.SendMoneyInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SendMoneySummarySendListFragment extends BHFragment {

	public static String FRAGMENT_SEND_MONEY_SUMMARY_MAIN_TAG = "send_money_summary_main_tag";
	private List<SendMoneyInfo> mSendMoneyList;
	private int mSelectedPayment = -1;
	private BHListViewAdapter adapter;

	@InjectView private ListView lvSumPaymentDate;
	

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_send;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sendmoney_summary_send_list;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		loadData();
		setWidgetsEventListener();
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
	}

	private void loadData() {
		(new BackgroundProcess(activity) {

			List<SendMoneyInfo> output;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void calling() {
//				String EmpIDOfMember;
//				if (BHPreference.PositionCode().contains("SaleLeader")) {
//					EmpIDOfMember = new EmployeeDetailController().getMemberByTeamCodeOrSubTeamCode(BHPreference.organizationCode(), BHPreference.teamCode(), "");
//				} else if (BHPreference.PositionCode().contains("SubTeamLeader")) {
//					EmpIDOfMember = new EmployeeDetailController().getMemberByTeamCodeOrSubTeamCode(BHPreference.organizationCode(), "", BHPreference.SubTeamCode());
//				} else {
//					EmpIDOfMember = BHPreference.employeeID();
//				}
				output = getSummarySendMoneyGroupByPaymentDate(BHPreference.organizationCode(), BHPreference.employeeID());
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					mSendMoneyList = null;
					mSendMoneyList = new ArrayList<SendMoneyInfo>();
					if(mSendMoneyList != null && output != null) {
						for(int i=0; i<output.size(); i++) {
							if(!output.get(i).Reference2.equals("MigrateRef2")) {
								mSendMoneyList.add(output.get(i));
							}
						}
					}

				//	mSendMoneyList = output;
					if (mSendMoneyList != null)
						bindPaymentList();
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.getMessage());					
				}
			}
		}).start();
	}

	private void setWidgetsEventListener() {
		lvSumPaymentDate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				mSelectedPayment = position - 1;
				print(mSelectedPayment);
			}
		});
	}

	private void bindPaymentList() {
		
		adapter = new BHListViewAdapter(activity) {
			
			class ViewHolder {
				public TextView txtPaymentDate;
				public TextView txtTransactionNo;
				public TextView txtSendAmount;
				public TextView txtSendDate;
				public ImageButton btnAdd;
				public LinearLayout llAdd;
				//public ImageButton btnEdit;
				/*** Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/
				public ImageView imageDelete;
			}

			
			@Override
			protected int viewForSectionHeader(int section) {
				// TODO Auto-generated method stub
				return R.layout.list_sendmoney_summary_send_list_header;
			}
			
			@Override
			protected int viewForItem(int section, int row) {
				// TODO Auto-generated method stub
				return R.layout.list_sendmoney_summary_send_list_item;
			}
			
		
			
			@Override
			protected int getItemCount(int section) {
				// TODO Auto-generated method stub
				return mSendMoneyList != null ? mSendMoneyList.size() : 0;
			}
		
				
		
			@Override
			protected void onViewItem(View view, Object holder, int section, final int row) {
				// TODO Auto-generated method stub

				ViewHolder vh = (ViewHolder) holder;
				final SendMoneyInfo info = mSendMoneyList.get(row);
				try {					
					final String transNO = (info.TransactionNo == null) || info.TransactionNo.isEmpty() ? "" : info.TransactionNo;
					
					vh.txtPaymentDate.setText(BHUtilities.dateFormat(info.PaymentDate) + "\n" + info.Reference2);
					vh.txtTransactionNo.setText(transNO);
					vh.txtSendAmount.setText(BHUtilities.numericFormat(info.SendAmount));
					//vh.txtSendDate.setText(BHUtilities.dateFormat(info.SendDate, "dd/MM/yyyy HH:mm:ss"));

					//vh.btnEdit.setVisibility(view.GONE);
					if (transNO.equals("")){
						vh.llAdd.setVisibility(View.VISIBLE);
						vh.btnAdd.setVisibility(view.VISIBLE);
						//vh.btnEdit.setVisibility(view.GONE);
						vh.txtSendDate.setText("");
						/*** [START] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/
						if (info.Status.equals(SendMoneyController.SendMoneyStatus.RECEIVED.toString())) {
							vh.imageDelete.setVisibility(View.GONE);
						} else {
							vh.imageDelete.setVisibility(View.VISIBLE);
						}
						/*** [START] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/
					} else {
						vh.llAdd.setVisibility(View.GONE);
						vh.btnAdd.setVisibility(view.GONE);
						//vh.btnEdit.setVisibility(view.VISIBLE);
						// Fixed - [BHPRJ00301-3971]
//						vh.txtSendDate.setText(BHUtilities.dateFormat(info.SendDate, "dd/MM/yyyy HH:mm"));
						vh.txtSendDate.setText(BHUtilities.dateFormat(info.SaveTransactionNoDate, "dd/MM/yyyy") + "\n" + BHUtilities.dateFormat(info.SaveTransactionNoDate, "HH:mm:ss"));
						/*** Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/
						vh.imageDelete.setVisibility(View.GONE);
					}
					
					vh.btnAdd.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							SendMoneyInfo sm = mSendMoneyList.get(row);
							SendMoneySpecifyTransactionNoFragment.Data selectedData = new SendMoneySpecifyTransactionNoFragment.Data();
							selectedData.sendMoneyID = sm.SendMoneyID;
							selectedData.transactionNo = "";
							SendMoneySpecifyTransactionNoFragment fm = BHFragment.newInstance(SendMoneySpecifyTransactionNoFragment.class, selectedData);
							showNextView(fm);
						}
					});

					vh.txtPaymentDate.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							print(row);
						}
					});

					vh.txtTransactionNo.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							print(row);
						}
					});

					vh.txtSendAmount.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							print(row);
						}
					});

					vh.txtSendDate.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							print(row);
						}
					});
//
//					vh.btnEdit.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							SendMoneyInfo sm = mSendMoneyList.get(row);
//							SendMoneySpecifyTransactionNoFragment.Data selectedData = new SendMoneySpecifyTransactionNoFragment.Data();
//							selectedData.sendMoneyID = sm.SendMoneyID;
//							selectedData.transactionNo = transNO;
//							SendMoneySpecifyTransactionNoFragment fm = BHFragment.newInstance(SendMoneySpecifyTransactionNoFragment.class, selectedData);
//							showNextView(fm);
//						}
//					});

					/*** [START] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/
					vh.imageDelete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							final String title = "คำเตือน";
							final String message = "คุณต้องการลบข้อมูลการนำส่งเงินเลขที่ " + info.Reference2 + " หรือไม่?";
							showYesNoDialogBoxForConfirmDelete(title, message, info);
						}
					});
					/*** [END] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/

				} catch (Exception e) {
					// e.printStackTrace();
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}
		};
		lvSumPaymentDate.setAdapter(adapter);
	}

	private void print(int position) {
		String strHeadOffice = "";
		String strBankName = "";
		String strCounterServiceName = "";
		String strAccountCode1 = "";
		String strChannelCode = "";
		String strChannelName = "";
		//ChannelItemInfo ci;
		SendMoneyPrintFragment.Data selectedData = new SendMoneyPrintFragment.Data();
		
		SendMoneyInfo sm = mSendMoneyList.get(position);
		strChannelCode = sm.ChannelCode;
		strChannelName = sm.ChannelName; 
		
		if (sm.ChannelCode.toLowerCase().trim().equals(ChannelController.ChannelCode.HeadOffice.toString().toLowerCase())) {
			strHeadOffice = sm.ChannelItemName;
			strAccountCode1 = sm.AccountCode1; 		
			selectedData.headOfficeOrBranch = strHeadOffice;

		} else if (sm.ChannelCode.toLowerCase().trim().equals(ChannelController.ChannelCode.Bank.toString().toLowerCase())) {
			strBankName = sm.ChannelItemName;
			strAccountCode1 = sm.AccountCode1;		
			selectedData.bankName = strBankName;

		} else if (sm.ChannelCode.toLowerCase().trim().equals(ChannelController.ChannelCode.CounterService.toString().toLowerCase())) {
			strCounterServiceName = sm.ChannelItemName;
			strAccountCode1 = sm.AccountCode1;						
			selectedData.counterService = strCounterServiceName;
		}
		
		selectedData.payeeName = sm.PayeeName;
		selectedData.reference2 = sm.Reference2;
		selectedData.channelCode = strChannelCode;
		selectedData.channelName = strChannelName;
		selectedData.accountCode1 = strAccountCode1;
		selectedData.empName = BHPreference.employeeID();
		selectedData.empCode = BHPreference.employeeID();
		selectedData.paymentType = sm.PaymentType;
		selectedData.paymentTypeName = sm.PaymentTypeName;
		selectedData.sendDate = sm.SendDate;
		selectedData.sendAmount = sm.SendAmount;
		selectedData.sendMoneyID = sm.SendMoneyID;
		selectedData.reference1 = sm.Reference1;

		SendMoneyPrintFragment fm = BHFragment.newInstance(
				SendMoneyPrintFragment.class, selectedData);
		showNextView(fm);
	}

	/*** [START] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/
	private void showYesNoDialogBoxForConfirmDelete(final String title, final String message, final SendMoneyInfo sm) {
		AlertDialog.Builder setupAlert;
		setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
				.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						deleteSendMoney(sm);
					}
				}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// just do nothing
					}
				});
		setupAlert.show();
	}

	private void deleteSendMoney(final SendMoneyInfo info){
		(new BackgroundProcess(activity) {

			@Override
			protected void before() {}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				TSRController.deleteSendMoneyByID(info.SendMoneyID, true);
			}

			@Override
			protected void after() {
				// TODO Auto-generated
				loadData();
				showMessage("ลบข้อมูลการส่งเงินเลขที่ " + info.Reference2 + " เรียบร้อยแล้ว");
				showNextView(BHFragment.newInstance(SendMoneySummaryMainFragment.class));
			}

		}).start();
	}
	/*** [END] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/


}