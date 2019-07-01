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
import th.co.thiensurat.data.info.ReportDailySendDocumentTranInfo;

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


public class ReportSummaryDailySendDocumentFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String SaleLevel;
        public String EmpID;
    }

    private Data data;

    private List<ReportDailySendDocumentTranInfo> sendDocumentTranList;
    private BHListViewAdapter adapter;


    @InjectView
    ListView lvSendDocument;
    @InjectView
    TextView txtShowTeam;


    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_daily_send_document;
    }

    @Override
    protected int titleID() {
        return R.string.menu_report_daily_summary_send_document;
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
        doBindDailySendDocument();
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

    private void doBindDailySendDocument() {

        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                sendDocumentTranList = TSRController.getReportDailySendDocumentTran(data.SaleLevel, data.EmpID);
            }

            @Override
            protected void after() {
                if (sendDocumentTranList != null) {
                    bindListDailySendDocumentTran();
                } else {
                    String title = "คำเตือน";
                    String message = "ไม่พบข้อมูล การเปลี่ยนเครื่อง";
                    showDialog(title, message);
                }
            }
        }.start();
    }

    private void bindListDailySendDocumentTran() {


        adapter = new BHListViewAdapter(activity) {
            class ViewHolder {
                public TextView txtSendDocumentDate; // วันที่ส่งเอกสาร
                public TextView txtDocumentType; //ชนิดเอกสาร
                public TextView txtDocumentName; // ชื่อเอกสาร
                public TextView txtDocumentNumber; // เลขที่เอกสาร
                public TextView txtDocumentStatus; //สถานะเอกสาร

            }

            @Override
            protected int viewForSectionHeader(int section) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_send_document_head;
            }

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_send_document;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub
                ReportDailySendDocumentTranInfo info = sendDocumentTranList.get(row);
                ViewHolder vh = (ViewHolder) holder;

                vh.txtSendDocumentDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.DatePrint)));
                vh.txtDocumentType.setText(BHUtilities.trim(String.valueOf(String.valueOf(info.DocumentType))));
                vh.txtDocumentName.setText(BHUtilities.trim(info.DocumentName));
                vh.txtDocumentNumber.setText(BHUtilities.trim(info.DocumentNumber));
                vh.txtDocumentStatus.setText(BHUtilities.trim(BHUtilities.dateFormat(info.ReceiveStatus)));
            }

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return sendDocumentTranList != null ? sendDocumentTranList.size() : 0;
            }
        };

        lvSendDocument.setAdapter(adapter);

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
