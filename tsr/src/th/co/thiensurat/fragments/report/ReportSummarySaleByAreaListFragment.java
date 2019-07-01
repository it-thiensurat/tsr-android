package th.co.thiensurat.fragments.report;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;

import java.util.Collections;
import java.util.List;

import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.ReportSaleByAreaController;
import th.co.thiensurat.data.info.ReportSaleByAreaListInfo;

/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/

public class ReportSummarySaleByAreaListFragment extends BHPagerFragment {

    @InjectView
    private Spinner spnArea;

    @InjectView
    private PinnedSectionListView lvArea;

    private List<ReportSaleByAreaListInfo> data;
    private int areaType;
    private String fortnightID;
    private boolean isActive = false;
    private BHListViewAdapter adapter;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_sale_by_area_list;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        bindControls();
    }

    public void refreshData(boolean isActive, String fortnightID) {
        boolean dataChange = !fortnightID.equals(this.fortnightID);
        this.isActive = isActive;
        if (isActive && dataChange) {
            this.fortnightID = fortnightID;
            refreshData();
        }
    }

    private void refreshData() {
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                data = new ReportSaleByAreaController().getList(BHPreference.employeeID(), fortnightID, areaType);
                if (data != null && data.size() > 0) {
                    ReportSaleByAreaListInfo summary = new ReportSaleByAreaListInfo();
                    summary.AreaName = "รวม";
                    for (ReportSaleByAreaListInfo info : data) {
                        summary.TotalSummary += info.TotalSummary;
                        summary.CashSummary += info.CashSummary;
                        summary.CreditSummary += info.CreditSummary;
                    }

                    data.add(summary);
                }
            }

            @Override
            protected void after() {
                adapter.notifyDataSetChanged();
            }
        }.start();
    }

    private void bindControls() {
        final String TAMBOL_TEXT = "ตำบล";
        final String AMPHUR_TEXT = "อำเภอ";
        final String PROVINCE_TEXT = "จังหวัด";
        String[] areaTypes = { TAMBOL_TEXT, AMPHUR_TEXT, PROVINCE_TEXT };
        BHSpinnerAdapter<String> areaAdapter = new BHSpinnerAdapter<String>(activity, areaTypes);
        spnArea.setAdapter(areaAdapter);

        spnArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = (String)parent.getItemAtPosition(position);
                switch (type) {
                    case TAMBOL_TEXT:
                        areaType = 3;
                        break;

                    case AMPHUR_TEXT:
                        areaType = 2;
                        break;

                    case PROVINCE_TEXT:
                        areaType = 1;
                        break;

                    default:
                        areaType = 0;
                        break;
                }

                if (isActive) {
                    refreshData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new BHListViewAdapter(activity) {
            class ViewHolder {
                public TextView tvAreaName;
                public TextView tvTotalSummary;
                public TextView tvCashSummary;
                public TextView tvCreditSummary;
            }

            @Override
            protected int viewForSectionHeader(int section) {
                // TODO Auto-generated method stub
                return R.layout.list_report_sale_by_area_list_header;
            }

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                return R.layout.list_report_sale_by_area_list_item;
            }

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return data != null ? data.size() : 0;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub
                try {
                    ReportSaleByAreaListInfo info = data.get(row);
                    ViewHolder vh = (ViewHolder) holder;
                    vh.tvAreaName.setText(BHUtilities.trim(info.AreaName));
                    vh.tvTotalSummary.setText(BHUtilities.numericFormat(info.TotalSummary));

                    vh.tvCashSummary.setText(BHUtilities.numericFormat(info.CashSummary));
                    vh.tvCreditSummary.setText(BHUtilities.numericFormat(info.CreditSummary));
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        };

        lvArea.setAdapter(adapter);
    }
}
