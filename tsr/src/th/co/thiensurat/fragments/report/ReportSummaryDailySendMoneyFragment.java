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
import th.co.thiensurat.data.info.ReportDailyChangeProTranInfo;
import th.co.thiensurat.data.info.ReportDailySendMoneyTranInfo;

/**
 * Created by Tanawut on 4/1/2558.
 */


/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/


public class ReportSummaryDailySendMoneyFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String SaleLevel;
        public String EmpID;
    }

    private Data data;

    private List<ReportDailySendMoneyTranInfo> sendMoneyTranList;
    private BHListViewAdapter adapter;


    @InjectView
    ListView lvSendMoney;
    @InjectView
    TextView txtShowTeam;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_daily_send_money;
    }

    @Override
    protected int titleID() {
        return R.string.menu_report_daily_summary_send_money;
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
        doBindDailySendMoney();
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();
        String Position;
        if (data.SaleLevel.equals("SaleLevel01")) {
            Position = "พนักงานขาย";
        } else {
            Position = "หัวหน้าทีม";
        }
        txtShowTeam.setText(String.format("ตำแหน่ง : %s  รหัส :  %s", Position, data.EmpID));
    }

    private void doBindDailySendMoney() {
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                sendMoneyTranList = TSRController.getReportDailySendMoneyTran(data.SaleLevel, data.EmpID);
            }

            @Override
            protected void after() {
                if (sendMoneyTranList != null) {
                    bindListDailySendMoneyTran();
                } else {
                    String title = "คำเตือน";
                    String message = "ไม่พบข้อมูล การเปลี่ยนเครื่อง";
                    showDialog(title, message);
                }
            }
        }.start();
    }

    private void bindListDailySendMoneyTran() {

        adapter = new BHListViewAdapter(activity) {
            class ViewHolder {
                public TextView txtDateSendMoney; //วันที่ส่งเงิน
                public TextView txtSendAmount; //จำนวนเงินที่นำส่ง
                public TextView txtChannelName; // ช่องทางการนำส่งเงิน
                public TextView txtChannelItemName; //สถานที่นำส่งเงิน
                public TextView txtPayeeName; // ชื่อผู้รับเงิน
                public TextView txtReference1; // เลขที่อ้างอิง1
                public TextView txtReference2; // เลยที่อ้างอิง2
                public TextView txtReferenceDate; // วันที่/เวลา
            }

            @Override
            protected int viewForSectionHeader(int section) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_send_money_head;
            }

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_send_money;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub
                ReportDailySendMoneyTranInfo info = sendMoneyTranList.get(row);
                ViewHolder vh = (ViewHolder) holder;

                vh.txtDateSendMoney.setText(BHUtilities.trim(BHUtilities.dateFormat(info.SendDate)));
                vh.txtSendAmount.setText(BHUtilities.trim(String.valueOf(info.SendAmount)));
                vh.txtChannelName.setText(BHUtilities.trim(info.SendTransaction));
                vh.txtChannelItemName.setText(BHUtilities.trim(info.PlaceTransaction));
                vh.txtPayeeName.setText(BHUtilities.trim(info.ReceivedByName));
                vh.txtReference1.setText(BHUtilities.trim(info.Reference1));
                vh.txtReference2.setText(BHUtilities.trim(info.Reference2));
                vh.txtReferenceDate.setText(BHUtilities.trim(String.format("วันที่ %s เวลา %s น.", BHUtilities.dateFormat(info.ReceivedDateTime, "dd/MM/yyyy"), BHUtilities.dateFormat(info.ReceivedDateTime, "HH:mm:ss"))));

            }

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return sendMoneyTranList != null ? sendMoneyTranList.size() : 0;

            }
        };

        lvSendMoney.setAdapter(adapter);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {

        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }

    }

}
