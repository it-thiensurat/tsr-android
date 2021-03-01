package th.co.thiensurat.fragments.sales;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.MoneyValueFilter;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.LimitController;
import th.co.thiensurat.data.controller.PaymentController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.BankInfo;
import th.co.thiensurat.data.info.ContractCloseAccountInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.LimitInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.TripInfo;
import th.co.thiensurat.fragments.credit.credit.CreditMainFragment;
import th.co.thiensurat.fragments.payment.next.NextPaymentListFragment;
import th.co.thiensurat.views.ViewTitle;

public class SaleFirstPaymentChoiceFragment extends BHFragment {

    private static String STATUS_CODE = "08";
    public static String FRAGMENT_SALE_FIRST_PAYMENT_TAG = "sale_first_payment_tag";

    public enum ProcessType {
        Sale, FirstPayment, NextPayment, SendDocument, ViewCompletedContract, ChangeContract, ImpoundProduct, EditContract, Credit
    }

    public static class Data extends BHParcelable {
        public String refNo;
        public ProcessType processType;
        public float paymentAmount;

        protected ContractInfo contract;
        protected TripInfo trip;
        protected BankInfo bank;

        private Date appointmentDate;
        private List<BankInfo> banks;

        public String assignID;
        public String requestNextPaymentID;

        public Date selectedDate;

        public Data(String refNo, ProcessType processType, float paymentAmount) {
            this.refNo = refNo;
            this.processType = processType;
            this.paymentAmount = paymentAmount;
        }

        public Data(String refNo, ProcessType processType, float paymentAmount, String assignID, Date selectedDate) {
            this.refNo = refNo;
            this.processType = processType;
            this.paymentAmount = paymentAmount;
            this.assignID = assignID;
            this.selectedDate = selectedDate;
        }

        public Data(Data src) {
            this(src.refNo, src.processType, src.paymentAmount);
            this.contract = src.contract;
            this.trip = src.trip;
            this.bank = src.bank;
        }

        public Data() {

        }
    }

    private Data data;

    private int maxPaymentPeriodNumber = 1;

    Calendar calendarAppointment;// = Calendar.getInstance(new Locale("th"));
    Calendar calendarAppointmentsForPartlyPaid;// = Calendar.getInstance(new Locale("th"));

    private String paymentType; // ช่องทางการชำระ/เงินสด/บัตรเครดิต/เช็ค
    private boolean payPartial; // สถานะ จ่ายเต็มบ่างส่วน
    private boolean isPostPone;

    private SalePaymentPeriodInfo summaryNetAmount = null;

    private List<SalePaymentPeriodInfo> salePaymentPeriodInfoList;
    private List<SalePaymentPeriodInfo> salePaymentPeriodInfoListForContractCloseAccount;

    private List<CheckBox> listCBPaymentPeriodNumber = new ArrayList<>();
    private List<CheckBox> tempListCBPaymentPeriodNumber = new ArrayList<>();
    private double defaultAmountPaid = 0.00;  //ค่าเริ่มต้นเป็น 0 สำหรับ Credit
    private boolean isPriceChanged = false;

    @InjectView
    private CheckBox checkBoxPayment; // ชำระทันที
    @InjectView
    private CheckBox checkBoxAppointments; // นัดชำระ
    @InjectView
    private CheckBox checkBoxPartlyPaid; // ชำระบางส่วน
    @InjectView
    private CheckBox checkBoxFullPaid; // ชำระเต็ม
    @InjectView
    private CheckBox checkBoxContractCloseAccount;
    @InjectView
    private CheckBox checkBoxCash; // เงินสด
    @InjectView
    private CheckBox checkBoxCredit; // เครดิต
    @InjectView
    private CheckBox checkBoxCheque; // เช็ค
    @InjectView
    private CheckBox checkBoxQrcode; // คิวอาร์โค้ด
    @InjectView
    private EditText editTextappointment; // วันที่นัดชำระ
    @InjectView
    private EditText editTextPrice; // ราคา
    @InjectView
    private EditText editTextBankSeries; // เลขที่บัตร
    @InjectView
    private EditText editTextAuthorizationCode; // รหัสอนุมัติ
    @InjectView
    private EditText editTextBranch; // สาขาธนาคาร
    @InjectView
    private EditText editTextChequeNumber; // เลขที่เช็ค
    @InjectView
    private EditText editTextDate; // ลงวันที่
    /*    @InjectView
        private ImageButton imageButtonDateCheque;//;วันที่เช็ค*/
    @InjectView
    private ImageButton imageButtonappointment; // วันที่นักชำระ
    @InjectView
    private LinearLayout linearLayoutShowDetail; // รูปแบบชำระทันที
    @InjectView
    private LinearLayout linearLayoutBank; // รูปแบบธนาคาร
    @InjectView
    private LinearLayout linearLayoutCredit; // รูปแบบเครดิต
    @InjectView
    private LinearLayout linearLayoutCheque; // รูปแบบเช็ค
    @InjectView
    private Spinner spinnerBank; // ธนาคาร
    @InjectView
    private LinearLayout layoutQrcode; // คิวอาร์โค้ด
    @InjectView
    private CheckBox checkBoxQRPayment;
    @InjectView
    private Button button_print_receipt;
    @InjectView
    private ImageView imageViewQr;

    @InjectView
    private LinearLayout linearLayoutHeadNumber;
    @InjectView
    private TextView txtNumber1;
    @InjectView
    private TextView txtNumber2;
    @InjectView
    private TextView txtNumber3;
    @InjectView
    private TextView txtNumber4;
    @InjectView
    private TextView txtNumber5;

    @InjectView
    LinearLayout listView, layoutForCredit, listViewContractCloseAccount, viewContractCloseAccountDiscount;
    @InjectView
    private TextView txtTotalOutStanding, txtContractCloseAccountDiscount;
    @InjectView
    private ViewTitle lblTitle;
    @InjectView
    private ImageButton btnChequeDate;

    @InjectView
    private LinearLayout llAppointmentsForPartlyPaid;
    @InjectView
    private EditText editTextAppointmentsForPartlyPaid;
    @InjectView
    private ImageButton imageButtonAppointmentsForPartlyPaid;


    Calendar myCal = Calendar.getInstance();

    @Override
    protected int titleID() {
        if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.Credit) {
            Log.e("zzz","1");
            return R.string.title_main_credit;
        } else if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.NextPayment) {
            Log.e("zzz","2");
            return R.string.title_payment_next;
        } else {
            Log.e("zzz","3");
            return R.string.title_sales;
        }
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_sale_first_payment_choice;
    }

    @Override
    protected int[] processButtons() {
        int[] ret;

        if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.Sale
                || Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.FirstPayment
                || Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.NextPayment
                || Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.Credit)
            ret = new int[]{R.string.button_back, R.string.button_next};
        else
            ret = new int[]{R.string.button_back};

        return ret;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = getData();
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        Log.e("page","8");

        if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.Sale) {
            saveStatusCode();
            txtNumber1.setText("...");
            txtNumber2.setText("8");
            txtNumber3.setText("9");
            txtNumber4.setText("10");
            txtNumber5.setText("11");
            txtNumber2.setBackgroundResource(R.drawable.circle_number_sale_color_red);
        } else {
            linearLayoutHeadNumber.setVisibility(View.GONE);
        }

        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelable(BH_FRAGMENT_DEFAULT_DATA);
        }

        if (data == null) {
            data = new SaleFirstPaymentChoiceFragment.Data();
        }

        data.refNo = BHPreference.RefNo();
        data.processType = Enum.valueOf(ProcessType.class, BHPreference.ProcessType());

        if (data.processType == ProcessType.Credit) {
            lblTitle.setText(R.string.title_next_payment_credit);
        } else if (data.processType == ProcessType.NextPayment) {
            lblTitle.setText(R.string.title_request_next_payment);
        }

//        if (data.banks == null) {
        getRelatedData();
//        } else {
//            bindBanks();
//        }

        initialDateControl();
        initialDateControlOfAppointmentsForPartlyPaid();

        bindCheckBox();
        checkBoxPayment.setChecked(true);

        editTextDate.setText(BHUtilities.dateFormat(new Date(), "dd/MM/yyyy"));
        editTextDate.setKeyListener(null);

        final DatePickerDialog.OnDateSetListener dpl = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCal.set(Calendar.YEAR, year);
                myCal.set(Calendar.MONTH, monthOfYear);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editTextDate.setText(BHUtilities.dateFormat(myCal.getTime(), "dd/MM/yyyy"));
            }
        };

        btnChequeDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DatePickerDialog dpd = new DatePickerDialog(arg0.getContext(),
                        dpl, myCal.get(Calendar.YEAR), myCal
                        .get(Calendar.MONTH), myCal
                        .get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        editTextPrice.setFilters(new InputFilter[]{new MoneyValueFilter()});
        editTextPrice.addTextChangedListener(new CurrencyTextWatcher());

        editTextPrice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {
                    if (checkBoxPartlyPaid.isChecked() && !isPriceChanged) {
                        for (CheckBox cb : listCBPaymentPeriodNumber) {
                            cb.setEnabled(false);
                            cb.setChecked(false);
                        }
                        isPriceChanged = true;
                    }
                }
                return false;
            }
        });
    }

    class CurrencyTextWatcher implements TextWatcher {
        boolean mEditing;

        public CurrencyTextWatcher() {
            mEditing = false;
        }

        public synchronized void afterTextChanged(Editable s) {
            if (!mEditing) {
                mEditing = true;

                String[] str = s.toString().split("\\.");
                if (str.length == 1 || (str.length > 1 && (str[1].toString().length() < 2))) {
                    if (str.length == 1) {
                        if (!s.toString().contains(".")) {
                            s.replace(0, s.length(), BHUtilities.parseNumericFormat(s.toString().replace(",", ""), BHUtilities.DEFAULT_INTEGER_FORMAT));
                        }
                    } else {
                        s.replace(0, s.length(), BHUtilities.parseNumericFormat(s.toString().replace(",", ""), BHUtilities.DEFAULT_FLOAT_FORMAT));
                    }
                } else {
                    s.replace(0, s.length(), BHUtilities.parseNumericFormat(s.toString().replace(",", ""), BHUtilities.DEFAULT_DOUBLE_FORMAT));
                }


                /*** [START] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/
                if (checkBoxPartlyPaid.isChecked()) {
                    float number = s.toString().equals("") ? 0f : Float.valueOf(s.toString().replace(",", ""));
                    if (number < data.contract.PAYAMT) {
                        llAppointmentsForPartlyPaid.setVisibility(View.VISIBLE);
                    } else {
                        //llAppointmentsForPartlyPaid.setVisibility(View.GONE);
                        ClearAppointmentsForPartlyPaid();
                    }
                } else {
                    //llAppointmentsForPartlyPaid.setVisibility(View.GONE);
                    ClearAppointmentsForPartlyPaid();
                }
                /*** [END] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/

                mEditing = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {
                if(start != 0 || (start == 0 && count == 1)){
                    if(checkBoxPartlyPaid.isChecked() && !isPriceChanged){
                        for (CheckBox cb : listCBPaymentPeriodNumber) {
                            cb.setEnabled(false);
                            cb.setChecked(false);
                        }

                        isPriceChanged = true;
                    }
                }
            }

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    @Override
    public void onResume() {
        bindDefault();

        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BH_FRAGMENT_DEFAULT_DATA, data);
        super.onSaveInstanceState(outState);
    }

    private void buttonNext(){
        if (isPostPone) {
            saveAppointmentData();
        } else {
            // savePaymentData();
            // check limit

            new BackgroundProcess(activity) {


                @Override
                protected void before() {
                }

                @Override
                protected void calling() {
                    if (paymentType.equals("Cash")) {
                        float paymentAmount = Float.valueOf(editTextPrice.getText().toString().replace(",", ""));
                        String typeOfLimit = LimitController.LimitType.Moneyonhand.toString();
                        if (BHPreference.sourceSystem().contains(EmployeeController.SourceSystem.Credit.toString())) {
                            typeOfLimit = LimitController.LimitType.MoneyonhandCredit.toString();
                        }
                        LimitInfo limitInfo = new LimitController().getLimitByLimitTypeAndEmployee(typeOfLimit, BHPreference.employeeID());
                        PaymentInfo paymentInfo = new PaymentController().getMoneyOnHand(BHPreference.organizationCode(), paymentType, BHPreference.teamCode(), BHPreference.employeeID());
                        if (((paymentInfo != null ? paymentInfo.MoneyOnHand : 0) + paymentAmount) > (limitInfo != null ? limitInfo.LimitMax : 0)) {
                            String title = "คำเตือน";
                            String msg = "วงเงินของคุณเกินยอดที่กำหนด กรุณาติดต่อเจ้าหน้าที่";
                            showNoticeDialogBox(title, msg);
                            return;
                        }
                    }
                    SaleConfirmBeforeReceiptFragment.Data cbr = new SaleConfirmBeforeReceiptFragment.Data();

                    cbr.contract = data.contract;
                    cbr.refNo = data.refNo;
                    cbr.processType = data.processType;
                    cbr.paymentAmount = data.paymentAmount;
                    cbr.bank = data.bank;
                    cbr.trip = data.trip;
                    cbr.paymentType = paymentType;
                    cbr.payPartial = payPartial;
                    cbr.isPostPone = isPostPone;

                    float PartlyPaidPrice = Float.valueOf(editTextPrice.getText().toString().replace(",", ""));
                    cbr.PartlyPaidPrice = PartlyPaidPrice;


                    /*** [START] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/
                    /*** [START] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป ***/

                    /*if (checkBoxPartlyPaid.isChecked() && !editTextAppointmentsForPartlyPaid.getText().toString().equals("")) {
                        float number = editTextPrice.getText().toString().equals("") ? 0f : Float.valueOf(editTextPrice.getText().toString().replace(",", ""));
                        if (number < data.contract.PAYAMT) {
                            String Date = editTextAppointmentsForPartlyPaid.getText().toString();

                            String strDay = Date.split("/")[0];
                            String strMonth = Date.split("/")[1];
                            String strYear = Date.split("/")[2];

                            Calendar cNewAppointmentDate = Calendar.getInstance(new Locale("th"));
                            cNewAppointmentDate.set(Calendar.YEAR, Integer.parseInt(strYear) - 543);
                            cNewAppointmentDate.set(Calendar.MONTH, Integer.parseInt(strMonth) - 1);
                            cNewAppointmentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDay));
                            cNewAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                            cNewAppointmentDate.set(Calendar.MINUTE, 0);
                            cNewAppointmentDate.set(Calendar.SECOND, 0);
                            cNewAppointmentDate.set(Calendar.MILLISECOND, 0);

                            cbr.dateOfAppointmentsForPartlyPaid = cNewAppointmentDate.getTime();

                        } else {
                            cbr.dateOfAppointmentsForPartlyPaid = null;
                        }
                    } else {
                        cbr.dateOfAppointmentsForPartlyPaid = null;
                    }*/

                    if (checkBoxPartlyPaid.isChecked() && calendarAppointmentsForPartlyPaid != null) {
                        float number = editTextPrice.getText().toString().equals("") ? 0f : Float.valueOf(editTextPrice.getText().toString().replace(",", ""));
                        if (number < data.contract.PAYAMT) {
                            cbr.dateOfAppointmentsForPartlyPaid = calendarAppointmentsForPartlyPaid.getTime();
                        } else {
                            cbr.dateOfAppointmentsForPartlyPaid = null;
                        }
                    } else {
                        cbr.dateOfAppointmentsForPartlyPaid = null;
                    }
                    /*** [END] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป  ***/
                    /*** [END] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/
                    cbr.maxPaymentPeriodNumber = maxPaymentPeriodNumber;

                    if (paymentType.equals("Credit")) {
                        cbr.creditCardNumber = editTextBankSeries.getText().toString();
                        cbr.creditCardApproveCode = editTextAuthorizationCode.getText().toString();
                    } else if (paymentType.equals("Cheque")) {
                        cbr.chequeBankBranch = editTextBranch.getText().toString();
                        cbr.chequeNumber = editTextChequeNumber.getText().toString();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        cbr.chequeDate = formatter.format(myCal.getTime()) + " 00:00:00";
                    }

                    Log.e("p9_ss",TSRController.DocumentGenType.Receipt+","+BHPreference.SubTeamCode()+","+BHPreference.saleCode());

                    cbr.receiptCode = TSRController.getAutoGenerateDocumentID(TSRController.DocumentGenType.Receipt, BHPreference.SubTeamCode(), BHPreference.saleCode());

                    if (data.processType == ProcessType.NextPayment && data.requestNextPaymentID != null) {
                        cbr.requestNextPaymentID = data.requestNextPaymentID;
                    }

                    if (checkBoxContractCloseAccount.isChecked()) {
                /*ContractCloseAccountInfo contractCloseAccount = new ContractCloseAccountInfo();
                contractCloseAccount.SalePaymentPeriodID = data.contract.MinSalePaymentPeriodID;
                contractCloseAccount.OutstandingAmount = data.contract.TotalOutstandingAmount;
                contractCloseAccount.DiscountAmount = data.contract.CloseDiscountAmount;
                contractCloseAccount.NetAmount = data.contract.TotalOutstandingAmount - data.contract.CloseDiscountAmount;
                cbr.contractCloseAccount = contractCloseAccount;*/

                        float TotalOutstandingAmount = salePaymentPeriodInfoListForContractCloseAccount.get(0).TotalOutstandingAmount;
                        float CloseDiscountAmount = salePaymentPeriodInfoListForContractCloseAccount.get(0).CloseDiscountAmount;

                        ContractCloseAccountInfo contractCloseAccount = new ContractCloseAccountInfo();
                        contractCloseAccount.SalePaymentPeriodID = salePaymentPeriodInfoListForContractCloseAccount.get(0).MinSalePaymentPeriodIDForContractCloseAccount;
                        contractCloseAccount.OutstandingAmount = TotalOutstandingAmount;
                        contractCloseAccount.DiscountAmount = CloseDiscountAmount;
                        contractCloseAccount.NetAmount = TotalOutstandingAmount - CloseDiscountAmount;
                        cbr.contractCloseAccount = contractCloseAccount;
                    }

                    cbr.selectedDate = data.selectedDate;

                    SaleConfirmBeforeReceiptFragment fm = BHFragment.newInstance(SaleConfirmBeforeReceiptFragment.class, cbr);
                    showNextView(fm);
                }

                @Override
                protected void after() {
                }
            }.start();
//            if (paymentType.equals("Cash")) {
//                float paymentAmount = Float.valueOf(editTextPrice.getText().toString().replace(",", ""));
//                String typeOfLimit = LimitController.LimitType.Moneyonhand.toString();
//                if (BHPreference.sourceSystem().contains(EmployeeController.SourceSystem.Credit.toString())) {
//                    typeOfLimit = LimitController.LimitType.MoneyonhandCredit.toString();
//                }
//                LimitInfo limitInfo = new LimitController().getLimitByLimitTypeAndEmployee(typeOfLimit, BHPreference.employeeID());
//                PaymentInfo paymentInfo = new PaymentController().getMoneyOnHand(BHPreference.organizationCode(), paymentType, BHPreference.teamCode(), BHPreference.employeeID());
//                if (((paymentInfo != null ? paymentInfo.MoneyOnHand : 0) + paymentAmount) > (limitInfo != null ? limitInfo.LimitMax : 0)) {
//                    String title = "คำเตือน";
//                    String msg = "วงเงินของคุณเกินยอดที่กำหนด กรุณาติดต่อเจ้าหน้าที่";
//                    showNoticeDialogBox(title, msg);
//                    return;
//                }
//            }
//            SaleConfirmBeforeReceiptFragment.Data cbr = new SaleConfirmBeforeReceiptFragment.Data();
//
//            cbr.contract = data.contract;
//            cbr.refNo = data.refNo;
//            cbr.processType = data.processType;
//            cbr.paymentAmount = data.paymentAmount;
//            cbr.bank = data.bank;
//            cbr.trip = data.trip;
//            cbr.paymentType = paymentType;
//            cbr.payPartial = payPartial;
//            cbr.isPostPone = isPostPone;
//
//            float PartlyPaidPrice = Float.valueOf(editTextPrice.getText().toString().replace(",", ""));
//            cbr.PartlyPaidPrice = PartlyPaidPrice;
//
//
//            /*** [START] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/
//            if (checkBoxPartlyPaid.isChecked() && !editTextAppointmentsForPartlyPaid.getText().toString().equals("")) {
//                float number = editTextPrice.getText().toString().equals("") ? 0f : Float.valueOf(editTextPrice.getText().toString().replace(",", ""));
//                if (number < data.contract.PAYAMT) {
//                    String Date = editTextAppointmentsForPartlyPaid.getText().toString();
//
//                    String strDay = Date.split("/")[0];
//                    String strMonth = Date.split("/")[1];
//                    String strYear = Date.split("/")[2];
//
//                    Calendar cNewAppointmentDate = Calendar.getInstance(new Locale("th"));
//                    cNewAppointmentDate.set(Calendar.YEAR, Integer.parseInt(strYear) - 543);
//                    cNewAppointmentDate.set(Calendar.MONTH, Integer.parseInt(strMonth) - 1);
//                    cNewAppointmentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDay));
//                    cNewAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
//                    cNewAppointmentDate.set(Calendar.MINUTE, 0);
//                    cNewAppointmentDate.set(Calendar.SECOND, 0);
//                    cNewAppointmentDate.set(Calendar.MILLISECOND, 0);
//
//                    cbr.dateOfAppointmentsForPartlyPaid = cNewAppointmentDate.getTime();
//
//                } else {
//                    cbr.dateOfAppointmentsForPartlyPaid = null;
//                }
//            } else {
//                cbr.dateOfAppointmentsForPartlyPaid = null;
//            }
//            /*** [END] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/
//            cbr.maxPaymentPeriodNumber = maxPaymentPeriodNumber;
//
//            if (paymentType.equals("Credit")) {
//                cbr.creditCardNumber = editTextBankSeries.getText().toString();
//                cbr.creditCardApproveCode = editTextAuthorizationCode.getText().toString();
//            } else if (paymentType.equals("Cheque")) {
//                cbr.chequeBankBranch = editTextBranch.getText().toString();
//                cbr.chequeNumber = editTextChequeNumber.getText().toString();
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                cbr.chequeDate = formatter.format(myCal.getTime()) + " 00:00:00";
//            }
//
//            cbr.receiptCode = TSRController.getAutoGenerateDocumentID(TSRController.DocumentGenType.Receipt, BHPreference.SubTeamCode(), BHPreference.saleCode());
//
//            if (data.processType == ProcessType.NextPayment && data.requestNextPaymentID != null) {
//                cbr.requestNextPaymentID = data.requestNextPaymentID;
//            }
//
//            if (checkBoxContractCloseAccount.isChecked()) {
//                /*ContractCloseAccountInfo contractCloseAccount = new ContractCloseAccountInfo();
//                contractCloseAccount.SalePaymentPeriodID = data.contract.MinSalePaymentPeriodID;
//                contractCloseAccount.OutstandingAmount = data.contract.TotalOutstandingAmount;
//                contractCloseAccount.DiscountAmount = data.contract.CloseDiscountAmount;
//                contractCloseAccount.NetAmount = data.contract.TotalOutstandingAmount - data.contract.CloseDiscountAmount;
//                cbr.contractCloseAccount = contractCloseAccount;*/
//
//                float TotalOutstandingAmount = salePaymentPeriodInfoListForContractCloseAccount.get(0).TotalOutstandingAmount;
//                float CloseDiscountAmount = salePaymentPeriodInfoListForContractCloseAccount.get(0).CloseDiscountAmount;
//
//                ContractCloseAccountInfo contractCloseAccount = new ContractCloseAccountInfo();
//                contractCloseAccount.SalePaymentPeriodID = salePaymentPeriodInfoListForContractCloseAccount.get(0).MinSalePaymentPeriodIDForContractCloseAccount;
//                contractCloseAccount.OutstandingAmount = TotalOutstandingAmount;
//                contractCloseAccount.DiscountAmount = CloseDiscountAmount;
//                contractCloseAccount.NetAmount = TotalOutstandingAmount - CloseDiscountAmount;
//                cbr.contractCloseAccount = contractCloseAccount;
//            }
//
//            cbr.selectedDate = data.selectedDate;
//
//            SaleConfirmBeforeReceiptFragment fm = BHFragment.newInstance(SaleConfirmBeforeReceiptFragment.class, cbr);
//            showNextView(fm);
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_next:
                if (validate()) {

                    //นัดชำระ
                    if(checkBoxAppointments.isChecked() && maxPaymentPeriodNumber != 1){
                        /*** [START] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป ***/

                        /*String Date = editTextappointment.getText().toString();
                        SalePaymentPeriodInfo periods = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoAndPaymentPeriodNumber(data.refNo, maxPaymentPeriodNumber + 1);

                        if (periods != null) {
                            String strDay = Date.split("/")[0];
                            String strMonth = Date.split("/")[1];
                            String strYear = Date.split("/")[2];

                            Calendar cPaymentAppointmentDate = Calendar.getInstance(new Locale("th"));
                            cPaymentAppointmentDate.setTime(periods.PaymentAppointmentDate);
                            cPaymentAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                            cPaymentAppointmentDate.set(Calendar.MINUTE, 0);
                            cPaymentAppointmentDate.set(Calendar.SECOND, 0);
                            cPaymentAppointmentDate.set(Calendar.MILLISECOND, 0);

                            Calendar cNewAppointmentDate = Calendar.getInstance(new Locale("th"));
                            cNewAppointmentDate.set(Calendar.YEAR, Integer.parseInt(strYear) - 543);
                            cNewAppointmentDate.set(Calendar.MONTH, Integer.parseInt(strMonth) - 1);
                            cNewAppointmentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDay));
                            cNewAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                            cNewAppointmentDate.set(Calendar.MINUTE, 0);
                            cNewAppointmentDate.set(Calendar.SECOND, 0);
                            cNewAppointmentDate.set(Calendar.MILLISECOND, 0);

                            if (cNewAppointmentDate.before(cPaymentAppointmentDate)) {
                                buttonNext();
                            } else {
                                Builder setupAlert;
                                setupAlert = new Builder(activity)
                                        .setTitle("แจ้งเตือน")
                                        .setMessage("วันที่เลื่อนนัดเกินวันที่นัดชำระของงวดถัดไป ต้องการทำต่อหรือไม่")
                                        .setCancelable(false)
                                        .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                buttonNext();
                                                dialog.cancel();
                                            }
                                        })
                                        .setPositiveButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.cancel();
                                            }
                                        });
                                setupAlert.show();
                            }
                        } else {
                            buttonNext();
                        }*/

                        SalePaymentPeriodInfo periods = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoAndPaymentPeriodNumber(data.refNo, maxPaymentPeriodNumber + 1);

                        if (periods != null) {

                            Calendar cPaymentAppointmentDate = Calendar.getInstance();
                            cPaymentAppointmentDate.setTime(periods.PaymentAppointmentDate);
                            cPaymentAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                            cPaymentAppointmentDate.set(Calendar.MINUTE, 0);
                            cPaymentAppointmentDate.set(Calendar.SECOND, 0);
                            cPaymentAppointmentDate.set(Calendar.MILLISECOND, 0);

                            if (calendarAppointment.getTimeInMillis() <= cPaymentAppointmentDate.getTimeInMillis()) {
                                buttonNext();
                            } else {
                                Builder setupAlert;
                                setupAlert = new Builder(activity)
                                        .setTitle("แจ้งเตือน")
                                        .setMessage("วันที่เลื่อนนัดเกินวันที่นัดชำระของงวดถัดไป ต้องการทำต่อหรือไม่")
                                        .setCancelable(false)
                                        .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                buttonNext();
                                                dialog.cancel();
                                            }
                                        })
                                        .setPositiveButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.cancel();
                                            }
                                        });
                                setupAlert.show();
                            }
                        } else {
                            buttonNext();
                        }

                        /*** [END] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป  ***/
                    } else if(checkBoxPartlyPaid.isChecked() && maxPaymentPeriodNumber != 1){ //นัดชำระบางส่วน

                        float number = editTextPrice.getText().toString().equals("") ? 0f : Float.valueOf(editTextPrice.getText().toString().replace(",", ""));

                        /*** [START] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป ***/

                        /*if (number < data.contract.PAYAMT && !editTextAppointmentsForPartlyPaid.getText().toString().equals("")) {
                            String Date = editTextAppointmentsForPartlyPaid.getText().toString();
                            SalePaymentPeriodInfo periods = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoAndPaymentPeriodNumber(data.refNo, maxPaymentPeriodNumber + 1);

                            if (periods != null) {
                                String strDay = Date.split("/")[0];
                                String strMonth = Date.split("/")[1];
                                String strYear = Date.split("/")[2];

                                Calendar cPaymentAppointmentDate = Calendar.getInstance(new Locale("th"));
                                cPaymentAppointmentDate.setTime(periods.PaymentAppointmentDate);
                                cPaymentAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                                cPaymentAppointmentDate.set(Calendar.MINUTE, 0);
                                cPaymentAppointmentDate.set(Calendar.SECOND, 0);
                                cPaymentAppointmentDate.set(Calendar.MILLISECOND, 0);

                                Calendar cNewAppointmentDate = Calendar.getInstance(new Locale("th"));
                                cNewAppointmentDate.set(Calendar.YEAR, Integer.parseInt(strYear) - 543);
                                cNewAppointmentDate.set(Calendar.MONTH, Integer.parseInt(strMonth) - 1);
                                cNewAppointmentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDay));
                                cNewAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                                cNewAppointmentDate.set(Calendar.MINUTE, 0);
                                cNewAppointmentDate.set(Calendar.SECOND, 0);
                                cNewAppointmentDate.set(Calendar.MILLISECOND, 0);

                                if (cNewAppointmentDate.before(cPaymentAppointmentDate)) {
                                    buttonNext();
                                } else {
                                    Builder setupAlert;
                                    setupAlert = new Builder(activity)
                                            .setTitle("แจ้งเตือน")
                                            .setMessage("วันที่เลื่อนนัดชำระบางส่วนเกินวันที่นัดชำระของงวดถัดไป ต้องการทำต่อหรือไม่")
                                            .setCancelable(false)
                                            .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    buttonNext();
                                                    dialog.cancel();
                                                }
                                            })
                                            .setPositiveButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    dialog.cancel();
                                                }
                                            });
                                    setupAlert.show();
                                }
                            } else {
                                buttonNext();
                            }
                        } else {
                            buttonNext();
                        }*/

                        if (number < data.contract.PAYAMT && calendarAppointmentsForPartlyPaid != null) {
                            SalePaymentPeriodInfo periods = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoAndPaymentPeriodNumber(data.refNo, maxPaymentPeriodNumber + 1);

                            if (periods != null) {
                                Calendar cPaymentAppointmentDate = Calendar.getInstance();
                                cPaymentAppointmentDate.setTime(periods.PaymentAppointmentDate);
                                cPaymentAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                                cPaymentAppointmentDate.set(Calendar.MINUTE, 0);
                                cPaymentAppointmentDate.set(Calendar.SECOND, 0);
                                cPaymentAppointmentDate.set(Calendar.MILLISECOND, 0);

                                if (calendarAppointmentsForPartlyPaid.getTimeInMillis() <= cPaymentAppointmentDate.getTimeInMillis()) {
                                    buttonNext();
                                } else {
                                    Builder setupAlert;
                                    setupAlert = new Builder(activity)
                                            .setTitle("แจ้งเตือน")
                                            .setMessage("วันที่เลื่อนนัดชำระบางส่วนเกินวันที่นัดชำระของงวดถัดไป ต้องการทำต่อหรือไม่")
                                            .setCancelable(false)
                                            .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    buttonNext();
                                                    dialog.cancel();
                                                }
                                            })
                                            .setPositiveButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    dialog.cancel();
                                                }
                                            });
                                    setupAlert.show();
                                }
                            } else {
                                buttonNext();
                            }
                        } else {
                            buttonNext();
                        }

                        /*** [END] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป  ***/
                    } else {
                        buttonNext();
                    }
                }
                break;
            case R.string.button_back:
                showLastView();
            default:
                break;
        }
    }

    private void saveStatusCode() {
        if (BHPreference.RefNo() != null) {
            TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
        } else {
            showMessage("ref is null");

        }
    }

    private void bindBanks() {
        if (data.banks != null) {
            List<String> bank = new ArrayList<>();
            for (BankInfo item : data.banks) {
                bank.add(item.BankName);
            }

            BHSpinnerAdapter<String> arrayBank = new BHSpinnerAdapter<String>(activity, bank);
            spinnerBank.setAdapter(arrayBank);
            spinnerBank.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    data.bank = data.banks.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void initialDateControl() {
        final DatePickerDialog.OnDateSetListener dpl = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (calendarAppointment == null) {
                    calendarAppointment =  Calendar.getInstance();
                }

                calendarAppointment.set(Calendar.YEAR, year);
                calendarAppointment.set(Calendar.MONTH, monthOfYear);
                calendarAppointment.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarAppointment.set(Calendar.HOUR_OF_DAY, 0);
                calendarAppointment.set(Calendar.MINUTE, 0);
                calendarAppointment.set(Calendar.SECOND, 0);
                calendarAppointment.set(Calendar.MILLISECOND, 0);
                Calendar todayWithoutTime = Calendar.getInstance();
                todayWithoutTime.set(Calendar.HOUR_OF_DAY, 0);
                todayWithoutTime.set(Calendar.MINUTE, 0);
                todayWithoutTime.set(Calendar.SECOND, 0);
                todayWithoutTime.set(Calendar.MILLISECOND, 0);
                if (view.isShown()) {
                    if (calendarAppointment.getTime().before(todayWithoutTime.getTime())) {
                        String title = "คำเตือน";
                        String message = "ห้ามเลือกวันที่นัดชำระย้อนหลัง!";
                        showNoticeDialogBox(title, message);
                    } else {
                        editTextappointment.setText(BHUtilities.dateFormat(calendarAppointment.getTime(), "dd/MM/yyyy"));
                    }
                }
            }
        };

        imageButtonappointment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (data.appointmentDate == null) {
                    data.appointmentDate = new Date();
                }
                Calendar calendar = Calendar.getInstance(new Locale("th"));
                calendar.setTime(data.appointmentDate);
                DatePickerDialog dpd = new DatePickerDialog(activity, dpl, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
    }

    private void initialDateControlOfAppointmentsForPartlyPaid() {
        final DatePickerDialog.OnDateSetListener dp2 = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (calendarAppointmentsForPartlyPaid == null) {
                    calendarAppointmentsForPartlyPaid = Calendar.getInstance();
                }
                calendarAppointmentsForPartlyPaid.set(Calendar.YEAR, year);
                calendarAppointmentsForPartlyPaid.set(Calendar.MONTH, monthOfYear);
                calendarAppointmentsForPartlyPaid.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarAppointmentsForPartlyPaid.set(Calendar.HOUR_OF_DAY, 0);
                calendarAppointmentsForPartlyPaid.set(Calendar.MINUTE, 0);
                calendarAppointmentsForPartlyPaid.set(Calendar.SECOND, 0);
                calendarAppointmentsForPartlyPaid.set(Calendar.MILLISECOND, 0);

                Calendar todayWithoutTime = Calendar.getInstance();
                todayWithoutTime.set(Calendar.HOUR_OF_DAY, 0);
                todayWithoutTime.set(Calendar.MINUTE, 0);
                todayWithoutTime.set(Calendar.SECOND, 0);
                todayWithoutTime.set(Calendar.MILLISECOND, 0);
                if (view.isShown()) {
                    if (calendarAppointmentsForPartlyPaid.getTime().before(todayWithoutTime.getTime())) {
                        String title = "คำเตือน";
                        String message = "ห้ามเลือกวันที่นัดชำระบางส่วนย้อนหลัง!";
                        showNoticeDialogBox(title, message);
                    } else {
                        editTextAppointmentsForPartlyPaid.setText(BHUtilities.dateFormat(calendarAppointmentsForPartlyPaid.getTime(), "dd/MM/yyyy"));
                    }
                }
            }
        };

        imageButtonAppointmentsForPartlyPaid.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (data.appointmentDate == null) {
                    data.appointmentDate = new Date();
                }
                Calendar calendar = Calendar.getInstance(new Locale("th"));
                calendar.setTime(data.appointmentDate);
                DatePickerDialog dpd = new DatePickerDialog(activity, dp2, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
    }

    private void bindDefault() {
        boolean isPayment = checkBoxPayment.isChecked();
        boolean isPostpone = checkBoxAppointments.isChecked();
        boolean isFullPaid = checkBoxFullPaid.isChecked();
        boolean isPartialPaid = checkBoxPartlyPaid.isChecked();
        boolean isCash = checkBoxCash.isChecked();
        boolean isCredit = checkBoxCredit.isChecked();
        boolean isCheque = checkBoxCheque.isChecked();
        boolean isQRCode = checkBoxQrcode.isChecked();
        boolean isCloseAccount = checkBoxContractCloseAccount.isChecked();

        editTextappointment.setVisibility(isPostpone ? View.VISIBLE : View.GONE);
        imageButtonappointment.setVisibility(isPostpone ? View.VISIBLE : View.GONE);

        linearLayoutShowDetail.setVisibility(isPayment ? View.VISIBLE : View.GONE);
        listViewContractCloseAccount.setVisibility(isCloseAccount ? View.VISIBLE : View.GONE);
        viewContractCloseAccountDiscount.setVisibility(isCloseAccount ? View.VISIBLE : View.GONE);
        linearLayoutBank.setVisibility(isCredit || isCheque ? View.VISIBLE : View.GONE);
        linearLayoutCredit.setVisibility(isCredit ? View.VISIBLE : View.GONE);
        linearLayoutCheque.setVisibility(isCheque ? View.VISIBLE : View.GONE);
//        layoutQrcode.setVisibility(isQRCode ? View.VISIBLE : View.GONE);

        editTextappointment.setEnabled(false);
        checkBoxFullPaid.setEnabled(isPayment && !isCloseAccount);
        checkBoxPartlyPaid.setEnabled(isPayment && !isCloseAccount);
        checkBoxCash.setEnabled(isPayment && (isFullPaid || isPartialPaid));
        checkBoxCredit.setEnabled(isPayment && isFullPaid);
        checkBoxCheque.setEnabled(isPayment && isFullPaid);
        checkBoxQrcode.setEnabled(isPayment && (isFullPaid || isPartialPaid));
        editTextPrice.setEnabled(isPartialPaid && !isCloseAccount);

        showButton();
    }

    private void bindCheckBox() {
        checkBoxPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxPayment.isChecked()) {
                    checkBoxAppointments.setChecked(false);
                    checkBoxQRPayment.setChecked(false);
                    ClearAppointments();
                }
                bindDefault();
            }
        });

        checkBoxAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxAppointments.isChecked()) {
                    checkBoxPayment.setChecked(false);
                    checkBoxFullPaid.setChecked(false);
                    checkBoxPartlyPaid.setChecked(false);
                    checkBoxCash.setChecked(false);
                    checkBoxCredit.setChecked(false);
                    checkBoxCheque.setChecked(false);
                    checkBoxQrcode.setChecked(false);
                    checkBoxContractCloseAccount.setChecked(false);
                    checkBoxQRPayment.setChecked(false);

                    for (CheckBox cb : listCBPaymentPeriodNumber){
                        cb.setEnabled(false);
                        cb.setChecked(false);
                    }
                }
                bindDefault();
            }
        });

        checkBoxFullPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxFullPaid.isChecked()) {
                    checkBoxQRPayment.setChecked(false);
                    payPartial = false;
                    checkBoxPartlyPaid.setChecked(false);
                    editTextPrice.setText(BHUtilities.numericFormat(data.contract.PAYAMT));
                } else {
                    if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {
                        editTextPrice.setText(BHUtilities.numericFormat(defaultAmountPaid));
                    }
                }

                if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {
                    for (CheckBox cb : listCBPaymentPeriodNumber){
                        cb.setEnabled(false);
                        cb.setChecked(checkBoxFullPaid.isChecked());
                    }
                    isPriceChanged = false;
                }

                bindDefault();
            }
        });

        checkBoxPartlyPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxPartlyPaid.isChecked()) {
                    payPartial = true;
                    checkBoxFullPaid.setChecked(false);
                    checkBoxCheque.setChecked(false);
                    checkBoxCredit.setChecked(false);
                    checkBoxQRPayment.setChecked(false);
                }

                if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {
                    editTextPrice.setText(BHUtilities.numericFormat(defaultAmountPaid));

                    for (CheckBox cb : listCBPaymentPeriodNumber){
                        cb.setEnabled(checkBoxPartlyPaid.isChecked());
                        cb.setChecked(false);
                    }
                    isPriceChanged = false;
                }

                bindDefault();
            }
        });


        /*** [START] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/
        checkBoxPartlyPaid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    float number = editTextPrice.getText().toString().equals("") ? 0f : Float.valueOf(editTextPrice.getText().toString().replace(",", ""));
                    if (number < data.contract.PAYAMT) {
                        llAppointmentsForPartlyPaid.setVisibility(View.VISIBLE);
                    } else {
                        /*editTextAppointmentsForPartlyPaid.setText("");
                        llAppointmentsForPartlyPaid.setVisibility(View.GONE);*/
                        ClearAppointmentsForPartlyPaid();
                    }
                } else {
                    /*editTextAppointmentsForPartlyPaid.setText("");
                    llAppointmentsForPartlyPaid.setVisibility(View.GONE);*/
                    ClearAppointmentsForPartlyPaid();
                }
            }
        });
        /*** [END] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/


        checkBoxCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxCash.isChecked()) {
                    paymentType = "Cash";
                    checkBoxCredit.setChecked(false);
                    checkBoxCheque.setChecked(false);
                    checkBoxQrcode.setChecked(false);
                    checkBoxQRPayment.setChecked(false);
                }
                bindDefault();
            }
        });

        checkBoxCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxCredit.isChecked()) {
                    paymentType = "Credit";
                    checkBoxCash.setChecked(false);
                    checkBoxCheque.setChecked(false);
                    checkBoxQrcode.setChecked(false);
                    checkBoxQRPayment.setChecked(false);
                }
                bindDefault();
            }
        });

        checkBoxCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxCheque.isChecked()) {
                    paymentType = "Cheque";
                    checkBoxCash.setChecked(false);
                    checkBoxCredit.setChecked(false);
                    checkBoxQrcode.setChecked(false);
                    checkBoxQRPayment.setChecked(false);
                }
                bindDefault();
            }
        });

        checkBoxQrcode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxQrcode.isChecked()) {
                    paymentType = "Qrcode";
                    checkBoxCash.setChecked(false);
                    checkBoxCredit.setChecked(false);
                    checkBoxCheque.setChecked(false);
                    checkBoxQRPayment.setChecked(false);
                }
                bindDefault();
            }
        });

        checkBoxQRPayment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxQRPayment.isChecked()) {
                    paymentType = "Qrcode";
                    checkBoxCash.setChecked(false);
                    checkBoxCredit.setChecked(false);
                    checkBoxCheque.setChecked(false);
                    checkBoxPayment.setChecked(false);
                    checkBoxAppointments.setChecked(false);
                    linearLayoutShowDetail.setVisibility(View.GONE);

                    hideButton();
                }
            }
        });

//        checkBoxCheque.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkBoxCheque.isChecked()) {
//                    paymentType = "Cheque";
//                    checkBoxCash.setChecked(false);
//                    checkBoxCredit.setChecked(false);
//                }
//                bindDefault();
//            }
//        });

        checkBoxContractCloseAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxContractCloseAccount.isChecked()) {
                    checkBoxFullPaid.setChecked(true);
                    checkBoxPartlyPaid.setChecked(false);

                    checkBoxFullPaid.setEnabled(false);
                    checkBoxPartlyPaid.setEnabled(false);

                    listView.setVisibility(View.GONE);
                    listViewContractCloseAccount.setVisibility(View.VISIBLE);
                    viewContractCloseAccountDiscount.setVisibility(View.VISIBLE);

                    float TotalOutstandingAmount = data.contract.TotalOutstandingAmount -  salePaymentPeriodInfoListForContractCloseAccount.get(0).CloseDiscountAmount;

                    if (txtTotalOutStanding.getVisibility() == View.VISIBLE) {
                        //txtTotalOutStanding.setText(BHUtilities.numericFormat(data.contract.TotalOutstandingAmount - data.contract.CloseDiscountAmount));
                        txtTotalOutStanding.setText(BHUtilities.numericFormat(TotalOutstandingAmount));
                    }

                    //editTextPrice.setText(BHUtilities.numericFormat(data.contract.TotalOutstandingAmount - data.contract.CloseDiscountAmount));
                    editTextPrice.setText(BHUtilities.numericFormat(TotalOutstandingAmount));
                    editTextPrice.setEnabled(false);
                } else {
                    checkBoxPartlyPaid.setEnabled(true);

                    listView.setVisibility(View.VISIBLE);
                    listViewContractCloseAccount.setVisibility(View.GONE);
                    viewContractCloseAccountDiscount.setVisibility(View.GONE);

                    editTextPrice.setEnabled(false);
                    if (txtTotalOutStanding.getVisibility() == View.VISIBLE) {
                        txtTotalOutStanding.setText(BHUtilities.numericFormat(data.contract.PAYAMT));
                    }

                    editTextPrice.setText(BHUtilities.numericFormat(data.contract.PAYAMT));
                    editTextPrice.setEnabled(false);
                }

                for (CheckBox cb : listCBPaymentPeriodNumber) {
                    cb.setEnabled(false);
                    cb.setChecked(true);
                }
                isPriceChanged = false;
                bindDefault();
            }
        });
    }

    private void hideButton() {
        button_print_receipt.setVisibility(View.VISIBLE);
        List<Integer> listId = new ArrayList<Integer>();
        listId.add(R.string.button_next);
        listId.add(R.string.button_back);
        activity.setViewProcessButtons(listId, View.GONE);

        button_print_receipt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleReceiptPayment_Qr.Data dataReceiptID = new SaleReceiptPayment_Qr.Data();
                dataReceiptID.contract = data.contract;
                dataReceiptID.selectedDate = data.selectedDate;
                dataReceiptID.paymentType = "Qrcode";
                SaleReceiptPayment_Qr fmReceipt = BHFragment.newInstance(SaleReceiptPayment_Qr.class, dataReceiptID);
                fmReceipt.forcePrint = true;
                showNextView(fmReceipt);
            }
        });
    }

    private void showButton() {
        button_print_receipt.setVisibility(View.GONE);
        List<Integer> listId = new ArrayList<Integer>();
        listId.add(R.string.button_next);
        listId.add(R.string.button_back);
        activity.setViewProcessButtons(listId, View.VISIBLE);
    }

    private void getRelatedData() {
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                if (data.banks == null) {
                    data.banks = getBank();
                }

                if (data.contract == null) {
                    if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {
                        data.contract = new ContractController().getContractByRefNoForNextPayment(BHPreference.organizationCode(), BHPreference.RefNo());
                    } else {
                        data.contract = getContractByRefNoByPaymentPeriodNumberNotTeam(BHPreference.organizationCode(), BHPreference.RefNo(), "1");
                    }

                    if (data.processType == ProcessType.Sale || data.processType == ProcessType.FirstPayment) {
                        data.contract.LastUpdateBy = BHPreference.employeeID();
                        data.contract.LastUpdateDate = new Date();
                        data.paymentAmount = data.contract.OutstandingAmount;
                        data.contract.PAYAMT = data.contract.OutstandingAmount;
                        updateContract(data.contract, true);
                    }
                }

                if (data.trip == null) {
                    data.trip = getCurrentTrip();
                }

                summaryNetAmount = getSummaryNetAmount(BHPreference.RefNo());
                if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {

                    if (salePaymentPeriodInfoList == null) {
                        if (data.processType == ProcessType.Credit && data.assignID != null) {
                            salePaymentPeriodInfoList = new SalePaymentPeriodController().getSalePaymentPeriodOutStandingByAssignID(data.assignID, BHPreference.RefNo());
                        } else if (data.processType == ProcessType.NextPayment && data.refNo != null) {
                            salePaymentPeriodInfoList = new SalePaymentPeriodController().getSalePaymentPeriodOutStandingByRefNo(data.refNo);
                        }
                    }

                    if (salePaymentPeriodInfoListForContractCloseAccount == null) {
                        salePaymentPeriodInfoListForContractCloseAccount = new SalePaymentPeriodController().getSalePaymentPeriodContractCloseAccountForOverdueByRefNo(BHPreference.RefNo(), BHPreference.organizationCode());
                    }
                }
            }

            @Override
            protected void after() {
                tempListCBPaymentPeriodNumber = new ArrayList<>();
                if(checkBoxPartlyPaid.isChecked()){
                    tempListCBPaymentPeriodNumber.addAll(listCBPaymentPeriodNumber);
                }
                listCBPaymentPeriodNumber = new ArrayList<>();

                if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {

                    if (salePaymentPeriodInfoList != null && salePaymentPeriodInfoList.size() > 0) {
                        layoutForCredit.setVisibility(View.VISIBLE);
                        float sumOutStandingAmount = 0;
                        for (SalePaymentPeriodInfo s : salePaymentPeriodInfoList) {
                            sumOutStandingAmount += s.OutstandingAmount;
                            String item1 = String.format("งวดที่ %d", s.PaymentPeriodNumber);
                            String item2 = BHUtilities.numericFormat(s.OutstandingAmount);
                            listView.addView(createItemListView(item1, item2, s));
                            maxPaymentPeriodNumber = s.PaymentPeriodNumber;
                        }
                        data.contract.PAYAMT = sumOutStandingAmount;
                        txtTotalOutStanding.setText(BHUtilities.numericFormat(data.contract.PAYAMT));
                    }

                /*if ((data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment)
                        && (data.contract != null && data.contract.MinPaymentPeriodNumber > 0 && data.contract.HoldSalePaymentPeriod == 0)) {
                    layoutForCredit.setVisibility(View.VISIBLE);
                    listViewContractCloseAccount.setVisibility(View.VISIBLE);
                    viewContractCloseAccountDiscount.setVisibility(View.VISIBLE);
                    if (data.contract.MinPaymentPeriodNumber > 2) {
                        if (data.contract.MinPaymentPeriodNumber == data.contract.MODE) {
                            // print minPaymentPeriodNumber
                            String item1 = String.format("งวดที่ %d", data.contract.MinPaymentPeriodNumber);
                            String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                            listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                        } else {
                            if (data.contract.MinOutStandingAmount != data.contract.NextNetAmount) {
                                //print minPaymentPeriodNumber
                                String item1 = String.format("งวดที่ %d", data.contract.MinPaymentPeriodNumber);
                                String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                                listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                                //print nextPaymentPeriodNumber == MODE ? nextPaymentPeriodNumber : nextPaymentPeriodNumber - MODE
                                String item3 = data.contract.NextPaymentPeriodNumber == data.contract.MODE ?
                                        String.format("งวดที่ %d", data.contract.NextPaymentPeriodNumber) : String.format("งวดที่ %d - %d งวดละ", data.contract.NextPaymentPeriodNumber, data.contract.MODE);
                                String item4 = BHUtilities.numericFormat(data.contract.NextNetAmount);
                                listViewContractCloseAccount.addView(createItemListView(item3, item4, null));
                            } else {
                                // print minPaymentPeriodNumber - MODE
                                String item1 = String.format("งวดที่ %d - %d งวดละ", data.contract.MinPaymentPeriodNumber, data.contract.MODE);
                                String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                                listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                            }
                        }
                    } else if (data.contract.MinPaymentPeriodNumber == 2) {
                        if (data.contract.MODE == 2) {
                            // print 2
                            String item1 = String.format("งวดที่ %d ", data.contract.MinPaymentPeriodNumber);
                            String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                            listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                        } else if (data.contract.MODE > 2) {
                            if (data.contract.MinOutStandingAmount != data.contract.ThirdNetAmount) {
                                // print 2
                                String item1 = String.format("งวดที่ %d ", data.contract.MinPaymentPeriodNumber);
                                String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                                listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                                // print MODE == 3 ? 3 : 3 - MODE
                                String item3 = data.contract.NextPaymentPeriodNumber == data.contract.MODE ?
                                        String.format("งวดที่ %d", data.contract.NextPaymentPeriodNumber) : String.format("งวดที่ %d - %d งวดละ", data.contract.NextPaymentPeriodNumber, data.contract.MODE);
                                String item4 = BHUtilities.numericFormat(data.contract.NextNetAmount);
                                listViewContractCloseAccount.addView(createItemListView(item3, item4, null));
                            } else {
                                // print 2 - MODE
                                String item1 = String.format("งวดที่ %d - %d งวดละ", data.contract.MinPaymentPeriodNumber, data.contract.MODE);
                                String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                                listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                            }
                        }
                    } else if (data.contract.MinPaymentPeriodNumber == 1) {
                        if (data.contract.MODE == 1) {
                            // print 1
                            String item1 = String.format("งวดที่ %d ", data.contract.MinPaymentPeriodNumber);
                            String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                            listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                        } else if (data.contract.MODE == 2) {
                            if (data.contract.MinOutStandingAmount != data.contract.SecondNetAmount) {
                                // print 1
                                String item1 = String.format("งวดที่ %d ", data.contract.MinPaymentPeriodNumber);
                                String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                                listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                                // print 2
                                String item3 = String.format("งวดที่ %d ", data.contract.NextPaymentPeriodNumber);
                                String item4 = BHUtilities.numericFormat(data.contract.NextNetAmount);
                                listViewContractCloseAccount.addView(createItemListView(item3, item4, null));
                            } else {
                                // print 1 - 2
                                String item1 = String.format("งวดที่ %d - %d งวดละ", data.contract.MinPaymentPeriodNumber, data.contract.MODE);
                                String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                                listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                            }
                        } else if (data.contract.MODE > 2) {
                            if (data.contract.MinOutStandingAmount != data.contract.SecondNetAmount) {
                                // print 1
                                String item1 = String.format("งวดที่ %d ", data.contract.MinPaymentPeriodNumber);
                                String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                                listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                                if (data.contract.SecondNetAmount != data.contract.ThirdNetAmount) {
                                    // print 2
                                    String item3 = String.format("งวดที่ %d ", data.contract.NextPaymentPeriodNumber);
                                    String item4 = BHUtilities.numericFormat(data.contract.NextNetAmount);
                                    listViewContractCloseAccount.addView(createItemListView(item3, item4, null));
                                    // print MODE == 3 ? 3 : 3 - MODE
                                    String item5 = (data.contract.NextPaymentPeriodNumber + 1) == data.contract.MODE ?
                                            String.format("งวดที่ %d", data.contract.NextPaymentPeriodNumber + 1) : String.format("งวดที่ %d - %d งวดละ", data.contract.NextPaymentPeriodNumber + 1, data.contract.MODE);
                                    String item6 = BHUtilities.numericFormat(data.contract.ThirdNetAmount);
                                    listViewContractCloseAccount.addView(createItemListView(item5, item6, null));
                                } else {
                                    // print 2 - MODE
                                    String item3 = String.format("งวดที่ %d - %d งวดละ", data.contract.NextPaymentPeriodNumber, data.contract.MODE);
                                    String item4 = BHUtilities.numericFormat(data.contract.NextNetAmount);
                                    listViewContractCloseAccount.addView(createItemListView(item3, item4, null));
                                }
                            } else if (data.contract.SecondNetAmount != data.contract.ThirdNetAmount) {
                                // print 1
                                String item1 = String.format("งวดที่ %d ", data.contract.MinPaymentPeriodNumber);
                                String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                                listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                                // print 2
                                String item3 = String.format("งวดที่ %d ", data.contract.NextPaymentPeriodNumber);
                                String item4 = BHUtilities.numericFormat(data.contract.NextNetAmount);
                                listViewContractCloseAccount.addView(createItemListView(item3, item4, null));
                                // print MODE == 3 ? 3 : 3 - MODE
                                String item5 = (data.contract.NextPaymentPeriodNumber + 1) == data.contract.MODE ?
                                        String.format("งวดที่ %d", data.contract.NextPaymentPeriodNumber + 1) : String.format("งวดที่ %d - %d งวดละ", data.contract.NextPaymentPeriodNumber + 1, data.contract.MODE);
                                String item6 = BHUtilities.numericFormat(data.contract.ThirdNetAmount);
                                listViewContractCloseAccount.addView(createItemListView(item5, item6, null));
                            } else {
                                // print 1 - MODE
                                String item1 = String.format("งวดที่ %d - %d งวดละ", data.contract.MinPaymentPeriodNumber, data.contract.MODE);
                                String item2 = BHUtilities.numericFormat(data.contract.MinOutStandingAmount);
                                listViewContractCloseAccount.addView(createItemListView(item1, item2, null));
                            }
                        }
                    }
                    txtContractCloseAccountDiscount.setText(BHUtilities.numericFormat(data.contract.CloseDiscountAmount));
                    listView.setVisibility(checkBoxContractCloseAccount.isChecked() ? View.GONE : View.VISIBLE);
                    listViewContractCloseAccount.setVisibility(checkBoxContractCloseAccount.isChecked() ? View.VISIBLE : View.GONE);
                    viewContractCloseAccountDiscount.setVisibility(checkBoxContractCloseAccount.isChecked() ? View.VISIBLE : View.GONE);

                    if (checkBoxContractCloseAccount.isChecked()) {
                        txtTotalOutStanding.setText(BHUtilities.numericFormat(data.contract.TotalOutstandingAmount - data.contract.CloseDiscountAmount));
                    }
                } else {
                    checkBoxContractCloseAccount.setEnabled(false);
                }*/


                    if (salePaymentPeriodInfoListForContractCloseAccount != null
                            && salePaymentPeriodInfoListForContractCloseAccount.size() > 0
                            && data.contract != null) {

                        layoutForCredit.setVisibility(View.VISIBLE);
                        listViewContractCloseAccount.setVisibility(View.GONE);
                        viewContractCloseAccountDiscount.setVisibility(View.GONE);

                        for (SalePaymentPeriodInfo s : salePaymentPeriodInfoListForContractCloseAccount) {
                            if (s.OverDue) {
                                //งวดทียังค้างชำระ
                                String item1 = String.format("งวดที่ %d", s.PaymentPeriodNumber);
                                String item2 = BHUtilities.numericFormat(s.OutstandingAmount);
                                listViewContractCloseAccount.addView(createItemListViewContractCloseAccountForOverdue(item1, item2, false));
                            } else {
                                //เรื่มสร้างตัดสด
                                if (s.PaymentPeriodNumber == s.MODE) {
                                    String item1 = String.format("งวดที่ %d", s.MODE);
                                    String item2 = BHUtilities.numericFormat(s.OutstandingAmount);
                                    listViewContractCloseAccount.addView(createItemListViewContractCloseAccountForOverdue(item1, item2, true));
                                    break;
                                } else {
                                    if (s.OutstandingAmount != s.NetAmount) {
                                        String item1 = String.format("งวดที่ %d", s.PaymentPeriodNumber);
                                        String item2 = BHUtilities.numericFormat(s.OutstandingAmount);
                                        listViewContractCloseAccount.addView(createItemListViewContractCloseAccountForOverdue(item1, item2, true));
                                    } else {
                                        String item1 = String.format("งวดที่ %d - %d งวดละ", s.PaymentPeriodNumber, s.MODE);
                                        String item2 = BHUtilities.numericFormat(s.NetAmount);
                                        listViewContractCloseAccount.addView(createItemListViewContractCloseAccountForOverdue(item1, item2, true));
                                        break;
                                    }
                                }
                            }
                        }

                        //txtContractCloseAccountDiscount.setText(BHUtilities.numericFormat(data.contract.CloseDiscountAmount));
                        txtContractCloseAccountDiscount.setText(BHUtilities.numericFormat( salePaymentPeriodInfoListForContractCloseAccount.get(0).CloseDiscountAmount));
                        /*listView.setVisibility(checkBoxContractCloseAccount.isChecked() ? View.GONE : View.VISIBLE);
                        listViewContractCloseAccount.setVisibility(checkBoxContractCloseAccount.isChecked() ? View.VISIBLE : View.GONE);
                        viewContractCloseAccountDiscount.setVisibility(checkBoxContractCloseAccount.isChecked() ? View.VISIBLE : View.GONE);

                        if (checkBoxContractCloseAccount.isChecked()) {
                            txtTotalOutStanding.setText(BHUtilities.numericFormat(data.contract.TotalOutstandingAmount - data.contract.CloseDiscountAmount));
                        }*/

                        if (checkBoxContractCloseAccount.isChecked()) {
                            checkBoxContractCloseAccount.callOnClick();
                        }

                        checkBoxContractCloseAccount.setEnabled(true);
                        if(salePaymentPeriodInfoListForContractCloseAccount.get(0).CloseDiscountAmount == 0) {
                            checkBoxContractCloseAccount.setEnabled(false);
                            checkBoxContractCloseAccount.setText("ตัดสดไม่ได้ เนื่องจากไม่เข้าเงื่อนไขการให้ส่วนลด");
                        }
                    }
                }

                if (editTextPrice.getText().toString().equals("")) {
                    if (data.processType == ProcessType.Sale || data.processType == ProcessType.FirstPayment) {
                        editTextPrice.setText(BHUtilities.numericFormat(data.paymentAmount));
                    } else  {
                        editTextPrice.setText(BHUtilities.numericFormat(defaultAmountPaid));
                    }
                } else {
                    if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {
                        if (!checkBoxContractCloseAccount.isChecked() && checkBoxFullPaid.isChecked()) {
                            checkBoxFullPaid.callOnClick();
                        }

                        if (checkBoxPartlyPaid.isChecked()) {
                            listView.setVisibility(View.VISIBLE);
                            listViewContractCloseAccount.setVisibility(View.GONE);
                            viewContractCloseAccountDiscount.setVisibility(View.GONE);

                            if (!isPriceChanged) {
                                editTextPrice.setText(BHUtilities.numericFormat(defaultAmountPaid));

                                for (int i = 0; i < tempListCBPaymentPeriodNumber.size(); i++) {
                                    if (tempListCBPaymentPeriodNumber.get(i).isChecked()) {
                                        listCBPaymentPeriodNumber.get(i).setChecked(true);
                                        listCBPaymentPeriodNumber.get(i).callOnClick();
                                    }

                                    listCBPaymentPeriodNumber.get(i).setEnabled(true);
                                }
                            }
                        }
                    }
                }

                //editTextPrice.setEnabled(checkBoxPartlyPaid.isChecked() ? true : false);
                bindBanks();
            }
        }).start();
    }

    public View createItemListViewContractCloseAccountForOverdue(String item1, final String item2, boolean isContractCloseAccount) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.list_sale_payment_period_detail, null);

        CheckBox cbPaymentPeriodNumber = (CheckBox) v.findViewById(R.id.chPaymentPeriodNumber);
        if (cbPaymentPeriodNumber != null) {
            cbPaymentPeriodNumber.setChecked(true);
            cbPaymentPeriodNumber.setEnabled(false);
            cbPaymentPeriodNumber.setText(item1);

            if (isContractCloseAccount) {
                cbPaymentPeriodNumber.setTextColor(getResources().getColor(R.color.holo_dark));
            }
        }

        TextView txtAmount = (TextView) v.findViewById(R.id.txtAmount);
        if (txtAmount != null) {
            txtAmount.setText(item2);

            if (isContractCloseAccount) {
                txtAmount.setTextColor(getResources().getColor(R.color.holo_dark));
                TextView lblBath = (TextView) v.findViewById(R.id.lblBath);
                if (lblBath != null) {
                    lblBath.setTextColor(getResources().getColor(R.color.holo_dark));
                }
            }
        }

        return v;
    }

    /*public View createItemListView(String item1, String item2, SalePaymentPeriodInfo s) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.list_credit_detail, null);
        TextView txtPaymentPeriodNumber = (TextView) v.findViewById(R.id.txtPaymentPeriodNumber);
        if (txtPaymentPeriodNumber != null) {
            txtPaymentPeriodNumber.setText(item1);
        }
        TextView txtAmount = (TextView) v.findViewById(R.id.txtAmount);
        if (txtAmount != null) {
            txtAmount.setText(item2);
        }
        if (s == null || (s != null && !s.OverDue)) {
            txtPaymentPeriodNumber.setTextColor(getResources().getColor(R.color.holo_dark));
            txtAmount.setTextColor(getResources().getColor(R.color.holo_dark));
            TextView lblBath = (TextView) v.findViewById(R.id.lblBath);
            if (lblBath != null) {
                lblBath.setTextColor(getResources().getColor(R.color.holo_dark));
            }
        }
        return v;
    }*/

    public View createItemListView(String item1, final String item2, SalePaymentPeriodInfo s) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.list_sale_payment_period_detail, null);

        final CheckBox cbPaymentPeriodNumber = (CheckBox) v.findViewById(R.id.chPaymentPeriodNumber);

        if(s != null) {
            cbPaymentPeriodNumber.setTag(String.format("%d,%s", s.PaymentPeriodNumber, item2.replace(",","")));
        } else {
            cbPaymentPeriodNumber.setTag(String.format("%d,%s", 0, item2.replace(",","")));
        }

        cbPaymentPeriodNumber.setChecked(false);
        cbPaymentPeriodNumber.setEnabled(false);
        if (cbPaymentPeriodNumber != null) {
            cbPaymentPeriodNumber.setText(item1);
        }
        TextView txtAmount = (TextView) v.findViewById(R.id.txtAmount);
        if (txtAmount != null) {
            txtAmount.setText(item2);
        }
        if (s == null || (s != null && !s.OverDue)) {
            cbPaymentPeriodNumber.setTextColor(getResources().getColor(R.color.holo_dark));
            txtAmount.setTextColor(getResources().getColor(R.color.holo_dark));
            TextView lblBath = (TextView) v.findViewById(R.id.lblBath);
            if (lblBath != null) {
                lblBath.setTextColor(getResources().getColor(R.color.holo_dark));
            }
        }

        cbPaymentPeriodNumber.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] _tag = cbPaymentPeriodNumber.getTag().toString().split(",");
                int _cbPaymentPeriodNumber = Integer.parseInt(_tag[0]);
                //int _amount = Integer.parseInt(_tag[1]);

                for (CheckBox cb : listCBPaymentPeriodNumber) {
                    String[] tag = cb.getTag().toString().split(",");

                    if (!tag[0].equals("0")) {
                        int paymentPeriodNumber = Integer.parseInt(tag[0]);


                        DecimalFormat formatter = new DecimalFormat(BHUtilities.DEFAULT_DOUBLE_FORMAT);
                        float allPayamt = 0;
                        float amount = 0;
                        try {
                            allPayamt = formatter.parse(editTextPrice.getText().toString()).floatValue();
                            amount = formatter.parse(tag[1]).floatValue();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (cbPaymentPeriodNumber.isChecked()) {
                            if (paymentPeriodNumber <= _cbPaymentPeriodNumber) {
                                if (!cb.isChecked() || (paymentPeriodNumber == _cbPaymentPeriodNumber)) {
                                    editTextPrice.setText(BHUtilities.numericFormat(allPayamt + amount));
                                }
                                cb.setChecked(cbPaymentPeriodNumber.isChecked());
                            }
                        } else {
                            if (paymentPeriodNumber >= _cbPaymentPeriodNumber) {
                                if (cb.isChecked() || (paymentPeriodNumber == _cbPaymentPeriodNumber)) {
                                    if (allPayamt >= amount) {
                                        editTextPrice.setText(BHUtilities.numericFormat(allPayamt - amount));
                                    }
                                }
                                cb.setChecked(cbPaymentPeriodNumber.isChecked());
                            }
                        }
                    }

                }
                isPriceChanged = false;
            }
        });

        listCBPaymentPeriodNumber.add(cbPaymentPeriodNumber);
        return v;
    }

    private void saveAppointmentData() {
        new BackgroundProcess(activity) {

            Date dtAppointmentDate;

            @Override
            protected void before() {
                /*** [START] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป ***/

                /*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String strAppointDate = editTextappointment.getText().toString();
                String strDay = strAppointDate.split("/")[0];
                String strMonth = strAppointDate.split("/")[1];
                String strYear = strAppointDate.split("/")[2];

                int intYear = 0;
                try {
                    intYear = Integer.parseInt(strYear);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                String strAppointmentDate = String.format("%s/%s/%d", strDay, strMonth, intYear - 543);
                try {
                    dtAppointmentDate = sdf.parse(strAppointmentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

                if (calendarAppointment != null) {
                    dtAppointmentDate = calendarAppointment.getTime();
                }
                /*** [END] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป  ***/
            }

            @Override
            protected void calling() {
                postponeAppointment(data.processType, data.refNo, dtAppointmentDate, maxPaymentPeriodNumber, true);
            }

            @Override
            protected void after() {
                if (data.processType == ProcessType.Sale) {
                    showNextView(new SalePhotographyFragment());
                } else if (data.processType == ProcessType.Credit) {
                    showLastView(CreditMainFragment.CREDIT_MAIN_FRAGMENT);
                } else if (data.processType == ProcessType.NextPayment) {
                    showLastView(NextPaymentListFragment.NEXT_PAYMENT_LIST_FRAGMENT);
                } else {
                    showLastView(FRAGMENT_SALE_FIRST_PAYMENT_TAG);
                }
            }
        }.start();
    }

    private boolean validate() {
        String title = "ชำระเงิน";
        String message = "";

        if (checkBoxAppointments.isChecked()) {
            isPostPone = true;

            /*** [START] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป ***/

            /*String Date = editTextappointment.getText().toString();
            if (Date.isEmpty()) {*/

            if (calendarAppointment == null) {
            /*** [END] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป  ***/
                message = "กรุณาเลือกวันนัดชำระ";
                showNoticeDialogBox(title, message);
                return false;
            } else {

                boolean booleanReturn = false;
                switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {

                    case Sale:
                    case FirstPayment:
                    case NextPayment:
                    case Credit:
                        booleanReturn = true;
                        break;
                    /*case NextPayment:
                    case Credit:
                        List<SalePaymentPeriodInfo> periods = new SalePaymentPeriodController().getNextPayment(data.refNo);

                        if (periods.size() > 1) {
                            String strDay = Date.split("/")[0];
                            String strMonth = Date.split("/")[1];
                            String strYear = Date.split("/")[2];

                            Calendar cPaymentAppointmentDate = Calendar.getInstance(new Locale("th"));
                            cPaymentAppointmentDate.setTime(periods.get(1).PaymentAppointmentDate);
                            cPaymentAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                            cPaymentAppointmentDate.set(Calendar.MINUTE, 0);
                            cPaymentAppointmentDate.set(Calendar.SECOND, 0);
                            cPaymentAppointmentDate.set(Calendar.MILLISECOND, 0);

                            Calendar cNewAppointmentDate = Calendar.getInstance(new Locale("th"));
                            cNewAppointmentDate.set(Calendar.YEAR, Integer.parseInt(strYear) - 543);
                            cNewAppointmentDate.set(Calendar.MONTH, Integer.parseInt(strMonth) - 1);
                            cNewAppointmentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDay));
                            cNewAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                            cNewAppointmentDate.set(Calendar.MINUTE, 0);
                            cNewAppointmentDate.set(Calendar.SECOND, 0);
                            cNewAppointmentDate.set(Calendar.MILLISECOND, 0);

                            if (cNewAppointmentDate.before(cPaymentAppointmentDate)) {
                                booleanReturn = true;
                            } else {

                                Calendar cNewDate = Calendar.getInstance(new Locale("th"));
                                cNewDate.setTime(new Date());
                                cNewDate.set(Calendar.HOUR_OF_DAY, 0);
                                cNewDate.set(Calendar.MINUTE, 0);
                                cNewDate.set(Calendar.SECOND, 0);
                                cNewDate.set(Calendar.MILLISECOND, 0);
                                if (cPaymentAppointmentDate.before(cNewDate)) {
                                    message = "ไม่สามารถเลื่อนนัดชำระได้";
                                } else {
                                    cPaymentAppointmentDate.add(Calendar.DATE, -1);

                                    message = "เลือกวันนัดชำระได้ไม่เกินวันที่ " + BHUtilities.dateFormat(cPaymentAppointmentDate.getTime(), "dd/MM/yyyy");
                                }
                                showNoticeDialogBox(title, message);

                                booleanReturn = false;
                            }

                        } else {
                            booleanReturn = true;
                        }

                        break;*/
                }

                return booleanReturn;
            }
        }

        if (editTextPrice.getText().toString().equals("")) {
            message = "กรุณาระบุจำนวนเงินที่ถูกต้อง";
            showNoticeDialogBox(title, message);
            return false;
        }

        DecimalFormat formatter = new DecimalFormat(BHUtilities.DEFAULT_DOUBLE_FORMAT);
        float amount = 0;
        try {
            amount = formatter.parse(editTextPrice.getText().toString()).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (amount <= 0) {
            message = "กรุณาระบุจำนวนเงินที่ถูกต้อง";
            showNoticeDialogBox(title, message);
            return false;
        }

        data.paymentAmount = amount;

        if (checkBoxPayment.isChecked()) {
            isPostPone = false;

            boolean isAmountOver = false;
            float outstandingAmount = 0;

            if (summaryNetAmount != null) {
                outstandingAmount = summaryNetAmount.OutstandingAmount;
                if (amount > outstandingAmount)
                    isAmountOver = true;
            }

            if (isAmountOver) {
                message = "จำนวนเงินที่ป้อนเกินกว่าจำนวนเงินที่ต้องชำระ กรุณาป้อนใหม่อีกครั้ง";
                showNoticeDialogBox(title, message);
                return false;
            } else if (checkBoxFullPaid.isChecked()) {
                if (checkBoxCash.isChecked()) {
                    // Nothing
                } else if (checkBoxCredit.isChecked()) {
                    String cardseries = editTextBankSeries.getText().toString();
                    String authorizationcode = editTextAuthorizationCode.getText().toString();
                    if (cardseries.length() != 16 || authorizationcode.length() == 0) {
                        message = "กรุณากรอกข้อมูลบัตรเครดิตให้ครบ";
                        showNoticeDialogBox(title, message);
                        return false;
                    }
                } else if (checkBoxCheque.isChecked()) {
                    String cardseries = editTextBranch.getText().toString();
                    String chequenumber = editTextChequeNumber.getText().toString();
                    String duedate = editTextDate.getText().toString();
                    if (chequenumber.length() != 7 || cardseries.length() == 0 || duedate.length() == 0) {
                        message = "กรุณากรอกข้อมูลเช็คให้ครบ";
                        showNoticeDialogBox(title, message);
                        return false;
                    }
                } else if (checkBoxQrcode.isChecked()) {

                } else {
                    message = "กรุณาเลือกช่องทางการชำระเงิน/เงินสด/บัตรเครดิต/เช็ค";
                    showNoticeDialogBox(title, message);
                    return false;
                }
            } else if (checkBoxPartlyPaid.isChecked()) {
                if (!checkBoxCash.isChecked() && !checkBoxQrcode.isChecked()) {
                    message = "กรุณาเลือกช่องทางการชำระเงิน/เงินสด";
                    showNoticeDialogBox(title, message);
                    return false;
                }

                /*** [START] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/
                boolean booleanReturn = false;
                switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {

                    case Sale:
                    case FirstPayment:
                    case NextPayment:
                    case Credit:
                        booleanReturn = true;
                        break;
                    /*case NextPayment:
                    case Credit:
                        float number = editTextPrice.getText().toString().equals("") ? 0f : Float.valueOf(editTextPrice.getText().toString().replace(",", ""));
                        if (number < data.contract.PAYAMT && !editTextAppointmentsForPartlyPaid.getText().toString().equals("")) {
                            String Date = editTextAppointmentsForPartlyPaid.getText().toString();
                            List<SalePaymentPeriodInfo> periods = new SalePaymentPeriodController().getNextPayment(data.refNo);

                            if (periods.size() > 1) {
                                String strDay = Date.split("/")[0];
                                String strMonth = Date.split("/")[1];
                                String strYear = Date.split("/")[2];

                                Calendar cPaymentAppointmentDate = Calendar.getInstance(new Locale("th"));
                                cPaymentAppointmentDate.setTime(periods.get(1).PaymentAppointmentDate);
                                cPaymentAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                                cPaymentAppointmentDate.set(Calendar.MINUTE, 0);
                                cPaymentAppointmentDate.set(Calendar.SECOND, 0);
                                cPaymentAppointmentDate.set(Calendar.MILLISECOND, 0);

                                Calendar cNewAppointmentDate = Calendar.getInstance(new Locale("th"));
                                cNewAppointmentDate.set(Calendar.YEAR, Integer.parseInt(strYear) - 543);
                                cNewAppointmentDate.set(Calendar.MONTH, Integer.parseInt(strMonth) - 1);
                                cNewAppointmentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDay));
                                cNewAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                                cNewAppointmentDate.set(Calendar.MINUTE, 0);
                                cNewAppointmentDate.set(Calendar.SECOND, 0);
                                cNewAppointmentDate.set(Calendar.MILLISECOND, 0);

                                if (cNewAppointmentDate.before(cPaymentAppointmentDate)) {
                                    booleanReturn = true;
                                } else {

                                    Calendar cNewDate = Calendar.getInstance(new Locale("th"));
                                    cNewDate.setTime(new Date());
                                    cNewDate.set(Calendar.HOUR_OF_DAY, 0);
                                    cNewDate.set(Calendar.MINUTE, 0);
                                    cNewDate.set(Calendar.SECOND, 0);
                                    cNewDate.set(Calendar.MILLISECOND, 0);
                                    if (cPaymentAppointmentDate.before(cNewDate)) {
                                        message = "ไม่สามารถเลื่อนนัดชำระบางส่วนได้";
                                    } else {
                                        cPaymentAppointmentDate.add(Calendar.DATE, -1);

                                        message = "เลือกวันนัดชำระบางส่วนได้ไม่เกินวันที่ " + BHUtilities.dateFormat(cPaymentAppointmentDate.getTime(), "dd/MM/yyyy");
                                    }
                                    showNoticeDialogBox(title, message);

                                    booleanReturn = false;
                                }

                            } else {
                                booleanReturn = true;
                            }


                        } else {
                            booleanReturn = true;
                        }
                        break;*/
                }
                return booleanReturn;
                /*** [END] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/

            } else {
                message = "กรุณาเลือกช่องทางการชำระเงิน/ชำระเต็มจำนวน/ชำระบางส่วน";
                showNoticeDialogBox(title, message);
                return false;
            }
        } else if (checkBoxAppointments.isChecked()) {
            /*** [START] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป ***/

            /*isPostPone = true;
            String Date = editTextappointment.getText().toString();
            if (Date.isEmpty()) {
                message = "กรุณาเลือกวันนัดชำระ";
                showNoticeDialogBox(title, message);
                return false;
            }*/

            isPostPone = true;
            if (calendarAppointment == null) {
                message = "กรุณาเลือกวันนัดชำระ";
                showNoticeDialogBox(title, message);
                return false;
            }
            /*** [END] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป  ***/
        } else {
            message = "กรุณาเลือกช่องทางการชำระเงิน/ชำระทันที/นัดชำระ";
            showNoticeDialogBox(title, message);
            return false;
        }
        return true;
    }

    private void showNoticeDialogBox(final String title, final String message) {
        // YIM change Dialog to run on ui thread
        getActivity().runOnUiThread(new Runnable(){
            public void run(){
                Builder setupAlert;
                setupAlert = new AlertDialog.Builder(activity);
                setupAlert.setTitle(title);
                setupAlert.setMessage(message);
                setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //???
                    }
                });
                setupAlert.show();
            }
        });
    }

    /*** [START] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป ***/
    private void ClearAppointmentsForPartlyPaid() {
        calendarAppointmentsForPartlyPaid = null;
        editTextAppointmentsForPartlyPaid.setText("");
        llAppointmentsForPartlyPaid.setVisibility(View.GONE);
    }

    private void ClearAppointments() {
        calendarAppointment = null;
        editTextappointment.setText("");
    }
    /*** [END] :: Fixed - [BHPROJ-1036-8595] - [LINE@17/09/2561] ทีม NBAH04 รหัสพนักงาน A55345 ส่วนที่รับชำระบางส่วนไม่ได้ ตามรูป  ***/
}
