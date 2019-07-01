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
import th.co.thiensurat.data.info.ReportDailySalesTranInfo;

/**
 * Created by Tanawut  Pongkan on 12/23/2014.
 */


/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/


public class ReportSummaryDailySaleFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String SaleLevel;
        public String EmpID;
    }

    private Data data;

    private List<ReportDailySalesTranInfo> reportDailySalesTranlist;
    private BHListViewAdapter adapter;

    @InjectView
    private ListView lvContract;
    @InjectView
    private TextView txtShowTeam;


    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_summary_daily_sale;
    }

    @Override
    protected int titleID() {
        return R.string.menu_report_sale;
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
        doBindDailySaleTranList();

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

    private void doBindDailySaleTranList() {
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                reportDailySalesTranlist = getReportDailySalesTran(data.SaleLevel, data.EmpID);
            }

            @Override
            protected void after() {
                super.after();
                if (reportDailySalesTranlist != null) {
                    bindListDailySaleTran();
                } else {
                    String title = "คำเตือน";
                    String message = "ไม่พบข้อมูล ยอดขาย";
                    showDialog(title, message);
                }
            }
        }.start();
    }


    private void bindListDailySaleTran() {

        adapter = new BHListViewAdapter(activity) {
            class ViewHolder {
                public TextView txtEffDate;  //วันที่ทำสัญญา
                public TextView txtProductSerialNumber; //รหัสสินค้า
                public TextView txtSale; // พนักงานขาย
                public TextView txtProductID; // รหัสสินค้า
                public TextView txtProductName; //ชื่อสินค้า
                public TextView txtIntroduceEmpName; // พนักงานแนะนำ
                public TextView txtCustomerID; // เลขที่ข้อมูลลูกค้า
                public TextView txtCustomerType; // ประเภทลูกค้า
                public TextView txtIDCard; // บัตรประชาชน
                public TextView txtPrefix; // คำนำหน้า
                public TextView txtCustomerName; // ชื่อลูกค้า
                public TextView txtAddressIDCard; // ที่อยู่ตามบัตร
                public TextView txtAddressInstall; // ที่อยู่ติดตั้ง
                public TextView txtAddressPayment; // ที่อยู่เก็บเงิน
                public TextView txtSALES; // ราคาขาย
                public TextView txtMODE; // สด/ผ่อน
                public TextView txtPackageModelDesc; // จำนวนงวด
                public TextView txtTradeInDiscount; // เครื่องเทิร์น/ส่วนลด
                public TextView txtTotalPrice; // ราคาสุทธิ
                public TextView txtInstallEmpName; // พนักงานติดตั้ง
                public TextView txtFirstPaymentAmount; // จำนวนเงินที่ชำระจริง
                public TextView txtPaymentDate; // วันที่รับชำระ
                public TextView txtPaymentType; // ประเภทการจ่ายเงิน
                public TextView txtBankSeries; // เลขที่บัญชี
                public TextView txtChequeNumber; // หมายเลขเช๊ค
                public TextView txtCreditNumber; // หมายเลขบัตรเครดิต
                public TextView txtManualReceipt; // เลขที่ใบเสร็จมือ
                public TextView txtPaymentAppointmentDate; // วันที่นัดชำระ
            }

            @Override
            protected int viewForSectionHeader(int section) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_sale_head;
            }

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                return R.layout.list_report_summary_daily_sale;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub

                ReportDailySalesTranInfo info = reportDailySalesTranlist.get(row);
                ViewHolder vh = (ViewHolder) holder;
                vh.txtEffDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.EFFDATE)));
                vh.txtProductSerialNumber.setText(BHUtilities.trim(info.ProductSerialNumber));
                vh.txtSale.setText(BHUtilities.trim(info.SaleLevel01));
                vh.txtProductID.setText(BHUtilities.trim(info.ProductID));
                vh.txtProductName.setText(BHUtilities.trim(info.ProductName));
                vh.txtIntroduceEmpName.setText(BHUtilities.trim(info.PreSaleLevel01));
                vh.txtCustomerID.setText(BHUtilities.trim(info.CustomerID));
                vh.txtCustomerType.setText(BHUtilities.trim(info.CustomerType));
                vh.txtIDCard.setText(BHUtilities.trim(info.IDCard));
                vh.txtPrefix.setText(BHUtilities.trim(info.PrefixName));
                vh.txtCustomerName.setText(BHUtilities.trim(info.CustomerName));
                vh.txtAddressIDCard.setText(BHUtilities.trim(info.AddressIDCard));
                vh.txtAddressInstall.setText(BHUtilities.trim(info.AddressInstall));
                vh.txtAddressPayment.setText(BHUtilities.trim(info.AddressPayment));
                vh.txtSALES.setText(BHUtilities.trim(String.valueOf(info.SALES)));
                vh.txtMODE.setText(BHUtilities.trim(String.valueOf(info.MODE)));
                vh.txtPackageModelDesc.setText(BHUtilities.trim(info.PackageModelDesc));
                vh.txtTradeInDiscount.setText(BHUtilities.trim(String.valueOf(info.TradeInDiscount)));
                vh.txtTotalPrice.setText(BHUtilities.trim(String.valueOf(info.TotalPrice)));
                vh.txtInstallEmpName.setText(BHUtilities.trim(info.InstallLevel01));
                vh.txtFirstPaymentAmount.setText(BHUtilities.trim(Float.toString(info.FirstPaymentAmount)));
                vh.txtPaymentDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.PayDate)));
                vh.txtPaymentType.setText(BHUtilities.trim(info.PaymentType));
                vh.txtBankSeries.setText(BHUtilities.trim(info.BookNo));
                vh.txtChequeNumber.setText(BHUtilities.trim(info.ChequeNumber));
                vh.txtCreditNumber.setText(BHUtilities.trim(info.CreditCardNumber));
                vh.txtManualReceipt.setText(BHUtilities.trim(info.ReceiptNo));
                vh.txtPaymentAppointmentDate.setText(BHUtilities.trim(BHUtilities.dateFormat(info.AppointmentDate)));

            }

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return reportDailySalesTranlist != null ? reportDailySalesTranlist.size() : 0;
            }
        };

        lvContract.setAdapter(adapter);
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
