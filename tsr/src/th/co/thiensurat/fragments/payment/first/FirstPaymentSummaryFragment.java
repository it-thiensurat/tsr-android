package th.co.thiensurat.fragments.payment.first;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirstPaymentSummaryFragment extends BHFragment {

    @InjectView
    FloatingGroupExpandableListView expandableListView;
    @InjectView
    TextView txtEmpID, txtEmployeeFullName;

    WrapperExpandableListAdapter wrapperAdapter;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<SalePaymentPeriodInfo>> listDataChild;

    @Override
    protected int titleID() {
        return R.string.title_payment_first;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_first_payment_summary;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back};
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

    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        prepareListData(BHPreference.organizationCode(), BHPreference.teamCode(), checkLeader() ? null : BHPreference.employeeID());
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (getEnum(groupPosition) == Types.SummaryFirstPaymentCompleted) {
                    SalePaymentPeriodInfo info = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                    if (!info.isLastChild) {
                        FirstPaymentCompleteListFragment.Data input = new FirstPaymentCompleteListFragment.Data();
                        input.PayDate = info.PayDate;
                        showNextView(BHFragment.newInstance(FirstPaymentCompleteListFragment.class, input));
                    }
                }
                return false;
            }
        });
    }

    private void prepareListData(final String OrganizationCode, final String teamCode, final String assigneeEmpID) {
        new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (listDataHeader == null) {
                    listDataHeader = new ArrayList<>();
                } else {
                    listDataHeader.clear();
                }
                if (listDataChild == null) {
                    listDataChild = new HashMap<>();
                } else {
                    listDataChild.clear();
                }
            }

            @Override
            protected void calling() {
                listDataHeader.add(getResources().getString(R.string.title_summary_first_payment));
                listDataHeader.add(getResources().getString(R.string.title_summary_first_payment_completed));
                listDataHeader.add(getResources().getString(R.string.title_summary_first_payment_hold));

                List<SalePaymentPeriodInfo> summaryFirstPaymentList = TSRController.getSummaryFirstPayment(OrganizationCode, teamCode, assigneeEmpID);
                List<SalePaymentPeriodInfo> summaryFirstPaymentCompleteList = TSRController.getSummaryFirstPaymentComplete(OrganizationCode, teamCode, assigneeEmpID);
                List<SalePaymentPeriodInfo> summaryNextPaymentList = TSRController.getSummaryNextPayment(OrganizationCode, teamCode, assigneeEmpID);

                if (summaryFirstPaymentList == null) {
                    summaryFirstPaymentList = new ArrayList<>();
                }
                if (summaryFirstPaymentCompleteList == null) {
                    summaryFirstPaymentCompleteList = new ArrayList<>();
                }
                if (summaryNextPaymentList == null) {
                    summaryNextPaymentList = new ArrayList<>();
                }
                // Footer
                SalePaymentPeriodInfo fSummaryFirstPayment = new SalePaymentPeriodInfo();
                fSummaryFirstPayment.isLastChild = true;
                SalePaymentPeriodInfo fSummaryFirstPaymentComplete = new SalePaymentPeriodInfo();
                fSummaryFirstPaymentComplete.isLastChild = true;
                SalePaymentPeriodInfo fSummaryNextPayment = new SalePaymentPeriodInfo();
                fSummaryNextPayment.isLastChild = true;

                summaryFirstPaymentList.add(summaryFirstPaymentList.size(), fSummaryFirstPayment);
                summaryFirstPaymentCompleteList.add(summaryFirstPaymentCompleteList.size(), fSummaryFirstPaymentComplete);
                summaryNextPaymentList.add(summaryNextPaymentList.size(), fSummaryNextPayment);

                listDataChild.put(listDataHeader.get(0), summaryFirstPaymentList);
                listDataChild.put(listDataHeader.get(1), summaryFirstPaymentCompleteList);
                listDataChild.put(listDataHeader.get(2), summaryNextPaymentList);
            }

            @Override
            protected void after() {
                txtEmpID.setText(BHPreference.employeeID());
                txtEmployeeFullName.setText(BHPreference.userFullName());
                listAdapter = new ExpandableListAdapter(activity, listDataHeader, listDataChild);
                wrapperAdapter = new WrapperExpandableListAdapter(listAdapter);
                expandableListView.setAdapter(wrapperAdapter);
            }
        }.start();
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

    public enum Types {
        SummaryFirstPayment, SummaryFirstPaymentCompleted, SummaryFirstPaymentHold
    }

    private Types getEnum(int type) {
        switch (type) {
            case 0:
                return Types.SummaryFirstPayment;
            case 1:
                return Types.SummaryFirstPaymentCompleted;
            default:
                return Types.SummaryFirstPaymentHold;
        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader;
        private HashMap<String, List<SalePaymentPeriodInfo>> _listDataChild;


        public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<SalePaymentPeriodInfo>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupType(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getChildTypeCount() {
            return _listDataHeader.size();
        }

        @Override
        public int getGroupTypeCount() {
            return _listDataHeader.size();
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final SalePaymentPeriodInfo childText = (SalePaymentPeriodInfo) getChild(groupPosition, childPosition);

            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(getItemLayout(getChildType(groupPosition, childPosition)), null);

            if (!isLastChild) {
                Types type = getEnum(getChildType(groupPosition, childPosition));
                if (type == Types.SummaryFirstPayment) {
                    TextView txtPaymentAppointmentDate = (TextView) convertView.findViewById(R.id.txtPaymentAppointmentDate);
                    TextView txtPaymentAppointmentDateCount = (TextView) convertView.findViewById(R.id.txtPaymentAppointmentDateCount);
                    TextView txtPaymentAppointmentDateSumAmount = (TextView) convertView.findViewById(R.id.txtPaymentAppointmentDateSumAmount);
                    txtPaymentAppointmentDate.setText(BHUtilities.dateFormat(childText.PaymentAppointmentDate));
                    txtPaymentAppointmentDateCount.setText(String.valueOf(childText.PaymentAppointmentDateCount));
                    txtPaymentAppointmentDateSumAmount.setText(BHUtilities.numericFormat(childText.SummaryNetAmount));
                } else if (type == Types.SummaryFirstPaymentCompleted) {
                    TextView txtPayDate = (TextView) convertView.findViewById(R.id.txtPayDate);
                    TextView txtCountRefNo = (TextView) convertView.findViewById(R.id.txtCountRefNo);
                    TextView txtSumFirstPaymentAmount = (TextView) convertView.findViewById(R.id.txtSumFirstPaymentAmount);
                    txtPayDate.setText(BHUtilities.dateFormat(childText.PayDate));
                    txtCountRefNo.setText(String.valueOf(childText.CountRefNo));
                    txtSumFirstPaymentAmount.setText(BHUtilities.numericFormat(childText.SumFirstPaymentAmount));
                } else if (type == Types.SummaryFirstPaymentHold) {
                    TextView txtPaymentAppointmentDate = (TextView) convertView.findViewById(R.id.txtPaymentAppointmentDate);
                    TextView txtPaymentAppointmentDateCount = (TextView) convertView.findViewById(R.id.txtPaymentAppointmentDateCount);
                    TextView txtPaymentAppointmentDateSumAmount = (TextView) convertView.findViewById(R.id.txtPaymentAppointmentDateSumAmount);
                    txtPaymentAppointmentDate.setText(BHUtilities.dateFormat(childText.PaymentAppointmentDate));
                    txtPaymentAppointmentDateCount.setText(String.valueOf(childText.PaymentAppointmentDateCount));
                    txtPaymentAppointmentDateSumAmount.setText(BHUtilities.numericFormat(childText.SummaryNetAmount));
                }
            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);

            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(getHeaderLayout(getGroupType(groupPosition)), null);

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setText(headerTitle);

            LinearLayout viewHeader = (LinearLayout) convertView.findViewById(R.id.viewHeader);
            viewHeader.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            viewHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            ImageView imgGroupIndicator = (ImageView) convertView.findViewById(R.id.imgGroupIndicator);
            imgGroupIndicator.setBackgroundResource(isExpanded ? R.drawable.minus : R.drawable.plus);

            RelativeLayout viewGroupIndicator = (RelativeLayout) convertView.findViewById(R.id.viewGroupIndicator);
            viewGroupIndicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExpandableListView mExpandableListView = (ExpandableListView) parent;
                    if (isExpanded) {
                        mExpandableListView.collapseGroup(groupPosition);
                    } else {
                        mExpandableListView.expandGroup(groupPosition);
                    }
                }
            });

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public int getHeaderLayout(int type) {
            switch (getEnum(type)) {
                case SummaryFirstPayment:
                    return R.layout.first_payment_summary_list_header;
                case SummaryFirstPaymentCompleted:
                    return R.layout.first_payment_summary_completed_list_header;
                default:
                    return R.layout.first_payment_summary_hold_list_header;
            }
        }

        public int getItemLayout(int type) {
            switch (getEnum(type)) {
                case SummaryFirstPayment:
                    return R.layout.first_payment_summary_list_item;
                case SummaryFirstPaymentCompleted:
                    return R.layout.first_payment_summary_completed_list_item;
                default:
                    return R.layout.first_payment_summary_hold_list_item;
            }
        }
    }
}
