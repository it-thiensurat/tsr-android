package th.co.thiensurat.fragments.sales;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.SurveyActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.PaymentController;
import th.co.thiensurat.data.controller.RequestNextPaymentController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.BankInfo;
import th.co.thiensurat.data.info.ContractCloseAccountInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.RequestNextPaymentInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodPaymentInfo;
import th.co.thiensurat.data.info.TripInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.service.GetCurrentLocation;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;

public class SaleConfirmBeforeReceiptFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String refNo;
        public ProcessType processType;
        public float paymentAmount;
        public float BalancesOfPeriod;

        public ContractInfo contract;
        public TripInfo trip;
        public BankInfo bank;

        public String paymentType; // ช่องทางการชำระ/เงินสด/บัตรเครดิต/เช็ค/คิวอาร์โค้ด
        public boolean payPartial; // สถานะ จ่ายเต็มบ่างส่วน
        public boolean isPostPone;
        public ContractCloseAccountInfo contractCloseAccount;

        public String creditCardNumber;
        public String creditCardApproveCode;
        public String creditEmployeeLevelPath;

        public String chequeBankBranch;
        public String chequeNumber;
        public String chequeDate;

        public String receiptCode;
        public float PartlyPaidPrice;

        public String requestNextPaymentID;

        public Date dateOfAppointmentsForPartlyPaid;
        public int maxPaymentPeriodNumber;

        public Date selectedDate;
    }

    private Data data;

    GetCurrentLocation currentLoc;


    // private PaymentInfo payment = null;
    private AddressInfo address = null;
    private SalePaymentPeriodInfo spp = null;

    @InjectView
    private TextView txt_name_sale,txtPayDate, txtReceiptCode, txtCONTNO, txtEFFDATE, txtCustomerName, txtIDCard, txtAddress, txtProductName, txtProductSerialNumber, txtPAYAMT, txtBalance, txtBank, txtBankSeries, txtAuthorizationCode, txtBranch, txtChequeNumber, txtChequeDate, txtSaleEmpName, txtSaleTeamName, labelBalance, txtThaiBaht, txtThaiBahtNum, txtNumber1, txtNumber2, txtNumber3, txtNumber4, txtNumber5, textViewModel, txtContractCloseAccountDiscount, labelBalancesOfPeriod, txtBalancesOfPeriod;
    @InjectView
    private LinearLayout bankview, creditview, chequeview, linearLayoutPartlyPaid, ItemLineBalance, linearLayoutHeadNumber, viewContractCloseAccountDiscount, ItemLineBalancesOfPeriod;
    @InjectView
    ViewTitle lblTitle;

    @Override
    protected int titleID() {
        if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.Credit) {
            return R.string.title_main_credit;
        } else if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.NextPayment) {
            return R.string.title_payment_next;
        } else {
            return R.string.title_payment_first;
        }
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_sale_confirm_before_receipt;
    }

    @Override
    protected int[] processButtons() {
        if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.Credit) {
            return new int[]{R.string.button_back, R.string.button_update_customer_phone, R.string.button_confirm_print};
        } else {
            return new int[]{R.string.button_back, R.string.button_confirm_print};
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        Log.e("page","9");
        if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            txtNumber1.setText("...");
            txtNumber2.setText("8");
            txtNumber3.setText("9");
            txtNumber4.setText("10");
            txtNumber5.setText("11");
            txtNumber3.setBackgroundResource(R.drawable.circle_number_sale_color_red);
        } else {
            linearLayoutHeadNumber.setVisibility(View.GONE);
        }

        data = getData();
        checklastperiod();

        if (data.processType == ProcessType.Credit || data.processType == ProcessType.NextPayment) {
            lblTitle.setText(R.string.title_next_payment_credit);
        }

        loadData();


        currentLoc = new GetCurrentLocation(getActivity());

        txt_name_sale.setText("ใบรับเงิน");
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_confirm_print:
                final String title = "คำเตือน";
                final String message = "คุณต้องการออกใบเสร็จรับเงิน?";
                if (lastPeriod) {
                    showDialogUpdate();
                } else {
                    showYesNoDialogBox(title, message);
                }

                break;
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_update_customer_phone :
                showDialogUpdate();
                break;
            default:
                break;
        }
    }

    private void showDialogUpdate() {
        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("ใส่เบอร์โทรลูกค้า");
        input.setInputType(InputType.TYPE_CLASS_PHONE);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(10);
        input .setFilters(filters);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(input);
        alertDialog.setMessage("กรุณาอัพเดทเบอร์ฯ ของลูกค้า");
        alertDialog.setTitle("อัพเดทข้อมูล");

        alertDialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateCustomerPhone(input.getText().toString());
            }
        });

        alertDialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showYesNoDialogBox(final String title, final String message) {
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.e("GPS","12345678");

                if(currentLoc.check_gps_open==1){
                   // currentLoc = new GetCurrentLocation(getActivity());
                  //  currentLoc = new GetCurrentLocation(getActivity());
                 //   currentLoc.connectGoogleApi();

                    String latitude2 = currentLoc.latitude;
                    String longitude2 = currentLoc.longitude;
                    Log.e("latlong_M",latitude2+","+longitude2);

                    Log.e("GPS","OPEN");
                    save_gps(data.receiptCode,BHPreference.employeeID() ,latitude2,longitude2);
                    savePaymentData();

                }
                else {

                        currentLoc = new GetCurrentLocation(getActivity());
                        currentLoc.connectGoogleApi();
                    Log.e("GPS","OFF");

                }
            }
        }).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // just do nothing
            }
        });
        setupAlert.show();
    }

    private void loadData() {
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                if (data.contract == null) {
                    data.contract = getContractByRefNoByPaymentPeriodNumber(BHPreference.organizationCode(), BHPreference.RefNo(), "1");
                }
                address = getAddress(BHPreference.RefNo(), AddressType.AddressInstall);
                spp = getSalePaymentPeriodInfoByPaymentComplete(BHPreference.RefNo(), "0");
            }

            @Override
            protected void after() {
                if (data.contract != null) {
                    bindContract();
                }

                if (address != null) {
                    bindAddress();
                }

                if (data.bank != null) {
                    bindBank();
                }
                setVisible();
            }
        }).start();
    }

    private void setVisible() {
        bankview.setVisibility(View.GONE);
        creditview.setVisibility(View.GONE);
        chequeview.setVisibility(View.GONE);

        if (data.paymentType.equals("Credit")) {
            bankview.setVisibility(View.VISIBLE);
            creditview.setVisibility(View.VISIBLE);
        } else if (data.paymentType.equals("Cheque")) {
            bankview.setVisibility(View.VISIBLE);
            chequeview.setVisibility(View.VISIBLE);
        }

        if (data.PartlyPaidPrice == spp.PaymentAmount) {
            linearLayoutPartlyPaid.setVisibility(View.VISIBLE);
        }
    }

    private void bindContract() {
        String strPaymentPeriodNumber = "";
        String strPartlyPaidPrice = "";
        String strBahtPartlyPaid = "";
        float partlyPaidPriceByPaymentPeriod = 0.0f;
        boolean status = false;
        int nextPaymentPeriodNumber = 0;
        int maxPaymentPeriodNumber = 0;

        txtPayDate.setText(BHUtilities.dateFormat(new Date()));
        txtReceiptCode.setText(data.receiptCode);
        txtCONTNO.setText(data.contract.CONTNO);
        txtEFFDATE.setText(BHUtilities.dateFormat(data.contract.EFFDATE));
        // txtCustomerName.setText(data.contract.CustomerFullName);
        txtCustomerName.setText(String.format("%s %s", BHUtilities.trim(data.contract.CustomerFullName), BHUtilities.trim(data.contract.CompanyName)));
        txtIDCard.setText(data.contract.IDCard);
        // txtAddress
        txtProductName.setText(data.contract.ProductName);
        textViewModel.setText(data.contract.MODEL);
        txtProductSerialNumber.setText(data.contract.ProductSerialNumber);
        txtPAYAMT.setText(BHUtilities.numericFormat(data.contract.PaymentAmount - data.contract.TradeInDiscount));

        if (!status) {
            if (data.contractCloseAccount != null) {
                partlyPaidPriceByPaymentPeriod = data.PartlyPaidPrice + data.contractCloseAccount.DiscountAmount;
            } else {
                partlyPaidPriceByPaymentPeriod = data.PartlyPaidPrice;
            }

            List<SalePaymentPeriodInfo> viewPaymentPeriod = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoComplete(BHPreference.RefNo());
            if (viewPaymentPeriod.size() == 1 && viewPaymentPeriod.get(0).PaymentPeriodNumber == 1) {
                for (SalePaymentPeriodInfo into : viewPaymentPeriod) {
                    if (partlyPaidPriceByPaymentPeriod == (into.PaymentAmount - into.Discount)) {
                        /*strPaymentPeriodNumber += "ค่างวดเงินสด";
                        strPartlyPaidPrice += BHUtilities.numericFormat(partlyPaidPriceByPaymentPeriod);
                        strBahtPartlyPaid += "บาท";*/
                        linearLayoutPartlyPaid.addView(getLinearLayoutPartlyPaid("ค่างวดเงินสด", BHUtilities.numericFormat(partlyPaidPriceByPaymentPeriod)));

                    } else {
                        /*strPaymentPeriodNumber += "ค่างวดเงินสด (ชำระบางส่วน)";
                        strPartlyPaidPrice += BHUtilities.numericFormat(partlyPaidPriceByPaymentPeriod);
                        strBahtPartlyPaid += "บาท";*/
                        linearLayoutPartlyPaid.addView(getLinearLayoutPartlyPaid("ค่างวดเงินสด\n(ชำระบางส่วน)", BHUtilities.numericFormat(partlyPaidPriceByPaymentPeriod)));
                    }
                }
            } else {
                maxPaymentPeriodNumber = viewPaymentPeriod.get(viewPaymentPeriod.size() - 1).PaymentPeriodNumber;
                for (SalePaymentPeriodInfo into : viewPaymentPeriod) {
                    if (partlyPaidPriceByPaymentPeriod > (into.PaymentAmount - into.Discount)) {
                        /*strPaymentPeriodNumber += String.format("ค่างวดที่ %d/%d", into.PaymentPeriodNumber, maxPaymentPeriodNumber) + "\n\n";
                        strPartlyPaidPrice += BHUtilities.numericFormat(into.PaymentAmount - into.Discount) + "\n\n";
                        strBahtPartlyPaid += "บาท" + "\n\n";*/
                        linearLayoutPartlyPaid.addView(getLinearLayoutPartlyPaid(String.format("ค่างวดที่ %d/%d", into.PaymentPeriodNumber, maxPaymentPeriodNumber), BHUtilities.numericFormat(into.PaymentAmount - into.Discount)));
                        partlyPaidPriceByPaymentPeriod = partlyPaidPriceByPaymentPeriod - (into.PaymentAmount - into.Discount);
                        nextPaymentPeriodNumber = into.PaymentPeriodNumber + 1;
                    } else if ((partlyPaidPriceByPaymentPeriod <= (into.PaymentAmount - into.Discount)) && !status) {
                        status = true;
                        if (partlyPaidPriceByPaymentPeriod == (into.PaymentAmount - into.Discount)) {
                            /*strPaymentPeriodNumber += String.format("ค่างวดที่ %d/%d", into.PaymentPeriodNumber, maxPaymentPeriodNumber);
                            strPartlyPaidPrice += BHUtilities.numericFormat(partlyPaidPriceByPaymentPeriod);
                            strBahtPartlyPaid += "บาท";*/
                            linearLayoutPartlyPaid.addView(getLinearLayoutPartlyPaid(String.format("ค่างวดที่ %d/%d", into.PaymentPeriodNumber, maxPaymentPeriodNumber), BHUtilities.numericFormat(partlyPaidPriceByPaymentPeriod)));
                            partlyPaidPriceByPaymentPeriod = partlyPaidPriceByPaymentPeriod - (into.PaymentAmount - into.Discount);
                            nextPaymentPeriodNumber = into.PaymentPeriodNumber + 1;
                        } else {
                            /*strPaymentPeriodNumber += String.format("ค่างวดที่ %d/%d (ชำระบางส่วน)", into.PaymentPeriodNumber, maxPaymentPeriodNumber);
                            strPartlyPaidPrice += BHUtilities.numericFormat(partlyPaidPriceByPaymentPeriod);
                            strBahtPartlyPaid += "บาท";*/
                            linearLayoutPartlyPaid.addView(getLinearLayoutPartlyPaid(String.format("ค่างวดที่ %d/%d\n(ชำระบางส่วน)", into.PaymentPeriodNumber, maxPaymentPeriodNumber), BHUtilities.numericFormat(partlyPaidPriceByPaymentPeriod)));
                            SalePaymentPeriodPaymentInfo sppPaymentList = new SalePaymentPeriodController().getSumSalePaymentPeriodPaymentBySalePaymentPeriodID(into.SalePaymentPeriodID);
                            //งวดแรก ชำระครบแล้วหรือไม่
                            if ((sppPaymentList.SumAmount + partlyPaidPriceByPaymentPeriod) == into.NetAmount) {
                                ItemLineBalancesOfPeriod.setVisibility(View.GONE);
                                nextPaymentPeriodNumber = into.PaymentPeriodNumber + 1;
                            } else {
                                nextPaymentPeriodNumber = into.PaymentPeriodNumber;
                                data.BalancesOfPeriod = into.NetAmount - (sppPaymentList.SumAmount + partlyPaidPriceByPaymentPeriod);
                                ItemLineBalancesOfPeriod.setVisibility(View.VISIBLE);
                                labelBalancesOfPeriod.setText(String.format("คงเหลืองวดที่ %d", nextPaymentPeriodNumber));
                                txtBalancesOfPeriod.setText(BHUtilities.numericFormat(data.BalancesOfPeriod));
                                if (nextPaymentPeriodNumber == maxPaymentPeriodNumber) {
                                    ItemLineBalance.setVisibility(View.GONE);
                                } else {
                                    ItemLineBalance.setVisibility(View.VISIBLE);
                                    nextPaymentPeriodNumber++;
                                    /*if (nextPaymentPeriodNumber == maxPaymentPeriodNumber) {
                                        labelBalance.setText(String.format("คงเหลืองวดที่ %d", nextPaymentPeriodNumber));
                                    } else {
                                        labelBalance.setText(String.format("คงเหลืองวดที่ %d - %d", nextPaymentPeriodNumber, maxPaymentPeriodNumber));
                                    }*/
                                }
                            }
                            break;
                        }

                    }
                }
            }
            //txtPartlyPaid.setText(strPaymentPeriodNumber);

            /*** [START] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับี ***/
            /*SpannableString periodAmount = new SpannableString(strPartlyPaidPrice);
            periodAmount.setSpan(new ForegroundColorSpan(Color.RED), 0, strPartlyPaidPrice.length(), 0);//สี
            periodAmount.setSpan(new StyleSpan(Typeface.BOLD), 0, strPartlyPaidPrice.length(), 0);//ตัวหนา
            periodAmount.setSpan(new TextAppearanceSpan(activity, R.style.TextView_Value2), 0 , strPartlyPaidPrice.length(), 0);//ขนาดตัว
            txtPartlyPaidPrice.setText(periodAmount, TextView.BufferType.SPANNABLE);*/

            //txtPartlyPaidPrice.setText(strPartlyPaidPrice);
            /*** [END] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับ***/
            //txtBahtPartlyPaid.setText(strBahtPartlyPaid);
        }
        //txtPartlyPaid.setText(String.format("ค่างวด %s (ชำระบางส่วน)",spp.PaymentPeriodNumber));
        //txtPartlyPaidPrice.setText(BHUtilities.numericFormat(data.PartlyPaidPrice));

        //txtSaleEmpName.setText("(" + data.contract.upperEmployeeName + ")");
        txtSaleEmpName.setText(String.format("(%s)", BHPreference.userFullName()));
        //txtSaleTeamName.setText("(ทีม "+ data.contract.SaleTeamCode + ")");
        txtSaleTeamName.setText(String.format("(ทีม %s)", BHPreference.teamCode()));
        //txtSaleTeamName.setText(String.format("(ทีม %s)", BHPreference.cashCode()));

        txtThaiBaht.setText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(data.PartlyPaidPrice)));
        txtThaiBahtNum.setText(BHUtilities.numericFormat(data.PartlyPaidPrice));

        final int finalNextPaymentPeriodNumber = nextPaymentPeriodNumber;
        new BackgroundProcess(activity) {
            float totalPay = 0;

            @Override
            protected void calling() {
                List<PaymentInfo> payments = new PaymentController().getPaymentByRefNo(BHPreference.organizationCode(), data.contract.RefNo);
                if (payments != null) {
                    for (int ii = 0; ii < payments.size(); ii++) {
                        totalPay += payments.get(ii).PAYAMT;
                    }
                }
            }

            @Override
            protected void after() {
                float paymentAmount = 0;
                if (data.contractCloseAccount != null) {
                    paymentAmount = data.paymentAmount + data.contractCloseAccount.DiscountAmount;
                } else {
                    paymentAmount = data.paymentAmount;
                }
                if (data.contractCloseAccount != null) {
                    viewContractCloseAccountDiscount.setVisibility(View.VISIBLE);
                    txtContractCloseAccountDiscount.setText(BHUtilities.numericFormat(data.contractCloseAccount.DiscountAmount));
                }
                if ((data.contract.TotalPrice - totalPay - paymentAmount) == 0) {
                    //ItemLineBalance.setVisibility(View.GONE);
                    // ถ้า mode > 1 แสดงว่าเป็นการผ่อนชำระ จะไม่แสดงคำว่า "ยอดรวมสุทธิ" แต่ถ้าเป็นจ่ายสดแบบไม่ผ่อน จะแสดงตามปกติ
                    if (data.contract.MODE > 1) {
                        ItemLineBalance.setVisibility(View.GONE);
                    } else {
                        ItemLineBalance.setVisibility(View.VISIBLE);
                        labelBalance.setText("ยอดรวมสุทธิ");
                        txtBalance.setText(BHUtilities.numericFormat(data.contract.TotalPrice));
                    }
                } else {
                    if (data.contract.MODE > 1) {
                        if (finalNextPaymentPeriodNumber == data.contract.MODE) {
                            labelBalance.setText(String.format("คงเหลืองวดที่ %d", finalNextPaymentPeriodNumber));
                        } else {
                            labelBalance.setText(String.format("คงเหลืองวดที่ %d - %d", finalNextPaymentPeriodNumber, data.contract.MODE));
                        }
                        txtBalance.setText(BHUtilities.numericFormat(data.contract.TotalPrice - totalPay - data.paymentAmount - data.BalancesOfPeriod));
                    } else {
                        labelBalance.setText(String.format("คงเหลือเงินสด"));
                        txtBalance.setText(BHUtilities.numericFormat(data.contract.TotalPrice - totalPay - data.paymentAmount - data.BalancesOfPeriod));
                    }

                }
                //txtฺBALAMT.setText(BHUtilities.numericFormat(data.contract.TotalPrice - totalPay - data.paymentAmount));
            }
        }.start();
    }

    private void bindAddress() {
        txtAddress.setText(BHUtilities.trim(address.Address()));
    }

    private void bindBank() {
        txtBank.setText(data.bank.BankName);

        if (data.paymentType.equals("Credit")) {
            txtBankSeries.setText(data.creditCardNumber);
            txtAuthorizationCode.setText(data.creditCardApproveCode);
        } else if (data.paymentType.equals("Cheque")) {
            txtBranch.setText(data.chequeBankBranch);
            txtChequeNumber.setText(data.chequeNumber);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = formatter.parse(data.chequeDate);
                txtChequeDate.setText(BHUtilities.dateFormat(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePaymentData() {
        new BackgroundProcess(activity) {
            PaymentInfo input = null;
            ContractCloseAccountInfo contractCloseAccount = null;

            @Override
            protected void before() {
                Date date = new Date();
                input = new PaymentInfo();

                input.PaymentID = DatabaseHelper.getUUID();
                input.OrganizationCode = BHPreference.organizationCode();
                input.PaymentType = data.paymentType;
                input.PayPartial = data.payPartial;
                input.RefNo = data.refNo;
                input.Status = PaymentInfo.PaymentStatus.Y.toString();
                if (data.trip != null) {
                    input.TripID = data.trip.TripID;
                }
                input.PayPeriod = "01";
                input.PayDate = date;
                input.PAYAMT = data.paymentAmount;

                /*** [START] Fixed - [BHPROJ-0024-573] ***/
//                input.CashCode = BHPreference.cashCode();
//                input.EmpID = BHPreference.employeeID();
//                input.TeamCode = BHPreference.teamCode();
                /* FIX BHPROJ-0026-930 แก้ไข ถ้าเป็นการเก็บเงินงวดแรกจะให้ EmpID ที่ Table Payment = SaleEmployeeCode ของ Contract นั้นๆ นอกนั้นจะเป็นคน Login */
                SalePaymentPeriodInfo spp = new SalePaymentPeriodController().getSalePaymentPeriodInfoByRefNoAndPaymentPeriodNumber(input.RefNo, 1);
                if (spp != null && spp.PaymentComplete) { // JOB CREDIT
                    input.CashCode = BHPreference.cashCode();
                    input.EmpID = BHPreference.employeeID();
                    input.TeamCode = BHPreference.teamCode();
                } else { // JOB SALE
                    input.CashCode = (data.contract.CreateBy == BHPreference.employeeID()) ? data.contract.SaleCode : BHPreference.saleCode();
                    input.EmpID = (data.contract.CreateBy == BHPreference.employeeID()) ? data.contract.SaleEmployeeCode : BHPreference.employeeID();
                    input.TeamCode = (data.contract.CreateBy == BHPreference.employeeID()) ? data.contract.SaleTeamCode : BHPreference.teamCode();
                }
                /*** [END] Fixed - [BHPROJ-0024-573] ***/

                input.CreditEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                input.CreateBy = BHPreference.employeeID();
                input.CreateDate = date;
                input.LastUpdateBy = BHPreference.employeeID();
                input.LastUpdateDate = date;
                input.ReceiptCode = data.receiptCode;

                if (data.paymentType.equals("Credit")) {
                    input.BankCode = data.bank.BankCode;
                    input.CreditCardNumber = data.creditCardNumber;
                    input.CreditCardApproveCode = data.creditCardApproveCode;
                } else if (data.paymentType.equals("Cheque")) {
                    input.BankCode = data.bank.BankCode;
                    input.ChequeBankBranch = data.chequeBankBranch;
                    input.ChequeNumber = data.chequeNumber;
                    input.ChequeDate = data.chequeDate;
                }

                if (data.contractCloseAccount != null) {
                    contractCloseAccount = new ContractCloseAccountInfo();
                    contractCloseAccount.ContractCloseAccountID = DatabaseHelper.getUUID();
                    contractCloseAccount.OrganizationCode = BHPreference.organizationCode();
                    contractCloseAccount.RefNo = data.refNo;
                    contractCloseAccount.PaymentID = input.PaymentID;
                    contractCloseAccount.SalePaymentPeriodID = data.contractCloseAccount.SalePaymentPeriodID;
                    contractCloseAccount.OutstandingAmount = data.contractCloseAccount.OutstandingAmount;
                    contractCloseAccount.DiscountAmount = data.contractCloseAccount.DiscountAmount;
                    contractCloseAccount.NetAmount = data.contractCloseAccount.NetAmount;
                    contractCloseAccount.EffectiveDate = new Date();
                    contractCloseAccount.EffectiveBy = BHPreference.employeeID();
                    contractCloseAccount.EffectiveTeamCode = BHPreference.teamCode();
                    contractCloseAccount.CreateDate = new Date();
                    contractCloseAccount.CreateBy = BHPreference.employeeID();
                    contractCloseAccount.LastUpdateDate = new Date();
                    contractCloseAccount.LastUpdateBy = BHPreference.employeeID();
                }
            }

            @Override
            protected void calling() {
                addPayment(input, true, data.processType, contractCloseAccount, data.dateOfAppointmentsForPartlyPaid, data.maxPaymentPeriodNumber);

                if (data.processType == ProcessType.NextPayment && data.requestNextPaymentID != null) {
                    RequestNextPaymentInfo requestNextPaymentInfo = new RequestNextPaymentController().getRequestNextPaymentByRequestNextPaymentID(
                            BHPreference.organizationCode(), data.requestNextPaymentID);
                    if (requestNextPaymentInfo != null) {
                        requestNextPaymentInfo.PaymentID = input.PaymentID;
                        requestNextPaymentInfo.Status = RequestNextPaymentController.RequestNextPaymentStatus.COMPLETED.toString();
                        requestNextPaymentInfo.EffectiveBy = BHPreference.employeeID();
                        requestNextPaymentInfo.EffectiveDate = new Date();
                        requestNextPaymentInfo.LastUpdateBy = BHPreference.employeeID();
                        requestNextPaymentInfo.LastUpdateDate = new Date();
                        requestNextPaymentInfo.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                        TSRController.updateRequestNextPayment(requestNextPaymentInfo, true);
                    }
                }
            }

            @Override
            protected void after() {
                /*SaleReceiptFirstPayment.Data receiptData = new SaleReceiptFirstPayment.Data();
                receiptData.refNo = data.refNo;
                receiptData.processType = data.processType;
                receiptData.contract = null;
                receiptData.payment = input;
                receiptData.bank = data.bank;
                SaleReceiptFirstPayment fm = BHFragment.newInstance(SaleReceiptFirstPayment.class, receiptData);
                showNextView(fm);*/

                //BHPreference.setProcessType(data.processType.toString());
                //BHPreference.setRefNo(data.refNo);







               /* SaleReceiptPayment.Data dataReceiptID = new SaleReceiptPayment.Data();
                dataReceiptID.PaymentID = input.PaymentID;
                dataReceiptID.selectedDate = data.selectedDate;
                SaleReceiptPayment fmReceipt = BHFragment.newInstance(SaleReceiptPayment.class, dataReceiptID);
                fmReceipt.forcePrint = true;
                showNextView(fmReceipt);
*/

                SaleReceiptPayment_old.Data dataReceiptID = new SaleReceiptPayment_old.Data();
                dataReceiptID.PaymentID = input.PaymentID;
                dataReceiptID.selectedDate = data.selectedDate;
                SaleReceiptPayment_old fmReceipt = BHFragment.newInstance(SaleReceiptPayment_old.class, dataReceiptID);
                fmReceipt.forcePrint = true;
                showNextView(fmReceipt);


            }
        }.start();
    }

    private LinearLayout getLinearLayoutPartlyPaid(String PartlyPaid, String PartlyPaidPrice) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 0);
        layout.setLayoutParams(layoutParams);
        //layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView txtPartlyPaid = new TextView(activity);
        txtPartlyPaid.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
        txtPartlyPaid.setGravity(Gravity.TOP | Gravity.LEFT);
        //txtPartlyPaid.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22f);
        txtPartlyPaid.setTextAppearance(activity, R.style.TextView_Value);
        txtPartlyPaid.setTypeface(null, Typeface.BOLD);
        txtPartlyPaid.setText(PartlyPaid);
        layout.addView(txtPartlyPaid);

        TextView txtPartlyPaidPrice = new TextView(activity);
        txtPartlyPaidPrice.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f));
        txtPartlyPaidPrice.setGravity(Gravity.TOP | Gravity.LEFT);
        //txtPartlyPaidPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23f);
        txtPartlyPaidPrice.setTextAppearance(activity, R.style.TextView_Value2);
        txtPartlyPaidPrice.setTypeface(null, Typeface.BOLD);
        txtPartlyPaidPrice.setTextColor(Color.RED);
        txtPartlyPaidPrice.setText(PartlyPaidPrice);
        layout.addView(txtPartlyPaidPrice);

        TextView txtBahtPartlyPaid = new TextView(activity);
        txtBahtPartlyPaid.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f));
        txtBahtPartlyPaid.setGravity(Gravity.TOP | Gravity.LEFT);
        //txtBahtPartlyPaid.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22f);
        txtBahtPartlyPaid.setTextAppearance(activity, R.style.TextView_Value);
        txtBahtPartlyPaid.setText("บาท");
        layout.addView(txtBahtPartlyPaid);

        return layout;
    }

    @Override
    public void onStart() {
        try {
            super.onStart();
        }
        catch (Exception ex){

        }

        currentLoc.connectGoogleApi();
    }

    @Override
    public void onStop() {
        super.onStop();
        currentLoc.disConnectGoogleApi();
    }

    private void save_gps(String ReceiptID,String EmpID,String Latitude,String Longitude) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.getgps(ReceiptID,EmpID,Latitude,Longitude);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                }
                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });

        } catch (Exception e) {

        }
    }

    /**
     * Edit by Teerayut Klinsanga
     *
     * 29/09/2020
     */
    private boolean lastPeriod = false;
    private void checklastperiod() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = null;
            if (BHGeneral.SERVICE_MODE.toString() == "UAT") {
                call = request.getLastPeriodUAT(data.contract.CONTNO);
            } else if (BHGeneral.SERVICE_MODE.toString() == "PRODUCTION") {
                call = request.getLastPeriod(data.contract.CONTNO);
            }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson=new Gson();
                    try {
                        JSONObject jsonObject=new JSONObject(gson.toJson(response.body()));
                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONObject obj = null;
                        Log.e("Last period", String.valueOf(array));
                        for (int i = 0; i < array.length(); i++) {
                            obj = array.getJSONObject(i);
                            lastPeriod = obj.getBoolean("Status");
                        }

                        if (lastPeriod) {
                            List<Integer> listId = new ArrayList<Integer>();
                            listId.add(R.string.button_confirm_print);
                            activity.setViewProcessButtons(listId, View.GONE);

                            List<Integer> list = new ArrayList<Integer>();
                            list.add(R.string.button_update_customer_phone);
                            activity.setViewProcessButtons(list, View.VISIBLE);
                        } else {
                            List<Integer> listId = new ArrayList<Integer>();
                            listId.add(R.string.button_confirm_print);
                            activity.setViewProcessButtons(listId, View.VISIBLE);

                            List<Integer> list = new ArrayList<Integer>();
                            list.add(R.string.button_update_customer_phone);
                            activity.setViewProcessButtons(list, View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });

        } catch (Exception e) {

        }
    }

    private void updateCustomerPhone(String phone) {
        if (lastPeriod && phone.isEmpty()) {
            AlertDialog.Builder setupAlert;
            setupAlert = new AlertDialog.Builder(activity)
                    .setTitle("แจ้งเตือน")
                    .setMessage("กรุณาระบุเบอร์โทรลูกค้า")
                    .setCancelable(false)
                    .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                            showDialogUpdate();
                        }
                    });
            setupAlert.show();
        } else {
            if (isValidMobile(phone)) {
                BHLoading.show(activity);
                try {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Service request = retrofit.create(Service.class);
                    Call call = null;
                    if (BHGeneral.SERVICE_MODE.toString() == "UAT") {
                        call = request.updateCustomerPhoneUAT(phone, data.contract.RefNo, BHPreference.employeeID());
                    } else {
                        call = request.updateCustomerPhone(phone, data.contract.RefNo, BHPreference.employeeID());
                    }

                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, retrofit2.Response response) {
                            Gson gson=new Gson();
                            try {
                                JSONObject jsonObject=new JSONObject(gson.toJson(response.body()));
                                JSONArray array = jsonObject.getJSONArray("data");
                                Log.e("Update", String.valueOf(array));
                                JSONObject obj = null;
                                String status = array.getJSONObject(0).getString("StatusUpdate");
//                                for (int i = 0; i < array.length(); i++) {
//                                    obj = array.getJSONObject(i);
//                                    status = obj.getString("StatusUpdate");
//                                }

                                Log.e("Update status", status);
                                if (status.equals("SUCCESS")) {
                                    lastPeriod = false;
                                    hideKeyboard(getActivity());
                                    List<Integer> listId = new ArrayList<Integer>();
                                    listId.add(R.string.button_confirm_print);
                                    activity.setViewProcessButtons(listId, View.VISIBLE);

                                    List<Integer> list = new ArrayList<Integer>();
                                    list.add(R.string.button_update_customer_phone);
                                    activity.setViewProcessButtons(list, View.GONE);
                                } else {
                                    AlertDialog.Builder setupAlert;
                                    setupAlert = new AlertDialog.Builder(activity)
                                            .setTitle("แจ้งเตือน")
                                            .setMessage("เกิดข้อผิดพลาดในขั้นตอนการอัพเดท\nกรุณาติดต่อไอที")
                                            .setCancelable(false)
                                            .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    dialog.cancel();
                                                }
                                            });
                                    setupAlert.show();
                                }

                                hideKeyboard(getActivity());
                                BHLoading.close();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                BHLoading.close();
                                hideKeyboard(getActivity());
                                List<Integer> listId = new ArrayList<Integer>();
                                listId.add(R.string.button_confirm_print);
                                activity.setViewProcessButtons(listId, View.VISIBLE);
                            }
                        }
                        @Override
                        public void onFailure(Call call, Throwable t) {
                            BHLoading.close();
                            Log.e("Update", String.valueOf(t.getLocalizedMessage()));
                        }
                    });

                } catch (Exception e) {
                    BHLoading.close();
                    Log.e("Update", e.getLocalizedMessage());
                }
            } else {
                AlertDialog.Builder setupAlert;
                setupAlert = new AlertDialog.Builder(activity)
                        .setTitle("แจ้งเตือน")
                        .setMessage("เบอร์โทรศัพท์ไม่ถูกต้อง")
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                showDialogUpdate();
                            }
                        });
                setupAlert.show();
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private boolean isValidMobile(String phone) {
        String reg = "^[0]{1}+[0-9]{4}+[0-9]{5}";
        if (phone.matches(reg)) {
            return true;
        } else {
            return false;
        }
    }

    private void getQrcodeReceipt() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.getLastPeriod(data.contract.CONTNO);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson=new Gson();
                    try {
                        JSONObject jsonObject=new JSONObject(gson.toJson(response.body()));
                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONObject obj = null;
                        for (int i = 0; i < array.length(); i++) {
                            obj = array.getJSONObject(i);
//                            lastPeriod = obj.getBoolean("Status");
                        }

//                        if (lastPeriod) {
//                            List<Integer> listId = new ArrayList<Integer>();
//                            listId.add(R.string.button_confirm_print);
//                            activity.setViewProcessButtons(listId, View.GONE);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });

        } catch (Exception e) {

        }
    }
    /**
     * End
     */
}
