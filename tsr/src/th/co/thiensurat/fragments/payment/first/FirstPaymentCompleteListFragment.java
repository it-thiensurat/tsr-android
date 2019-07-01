package th.co.thiensurat.fragments.payment.first;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleContractPrintFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;

public class FirstPaymentCompleteListFragment extends BHFragment {
    @InjectView
    private TextView txtCountCredit;
    @InjectView
    ListView listView;

    public static class Data extends BHParcelable {
        public Date PayDate;
    }

    private SalePaymentPeriodAdapter salePaymentPeriodAdapter;
    private List<SalePaymentPeriodInfo> salePaymentPeriodList;
    private Data data;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_first_payment_complete_list;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();

        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup headerListView = (ViewGroup) inflater.inflate(R.layout.list_first_payment_complete_header, listView, false);
        listView.addHeaderView(headerListView, null, false);

        salePaymentPeriodList = new ArrayList<>();
        salePaymentPeriodAdapter = new SalePaymentPeriodAdapter(activity, R.layout.list_first_payment_complete, salePaymentPeriodList);
        listView.setAdapter(salePaymentPeriodAdapter);

        getFirstPaymentCompleteListByPaymentAppointDate(BHPreference.organizationCode(), BHPreference.teamCode()
                , checkLeader() ? null : BHPreference.employeeID(), data.PayDate);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SalePaymentPeriodInfo info = (SalePaymentPeriodInfo) adapterView.getItemAtPosition(position);
                BHPreference.setProcessType(SaleFirstPaymentChoiceFragment.ProcessType.ViewCompletedContract.toString());
                BHPreference.setRefNo(info.RefNo);
                SaleContractPrintFragment.Data input = new SaleContractPrintFragment.Data();
                input.resTitle = R.string.title_payment_first;
                showNextView(BHFragment.newInstance(SaleContractPrintFragment.class, input));
            }
        });
    }

    public void getFirstPaymentCompleteListByPaymentAppointDate(final String OrganizationCode, final String AssigneeTeamCode, final String AssigneeEmpID, final Date PaymentAppointmentDate) {
        new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (salePaymentPeriodList == null) {
                    salePaymentPeriodList = new ArrayList<>();
                } else {
                    salePaymentPeriodList.clear();
                }
            }

            @Override
            protected void calling() {
                List<SalePaymentPeriodInfo> result = new SalePaymentPeriodController().getFirstPaymentCompleteByPayDate(
                        OrganizationCode, AssigneeTeamCode, AssigneeEmpID, PaymentAppointmentDate);
                if (result != null && result.size() > 0) {
                    salePaymentPeriodList.addAll(result);
                }
            }

            @Override
            protected void after() {
                txtCountCredit.setText(String.format("วันที่ " + BHUtilities.dateFormat(data.PayDate) + " ลูกค้าที่เก็บเงินงวดแรกแล้ว %d คน", salePaymentPeriodList.size()));
                salePaymentPeriodAdapter.notifyDataSetChanged();
            }
        }.start();
    }

    public class SalePaymentPeriodAdapter extends BHArrayAdapter<SalePaymentPeriodInfo> {

        public SalePaymentPeriodAdapter(Context context, int resource, List<SalePaymentPeriodInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txtNo, txtCustomerFullNameAndCONTNO, txtPaymentPeriodNumber, txtNetAmount;
        }

        @Override
        protected void onViewItem(final int position, View view, Object holder, SalePaymentPeriodInfo info) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txtNo.setText(String.valueOf(position + 1));
            viewHolder.txtCustomerFullNameAndCONTNO.setText(info.CustomerFullName + "\n" + info.CONTNO);
            viewHolder.txtPaymentPeriodNumber.setText(String.valueOf(info.PaymentPeriodNumber));
            viewHolder.txtNetAmount.setText(BHUtilities.numericFormat(info.FirstPaymentAmount));
        }
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

    private boolean checkLeader() {
        String[] arrUserPosition = BHPreference.PositionCode().split(",");
        for (String s : arrUserPosition) {
            if (s.equals("SaleLeader") || s.equals("SubTeamLeader")) {
                return true;
            }
        }
        return false;
    }

}
