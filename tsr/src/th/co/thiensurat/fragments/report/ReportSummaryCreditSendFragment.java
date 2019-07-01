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
import th.co.thiensurat.data.info.ReportFirstAndNextReceiveInfo;

/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/


public class ReportSummaryCreditSendFragment extends BHFragment {

	@InjectView
	private ListView lvSend;
	private BHListViewAdapter adapter;

	public String TEAM, YEAR;

	private float sumTotal;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_credit_send;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.main_menu_default;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_print };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		loadData();
	}

	public void refreshData(String fortNight, String teamCode) {
		Log.i("TEST", fortNight);
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
				String fortnight = YEAR;
				String teamcode = TEAM;
				try {
					report = getReportFirstandNext(fortnight, teamcode);
					if (report != null) {
						report.add(report.size(),
								new ReportFirstAndNextReceiveInfo());
						report.add(report.size(),
								new ReportFirstAndNextReceiveInfo());

						sumTotal = 0;

						for (int i = 0; i < report.size(); i++) {
							sumTotal = sumTotal + report.get(i).MoneyToSend;

						}

						report.get(report.size() - 1).MoneyToSend = sumTotal;

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
							public TextView txtDateCredit;
							public TextView txtRef;
							public TextView txtTotal;
							public TextView txtDateSendMoney;

						}

						@Override
						protected int viewForSectionHeader(int section) {
							// TODO Auto-generated method stub

							return R.layout.list_report_summary_credit_send_header;

						}

						@Override
						protected int viewForItem(int section, int row) {
							// TODO Auto-generated method stub
							return R.layout.list_report_summary_credit_send_item;
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

								ViewHolder vh = (ViewHolder) holder;
								vh.txtDateCredit.setText(BHUtilities
										.dateFormat(report.get(row).Date)
										.toString());
								vh.txtRef.setText(String.valueOf(report
										.get(row).MoneyIndex));
								vh.txtDateSendMoney
										.setText(BHUtilities
												.dateFormat(
														report.get(row).SendDateAndTime)
												.toString());
								vh.txtTotal.setText(String.valueOf(report
										.get(row).MoneyToSend));

								if (vh.txtTotal != null) {
									vh.txtTotal
											.setText(BHUtilities.numericFormat(
													report.get(row).MoneyToSend,
													BHUtilities.DEFAULT_INTEGER_FORMAT));
								}

								if (row == report.size() - 1) {
									vh.txtDateCredit.setText("ยอดรวม");
									vh.txtRef.setText("");
									vh.txtDateSendMoney.setText("");
								} else if (row == report.size() - 2) {
									vh.txtDateCredit.setText("");
									vh.txtRef.setText("");
									vh.txtTotal.setText("");
									vh.txtDateSendMoney.setText("");

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
					lvSend.setVisibility(View.VISIBLE);
					lvSend.setAdapter(adapter);
				} else {
					lvSend.setVisibility(View.GONE);
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
