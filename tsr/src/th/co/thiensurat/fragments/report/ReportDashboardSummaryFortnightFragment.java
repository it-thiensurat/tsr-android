package th.co.thiensurat.fragments.report;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetDashboardReportInputInfo;
import th.co.thiensurat.service.data.GetDashboardReportOutputInfo;
import th.co.thiensurat.service.data.ReportDashboardPaymentAmountInfo;
import th.co.thiensurat.service.data.ReportDashboardPaymentInfo;
import th.co.thiensurat.service.data.ReportDashboardRequestInfo;
import th.co.thiensurat.service.data.ReportDashboardTeamInfo;

/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/

public class ReportDashboardSummaryFortnightFragment extends BHFragment {
	@InjectView
	private ListView lvReportInstall;
	@InjectView
	private ListView lvReportCredit;
	@InjectView
	private TextView txtLoss,txtImound,txtChangeProduct,txtChangeContract,txtCount,txtTotal;
	
	private static final String SUP = "Supervisor";
	float sumcredit , sumtotal ,sumcash , sumcount , sum;
	
	List<ReportDashboardTeamInfo> dbTeamList;
	List<ReportDashboardPaymentInfo> dbPayList;
	ReportDashboardPaymentAmountInfo dbPayAmt;
	ReportDashboardRequestInfo dbReq;
	
	List<ReportDashboardPaymentAmountInfo> outputReport;
	
	
	
	private BHListViewAdapter adapter;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_dashboard_summary_daily;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.main_menu_total;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		Data();
	
	}

	private void Data() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			GetDashboardReportInputInfo input = new GetDashboardReportInputInfo();
			GetDashboardReportOutputInfo output = new GetDashboardReportOutputInfo();

			@Override
			protected void before() {
				// TODO Auto-generated method stub

				input.FortnightID = BHPreference.fortnightPerYear();
				input.TeamCode = BHPreference.teamCode();
				
				if (BHPreference.PositionCode().equals(SUP)) {
					input.IsSVP = true;
				}else {
					input.IsSVP = false;
				}
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = TSRService.reportGetDashboard(input, false);

			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					dbTeamList = output.Info.DashboardTeam;

					dbPayList = new ArrayList<ReportDashboardPaymentInfo>();
					dbPayList.add(0, output.Info.DashboardPayment.get(0));

					dbPayAmt = output.Info.DashboardPaymentAmount;

					dbReq = output.Info.DashboardRequest;

					outputReport = new ArrayList<ReportDashboardPaymentAmountInfo>();
					ReportDashboardPaymentAmountInfo outputInfo = new ReportDashboardPaymentAmountInfo();
					// dbPayAmt = new ReportDashboardPaymentAmountInfo();
					// //วันนี้ output.Info.DashboardPayment.get(1)
					outputInfo.Sum_Cash_Appointment = output.Info.DashboardPayment
							.get(0).CashAppointmentCount;
					outputInfo.Sum_Cash_Payment = output.Info.DashboardPayment
							.get(0).CashPaymentCount;
					outputInfo.Sum_Cash_Incomplete_Payment = output.Info.DashboardPayment
							.get(0).CashRemainCount;
					outputInfo.Sum_Credit_Appointment = output.Info.DashboardPayment
							.get(0).CreditAppointmentCoun;
					outputInfo.Sum_Credit_Incomplete_Payment = output.Info.DashboardPayment
							.get(0).CreditRemainCount;
					outputInfo.Sum_Credit_Payment = output.Info.DashboardPayment
							.get(0).CreditPaymentCount;

					// outputInfo.Sum_Cash_Appointment = 1;
					// outputInfo.Sum_Cash_Payment = 2;
					// outputInfo.Sum_Cash_Incomplete_Payment = 3;
					// outputInfo.Sum_Credit_Appointment = 4;
					// outputInfo.Sum_Credit_Incomplete_Payment = 5;
					// outputInfo.Sum_Credit_Payment = 6;
					//
					//
					// dbPayAmt.Sum_Cash_Appointment = 5;
					// dbPayAmt.Sum_Cash_Incomplete_Payment = 3;
					// dbPayAmt.Sum_Cash_Payment = 2;
					// dbPayAmt.Sum_Credit_Appointment = 9;
					// dbPayAmt.Sum_Credit_Incomplete_Payment = 8;
					// dbPayAmt.Sum_Credit_Payment = 7;
					// //

					sumcount = outputInfo.Sum_Cash_Payment
							+ outputInfo.Sum_Credit_Payment;
					sum = dbPayAmt.Sum_Cash_Payment
							+ dbPayAmt.Sum_Credit_Payment;

					outputReport.add(0, outputInfo);
					outputReport.add(1, dbPayAmt);

					bindSummaryCreditList();
					bindSummaryInstallList();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}		
				
				
							
				
			}

			private void bindSummaryCreditList() {
				// TODO Auto-generated method stub
//				
				adapter = new BHListViewAdapter(activity) {
					class ViewHolder {
						public TextView txtTitle;
						public TextView txtSumCash;
						public TextView txtCash;
						public TextView txtNotCash;
						public TextView txtSumCredit;
						public TextView txtCredit;
						public TextView txtNotCredit;
					}
					
					

					@Override
					protected int viewForSectionHeader(int section) {
						// TODO Auto-generated method stub

						return R.layout.list_report_dashboard_summary_daily_credit_header;
					}

					@Override
					protected int viewForItem(int section, int row) {
						// TODO Auto-generated method stub
						return R.layout.list_report_dashboard_summary_daily_credit_item;
					}

					
					@Override
					protected void onViewItem(View view, Object holder,
							int section, int row) {
						// TODO Auto-generated method stub
					try {
						
						
					
						
						
						ReportDashboardPaymentAmountInfo info = outputReport.get(row);
						ViewHolder vh = (ViewHolder) holder;
//							
							if (row == 0) {
								vh.txtTitle.setText(String.format("%s ",BHUtilities.trim("วันนี้")));
							} else {
								vh.txtTitle.setText(String.format("%s ",BHUtilities.trim("รวมจำนวนเงิน")));
							}
								
								vh.txtSumCash.setText(BHUtilities.numericFormat(info.Sum_Cash_Appointment, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtCash.setText(BHUtilities.numericFormat(info.Sum_Cash_Payment, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtNotCash.setText(BHUtilities.numericFormat(info.Sum_Cash_Incomplete_Payment, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtSumCredit.setText(BHUtilities.numericFormat(info.Sum_Credit_Appointment, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtCredit.setText(BHUtilities.numericFormat(info.Sum_Credit_Payment, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtNotCredit.setText(BHUtilities.numericFormat(info.Sum_Credit_Incomplete_Payment, BHUtilities.DEFAULT_INTEGER_FORMAT));
								
								

							
																		
				
					} catch (Exception e) {
						// TODO: handle exception
						  e.printStackTrace();
					}
						
					}

					@Override
					protected int getItemCount(int section) {
						// TODO Auto-generated method stub
						return 2 ;
					}
				};

				lvReportCredit.setAdapter(adapter);
				
				
						txtCount.setText(BHUtilities.numericFormat(sumcount,BHUtilities.DEFAULT_INTEGER_FORMAT));
						txtTotal.setText(BHUtilities.numericFormat(sum,BHUtilities.DEFAULT_INTEGER_FORMAT));

			}

			private void bindSummaryInstallList() {
				// TODO Auto-generated method stub

				adapter = new BHListViewAdapter(activity) {
					class ViewHolder {
						public TextView txtTitle;
						public TextView txtTarget;
						public TextView txtSummary;
						public TextView txtSumCash;
						public TextView txtCash;
						public TextView txtNotCash;
						public TextView txtSumCredit;
						public TextView txtCredit;
						public TextView txtNotCredit;
					}

					@Override
					protected int viewForSectionHeader(int section) {
						// TODO Auto-generated method stub

						return R.layout.list_report_dashboard_summary_daily_header;
					}

					@Override
					protected int viewForItem(int section, int row) {
						// TODO Auto-generated method stub
						return R.layout.list_report_dashboard_summary_daily_item;
					}

					
					@Override
					protected void onViewItem(View view, Object holder,
							int section, int row) {
						// TODO Auto-generated method stub
					try {
						
						ReportDashboardTeamInfo info = dbTeamList.get(row);
						ViewHolder vh = (ViewHolder) holder;
						vh.txtTitle.setText(String.format("%s ", BHUtilities.trim(info.EmployeeName)));
						vh.txtTarget.setText(BHUtilities.numericFormat(info.Target));
						
						vh.txtCash.setText(BHUtilities.numericFormat(info.Count_Cash_Complete_Payment));
						vh.txtNotCash.setText(BHUtilities.numericFormat(info.Count_Cash_Incomplete_Payment));
						
						vh.txtCredit.setText(BHUtilities.numericFormat(info.Count_Credit_Complete_Payment));
						vh.txtNotCredit.setText(BHUtilities.numericFormat(info.Count_Credit_Incomplete_Payment));
						
						sumcash = info.Count_Cash_Complete_Payment + info.Count_Cash_Incomplete_Payment;
						sumcredit = info.Count_Credit_Complete_Payment + info.Count_Credit_Incomplete_Payment;
						sumtotal = sumcash + sumcredit;
						
						vh.txtSummary.setText(BHUtilities.numericFormat(sumtotal, BHUtilities.DEFAULT_INTEGER_FORMAT));
						vh.txtSumCash.setText(BHUtilities.numericFormat(sumcash, BHUtilities.DEFAULT_INTEGER_FORMAT));
						vh.txtSumCredit.setText(BHUtilities.numericFormat(sumcredit, BHUtilities.DEFAULT_INTEGER_FORMAT));
//						
						
//						vh.txtTitle.setText("วันนี้");
//						vh.txtSumCash.setText("0");
//						vh.txtCash.setText("0");
//						vh.txtNotCash.setText("0");
//						vh.txtSumCredit.setText("0");
//						vh.txtCredit.setText("0");
//						vh.txtNotCredit.setText("0");
//						
						
					} catch (Exception e) {
						// TODO: handle exception
						  e.printStackTrace();
					}
						
					}

					@Override
					protected int getItemCount(int section) {
						// TODO Auto-generated method stub
						return dbTeamList != null ? dbTeamList.size() : 0;
					//return 3;
					}
				};

				lvReportInstall.setAdapter(adapter);
				DataReq();
			

			}

			

			private void DataReq() {
				// TODO Auto-generated method stub
				txtImound.setText(BHUtilities.numericFormat(dbReq.Count_ImpoundProduct));
				txtChangeProduct.setText(BHUtilities.numericFormat(dbReq.Count_ChangeProduct));
				txtChangeContract.setText(BHUtilities.numericFormat(dbReq.Count_ChangeContract));
				txtLoss.setText(BHUtilities.numericFormat(dbReq.Count_WriteOffNPL));
			}
		}).start();

	}

}
