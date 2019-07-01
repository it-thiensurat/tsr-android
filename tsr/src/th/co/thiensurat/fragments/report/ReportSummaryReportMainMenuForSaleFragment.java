package th.co.thiensurat.fragments.report;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.info.MenuInfo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


/********************************************************
 *
 *
 * [Checked@10/02/2016] ไม่ได้ใช้งาน File นี้แล้ว เนื่องจากแยก Menu report ไปเป็น Sub-Menu แล้ว
 * ตาม Issue No. [BHPROJ-0016-855] [Android-Menu] แก้ไข Menu ข้อมูลหลัก (Report) ให้เป็น Sub-Report เหมือนเมนู ระบบแก้ปัญหา (เมนูอื่น ๆ) แทนที่จะเป็นหน้า Fragment ย่อยแล้วให้กด List view อย่างในปัจจุบัน
 *
 *
 *****************************************************/


public class ReportSummaryReportMainMenuForSaleFragment extends BHFragment {

    @InjectView
    private ListView lvMenu;
    // private EfficientAdapter mEfficientAdapter;
    private static TypedArray first_payment_menu_icons;
    private static String[] first_payment_menu_titles;

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        // return 0;
        return R.layout.fragment_report_main_menu;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return null;
        // return new int[] { R.string.button_back };
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        setupMenu();


    }


    // private void GetEmpDetail() {
    // // TODO Auto-generated method stub
    // (new BackgroundProcess(activity) {
    // @Override
    // protected void calling() {
    // // TODO Auto-generated method stub
    // EmpDetail = getEmpDetailByempCode(BHPreference.employeeID());
    // }
    //
    // @Override
    // protected void after() {
    // // TODO Auto-generated method stub
    // if (EmpDetail != null) {
    // FirstPaymentCustomerListFragment.Data data = new
    // FirstPaymentCustomerListFragment.Data();
    // data.EmpDetail = EmpDetail;
    // FirstPaymentCustomerListFragment fm =
    // BHFragment.newInstance(FirstPaymentCustomerListFragment.class, data);
    // showNextView(fm);
    // } else {
    //
    // }
    //
    // }
    // }).start();
    // }

    private void setupMenu() {
        // first_payment_menu_titles = getResources().getStringArray(
        // R.array.first_payment_menu_titles);
        // first_payment_menu_icons = getResources().obtainTypedArray(
        // R.array.first_payment_menu_icon);
        try {
            // lvMenu.setAdapter(new EfficientAdapter(getActivity()));
            // setAdapter();
            bindMenu();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }

    private void selectedMenu(int position, int titleResourceID) {
        BHFragment bhFragment=null;
        switch (titleResourceID) {

            case R.string.menu_report_daily_summary:            // รายงานสรุปยอดขาย - ReportDashboard (SALE)
                //showNextView(BHFragment.newInstance(ReportDailySummaryFragment.class));
//                ReportDailySummaryFragment.Data selectedData = new ReportDailySummaryFragment.Data();
//                selectedData.currentSaleLevel = 6;
//                selectedData.fillterString = "";
//                ReportDailySummaryFragment fm = BHFragment.newInstance(ReportDailySummaryFragment.class, selectedData);
//                showNextView(fm);

                bhFragment= BHFragment.newInstance(ReportDashboardSummaryMainFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_emp_team:             // รายงานโครงสร้างพนักงาน - ReportSaleAndDriver (SALE + CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryEmployeeTeamFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_product:          // รายงานยอดสินค้าคงเหลือ - ReportInventory (SALE)

                bhFragment= BHFragment.newInstance(ReportSummaryProductFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_install_payment:            // รายงานยอดติดตั้ง/ขายสินค้า - ReportInstallAndPayment (SALE)
                // GetEmpDetail();
                bhFragment= BHFragment.newInstance(ReportSummaryInstallAndPaymentFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_change_product:   // รายงานเปลี่ยนเครื่อง - ReportChangeProduct (SALE + CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryChangeProductFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_impound_product:                  // รายงานถอดเครื่อง - ReportImpoundProduct (SALE + CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryImpoundProductFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_change_contract:                  // รายงานเปลี่ยนสัญญา - ReportChangeContract (SALE + CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryChangeContractFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_payment_appointment:              // รายงานนัดเก็บเงินงวดแรก - ReportPaymentAppointment (SALE)
                bhFragment= BHFragment.newInstance(ReportSummaryPaymentAppointmentFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_writeoff_npl:             // รายงานตัดสัญญาค้าง - ReportWriteOffNPL (SALE)
                bhFragment= BHFragment.newInstance(ReportSummaryWriteOffNPLMainFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_approved:          // รายงานขออนุมัติ - ReportApproved  (SALE + CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryApprovedFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_complain:             // รายงานสรุปการแจ้งปัญหา - ReportComplain (SALE + CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryComplainFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_trade_product:                    // รายงานเครื่องเทิร์น - ReportTradeInProduct (SALE)
                bhFragment= BHFragment.newInstance(ReportSummaryTradeProductMainFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
				break;

            case R.string.menu_report_payment_sendmoney:    // รายงานเก็บเงินงวดแรก/ถัดไป - ReportPaymentAndSendMoney (SALE)
                // GetEmpDetail();
                bhFragment= BHFragment.newInstance(ReportSummaryPaymentAndSendMoneyFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_zone:             // รายงานพื้นที่ขาย - ReportSaleArea (SALE)
                bhFragment= BHFragment.newInstance(ReportSummarySaleByAreaFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_re_print: // รายงานการพิมพ์เอกสารที่มีการ Re-Print ReportRePrintDocument (SALE + CREDIT)
                bhFragment= BHFragment.newInstance(ReportRePrintDocumentFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;
            /*
            case R.string.menu_report_credit_saleaudit:     // รายงานการตรวจสอบการ์ดงวดแรก - ReportSaleAudit (CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryCreditSaleAuditFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_credit_next_payment:  // รายงานการเก็บเงินค่างวด - ReportNextPayment (CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryCreditNextPaymentFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_credit_sendmoney:    // รายงานสรุปการส่งเงิน - ReportSendMoney (CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryCreditSendMoneyFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;

            case R.string.menu_report_credit_contract_close_account: // รายงานตัดสด - ReportContractCloseAccount (CREDIT)
                bhFragment= BHFragment.newInstance(ReportSummaryCreditContractCloseAccountFragment.class);
                bhFragment.bOrentation=true;
                showNextView(bhFragment);
                break;
            */

            default:
                break;
        }
    }

    private void bindMenu() {
        final MenuInfo[] menus = MenuInfo.from(R.array.report_sale_menu);
        BHListViewAdapter adapter = new BHListViewAdapter(activity) {
            class ViewHolder {
                public TextView txtTitle;
            }

            @Override
            protected int viewForItem(int section, int row) {
                return R.layout.list_report_menu;
            }

            @Override
            protected int getItemCount(int section) {
                return menus.length;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                MenuInfo info = menus[row];
                ViewHolder vh = (ViewHolder) holder;
                vh.txtTitle.setText(info.titleID);

            }
        };
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                MenuInfo info = menus[position];
//                selectedMenu = position;
//                view.setSelected(true);
                selectedMenu(position, info.titleID);
            }
        });
    }


}
