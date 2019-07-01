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
import th.co.thiensurat.data.info.ReportSummaryTradeProductNotReceiveInfo;
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

public class ReportSummaryTradeProductNotReceiveFragment extends BHFragment {

	// @InjectView
	// private Spinner spnFortnight;
	@InjectView
	private ListView lvSummary;
	private BHListViewAdapter adapter;

	private List<ReportSummaryTradeProductNotReceiveInfo> reportDataList;

	private int sumRemainTSRProductCount;
	private int sumRemainOtherProductCount;
	private int sumRemainProductCountTotal;

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
				// TSRController.getReportSummaryTradeProductNotReceive("TEMP01",
				// "PAK-30", "");
				// reportDataList =
				// TSRController.getReportSummaryTradeProductNotReceive(ReportSummaryTradeProductMainFragment.FortnightID,
				// "PAK-30", ReportSummaryTradeProductMainFragment.SaleCode);

				reportDataList = TSRController.getReportSummaryTradeProductNotReceive(ReportSummaryTradeProductMainFragment.FortnightID,
						BHPreference.teamCode(), ReportSummaryTradeProductMainFragment.SaleCode);

				if (reportDataList != null) {
					reportDataList.add(reportDataList.size(), new ReportSummaryTradeProductNotReceiveInfo());
					reportDataList.add(reportDataList.size(), new ReportSummaryTradeProductNotReceiveInfo());

					sumRemainTSRProductCount = 0;
					sumRemainOtherProductCount = 0;
					sumRemainProductCountTotal = 0;

					for (int i = 0; i < reportDataList.size(); i++) {
						sumRemainTSRProductCount += reportDataList.get(i).RemainTSRProductCount;
						sumRemainOtherProductCount += reportDataList.get(i).RemainOtherProductCount;
						sumRemainProductCountTotal += reportDataList.get(i).RemainProductCountTotal;
					}

					reportDataList.get(reportDataList.size() - 1).RemainTSRProductCount = sumRemainTSRProductCount;
					reportDataList.get(reportDataList.size() - 1).RemainOtherProductCount = sumRemainOtherProductCount;
					reportDataList.get(reportDataList.size() - 1).RemainProductCountTotal = sumRemainProductCountTotal;

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
				// public TextView txtReturnProductCount;
				public TextView txtReturnDate;

			}

			@Override
			protected int viewForSectionHeader(int section) {
				// TODO Auto-generated method stub

				return R.layout.list_report_summary_trade_product_not_receive_header;

			}

			@Override
			protected int viewForItem(int section, int row) {
				// TODO Auto-generated method stub
				return R.layout.list_report_summary_trade_product_not_receive_item;
			}

			@Override
			protected void onViewItem(View view, Object holder, int section, int row) {
				// TODO Auto-generated method stub
				try {
					ViewHolder vh = (ViewHolder) holder;
					vh.txtSaleDate.setText(BHUtilities.dateFormat(reportDataList.get(row).SaleDate).toString());
					vh.txtTSRProductCount.setText(String.valueOf(reportDataList.get(row).RemainTSRProductCount));
					vh.txtOtherProductCount.setText(String.valueOf(reportDataList.get(row).RemainOtherProductCount));
					vh.txtProductCountTotal.setText(String.valueOf(reportDataList.get(row).RemainProductCountTotal));
					// vh.txtReturnProductCount.setText(reportDataList.get(row).ReturnProductCount);
					vh.txtReturnDate.setText(BHUtilities.dateFormat(reportDataList.get(row).ReturnDate).toString());

					if (row == reportDataList.size() - 1) {
						vh.txtSaleDate.setText("ยอดค้าง");
						vh.txtReturnDate.setText("");
					} else if (row == reportDataList.size() - 2) {
						vh.txtSaleDate.setText("");
						vh.txtTSRProductCount.setText("");
						vh.txtOtherProductCount.setText("");
						vh.txtProductCountTotal.setText("");
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
