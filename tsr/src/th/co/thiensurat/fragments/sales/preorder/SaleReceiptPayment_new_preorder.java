package th.co.thiensurat.fragments.sales.preorder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.PrinterController;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.Get_data_online;
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.controller.PaymentController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.fragments.credit.credit.CreditMainFragment_intro;
import th.co.thiensurat.fragments.document.manual.ManualDocumentDetailFragment;
import th.co.thiensurat.fragments.payment.first.FirstPaymentMainMenuFragment;
import th.co.thiensurat.fragments.payment.next.NextPaymentListFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.sales.SalePhotographyFragment;
import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;

//import th.co.thiensurat.data.info.GET_data_payment_online;
//import th.co.thiensurat.retrofit.api.SQLiteHelper;

public class SaleReceiptPayment_new_preorder extends BHFragment {

    public boolean forcePrint;

    public SaleReceiptPayment_new_preorder() {
        forcePrint = false;
    }

    private final String STATUS_CODE = "09";

    private String Contno = "";


    Get_data_online getData_cedit;
    List<Get_data_online> GetDataAdapter1;


    public static class Data extends BHParcelable {
        public String SalePaymentPeriodID;
        public String PaymentID;
        public String ReceiptID;
        public int resTitle;
        public Date selectedDate;
        public String contno;
    }

    private Data data;

    private List<PaymentInfo> payments;


//        List<GET_data_payment_online> get_data_payment_onlines;
//    GET_data_payment_online get_data_payment_online;
    Cursor cursor;
    @InjectView
    public ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    @InjectView
    public LinearLayout viewPagerCountDots;
    private int dotsCount;
    private TextView[] dots;
    private int currentViewPosition = 0;
    int xx = 0;

    SQLiteDatabase sqLiteDatabase;

    TextView tvReceiptNo;

    @InjectView
    public ViewTitle lblTitle;

    @Override
    protected int titleID() {
        data = getData();
        int titleID1 = 0;
        switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
            case ViewCompletedContract:
                titleID1 = (data != null && data.resTitle != 0) ? data.resTitle : R.string.title_sales;
                break;
            case Sale:
                titleID1 = R.string.title_sales;
                break;
            case FirstPayment:
                titleID1 = R.string.title_payment_first;
                break;
            case NextPayment:
                titleID1 = R.string.title_payment_next;
                break;
            case SendDocument:
                titleID1 = R.string.title_document_main;
                break;
            case Credit:
                titleID1 = R.string.title_main_credit;
                break;
        }

        return titleID1;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_receipt_payment;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        int[] ret = null;

        switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
            case Sale:
                ret = new int[]{R.string.button_print, R.string.button_save_manual_receipt, R.string.button_camera};
                break;
            case ViewCompletedContract:
            case SendDocument:
                ret = new int[]{R.string.button_back, R.string.button_save_manual_receipt, R.string.button_print};
                break;
            case FirstPayment:
            case NextPayment:
            case Credit:
                ret = new int[]{R.string.button_print, R.string.button_save_manual_receipt, R.string.button_end};
                break;
        }
        return ret;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();
        switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
            case FirstPayment:
                lblTitle.setText(R.string.caption_payment_first);
                break;
            case Sale:
                lblTitle.setText(R.string.caption_payment_first);
                saveStatusCode();
                break;
            case ViewCompletedContract:
            case SendDocument:
                lblTitle.setText(R.string.label_payment);
                break;
            case NextPayment:
                lblTitle.setText(R.string.title_request_next_payment);
                break;
            case Credit:
                lblTitle.setText(R.string.title_next_payment_credit);
                break;
        }

//        get_data_payment_onlines = new ArrayList<>();

        GetDataAdapter1 = new ArrayList<>();

        initViews();
        try {
            Contno = data.contno;
        } catch (NullPointerException ex) {

        }
    }

    private void saveStatusCode() {
        if (BHPreference.RefNo() != null) {
            TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
        } else {
            showMessage("ref is null");
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_camera:
                showNextView(new SalePhotographyFragment());
//                printDocument(payments);
                break;
            case R.string.button_print:
                if (viewPager.getChildCount() > 1) {
                    final List<PaymentInfo> newPayments = new ArrayList<PaymentInfo>();

                    for (PaymentInfo info : payments) {
                        if (info.EmpID.equals(BHPreference.employeeID())) {
                            newPayments.add(info);
                        }
                    }

                    if (newPayments.size() > 1 && forcePrint == false) {
                        AlertDialog.Builder setupAlert;
                        /*** [START] :: Fixed - [BHPROJ-0026-3275] :: [Android-พิมพ์ใบเสร็จ] การพิมพ์ใบเสร็จกรณีเก็บเงินพร้อมกันหลายงวด  ***/
                        setupAlert = new AlertDialog.Builder(activity)
                                .setTitle("พิมพ์ใบเสร็จ")
                                .setMessage("ยืนยันการพิมพ์ใบเสร็จ")
                                .setCancelable(false);

                        final SaleFirstPaymentChoiceFragment.ProcessType processType = Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType());
                        if (processType.equals(SaleFirstPaymentChoiceFragment.ProcessType.Credit) || processType.equals(SaleFirstPaymentChoiceFragment.ProcessType.FirstPayment) ||
                                processType.equals(SaleFirstPaymentChoiceFragment.ProcessType.NextPayment) || processType.equals(SaleFirstPaymentChoiceFragment.ProcessType.Sale)) {
                            setupAlert = setupAlert.setPositiveButton("พิมพ์ทั้งหมด", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    printDocument(newPayments);
                                    dialog.cancel();
                                }
                            });
//                            .setNeutralButton("พิมพ์ทีละหน้า", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    printDocumentWithInterrupt(newPayments);
//                                    dialog.cancel();
//                                }
//                            });
                        } else {
                            setupAlert = setupAlert.setPositiveButton("พิมพ์ทั้งหมด", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    printDocument(newPayments);
                                    dialog.cancel();
                                }
                            }).setNeutralButton(String.format("พิมพ์เฉพาะหน้านี้", viewPager.getCurrentItem() + 1), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    List<PaymentInfo> listInfo = new ArrayList<>();
                                    listInfo.add(payments.get(viewPager.getCurrentItem()));
                                    printDocument(listInfo);
                                    dialog.cancel();
                                }
                            });
                        }


                        setupAlert = setupAlert.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });


                        setupAlert.show();
                        /*** [END] :: Fixed - [Android-พิมพ์ใบเสร็จ] การพิมพ์ใบเสร็จกรณีเก็บเงินพร้อมกันหลายงวด  ***/


                    } else if (newPayments.size() == 1 || forcePrint == true) {
                        printDocument(newPayments);
                    }
                } else {
                    printDocument(payments);
                }
                break;
            case R.string.button_save_manual_receipt:
                ManualDocumentDetailFragment.Data data1 = new ManualDocumentDetailFragment.Data();
                data1.DocumentNumber = payments.get(viewPager.getCurrentItem()).ReceiptID;
                data1.DocumentNo = payments.get(viewPager.getCurrentItem()).ReceiptCode;
                data1.DocumentType = DocumentHistoryController.DocumentType.Receipt.toString();

                ManualDocumentDetailFragment fmManualDocDetail = BHFragment.newInstance(ManualDocumentDetailFragment.class, data1);
                showNextView(fmManualDocDetail);
                break;

            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_end:
                if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.FirstPayment.toString())) {
                    activity.showView(new FirstPaymentMainMenuFragment());
                } else if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.Credit.toString())) {
                    //activity.showView(new CreditMainFragment());




/*                    CreditListFragment.Data input = new CreditListFragment.Data();
                    input.selectedDate = data.selectedDate;
                    CreditListFragment fragment = BHFragment.newInstance(CreditListFragment.class, input);
                    activity.showView(fragment);*/

                    activity.showNextView(BHFragment.newInstance(CreditMainFragment_intro.class));




                } else if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.NextPayment.toString())) {
                    activity.showView(new NextPaymentListFragment());
                }
                break;

            default:
                break;
        }
    }

    private void printDocument(List<PaymentInfo> info) {
        if (info == null) return;
        new PrinterController(activity).newPrintReceipt_preorder(info);
        forcePrint = false;
    }

    private void printDocumentWithInterrupt(List<PaymentInfo> info) {
        if (info == null) return;
        new PrinterController(activity).newPrintReceipt(info, true);
    }

    private void setUiPageViewController() {
        viewPagerCountDots.removeAllViews();
        dotsCount = myViewPagerAdapter.getCount();
        dots = new TextView[dotsCount];

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        //params.setMargins(2, 0, 1, 0);

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(activity);
            dots[i].setText(Html.fromHtml("&#149;"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(getResources().getColor(R.color.dot_gray_dark));
            dots[i].setLayoutParams(params);
            viewPagerCountDots.addView(dots[i]);
        }

        dots[currentViewPosition].setTextColor(getResources().getColor(R.color.dot_red));
    }

    private void setViewPagerItemsWithAdapter() {

        /*** [START] :: Fixed - [BHPROJ-0025-815] :: [Android-Reprint ใบสัญญา+ใบเสร็จ] กรณีเป็นฝ่ายเก็บเงินจะ Re-Print ได้เฉพาะใบเสร็จรับเงินที่เค้าเป็นคนเก็บเท่านั้น จะไม่สามารถกลับไป Re-Print ใบสัญญา หรือ ใบเสร็จรับเงินของคนอื่นได้  ***/
        List<Integer> listId = new ArrayList<Integer>();
        listId.add(R.string.button_print);
        listId.add(R.string.button_save_manual_receipt);

        if (!payments.get(0).EmpID.equals(BHPreference.employeeID()) || payments.get(0).VoidStatus == true) {
            activity.setViewProcessButtons(listId, View.GONE);
        } else {
            activity.setViewProcessButtons(listId, View.VISIBLE);
        }
        /*** [END] :: Fixed - [BHPROJ-0025-815] :: [Android-Reprint ใบสัญญา+ใบเสร็จ] กรณีเป็นฝ่ายเก็บเงินจะ Re-Print ได้เฉพาะใบเสร็จรับเงินที่เค้าเป็นคนเก็บเท่านั้น จะไม่สามารถกลับไป Re-Print ใบสัญญา หรือ ใบเสร็จรับเงินของคนอื่นได้  ***/


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String currentDate = df.format(c.getTime());

        try {
            String ReceiptDate = df.format(payments.get(0).PayDate);

            if (!ReceiptDate.equals(currentDate)) {
                activity.setViewProcessButtons(listId, View.GONE);
            }
        } catch (Exception ex) {

        }

//        myViewPagerAdapter.setNewItem(payments);
        myViewPagerAdapter = new MyViewPagerAdapter(payments);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(currentViewPosition);
        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void doVoidReceipt(String RefNo, String ReceiptID) {

        /*TSRController.voidReceipt(RefNo, ReceiptID, BHPreference.employeeID(), true);
        viewPager.removeAllViews();
        initViews();*/

        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                TSRController.voidReceipt(RefNo, ReceiptID, BHPreference.employeeID(), true);
            }

            @Override
            protected void after() {
                viewPager.removeAllViews();
                initViews();
            }
        }.start();
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setTextColor(getResources().getColor(R.color.dot_gray_dark));
            }
            dots[position].setTextColor(getResources().getColor(R.color.dot_red));

            /*** [START] :: Fixed - [BHPROJ-0025-815] :: [Android-Reprint ใบสัญญา+ใบเสร็จ] กรณีเป็นฝ่ายเก็บเงินจะ Re-Print ได้เฉพาะใบเสร็จรับเงินที่เค้าเป็นคนเก็บเท่านั้น จะไม่สามารถกลับไป Re-Print ใบสัญญา หรือ ใบเสร็จรับเงินของคนอื่นได้  ***/
            List<Integer> listId = new ArrayList<Integer>();
            listId.add(R.string.button_print);
            listId.add(R.string.button_save_manual_receipt);

            try {
                if (!payments.get(position).EmpID.equals(BHPreference.employeeID()) || payments.get(position).VoidStatus == true) {
                    activity.setViewProcessButtons(listId, View.GONE);
                } else {
                    activity.setViewProcessButtons(listId, View.VISIBLE);
                }
                /*** [END] :: Fixed - [BHPROJ-0025-815] :: [Android-Reprint ใบสัญญา+ใบเสร็จ] กรณีเป็นฝ่ายเก็บเงินจะ Re-Print ได้เฉพาะใบเสร็จรับเงินที่เค้าเป็นคนเก็บเท่านั้น จะไม่สามารถกลับไป Re-Print ใบสัญญา หรือ ใบเสร็จรับเงินของคนอื่นได้  ***/
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String currentDate = df.format(c.getTime());

                String ReceiptDate = df.format(payments.get(position).PayDate);

                if (!ReceiptDate.equals(currentDate)) {
                    activity.setViewProcessButtons(listId, View.GONE);
                }
            } catch (Exception ex) {

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void prepareReceiptForVoid() {
        try {
            /*** [START] :: ตรวจสอการยกเลิกใบเสร็จของการตัดสด เนื่องจากการยกเลิกใบเสร็จจะทำการลข้อมูลของการตัดสดทำให้ไมสามารถตรวจได้ว่าใบเสร็จนั้นมาจากตัดสด ทำให้ใบเสร็จแสดงหลายรายการ ***/
            String tempReceiptCode = null;
            List<PaymentInfo> newListPayment = new ArrayList<>();
            for (int i = 0; i < payments.size(); i++) {
                PaymentInfo p = payments.get(i);

                if (!p.ReceiptCode.equals(tempReceiptCode)) {
                    tempReceiptCode = p.ReceiptCode;
                    newListPayment.add(p);
                }
            }
            payments = newListPayment;
            /*** [END] :: ตรวจสอการยกเลิกใบเสร็จของการตัดสด เนื่องจากการยกเลิกใบเสร็จจะทำการลข้อมูลของการตัดสดทำให้ไมสามารถตรวจได้ว่าใบเสร็จนั้นมาจากตัดสด ทำให้ใบเสร็จแสดงหลายรายการ ***/


            Boolean voidStamp = false;

            for (int i = payments.size(); i > 0; i--) {
                payments.get(i - 1).CanVoid = false;
                payments.get(i - 1).VoidStatus = false;

                if (!voidStamp
                        && (payments.get(i - 1).Amount > 0 || payments.get(i - 1).PaymentID.equals(payments.get(i - 1).CloseAccountPaymentID))
                        && payments.get(i - 1).PaymentID.length() > 20 && payments.get(i - 1).CreateBy.equals(BHPreference.employeeID())) {
                    payments.get(i - 1).CanVoid = true;
                    voidStamp = true;
                }

                if (payments.get(i - 1).Amount == 0 && !payments.get(i - 1).PaymentID.equals(payments.get(i - 1).CloseAccountPaymentID)) {
                    payments.get(i - 1).VoidStatus = true;
                }

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String currentDate = df.format(c.getTime());

                String ReceiptDate = df.format(payments.get(i - 1).PayDate);

                if (!ReceiptDate.equals(currentDate)) {
                    payments.get(i - 1).CanVoid = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        Log.e("test_page","okok");
        /**
         RefNo                      ของสัญญานั้น
         SalePaymentPeriodID        ID ของงวดการชำระเงิน
         PaymentID                  ID ของการจ่ายเงิน
         ReceiptID                  ID ของการออกใบเสร็จ

         PayDate                    วันที่จ่ายเงิน
         ReceiptCode                เลขที่ใบเสร็จ
         CONTNO                     เลขที่สัญญา

         ProductName                ชื่อสินค้า
         MODEL                      รุ่ย
         ProductSerialNumber        รหัสสินค้า

         CloseAccountDiscountAmount ส่วนลดการตัดสด

         PaymentPeriodNumber        งวดที่
         MODE                       จำนวนงวดทั้งหมด
         BalancesOfPeriod           ยอดเงินคงเหลือของงวด
         Amount                     ยอดเงินที่ชำระตามใบเสร็จ
         Balances                   ยอดเงินคงเหลือของสัญญา

         PAYAMT                     จำนวนเงินที่จ่าย
         NetAmount                  ยอดเงินที่ต้องชำระ

         PaymentType                แบบการชำระเงิน (Cash, Credit, Cheque)

         BankName                   ชื่อธนาคาร

         ChequeNumber               เลขที่เช็ด
         ChequeBankBranch           สาขา
         ChequeDate                 ลงวันที่ของเช็ด

         CreditCardNumber           เลขที่บัตรเคดิต
         **/

        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                payments = new PaymentController().getPaymentForReceiptByRefNo(BHPreference.RefNo(), BHPreference.organizationCode());
                prepareReceiptForVoid();
            }

            @Override
            protected void after() {

                switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
                    case Sale:
                    case FirstPayment:
                    case NextPayment:
                    case Credit:
                        List<PaymentInfo> newPaymentInfoListForNextPayment = new ArrayList<PaymentInfo>(payments);
                        int numNextPayment = 0;
                        for (PaymentInfo info : newPaymentInfoListForNextPayment) {
                            if (!info.PaymentID.equals(data.PaymentID)) {
                                payments.remove(numNextPayment);
                            } else {
                                numNextPayment++;
                            }
                        }
                        break;
                    case SendDocument:
                        List<PaymentInfo> newPaymentInfoListForSendDocument = new ArrayList<PaymentInfo>(payments);
                        int numSendDocument = 0;
                        for (PaymentInfo info : newPaymentInfoListForSendDocument) {
                            if (!info.ReceiptID.equals(data.ReceiptID)) {
                                payments.remove(numSendDocument);
                            } else {
                                numSendDocument++;
                            }
                        }
                        break;
                    default:
                        break;
                }


                if (payments != null && payments.size() > 0) {
                    try {
                        if (isConnectingToInternet() && !forcePrint) {
                            load_data2(Contno);
                        } else {
                            setViewPagerItemsWithAdapter();
                            setUiPageViewController();
                        }
                    } catch (NullPointerException e) {

                    }

                    if (forcePrint == true) {
                        activity.forceButtonClick(R.string.button_print);
                    }
                } else {
                    showLastView();
                    showDialog("แจ้งเตือน", "ไม่พบข้อมูลใบเสร็จ");
                }
            }
        }).start();
    }


    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
    String CONCON = "";

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private List<PaymentInfo> items = new ArrayList<PaymentInfo>();


        public MyViewPagerAdapter(List<PaymentInfo> payments) {
            this.items = payments;
        }

        public void setNewItem(List<PaymentInfo> payments) {
            this.items = payments;;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_sale_receipt_payment_item, container, false);
            PaymentInfo info = payments.get(position);
//            Log.e("Log test", info.ReceiptCode);
//            Log.e("Log test2", info.PaymentID);
//            get_data_payment_onlines = new ArrayList<>();

            String bahtLabel = " บาท";
//    try {
            //CONCON=payments.get(position).CONTNO;
            //  load_data(CONCON);
            DebtorCustomerInfo debtorCustomerInfo = TSRController.getDebCustometByID(payments.get(position).CustomerID);
            AddressInfo addressInfo = TSRController.getAddress(payments.get(position).RefNo, AddressInfo.AddressType.AddressInstall);
            //EmployeeInfo employeeInfo = TSRController.getEmployeeByEmployeeIDAndPositionCodeSaleAndCerdit(BHPreference.organizationCode(), payments.get(position).CreateBy);
            //EmployeeInfo employeeInfo = TSRController.getEmployeeByTreeHistoryIDAndEmployeeID(BHPreference.organizationCode(), payments.get(position).CreditEmployeeLevelPath, payments.get(position).CreateBy);
            TextView txtReceiptHeadTitle = (TextView) view.findViewById(R.id.txtReceiptHeadTitle);

            try {
                if(payments.get(position).status_online==1){
                    txtReceiptHeadTitle.setText("ใบรับเงิน" + (payments.get(position).VoidStatus == true ? "  (QR PAYMENT)" : ""));
                } else {
                    txtReceiptHeadTitle.setText("ใบรับเงิน" + (payments.get(position).VoidStatus == true ? "  (ใบเสร็จถูกยกเลิก)" : ""));
                }
            } catch (Exception e) {

            }
            TextView tvReceiptDate = (TextView) view.findViewById(R.id.tvReceiptDate); //วันที่รับเงิน
            tvReceiptDate.setText(BHUtilities.dateFormat(payments.get(position).PayDate));

            TextView tvReceiptNo = (TextView) view.findViewById(R.id.tvReceiptNo); //เลขที่ใบเสร็จ
            tvReceiptNo.setText(payments.get(position).ReceiptCode);

            //payments.get(position).ReceiptCode= BHApplication.getInstance().getPrefManager().getPreferrence("getReceiptCode");
          //  tvReceiptNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getReceiptCode"));

            LinearLayout llReferenceNo = (LinearLayout) view.findViewById(R.id.llReferenceNo);//เลขที่อ้างอิง
            TextView tvReferenceNo = (TextView) view.findViewById(R.id.tvReferenceNo);
            if (payments.get(position).ManualVolumeNo != null && payments.get(position).ManualRunningNo > 0) {
                String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(payments.get(position).ManualVolumeNo), payments.get(position).ManualRunningNo).replace(' ', '0');
                tvReferenceNo.setText(ManualDocumentBookRunningNo);
            } else {
                llReferenceNo.setVisibility(View.GONE);
            }


            TextView txt_contno = (TextView) view.findViewById(R.id.txt_contno); //รหัสสินค้า
            TextView txt_date_contno = (TextView) view.findViewById(R.id.txt_date_contno); //รหัสสินค้า

            txt_contno.setText("เลขที่ใบจอง");
            txt_date_contno.setText("วันที่ทำใบจอง");




            TextView tvContractNo = (TextView) view.findViewById(R.id.tvContractNo); //เลขที่สัญญา
            tvContractNo.setText(payments.get(position).CONTNO);

            TextView tvContractDate = (TextView) view.findViewById(R.id.tvContractDate); //วันที่ทำสัญญา
            TextView tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName); //ชื่อลูกค้า
            TextView tvCitizenNo = (TextView) view.findViewById(R.id.tvCitizenNo); //เลขที่บัตรประชาชน
            TextView tvCustomerAddress = (TextView) view.findViewById(R.id.tvCustomerAddress); //ที่อยู่ติดตั้ง
            TextView tvProductName = (TextView) view.findViewById(R.id.tvProductName); //ชื่อสินค้า
            TextView textViewModel = (TextView) view.findViewById(R.id.textViewModel); //รุ่นสินค้า
            TextView tvProductNo = (TextView) view.findViewById(R.id.tvProductNo); //รหัสสินค้า

            try {
                tvContractDate.setText(BHUtilities.dateFormat(payments.get(position).EFFDATE));

            } catch (Exception e) {

            }

            try {
                tvCustomerName.setText(debtorCustomerInfo.CustomerFullName());
            } catch (Exception ex) {
                tvCustomerName.setText(payments.get(position).CustomerName);
            }

            try {
                tvCitizenNo.setText(debtorCustomerInfo.IDCard);
            } catch (Exception ex) {
                tvCitizenNo.setText(payments.get(position).IDCard);
            }

            try {
                tvCustomerAddress.setText(addressInfo.Address());
            } catch (Exception ex) {

            }

            tvProductName.setText(payments.get(position).ProductName);
            textViewModel.setText(payments.get(position).MODEL);
            tvProductNo.setText(payments.get(position).ProductSerialNumber);


            /**ส่วนลดตัดสด**/
            LinearLayout llDiscount = (LinearLayout) view.findViewById(R.id.llDiscount); //ส่วนลดตัดสด ถ้าไม่มีให้ซ่อน (แสดง/ซ่อน LinearLayout)
            if (payments.get(position).CloseAccountPaymentPeriodNumber == payments.get(position).PaymentPeriodNumber && payments.get(position).BalancesOfPeriod == 0) {
                if (payments.get(position).status_online == 1) {
                    llDiscount.setVisibility(View.GONE);
                } else {
                    llDiscount.setVisibility(View.VISIBLE);
                }
                TextView tvCloseAccountOutstandingAmountLabel = (TextView) view.findViewById(R.id.tvCloseAccountOutstandingAmountLabel); //ชำระงวดที่ 2-n
                TextView tvCloseAccountOutstandingAmount = (TextView) view.findViewById(R.id.tvCloseAccountOutstandingAmount); //ชำระงวดที่ 2-n จำนวน
                TextView tvDiscountAmount = (TextView) view.findViewById(R.id.tvDiscountAmount); //ส่วนลดตัดสด
                LinearLayout sss = (LinearLayout) view.findViewById(R.id.sss); //ส่วนลดตัดสด
                if (payments.get(position).status_online == 1) {
                    if(ssss!=0){
                        if(ssss>1){
                            tvCloseAccountOutstandingAmountLabel.setText("ชำระงวดที่ "+payments.get(position).PeriodTotal+ "\n (ชำระบางส่วน)");
                        } else {
                            tvCloseAccountOutstandingAmountLabel.setText("ชำระงวดที่ "+payments.get(position).PeriodTotal);
                        }
                    }

                    tvCloseAccountOutstandingAmountLabel.setVisibility(View.GONE);
                  // tvCloseAccountOutstandingAmount.setText(TotalPayment+bahtLabel);
                    final SpannableString text = new SpannableString(payments.get(position).TotalPayment + bahtLabel);
                    text.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    text.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvCloseAccountOutstandingAmount.setText(text);
                    tvCloseAccountOutstandingAmount.setVisibility(View.GONE);
                    tvDiscountAmount.setText("xxxxxxxx b"+ bahtLabel);
                    tvDiscountAmount.setVisibility(View.GONE);
                    sss.setVisibility(View.GONE);
                } else {
                    tvCloseAccountOutstandingAmountLabel.setText(String.format("ชำระงวดที่ %d-%d", payments.get(position).PaymentPeriodNumber, payments.get(position).MODE));
                    tvCloseAccountOutstandingAmount.setText(BHUtilities.numericFormat(payments.get(position).CloseAccountOutstandingAmount) + bahtLabel);
                    tvDiscountAmount.setText(BHUtilities.numericFormat(payments.get(position).CloseAccountDiscountAmount) + bahtLabel);
                    tvDiscountAmount.setVisibility(View.VISIBLE);
                }
            } else {
                llDiscount.setVisibility(View.GONE);
            }

            /**ยอดเงินที่จ่ายมาตามใบเสร็จ**/
            TextView tvPeriodAmountLabel = (TextView) view.findViewById(R.id.tvPeriodAmountLabel); //ค่างวดเงินสด หรือ ค่างวดที่ 1/n ((ชำระบางส่วน) ถ้าชำระไม่ครบตามงวด)
            TextView tvPeriodAmount = (TextView) view.findViewById(R.id.tvPeriodAmount); //ยอดเงินที่จ่ายมาตามใบเสร็จ
            TextView txtThaiBaht = (TextView) view.findViewById(R.id.txtThaiBaht); //จำนวนเงินที่จ่ายมาตามใบเสร็จเป็นตัวหนังสือ

            if (payments.get(position).MODE == 1) {
                if (payments.get(position).BalancesOfPeriod == 0) {
                    tvPeriodAmountLabel.setText("ค่างงวดเงินสด\n(ชำระครบ)");
                } else {
                    tvPeriodAmountLabel.setText("ค่างวดเงินสด\n(ชำระบางส่วน)");
                }
            } else {
                if (payments.get(position).CloseAccountPaymentPeriodNumber == payments.get(position).PaymentPeriodNumber && payments.get(position).BalancesOfPeriod == 0) {
                    if(payments.get(position).status_online==1){
                        if(ssss!=0){
                            if(ssss>1){
                                tvPeriodAmountLabel.setText("ค่างวดที่ "+payments.get(position).PeriodTotal+ "\n (ชำระบางส่วน)");
                            } else {
                                tvPeriodAmountLabel.setText("ค่างวดที่ "+payments.get(position).MaxPaymentPeriodNumber);
                            }
                        }
                      //  tvPeriodAmountLabel.setText("ค่างวดที่ ");
                        final SpannableString text = new SpannableString(payments.get(position).TotalPayment + bahtLabel);
                        text.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        text.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        tvPeriodAmount.setText(text);
                    } else {

                    }
                    //tvPeriodAmountLabel.setVisibility(View.GONE);   // แก้ไหม่
                } else {
                    if (payments.get(position).BalancesOfPeriod == 0) {
                        String txtPeriodAmountLabel = "";
                        if (payments.get(position).PaymentPeriodNumber == payments.get(position).MODE) {
                            txtPeriodAmountLabel = "ค่างวดที่ %d/%d\n(ชำระครบ)";
                        } else {
                            txtPeriodAmountLabel = "ค่างวดที่ %d/%d";
                        }
                        tvPeriodAmountLabel.setText(String.format(txtPeriodAmountLabel, payments.get(position).PaymentPeriodNumber, payments.get(position).MODE));
                    } else {
                        if (payments.get(position).Amount == 0) {
                            if(payments.get(position).status_online==1){
                                tvPeriodAmountLabel.setText("ค่างวดที่ xxxxxxxxx");
                            } else {
                                tvPeriodAmountLabel.setText(String.format("ค่างวดที่ %d/%d\n", payments.get(position).PaymentPeriodNumber, payments.get(position).MODE));
                            }
                        } else {
                            tvPeriodAmountLabel.setText(String.format("ค่างวดที่ %d/%d\n(ชำระบางส่วน)", payments.get(position).PaymentPeriodNumber, payments.get(position).MODE));
                        }
                    }
                }
            }
            /*** [START] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับี ***/

            if (payments.get(position).CloseAccountPaymentPeriodNumber == payments.get(position).PaymentPeriodNumber && payments.get(position).BalancesOfPeriod == 0) {
                SpannableString periodAmount = new SpannableString(BHUtilities.numericFormat(payments.get(position).CloseAccountNetAmount) + bahtLabel);
                periodAmount.setSpan(new ForegroundColorSpan(Color.RED), 0, BHUtilities.numericFormat(payments.get(position).CloseAccountNetAmount).length(), 0);//สี
                periodAmount.setSpan(new StyleSpan(Typeface.BOLD), 0, BHUtilities.numericFormat(payments.get(position).CloseAccountNetAmount).length(), 0);//ตัวหนา
                periodAmount.setSpan(new TextAppearanceSpan(activity, R.style.TextView_Value2), 0, BHUtilities.numericFormat(payments.get(position).CloseAccountNetAmount).length(), 0);//ขนาดตัว

                if(payments.get(position).status_online==1) {
                    txtThaiBaht.setText(payments.get(position).TotalPaymentText+" บาทถ้วน");
                    //txtThaiBaht.setVisibility(View.GONE);
                }
                else {
                    tvPeriodAmount.setText(periodAmount, TextView.BufferType.SPANNABLE);
                    txtThaiBaht.setText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(payments.get(position).CloseAccountNetAmount)));
                    txtThaiBaht.setVisibility(View.VISIBLE);
                }
            } else {
                SpannableString periodAmount = new SpannableString(BHUtilities.numericFormat(payments.get(position).Amount) + bahtLabel);
                periodAmount.setSpan(new ForegroundColorSpan(Color.RED), 0, BHUtilities.numericFormat(payments.get(position).Amount).length(), 0);//สี
                periodAmount.setSpan(new StyleSpan(Typeface.BOLD), 0, BHUtilities.numericFormat(payments.get(position).Amount).length(), 0);//ตัวหนา
                periodAmount.setSpan(new TextAppearanceSpan(activity, R.style.TextView_Value2), 0, BHUtilities.numericFormat(payments.get(position).Amount).length(), 0);//ขนาดตัว
                if(payments.get(position).status_online==1) {
                    txtThaiBaht.setText(payments.get(position).TotalPaymentText+" บาทถ้วน");
                    txtThaiBaht.setVisibility(View.GONE);
                } else {
                    txtThaiBaht.setText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(payments.get(position).Amount)));
                    txtThaiBaht.setVisibility(View.VISIBLE);
                    tvPeriodAmount.setText(periodAmount, TextView.BufferType.SPANNABLE);
                }
            }

            try {
                if(payments.get(position).status_online==1){
                    //tvPeriodAmount.setVisibility(View.GONE);   // แก้ไข ฮาดโค้ด
                   // tvPeriodAmount.setText(BHUtilities.numericFormat(payments.get(position).Amount) + bahtLabel);
                } else {
                   // tvPeriodAmount.setVisibility(View.VISIBLE);   // แก้ไข ฮาดโค้ด
                    txtThaiBaht.setVisibility(payments.get(position).VoidStatus ? View.GONE : View.VISIBLE);
                    tvPeriodAmount.setText(payments.get(position).VoidStatus ? "ยกเลิกการชำระเงิน" : tvPeriodAmount.getText());
                }
            } catch (Exception e) {

            }

            //tvPeriodAmount.setText(BHUtilities.numericFormat(payments.get(position).Amount) + bahtLabel);
            /*** [END] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับ***/
            //เพิ่มการตรวจ VoidStatus = true ให้ปิดการแสดงผล เพราะมีการปรับข้อมูลทำให้ไม่สามารถคำนวณค่าได้ถูกต้อง
            if ((payments.get(position).CloseAccountPaymentPeriodNumber == payments.get(position).PaymentPeriodNumber && payments.get(position).BalancesOfPeriod == 0) || payments.get(position).VoidStatus) {
                LinearLayout llBalancesOfPeriod = (LinearLayout) view.findViewById(R.id.llBalancesOfPeriod); //ยอดเงินคงเหลือของงวด ถ้าไม่มีให้ซ่อน (แสดง/ซ่อน LinearLayout)
                TextView tvBalancesOfPeriodLabel = (TextView) view.findViewById(R.id.tvBalancesOfPeriodLabel); //คงเหลืองวดที่ n
                TextView tvBalancesOfPeriod = (TextView) view.findViewById(R.id.tvBalancesOfPeriod); //จำนวนเงินคงเหลือของงวด
                if(payments.get(position).status_online==1){
                    llBalancesOfPeriod.setVisibility(View.VISIBLE);
                    tvBalancesOfPeriod.setText(payments.get(position).PeriodTotalPrice+bahtLabel);

                    tvBalancesOfPeriodLabel.setText("คงเหลืองวดที่ "+payments.get(position).PeriodTotal);
                } else {
                    llBalancesOfPeriod.setVisibility(View.GONE);
                }
                LinearLayout llBalanceAmount = (LinearLayout) view.findViewById(R.id.llBalanceAmount); //ยอดคงเหลือ ถ้าไม่มีให้ซ่อน (แสดง/ซ่อน LinearLayout)


                //if(payments.get(position).status_online==1){

               // }
              //  else {
                    llBalanceAmount.setVisibility(View.GONE);
                //}


            } else {
                /**ยอดเงินคงเหลือของงวดนั้น**/
                LinearLayout llBalancesOfPeriod = (LinearLayout) view.findViewById(R.id.llBalancesOfPeriod); //ยอดเงินคงเหลือของงวด ถ้าไม่มีให้ซ่อน (แสดง/ซ่อน LinearLayout)
                TextView tvBalancesOfPeriodLabel = (TextView) view.findViewById(R.id.tvBalancesOfPeriodLabel); //คงเหลืองวดที่ n
                TextView tvBalancesOfPeriod = (TextView) view.findViewById(R.id.tvBalancesOfPeriod); //จำนวนเงินคงเหลือของงวด

                if (payments.get(position).BalancesOfPeriod == 0) {
                    llBalancesOfPeriod.setVisibility(View.GONE);
                } else {
                    llBalancesOfPeriod.setVisibility(View.VISIBLE);
                    if (payments.get(position).MODE == 1) {
                        tvBalancesOfPeriodLabel.setText("คงเหลือเงินสด");
                    } else {
                        tvBalancesOfPeriodLabel.setText(String.format("คงเหลืองวดที่ %d", payments.get(position).PaymentPeriodNumber));
                       // tvBalancesOfPeriodLabel.setText("คงเหลืองวดที่ xx-xx");
                    }

                    if(payments.get(position).status_online==1){
                        tvBalancesOfPeriod.setText(payments.get(position).PeriodTotalPrice+bahtLabel);
                    } else {
                        tvBalancesOfPeriod.setText(BHUtilities.numericFormat(payments.get(position).BalancesOfPeriod) + bahtLabel);
                    }
                }

                /**ยอดคงเหลือของงวดถัดไป**/
                LinearLayout llBalanceAmount = (LinearLayout) view.findViewById(R.id.llBalanceAmount); //ยอดคงเหลือ ถ้าไม่มีให้ซ่อน (แสดง/ซ่อน LinearLayout)
                TextView tvBalanceAmountLabel = (TextView) view.findViewById(R.id.tvBalanceAmountLabel); //คงเหลือเงินสด หรื คงเหลืองวดที่ 1 - n หรือ คงเหลืองวดที่ n
                TextView tvBalanceAmount = (TextView) view.findViewById(R.id.tvBalanceAmount); //จำนวนเงินคงเหลือ

                //เพิ่มการตรวจ VoidStatus = true ให้ปิดการแสดงผล เพราะมีการปรับข้อมูลทำให้ไม่สามารถคำนวณค่าได้ถูกต้อง
                if (payments.get(position).Balances - payments.get(position).BalancesOfPeriod == 0 || payments.get(position).VoidStatus) {
                    llBalanceAmount.setVisibility(View.GONE);
                } else {
                    llBalanceAmount.setVisibility(View.VISIBLE);
                    if (payments.get(position).MODE == 1) {
                        tvBalanceAmountLabel.setText("คงเหลือเงินสด");
                    } else {
                        /*if (payments.get(position).BalancesOfPeriod == 0) {
                            if ((payments.get(position).PaymentPeriodNumber + 1) == payments.get(position).MODE) {
                                tvBalanceAmountLabel.setText(String.format("คงเหลืองวดที่ %d", payments.get(position).MODE));
                            } else {
                                tvBalanceAmountLabel.setText(String.format("คงเหลืองวดที่ %d - %d", payments.get(position).PaymentPeriodNumber + 1, payments.get(position).MODE));
                            }
                        } else {
                            if (payments.get(position).PaymentPeriodNumber == payments.get(position).MODE) {
                                tvBalanceAmountLabel.setText(String.format("คงเหลืองวดที่ %d", payments.get(position).MODE));
                            } else {
                                tvBalanceAmountLabel.setText(String.format("คงเหลืองวดที่ %d - %d", payments.get(position).PaymentPeriodNumber, payments.get(position).MODE));
                            }
                        }*/

                        if(payments.get(position).status_online==1){
                            tvBalanceAmountLabel.setText("คงเหลืองวดที่ xx-xx");
                        } else {
                            tvBalanceAmountLabel.setText(String.format("คงเหลืองวดที่ %d - %d", payments.get(position).PaymentPeriodNumber + 1, payments.get(position).MODE));
                        }
                    }

                    if(payments.get(position).status_online==1){
                        tvBalanceAmount.setText(payments.get(position).TotalPaymentText+ bahtLabel);
                    } else {
                        tvBalanceAmount.setText(BHUtilities.numericFormat(payments.get(position).Balances - payments.get(position).BalancesOfPeriod) + bahtLabel);
                    }
                }

            }

            /**ชำระโดยบัตรเครดิต**/
            LinearLayout llCreditAmount = (LinearLayout) view.findViewById(R.id.llCreditAmount); //ชำระโดยบัตรเครดิต ถ้าไม่ใช่ให้ซ่อน (แสดง/ซ่อน LinearLayout)
            TextView tvCreditName = (TextView) view.findViewById(R.id.tvCreditName); //ชื่อธนาคารของบัตรเครดิต
            TextView tvCreditNumber = (TextView) view.findViewById(R.id.tvCreditNumber); //เลขที่บัตรของธนาคาร

            /**ชำระโดยเช็ค**/
            LinearLayout llChequeAmount = (LinearLayout) view.findViewById(R.id.llChequeAmount);  //ชำระโดยเช็ค ถ้าไม่ใช่ให้ซ่อน (แสดง/ซ่อน LinearLayout)
            TextView tvChequeName = (TextView) view.findViewById(R.id.tvChequeName); //ชื่อธนาคารของเช็ด
            TextView tvChequeBrach = (TextView) view.findViewById(R.id.tvChequeBrach); //สาขาเช็ด
            TextView tvChequeNo = (TextView) view.findViewById(R.id.tvChequeNo); //เลขที่เช็ด
            TextView tvChequeDate = (TextView) view.findViewById(R.id.tvChequeDate); //วันที่ลงเช็ด

            switch (Enum.valueOf(PaymentInfo.PaymentType1.class, payments.get(position).PaymentType)) {
                case Cash:
                    llCreditAmount.setVisibility(View.GONE);
                    llChequeAmount.setVisibility(View.GONE);
                    break;
                case Credit:
                    llCreditAmount.setVisibility(View.VISIBLE);
                    llChequeAmount.setVisibility(View.GONE);

                    tvCreditName.setText(payments.get(position).BankName);
                    tvCreditNumber.setText(payments.get(position).CreditCardNumber);
                    break;
                case Cheque:
                    llCreditAmount.setVisibility(View.GONE);
                    llChequeAmount.setVisibility(View.VISIBLE);

                    tvChequeName.setText(payments.get(position).BankName);
                    tvChequeBrach.setText(payments.get(position).ChequeBankBranch);
                    tvChequeNo.setText(payments.get(position).ChequeNumber);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date date = formatter.parse(payments.get(position).ChequeDate);
                        tvChequeDate.setText(BHUtilities.dateFormat(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

            /**พนักงานที่ออกใบเสร็จ**/
            TextView txtSaleEmpName = (TextView) view.findViewById(R.id.txtSaleEmpName); //ชื่อเต็มของพนักงานที่ออกใบเสร็จ
            TextView txtSaleTeamName = (TextView) view.findViewById(R.id.txtSaleTeamName); //ทีมของพนักงานที่ออกใบเสร็จ

            if(payments.get(position).status_online==1){
                //txtSaleEmpName.setText(String.format("(%s)", payments.get(position).SaleEmployeeName != null ? payments.get(position).SaleEmployeeName : ""));
                //txtSaleTeamName.setText(String.format("(ทีม %s)", payments.get(position).TeamCode != null ? payments.get(position).TeamCode : ""));
                txtSaleEmpName.setText("QR PAYMENT");
                        txtSaleTeamName.setVisibility(View.GONE);
            }
            else {
                txtSaleEmpName.setText(String.format("(%s)", payments.get(position).SaleEmployeeName != null ? payments.get(position).SaleEmployeeName : ""));
                txtSaleTeamName.setText(String.format("(ทีม %s)", payments.get(position).TeamCode != null ? payments.get(position).TeamCode : ""));

                txtSaleTeamName.setVisibility(View.VISIBLE);
            }

                /*if(employeeInfo != null) {
                    txtSaleEmpName.setText(String.format("(%s)", employeeInfo.SaleEmployeeName != null ? employeeInfo.SaleEmployeeName : ""));
                    txtSaleTeamName.setText(String.format("(ทีม %s)", employeeInfo.TeamCode != null ? employeeInfo.TeamCode : ""));
                } else {
                    txtSaleEmpName.setText("");
                    txtSaleTeamName.setText("");
                }*/

            /*Void Button*/
            Button voidBtn = (Button) view.findViewById(R.id.btnVoidReceipt);
            voidBtn.setText("ยกเลิกใบรับเงิน");

            try {
                voidBtn.setVisibility(payments.get(position).CanVoid ? view.VISIBLE : view.GONE);
                voidBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentViewPosition = position;
                        AlertDialog.Builder setupAlert;
                        setupAlert = new AlertDialog.Builder(activity)
                                .setTitle("ยกเลิกใบรับเงิน")
                                .setMessage("ต้องการยกเลิกยกเลิกใบรับเงินหมายเลข " + payments.get(position).ReceiptCode + " ใช่หรือไม่")
                                .setCancelable(false);

                        setupAlert = setupAlert.setPositiveButton("ใช่ ฉันต้องการยกเลิกใบรับเงินนี้", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                doVoidReceipt(payments.get(position).RefNo, payments.get(position).ReceiptID);
                            }
                        }).setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        setupAlert.show();

                    }
                });
            } catch (Exception e) {

            }

            Button btnPrintImage = (Button) view.findViewById(R.id.btnPrintImage);
            btnPrintImage.setVisibility(View.GONE);
            //
            //            if (!payments.get(position).EmpID.equals(BHPreference.employeeID()) || payments.get(position).VoidStatus == true) {
            //                btnPrintImage.setVisibility(View.GONE);
            //            } else {
            //                btnPrintImage.setVisibility(View.VISIBLE);
            //
            //                btnPrintImage.setOnClickListener(new View.OnClickListener() {
            //                    @Override
            //                    public void onClick(View v) {
            //                        new PrinterController(activity).newImagePrintReceipt(payments.get(position));
            //                    }
            //                });
            //            }


            TextView txtHeader = (TextView) view.findViewById(R.id.txtReceiptHeadTitle);

            if (!(!payments.get(position).EmpID.equals(BHPreference.employeeID()) || payments.get(position).VoidStatus == true)) {
                txtHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PrinterController(activity).newImagePrintReceipt(payments.get(position));
                    }
                });
            }


//    } catch (Exception ex) {
//        Log.e("Catch test", ex.getLocalizedMessage());
//    }

            ((ViewPager) container).addView(view);

            return view;
        }

        @Override
        public int getCount() {
            //   return items.size()+sizee;
            return items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            ((ViewPager) container).removeView(view);
        }
    }

    int paymentSize = 0;

    private void load_data2(String CONCON) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.payment(CONCON);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
//                        int item = jsonObject.getJSONArray("data").length();
//                        Log.e("New item", "" + jsonObject.getJSONArray("data"));
                      //  JSON_PARSE_DATA_AFTER_WEBCALL2(jsonObject.getJSONArray("data"));
                        //JSON_PARSE_DATA_AFTER_WEBCALL22(jsonObject.getJSONArray("data"));
                        JSON_PARSE_DATA_AFTER_WEBCALL_TEST(jsonObject.getJSONArray("data"),CONCON);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data", "22");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                }
            });

        } catch (Exception e) {
            Log.e("data", "3");
        }
    }
    private void load_data3(String CONCON) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.payment2(CONCON);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
//                        int item = jsonObject.getJSONArray("data").length();
//                        Log.e("New item", "" + jsonObject.getJSONArray("data"));
                          // JSON_PARSE_DATA_AFTER_WEBCALL2(jsonObject.getJSONArray("data"));
                       JSON_PARSE_DATA_AFTER_WEBCALL22(jsonObject.getJSONArray("data"));
                        //JSON_PARSE_DATA_AFTER_WEBCALL_TEST(jsonObject.getJSONArray("data"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data", "22");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                }
            });

        } catch (Exception e) {
            Log.e("data", "3");
        }
    }

    String ReceiptCode = "";
    String CONTNO = "";
    String CustomerName = "";
    String IDCard = "";
    String AddressInstall = "";
    String ProductName = "";
    String MODEL = "";
    String ProductSerialNumber = "";
    String MaxPaymentPeriodNumber = "";
    String TotalPayment = "";
    String TotalPaymentText = "";
    String PeriodTotal = "";
    String PeriodTotalPrice = "";
    String EFFDATE = "";
    String DatePayment = "";
    String ChanelName = "";
    String TeamName = "";
    String SalePaymentPeriod_SalePaymentPeriodID = "";
    String SalePaymentPeriod_RefNo = "";
    String SalePaymentPeriod_PaymentPeriodNumber = "";
    String SalePaymentPeriod_PaymentAmount = "";
    String SalePaymentPeriod_Discount = "";
    String SalePaymentPeriod_NetAmount = "";
    String SalePaymentPeriod_PaymentComplete = "";
    String SalePaymentPeriod_TripID = "";
    String SalePaymentPeriod_CreateBy = "";
    String SalePaymentPeriod_LastUpdateBy = "";
    String SalePaymentPeriod_CloseAccountDiscountAmount = "";
    String SalePaymentPeriod_CreateDate = "";
    String SalePaymentPeriod_PaymentDueDate = "";
    String SalePaymentPeriod_PaymentAppointmentDate = "";
    String SalePaymentPeriod_LastUpdateDate = "";
    String SalePaymentPeriod_SyncedDate = "";
    String DatePayment1 = "";
    String paymentID = "";
    String PaymentID = "";
    String OrganizationCode = "";
    String SendMoneyID = "";
    String PaymentType = "";
    String PayPartial = "";
    String BankCode = "";
    String ChequeNumber = "";
    String ChequeBankBranch = "";
    String ChequeDate = "";
    String CreditCardNumber = "";
    String CreditCardApproveCode = "";
    String CreditEmployeeLevelPath = "";
    String TripID = "";
    String Status = "";
    String RefNo = "";
    String PayPeriod = "";
    String PayDate = "";
    String PAYAMT = "";
    String CashCode = "";
    String EmpID = "";
    String TeamCode = "";
    String receiptkind = "";
    String Kind = "";
    String BookNo = "";
    String ReceiptNo = "";
    String CreateDate = "";
    String CreateBy = "";
    String LastUpdateDate = "";
    String LastUpdateBy = "";
    String SyncedDate = "";
    String[] ReceiptCode_N;
    String[] CONTNO_N;
    String[] CustomerName_N;
    String[] IDCard_N;
    String[] AddressInstall_N;
    String[] ProductName_N;
    String[] MODEL_N;
    String[] ProductSerialNumber_N;
    String[] MaxPaymentPeriodNumber_N;
    String[] TotalPayment_N;
    String[] TotalPaymentText_N;
    String[] PeriodTotal_N;
    String[] PeriodTotalPrice_N;
    String[] EFFDATE_N;
    String[] DatePayment_N;
    String[] ChanelName_N;
    String[] TeamName_N;

    int xxx = 0;

    public void JSON_PARSE_DATA_AFTER_WEBCALL2(JSONArray array) {



        if (array.length() == 0) {
            Log.e("array1", "1111");
            setViewPagerItemsWithAdapter();
            setUiPageViewController();

        } else {




           // else {
                Log.e("array1", String.valueOf(array.length()));
                PaymentInfo paymentInfo = null;
                List<PaymentInfo> paymentInfos = new ArrayList<>();
                List<PaymentInfo> newPaymentInfoListForReceipt = new ArrayList<PaymentInfo>(payments);
                Log.e("Payment size1", String.valueOf(newPaymentInfoListForReceipt.size()));
                try {
//                for (int j = 0; j < payments.size(); j++) {
//                    PaymentInfo info = payments.get(j);
                    for (PaymentInfo info : newPaymentInfoListForReceipt) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = null;
                            try {
                                json = array.getJSONObject(i);
                                ReceiptCode = json.getString("ReceiptCode") + "";
                                CONTNO = json.getString("CONTNO") + "";
                                CustomerName = json.getString("CustomerName") + "";
                                IDCard = json.getString("IDCard") + "";
                                AddressInstall = json.getString("AddressInstall") + "";
                                ProductName = json.getString("ProductName") + "";
                                MODEL = json.getString("MODEL") + "";
                                ProductSerialNumber = json.getString("ProductSerialNumber") + "";
                                MaxPaymentPeriodNumber = json.getString("MaxPaymentPeriodNumber") + "";
                                TotalPayment = json.getString("TotalPayment") + "";
                                TotalPaymentText = json.getString("TotalPaymentText") + "";
                                PeriodTotal = json.getString("PeriodTotal") + "";
                                PeriodTotalPrice = json.getString("PeriodTotalPrice") + "";
                                EFFDATE = json.getString("EFFDATE") + "";
                                DatePayment = json.getString("DatePayment") + "";
                                ChanelName = json.getString("ChanelName") + "";
                                TeamName = json.getString("TeamName") + "";
                                SalePaymentPeriod_SalePaymentPeriodID = json.getString("SalePaymentPeriod_SalePaymentPeriodID") + "";
                                SalePaymentPeriod_RefNo = json.getString("SalePaymentPeriod_RefNo") + "";
                                SalePaymentPeriod_PaymentPeriodNumber = json.getString("SalePaymentPeriod_PaymentPeriodNumber") + "";
                                SalePaymentPeriod_PaymentAmount = json.getString("SalePaymentPeriod_PaymentAmount") + "";
                                SalePaymentPeriod_Discount = json.getString("SalePaymentPeriod_Discount") + "";
                                SalePaymentPeriod_NetAmount = json.getString("SalePaymentPeriod_NetAmount") + "";
                                SalePaymentPeriod_PaymentComplete = json.getString("SalePaymentPeriod_PaymentComplete") + "";
                                SalePaymentPeriod_TripID = json.getString("SalePaymentPeriod_TripID") + "";
                                SalePaymentPeriod_CreateBy = json.getString("SalePaymentPeriod_CreateBy") + "";
                                SalePaymentPeriod_LastUpdateBy = json.getString("SalePaymentPeriod_LastUpdateBy") + "";
                                SalePaymentPeriod_CloseAccountDiscountAmount = json.getString("SalePaymentPeriod_CloseAccountDiscountAmount") + "";
                                PaymentID = json.getString("Payment_paymentID") + "";
                                OrganizationCode = json.getString("Payment_OrganizationCode") + "";
                                SendMoneyID = json.getString("Payment_SendMoneyID") + "";
                                PaymentType = json.getString("Payment_PaymentType") + "";
                                PayPartial = json.getString("Payment_PayPartial") + "";
                                BankCode = json.getString("Payment_BankCode") + "";
                                ChequeNumber = json.getString("Payment_ChequeNumber") + "";
                                ChequeBankBranch = json.getString("Payment_ChequeBankBranch") + "";
                                ChequeDate = json.getString("Payment_ChequeDate") + "";
                                CreditCardNumber = json.getString("Payment_CreditCardNumber") + "";
                                CreditCardApproveCode = json.getString("Payment_CreditCardApproveCode") + "";
                                CreditEmployeeLevelPath = json.getString("Payment_CreditEmployeeLevelPath") + "";
                                TripID = json.getString("Payment_TripID") + "";
                                Status = json.getString("Payment_Status") + "";
                                RefNo = json.getString("Payment_RefNo") + "";
                                PayPeriod = json.getString("Payment_PayPeriod") + "";
                                PAYAMT = json.getString("Payment_PAYAMT") + "";
                                CashCode = json.getString("Payment_CashCode") + "";
                                EmpID = json.getString("Payment_EmpID") + "";
                                TeamCode = json.getString("Payment_TeamCode") + "";
                                receiptkind = json.getString("Payment_receiptkind") + "";
                                Kind = json.getString("Payment_Kind") + "";
                                BookNo = json.getString("Payment_BookNo") + "";
                                ReceiptNo = json.getString("Payment_ReceiptNo") + "";
                                CreateBy = json.getString("Payment_CreateBy") + "";
                                LastUpdateBy = json.getString("Payment_LastUpdateBy") + "";


                                CreateDate = json.getJSONObject("Payment_CreateDate").getString("date") + "";

                                LastUpdateDate = json.getJSONObject("Payment_LastUpdateDate").getString("date") + "";


                                // PayDate=json.getJSONObject("Payment_PayDate").getString("date")+"";
                                // SyncedDate=json.getJSONObject("Payment_SyncedDate").getString("date")+"";
                                // DatePayment1=json.getJSONObject("DatePayment1").getString("date")+"";
                                // SalePaymentPeriod_CreateDate=json.getJSONObject("SalePaymentPeriod_CreateDate").getString("date")+"";
                                //SalePaymentPeriod_PaymentDueDate=json.getJSONObject("SalePaymentPeriod_PaymentDueDate").getString("date")+"";
                                // SalePaymentPeriod_PaymentAppointmentDate=json.getJSONObject("SalePaymentPeriod_PaymentAppointmentDate").getString("date")+"";
                                // SalePaymentPeriod_LastUpdateDate=json.getJSONObject("SalePaymentPeriod_LastUpdateDate").getString("date")+"";
                                //SalePaymentPeriod_SyncedDate=json.getJSONObject("SalePaymentPeriod_SyncedDate").getString("date")+"";


                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date effDate = null;
                                Date payDate = null;
                                Date creatDate = null;
                                Date updateDate = null;

                                payDate = sdf.parse(DatePayment);
                                effDate = sdf.parse(EFFDATE);
                                try {


                                    creatDate = sdf.parse(json.getJSONObject("Payment_CreateDate").getString("date"));
                                    updateDate = sdf.parse(json.getJSONObject("Payment_LastUpdateDate").getString("date"));

                                } catch (ParseException ex) {
                                    Log.e("Exception", ex.getLocalizedMessage());
                                }
                                if (info.PaymentID.equals(PaymentID)) {
                                    xxx=0;
                                    Log.e("PaymentID_xx", ReceiptCode);

                                    paymentInfo = new PaymentInfo();
                                    paymentInfo.ReceiptCode = ReceiptCode;
                                    paymentInfo.CONTNO = CONTNO;
                                    paymentInfo.EFFDATE = effDate;
                                    paymentInfo.CustomerName = CustomerName;
                                    paymentInfo.IDCard = IDCard;
                                    paymentInfo.ProductName = ProductName;
                                    paymentInfo.ProductCode = ProductSerialNumber;
                                    paymentInfo.ProductModel = MODEL;


                                    paymentInfo.MODEL = MODEL;
                                    paymentInfo.ProductSerialNumber = ProductSerialNumber;

                                    paymentInfo.CloseAccountOutstandingAmount = Float.parseFloat(TotalPayment);

                                    paymentInfo.CloseAccountDiscountAmount = Float.parseFloat(SalePaymentPeriod_CloseAccountDiscountAmount);

                                    // tvProductNo.setText(payments.get(position).ProductSerialNumber);

                                    //paymentInfo.PaymentPeriodNumber= Integer.parseInt(PeriodTotal);
                                    //paymentInfo.MODE=0;
                                    paymentInfo.VoidStatus = false;

                                    paymentInfo.PaymentID = PaymentID;
                                    paymentInfo.OrganizationCode = OrganizationCode;
                                    paymentInfo.SendMoneyID = SendMoneyID;
                                    paymentInfo.PaymentType = PaymentType;
//                                paymentInfo.PayPartial = json.getString("Payment_PayPartial").equals("0") ? false : true;


                                    paymentInfo.VoidStatus = true;
                                    paymentInfo.status_online=1;
                                    paymentInfo.PeriodTotal=PeriodTotal;
                                    paymentInfo.TotalPaymentText=TotalPaymentText;
                                    paymentInfo.PeriodTotalPrice=PeriodTotalPrice;
                                    paymentInfo.TotalPayment=TotalPayment;

                                    paymentInfo.BankCode = BankCode;
                                    paymentInfo.ChequeNumber = ChequeNumber;
                                    paymentInfo.ChequeBankBranch = ChequeBankBranch;
                                    paymentInfo.ChequeDate = ChequeDate;
                                    paymentInfo.CreditCardNumber = CreditCardNumber;
                                    paymentInfo.CreditCardApproveCode = CreditCardApproveCode;
                                    paymentInfo.CreditEmployeeLevelPath = CreditEmployeeLevelPath;
                                    paymentInfo.TripID = TripID;
                                    paymentInfo.Status = Status;
                                    paymentInfo.RefNo = RefNo;
                                    paymentInfo.PayPeriod = PayPeriod;
                                    paymentInfo.PayDate = payDate;
                                    paymentInfo.PAYAMT = Float.parseFloat(PAYAMT);
                                    paymentInfo.CashCode = CashCode;
                                    paymentInfo.EmpID = EmpID;
                                    paymentInfo.TeamCode = TeamCode;
                                    paymentInfo.receiptkind = receiptkind;
                                    paymentInfo.Kind = Kind;
                                    paymentInfo.BookNo = BookNo;
                                    paymentInfo.ReceiptNo = ReceiptNo;
//                                paymentInfo.CreateDate = creatDate;
                                    paymentInfo.CreateBy = CreateBy;
//                                paymentInfo.LastUpdateDate = updateDate;
                                    paymentInfo.LastUpdateBy = LastUpdateBy;

                                    payments.add(payments.size(), paymentInfo);
                                }
                                else {
                                    Log.e("PaymentID_xx2", ReceiptCode);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("initail data", e.getLocalizedMessage());
                            }
                        }

                    }
                    Log.e("Payment size2", String.valueOf(payments.size()));
//                payments.addAll(payments.size(), paymentInfos);
//                payments.add(payments.size(), );
//                myViewPagerAdapter = new MyViewPagerAdapter();
//                myViewPagerAdapter.setNewItem(payments);


                    if(xxx==1) {
                      //   load_data3(CONCON);
                    }
                    else {
                        prepareReceiptForVoid();
                        setViewPagerItemsWithAdapter();
                        setUiPageViewController();
                        myViewPagerAdapter.notifyDataSetChanged();
                    }

                } catch (Exception ex) {

                }
        }
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL_TEST(JSONArray array,String CONCON) {
        if (array.length() == 0) {
            Log.e("array1", "1111");
            setViewPagerItemsWithAdapter();
            setUiPageViewController();

        } else {

            try {

                PaymentInfo paymentInfo = null;
                List<PaymentInfo> paymentInfos = new ArrayList<>();
                List<PaymentInfo> newPaymentInfoListForReceipt = new ArrayList<PaymentInfo>(payments);

                    for (int i = 0; i < array.length(); i++) {

                        final Get_data_online GetDataAdapter2 = new Get_data_online();

                        JSONObject json = null;
                        try {
                            json = array.getJSONObject(i);

                            ReceiptCode = json.getString("ReceiptCode") + "";
                            CONTNO = json.getString("CONTNO") + "";
                            CustomerName = json.getString("CustomerName") + "";
                            IDCard = json.getString("IDCard") + "";
                            AddressInstall = json.getString("AddressInstall") + "";
                            ProductName = json.getString("ProductName") + "";
                            MODEL = json.getString("MODEL") + "";
                            ProductSerialNumber = json.getString("ProductSerialNumber") + "";
                            MaxPaymentPeriodNumber = json.getString("MaxPaymentPeriodNumber") + "";
                            TotalPayment = json.getString("TotalPayment") + "";
                            TotalPaymentText = json.getString("TotalPaymentText") + "";
                            PeriodTotal = json.getString("PeriodTotal") + "";
                            PeriodTotalPrice = json.getString("PeriodTotalPrice") + "";
                            EFFDATE = json.getString("EFFDATE") + "";
                            DatePayment = json.getString("DatePayment") + "";
                            ChanelName = json.getString("ChanelName") + "";
                            TeamName = json.getString("TeamName") + "";



                            GetDataAdapter2.setReceiptCode(ReceiptCode);
                            GetDataAdapter2.setCONTNO(CONTNO);
                            GetDataAdapter2.setIDCard(IDCard);
                            GetDataAdapter2.setAddressInstall(AddressInstall);
                            GetDataAdapter2.setProductName(ProductName);
                            GetDataAdapter2.setMODEL(MODEL);
                            GetDataAdapter2.setProductSerialNumber(ProductSerialNumber);
                            GetDataAdapter2.setMaxPaymentPeriodNumber(MaxPaymentPeriodNumber);
                            GetDataAdapter2.setTotalPayment(TotalPayment);
                            GetDataAdapter2.setTotalPaymentText(TotalPaymentText);
                            GetDataAdapter2.setPeriodTotal(PeriodTotal);



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("initail data", e.getLocalizedMessage());
                        }

                        GetDataAdapter1.add(GetDataAdapter2);
                    }





                if(payments.size()==GetDataAdapter1.size()){
                    Log.e("5555","xxxx");
                    prepareReceiptForVoid();
                    setViewPagerItemsWithAdapter();
                    setUiPageViewController();
                    myViewPagerAdapter.notifyDataSetChanged();
                }
                else {
                    Log.e("5555","cccc");
                    load_data3(CONCON);
                }







            }
            catch (Exception e){

            }

        }
    }


    int ssss=0;
    public void JSON_PARSE_DATA_AFTER_WEBCALL22(JSONArray array) {
        if (array.length() == 0) {
            ssss=array.length();
            Log.e("array1", "1111");
            setViewPagerItemsWithAdapter();
            setUiPageViewController();

        } else {

            ssss=array.length();
            Log.e("array1", String.valueOf(array.length()));
            PaymentInfo paymentInfo = null;
            List<PaymentInfo> paymentInfos = new ArrayList<>();
            List<PaymentInfo> newPaymentInfoListForReceipt = new ArrayList<PaymentInfo>(payments);
            Log.e("Payment size1", String.valueOf(newPaymentInfoListForReceipt.size()));
            try {
//                for (int j = 0; j < payments.size(); j++) {
//                    PaymentInfo info = payments.get(j);
                //for (PaymentInfo info : newPaymentInfoListForReceipt) {
                for (PaymentInfo info : payments) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = null;
                        try {
                            json = array.getJSONObject(i);
                            ReceiptCode = json.getString("ReceiptCode") + "";
                            CONTNO = json.getString("CONTNO") + "";
                            CustomerName = json.getString("CustomerName") + "";
                            IDCard = json.getString("IDCard") + "";
                            AddressInstall = json.getString("AddressInstall") + "";
                            ProductName = json.getString("ProductName") + "";
                            MODEL = json.getString("MODEL") + "";
                            ProductSerialNumber = json.getString("ProductSerialNumber") + "";
                            MaxPaymentPeriodNumber = json.getString("MaxPaymentPeriodNumber") + "";
                            TotalPayment = json.getString("TotalPayment") + "";
                            TotalPaymentText = json.getString("TotalPaymentText") + "";
                            PeriodTotal = json.getString("PeriodTotal") + "";
                            PeriodTotalPrice = json.getString("PeriodTotalPrice") + "";
                            EFFDATE = json.getString("EFFDATE") + "";
                            DatePayment = json.getString("DatePayment") + "";
                            ChanelName = json.getString("ChanelName") + "";
                            TeamName = json.getString("TeamName") + "";
                            SalePaymentPeriod_SalePaymentPeriodID = json.getString("SalePaymentPeriod_SalePaymentPeriodID") + "";
                            SalePaymentPeriod_RefNo = json.getString("SalePaymentPeriod_RefNo") + "";
                            SalePaymentPeriod_PaymentPeriodNumber = json.getString("SalePaymentPeriod_PaymentPeriodNumber") + "";
                            SalePaymentPeriod_PaymentAmount = json.getString("SalePaymentPeriod_PaymentAmount") + "";
                            SalePaymentPeriod_Discount = json.getString("SalePaymentPeriod_Discount") + "";
                            SalePaymentPeriod_NetAmount = json.getString("SalePaymentPeriod_NetAmount") + "";
                            SalePaymentPeriod_PaymentComplete = json.getString("SalePaymentPeriod_PaymentComplete") + "";
                            SalePaymentPeriod_TripID = json.getString("SalePaymentPeriod_TripID") + "";
                            SalePaymentPeriod_CreateBy = json.getString("SalePaymentPeriod_CreateBy") + "";
                            SalePaymentPeriod_LastUpdateBy = json.getString("SalePaymentPeriod_LastUpdateBy") + "";
                            SalePaymentPeriod_CloseAccountDiscountAmount = json.getString("SalePaymentPeriod_CloseAccountDiscountAmount") + "";
                            PaymentID = json.getString("Payment_paymentID") + "";
                            OrganizationCode = json.getString("Payment_OrganizationCode") + "";
                            SendMoneyID = json.getString("Payment_SendMoneyID") + "";
                            PaymentType = json.getString("Payment_PaymentType") + "";
                            PayPartial = json.getString("Payment_PayPartial") + "";
                            BankCode = json.getString("Payment_BankCode") + "";
                            ChequeNumber = json.getString("Payment_ChequeNumber") + "";
                            ChequeBankBranch = json.getString("Payment_ChequeBankBranch") + "";
                            ChequeDate = json.getString("Payment_ChequeDate") + "";
                            CreditCardNumber = json.getString("Payment_CreditCardNumber") + "";
                            CreditCardApproveCode = json.getString("Payment_CreditCardApproveCode") + "";
                            CreditEmployeeLevelPath = json.getString("Payment_CreditEmployeeLevelPath") + "";
                            TripID = json.getString("Payment_TripID") + "";
                            Status = json.getString("Payment_Status") + "";
                            RefNo = json.getString("Payment_RefNo") + "";
                            PayPeriod = json.getString("Payment_PayPeriod") + "";
                            PAYAMT = json.getString("Payment_PAYAMT") + "";
                            CashCode = json.getString("Payment_CashCode") + "";
                            EmpID = json.getString("Payment_EmpID") + "";
                            TeamCode = json.getString("Payment_TeamCode") + "";
                            receiptkind = json.getString("Payment_receiptkind") + "";
                            Kind = json.getString("Payment_Kind") + "";
                            BookNo = json.getString("Payment_BookNo") + "";
                            ReceiptNo = json.getString("Payment_ReceiptNo") + "";
                            CreateBy = json.getString("Payment_CreateBy") + "";
                            LastUpdateBy = json.getString("Payment_LastUpdateBy") + "";


                            CreateDate = json.getJSONObject("Payment_CreateDate").getString("date") + "";

                            LastUpdateDate = json.getJSONObject("Payment_LastUpdateDate").getString("date") + "";


                            // PayDate=json.getJSONObject("Payment_PayDate").getString("date")+"";
                            // SyncedDate=json.getJSONObject("Payment_SyncedDate").getString("date")+"";
                            // DatePayment1=json.getJSONObject("DatePayment1").getString("date")+"";
                            // SalePaymentPeriod_CreateDate=json.getJSONObject("SalePaymentPeriod_CreateDate").getString("date")+"";
                            //SalePaymentPeriod_PaymentDueDate=json.getJSONObject("SalePaymentPeriod_PaymentDueDate").getString("date")+"";
                            // SalePaymentPeriod_PaymentAppointmentDate=json.getJSONObject("SalePaymentPeriod_PaymentAppointmentDate").getString("date")+"";
                            // SalePaymentPeriod_LastUpdateDate=json.getJSONObject("SalePaymentPeriod_LastUpdateDate").getString("date")+"";
                            //SalePaymentPeriod_SyncedDate=json.getJSONObject("SalePaymentPeriod_SyncedDate").getString("date")+"";


                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date effDate = null;
                            Date payDate = null;
                            Date creatDate = null;
                            Date updateDate = null;

                            payDate = sdf.parse(DatePayment);
                            effDate = sdf.parse(EFFDATE);
                            try {


                                creatDate = sdf.parse(json.getJSONObject("Payment_CreateDate").getString("date"));
                                updateDate = sdf.parse(json.getJSONObject("Payment_LastUpdateDate").getString("date"));

                            } catch (ParseException ex) {
                                Log.e("Exception", ex.getLocalizedMessage());
                            }


                            if (!info.PaymentID.equals(PaymentID)) {
                                Log.e("PaymentID data", PaymentID);

                                paymentInfo = new PaymentInfo();
                                paymentInfo.ReceiptCode = ReceiptCode;
                                paymentInfo.CONTNO = CONTNO;
                                paymentInfo.EFFDATE = effDate;
                                paymentInfo.CustomerName = CustomerName;
                                paymentInfo.IDCard = IDCard;
                                paymentInfo.ProductName = ProductName;
                                paymentInfo.ProductCode = ProductSerialNumber;
                                paymentInfo.ProductModel = MODEL;


                                paymentInfo.MODEL = MODEL;
                                paymentInfo.ProductSerialNumber = ProductSerialNumber;

                               // paymentInfo.CloseAccountOutstandingAmount = Float.parseFloat(TotalPayment);

                                paymentInfo.CloseAccountDiscountAmount = Float.parseFloat(SalePaymentPeriod_CloseAccountDiscountAmount);

                                // tvProductNo.setText(payments.get(position).ProductSerialNumber);

                                //paymentInfo.PaymentPeriodNumber= Integer.parseInt(PeriodTotal);
                                //paymentInfo.MODE=0;
                                paymentInfo.VoidStatus = true;
                                paymentInfo.status_online=1;
                                paymentInfo.PeriodTotal=PeriodTotal;
                                paymentInfo.TotalPaymentText=TotalPaymentText;
                                paymentInfo.PeriodTotalPrice=PeriodTotalPrice;
                                paymentInfo.TotalPayment=TotalPayment;
                                paymentInfo.MaxPaymentPeriodNumber=MaxPaymentPeriodNumber;

                               // paymentInfo.PaymentPeriodNumber= 100;
                               // paymentInfo.BalancesOfPeriod= 1;

                                //TotalPayment

                                paymentInfo.PaymentID = PaymentID;
                                paymentInfo.OrganizationCode = OrganizationCode;
                                paymentInfo.SendMoneyID = SendMoneyID;
                                paymentInfo.PaymentType = PaymentType;
//                                paymentInfo.PayPartial = json.getString("Payment_PayPartial").equals("0") ? false : true;
                                paymentInfo.BankCode = BankCode;
                                paymentInfo.ChequeNumber = ChequeNumber;
                                paymentInfo.ChequeBankBranch = ChequeBankBranch;
                                paymentInfo.ChequeDate = ChequeDate;
                                paymentInfo.CreditCardNumber = CreditCardNumber;
                                paymentInfo.CreditCardApproveCode = CreditCardApproveCode;
                                paymentInfo.CreditEmployeeLevelPath = CreditEmployeeLevelPath;
                                paymentInfo.TripID = TripID;
                                paymentInfo.Status = Status;
                                paymentInfo.RefNo = RefNo;
                                paymentInfo.PayPeriod = PayPeriod;
                                paymentInfo.PayDate = payDate;
                                paymentInfo.PAYAMT = Float.parseFloat(PAYAMT);
                                paymentInfo.CashCode = CashCode;
                                paymentInfo.EmpID = EmpID;
                                paymentInfo.TeamCode = TeamCode;
                                paymentInfo.receiptkind = receiptkind;
                                paymentInfo.Kind = Kind;
                                paymentInfo.BookNo = BookNo;
                                paymentInfo.ReceiptNo = ReceiptNo;
//                                paymentInfo.CreateDate = creatDate;
                                paymentInfo.CreateBy = CreateBy;
//                                paymentInfo.LastUpdateDate = updateDate;
                                paymentInfo.LastUpdateBy = LastUpdateBy;

                                payments.add(payments.size(), paymentInfo);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("initail data", e.getLocalizedMessage());
                        }
                    }


                    payments.addAll(payments.size(), paymentInfos);
                    Log.e("Payment size2", String.valueOf(payments.size()));


                    prepareReceiptForVoid();
                    setViewPagerItemsWithAdapter();
                    setUiPageViewController();
                    myViewPagerAdapter.notifyDataSetChanged();

                }




            } catch (Exception ex) {

            //    Log.e("catch", ex.getLocalizedMessage());

            }

        }
    }









                    }













