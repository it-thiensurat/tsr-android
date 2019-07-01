package th.co.thiensurat.fragments.credit.Spare;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.SpareDrawdownDetailController;
import th.co.thiensurat.data.info.SpareDrawdownDetailInfo;
import th.co.thiensurat.data.info.SpareDrawdownInfo;

public class SpareDrawdownDetailFragment extends BHFragment{

    public static class Data extends BHParcelable {
        public static SpareDrawdownInfo spareDrawdownInfo;
    }

    // For ChangeContract
    private Data data;

    @InjectView
    public TextView tvDrawdownDate, tvDrawdownNo;

    @InjectView
    public LinearLayout llDrawdownNo;

    @InjectView
    public ListView lvSummaryDetails;

    public List<SpareDrawdownDetailInfo> sddList;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_spare_drawdown_detail;
    }

    @Override
    protected int titleID() {
        return R.string.title_detail_data_spare;
    }

    @Override
    protected int[] processButtons() {
        if(data == null) {
            data = getData();
        }

        int[] ret = null;

        switch (Enum.valueOf(SpareDrawdownInfo.SpareDrawdownStatus.class, data.spareDrawdownInfo.Status)){
            case REQUEST:
                ret = new int[]{R.string.button_back};
                break;
            case APPROVED:
                ret = new int[]{R.string.button_back, R.string.button_print};
                break;
        }

        return ret;

    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        if(data == null) {
            data = getData();
        }

        tvDrawdownDate.setText(BHUtilities.dateFormat(data.spareDrawdownInfo.RequestDate, "dd/MM/yyyy : HH.mm"));

        if(data.spareDrawdownInfo.SpareDrawdownPaperID != null){
            tvDrawdownNo.setText(data.spareDrawdownInfo.SpareDrawdownPaperID);
            llDrawdownNo.setVisibility(View.VISIBLE);
        } else {
            llDrawdownNo.setVisibility(View.GONE);
        }

        sddList = new SpareDrawdownDetailController().getSpareDrawdownDetailBySpareDrawdownID(data.spareDrawdownInfo.SpareDrawdownID);

        if(sddList != null && sddList.size() > 0) {
            SpareDrawdownDetailAdapter adapter = new SpareDrawdownDetailAdapter(activity, R.layout.spare_drawdown_summary_detail_item, sddList);
            lvSummaryDetails.setAdapter(adapter);
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_print:

                break;
            default:
                break;
        }
    }


    public class SpareDrawdownDetailAdapter extends BHArrayAdapter<SpareDrawdownDetailInfo> {

        public SpareDrawdownDetailAdapter(Context context, int resource, List<SpareDrawdownDetailInfo> objects) {
            super(context, resource, objects);
        }


        private class ViewHolder {
            public TextView txtPartSpareName, txtPartSpareCode, txtQTY, txtUnit;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, SpareDrawdownDetailInfo info) {
            final ViewHolder vh = (ViewHolder) holder;

            vh.txtPartSpareName.setText(info.PartSpareName);
            vh.txtPartSpareCode.setText(info.PartSpareCode);

            switch (Enum.valueOf(SpareDrawdownInfo.SpareDrawdownStatus.class, data.spareDrawdownInfo.Status)){
                case REQUEST:
                    vh.txtQTY.setText(String.valueOf(info.RequestQTY));
                    break;
                case APPROVED:
                    vh.txtQTY.setText(String.valueOf(info.ApproveQTY));
                    break;
            }

            vh.txtUnit.setText(info.PartSpareUnit);
        }
    }
}
