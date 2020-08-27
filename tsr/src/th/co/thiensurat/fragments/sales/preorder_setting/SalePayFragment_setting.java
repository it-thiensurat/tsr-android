package th.co.thiensurat.fragments.sales.preorder_setting;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.TradeInBrandController;
import th.co.thiensurat.data.controller.TripController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DiscountLimitInfo;
import th.co.thiensurat.data.info.PackagePeriodDetailInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.TradeInBrandInfo;
import th.co.thiensurat.data.info.TripInfo;

import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;

import static th.co.thiensurat.business.controller.TSRController.addSalePaymentPeriod;
import static th.co.thiensurat.business.controller.TSRController.updateContract;

public class SalePayFragment_setting extends BHFragment {

    private String STATUS_CODE = "05";
    public static String FRAGMENT_SALE_PAY_TAG = "sale_pay_tag";
    public static final String SALEPAY_TRADE_LIST = "th.co.thiensurat.salepay.traedinbrand";
    private ContractInfo Contract;
    private int HasTradein = 0;
    private String TradeInBrandCode;
    private float discount;
    private float sumPrice;

    private float limitCredit;
    private float limitCash;

    public static class Data extends BHParcelable {

        // For ChangeContract
        public String selectedCauseName;
        public AddressInfo addressIDCard;
        public AddressInfo addressInstall;
        public AddressInfo addressPayment;
        public String productName;
        public String customerFullName;
        public String idCard;
        public ChangeContractInfo chgContractRequest;
        public ChangeContractInfo chgContractApprove;
        public ChangeContractInfo chgContractAction;
        public ContractInfo oldContract;
        public ContractInfo newContract;
        public AssignInfo assign;
        public List<SalePaymentPeriodInfo> newSPPList;
        public List<PaymentInfo> newPayment;
    }

    private Data data;

    @InjectView
    EditText editTextPrice;
    @InjectView
    EditText editTextBrand;
    @InjectView
    EditText editTextBrandNumber;
    @InjectView
    EditText editTextSumPrice;
    @InjectView
    EditText editTextDiscountChange;
    @InjectView
    LinearLayout linearLayoutProductChange;
    @InjectView
    TextView textDetail;
    @InjectView
    TextView textBrand;
    @InjectView
    TextView textModel;
    @InjectView
    TextView textNumber;
    @InjectView
    Spinner spinnerBrand;
    @InjectView
    Spinner spinnerChange;
    @InjectView
    LinearLayout linearLayoutDistCount;

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
    private ImageButton ibScanBarcode;


    private List<TradeInBrandInfo> TradeInBrandList = null;
    private List<PackagePeriodDetailInfo> pkgPeriod = new ArrayList<PackagePeriodDetailInfo>();
    private List<SalePaymentPeriodInfo> outputSPP = new ArrayList<SalePaymentPeriodInfo>();

    private static final int REQUEST_QR_SCAN = 2469;

    int check_scan=0;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        if(BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())){
            return R.string.title_change_contract;
        } else {
            return R.string.title_sales;
        }
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_pay;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
            Log.e("page5","1111");
            return new int[]{R.string.button_back, R.string.button_next};
        } else if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            Log.e("page5","2222");
            return new int[]{R.string.button_back, R.string.button_detail_payment};
        }
        else {
            return new int[]{R.string.button_back, R.string.button_detail_payment};

        //    return new int[]{R.string.button_back, R.string.button_next};            //Log.e("page5","3333");
        }
        //return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        editTextDiscountChange.setText("0");

        if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            txtNumber1.setText("...");
            txtNumber2.setText("5");
            txtNumber3.setText("6");
            txtNumber4.setText("7");
            txtNumber5.setText("...");
            txtNumber2.setBackgroundResource(R.drawable.circle_number_sale_color_red);
            linearLayoutHeadNumber.setVisibility(View.VISIBLE);
        } else {
           // linearLayoutHeadNumber.setVisibility(View.GONE);

            txtNumber1.setText("...");
            txtNumber2.setText("5");
            txtNumber3.setText("6");
            txtNumber4.setText("7");
            txtNumber5.setText("...");
            txtNumber2.setBackgroundResource(R.drawable.circle_number_sale_color_red);
            linearLayoutHeadNumber.setVisibility(View.VISIBLE);

        }

        if (savedInstanceState != null) {
            TradeInBrandList = savedInstanceState.getParcelableArrayList(SALEPAY_TRADE_LIST);
        }

        if (!BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
            saveStatusCode();
        }
        else {
            saveStatusCode();
        }

        if (TradeInBrandList == null) {
            DataBase();
        } else {
            bindTradeinbrand();
        }

        data = getData();
        if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
            ContactDB(BHPreference.RefNo());
        } else if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            Default();
            ContactDB(BHPreference.RefNo());
        }
        else {
            ContactDB(BHPreference.RefNo());

        }

        SpinnerTurn();
        SumPrice();

        DiscountChange_onFocus();




        ibScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

                    @Override
                    public void onSuccess(BHPermissions bhPermissions) {
                        Intent intent = new IntentIntegrator(activity).createScanIntent();
                        startActivityForResult(intent, REQUEST_QR_SCAN);
                    }

                    @Override
                    public void onNotSuccess(BHPermissions bhPermissions) {
                        bhPermissions.openAppSettings(getActivity());
                    }

                    @Override
                    public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
                        bhPermissions.showMessage(getActivity(), permissionType);
                    }

                }, BHPermissions.PermissionType.CAMERA);
                /*** [END] :: Permission ***/

            }
        });


    }

    private void saveStatusCode() {
        // TODO Auto-generated method stub
        TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(SALEPAY_TRADE_LIST, (ArrayList<TradeInBrandInfo>) TradeInBrandList);
        super.onSaveInstanceState(savedInstanceState);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_QR_SCAN) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showMessage("ยังไม่ได้ทำการสแกน กรุณาทำการสแกนอีกครั้ง");
            } else if (resultCode == Activity.RESULT_OK) {
                String barcode = intent.getStringExtra(Intents.Scan.RESULT);

                Log.e("barcode", barcode);

                editTextBrandNumber.setText(barcode);
                check_scan=1;


            }
        }
    }

    private void ContactDB(final String ref) {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
                    //Contract = (ContractInfo) data.oldContract.copy();
                    Contract = (ContractInfo) data.newContract.copy();

                    Log.e("hhh","1");
                } else {
                    Log.e("hhh","2");

                    Log.e("refref",ref);
                    Contract = getContract(ref);
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (Contract != null) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    // String ProductPrice = df.format(Contract.SALES);

                    editTextPrice.setText(BHUtilities.numericFormat(Contract.SALES));

                    String ProductDiscount = df.format(Contract.TradeInDiscount);
                    editTextDiscountChange.setText(ProductDiscount);

                    spinnerChange.setSelection(Contract.HasTradeIn);    // Contract.HasTradeIn = 0 = ไม่มีเครื่องเทิร์น
                    editTextBrandNumber.setText(Contract.TradeInProductCode);
                    editTextBrand.setText(Contract.TradeInProductModel);

//                    if ((Contract.MODE > 1) && (Contract.HasTradeIn == 0)) {
//                        editTextDiscountChange.setText("0");
//                        SumPrice();
//                    }
                    // Why?
                    if (Contract.MODE > 1) {
                        linearLayoutDistCount.setVisibility(View.GONE);
                    }

                    if (Contract.MODE == 1) {
                        linearLayoutDistCount.setVisibility(View.VISIBLE);
                    }

//                    String Brand = Contract.TradeInBrandName;
                    String Brand = null;

                    if (Contract.TradeInBrandCode != null) {
//                        if(BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
                        TradeInBrandInfo tradeInBrandCode = new TradeInBrandController().getTradeInBrandByTradeInBrandCode(Contract.TradeInBrandCode);
                        if (tradeInBrandCode != null && tradeInBrandCode.TradeInBrandName != null) {
                            Brand = tradeInBrandCode.TradeInBrandName;
                        }
//                        } else {
//                            if (Contract.TradeInBrandCode.equals("AQUATEK")) {
//                                Brand = "AQUATEK อาควอเท็ค";
//                            } else if (Contract.TradeInBrandCode.equals("GREENPLUS")) {
//                                Brand = "GREEN PLUS กรีนพลัส";
//                            } else if (Contract.TradeInBrandCode.equals("MAXCOOL")) {
//                                Brand = "MAXCOOL แม็คคูล";
//                            } else if (Contract.TradeInBrandCode.equals("PANASONIC")) {
//                                Brand = "PANASONIC พานาโซนิค";
//                            } else if (Contract.TradeInBrandCode.equals("SAFE")) {
//                                Brand = "SAFE เซฟ";
//                            } else if (Contract.TradeInBrandCode.equals("SHARP")) {
//                                Brand = "SHARP ชาร์ป";
//                            } else if (Contract.TradeInBrandCode.equals("STIEBEL")) {
//                                Brand = "STIEBEL ELTRON สตีเบล";
//                            } else if (Contract.TradeInBrandCode.equals("TREATTON")) {
//                                Brand = "TREATTON ทรีดตั้น";
//                            } else if (Contract.TradeInBrandCode.equals("UNIPURE")) {
//                                Brand = "UNI-PURE ยูนิเพียว";
//                            } else if (Contract.TradeInBrandCode.equals("UNITECH")) {
//                                Brand = "UNITECH ยูนิเทค";
//                            } else if (Contract.TradeInBrandCode.equals("Other")) {
//                                Brand = "อื่นๆ";
//                            }
//                        }
                    }
                    ArrayAdapter<String> ptypeAdapter = (ArrayAdapter) spinnerBrand.getAdapter();
                    int typePosition = ptypeAdapter.getPosition(Brand);
                    spinnerBrand.setSelection(typePosition);

                }
            }

        }).start();
    }

    private void Default() {
        // TODO Auto-generated method stub
        linearLayoutProductChange.setVisibility(View.GONE);
        editTextSumPrice.setEnabled(false);
        editTextPrice.setEnabled(false);
    }

    private void SpinnerTurn() {
        // TODO Auto-generated method stub

        List<String> pay_change = new ArrayList<String>();
        // <item>ไม่มีเครื่องเทิร์น</item>
        // <item>มีเครื่องเทิร์น</item>
        pay_change.add("ไม่มีเครื่องเทิร์น");
        pay_change.add("มีเครื่องเทิร์น");

        BHSpinnerAdapter<String> arraypay_change = new BHSpinnerAdapter<String>(activity, pay_change);
        spinnerChange.setAdapter(arraypay_change);

        spinnerChange.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String item = parent.getItemAtPosition(position).toString();
                if (parent.getChildCount() > 0)
                    ((TextView) parent.getChildAt(0)).setTextSize(20);

                if (item.equals("ไม่มีเครื่องเทิร์น")) {
                    //TradeInBrandCode = "";
                    HasTradein = 0;
                    linearLayoutProductChange.setVisibility(View.GONE);
                    linearLayoutDistCount.setVisibility(View.GONE);
                    editTextDiscountChange.setText("0");

                } else if (item.equals("มีเครื่องเทิร์น")) {
                    linearLayoutProductChange.setVisibility(View.VISIBLE);
                    linearLayoutDistCount.setVisibility(View.VISIBLE);
//                    editTextDiscountChange.setText("0");
                    HasTradein = 1;
                    if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
//                        TradeInBrandCode = spinnerBrand.getSelectedItem().toString();
                        TradeInBrandCode = data.newContract.TradeInBrandCode;
                    }
                }

                if (Contract != null && Contract.MODE == 1) {
                    linearLayoutDistCount.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void DataBase() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                TradeInBrandList = getTradeInBrand();
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (TradeInBrandList != null) {
                    bindTradeinbrand();
                }
            }

        }).start();
    }

    private void bindTradeinbrand() {
        // TODO Auto-generated method stub
        List<String> tradeinbrand = new ArrayList<String>();
        for (TradeInBrandInfo item : TradeInBrandList) {
            tradeinbrand.add(String.format(item.TradeInBrandName));
        }


        BHSpinnerAdapter<String> arraytradeinbrand = new BHSpinnerAdapter<String>(activity, tradeinbrand);
        spinnerBrand.setAdapter(arraytradeinbrand);
        spinnerBrand.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (HasTradein == 1)
                    TradeInBrandCode = TradeInBrandList.get(position).TradeInBrandCode;
                else
                    TradeInBrandCode = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void SumPrice() {
        // TODO Auto-generated method stub
        editTextPrice.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                // editTextSumPrice.setText(addNumber());
                editTextSumPrice.setText(BHUtilities.numericFormat(Float.parseFloat(addNumber())));
            }
        });
        editTextDiscountChange.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                // editTextDiscountChange.setOnTouchListener(new
                // View.OnTouchListener() {
                //
                // @Override
                // public boolean onTouch(View v, MotionEvent event) {
                // // TODO Auto-generated method stub
                // editTextDiscountChange.setText("");
                // return false;
                // }
                // });

                // editTextSumPrice.setText(addNumber());
                editTextSumPrice.setText(BHUtilities.numericFormat(Float.parseFloat(addNumber())));
            }
        });
    }

    private String addNumber() {
        // int number1;
        // int number2;
        float number1;
        float number2;
        if (editTextPrice.getText().toString() != "" && editTextPrice.getText().length() > 0) {
            // number1 = Integer.parseInt(editTextPrice.getText().toString());
            number1 = Float.parseFloat(editTextPrice.getText().toString().replace(",", ""));
        } else {
            number1 = 0;
        }
        if (editTextDiscountChange.getText().toString() != "" && editTextDiscountChange.getText().length() > 0) {
            // number2 =
            // Integer.parseInt(editTextDiscountChange.getText().toString());
            number2 = Float.parseFloat(editTextDiscountChange.getText().toString().replace(",", ""));
        } else {
            number2 = 0;
        }
        // return Integer.toString(number1 - number2);
        return Float.toString(number1 - number2);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_detail_payment:
                String stringprice = editTextPrice.getText().toString();
                String stringdiscount = editTextDiscountChange.getText().toString();
                String stringsumprice = editTextSumPrice.getText().toString();

                if (stringprice.length() != 0 && stringdiscount.length() != 0 && stringsumprice.length() != 0) {
                    if (GetSelectedPackagePeriodDetail()) {
                        saveSale();
                    }
                } else {
                    showMessage("กรุณากรอกข้อมมูลให้ครบทุกช่อง **ส่วนลดถ้าไม่มีให้ใส่ 0 บาท**");
                }
                break;
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_next:
                String stringprice1 = editTextPrice.getText().toString();
                String stringdiscount1 = editTextDiscountChange.getText().toString();
                String stringsumprice1 = editTextSumPrice.getText().toString();
                if (stringprice1.length() != 0 && stringdiscount1.length() != 0 && stringsumprice1.length() != 0) {
                //    Contract.MODE = pkgPeriod.size();
                    if (GetSelectedPackagePeriodDetail()) {
                        Contract.HasTradeIn = HasTradein;
                    //       Contract.MODE = pkgPeriod.size();


                        try {
                            Contract.CONTNO = data.newContract.CONTNO;
                            Contract.RefNo = data.newContract.RefNo;

                        }
                        catch (Exception ex){

                        }

                        if (Contract.HasTradeIn == 1) {

                            if(check_scan==1){
                                Contract.TradeInProductCode = editTextBrandNumber.getText().toString()+"_1";

                            }
                            else {
                                Contract.TradeInProductCode = editTextBrandNumber.getText().toString();

                            }

                            Contract.TradeInBrandCode = TradeInBrandList.get(spinnerBrand.getSelectedItemPosition()).TradeInBrandCode;
                            Contract.TradeInProductModel = editTextBrand.getText().toString();
                        } else {
                            Contract.TradeInProductCode = null;
                            Contract.TradeInBrandCode = null;
                            Contract.TradeInProductModel = null;
                        }

                        String stringDiscount = editTextDiscountChange.getText().toString().replace(",", "");
                        Contract.TradeInDiscount = Float.parseFloat(stringDiscount);// DISCOUNT;

                        String stringSumprice = editTextSumPrice.getText().toString().replace(",", "");
                        Contract.TotalPrice = Float.parseFloat(stringSumprice);

                        //ChangeContractResultFragment.Data data2 = new ChangeContractResultFragment.Data();
                        SaleDetailCheckFragment_setting.Data data2 = new SaleDetailCheckFragment_setting.Data();

                        Contract.ProductName = data.productName;
                        Contract.IDCard = data.idCard;
                        Contract.CustomerFullName = data.customerFullName;

                        data2.oldContract = data.oldContract;
                        data2.newContract = Contract;

                        if (BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString())) {

                            /**- ในการเปลี่ยนสัญญาของงานเก็บเงิน ต้องทำให้ยอดเงินที่จ่ายเงินมาแล้วของ งวดแรก ของ Package ของสัญญาใหม่ Completed อยู่เสมอ โดยถ้ายังคงมีคงเหลือที่ต้องจ่ายก็ให้ไปเป็นยอดเงินที่ต้องจ่ายในงวดที่ 2 แทน ยกตัวอย่างเช่น *** แต่ case นี้ จะติดปัญญาในเรื่องของการแสดงข้อมูล Contract ในส่วนที่ระบุว่า ยอดเงินตั้งแต่งวดที่ 2-n จ่ายเท่าไหร่? ***
                             ==================================================================================================================================================
                             ||    สัญญาเดิม 5 งวด ๆ ละ 800 (จ่ายมาแล้ว 800)             ||        สัญญาใหม่ 3 งวด ๆ ละ 1,000 บาท
                             ==================================================================================================================================================
                             งวดที่ 1   ||    ยอด 800 จ่าย Completed แล้ว 800                             ||       ปกติต้องเป็นยอด 1,000 แต่จากสัญญาเดิม จ่ายมาเพียง 800 จะส่งผลให้งวดที่แรกจ่ายไม่ Completed จึงต้องเปลี่ยนยอดที่ต้องจ่ายของงวดที่ 1 เป็น 800 จึงจะถือว่า Completed
                             งวดที่ 2   ||    ยอด 800                                                                ||        ปกติต้องเป็นยอด 1,000 จากงวดที่ 1 ถูกเปลี่ยนเป็น 800 ฉะนั้นต้องเอาส่วนต่างมาปรับเป็นยอดที่ต้องจ่ายของงวดที่แทน จึงกลายเป็นยอดที่ต้องจ่าย 1,000+200 = 1,200 บาท
                             งวดที่ 3   ||    ยอด 800                                                                ||        ยอด 1,000
                             งวดที่ 4   ||    ยอด 800                                                                ||        ยอด 1,000
                             งวดที่ 5   ||    ยอด 800                                                                ||        ยอด 1,000 **/

                            float sumPayment = 0; //ยอดรวมการจ่ายเงิน

                            if (data.newPayment != null) {
                                for (PaymentInfo info : data.newPayment) {
                                    sumPayment += info.PAYAMT;
                                }
                            }

                            float settlement = 0; // ส่วนต่าง

                            for (SalePaymentPeriodInfo item : data.newSPPList) {

                                if(item.PaymentPeriodNumber == 1){
                                    if(sumPayment < (item.PaymentAmount - Contract.TradeInDiscount)){
                                        settlement = item.PaymentAmount - (sumPayment + Contract.TradeInDiscount);
                                        item.PaymentAmount = sumPayment + Contract.TradeInDiscount;
                                        item.Discount = Contract.TradeInDiscount;
                                        item.NetAmount = item.PaymentAmount - Contract.TradeInDiscount;
                                    } else {
                                        item.Discount = Contract.TradeInDiscount;
                                        item.NetAmount = item.PaymentAmount - Contract.TradeInDiscount;
                                    }
                                }

                                /*if ((sumPayment < (item.PaymentAmount - Contract.TradeInDiscount)) && (item.PaymentPeriodNumber == 1)) {
                                    settlement = item.PaymentAmount - (sumPayment + Contract.TradeInDiscount);
                                    item.PaymentAmount = sumPayment + Contract.TradeInDiscount;
                                    item.Discount = Contract.TradeInDiscount;
                                    item.NetAmount = item.PaymentAmount - Contract.TradeInDiscount;
                                }*/

                                if (item.PaymentPeriodNumber == 2) {
                                    item.PaymentAmount += settlement;
                                    item.NetAmount = item.PaymentAmount - item.Discount;
                                }
                            }

                        } else {
                            for (int i = 0; i < data.newSPPList.size(); i++) {
                                if (data.newSPPList.get(i).PaymentPeriodNumber == 1) {
                                    data.newSPPList.get(i).Discount = Contract.TradeInDiscount;
                                    data.newSPPList.get(i).NetAmount = data.newSPPList.get(i).PaymentAmount - Contract.TradeInDiscount;
                                    //data.newSPPList.get(i).NetAmount -= data.newSPPList.get(i).Discount;
                                }
                            }
                        }

                        data2.newSPPList = data.newSPPList;
                        data2.newPayment = data.newPayment;
                        data2.selectedCauseName = data.selectedCauseName;
                        data2.newAddressIDCard = data.addressIDCard;
                        data2.newAddressInstall = data.addressInstall;
                        data2.newAddressPayment = data.addressPayment;
                        data2.chgContractRequest = data.chgContractRequest;
                        data2.chgContractApprove = data.chgContractApprove;
                        data2.chgContractAction = data.chgContractAction;
                        data2.assign = data.assign;

                        data2.changeContractType = "04";

                        /*ChangeContractResultFragment fmCCResult = BHFragment.newInstance(ChangeContractResultFragment.class, data2);
                        showNextView(fmCCResult);*/

                        SaleDetailCheckFragment_setting fm = BHFragment.newInstance(SaleDetailCheckFragment_setting.class, data2);
                        showNextView(fm);
                    }
                } else {
                    showMessage("กรุณากรอกข้อมมูลให้ครบทุกช่อง **ส่วนลดถ้าไม่มีให้ใส่ 0 บาท**");
                }
                break;
            default:
                break;
        }
    }

    private boolean GetSelectedPackagePeriodDetail() {
        discount = Float.parseFloat(editTextDiscountChange.getText().toString().replace(",", ""));
        DiscountLimitInfo discountLimitInfo = null;

        if (Contract.MODE == 1) {
            discountLimitInfo = TSRController.getDiscountLimitByTypeAndProductID("Cash", Contract.ProductID, discount);
        } else {
            discountLimitInfo = TSRController.getDiscountLimitByTypeAndProductID("Credit", Contract.ProductID, discount);
        }



        if ((Contract.MODE == 1) && ((spinnerChange.getSelectedItem().equals("ไม่มีเครื่องเทิร์น") && discount > 0 && discountLimitInfo == null)
                || (spinnerChange.getSelectedItem().equals("มีเครื่องเทิร์น") && discountLimitInfo == null))) {
            showNoticeDialogBox("บันทึกส่วนลด", "ไม่สามารถบันทึกข้อมูลได้ เนื่องจากเงินสดที่สามารถลดได้ไม่ตรงกับข้อมูล");
            return false;
        } else if ((Contract.MODE > 1) && spinnerChange.getSelectedItem().equals("มีเครื่องเทิร์น") && discountLimitInfo == null) {
            showNoticeDialogBox("บันทึกส่วนลด", "ไม่สามารถบันทึกข้อมูลได้ เนื่องจากเงินผ่อนที่สามารถลดได้ไม่ตรงกับข้อมูล");
            return false;
        }



        return true;
    }

    public void saveSale() {
        /*** [START] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/


        /*new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                Log.d("test", "saveSale calling");
                pkgPeriod = TSRController.getPackagePeriodDetail(Contract.OrganizationCode, Contract.MODEL);
                outputSPP = getSalePaymentPeriodByRefNo(Contract.RefNo);
            }

            @Override
            protected void after() {
                Log.d("test", "saveSale after");
                if (pkgPeriod != null) {
                    if (outputSPP != null) {
                        deleteSalePaymentPeriodByRefNo(Contract.RefNo, true);
                    }
                    AddSalePaymentPeriod(pkgPeriod);
                }
            }
        }.start();*/

        new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                pkgPeriod = TSRController.getPackagePeriodDetail(Contract.OrganizationCode, Contract.MODEL);
                outputSPP = getSalePaymentPeriodByRefNo(Contract.RefNo);

                if (pkgPeriod != null) {
                    if (outputSPP != null) {
                        deleteSalePaymentPeriodByRefNo(Contract.RefNo, true);
                    }
                    AddSalePaymentPeriod(pkgPeriod);
                    UpdateContractDB();
                }
            }

            @Override
            protected void after() {
                showNextView(new SaleDetailCheckFragment_setting());
            }
        }.start();

        /*** [END] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/
    }

    private void AddSalePaymentPeriod(final List<PackagePeriodDetailInfo> pkgListInputInfo) {

        /*** [START] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/

        /*// TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            SalePaymentPeriodInfo input = new SalePaymentPeriodInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method
                try {
                    for (PackagePeriodDetailInfo item : pkgListInputInfo) {
                        String stringDiscount = editTextDiscountChange.getText().toString().replace(",", "");
                        discount = Float.parseFloat(stringDiscount);
                        String stringSumprice = editTextSumPrice.getText().toString().replace(",", "");
                        sumPrice = Float.parseFloat(stringSumprice);

                        input.SalePaymentPeriodID = DatabaseHelper.getUUID();
                        input.RefNo = BHPreference.RefNo();
                        input.PaymentPeriodNumber = item.PaymentPeriodNumber;
                        input.PaymentAmount = item.PaymentAmount;
                        input.Discount = (item.PaymentPeriodNumber == 1) ? discount : 0;
                        input.NetAmount = (item.PaymentPeriodNumber == 1) ? (item.PaymentAmount - discount) : item.PaymentAmount;
                        input.PaymentComplete = false;
                        input.PaymentDueDate = BHUtilities.addMonth(new Date(), item.PaymentPeriodNumber);
                        input.PaymentAppointmentDate = BHUtilities.addMonth(new Date(), item.PaymentPeriodNumber);

                        // Fixed - [BHPROJ-0024-407]
                        TripInfo trip = new TripController().getCurrentTrip();
                        if (trip != null) {
                            input.TripID = trip.TripID;
                        }

                        input.CreateDate = new Date();
                        input.CreateBy = Contract.CreateBy;
                        input.LastUpdateDate = new Date();
                        input.LastUpdateBy = Contract.LastUpdateBy;
                        input.SyncedDate = new Date();

                        addSalePaymentPeriod(input, true);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                UpdateContractDB();
            }
        }).start();*/


        SalePaymentPeriodInfo input = new SalePaymentPeriodInfo();
        for (PackagePeriodDetailInfo item : pkgListInputInfo) {
            String stringDiscount = editTextDiscountChange.getText().toString().replace(",", "");
            discount = Float.parseFloat(stringDiscount);
            String stringSumprice = editTextSumPrice.getText().toString().replace(",", "");
            sumPrice = Float.parseFloat(stringSumprice);

            input.SalePaymentPeriodID = DatabaseHelper.getUUID();
            input.RefNo = BHPreference.RefNo();
            input.PaymentPeriodNumber = item.PaymentPeriodNumber;
            input.PaymentAmount = item.PaymentAmount;
            input.Discount = (item.PaymentPeriodNumber == 1) ? discount : 0;
            input.NetAmount = (item.PaymentPeriodNumber == 1) ? (item.PaymentAmount - discount) : item.PaymentAmount;
            input.PaymentComplete = false;
            input.PaymentDueDate = BHUtilities.addMonth(new Date(), item.PaymentPeriodNumber);
            input.PaymentAppointmentDate = BHUtilities.addMonth(new Date(), item.PaymentPeriodNumber);

            // Fixed - [BHPROJ-0024-407]
            TripInfo trip = new TripController().getCurrentTrip();
            if (trip != null) {
                input.TripID = trip.TripID;
            }

            input.CreateDate = new Date();
            input.CreateBy = Contract.CreateBy;
            input.LastUpdateDate = new Date();
            input.LastUpdateBy = Contract.LastUpdateBy;
            input.SyncedDate = new Date();

            addSalePaymentPeriod(input, true);
        }

        /*** [END] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/
    }

    private void UpdateContractDB() {

        /*** [START] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/

        /*// TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void before() {
                // TODO Auto-generated method stub
                Contract.RefNo = BHPreference.RefNo();
                Contract.HasTradeIn = HasTradein;
                if (HasTradein == 1) {
                    Contract.TradeInReturnFlag = false;     //Contract.HasTradeIn = 1 = มีเครื่องเทิร์น
                    Contract.TradeInProductCode = editTextBrandNumber.getText().toString();
                    Contract.TradeInBrandCode = TradeInBrandList.get(spinnerBrand.getSelectedItemPosition()).TradeInBrandCode;
                    Contract.TradeInProductModel = editTextBrand.getText().toString();

                } else {
                    Contract.TradeInProductCode = null;
                    Contract.TradeInBrandCode = null;
                    Contract.TradeInProductModel = null;
                }
                Contract.TotalPrice = sumPrice;
                Contract.TradeInDiscount = discount;
                Contract.MODE = pkgPeriod.size();
            }

            @Override
            protected void calling() {
                updateContract(Contract, true);
            }

            @Override
            protected void after() {
                showNextView(new SaleDetailCheckFragment());
            }
        }).start();*/


        Contract.RefNo = BHPreference.RefNo();
        Contract.HasTradeIn = HasTradein;
        if (HasTradein == 1) {
            Contract.TradeInReturnFlag = false;     //Contract.HasTradeIn = 1 = มีเครื่องเทิร์น

            if(check_scan==1){
                Contract.TradeInProductCode = editTextBrandNumber.getText().toString()+"_1";

            }
            else {
                Contract.TradeInProductCode = editTextBrandNumber.getText().toString();

            }

          //  Contract.TradeInProductCode = editTextBrandNumber.getText().toString();
            Contract.TradeInBrandCode = TradeInBrandList.get(spinnerBrand.getSelectedItemPosition()).TradeInBrandCode;
            Contract.TradeInProductModel = editTextBrand.getText().toString();

        } else {
            Contract.TradeInProductCode = null;
            Contract.TradeInBrandCode = null;
            Contract.TradeInProductModel = null;
        }
        Contract.TotalPrice = sumPrice;
        Contract.TradeInDiscount = discount;
        Contract.MODE = pkgPeriod.size();

        updateContract(Contract, true);

        /*** [END] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/
    }

    private void DiscountChange_onFocus() {
        editTextDiscountChange.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                String strDiscount = editTextDiscountChange.getText().toString().replace(",", "");
                Float discount;
                if (hasFocus == true) {
                    if (!strDiscount.equals("")) {
                        discount = Float.parseFloat(strDiscount);
                        if (discount <= 0)
                            editTextDiscountChange.setText("");
                        else
                            editTextDiscountChange.setText(BHUtilities.numericFormat(discount));
                    }
                } else {
                    if (strDiscount.equals(""))
                        editTextDiscountChange.setText("0");
                    else {
                        discount = Float.parseFloat(strDiscount);
                        editTextDiscountChange.setText(BHUtilities.numericFormat(discount));
                    }
                }

            }
        });
    }

    private void showNoticeDialogBox(final String title, final String message) {
        Builder setupAlert;
        setupAlert = new Builder(activity);
        setupAlert.setTitle(title);
        setupAlert.setMessage(message);
        setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //???
            }
        });
        setupAlert.show();
    }


}