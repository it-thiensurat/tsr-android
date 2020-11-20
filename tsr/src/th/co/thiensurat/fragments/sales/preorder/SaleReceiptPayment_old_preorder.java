package th.co.thiensurat.fragments.sales.preorder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.controller.PaymentController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.fragments.credit.credit.CreditListFragment;
import th.co.thiensurat.fragments.document.manual.preorder.ManualDocumentDetailFragment_preorder2;
import th.co.thiensurat.fragments.payment.first.FirstPaymentMainMenuFragment;
import th.co.thiensurat.fragments.payment.next.NextPaymentListFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.sales.SalePhotographyFragment;
import th.co.thiensurat.views.ViewTitle;

public class SaleReceiptPayment_old_preorder extends BHFragment {

    public boolean forcePrint;
    public SaleReceiptPayment_old_preorder(){
        forcePrint = false;
    }

    private final String STATUS_CODE = "09";

    public static class Data extends BHParcelable {
        public String SalePaymentPeriodID;
        public String PaymentID;
        public String ReceiptID;
        public int resTitle;
        public Date selectedDate;
    }

    private Data data;

    private List<PaymentInfo> payments;

    @InjectView
    public ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    @InjectView
    public LinearLayout viewPagerCountDots;
    private int dotsCount;
    private TextView[] dots;
    private int currentViewPosition = 0;

    @InjectView
    public ViewTitle lblTitle;

    @Override
    protected int titleID() {
        data = getData();
        int titleID1 = 0;
        switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
            case ViewCompletedContract:
                titleID1 = (data != null && data.resTitle != 0) ? data.resTitle : R.string.title_sales_preorder;
                break;
            case Sale:
                titleID1 = R.string.title_sales_preorder;
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

        initViews();


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
                        }else{
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
                ManualDocumentDetailFragment_preorder2.Data data1 = new ManualDocumentDetailFragment_preorder2.Data();
                data1.DocumentNumber = payments.get(viewPager.getCurrentItem()).ReceiptID;
                data1.DocumentNo = payments.get(viewPager.getCurrentItem()).ReceiptCode;
                data1.DocumentType = DocumentHistoryController.DocumentType.Receipt.toString();

                ManualDocumentDetailFragment_preorder2 fmManualDocDetail = BHFragment.newInstance(ManualDocumentDetailFragment_preorder2.class, data1);
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





                    CreditListFragment.Data input = new CreditListFragment.Data();
                    input.selectedDate = data.selectedDate;
                    CreditListFragment fragment = BHFragment.newInstance(CreditListFragment.class, input);
                    activity.showView(fragment);


                   // activity.showNextView(BHFragment.newInstance(CreditMainFragment_intro.class));



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

        try {
            if (!payments.get(0).EmpID.equals(BHPreference.employeeID()) || payments.get(0).VoidStatus == true) {
                activity.setViewProcessButtons(listId, View.GONE);
            } else {
                activity.setViewProcessButtons(listId, View.VISIBLE);
            }
        }
        catch (Exception ex){

        }

        /*** [END] :: Fixed - [BHPROJ-0025-815] :: [Android-Reprint ใบสัญญา+ใบเสร็จ] กรณีเป็นฝ่ายเก็บเงินจะ Re-Print ได้เฉพาะใบเสร็จรับเงินที่เค้าเป็นคนเก็บเท่านั้น จะไม่สามารถกลับไป Re-Print ใบสัญญา หรือ ใบเสร็จรับเงินของคนอื่นได้  ***/



        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String currentDate = df.format(c.getTime());

        String ReceiptDate = df.format(payments.get(0).PayDate);

        if (!ReceiptDate.equals(currentDate)) {
            activity.setViewProcessButtons(listId, View.GONE);
        }


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

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void prepareReceiptForVoid(){
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initViews() {

        Log.e("page_test","moomoo");
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
                    setViewPagerItemsWithAdapter();
                    setUiPageViewController();
                    if(forcePrint == true) {
                        activity.forceButtonClick(R.string.button_print);
                    }

                } else {
                    showLastView();
                    showDialog("แจ้งเตือน", "ไม่พบข้อมูลใบเสร็จ");
                }
            }
        }).start();


    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private List<PaymentInfo> items;

        public MyViewPagerAdapter(List<PaymentInfo> payments) {
            this.items = payments;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_sale_receipt_payment_item, container, false);

            DebtorCustomerInfo debtorCustomerInfo = TSRController.getDebCustometByID(payments.get(position).CustomerID);
            AddressInfo addressInfo = TSRController.getAddress(payments.get(position).RefNo, AddressInfo.AddressType.AddressInstall);
            //EmployeeInfo employeeInfo = TSRController.getEmployeeByEmployeeIDAndPositionCodeSaleAndCerdit(BHPreference.organizationCode(), payments.get(position).CreateBy);
            //EmployeeInfo employeeInfo = TSRController.getEmployeeByTreeHistoryIDAndEmployeeID(BHPreference.organizationCode(), payments.get(position).CreditEmployeeLevelPath, payments.get(position).CreateBy);
            String bahtLabel = " บาท";

            TextView txtReceiptHeadTitle = (TextView) view.findViewById(R.id.txtReceiptHeadTitle);

            try {
                txtReceiptHeadTitle.setText("ใบรับเงิน" + (payments.get(position).VoidStatus == true ? "  (ใบเสร็จถูกยกเลิก)" : ""));

            }
            catch (Exception ex){

            }

            TextView tvReceiptDate = (TextView) view.findViewById(R.id.tvReceiptDate); //วันที่รับเงิน
            tvReceiptDate.setText(BHUtilities.dateFormat(payments.get(position).PayDate));

            TextView tvReceiptNo = (TextView) view.findViewById(R.id.tvReceiptNo); //เลขที่ใบเสร็จ

           // tvReceiptNo.setText(payments.get(position).ReceiptCode);
            payments.get(position).ReceiptCode=BHApplication.getInstance().getPrefManager().getPreferrence("getReceiptCode");
            tvReceiptNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getReceiptCode"));

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

            Log.e("xxx","xxx");

            TextView tvContractNo = (TextView) view.findViewById(R.id.tvContractNo); //เลขที่สัญญา
            //tvContractNo.setText(payments.get(position).CONTNO);

            payments.get(position).CONTNO=BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo");
            tvContractNo.setText(BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo"));

            TextView tvContractDate = (TextView) view.findViewById(R.id.tvContractDate); //วันที่ทำสัญญา
            tvContractDate.setText(BHUtilities.dateFormat(payments.get(position).EFFDATE));

            TextView tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName); //ชื่อลูกค้า
            tvCustomerName.setText(debtorCustomerInfo.CustomerFullName());

            TextView tvCitizenNo = (TextView) view.findViewById(R.id.tvCitizenNo); //เลขที่บัตรประชาชน
            tvCitizenNo.setText(debtorCustomerInfo.IDCard);

            TextView tvCustomerAddress = (TextView) view.findViewById(R.id.tvCustomerAddress); //ที่อยู่ติดตั้ง
            tvCustomerAddress.setText(addressInfo.Address());

            TextView tvProductName = (TextView) view.findViewById(R.id.tvProductName); //ชื่อสินค้า
            tvProductName.setText(payments.get(position).ProductName);

            TextView textViewModel = (TextView) view.findViewById(R.id.textViewModel); //รุ่นสินค้า
            textViewModel.setText(payments.get(position).MODEL);

            TextView tvProductNo = (TextView) view.findViewById(R.id.tvProductNo); //รหัสสินค้า
            tvProductNo.setText(payments.get(position).ProductSerialNumber);





            /**ส่วนลดตัดสด**/
            LinearLayout llDiscount = (LinearLayout) view.findViewById(R.id.llDiscount); //ส่วนลดตัดสด ถ้าไม่มีให้ซ่อน (แสดง/ซ่อน LinearLayout)
            if (payments.get(position).CloseAccountPaymentPeriodNumber == payments.get(position).PaymentPeriodNumber && payments.get(position).BalancesOfPeriod == 0) {
                llDiscount.setVisibility(View.VISIBLE);

                TextView tvCloseAccountOutstandingAmountLabel = (TextView) view.findViewById(R.id.tvCloseAccountOutstandingAmountLabel); //ชำระงวดที่ 2-n
                TextView tvCloseAccountOutstandingAmount = (TextView) view.findViewById(R.id.tvCloseAccountOutstandingAmount); //ชำระงวดที่ 2-n จำนวน
                TextView tvDiscountAmount = (TextView) view.findViewById(R.id.tvDiscountAmount); //ส่วนลดตัดสด

                tvCloseAccountOutstandingAmountLabel.setText(String.format("ชำระงวดที่ %d-%d", payments.get(position).PaymentPeriodNumber, payments.get(position).MODE));
                tvCloseAccountOutstandingAmount.setText(BHUtilities.numericFormat(payments.get(position).CloseAccountOutstandingAmount) + bahtLabel);

                tvDiscountAmount.setText(BHUtilities.numericFormat(payments.get(position).CloseAccountDiscountAmount) + bahtLabel);
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
                    tvPeriodAmountLabel.setText("จำนวนที่ชำระ");
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
                        if(payments.get(position).Amount == 0){
                            tvPeriodAmountLabel.setText(String.format("ค่างวดที่ %d/%d\n", payments.get(position).PaymentPeriodNumber, payments.get(position).MODE));

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
                tvPeriodAmount.setText(periodAmount, TextView.BufferType.SPANNABLE);

                txtThaiBaht.setText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(payments.get(position).CloseAccountNetAmount)));
            } else {
                SpannableString periodAmount = new SpannableString(BHUtilities.numericFormat(payments.get(position).Amount) + bahtLabel);
                periodAmount.setSpan(new ForegroundColorSpan(Color.RED), 0, BHUtilities.numericFormat(payments.get(position).Amount).length(), 0);//สี
                periodAmount.setSpan(new StyleSpan(Typeface.BOLD), 0, BHUtilities.numericFormat(payments.get(position).Amount).length(), 0);//ตัวหนา
                periodAmount.setSpan(new TextAppearanceSpan(activity, R.style.TextView_Value2), 0, BHUtilities.numericFormat(payments.get(position).Amount).length(), 0);//ขนาดตัว
                tvPeriodAmount.setText(periodAmount, TextView.BufferType.SPANNABLE);

                txtThaiBaht.setText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(payments.get(position).Amount)));

            }

            try {
                txtThaiBaht.setVisibility(payments.get(position).VoidStatus ? View.GONE : View.VISIBLE);
                tvPeriodAmount.setText(payments.get(position).VoidStatus ? "ยกเลิกการชำระเงิน" : tvPeriodAmount.getText());

            }
            catch (Exception ex){

            }

            //tvPeriodAmount.setText(BHUtilities.numericFormat(payments.get(position).Amount) + bahtLabel);
            /*** [END] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับ***/



            try {
                //เพิ่มการตรวจ VoidStatus = true ให้ปิดการแสดงผล เพราะมีการปรับข้อมูลทำให้ไม่สามารถคำนวณค่าได้ถูกต้อง
                if ((payments.get(position).CloseAccountPaymentPeriodNumber == payments.get(position).PaymentPeriodNumber && payments.get(position).BalancesOfPeriod == 0) || payments.get(position).VoidStatus) {
                    LinearLayout llBalancesOfPeriod = (LinearLayout) view.findViewById(R.id.llBalancesOfPeriod); //ยอดเงินคงเหลือของงวด ถ้าไม่มีให้ซ่อน (แสดง/ซ่อน LinearLayout)
                    llBalancesOfPeriod.setVisibility(View.GONE);

                    LinearLayout llBalanceAmount = (LinearLayout) view.findViewById(R.id.llBalanceAmount); //ยอดคงเหลือ ถ้าไม่มีให้ซ่อน (แสดง/ซ่อน LinearLayout)
                    llBalanceAmount.setVisibility(View.GONE);

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
                        }
                        tvBalancesOfPeriod.setText(BHUtilities.numericFormat(payments.get(position).BalancesOfPeriod) + bahtLabel);
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
                            tvBalanceAmountLabel.setText(String.format("คงเหลืองวดที่ %d - %d", payments.get(position).PaymentPeriodNumber + 1, payments.get(position).MODE));
                        }
                        tvBalanceAmount.setText(BHUtilities.numericFormat(payments.get(position).Balances - payments.get(position).BalancesOfPeriod) + bahtLabel);
                    }
                }
            }
            catch (Exception EX){

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

            txtSaleEmpName.setText(String.format("(%s)", payments.get(position).SaleEmployeeName != null ? payments.get(position).SaleEmployeeName : ""));
            txtSaleTeamName.setText(String.format("(ทีม %s)", payments.get(position).TeamCode != null ? payments.get(position).TeamCode : ""));
            //txtSaleTeamName.setText(String.format("(ทีม %s)", payments.get(position).TeamCode != null ? payments.get(position).CashCode : ""));

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

            }
            catch (Exception ex){

            }

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

            Button btnPrintImage = (Button) view.findViewById(R.id.btnPrintImage);
            btnPrintImage.setVisibility(View.GONE);


            TextView txtHeader = (TextView) view.findViewById(R.id.txtReceiptHeadTitle);

            try {
                if (!(!payments.get(position).EmpID.equals(BHPreference.employeeID()) || payments.get(position).VoidStatus == true))
                {
                    txtHeader.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new PrinterController(activity).newImagePrintReceipt(payments.get(position));
                        }
                    });
                }
            }
            catch (Exception ex){

            }



            ((ViewPager) container).addView(view);

            return view;
        }

        @Override
        public int getCount() {
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

}
