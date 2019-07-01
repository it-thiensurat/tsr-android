package th.co.thiensurat.fragments.sendmoney;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
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
import th.co.thiensurat.data.info.PaymentInfo;

public class SendMoneySummaryByPaymentTypeFragment extends BHFragment {

    private List<PaymentInfo> mPaymentTypeList;
    private Date mPayDate;
    private float mPAYAMT;
    private String mPaymentType = "";
    private String mPaymentTypeName = "";

    @InjectView private ListView lvSumPaymentType;
    @InjectView private TextView txtPayDate;
    @InjectView private TextView txtPAYAMTTotal;
    @InjectView private EditText edtSendDate;
    @InjectView private ImageButton btnSendDate;
    @InjectView private LinearLayout sendmoneydateview;
    @InjectView private LinearLayout sendmoneydateheaderview;
    @InjectView private EditText edtSendAmount;
    @InjectView private TextView txtRemain;

    Calendar c = Calendar.getInstance();
    PaymentInfo payment;

    public static class Data extends BHParcelable {
        public Date payDate;
        public float payAMT;
    }

    private Data data;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_payment_send;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sendmoney_summary_by_paymenttype;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back, R.string.button_next};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();
        mPayDate = data.payDate;
        mPAYAMT = data.payAMT;

        clearData();
        setSendMoneyDateVisibility(View.GONE);
        updateTitle();
        loadData();
        setWidgetsEventListener();
        calculate();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_next:
                final String _senddate = edtSendDate.getText().toString();
                final String _sendamount = edtSendAmount.getText().toString();
                if (verifySendData(_senddate, _sendamount) == true) {
                    String strSendAmount = _sendamount.replaceAll(",", "");
                    float sendAmount = Float.parseFloat(strSendAmount);
                    if(sendAmount > 49000) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("แจ้งเตือน");
                        builder.setMessage("การส่งเงินทาง Lotus จะส่งเงินได้ไม่เกิน 49,000.00 บาทต่อครั้ง ท่านต้องการยืนยันจำนวนเงินที่ต้องการนำส่งหรือไม่");
                        builder.setCancelable(false);
                        builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                next();
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        next();
                    }

                }
                break;
            default:
                break;
        }
    }

    private void updateTitle() {
        txtPayDate.setText(BHUtilities.dateFormat(mPayDate, "dd/MM/yyyy"));
        txtPAYAMTTotal.setText(BHUtilities.numericFormat(mPAYAMT));
    }

    private void next() {
        SendMoneySelectChannelFragment.Data selectedData = new SendMoneySelectChannelFragment.Data();
        selectedData.paymentType = mPaymentType;
        selectedData.paymentTypeName = mPaymentTypeName;
        selectedData.payDate = mPayDate;
        String strSendAmount = edtSendAmount.getText().toString().replaceAll("[,]", "");
        float sendAmount = (strSendAmount == null || strSendAmount.isEmpty()) ? 0 : Float.parseFloat(strSendAmount);
        selectedData.sendAmount = sendAmount;

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        selectedData.sendDate = c.getTime();

        /*String sendDateString = edtSendDate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = sdf.parse(sendDateString);
            selectedData.sendDate = convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        SendMoneySelectChannelFragment fm = BHFragment.newInstance(SendMoneySelectChannelFragment.class, selectedData);
        showNextView(fm);
    }

    private void loadData() {
        (new BackgroundProcess(activity) {

            List<PaymentInfo> output;

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
                output = getSummaryPaymentGroupByPaymentTypeByPaymentDate(BHPreference.organizationCode(), mPayDate, BHPreference.teamCode(), BHPreference.employeeID());
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                try {
                    mPaymentTypeList = null;
                    mPaymentTypeList = output;
                    bindPaymentTypeList();
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                }
            }
        }).start();
    }

    public class NumberTextWatcher implements TextWatcher {

        private DecimalFormat df;
        private DecimalFormat dfnd;
        private boolean hasFractionalPart;

        private EditText et;

        public NumberTextWatcher(EditText et) {
            df = new DecimalFormat("#,##0.00");
            df.setDecimalSeparatorAlwaysShown(true);
            dfnd = new DecimalFormat("#,##0");
            this.et = et;
            hasFractionalPart = false;
        }

        @SuppressWarnings("unused")
        private static final String TAG = "NumberTextWatcher";

        public void afterTextChanged(Editable s) {
            et.removeTextChangedListener(this);

            try {
                int inilen, endlen;
                inilen = et.getText().length();

                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                Number n = df.parse(v);
                int cp = et.getSelectionStart();
                if (hasFractionalPart) {
                    et.setText(df.format(n));
                } else {
                    et.setText(dfnd.format(n));
                }
                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel <= et.getText().length()) {
                    et.setSelection(sel);
                } else {
                    // place cursor at the end?
                    et.setSelection(et.getText().length() - 1);
                }
            } catch (NumberFormatException nfe) {
                // do nothing?
            } catch (ParseException e) {
                // do nothing?
            }

            et.addTextChangedListener(this);
            calculate();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
                hasFractionalPart = true;
            } else {
                hasFractionalPart = false;
            }
        }
    }

    private void setWidgetsEventListener() {

        final DatePickerDialog.OnDateSetListener dpl = new OnDateSetListener() {

//            int chkDisplay = 0;

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Calendar todayWithoutTime = Calendar.getInstance();
                todayWithoutTime.set(Calendar.HOUR_OF_DAY, 0);
                todayWithoutTime.set(Calendar.MINUTE, 0);
                todayWithoutTime.set(Calendar.SECOND, 0);
                todayWithoutTime.set(Calendar.MILLISECOND, 0);
                if (view.isShown()) {
                    if (c.getTime().before(todayWithoutTime.getTime())) {
                        String title = "คำเตือน";
                        String message = "ห้ามเลือกวันที่ส่งเงินย้อนหลัง!";
                        showNoticeDialogBox(title, message);
                        edtSendDate.setText("");
                    } else {
                        edtSendDate.setText(BHUtilities.dateFormat(c.getTime(), "dd/MM/yyyy"));
                    }
                }
            }
        };

        btnSendDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                DatePickerDialog dpd = new DatePickerDialog(arg0.getContext(),
                        dpl, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        lvSumPaymentType.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                setSendMoneyDateVisibility(View.VISIBLE);
                payment = mPaymentTypeList.get(position);
                mPaymentType = payment.PaymentType;
                mPaymentTypeName = payment.PaymentTypeName;
                String strDiffSendAmount = BHUtilities.numericFormat(payment.DiffSendAmount);
                edtSendAmount.setText(strDiffSendAmount);
                calculate();
            }
        });

        edtSendAmount.addTextChangedListener(new NumberTextWatcher(edtSendAmount));
        /*edtSendAmount.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                //if ((event.getAction() == KeyEvent.ACTION_DOWN)
                //&& (keyCode == KeyEvent.KEYCODE_ENTER))
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    calculate();
                }
                return false;
            }
        });*/

    }

    private void bindPaymentTypeList() {
        BHArrayAdapter<PaymentInfo> adapter = new BHArrayAdapter<PaymentInfo>(
                activity, R.layout.list_sendmoney_summary_by_paymenttype_item,
                mPaymentTypeList) {

            class ViewHolder {
                public TextView txtPaymentType;
                public TextView txtDiffSendAmount;
            }

            @Override
            protected void onViewItem(final int position, View view,
                                      Object holder, PaymentInfo info) {
                // TODO Auto-generated method stub
                ViewHolder vh = (ViewHolder) holder;
                try {
                    String strDiffSendAmount = BHUtilities.numericFormat(info.DiffSendAmount);
                    vh.txtPaymentType.setText(String.format("นำส่ง%s", info.PaymentTypeName));
                    vh.txtDiffSendAmount.setText(strDiffSendAmount);
                } catch (Exception e) {
                    // e.printStackTrace();
                    Log.e(this.getClass().getName(), e.getMessage());
                }
            }
        };
        lvSumPaymentType.setAdapter(adapter);
    }

    private void clearData() {
        edtSendDate.setText("");
        edtSendAmount.setText("0");
    }

    private void setSendMoneyDateVisibility(int code) {
        sendmoneydateview.setVisibility(code);
        sendmoneydateheaderview.setVisibility(code);
    }

    private void calculate() {
        // txtRemain = txtSumPaymentAmount - edtSendAmount
        String strPAYAMTTotal = txtPAYAMTTotal.getText().toString().replaceAll("[,]", "");
        String strSendAmount = edtSendAmount.getText().toString().replaceAll("[,]", "");
        float sumPaymentAmount = (strPAYAMTTotal == null || strPAYAMTTotal.isEmpty()) ? 0 : Float.parseFloat(strPAYAMTTotal);
        float sendAmount = (strSendAmount == null || strSendAmount.isEmpty() || strSendAmount.equals(".")) ? 0 : Float.parseFloat(strSendAmount);
        float remain = sumPaymentAmount - sendAmount;

        txtRemain.setText(BHUtilities.numericFormat(remain));
    }

    private void showNoticeDialogBox(final String title, final String message) {
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity);
        setupAlert.setTitle(title);
        setupAlert.setMessage(message);
        setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // ??
                    }
                });
        setupAlert.show();
    }


    private boolean verifySendData(final String _senddate,
                                   final String _sendamount) {
        boolean ret = false;
        String strSendAmount = _sendamount.replaceAll(",", "");
        float sendAmount = Float.parseFloat(strSendAmount);
        if (_senddate.equals("") || _sendamount.equals("")) {
            String title = "คำเตือน";
            String message = "กรุณาป้อนวันที่ส่งเงินและจำนวนเงินก่อน!";
            showNoticeDialogBox(title, message);
        } else if (sendAmount <= 0) {
            String title = "คำเตือน";
            String message = "กรุณาป้อนจำนวนเงินก่อน!";
            showNoticeDialogBox(title, message);
        } else if ((payment.DiffSendAmount - sendAmount) < 0) {
            String title = "คำเตือน";
            String message = "จำนวนเงินที่กรอกไม่ถูกต้อง!";
            showNoticeDialogBox(title, message);
        } else {

            ret = true;
        }

        return ret;
    }
}
