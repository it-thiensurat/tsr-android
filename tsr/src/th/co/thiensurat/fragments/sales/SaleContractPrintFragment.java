package th.co.thiensurat.fragments.sales;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.SignatureActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.PrinterController;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.controller.ThemalPrintController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.PackagePeriodDetailInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.views.ViewTitle;

import static android.app.Activity.RESULT_OK;
import static th.co.thiensurat.business.controller.TSRController.updateProductStock;
import static th.co.thiensurat.data.controller.DocumentController.getAlbumStorageDir;
import static th.co.thiensurat.data.controller.DocumentController.getResizedBitmap;

public class SaleContractPrintFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public int resTitle;
    }

    // public static String FRAGMENT_SALE_CONTRACT_PRINT_TAG =
    // "sale_first_payment_tag";
    private static final String STATUS_CODE = "07";
    private static String TSR_COMMITTEE_NAME = "นายวิรัช  วงศ์นิรันดร์";

    @InjectView
    private ScrollView scrollView1;
    @InjectView
    private TextView txtEFFDate;
    @InjectView
    private TextView txtRef;
    @InjectView
    private TextView txtContNo;
    @InjectView
    private TextView txtIDCard;
    @InjectView
    private TextView txtCustomerFullName;
    @InjectView
    private TextView txtAddressIDCard;
    @InjectView
    private TextView txtAddressInstall;
    @InjectView
    private TextView txtProduct;
    @InjectView
    private TextView txtProductSerialNumber;
    @InjectView
    private LinearLayout linearLayoutSaleAmount;
    @InjectView
    private TextView txtSales;
    @InjectView
    private LinearLayout linearLayoutSaleDiscount;
    @InjectView
    private TextView txtTradeInDiscount;
    @InjectView
    private LinearLayout linearLayoutSaleNetAmount;
    @InjectView
    private TextView txtTotalPrice;
    @InjectView
    private LinearLayout linearLayoutFirstPaymentAmount;
    @InjectView
    private TextView txtFirstPaymentAmount;
    @InjectView
    private LinearLayout linearLayoutNextPaymentAmount;
    @InjectView
    private TextView txtNextPaymentAmountLabel;
    @InjectView
    private TextView txtNextPaymentAmountValue;
    @InjectView
    private TextView txtTSRCommittee;
    @InjectView
    private TextView txtCustomer;
    @InjectView
    private TextView txtCustomerName;
    @InjectView
    private TextView txtSaleLeaderName;
    @InjectView
    private TextView txtSaleEmpName;
    @InjectView
    private TextView txtSaleTeamName;
    @InjectView
    private TextView txtSaleEmpID;
    @InjectView
    private TextView txtPaymentAmount;
    @InjectView
    private ViewTitle txtCaption;
    @InjectView
    private TextView textViewModel, lblCustomerFullName;
    @InjectView
    private LinearLayout linearLayoutPayment;
    @InjectView
    private TextView txtViewSale;
    @InjectView
    private LinearLayout linearLayoutCredit;
    @InjectView
    private LinearLayout linearLayoutCash;
    @InjectView
    private TextView txtAddressInstallPhoneNumber, txtAddressIDCardPhoneNumber;
    @InjectView
    private LinearLayout llAddressInstallPhoneNumber, llAddressIDCardPhoneNumber;

    @InjectView
    private TextView editTextSign;
    @InjectView
    private LinearLayout signature_layout;
    @InjectView
    private Button btnVoidContract;
    @InjectView
    private Button btnSignature;
    @InjectView ImageView imgSignature;


    private ContractInfo contract = null;
    // private List<SalePaymentPeriodInfo> sppList = null;
    private AddressInfo addressIDCard = null;
    private AddressInfo addressInstall = null;
    // private List<PackagePeriodDetailInfo> packagePeriodDetailList = null;
    private PackagePeriodDetailInfo maxPackagePeriod = null;
    private List<SalePaymentPeriodInfo> paymentPeriodOutput;
    /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
    //ManualDocumentInfo manual;
    /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/


    private Data data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getData();
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_contract_print;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        if (data != null && data.resTitle != 0) {
            return data.resTitle;
        } else {
            return R.string.title_sales;
        }
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        int[] ret;
        if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.Sale)
            ret = new int[]{R.string.button_pay, R.string.button_print}; // ,
            // R.string.button_save_manual_contract
        else if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.ViewCompletedContract)
            ret = new int[]{R.string.button_back, R.string.button_print, R.string.button_receipt};// ,
            // R.string.button_save_manual_contract
        else
            ret = new int[]{R.string.button_back};
        return ret;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            saveStatusCode();
        }
        loadData();

        linearLayoutPayment.setVisibility(View.GONE);
    }

    private void loadData() {
        (new BackgroundProcess(activity) {


            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                Log.e("Process", ProcessType.SendDocument.toString() + ": " + BHPreference.ProcessType());
                if (BHPreference.ProcessType().equals(ProcessType.SendDocument.toString())) {
                    contract = getContractByRefNoForSendDocuments(BHPreference.organizationCode(), BHPreference.RefNo());
                } else {
                    contract = new ContractController().getContractByRefNoNotCheckActive(BHPreference.organizationCode(), BHPreference.RefNo());
                }

                // YIM Change TSR_COMMITTEE_NAME
                if (BHGeneral.isOpenDepartmentSignature&&BHPreference.hasDepartmentSignatureImage()){
                    EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                    if (saleLeader != null) {
                        TSR_COMMITTEE_NAME = saleLeader.DepartmentHeadName;
                    }
                }
                addressIDCard = getAddress(BHPreference.RefNo(), AddressType.AddressIDCard);
                addressInstall = getAddress(BHPreference.RefNo(), AddressType.AddressInstall);
                // packagePeriodDetailList = getPackagePeriodDetail(
                // contract.OrganizationCode, contract.MODEL);
                //maxPackagePeriod = getMaxPackagePeriodDetailByModel(BHPreference.organizationCode(), contract.MODEL);
                //paymentPeriodOutput = getSalePaymentPeriodByRefNo(BHPreference.RefNo());
                paymentPeriodOutput = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(BHPreference.RefNo());
                // sppList = getSalePaymentPeriodByRefNo(BHPreference
                // .RefNo());
            }

            @Override
            protected void after() {
                if (contract != null) {

                    /*** [START] :: Fixed - [BHPROJ-0025-815] :: [Android-Reprint ใบสัญญา+ใบเสร็จ] กรณีเป็นฝ่ายเก็บเงินจะ Re-Print ได้เฉพาะใบเสร็จรับเงินที่เค้าเป็นคนเก็บเท่านั้น จะไม่สามารถกลับไป Re-Print ใบสัญญา หรือ ใบเสร็จรับเงินของคนอื่นได้  ***/
                    if(!contract.CreateBy.equals(BHPreference.employeeID())){
                        List<Integer> listId = new ArrayList<Integer>();
                        listId.add(R.string.button_print);

                        activity.setViewProcessButtons(listId, View.GONE);
                    } else {
                        /*** [START] :: Fixed - [BHPROJ-1036-8796] - ไม่ให้แก้ไขชื่อ ที่อยู่ และภาพถ่าย ข้ามวัน ให้แก้ไขได้ภายในวันที่ทำสัญญาเท่านั้น รวมทั้งการพิมพ์สัญญาต้องพิมพ์ข้ามวันไม่ได้ด้วย ***/
                        if (contract.EFFDATE != null) {
                            Calendar cEFFDATE = Calendar.getInstance();
                            cEFFDATE.setTime(contract.EFFDATE);

                            Calendar cStartDate = Calendar.getInstance();
                            cStartDate.set(Calendar.HOUR_OF_DAY, 0);
                            cStartDate.set(Calendar.MINUTE, 0);
                            cStartDate.set(Calendar.SECOND, 0);
                            cStartDate.set(Calendar.MILLISECOND, 0);

                            Calendar cEndDate = Calendar.getInstance();
                            cEndDate.set(Calendar.HOUR_OF_DAY, 23);
                            cEndDate.set(Calendar.MINUTE, 59);
                            cEndDate.set(Calendar.SECOND, 59);
                            cEndDate.set(Calendar.MILLISECOND, 999);

                            if (cEFFDATE.before(cStartDate) || cEFFDATE.after(cEndDate)) {
                                List<Integer> listId = new ArrayList<Integer>();
                                listId.add(R.string.button_print);

                                activity.setViewProcessButtons(listId, View.GONE);
                            }
                        }
                        /*** [END] :: Fixed - [BHPROJ-1036-8796] - ไม่ให้แก้ไขชื่อ ที่อยู่ และภาพถ่าย ข้ามวัน ให้แก้ไขได้ภายในวันที่ทำสัญญาเท่านั้น รวมทั้งการพิมพ์สัญญาต้องพิมพ์ข้ามวันไม่ได้ด้วย  ***/
                    }

                    if(contract.STATUS.equals("VOID")) {
                        List<Integer> listId = new ArrayList<Integer>();
                        listId.add(R.string.button_receipt);
                        activity.setViewProcessButtons(listId, View.GONE);
                    }
                    /*** [END] :: Fixed - [BHPROJ-0025-815] :: [Android-Reprint ใบสัญญา+ใบเสร็จ] กรณีเป็นฝ่ายเก็บเงินจะ Re-Print ได้เฉพาะใบเสร็จรับเงินที่เค้าเป็นคนเก็บเท่านั้น จะไม่สามารถกลับไป Re-Print ใบสัญญา หรือ ใบเสร็จรับเงินของคนอื่นได้  ***/


                    if (paymentPeriodOutput != null) {
                        String title = paymentPeriodOutput.size() > 1 ? "" + getResources().getString(R.string.sale_contract_payment) : ""
                                + getResources().getString(R.string.sale_contract_installment);
                        txtCaption.setText(title + (contract.STATUS.equals("VOID") ? " (สัญญานี้ถูกยกเลิกแล้ว) " : ""));
                        String titleLblCustomerFullName = paymentPeriodOutput.size() > 1 ? "" + getResources().getString(R.string.change_contract_customer_fullname) : ""
                                + getResources().getString(R.string.change_contract_customer_fullname_cash);
                        lblCustomerFullName.setText(titleLblCustomerFullName);
                    }
                    /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

                    /*manual = TSRController.getManualDocumentContractByDocumentNumber(contract.RefNo);
                    // int ManualRunningNo_Integer = manual.ManualRunningNo ;
                    if (manual != null) {
                        String ManualDocumentBookRunningNo = String.format("%4d", manual.ManualRunningNo).replace(' ', '0');
                        txtRef.setText(ManualDocumentBookRunningNo);
                    } else {
                        txtRef.setText("");
                    }*/

                    txtRef.setText(contract.ContractReferenceNo != null ? contract.ContractReferenceNo : "");

                    /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

                    txtEFFDate.setText(BHUtilities.dateFormat(contract.EFFDATE));
                    txtContNo.setText(BHUtilities.trim(contract.CONTNO) + (contract.STATUS.equals("VOID") ? " (สัญญานี้ถูกยกเลิกแล้ว) " : ""));
                    txtCustomerFullName.setText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));
                    txtIDCard.setText(BHUtilities.trim(contract.IDCard));
                    txtProduct.setText(BHUtilities.trim(contract.ProductName));
                    txtProductSerialNumber.setText(BHUtilities.trim(contract.ProductSerialNumber));
                    txtPaymentAmount.setText(BHUtilities.numericFormat(contract.PaymentAmount));
                    txtSales.setText(BHUtilities.numericFormat(contract.SALES));
                    txtTradeInDiscount.setText(BHUtilities.numericFormat(contract.TradeInDiscount));
                    txtTotalPrice.setText(BHUtilities.numericFormat(contract.TotalPrice));
                    textViewModel.setText(contract.MODEL);

                    if (contract.MODE == 1) {
                        if (contract.TradeInDiscount != 0) {
                            txtViewSale.setText("ราคา");
                        } else {
                            linearLayoutSaleDiscount.setVisibility(View.GONE);
                            linearLayoutSaleNetAmount.setVisibility(View.GONE);
                        }
                        linearLayoutFirstPaymentAmount.setVisibility(View.GONE);
                        linearLayoutNextPaymentAmount.setVisibility(View.GONE);
                    } else {
                        if (contract.TradeInDiscount != 0) {
                            txtViewSale.setText("ราคา");
                        } else {
                            linearLayoutSaleDiscount.setVisibility(View.GONE);
                            linearLayoutSaleNetAmount.setVisibility(View.GONE);
                        }
                    }

                    if (contract.MODE == 1) {
                        linearLayoutCash.setVisibility(View.VISIBLE);
                        linearLayoutCredit.setVisibility(View.GONE);
                        txtCustomer.setText(String.format("(%s%s)", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));

                    } else {
                        // YIM Dummy Signature image into view
                        /*
                        if(BHGeneral.isOpenDepartmentSignature) {
                            ThemalPrintController themalPrintController = new ThemalPrintController(null, null);
                            Bitmap imgSignature = themalPrintController.GenerateSignature("");

                            LinearLayout linearLayoutTSRCommittee = (LinearLayout) txtTSRCommittee.getParent();
                            ImageView imvSignature = new ImageView(getActivity());
                            imvSignature.setImageBitmap(imgSignature);
                            linearLayoutTSRCommittee.addView(imvSignature, 0);
                        }
                        */

                        linearLayoutCredit.setVisibility(View.VISIBLE);
                        linearLayoutCash.setVisibility(View.GONE);
                        txtTSRCommittee.setText(BHUtilities.trim("(" + TSR_COMMITTEE_NAME + ")"));
                        txtCustomerName.setText(String.format("(%s%s)", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));

                        // txtCustomerName.setText(BHUtilities.trim("("
                        // + contract.CustomerFullName + ")"));
                        txtSaleLeaderName.setText(BHUtilities.trim("("
                                + ((contract.upperEmployeeName == null || contract.upperEmployeeName.isEmpty()) ? "" : contract.upperEmployeeName) + ")"));
                        txtSaleEmpName.setText(BHUtilities.trim("(" + contract.SaleEmployeeName + ")"));
                        txtSaleTeamName.setText(BHUtilities.trim(""
                                + ((contract.SaleTeamName == null || contract.SaleTeamName.isEmpty()) ? "" : contract.SaleTeamName)));

                        txtSaleEmpID.setText(BHUtilities.trim("รหัส " + contract.SaleCode));

                    }


                    txtFirstPaymentAmount.setText(BHUtilities.numericFormat(contract.PaymentAmount - contract.TradeInDiscount));


                    /*if (addressIDCard != null) {
                        txtAddressIDCard.setText(BHUtilities.trim(addressIDCard.Address()));
                    }

                    if (addressInstall != null) {
                        txtAddressInstall.setText(BHUtilities.trim(addressInstall.Address()));
                    }*/

                    if( addressIDCard != null && addressInstall != null){
                        txtAddressIDCard.setText(BHUtilities.trim(addressIDCard.Address()));
                        txtAddressInstall.setText(BHUtilities.trim(addressInstall.Address()));

                        switch (contract.CustomerType) {
                            case "0":
                            case "2":
                                if (addressInstall.TelMobile != null && addressIDCard.TelMobile != null) {
                                    if(addressInstall.TelMobile.equals(addressIDCard.TelMobile)){
                                        llAddressIDCardPhoneNumber.setVisibility(View.GONE);
                                        txtAddressInstallPhoneNumber.setText(addressInstall.TelMobile);
                                    } else {
                                        txtAddressIDCardPhoneNumber.setText(addressIDCard.TelMobile);
                                        txtAddressInstallPhoneNumber.setText(addressInstall.TelMobile);
                                    }
                                } else {
                                    txtAddressIDCardPhoneNumber.setText(addressIDCard.TelMobile != null ? addressIDCard.TelMobile : "");
                                    txtAddressInstallPhoneNumber.setText(addressInstall.TelMobile != null ? addressInstall.TelMobile : "");
                                }

                                break;
                            case "1":
                                if (addressInstall.TelMobile != null && addressIDCard.TelMobile != null) {
                                    if(addressInstall.TelMobile.equals(addressIDCard.TelMobile)){
                                        llAddressIDCardPhoneNumber.setVisibility(View.GONE);
                                        txtAddressInstallPhoneNumber.setText(addressInstall.TelHome);
                                    } else {
                                        txtAddressIDCardPhoneNumber.setText(addressIDCard.TelHome);
                                        txtAddressInstallPhoneNumber.setText(addressInstall.TelHome);
                                    }
                                } else {
                                    txtAddressIDCardPhoneNumber.setText(addressIDCard.TelHome != null ? addressIDCard.TelHome : "");
                                    txtAddressInstallPhoneNumber.setText(addressInstall.TelHome != null ? addressInstall.TelHome : "");
                                }
                                break;
                        }
                    }


                    if (paymentPeriodOutput != null && paymentPeriodOutput.size() > 0) {
                        txtNextPaymentAmountLabel.setText(String.format("งวดที่ 2 ถึงงวดที่ %d ต้องชำระงวดละ", contract.MODE));

                        for (SalePaymentPeriodInfo item : paymentPeriodOutput) {
                            txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(item.NetAmount));
                        }
                        if (contract.MODE == 1) {
                            linearLayoutNextPaymentAmount.setVisibility(View.GONE);
                            // textViewInstallment.setText("");
                            // textViewNextPaymentCurrency.setText("");
                        }
                    }

                    btnVoidContract.setVisibility(View.GONE);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String currentDate = df.format(c.getTime());

                    String ContractDate = df.format(contract.EFFDATE);

                    if (!contract.STATUS.equals("VOID") && ContractDate.equals(currentDate) && Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.ViewCompletedContract) {
                        btnVoidContract.setVisibility(View.VISIBLE);
                    }


                    btnVoidContract.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder setupAlert;
                            setupAlert = new AlertDialog.Builder(activity)
                                    .setTitle("ยกเลิกสัญญา")
                                    .setMessage("ต้องการยกเลิกสัญญาหมายเลข " + contract.CONTNO  + " ใช่หรือไม่")
                                    .setCancelable(false);

                            setupAlert = setupAlert.setPositiveButton("ใช่ ฉันต้องการยกเลิกสัญญานี้", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                    doVoidContract(contract.RefNo, contract.CONTNO, contract.ProductSerialNumber);
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

                    /**
                     *
                     * Edit by Teerayut Klinsanga
                     *
                     * Add customer signature
                     * Date: 2019-08-19 14:00:00
                     *
                     */

                    Bitmap bitmap = null;
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    File customerSign = new File(getAlbumStorageDir(contract.CONTNO), String.format("signature_%s.jpg", contract.CONTNO));

                    if (Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.ViewCompletedContract) {
                        btnSignature.setVisibility(View.GONE);
                        editTextSign.setVisibility(View.GONE);
                        if (customerSign.exists()) {
                            bitmap = BitmapFactory.decodeFile(customerSign.getAbsolutePath(), bmOptions);
                            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                            imgSignature.setImageBitmap(getResizedBitmap(bitmap, 250, 80));
                            imgSignature.setEnabled(false);
                        }
                    }
//                    if (!contract.STATUS.equals("VOID") && Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.ViewCompletedContract) {
//                        signature_layout.setVisibility(View.GONE);
//                    }
//
                    if (contract.EFFDATE != null) {
                        Calendar cEFFDATE = Calendar.getInstance();
                        cEFFDATE.setTime(contract.EFFDATE);

                        Calendar cStartDate = Calendar.getInstance();
                        cStartDate.set(Calendar.HOUR_OF_DAY, 0);
                        cStartDate.set(Calendar.MINUTE, 0);
                        cStartDate.set(Calendar.SECOND, 0);
                        cStartDate.set(Calendar.MILLISECOND, 0);

                        Calendar cEndDate = Calendar.getInstance();
                        cEndDate.set(Calendar.HOUR_OF_DAY, 23);
                        cEndDate.set(Calendar.MINUTE, 59);
                        cEndDate.set(Calendar.SECOND, 59);
                        cEndDate.set(Calendar.MILLISECOND, 999);

                        if (cEFFDATE.before(cStartDate) || cEFFDATE.after(cEndDate)) {
//                            signature_layout.setVisibility(View.GONE);
                            btnSignature.setVisibility(View.GONE);
                            editTextSign.setVisibility(View.GONE);

                            if (customerSign.exists()) {
                                bitmap = BitmapFactory.decodeFile(customerSign.getAbsolutePath(), bmOptions);
                                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                                btnSignature.setVisibility(View.GONE);
                                imgSignature.setImageBitmap(getResizedBitmap(bitmap, 250, 80));
                            }
                        }
                    }
////
////
//                    if (!customerSign.exists()) {
//                        btnSignature.setVisibility(View.VISIBLE);
//                    } else {
//                        bitmap = BitmapFactory.decodeFile(customerSign.getAbsolutePath(), bmOptions);
//                        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
//                        btnSignature.setVisibility(View.GONE);
//                        imgSignature.setImageBitmap(getResizedBitmap(bitmap, 250, 80));
//                    }

                    imgSignature.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), SignatureActivity.class);
                            intent.putExtra("CONTRACT_NUMBER", contract.CONTNO);
                            startActivityForResult(intent, 999);
                        }
                    });

                    btnSignature.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), SignatureActivity.class);
                            intent.putExtra("CONTRACT_NUMBER", contract.CONTNO);
                            startActivityForResult(intent, 999);
                        }
                    });


                    /**
                     *
                     * End
                     *
                     */


                } else {
                    scrollView1.setVisibility(View.GONE);
                    showMessage("ไม่พบข้อมูล");
                }

            }
        }).start();
    }

    private void doVoidContract(final String RefNo, final String ContractNo, final String ProductSerialNumber){
        // ทำการยกเลิกสัญญา

        (new BackgroundProcess(activity) {
            ProductStockInfo ps = null;

            @Override
            protected void calling() {
                // TODO Auto-generated method stub

                TSRController.voidContract(RefNo, ContractNo,BHPreference.employeeID(), true);

                ps = getProductStock(ProductSerialNumber, ProductStockController.ProductStockStatus.SOLD);
                if (ps !=null) {
                    ps.ProductSerialNumber = ProductSerialNumber;
                    ps.OrganizationCode = BHPreference.organizationCode();
                    ps.Status = ProductStockController.ProductStockStatus.CHECKED.toString();
                    ps.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.teamCode();
                    ps.ScanDate = new Date();
                    updateProductStock(ps,true);
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated
                // method stub
                AlertDialog.Builder VoidNoti;

                VoidNoti = new AlertDialog.Builder(activity)
                        .setTitle("ยกเลิกสัญญา")
                        .setMessage("ระบบดำเนินการยกเลิกสัญญาหมายเลข " + contract.CONTNO  + " เรียบร้อยแล้ว")
                        .setCancelable(false);
                VoidNoti = VoidNoti.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        showLastView();
                    }
                });

                VoidNoti.show();
            }
        }).start();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_pay:
                float paymentAmount;
                //final String strModel = (contract.MODEL == null) ? "" : contract.MODEL;
                //if (strModel.equals("SA01P") || strModel.equals("PH01P")) {
                if (contract.MODE == 1) {
                    String strPaymentAmount = txtPaymentAmount.getText().toString().replaceAll("[,]", "");
                    paymentAmount = (strPaymentAmount == null || strPaymentAmount.isEmpty()) ? 0 : Float.parseFloat(strPaymentAmount);
                } else {
                    String strPaymentAmount = txtFirstPaymentAmount.getText().toString().replaceAll("[,]", "");
                    paymentAmount = (strPaymentAmount == null || strPaymentAmount.isEmpty()) ? 0 : Float.parseFloat(strPaymentAmount);
                }

                /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

                /*if (manual != null) {

                    SaleFirstPaymentChoiceFragment.Data paymentData = new SaleFirstPaymentChoiceFragment.Data(BHPreference.RefNo(), ProcessType.Sale, paymentAmount);
                    BHPreference.setProcessType(ProcessType.Sale.toString());
                    SaleFirstPaymentChoiceFragment fragment = BHFragment.newInstance(SaleFirstPaymentChoiceFragment.class, paymentData);
                    showNextView(fragment);
                } else {
                    String title = "กรุณาตรวจสอบข้อมูล";
                    String message = "กรุณาบันทึกใบสัญญามือ";
                    showNoticeDialogBox(title, message);
                }*/

                SaleFirstPaymentChoiceFragment.Data paymentData = new SaleFirstPaymentChoiceFragment.Data(BHPreference.RefNo(), ProcessType.Sale, paymentAmount);
                BHPreference.setProcessType(ProcessType.Sale.toString());
                SaleFirstPaymentChoiceFragment fragment = BHFragment.newInstance(SaleFirstPaymentChoiceFragment.class, paymentData);
                showNextView(fragment);

                /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

//                printDocument();
                break;
            case R.string.button_print:
                if (contract != null){
                    for (SalePaymentPeriodInfo item : paymentPeriodOutput) {
                        contract.NextPaymentAmount = item.NetAmount;
                    }
                    printDocument();
                } else {
                    showMessage("ไม่พบข้อมูล");
                }
                break;

            case R.string.button_receipt:
                if (data != null && data.resTitle != 0 && Enum.valueOf(ProcessType.class, BHPreference.ProcessType()) == ProcessType.ViewCompletedContract) {
                    SaleReceiptPayment.Data input = new SaleReceiptPayment.Data();
                    input.resTitle = data.resTitle;
                    showNextView(BHFragment.newInstance(SaleReceiptPayment.class, input));
                } else {
                    showNextView(new SaleReceiptPayment());
                }
                break;
            case R.string.button_back:
                showLastView();
            default:
                break;
        }
    }

    private void printDocument() {
        /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
        /*if (manual != null) {
            new PrinterController(activity).printContract(contract, addressIDCard, addressInstall, manual);
        } else {
            String title = "กรุณาตรวจสอบข้อมูล";
            String message = "กรุณาบันทึกใบสัญญามือ";
            showNoticeDialogBox(title, message);
        }*/

//        new PrinterController(activity).printContract(contract, addressIDCard, addressInstall);
        /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

        /**
         *
         * Edit by Teerayut Klinsanga
         * 07/08/2019
         */
        new PrinterController(activity).printNewImageContract(contract, addressIDCard, addressInstall);
        /**
         * End
         */
    }

    private void saveStatusCode() {
        TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
    }

    private void showNoticeDialogBox(final String title, final String message) {
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity);
        setupAlert.setTitle(title);
        setupAlert.setMessage(message);
        setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        setupAlert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            Bitmap bitmap = null;
            File customerSign = new File(getAlbumStorageDir(contract.CONTNO), String.format("signature_%s.jpg", contract.CONTNO));
            if (!customerSign.exists()) {
                imgSignature.setImageBitmap(null);
                btnSignature.setVisibility(View.VISIBLE);
            } else {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(customerSign.getAbsolutePath(), bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                btnSignature.setVisibility(View.GONE);
                imgSignature.setImageBitmap(getResizedBitmap(bitmap, 250, 80));
            }
        }
    }
}
