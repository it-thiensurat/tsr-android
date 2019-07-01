package th.co.thiensurat.fragments.report;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.ReportInstallAndPaymentInfo;
import th.co.thiensurat.data.info.ReportFirstAndNextReceiveInfo;
import th.co.thiensurat.data.info.ReportInventoryInfo;

/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/

public class ReportSummaryCreditCrFragment extends BHFragment {

	@InjectView
	private ListView lvCredit;
	private BHListViewAdapter adapter;
	public String TEAM, YEAR;

	private float sumHold_Cash;
	private float sumHold_Credit;
	private float sumInstall_Cash;
	private float sumInstall_Credit;
	private float sumFirst;
	private float sumNext;
	
	
	
	
	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_credit_cr;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_print };
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.main_menu_default;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		loadData();
	}

	public void refreshData(String fortNight, String teamCode) {
		// Log.i("TEST", fortNight);
		TEAM = teamCode;
		YEAR = fortNight;
		loadData();

	}

	private void loadData() {
		// TODO Auto-generated method stub

		(new BackgroundProcess(activity) {
			List<ReportFirstAndNextReceiveInfo> report;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
//				String fortnight = YEAR;
//				String teamcode = TEAM;
				try {
					report = getReportFirstandNext(YEAR, TEAM);
					
					if (report != null) {
						report.add(report.size(), new ReportFirstAndNextReceiveInfo());
						report.add(report.size(), new ReportFirstAndNextReceiveInfo());

						
//						public TextView txtDate;
//						public TextView txtHold_Cash;
//						public TextView txtHold_Credit;
//						public TextView txtInstall_Cash;
//						public TextView txtInstall_Credit;
//						public TextView txtFirst;
//						public TextView txtNext;

						sumHold_Cash = 0;
						sumHold_Credit = 0;
						sumInstall_Cash = 0;
						sumInstall_Credit = 0;
						sumFirst = 0;
						sumNext = 0;

						for (int i = 0; i < report.size(); i++) {
							sumHold_Cash = sumHold_Cash + report.get(i).ReceiveOutFresh;
							sumHold_Credit = sumHold_Credit	+ report.get(i).ReceiveOutInstallment;
							sumInstall_Cash = sumInstall_Cash + report.get(i).ReceiveStainFresh;
							sumInstall_Credit = sumInstall_Credit + report.get(i).ReceiveStainInstallment;
							sumFirst = sumFirst	+ report.get(i).FirstMoney;
							sumNext = sumNext + report.get(i).NextMoney;
						}

						report.get(report.size() - 1).ReceiveOutFresh = sumHold_Cash;
						report.get(report.size() - 1).ReceiveOutInstallment = sumHold_Credit;
						report.get(report.size() - 1).ReceiveStainFresh = sumInstall_Cash;
						report.get(report.size() - 1).ReceiveStainInstallment = sumInstall_Credit;
						report.get(report.size() - 1).FirstMoney = sumFirst;
						report.get(report.size() - 1).NextMoney = sumNext;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (report != null) {
					adapter = new BHListViewAdapter(activity) {
						class ViewHolder {
							public TextView txtDate;
							public TextView txtHold_Cash;
							public TextView txtHold_Credit;
							public TextView txtInstall_Cash;
							public TextView txtInstall_Credit;
							public TextView txtFirst;
							public TextView txtNext;

						}

						@Override
						protected int viewForSectionHeader(int section) {
							// TODO Auto-generated method stub

							return R.layout.list_report_summary_credit_header;

						}

						@Override
						protected int viewForItem(int section, int row) {
							// TODO Auto-generated method stub
							return R.layout.list_report_summary_credit_item;
						}

						@Override
						protected void onViewItem(View view, Object holder,
								int section, int row) {
							// TODO Auto-generated method stub
							// ViewHolder vh = (ViewHolder) holder;
							// vh.txtTitle.setText("19/12/2557");
							// vh.txtTarget.setText("30");
							// vh.txtSummary.setText("10");
							// vh.txtSumCash.setText("2");
							// vh.txtPayCash.setText("1");
							// vh.txtNotCash.setText("1");
							// vh.txtSumInstallment.setText("8");
							try {
								ReportFirstAndNextReceiveInfo info = report.get(row);
								ViewHolder vh = (ViewHolder) holder;
								vh.txtDate.setText(BHUtilities.dateFormat(
										info.Date,
										BHUtilities.DEFAULT_DATE_FORMAT));
								vh.txtHold_Cash.setText(BHUtilities.numericFormat(info.ReceiveOutFresh, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtHold_Credit
										.setText(BHUtilities
												.numericFormat(info.ReceiveOutInstallment, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtInstall_Cash
										.setText(BHUtilities
												.numericFormat(info.ReceiveStainFresh, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtInstall_Credit
										.setText(BHUtilities
												.numericFormat(info.ReceiveStainInstallment, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtFirst
										.setText(BHUtilities
												.numericFormat(info.FirstMoney, BHUtilities.DEFAULT_INTEGER_FORMAT));
								vh.txtNext.setText(BHUtilities
										.numericFormat(info.NextMoney, BHUtilities.DEFAULT_INTEGER_FORMAT));
								
								if (row == report.size() - 1) {
									vh.txtDate.setText("ยอดรวม");

								} else if (row == report.size() - 2) {
									vh.txtHold_Cash.setText("");
									vh.txtHold_Credit.setText("");
									vh.txtInstall_Cash.setText("");
									vh.txtInstall_Credit.setText("");
									vh.txtFirst.setText("");
									vh.txtNext.setText("");

								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						}

						@Override
						protected int getItemCount(int section) {
							// TODO Auto-generated method stub
							return report != null ? report.size() : 0;
						}
					};
					lvCredit.setVisibility(View.VISIBLE);
					lvCredit.setAdapter(adapter);
				}
				else {
					lvCredit.setVisibility(View.GONE);
				}

			}
		}).start();

	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_print:

			break;

		case R.string.button_back:
			showLastView();
			break;

		default:
			break;
		}
	}

}
