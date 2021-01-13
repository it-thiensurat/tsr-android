package th.co.thiensurat.fragments.payment.first;

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
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FirstPaymentCustomerListFragment extends BHFragment {

    private static List<SalePaymentPeriodInfo> mCustomerList;
    @InjectView
    private ListView lvCustomerCont;
    @InjectView
    private TextView txtCustomerNopayment;
    private FirstPaymentGroupCustomerByArea.Result areaData = null;
    private FirstPaymentGroupCustomerByAppointmentDateFragment.Result appointmentDateData = null;
    private String mAssigneeEmpID;
    private Date mAppointmentDate;
    private boolean mFilter = false;

    // private int mSelectedContract = -1;
    // public static class Data extends BHParcelable {
    // public EmployeeDetailInfo EmpDetail;
    // }

    // private Data data;

    private boolean isLeader = false;


    @Override
    public String fragmentTag() {
        // TODO Auto-generated method stub
        return SaleFirstPaymentChoiceFragment.FRAGMENT_SALE_FIRST_PAYMENT_TAG;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_payment_first;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_first_payment_customer_list;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        // return new int[] { R.string.button_back, R.string.button_group,
        // R.string.button_appointment_date };

        // data = getData();
        // if (data.EmpDetail.PositionCode.equals("SaleLeader")) {
        // return new int[] { R.string.button_back, R.string.button_group,
        // R.string.button_appointment_date };
        // } else {
        // return new int[] { R.string.button_back,
        // R.string.button_appointment_date };
        // }

        checkLeader();

        if (isLeader) {
            return new int[]{R.string.button_back, R.string.button_group,
                    R.string.button_appointment_date};
        } else {
            return new int[]{R.string.button_back,
                    R.string.button_appointment_date};
        }
    }

    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // data = getData();
        // if (!mFilter)
        // if (data.EmpDetail.PositionCode.equals("SaleLeader")) {
        // getAllData();
        // } else {
        // getByAssigneeEmpID(BHPreference.employeeID());
        // }

        if (!mFilter) {
            if (isLeader) {
                getAllData();
            } else {
                getByAssigneeEmpID(BHPreference.employeeID());
            }
        }

        setWidgetsEventListener();
    }

    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_group:
                mFilter = true;
                FirstPaymentGroupCustomerByArea fm = BHFragment.newInstance(
                        FirstPaymentGroupCustomerByArea.class, null,
                        new BHFragmentCallback() {

                            @Override
                            public void onResult(BHParcelable data) {
                                // TODO Auto-generated method stub
                                if (data != null) {
                                    FirstPaymentCustomerListFragment.this.areaData = (FirstPaymentGroupCustomerByArea.Result) data;
                                    mAssigneeEmpID = FirstPaymentCustomerListFragment.this.areaData.assigneeEmpID;
                                    // mAssigneeEmpID =
                                    // BHPreference.AssigneeEmpID();
                                    if (FirstPaymentCustomerListFragment.this.areaData.back)
                                        getAllData();
                                    else
                                        getByAssigneeEmpID(mAssigneeEmpID);
                                } else
                                    getAllData();
                            }
                        });
                showNextView(fm);
                break;
            case R.string.button_appointment_date:
                mFilter = true;
                FirstPaymentGroupCustomerByAppointmentDateFragment fmAppointDate = BHFragment
                        .newInstance(
                                FirstPaymentGroupCustomerByAppointmentDateFragment.class,
                                null, new BHFragmentCallback() {

                                    @Override
                                    public void onResult(BHParcelable data) {
                                        // TODO Auto-generated method stub
                                        if (data != null) {
                                            FirstPaymentCustomerListFragment.this.appointmentDateData = (FirstPaymentGroupCustomerByAppointmentDateFragment.Result) data;
                                            mAppointmentDate = FirstPaymentCustomerListFragment.this.appointmentDateData.paymentAppointmentDate;

                                            if (FirstPaymentCustomerListFragment.this.appointmentDateData.back)
                                                getAllData();
                                            else
                                                getByAppointmentDate();
                                        } else
                                            getAllData();
                                    }
                                });
                showNextView(fmAppointDate);
                break;

            default:
                break;
        }
    }

    private void getAllData() {

        (new BackgroundProcess(activity) {

            List<SalePaymentPeriodInfo> output = null;

            @Override
            protected void before() {
                // TODO Auto-generated method stub
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                Log.e("PAGE","ระบบเก็บเงินงวดแรก");
                output = getNextSalePaymentPeriodByContractTeam(
                        BHPreference.organizationCode(),
                        BHPreference.teamCode());
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                try {
                    mCustomerList = null;
                    mCustomerList = output;
                    updateTitle();
                    bindContract();
                } catch (Exception e) {
                    log(e.getMessage());
                }
            }
        }).start();
    }

    private void getByAssigneeEmpID(final String mAssigneeEmpID) {

        (new BackgroundProcess(activity) {
            List<SalePaymentPeriodInfo> output = null;

            @Override
            protected void before() {
                // TODO Auto-generated method stub
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub


                output = getNextSalePaymentPeriodByAssigneeEmpID(
                        BHPreference.organizationCode(), BHPreference.teamCode(), mAssigneeEmpID);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                try {
                    mCustomerList = null;
                    mCustomerList = output;
                    updateTitle();
                    bindContract();
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                }
            }
        }).start();
    }

    private void getByAppointmentDate() {

        (new BackgroundProcess(activity) {
            List<SalePaymentPeriodInfo> output = null;

            @Override
            protected void before() {
                // TODO Auto-generated method stub
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub



                output = getNextSalePaymentPeriodByContractTeamByAppointmentDate(
                        BHPreference.organizationCode(),
                        BHPreference.teamCode(), mAppointmentDate);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                try {
                    mCustomerList = null;
                    mCustomerList = output;
                    updateTitle();
                    bindContract();
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                }
            }
        }).start();

    }

    private void updateTitle() {
        int count = 0;
        if (mCustomerList != null)
            count = mCustomerList.size();
        txtCustomerNopayment.setText(String.format("%s %d %s",
                "ลูกค้าที่ยังไม่ได้ชำระเงินงวดแรก", count, "ราย"));
    }

    private void setWidgetsEventListener() {

        // lvCustomerCont.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int
        // position, long id) {
        // // TODO Auto-generated method stub
        // mFilter = false;
        // mSelectedContract = position - 1;
        // SalePaymentPeriodInfo spp = mCustomerList.get(mSelectedContract);
        // FirstPaymentCustomerDetailFragment.Data selectedData = new
        // FirstPaymentCustomerDetailFragment.Data();
        // selectedData.refNo = spp.RefNo;
        //
        // FirstPaymentCustomerDetailFragment fm =
        // BHFragment.newInstance(FirstPaymentCustomerDetailFragment.class,
        // selectedData);
        // showNextView(fm);
        // }
        // });

    }

    private void bindContract() {
        // LayoutInflater inflater = activity.getLayoutInflater();
        // ViewGroup header = (ViewGroup)
        // inflater.inflate(R.layout.list_first_payment_customer_list_header,
        // lvCustomerCont, false);
        // lvCustomerCont.addHeaderView(header, null, false);

        BHArrayAdapter<SalePaymentPeriodInfo> adapter = new BHArrayAdapter<SalePaymentPeriodInfo>(
                activity, R.layout.list_first_payment_customer_list_item,
                mCustomerList) {

            class ViewHolder {
                public TextView txtContNO;
                public TextView txtCustomerFullName;
                public TextView txtInstallDate;
                public TextView txtPaymentAmount;
                public TextView txtAssigneeEmpName;
                public TextView txtStatusColor;
                public TextView txtPaymentAppointmentDate;
                public ImageButton imgArrow, imgEdit;
                public ImageView imgAdd;

            }

            @Override
            protected void onViewItem(final int position, View view,
                                      Object holder, SalePaymentPeriodInfo info) {
                // TODO Auto-generated method stub

                ViewHolder vh = (ViewHolder) holder;

                String contNO = (String) info.CONTNO;
                String customerFullName = (String) info.CustomerFullName;
                // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
                // Locale.US);
                // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                // String installDate = sdf.format(info.InstallDate);
                String installDate = BHUtilities.dateFormat(
                        info.InstallDate, "dd/MM/yy");
                // String paymentAmount = (String) Float
                // .toString(info.PaymentAmount);
                // String paymentAmount =
                // BHUtilities.numericFormat(info.PaymentAmount);
                String outstandingAmount = BHUtilities
                        .numericFormat(info.OutstandingAmount);
                String assigneeEmpName = (info.AssigneeEmpName == null || info.AssigneeEmpName
                        .isEmpty()) ? "" : (String) info.AssigneeEmpName;

                vh.txtContNO.setText(contNO);
                // vh.txtCustomerFullName.setText("ชื่อลูกค้า : " +
                // BHUtilities.trim(info.CustomerFullName) +
                // BHUtilities.trim(info.CompanyName));
                vh.txtCustomerFullName.setText(String.format("%s %s",
                        BHUtilities.trim(info.CustomerFullName),
                        BHUtilities.trim(info.CompanyName)));
                vh.txtInstallDate.setText(installDate);
                vh.txtPaymentAmount.setText(outstandingAmount);
                vh.txtAssigneeEmpName.setText(assigneeEmpName);
                vh.txtPaymentAppointmentDate.setText(BHUtilities
                        .dateFormat(info.PaymentAppointmentDate));

                vh.imgAdd.setVisibility(View.GONE);
                vh.imgEdit.setVisibility(View.GONE);
                if (isLeader) {
                    if (assigneeEmpName.equals("")) {
                        vh.imgAdd.setVisibility(View.VISIBLE);
                    } else {
                        vh.imgEdit.setVisibility(View.VISIBLE);
                    }
                }

                vh.imgAdd.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mFilter = false;

                        SalePaymentPeriodInfo spp = mCustomerList
                                .get(position);
                        FirstPaymentEmployeeAssignFragment.Data selectedData = new FirstPaymentEmployeeAssignFragment.Data();
                        selectedData.salepaymentperiod = spp;
                        selectedData.customerNoPayment = getCustomerNoPayment();
                        FirstPaymentEmployeeAssignFragment fm = BHFragment
                                .newInstance(
                                        FirstPaymentEmployeeAssignFragment.class,
                                        selectedData);
                        showNextView(fm);
                    }
                });

                vh.imgEdit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mFilter = false;

                        SalePaymentPeriodInfo spp = mCustomerList
                                .get(position);
                        FirstPaymentEmployeeAssignFragment.Data selectedData = new FirstPaymentEmployeeAssignFragment.Data();
                        selectedData.salepaymentperiod = spp;
                        selectedData.customerNoPayment = getCustomerNoPayment();
                        FirstPaymentEmployeeAssignFragment fm = BHFragment
                                .newInstance(
                                        FirstPaymentEmployeeAssignFragment.class,
                                        selectedData);
                        showNextView(fm);
                    }
                });

                vh.imgArrow.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mFilter = false;
                        // mSelectedContract = position - 1;
                        // SalePaymentPeriodInfo spp =
                        // mCustomerList.get(mSelectedContract);
                        SalePaymentPeriodInfo spp = mCustomerList
                                .get(position);
                        FirstPaymentCustomerDetailFragment.Data selectedData = new FirstPaymentCustomerDetailFragment.Data();
                        selectedData.refNo = spp.RefNo;

                        FirstPaymentCustomerDetailFragment fm = BHFragment
                                .newInstance(
                                        FirstPaymentCustomerDetailFragment.class,
                                        selectedData);
                        showNextView(fm);
                    }
                });

                if (BHPreference.fortnightPerYear() != null) {
                    FortnightInfo forthNigth = new TSRController()
                            .getCurrentFortnight("0");

                    int fortnightPerYear = Integer.parseInt(BHPreference
                            .fortnightPerYear());
                    int diffFortNightYear = forthNigth.FortnightYear
                            - info.FortnightYear;
                    int diffForthNight = (forthNigth.FortnightNumber + diffFortNightYear
                            * fortnightPerYear)
                            - info.FortnightNumber;

                    // int countPostpone = info.CountPostpone;
                    if (diffForthNight > 2)
                        vh.txtStatusColor.setBackgroundColor(getResources()
                                .getColor(R.color.bg_loss_payment_status_4));
                    else if (diffForthNight == 2)
                        vh.txtStatusColor.setBackgroundColor(getResources()
                                .getColor(R.color.bg_loss_payment_status_3));
                    else if (diffForthNight == 1)
                        vh.txtStatusColor.setBackgroundColor(getResources()
                                .getColor(R.color.bg_loss_payment_status_2));
                    else
                        vh.txtStatusColor.setBackgroundColor(getResources()
                                .getColor(R.color.bg_loss_payment_status_1));
                }
            }
        };
        lvCustomerCont.setAdapter(adapter);
    }

    private int getCustomerNoPayment() {
        int ret = 0;
        for (SalePaymentPeriodInfo item : mCustomerList) {
            if (item.AssigneeEmpID == null || item.AssigneeEmpID.isEmpty())
                ret += 1;
        }
        return ret;
    }

    private void checkLeader() {
        String strUserPosition = BHPreference.PositionCode();
        String[] arrUserPosition = strUserPosition.split(",");
        //for(x=0;x<resultArr.length;x++)
        int cnt = 0;
        for (int i = 0; i < arrUserPosition.length; i++) {
            if (arrUserPosition[i].toString().toLowerCase().equals("saleleader") || arrUserPosition[i].toString().toLowerCase().equals("subteamleader")) {
                cnt += 1;
            }
        }
        if (cnt > 0)
            isLeader = true;
        else
            isLeader = false;
    }
}
