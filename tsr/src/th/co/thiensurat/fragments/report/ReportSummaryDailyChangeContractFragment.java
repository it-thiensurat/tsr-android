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
import th.co.thiensurat.data.info.ReportDailyChangeContTranInfo;

/**
 * Created by Tanawut  Pongkan on 12/27/2014.
 */


/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/


public class ReportSummaryDailyChangeContractFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String SaleLevel;
        public String EmpID;
    }

    private Data data;

    private List<ReportDailyChangeContTranInfo> changeContTranList;
    private BHListViewAdapter adapter;

    @InjectView
    ListView lvChangeContract;
    @InjectView
    TextView txtShowTeam;


    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_daily_change_contract;
    }

    @Override
    protected int titleID() {
        return R.string.menu_report_change_contract;
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
        doBindDailyChangeContract();
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

    private void doBindDailyChangeContract() {
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                changeContTranList = TSRController.getReportDailyChangeContTran(data.SaleLevel, data.EmpID);
            }

            @Override
            protected void after() {
                super.after();

                if (changeContTranList != null) {
                    bindListDailyChangeConTran();
                } else {
                    String title = "คำเตือน";
                    String message = "ไม่พบข้อมูล การเปลี่ยนสัญญา";
                    showDialog(title, message);
                }
            }
        }.start();

    }

    private void bindListDailyChangeConTran() {

        adapter = new BHListViewAdapter(activity) {
            class ViewHolder {
                public TextView txtEffDate;
                public TextView txtCONTNO;
                public TextView txtCustomerName;
                public TextView txtIDCard;
                public TextView txtAddressIDCard;
                public TextView txtAddressInstall;
                public  TextView txtProductSerialNumber;
                public TextView txtProductID;
                public TextView txtProductName;
                public TextView txtTotalPrice;
                public TextView txtMODE;
                public TextView txtRequestProblem;
                public TextView txtChangeContractDate;
                public TextView txtEmpNameApprove;
                public TextView txtApproveDate;
                public TextView txtApproveDetail;
                public TextView txtEmpNameChangeContract;
            }

            @Override
            protected int viewForSectionHeader(int section) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_change_contract_head;
            }

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_change_contract;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub
                ReportDailyChangeContTranInfo info = changeContTranList.get(row);

                ViewHolder vh = (ViewHolder) holder;
                vh. txtEffDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.EFFDATE)));
                vh. txtCONTNO.setText(BHUtilities.trim(info.CONTNO));
                vh. txtCustomerName.setText(BHUtilities.trim(info.CustomerName));
                vh. txtIDCard.setText(BHUtilities.trim(info.IDCard));
                vh. txtAddressIDCard.setText(BHUtilities.trim(info.CusAddressID));
                vh. txtAddressInstall.setText(BHUtilities.trim(info.CusAddressInstall));
                vh.txtProductSerialNumber.setText(BHUtilities.trim(info.ProductSerialNumber));
                vh. txtProductID.setText(BHUtilities.trim(info.ProductModel));
                vh. txtProductName.setText(BHUtilities.trim(info.ProductName));
                vh. txtTotalPrice.setText(BHUtilities.trim(String.valueOf(info.TotalPrice)));
                vh. txtMODE.setText(BHUtilities.trim(String.valueOf(info.Mode)));
                vh. txtRequestProblem.setText(BHUtilities.trim(info.RequestProblemID));
                vh. txtChangeContractDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.EffectiveDate)));
                vh. txtEmpNameApprove.setText(BHUtilities.trim(info.ApprovedBy));
                vh. txtApproveDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.ApprovedDate)));
                vh. txtApproveDetail.setText(BHUtilities.trim(info.ApprovedBy));
                vh. txtEmpNameChangeContract.setText(BHUtilities.trim(""));

            }

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return changeContTranList != null ? changeContTranList.size() : 0;
            }
        };

        lvChangeContract.setAdapter(adapter);
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
