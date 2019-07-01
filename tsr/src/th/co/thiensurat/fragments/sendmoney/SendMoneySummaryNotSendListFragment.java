package th.co.thiensurat.fragments.sendmoney;

import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.info.PaymentInfo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SendMoneySummaryNotSendListFragment extends BHFragment {

    private List<PaymentInfo> mPaymentList = null;
    private int mSelectedPayment = -1;
    @InjectView private ListView lvSumPaymentDate;


    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_payment_send;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sendmoney_summary_not_send_list;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        loadData();
        setWidgetsEventListener();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
    }

    private void loadData() {
        (new BackgroundProcess(activity) {

            //List<PaymentInfo> output;

            @Override
            protected void before() {
                // TODO Auto-generated method stub
            }

            @Override
            protected void calling() {
//                String EmpIDOfMember;
//                if (BHPreference.PositionCode().contains("SaleLeader")) {
//                    EmpIDOfMember = new EmployeeDetailController().getMemberByTeamCodeOrSubTeamCode(BHPreference.organizationCode(), BHPreference.teamCode(), "");
//                } else if (BHPreference.PositionCode().contains("SubTeamLeader")) {
//                    EmpIDOfMember = new EmployeeDetailController().getMemberByTeamCodeOrSubTeamCode(BHPreference.organizationCode(), "", BHPreference.SubTeamCode());
//                } else {
//                    EmpIDOfMember = BHPreference.employeeID();
//                }
                mPaymentList = getSummaryPaymentGroupByPaymentDate(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                try {
                    //mPaymentList = null;
                    //mPaymentList = output;
                    if (mPaymentList != null)
                        bindPaymentList();
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                }
            }
        }).start();
    }

    private void setWidgetsEventListener() {
        lvSumPaymentDate.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                mSelectedPayment = position - 1;

                PaymentInfo payment = mPaymentList.get(mSelectedPayment);
                SendMoneySummaryByPaymentTypeFragment.Data selectedData = new SendMoneySummaryByPaymentTypeFragment.Data();
                selectedData.payDate = payment.PayDate;
                selectedData.payAMT = payment.PAYAMT;

                SendMoneySummaryByPaymentTypeFragment fm = BHFragment.newInstance(SendMoneySummaryByPaymentTypeFragment.class, selectedData);
                showNextView(fm);
            }
        });
    }

    private void bindPaymentList() {
        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_sendmoney_summary_not_send_list_header, lvSumPaymentDate, false);
        lvSumPaymentDate.addHeaderView(header, null, false);

        BHArrayAdapter<PaymentInfo> adapter = new BHArrayAdapter<PaymentInfo>(activity, R.layout.list_sendmoney_summary_not_send_list_item, mPaymentList) {

            class ViewHolder {
                public TextView txtPayDate;
                public TextView txtPAYAMT;
            }

            @Override
            protected void onViewItem(final int position, View view,
                                      Object holder, PaymentInfo info) {
                // TODO Auto-generated method stub

                ViewHolder vh = (ViewHolder) holder;
                try {
                    String strPayDate = BHUtilities.dateFormat(info.PayDate,
                            "dd/MM/yyyy");
                    vh.txtPayDate.setText(strPayDate);
                    vh.txtPAYAMT.setText(BHUtilities.numericFormat(info.PAYAMT));

                } catch (Exception e) {
                    // e.printStackTrace();
                    Log.e(this.getClass().getName(), e.getMessage());
                }
            }
        };
        lvSumPaymentDate.setAdapter(adapter);
    }

}
