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
import th.co.thiensurat.data.info.ReportDailySalesTranInfo;
import th.co.thiensurat.data.info.ReportDailyWriteOffNPLTranInfo;

/**
 * Created by Tanawut  Pongkan on 12/28/2014.
 */


/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/


public class ReportSummaryDailyWriteOffNPLFragment extends BHFragment {
    public static class Data extends BHParcelable {
        public String SaleLevel;
        public String EmpID;
    }

    private Data data;

    private List<ReportDailyWriteOffNPLTranInfo> writeOffNPLTranlist;
    private BHListViewAdapter adapter;

    @InjectView
    private ListView lvWriteOff;
    @InjectView
    private TextView txtShowTeam;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_daily_write_off_npl;
    }

    @Override
    protected int titleID() {
        return R.string.menu_report_writeoff_npl;
    }

    @Override
    public boolean enableLandscape() {
        return true;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back};
    }

    @Override
    public void onResume() {
        super.onResume();
        doBindDailyWriteOffNPL();
    }


    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();
        String Position= "";
        if (data.SaleLevel.equals("SaleLevel01")) {
            Position = "พนักงานขาย";
        }else {
            Position = "หน้าหนาทีม";
        }
        txtShowTeam.setText(String.format("ตำแหน่ง : %s  รหัส :  %s", Position, data.EmpID));
    }

    private void doBindDailyWriteOffNPL() {

        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                writeOffNPLTranlist = TSRController.getReportDailyWriteOffNPLTran(data.SaleLevel, data.EmpID);
            }

            @Override
            protected void after() {
                super.after();
                if (writeOffNPLTranlist != null) {
                    bindListDailyWriteOffNPLTran();
                } else {
                    String title = "คำเตือน";
                    String message = "ไม่พบข้อมูล ตัดสัญญาค้าง";
                    showDialog(title, message);
                }
            }
        }.start();

    }

    private void bindListDailyWriteOffNPLTran() {

        adapter = new BHListViewAdapter(activity) {
            class ViewHolder {
                public TextView txtProductSerialNumber;
                public TextView txtProductID;
                public TextView txtProductName;
                public TextView txtCONTNO;
                public TextView txtCustomerName;
                public TextView txtIDCard;
                public TextView txtAddressInstall;
                public TextView txtTelHome;
                public TextView txtTelMobile;
                public TextView txtTelOffice;
                public TextView txtInstallDate;
                public TextView txtFortnight;
                public TextView txtProblemLoss;
                public TextView txtProblemLossOther;
                public TextView txtLossDate;
                public TextView txtEmpNameApprove;
                public TextView txtApproveDate;
                public TextView txtApproveOther;
            }

            @Override
            protected int viewForSectionHeader(int section) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_writeoff_head;
            }

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_writeoff;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub

                ReportDailyWriteOffNPLTranInfo info =  writeOffNPLTranlist.get(row);
                ViewHolder vh = (ViewHolder) holder;

                vh. txtProductSerialNumber.setText(BHUtilities.trim(info.ProductSerialNumber));
                vh. txtProductID.setText(BHUtilities.trim(info.ProductModel));
                vh. txtProductName.setText(BHUtilities.trim(info.ProductName));
                vh. txtCONTNO.setText(BHUtilities.trim(info.CONTNO));
                vh. txtCustomerName.setText(BHUtilities.trim(info.CustomerName));
                vh. txtIDCard.setText(BHUtilities.trim(info.IDCard));
                vh. txtAddressInstall.setText(BHUtilities.trim(info.CusAddressInstall));
                vh. txtTelHome.setText(BHUtilities.trim(info.TelHome));
                vh. txtTelMobile.setText(BHUtilities.trim(info.TelMobile));
                vh. txtTelOffice.setText(BHUtilities.trim(info.TelOffice));
                vh. txtInstallDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.InstallDate)));
                vh. txtFortnight.setText(BHUtilities.trim(info.FortnightNumber));
                vh. txtProblemLoss.setText(BHUtilities.trim(info.RequestProblemID));
                vh. txtProblemLossOther.setText(BHUtilities.trim(info.RequestDetail));
                vh. txtLossDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.EffectiveDate)));
                vh. txtEmpNameApprove.setText(BHUtilities.trim(info.ApprovedBy));
                vh. txtApproveDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.ApprovedDate)));
                vh. txtApproveOther.setText(BHUtilities.trim(info.ApprovedDetail));


            }

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return writeOffNPLTranlist != null ? writeOffNPLTranlist.size() : 0;
            }
        };

        lvWriteOff.setAdapter(adapter);

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
