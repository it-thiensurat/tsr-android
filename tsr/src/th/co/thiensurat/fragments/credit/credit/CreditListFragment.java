package th.co.thiensurat.fragments.credit.credit;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;

public class CreditListFragment extends BHFragment {
    @InjectView
    private TextView txtCountCredit;
    @InjectView
    ListView listView;
    @InjectView
    EditText edtSearch;
    @InjectView
    Button btnSearch;

    CheckBox chkHeader;
    boolean selectedChkHeader;

    private CustomerAdapter customerAdapter;
    private List<AssignInfo> creditList;
    private Data data;

    private boolean isSearch;   // Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile

    public static class Data extends BHParcelable {
        public Date selectedDate;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_list;
    }

    @Override
    protected int[] processButtons() {
        //return new int[]{R.string.button_back,R.string.button_credit_choose_date, R.string.button_display_map};
        return new int[]{R.string.button_credit_choose_date, R.string.button_display_map};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();

        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup headerListView = (ViewGroup) inflater.inflate(R.layout.list_credit_header, listView, false);
        listView.addHeaderView(headerListView, null, false);

        chkHeader = (CheckBox) headerListView.findViewById(R.id.chkHeader);
        chkHeader.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectedChkHeader = b;
            }
        });
        chkHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (creditList != null && creditList.size() > 0) {
                    for (AssignInfo info : creditList) {
                        info.Selected = selectedChkHeader;
                    }
                    if (customerAdapter != null) {
                        customerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        chkHeader.setChecked(selectedChkHeader);

        if (creditList == null) {
            getCreditList("%" + edtSearch.getText().toString() + "%");
        }else{
            /*** [START] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile ***/
            //txtCountCredit.setText(String.format("วันที่ " + BHUtilities.dateFormat(data.selectedDate) + " ลูกค้าที่ต้องเก็บเงินจำนวน %d คน", creditList.size()));
            if(isSearch){
                txtCountCredit.setText(String.format("ลูกค้าที่ต้องเก็บเงินจำนวน %d คน", creditList.size()));
            } else {
                txtCountCredit.setText(String.format("วันที่ " + BHUtilities.dateFormat(data.selectedDate) + " ลูกค้าที่ต้องเก็บเงินจำนวน %d คน", creditList.size()));
            }
            /*** [END] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile ***/
        }

        customerAdapter = new CustomerAdapter(activity, R.layout.list_credit, creditList);
        listView.setAdapter(customerAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedChkHeader = false;
                /*** [START] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile ***/
                isSearch = true;
                //getCreditList("%" + edtSearch.getText().toString() + "%");
                getNewCreditList("%" + edtSearch.getText().toString() + "%");
                /*** [END] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile ***/
            }
        });

    }

    public void callCreditDetail(AssignInfo assign) {
        // next payment
        CreditDetailFragment.Data input = new CreditDetailFragment.Data();
        input.AssignID = assign.AssignID;
        input.selectedDate = data.selectedDate;
        CreditDetailFragment fragment = BHFragment
                .newInstance(CreditDetailFragment.class, input);
        showNextView(fragment);
    }


    public void callCreditPayment(AssignInfo assign){

        float total = Double.valueOf("0").floatValue();
        SaleFirstPaymentChoiceFragment.Data paymentData = new SaleFirstPaymentChoiceFragment.Data(assign.RefNo,
                SaleFirstPaymentChoiceFragment.ProcessType.Credit, total, assign.AssignID, data.selectedDate);
        BHPreference.setProcessType(SaleFirstPaymentChoiceFragment.ProcessType.Credit.toString());
        BHPreference.setRefNo(assign.RefNo);
        SaleFirstPaymentChoiceFragment fragment = BHFragment.newInstance(SaleFirstPaymentChoiceFragment.class, paymentData);
        showNextView(fragment);
    }

    public void getCreditList(final String search) {
        new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (creditList == null) {
                    creditList = new ArrayList<AssignInfo>();
                }
            }

            @Override
            protected void calling() {
                creditList.clear();
                List<AssignInfo> result = new AssignController().getSalePaymentPeriodListForAssignCredit(BHPreference.organizationCode(),
                        BHPreference.teamCode(), BHPreference.employeeID(), data.selectedDate, search, AddressInfo.AddressType.AddressPayment.toString());
                if (result != null && result.size() > 0) {
                    creditList.addAll(result);
                }
            }

            @Override
            protected void after() {
                txtCountCredit.setText(String.format("วันที่ " + BHUtilities.dateFormat(data.selectedDate) + " ลูกค้าที่ต้องเก็บเงินจำนวน %d คน", creditList.size()));
                if (creditList.size() <= 0) {
                    BHUtilities.alertDialog(activity, "แจ้งเตือน", "ไม่พบข้อมูลการเก็บเงินของวันที่ " + BHUtilities.dateFormat(data.selectedDate)).show();
                }
                chkHeader.setChecked(selectedChkHeader);
                customerAdapter.notifyDataSetChanged();
            }
        }.start();
    }

    /*** [START] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile ***/
    public void getNewCreditList(final String search) {
        new BackgroundProcess(activity) {
            List<AssignInfo> result;

            @Override
            protected void before() {
                if (creditList == null) {
                    creditList = new ArrayList<AssignInfo>();
                }
            }

            @Override
            protected void calling() {
                /*creditList.clear();
                List<AssignInfo> result = new AssignController().getNewSalePaymentPeriodListForAssignCredit(BHPreference.organizationCode(),
                        BHPreference.teamCode(), BHPreference.employeeID(), search, AddressInfo.AddressType.AddressPayment.toString());
                if (result != null && result.size() > 0) {
                    creditList.addAll(result);
                }*/

               result = new AssignController().getNewSalePaymentPeriodListForAssignCredit(BHPreference.organizationCode(),
                        BHPreference.teamCode(), BHPreference.employeeID(), search, AddressInfo.AddressType.AddressPayment.toString());
            }

            @Override
            protected void after() {
                creditList.clear();
                if (result != null && result.size() > 0) {
                    creditList.addAll(result);
                }

                txtCountCredit.setText(String.format("ลูกค้าที่ต้องเก็บเงินจำนวน %d คน", creditList.size()));
                if (creditList.size() <= 0) {
                    BHUtilities.alertDialog(activity, "แจ้งเตือน", "ไม่พบข้อมูลการเก็บเงินของวันที่ " + BHUtilities.dateFormat(data.selectedDate)).show();
                }
                chkHeader.setChecked(selectedChkHeader);
                customerAdapter.notifyDataSetChanged();
            }
        }.start();
    }
    /*** [END] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile ***/

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                //showLastView();
                activity.showView(BHFragment.newInstance(CreditMainFragment.class));
                break;
            case R.string.button_credit_choose_date :
                CreditMainFragment selecte_date_fragment = BHFragment.newInstance(CreditMainFragment.class);
                showNextView(selecte_date_fragment);
                break;
            case R.string.button_display_map:
                List<AssignInfo> customerList = new ArrayList<AssignInfo>();
                if (creditList != null && creditList.size() > 0) {
                    for (AssignInfo assign : creditList) {
                        if (assign.Selected) {
                            customerList.add(assign);
                        }
                    }
                }
                switch (customerList.size()) {
                    case 0:
                        BHUtilities.alertDialog(activity, "แจ้งเตือน", "กรุณาเลือกข้อมูล").show();
                        break;
                    default:
                        CreditMapFragment.Data input = new CreditMapFragment.Data();
                        input.customerList = customerList;
                        input.selectedDate = data.selectedDate;
                        CreditMapFragment fragment = BHFragment
                                .newInstance(CreditMapFragment.class, input);
                        showNextView(fragment);
                        break;
                }
                break;
            default:
                break;
        }
    }

    public class CustomerAdapter extends BHArrayAdapter<AssignInfo> {

        public CustomerAdapter(Context context, int resource, List<AssignInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txtNo, txtCustomerFullNameAndCONTNO, txtPaymentPeriodNumber, txtNetAmount;
            public CheckBox chkSelected;
            public LinearLayout fakeClick;
        }

        @Override
        protected void onViewItem(final int position, View view, Object holder, AssignInfo info) {
            final ViewHolder viewHolder = (ViewHolder) holder;

            /*** [START] :: Fixed - [BHPROJ-0026-3278] :: [Android-ระบบเก็บเงินค่างวด] การแสดงลำดับในหน้าแสดงรายชื่อลูกค้าเก็บเงิน นำตัวเลข Order Expect มาแสดงได้เลย  ***/
            viewHolder.txtNo.setText(String.valueOf(info.OrderExpect));
            /*** [END] :: Fixed - [BHPROJ-0026-3278] :: [Android-ระบบเก็บเงินค่างวด] การแสดงลำดับในหน้าแสดงรายชื่อลูกค้าเก็บเงิน นำตัวเลข Order Expect มาแสดงได้เลย  ***/

            /*** [START] :: Fixed - [BHPROJ-0026-740] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/
//            AddressInfo addressInfo = TSRController.getAddress(info.RefNo, AddressInfo.AddressType.AddressPayment);

            SpannableString txtCustomer = new SpannableString( info.CustomerFullName + "\n" + info.CONTNO + "\n" + info.getAddress().Address() + "\nTel. " + info.getAddress().Telephone());
            txtCustomer.setSpan(new ForegroundColorSpan(getResources().getColor(getColor(info.HoldSalePaymentPeriod))), 0, info.CustomerFullName.length(), 0);
            txtCustomer.setSpan(new ForegroundColorSpan(Color.BLACK), info.CustomerFullName.length(), txtCustomer.length(), 0);
            txtCustomer.setSpan(new ForegroundColorSpan(Color.GRAY), info.CustomerFullName.length() + info.CONTNO.length() + 1, txtCustomer.length(), 0);
            viewHolder.txtCustomerFullNameAndCONTNO.setText(txtCustomer, TextView.BufferType.SPANNABLE);

            //viewHolder.txtCustomerFullNameAndCONTNO.setText(info.CustomerFullName + "\n" + info.CONTNO);
            /*** [END] :: Fixed - [BHPROJ-0026-74            0] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/

            /*** [START] :: Fixed - [BHPROJ-0026-3280] :: [Android-ระบบเก็บเงินค่างวด] ในหน้าจอรายการสัญญาทั้งหมดของวัน AppointmentDate ที่ถูกเลือก การแสดงตัวเลขงวดเก็บเงิน ให้แสดงงวดต่ำสุด  ***/
            if(info.MinPaymentPeriodNumber == info.PaymentPeriodNumber) {
                viewHolder.txtPaymentPeriodNumber.setText(String.valueOf(info.PaymentPeriodNumber));
            } else {
                viewHolder.txtPaymentPeriodNumber.setText(String.format("%d-%d", info.MinPaymentPeriodNumber, info.PaymentPeriodNumber));
            }
            /*** [END] :: Fixed - [BHPROJ-0026-3280] :: [Android-ระบบเก็บเงินค่างวด] ในหน้าจอรายการสัญญาทั้งหมดของวัน AppointmentDate ที่ถูกเลือก การแสดงตัวเลขงวดเก็บเงิน ให้แสดงงวดต่ำสุด  ***/

            viewHolder.txtNetAmount.setText(BHUtilities.numericFormat(Double.valueOf(info.OutStandingPayment)));
            viewHolder.chkSelected.setChecked(info.Selected);
            viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    creditList.get(position).Selected = b;
                    if (!b) {
                        selectedChkHeader = false;
                        chkHeader.setChecked(selectedChkHeader);
                    } else {
                        int count = 0;
                        for (AssignInfo assign : creditList) {
                            if (assign.Selected) {
                                count++;
                            }
                        }
                        if (count == creditList.size()) {
                            selectedChkHeader = true;
                            chkHeader.setChecked(selectedChkHeader);
                        }
                    }
                }
            });
            viewHolder.fakeClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.chkSelected.setChecked(!viewHolder.chkSelected.isChecked());
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //callCreditDetail(creditList.get(position));
                    callCreditPayment(creditList.get(position));
                }
            });
            //view.setBackgroundColor(getResources().getColor(getColor(info.HoldSalePaymentPeriod)));
        }

        public int getColor(int HoldSalePaymentPeriod) {
            switch (HoldSalePaymentPeriod) {
                case 0:
                    return R.color.hold_payment_0;
                case 1:
                    return R.color.hold_payment_1;
                case 2:
                    return R.color.hold_payment_2;
                default:
                    return R.color.hold_payment_3;
            }
        }
    }
}