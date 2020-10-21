package th.co.thiensurat.fragments.credit.pc;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.info.AssignInfo;


public class CreditMainFragment_pc extends BHFragment {
    @InjectView
    private TextView txtCountCredit;
    @InjectView
    ListView listView;
    private PaymentAppointmentDateAdapter paymentAppointmentDateAdapter;
    private List<AssignInfo> paymentAppointmentDateList;

    public static final String CREDIT_MAIN_FRAGMENT = "CREDIT_MAIN_FRAGMENT";

    @Override
    protected int fragmentID() {
        return  R.layout.fragment_credit_main_fragment;
    }

    @Override
    public String fragmentTag() {
        return CREDIT_MAIN_FRAGMENT;
    }

    @Override
    protected int[] processButtons() {
        return new int[0];
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        paymentAppointmentDateList = new ArrayList<AssignInfo>();
        paymentAppointmentDateAdapter = new PaymentAppointmentDateAdapter(activity, R.layout.list_credit_import_credit_select_date, paymentAppointmentDateList);
        listView.setAdapter(paymentAppointmentDateAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                CreditListFragment_pc.Data input = new CreditListFragment_pc.Data();
                input.selectedDate = paymentAppointmentDateList.get(position).PaymentAppointmentDate;
                CreditListFragment_pc fragment = BHFragment
                        .newInstance(CreditListFragment_pc.class, input);
                showNextView(fragment);

               //  showNextView(BHFragment.newInstance(CreditMainFragment_intro.class));


            }
        });
        getPaymentAppointmentDateList();
    }

    public void getPaymentAppointmentDateList() {
        new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (paymentAppointmentDateList == null) {
                    paymentAppointmentDateList = new ArrayList<AssignInfo>();
                }
            }

            @Override
            protected void calling() {
                paymentAppointmentDateList.clear();
                // List<AssignInfo> result = new AssignController().getAssignSalePaymentPeriodGroupByPaymentAppointmentDate(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());

                /// แสดงวันที่ถึงวันปิดทริปเท่านั้น
                List<AssignInfo> result = new AssignController().getAssignSalePaymentPeriodGroupByPaymentAppointmentDateNotOverEndOfTrip(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());
                if (result != null && result.size() > 0) {
                    paymentAppointmentDateList.addAll(result);
                }
            }

            @Override
            protected void after() {
                int sum = 0;
                for (AssignInfo paymentAppointmentDate : paymentAppointmentDateList) {
                    sum += paymentAppointmentDate.CountCreditGroupByPaymentAppointmentDate;
                }
                /*** [START] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile ***/
                //txtCountCredit.setText(String.format("ลูกค้าที่ต้องเก็บเงินจำนวน %d คน", sum));
                txtCountCredit.setText(String.format("ลูกค้าที่ต้องเก็บเงินทั้งหมด"));
                /*** [END] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile ***/
                paymentAppointmentDateAdapter.notifyDataSetChanged();
            }
        }.start();
    }

    public class PaymentAppointmentDateAdapter extends BHArrayAdapter<AssignInfo> {

        public PaymentAppointmentDateAdapter(Context context, int resource, List<AssignInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txtPaymentAppointmentDate, txtCountCredit;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, AssignInfo info) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txtPaymentAppointmentDate.setText(BHUtilities.dateFormat(info.PaymentAppointmentDate));
            viewHolder.txtCountCredit.setText("จำนวน " + info.CountCreditGroupByPaymentAppointmentDate + " คน");
        }
    }
}
