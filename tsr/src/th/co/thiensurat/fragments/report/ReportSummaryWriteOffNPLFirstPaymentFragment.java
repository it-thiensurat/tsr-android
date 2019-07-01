package th.co.thiensurat.fragments.report;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ReportSummaryWriteOffNPLInfo;
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

public class ReportSummaryWriteOffNPLFirstPaymentFragment extends BHFragment {

	// @InjectView
	// private Spinner spnFortnight;
	@InjectView
	private ListView lvSummary;
	private BHListViewAdapter adapter;

	private final String WriteOffNPLType = "งวดแรก";

	private List<ReportSummaryWriteOffNPLInfo> reportDataList;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_write_off_npl;
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
		// return null;
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

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// getReportData("TEMP01","งวดแรก");
	}
/*
	public void getReportData() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			@Override
			protected void before() {
				// TODO Auto-generated method stub

			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				// reportDataList =
				// TSRController.getReportSummaryWriteOffNPL(ReportSummaryWriteOffNPLMainFragment.FortnightID,
				// "PAK-23", WriteOffNPLType);
				reportDataList = TSRController.getReportSummaryWriteOffNPL(
						ReportSummaryWriteOffNPLMainFragment.FortnightID,
						BHPreference.teamCode(), WriteOffNPLType);

			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {

					if (reportDataList != null)
						bindSummaryList();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void bindSummaryList() {

		adapter = new BHListViewAdapter(activity) {
			class ViewHolder {
				public TextView txtWriteOffNPLDate;
				public TextView txtProductSerialNumber;
				public TextView txtPaymentPeriod;
				public TextView txtPaymentAmount;
				public TextView txtProblemName;

			}

			@Override
			protected int viewForSectionHeader(int section) {
				// TODO Auto-generated method stub

				return R.layout.list_report_summary_write_off_npl_first_payment_header;

			}

			@Override
			protected int viewForItem(int section, int row) {
				// TODO Auto-generated method stub
				return R.layout.list_report_summary_write_off_npl_first_payment_item;
			}

			@Override
			protected void onViewItem(View view, Object holder, int section,
					int row) {
				// TODO Auto-generated method stub
				try {

					ViewHolder vh = (ViewHolder) holder;
					vh.txtWriteOffNPLDate
							.setText(BHUtilities.dateFormat(
									reportDataList.get(row).WriteOffNPLDate)
									.toString());
					vh.txtProductSerialNumber
							.setText(reportDataList.get(row).ProductSerialNumber);
					vh.txtPaymentAmount.setText(String.valueOf(reportDataList
							.get(row).PaymentAmount));
					vh.txtPaymentPeriod.setText(String.valueOf(reportDataList
							.get(row).PaymentPeriodNumber));
					vh.txtProblemName
							.setText(reportDataList.get(row).ProblemName);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			protected int getItemCount(int section) {
				// TODO Auto-generated method stub
				return reportDataList.size();
			}
		};

		lvSummary.setAdapter(adapter);
	}
*/
}
