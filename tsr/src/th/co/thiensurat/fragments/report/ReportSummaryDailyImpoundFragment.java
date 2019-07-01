package th.co.thiensurat.fragments.report;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ReportDailyImpoundTranInfo;

/**
 * Created by Tanawut  Pongkan on 12/25/2014.
 */

/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/


public class ReportSummaryDailyImpoundFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String SaleLevel;
        public String EmpID;
    }

    private Data data;

    private List<ReportDailyImpoundTranInfo> impoundProductlist;
    private BHListViewAdapter adapter;

    @InjectView
    private ListView lvImpoundProduct;
    @InjectView
    private TextView txtShowTeam;


    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_daily_impound;
    }

    @Override
    protected int titleID() {
        return R.string.menu_report_impound_product;
    }


    @Override
    public boolean enableLandscape() {
        return true;
    }
    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back};
    }

    @Override
    public void onResume() {
        super.onResume();
        doBindDailyImpoundProductList();
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        data = getData();
        String Position= "";
        if (data.SaleLevel.equals("SaleLevel01")) {
            Position = "พนักงานขาย";
        }else {
            Position = "หัวหน้าทีม";
        }
        txtShowTeam.setText(String.format("ตำแหน่ง : %s  รหัส :  %s", Position, data.EmpID));

    }

    private void doBindDailyImpoundProductList() {
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                impoundProductlist = TSRController.getReportDailyImpoundTran(data.SaleLevel, data.EmpID);
            }

            @Override
            protected void after() {
                super.after();
                if (impoundProductlist != null) {
                    bindListDailyImpoundProduct();
                } else {
                    String title = "คำเตือน";
                    String message = "ไม่พบข้อมูล การถอดเครื่อง";
                    showDialog(title, message);
                }
            }
        }.start();
    }

    private void bindListDailyImpoundProduct() {
        adapter = new BHListViewAdapter(activity) {
            class ViewHolder {
                public TextView txtProductSerialNumber; //รหัสสินค้า
                public TextView txtProductName; //ชื่อสินค้า
                public TextView txtEffDate; // วันทีทำสัญญา
                public TextView txtAddressInstall; // ที่อยู่ติดตั้ง
                public TextView txtProblemImpoundProduct; //สาเหตุการถอด
                public TextView txtProblemImpoundProductOther; // สาเหตุอื่นๆ
                public TextView txtImpoundProductDate; // วันที่ถอด
                public TextView txtEmpNameImpoundProduct; // ชื่อผู้ถอด
                public TextView txtEmpNameApprove; // ผู้ที่ทำการอนุมัติ
                public TextView txtApproveDate; // วันที่อนุมัติ
                public TextView txtApproveOther; // หมายเหตุการอนุมัติ
            }

            @Override
            protected int viewForSectionHeader(int section) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_impound_product_head;
            }

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_impound_product;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub
                ReportDailyImpoundTranInfo info = impoundProductlist.get(row);

                ViewHolder vh = (ViewHolder) holder;
                vh.txtProductSerialNumber.setText(BHUtilities.trim(info.ProductSerialNumber));
                vh.txtProductName.setText(BHUtilities.trim(info.ProductName));
                vh.txtEffDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.EFFDATE)));
                vh.txtAddressInstall.setText(BHUtilities.trim(info.CusAddressInstall));
                vh.txtProblemImpoundProduct.setText(BHUtilities.trim(info.RequestProblemID));
                vh.txtProblemImpoundProductOther.setText(BHUtilities.trim(info.RequestDetail));
                vh.txtImpoundProductDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.InstallDate)));
                vh.txtEmpNameImpoundProduct.setText(BHUtilities.trim(info.EffectiveBy));
                vh.txtEmpNameApprove.setText(BHUtilities.trim(info.ApprovedBy));
                vh.txtApproveDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.ApprovedDate)));
                vh.txtApproveOther.setText(BHUtilities.trim(info.ApprovedDetail));

            }

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return impoundProductlist != null ? impoundProductlist.size() : 0;
            }
        };

        lvImpoundProduct.setAdapter(adapter);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        super.onProcessButtonClicked(buttonID);
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }


}
