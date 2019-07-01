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

/**
 * Created by Tanawut  Pongkan on 12/26/2014.
 */

/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/


public class ReportSummaryDailyChangeProductFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String SaleLevel;
        public String EmpID;
    }

    private Data data;

    private List<ReportDailyChangeProTranInfo> changeProTranList;
    private BHListViewAdapter adapter;

    @InjectView
    ListView lvChangeProduct;
    @InjectView
    TextView txtShowTeam;


    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_daily_change_product;
    }

    @Override
    protected int titleID() {
        return R.string.menu_report_change_product;
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
        doBindDailyChangeProduct();
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

    private void doBindDailyChangeProduct() {
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                changeProTranList = TSRController.getReportDailyChangeProTran(data.SaleLevel,data.EmpID);
            }

            @Override
            protected void after() {
                super.after();
                if (changeProTranList != null) {
                    bindListDailyChangeProductTran();
                } else {
                    String title = "คำเตือน";
                    String message = "ไม่พบข้อมูล การเปลี่ยนเครื่อง";
                    showDialog(title, message);
                }
            }
        }.start();
    }

    private void bindListDailyChangeProductTran() {

        adapter = new BHListViewAdapter(activity) {
            class ViewHolder {
                public TextView txtProductSerialNumber; //รหัสสินค้า
                public TextView txtProductID; // รหัสสินค้า
                public TextView txtProductName; //ชื่อสินค้า
                public TextView txtCONTNO; // เลขที่สัญญา
                public TextView txtCustomerName; // ชื่อลูกค้า
                public TextView txtAddressInstall; // ที่อยู่ติดตั้ง
                public TextView txtTelHome; // เบอร์บ้าน
                public TextView txtTelMobile; // เบอร์ที่ทำงาน
                public TextView txtTelOffice; // เบอร์ที่ทำงาน
                public TextView txtInstallDate; //วันที่ติดตั้ง
                public TextView txtChangeProductOther; // หมายเหตุการเปลี่ยนแปลง
                public TextView txtOther; // เพิ่มเติม
                public TextView txtNewProductSerialNumber; // รหัสสินค้าใหม่
                public TextView txtChangeDate; // วันที่ทำการเปลี่ยน
                public TextView txtEmpNameChange; // ผู้ที่ทำการเปลี่ยน
            }

            @Override
            protected int viewForSectionHeader(int section) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_change_product_head;
            }

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_change_product;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub
                ReportDailyChangeProTranInfo info = changeProTranList.get(row);

                ViewHolder vh = (ViewHolder) holder;
                vh. txtProductSerialNumber.setText(BHUtilities.trim(info.ProductSerialNumber));
                vh. txtProductID.setText(BHUtilities.trim(info.ProductModel));
                vh. txtProductName.setText(BHUtilities.trim(info.ProductName));
                vh. txtCONTNO.setText(BHUtilities.trim(info.CONTNO));
                vh. txtCustomerName.setText(BHUtilities.trim(info.CustomerName));
                vh. txtAddressInstall.setText(BHUtilities.trim(info.CusAddressInstall));
                vh. txtTelHome.setText(BHUtilities.trim(info.TelHome));
                vh. txtTelMobile.setText(BHUtilities.trim(info.TelMobile));
                vh. txtTelOffice.setText(BHUtilities.trim(info.TelOffice));
                vh. txtInstallDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.InstallDate)));
                vh. txtChangeProductOther.setText(BHUtilities.trim(info.ResultProblemID));
                vh. txtOther.setText(BHUtilities.trim(info.ResultDetail));
                vh. txtNewProductSerialNumber.setText(BHUtilities.trim(info.NewProductSerialNumber));
                vh. txtChangeDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.EffectiveDate)));
                vh. txtEmpNameChange.setText(BHUtilities.trim(info.EffectiveBy));

            }

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return changeProTranList != null ? changeProTranList.size() : 0;
            }
        };

        lvChangeProduct.setAdapter(adapter);
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
