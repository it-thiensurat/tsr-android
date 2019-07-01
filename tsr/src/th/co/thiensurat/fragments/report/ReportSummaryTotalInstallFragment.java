package th.co.thiensurat.fragments.report;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.ReportInstallAndPaymentInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/

public class ReportSummaryTotalInstallFragment extends BHFragment {

	public static class Data extends BHParcelable {
		public String fortnight;
		public String teamcode;
	}

	private Data data;
	// private ReportSummaryTotalFragment parent;

	@InjectView
	private ListView lvTotalInstall;
	private BHListViewAdapter adapter;
	private static final String INSTALL = "Install";

	public String EMP, YEAR, TEAM;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_total_install;
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

	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// data = getData();
		loadData();

	}

	public void refreshData(String fortNight, String empID, String teamSale) {
		// Log.i("TEST", fortNight);
		EMP = empID;
		YEAR = fortNight;
		TEAM = teamSale;
		loadData();
	}

	public void loadData() {
		(new BackgroundProcess(activity) {
			List<ReportInstallAndPaymentInfo> report;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub

				try {
					if (EMP == null) {
						report = getReportByTeam(YEAR, INSTALL, TEAM);
					} else {
						report = getReportBySale(YEAR, INSTALL, EMP);
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
							public TextView txtCount_Install_Cash;
							public TextView txtCount_Install_Credit;
							public TextView txtSum_Count_Cash;
							public TextView txtSum_Count_Credit;

						}

						@Override
						protected int viewForSectionHeader(int section) {
							// TODO Auto-generated method stub

							return R.layout.list_report_summary_total_header;

						}

						@Override
						protected int viewForItem(int section, int row) {
							// TODO Auto-generated method stub
							return R.layout.list_report_summary_total_item;
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
								ReportInstallAndPaymentInfo info = report
										.get(row);
								ViewHolder vh = (ViewHolder) holder;
								vh.txtDate.setText(BHUtilities.dateFormat(
										info.ReportDate,
										BHUtilities.DEFAULT_DATE_FORMAT));
								vh.txtHold_Cash.setText(BHUtilities
										.numericFormat(info.Count_Hold_Cash));
								vh.txtHold_Credit.setText(BHUtilities
										.numericFormat(info.Count_Hold_Credit));
								vh.txtCount_Install_Cash
										.setText(BHUtilities
												.numericFormat(info.Count_Install_Cash));
								vh.txtCount_Install_Credit
										.setText(BHUtilities
												.numericFormat(info.Count_Install_Credit));
								vh.txtSum_Count_Cash.setText(BHUtilities
										.numericFormat(info.Sum_Count_Cash));
								vh.txtSum_Count_Credit.setText(BHUtilities
										.numericFormat(info.Sum_Count_Credit));

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

					lvTotalInstall.setAdapter(adapter);
					lvTotalInstall.setVisibility(View.VISIBLE);

				} else {

					lvTotalInstall.setVisibility(View.GONE);

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
