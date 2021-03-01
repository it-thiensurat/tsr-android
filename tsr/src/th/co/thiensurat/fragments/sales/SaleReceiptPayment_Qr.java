package th.co.thiensurat.fragments.sales;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.PrinterController;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.fragments.credit.credit.CreditListFragment;
import th.co.thiensurat.fragments.document.manual.ManualDocumentDetailFragment;
import th.co.thiensurat.fragments.payment.first.FirstPaymentMainMenuFragment;
import th.co.thiensurat.fragments.payment.next.NextPaymentListFragment;
import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaleReceiptPayment_Qr#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleReceiptPayment_Qr extends BHFragment {

    public boolean forcePrint;
    public SaleReceiptPayment_Qr(){
        forcePrint = false;
    }

    private final String STATUS_CODE = "09";

    public static class Data extends BHParcelable {
        protected ContractInfo contract;
        public Date selectedDate;
        public String paymentType;
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
//            case ViewCompletedContract:
//                titleID1 = (data != null && data.resTitle != 0) ? data.resTitle : R.string.title_sales;
//                break;
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
        return R.layout.fragment_sale_receipt_payment__qr;
    }

    @Override
    protected int[] processButtons() {
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
        getQrcodeReceipt();
//        Log.e("Contract for qr", String.valueOf(data.contract));

        switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
            case FirstPayment:
                lblTitle.setText(R.string.caption_payment_first);
                break;
            case Sale:
                lblTitle.setText(R.string.caption_payment_first);
//                saveStatusCode();
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
//                                    printDocument(newPayments);
                                    dialog.cancel();
                                }
                            });
                        }else{
                            setupAlert = setupAlert.setPositiveButton("พิมพ์ทั้งหมด", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    printDocument(newPayments);
                                    dialog.cancel();
                                }
                            }).setNeutralButton(String.format("พิมพ์เฉพาะหน้านี้", viewPager.getCurrentItem() + 1), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    List<PaymentInfo> listInfo = new ArrayList<>();
                                    listInfo.add(payments.get(viewPager.getCurrentItem()));
//                                    printDocument(listInfo);
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
//                        printDocument(newPayments);
                    }
                } else {
//                    printDocument(payments);
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
                    CreditListFragment.Data input = new CreditListFragment.Data();
                    input.selectedDate = data.selectedDate;
                    CreditListFragment fragment = BHFragment.newInstance(CreditListFragment.class, input);
                    activity.showView(fragment);
                } else if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.NextPayment.toString())) {
                    activity.showView(new NextPaymentListFragment());
                }
                break;

            default:
                break;
        }
    }

    private void getQrcodeReceipt() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.getQrReceipt("20109537");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson=new Gson();
                    try {
//                        JSONObject jsonObject=new JSONObject(gson.toJson(response.body()));
                        JSONArray jsonArray = new JSONArray(gson.toJson(response.body()));
//                        Log.e("JSONObjec", String.valueOf(jsonArray));
//                        JSONArray array = jsonObject.getJSONArray("data");
                        String obj = "";
//                        Log.e("for qr receipt", String.valueOf(array));
                        List<PaymentInfo> paymentInfoList = new ArrayList<>();
                        PaymentInfo paymentInfo;
                        for (int i = 0; i < jsonArray.length(); i++) {
//                            obj = jsonArray.getJSONObject(i).getString("ReceiptCode");
//                            paymentInfoList.set(i, )
                            paymentInfo = new PaymentInfo();
                            paymentInfo.ReceiptCode = jsonArray.getJSONObject(i).getString("ReceiptCode");
                            paymentInfo.CONTNO = jsonArray.getJSONObject(i).getString("CONTNO");
                            paymentInfo.CustomerName = jsonArray.getJSONObject(i).getString("CustomerName");
                            paymentInfo.ProductName = jsonArray.getJSONObject(i).getString("ProductName");
                            paymentInfo.MODEL = data.contract.MODEL;
                            paymentInfo.ProductSerialNumber = data.contract.ProductSerialNumber;
//                            paymentInfo.PayDate = jsonArray.getJSONObject(i).getString("DatePayment");
                            paymentInfo.Amount = Float.parseFloat(jsonArray.getJSONObject(i).getString("Amount").replace(",", ""));
                            paymentInfo.PaymentPeriodNumber = jsonArray.getJSONObject(i).getInt("PaymentPeriodNumber");
                            paymentInfo.MODE = jsonArray.getJSONObject(i).getInt("maxPeriod");
                            paymentInfo.EmpID = data.contract.EmpID;
                            paymentInfo.EFFDATE = data.contract.EFFDATE;
                            paymentInfo.IDCard = data.contract.IDCard;
                            paymentInfo.MODE = data.contract.MODE;
                            paymentInfo.RefNo = data.contract.RefNo;
                            paymentInfo.SaleEmployeeName = data.contract.SaleEmployeeName;
                            paymentInfo.TeamCode = data.contract.SaleTeamCode;
                            paymentInfo.VoidStatus = false;
                            paymentInfo.PaymentType = data.paymentType;
                            paymentInfo.Balances = Float.parseFloat(jsonArray.getJSONObject(i).getString("balance").replace(",", ""));

                            paymentInfoList.add(paymentInfo);
                        }

                        payments = paymentInfoList;
                        Log.e("Payment list", String.valueOf(paymentInfoList));
                        myViewPagerAdapter = new MyViewPagerAdapter(paymentInfoList);
                        viewPager.setAdapter(myViewPagerAdapter);
                        viewPager.setCurrentItem(currentViewPosition);
                        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
                        setUiPageViewController();
                        Log.e("ReceiptCode", String.valueOf(obj));
                    } catch (JSONException e) {
                        Log.e("JSONException", e.getLocalizedMessage());
                    }
                }
                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("onFailure", t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            Log.e("Exception", e.getLocalizedMessage());
        }
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
//            List<Integer> listId = new ArrayList<Integer>();
//            listId.add(R.string.button_print);
//            listId.add(R.string.button_save_manual_receipt);
//
//            if (!payments.get(position).EmpID.equals(BHPreference.employeeID()) || payments.get(position).VoidStatus == true) {
//                activity.setViewProcessButtons(listId, View.GONE);
//            } else {
//                activity.setViewProcessButtons(listId, View.VISIBLE);
//            }
            /*** [END] :: Fixed - [BHPROJ-0025-815] :: [Android-Reprint ใบสัญญา+ใบเสร็จ] กรณีเป็นฝ่ายเก็บเงินจะ Re-Print ได้เฉพาะใบเสร็จรับเงินที่เค้าเป็นคนเก็บเท่านั้น จะไม่สามารถกลับไป Re-Print ใบสัญญา หรือ ใบเสร็จรับเงินของคนอื่นได้  ***/
//            Calendar c = Calendar.getInstance();
//            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//            String currentDate = df.format(c.getTime());
//
//            String ReceiptDate = df.format(payments.get(position).PayDate);

//            if (!ReceiptDate.equals(currentDate)) {
//                activity.setViewProcessButtons(listId, View.GONE);
//            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

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
            String bahtLabel = " บาท";
            TextView txtReceiptHeadTitle = (TextView) view.findViewById(R.id.txtReceiptHeadTitle);
            txtReceiptHeadTitle.setText("ใบรับเงิน");
            TextView tvReceiptDate = (TextView) view.findViewById(R.id.tvReceiptDate); //วันที่รับเงิน
            tvReceiptDate.setText(BHUtilities.dateFormat(payments.get(position).PayDate));

            TextView tvReceiptNo = (TextView) view.findViewById(R.id.tvReceiptNo); //เลขที่ใบเสร็จ
            tvReceiptNo.setText(payments.get(position).ReceiptCode);

            LinearLayout llReferenceNo = (LinearLayout) view.findViewById(R.id.llReferenceNo);//เลขที่อ้างอิง
            TextView tvReferenceNo = (TextView) view.findViewById(R.id.tvReferenceNo);
            if (payments.get(position).ManualVolumeNo != null && payments.get(position).ManualRunningNo > 0) {
                String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(payments.get(position).ManualVolumeNo), payments.get(position).ManualRunningNo).replace(' ', '0');
                tvReferenceNo.setText(ManualDocumentBookRunningNo);
            } else {
                llReferenceNo.setVisibility(View.GONE);
            }

            TextView tvContractNo = (TextView) view.findViewById(R.id.tvContractNo); //เลขที่สัญญา
            tvContractNo.setText(payments.get(position).CONTNO);

            TextView tvContractDate = (TextView) view.findViewById(R.id.tvContractDate); //วันที่ทำสัญญา
            tvContractDate.setText(BHUtilities.dateFormat(payments.get(position).EFFDATE));

            TextView tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName); //ชื่อลูกค้า
            tvCustomerName.setText(payments.get(position).CustomerName);

            TextView tvCitizenNo = (TextView) view.findViewById(R.id.tvCitizenNo); //เลขที่บัตรประชาชน
            tvCitizenNo.setText(payments.get(position).IDCard);

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

            txtThaiBaht.setVisibility(View.GONE);
//            txtThaiBaht.setVisibility(payments.get(position).VoidStatus ? View.GONE : View.VISIBLE);
//            tvPeriodAmount.setText(payments.get(position).VoidStatus ? "ยกเลิกการชำระเงิน" : tvPeriodAmount.getText());

            //tvPeriodAmount.setText(BHUtilities.numericFormat(payments.get(position).Amount) + bahtLabel);
            /*** [END] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับ***/


            //เพิ่มการตรวจ VoidStatus = true ให้ปิดการแสดงผล เพราะมีการปรับข้อมูลทำให้ไม่สามารถคำนวณค่าได้ถูกต้อง
            if ((payments.get(position).CloseAccountPaymentPeriodNumber == payments.get(position).PaymentPeriodNumber && payments.get(position).BalancesOfPeriod == 0)) {
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
//                if (payments.get(position).Balances - payments.get(position).BalancesOfPeriod == 0 || payments.get(position).VoidStatus) {
                if (payments.get(position).Balances - payments.get(position).BalancesOfPeriod == 0) {
                    llBalanceAmount.setVisibility(View.GONE);
                } else {
                    llBalanceAmount.setVisibility(View.VISIBLE);
                    if (payments.get(position).MODE == 1) {
                        tvBalanceAmountLabel.setText("คงเหลือเงินสด");
                    } else {
                        tvBalanceAmountLabel.setText(String.format("คงเหลืองวดที่ %d - %d", payments.get(position).PaymentPeriodNumber + 1, payments.get(position).MODE));
                    }
                    tvBalanceAmount.setText(BHUtilities.numericFormat(payments.get(position).Balances - payments.get(position).BalancesOfPeriod) + bahtLabel);
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
                case Qrcode:
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

            Button voidBtn = (Button) view.findViewById(R.id.btnVoidReceipt);
            voidBtn.setVisibility(view.GONE);
//            voidBtn.setVisibility(payments.get(position).CanVoid ? view.VISIBLE : view.GONE);
//            voidBtn.setText("ยกเลิกใบรับเงิน");
//            voidBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    currentViewPosition = position;
//                    AlertDialog.Builder setupAlert;
//                    setupAlert = new AlertDialog.Builder(activity)
//                            .setTitle("ยกเลิกใบเสร็จ")
//                            .setMessage("ต้องการยกเลิกใบเสร็จหมายเลข " + payments.get(position).ReceiptCode + " ใช่หรือไม่")
//                            .setCancelable(false);
//
//                    setupAlert = setupAlert.setPositiveButton("ใช่ ฉันต้องการยกเลิกใบเสร็จนี้", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            dialog.cancel();
//                            doVoidReceipt(payments.get(position).RefNo, payments.get(position).ReceiptID);
//                        }
//                    }).setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    setupAlert.show();
//
//                }
//            });

            Button btnPrintImage = (Button) view.findViewById(R.id.btnPrintImage);
            btnPrintImage.setVisibility(View.GONE);

            TextView txtHeader = (TextView) view.findViewById(R.id.txtReceiptHeadTitle);
//            if (!(!payments.get(position).EmpID.equals(BHPreference.employeeID())))
//            {
//                txtHeader.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        new PrinterController(activity).newImagePrintReceipt(payments.get(position));
//                    }
//                });
//            }

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
}