package th.co.thiensurat.fragments.report;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.ReportApprovedInfo;
import th.co.thiensurat.data.info.ReportCustomerProblemInfo;
import android.os.Bundle;
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

public class ReportSummaryRequestImpoundFragment extends BHFragment {

	public static class Data extends BHParcelable {
		public String fortnight;
		public String teamcode;

	}

	private Data data;

	@InjectView
	private ListView lvImpound;

	private BHListViewAdapter adapter;

	public String TEAM, YEAR;
	private static final String IMPOUND = "Impound";

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_request_impound;
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
		
	}

	public void refreshData(String fortNight, String teamCode) {
		// Log.i("TEST", fortNight);
		TEAM = teamCode;
		YEAR = fortNight;
		loadData();

	}

	private void loadData() {
		(new BackgroundProcess(activity) {

			List<ReportApprovedInfo> report;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				report = getReportRequest(YEAR, TEAM, IMPOUND);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (report != null) {
					adapter = new BHListViewAdapter(activity) {
						class ViewHolder {
							public TextView txtDateRequest;
							public TextView txtStatus;
							public TextView txtCustomer;
							public TextView txtApproved;
							public TextView txtEmp;
							public TextView txtDateApproved;
							

						}

						@Override
						protected int viewForSectionHeader(int section) {
							// TODO Auto-generated method stub

							return 0;

						}

						@Override
						protected int viewForItem(int section, int row) {
							// TODO Auto-generated method stub
							return R.layout.list_report_summary_request;
						}

						@Override
						protected void onViewItem(View view, Object holder,
								int section, int row) {
							// TODO Auto-generated method stub
//							ViewHolder vh = (ViewHolder) holder;
//							vh.txtDateInstall.setText("19/12/2557");
//							vh.txtDateNotify.setText("30");
//							vh.txtFortnight.setText("10");
//							vh.txtEmp.setText("2");
//							vh.txtCus.setText("1");
//							vh.txtDateClear.setText("1");
//							vh.txtProblem.setText("8");
							try {
								ReportApprovedInfo info = report.get(row);
								ViewHolder vh = (ViewHolder) holder;
								vh.txtDateRequest.setText(BHUtilities.dateFormat(info.RequestDate,BHUtilities.DEFAULT_DATE_FORMAT));
								vh.txtStatus.setText(BHUtilities.trim(info.Status));
								vh.txtCustomer.setText(BHUtilities.trim(info.CustomerName));
								vh.txtApproved.setText(BHUtilities.trim(info.EffectiveBy));
								vh.txtEmp.setText(BHUtilities.trim(info.SaleLevel01));
								vh.txtDateApproved.setText(BHUtilities.dateFormat(info.EffectiveDate,BHUtilities.DEFAULT_DATE_FORMAT));
								
								
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

					lvImpound.setAdapter(adapter);
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
