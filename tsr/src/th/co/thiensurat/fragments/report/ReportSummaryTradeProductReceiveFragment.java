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
import th.co.thiensurat.data.info.ReportSummaryTradeProductReceiveInfo;
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

public class ReportSummaryTradeProductReceiveFragment extends BHFragment {

	// @InjectView
	// private Spinner spnFortnight;
	@InjectView
	private ListView lvSummary;
	private BHListViewAdapter adapter;

	private List<ReportSummaryTradeProductReceiveInfo> reportDataList;

	private int sumTSRProductCount;
	private int sumOtherProductCount;
	private int sumProductCountTotal;
	private int sumReturnProductCount;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_trade_product;
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
		//return null;
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
		// getReportData();
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
				// TSRController.getReportSummaryTradeProductReceive("TEMP01",
				// "PAK-30", "");
				// reportDataList =
				// TSRController.getReportSummaryTradeProductReceive(ReportSummaryTradeProductMainFragment.FortnightID,
				// "PAK-30", ReportSummaryTradeProductMainFragment.SaleCode);

				reportDataList = TSRController.getReportSummaryTradeProductReceive(ReportSummaryTradeProductMainFragment.FortnightID, BHPreference.teamCode(),
						ReportSummaryTradeProductMainFragment.SaleCode);

				if (reportDataList != null) {
					reportDataList.add(reportDataList.size(), new ReportSummaryTradeProductReceiveInfo());
					reportDataList.add(reportDataList.size(), new ReportSummaryTradeProductReceiveInfo());

					sumTSRProductCount = 0;
					sumOtherProductCount = 0;
					sumProductCountTotal = 0;
					sumReturnProductCount = 0;

					for (int i = 0; i < reportDataList.size(); i++) {
						sumTSRProductCount = sumTSRProductCount + reportDataList.get(i).TSRProductCount;
						sumOtherProductCount = sumOtherProductCount + reportDataList.get(i).OtherProductCount;
						sumProductCountTotal = sumProductCountTotal + reportDataList.get(i).ProductCountTotal;
						sumReturnProductCount = sumReturnProductCount + reportDataList.get(i).ReturnProductCount;
					}

					reportDataList.get(reportDataList.size() - 1).TSRProductCount = sumTSRProductCount;
					reportDataList.get(reportDataList.size() - 1).OtherProductCount = sumOtherProductCount;
					reportDataList.get(reportDataList.size() - 1).ProductCountTotal = sumProductCountTotal;
					reportDataList.get(reportDataList.size() - 1).ReturnProductCount = sumReturnProductCount;
				}
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
				public TextView txtSaleDate;
				public TextView txtTSRProductCount;
				public TextView txtOtherProductCount;
				public TextView txtProductCountTotal;
				public TextView txtReturnProductCount;
				public TextView txtReturnDate;

			}

			@Override
			protected int viewForSectionHeader(int section) {
				// TODO Auto-generated method stub

				return R.layout.list_report_summary_trade_product_receive_header;

			}

			@Override
			protected int viewForItem(int section, int row) {
				// TODO Auto-generated method stub
				return R.layout.list_report_summary_trade_product_receive_item;
			}

			@Override
			protected void onViewItem(View view, Object holder, int section, int row) {
				// TODO Auto-generated method stub

				try {
					ViewHolder vh = (ViewHolder) holder;
					vh.txtSaleDate.setText(BHUtilities.dateFormat(reportDataList.get(row).SaleDate).toString());
					vh.txtTSRProductCount.setText(String.valueOf(reportDataList.get(row).TSRProductCount));
					vh.txtOtherProductCount.setText(String.valueOf(reportDataList.get(row).OtherProductCount));
					vh.txtProductCountTotal.setText(String.valueOf(reportDataList.get(row).ProductCountTotal));
					vh.txtReturnProductCount.setText(String.valueOf(reportDataList.get(row).ReturnProductCount));
					vh.txtReturnDate.setText(BHUtilities.dateFormat(reportDataList.get(row).ReturnDate).toString());

					if (row == reportDataList.size() - 1) {
						vh.txtSaleDate.setText("ยอดรวม");
						vh.txtReturnDate.setText("");
					} else if (row == reportDataList.size() - 2) {
						vh.txtSaleDate.setText("");
						vh.txtTSRProductCount.setText("");
						vh.txtOtherProductCount.setText("");
						vh.txtProductCountTotal.setText("");
						vh.txtReturnProductCount.setText("");
						vh.txtReturnDate.setText("");
					}
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
