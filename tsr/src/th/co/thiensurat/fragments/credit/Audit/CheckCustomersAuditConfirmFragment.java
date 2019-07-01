package th.co.thiensurat.fragments.credit.Audit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import th.co.bighead.utilities.BHBitmap;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ComplainController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.SaleAuditController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.controller.SalePaymentPeriodPaymentController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ComplainInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.SaleAuditInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodPaymentInfo;

import static th.co.thiensurat.business.controller.TSRController.getAddress;
import static th.co.thiensurat.business.controller.TSRController.getContract;
import static th.co.thiensurat.business.controller.TSRController.getDebCustometByID;

public class CheckCustomersAuditConfirmFragment extends BHFragment {

    @InjectView private LinearLayout linearLayoutDiscount; //ส่วนลดเครื่องเทิร์น ไม่มีไม่ต้องแสดง
    @InjectView private LinearLayout linearLayoutAgreementDetails; //รายละเอียดข้อตกลง
    @InjectView private LinearLayout linearLayoutNoProblem; //ไม่มีการแจ้งปัญหา
    @InjectView private LinearLayout linearLayoutProblem; //มีการแจ้งปัญหา

    /*** [START] :: Fixed - [BHPROJ-0025-713] :: ให้แสดงข้อมูล ที่อยู่ ติดตั้ง และ ที่อยู่เก็บเงิน (กรณีที่ทั้ง 2 ที่อยู่แตกต่างกัน) ***/
    @InjectView private LinearLayout linearLayoutPaymentAddress; //ช่องแสดงที่อยู่เก็บเงิน
    @InjectView private TextView textViewPaymentAddress; //ที่อยู่เก็บเงิน
    /*** [END] :: Fixed - [BHPROJ-0025-713] :: ให้แสดงข้อมูล ที่อยู่ ติดตั้ง และ ที่อยู่เก็บเงิน (กรณีที่ทั้ง 2 ที่อยู่แตกต่างกัน) ***/

    @InjectView private TextView textViewContractDate; //วันที่ทำสัญญา
    @InjectView private TextView textViewContractNo; //เลขที่สัญญา
    @InjectView private TextView textViewContractCustomerFullname; //ผู้เช่าซื้อ
    @InjectView private TextView textViewCardNo; //เลขที่บัตร
    @InjectView private TextView textViewCustomerAddress; //ที่อยู่บัตร
    @InjectView private TextView textViewInstallAddress; //ที่อยู่ติดตั้ง
    @InjectView private TextView textViewProduct; //สินค้า
    @InjectView private TextView textViewProductPrice; //ราคาขาย
    @InjectView private TextView textViewDiscount; //ส่วนลดเครื่องเทิร์น
    @InjectView private TextView textViewProductPriceNet; //ราคาสุทธิ
    @InjectView private TextView textViewFirstPaymentAmoun; //งวดที่ 1 ที่ต้องชำระ
    @InjectView private TextView textViewTitleNextPaymentAmoun; //งวดที่ 2 ถึง 12 ต้องชำระงวดละ
    @InjectView private TextView textViewNextPaymentAmoun; //งวดที่ 2 ถึง 12 ต้องชำระงวดละ

    @InjectView private TextView textViewAuditDate; //วันที่ตรวจ
    @InjectView private TextView textViewPaymentDetails; //รายละเอียดการชำระเงินงวดแรกทั้งหมด

    @InjectView private RadioGroup radioGroup_payment; //การชำระเงิน
    @InjectView private RadioButton radio_correct; //ถูกต้อง
    @InjectView private RadioButton radio_incorrect; //ไม่ถูกต้อง

    @InjectView private RadioGroup radioGroup_usage_or_install; //การใช้งาน/ติดตั้ง
    @InjectView private RadioButton radio_successful; //เรียบร้อย
    @InjectView private RadioButton radio_not_successful; //ไม่เรียบร้อย

    @InjectView private RadioGroup radioGroup_other_deals; //ข้อตกลงอื่นๆ
    @InjectView private RadioButton radio_not; //ไม่มี
    @InjectView private RadioButton radio_have; //มี

    @InjectView private TextView textViewPaymentAppointment;//วันที่นัดชำระ งวดที่ ..
    @InjectView private TextView textViewDatePaymentAppointment; //วันที่นัดชำระ

    @InjectView private TextView textViewAgreementDetails; //รายละเอียดข้อตกลง

    @InjectView private ImageView imageViewAudit; //ภาพถ่ายสินค้า

    @InjectView private TextView textViewProblem; //ปัญหา
    @InjectView private TextView textViewCause; //สาเหุต
    @InjectView private TextView textViewNoProblem;//เลขที่แจ้งปัญหา
    @InjectView private TextView textViewDateProblem;//วันที่แจ้งปัญหา

    public static class Data extends BHParcelable {
        public String refNo;

        public Date auditDate; //;วันที่ตรวจสอบ

        public boolean isPassFirstPayment; //การชำระเงิน(true = ถูกต้อง, false = ไม่ถูกต้อง)
        public boolean isPassInstall; //การใช้งาน/การติดตั้ง(true = เรียบร้อย, false = ไม่เรียบร้อย)
        public boolean hasOtherOffer; //ข้อตกลงอื่นๆ(true = มี, false = ไม่มี)
        public String otherOfferDetail; //รายละเอียดข้อตกลง


        //Confirm (isPassFirstPayment == true, isPassInstall == true, hasOtherOffer == false)
        public Date appointmentDate; //วันที่นัดชำระเงิน งวดที่ ...
        public String imageID; //ID ภาพถ่ายสินค้า

        //Complain (isPassFirstPayment == true||false, isPassInstall == true||false, hasOtherOffer == true||false)
        public ProblemInfo problemInfo; //ปัญหา
        public String cause; //สาเหตุ

    }

    private Data data;

    private ContractInfo contractInfo;
    private String firstSalePaymentPeriodId;
    private String nextPaymentPeriodNumber;
    private Date dateProblem = new Date();
    private Date lastFirstPaymentDate;  // Fixed - [BHPROJ-0025-745]

    private String complainPaperID;

    //img
    private static String Parth;
    private static String IMAGE_DIRECTORY_NAME;
    private static String imageTypeCode;
    private Uri fileUri;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_audit_check_customers_audit_confirm;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_confirm};

    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        data = getData();
        contractInfo = getContract(data.refNo);

        if (contractInfo != null) {

            showCustomerDetail();

            textViewAuditDate.setText(BHUtilities.dateFormat(data.auditDate, "dd/MM/yyyy : HH:mm น.", new Locale("th")));

            showPaymentDetails();

            bindRadio();

            bindNoProblem();//ไม่มีปัญหา
            if(data.problemInfo != null){
                bindProblem();//แจ้งปัญหา
            }

            if (data.hasOtherOffer) {
                bindOtherOfferDetail();
            }

            imageViewAudit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileUri, "image/*");
                    startActivity(Intent.createChooser(intent, "Share images to.."));
                }
            });

        } else {
            showDialog("แจ้งเตือน", "ไม่พบข้อมูลของสัญญานี้");
            showLastView();
        }
    }

    private void showCustomerDetail() {
        textViewContractDate.setText(BHUtilities.dateFormat(contractInfo.EFFDATE));
        textViewContractNo.setText(contractInfo.CONTNO);

        DebtorCustomerInfo debtorCustomerInfo = getDebCustometByID(contractInfo.CustomerID);
        textViewContractCustomerFullname.setText(debtorCustomerInfo.CustomerFullName());
        if (debtorCustomerInfo.CustomerType.equals(1)) { //(1)นิติบุคคล
            textViewCardNo.setText(debtorCustomerInfo.AuthorizedIDCard);
        } else { // (0)บุคคลธรรมดา, (2)ต่างชาติ
            textViewCardNo.setText(debtorCustomerInfo.IDCard);
        }

        AddressInfo addressInfoIDCard = getAddress(BHPreference.RefNo(), AddressInfo.AddressType.AddressIDCard);
        textViewCustomerAddress.setText(addressInfoIDCard.Address());

        AddressInfo addressInfoInstall = getAddress(BHPreference.RefNo(), AddressInfo.AddressType.AddressInstall);
        textViewInstallAddress.setText(addressInfoInstall.Address());

        /*** [START] :: Fixed - [BHPROJ-0025-713] :: ให้แสดงข้อมูล ที่อยู่ ติดตั้ง และ ที่อยู่เก็บเงิน (กรณีที่ทั้ง 2 ที่อยู่แตกต่างกัน) ***/
        AddressInfo addressInfoPayment = getAddress(BHPreference.RefNo(), AddressInfo.AddressType.AddressPayment);
        textViewPaymentAddress.setText(addressInfoPayment.Address());

        if (addressInfoInstall.Address().equals(addressInfoPayment.Address())) {
            linearLayoutPaymentAddress.setVisibility(View.GONE);
        }
        /*** [END] :: Fixed - [BHPROJ-0025-713] :: ให้แสดงข้อมูล ที่อยู่ ติดตั้ง และ ที่อยู่เก็บเงิน (กรณีที่ทั้ง 2 ที่อยู่แตกต่างกัน) ***/

        //-- Fixed - [BHPROJ-0025-760]
        textViewProduct.setText(contractInfo.MODEL + (contractInfo.ProductSerialNumber.equals("") ? "" : " เลขที่เครื่อง " + contractInfo.ProductSerialNumber));

        float sumNetAmount = 0;
        float sumDiscount = 0;
        float firstPaymentAmoun = 0;
        List<SalePaymentPeriodInfo> sppList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(data.refNo);

        for (SalePaymentPeriodInfo info : sppList) {
            if (info.PaymentPeriodNumber == 1) {
                firstPaymentAmoun = info.NetAmount;
                firstSalePaymentPeriodId = info.SalePaymentPeriodID;
            }

            if (!info.PaymentComplete && (nextPaymentPeriodNumber == null)) {
                nextPaymentPeriodNumber = String.valueOf(info.PaymentPeriodNumber);
            }

            sumNetAmount += info.PaymentAmount;
            sumDiscount += info.Discount;
        }

        textViewProductPrice.setText(BHUtilities.numericFormat(sumNetAmount));
        if (sumDiscount == 0) {
            linearLayoutDiscount.setVisibility(View.GONE);
        } else {
            textViewDiscount.setText(BHUtilities.numericFormat(sumDiscount));
        }
        textViewProductPriceNet.setText(BHUtilities.numericFormat(sumNetAmount - sumDiscount));
        textViewFirstPaymentAmoun.setText(BHUtilities.numericFormat(firstPaymentAmoun));

        if (sppList.size() == 2) {
            textViewTitleNextPaymentAmoun.setText("งวดที่ " + sppList.get(sppList.size() - 1).PaymentPeriodNumber + " ต้องชำระงวดละ");
        } else {
            textViewTitleNextPaymentAmoun.setText("งวดที่ 2 ถึง " + sppList.get(sppList.size() - 1).PaymentPeriodNumber + "\nต้องชำระงวดละ");
        }

        textViewNextPaymentAmoun.setText(BHUtilities.numericFormat(sppList.get(sppList.size() - 1).NetAmount));

        textViewPaymentAppointment.setText("วันที่นัดชำระ งวดที่ " + nextPaymentPeriodNumber);
    }

    private void showPaymentDetails() {
        List<SalePaymentPeriodPaymentInfo> spppList = new SalePaymentPeriodPaymentController().getSalePaymentPeriodPaymentBySalePaymentPeriodID(firstSalePaymentPeriodId);

        if (spppList != null) {
            String paymentDetails = "";
            lastFirstPaymentDate = spppList.get(0).DatePayment;     // Fixed - [BHPROJ-0025-745]
            for (SalePaymentPeriodPaymentInfo info : spppList) {

                /*** [START] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/
                if (!lastFirstPaymentDate.after(info.DatePayment)) {
                    lastFirstPaymentDate = info.DatePayment;
                }
                /*** [END] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/

                if (!paymentDetails.equals("")) {
                    paymentDetails += "\n";
                }
                paymentDetails += "งวดที่ 1 จำนวนเงิน " + BHUtilities.numericFormat(info.Amount) + " บาท ใบเสร็จ " + info.ReceiptCode;
            }
            textViewPaymentDetails.setText(paymentDetails);
        }
    }

    private void bindRadio() {

        //การชำระเงิน
        if (data.isPassFirstPayment) {
            radio_correct.setChecked(true);
        } else {
            radio_incorrect.setChecked(true);
        }
        radio_correct.setClickable(false);
        radio_incorrect.setClickable(false);

        //การใช้งาน/ติดตั้ง
        if (data.isPassInstall) {
            radio_successful.setChecked(true);
        } else {
            radio_not_successful.setChecked(true);
        }
        radio_successful.setClickable(false);
        radio_not_successful.setClickable(false);

        //ข้อตกลงอื่นๆ
        if (!data.hasOtherOffer) {
            radio_not.setChecked(true);
        } else {
            radio_have.setChecked(true);
        }
        radio_not.setClickable(false);
        radio_have.setClickable(false);
    }


    private void bindNoProblem() {
        linearLayoutNoProblem.setVisibility(View.VISIBLE);

        BHStorage.FolderType F = BHStorage.FolderType.Picture;
        Parth = BHStorage.getFolder(F);

        IMAGE_DIRECTORY_NAME = data.refNo;
        imageTypeCode = ContractImageController.ImageType.SALEAUDIT.toString();

        File mediaFile = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode + "/" + data.imageID + ".jpg");
        fileUri = Uri.fromFile(mediaFile);

        textViewDatePaymentAppointment.setText(BHUtilities.dateFormat(data.appointmentDate));

        /*** [START] :: Fixed - [BHPROJ-0025-715] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันตรวจสอบ) ให้ขึ้นตัวหนังสือแจ้งเตือน ***/

        /*** [START] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/
        /*
        Date currentDate = new Date();
        long diff = data.appointmentDate.getTime() - currentDate.getTime();
        */
        long diff = data.appointmentDate.getTime() - lastFirstPaymentDate.getTime();
        /*** [END] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/

        long days = (diff / (24 * 60 * 60 * 1000)) + 1;
        Log.e("Difference: ", " days: " + days);
        if (days > 45) {
            textViewDatePaymentAppointment.setTextColor(Color.RED);
        } else {
            textViewDatePaymentAppointment.setTextColor(Color.BLACK);
        }
        /*** [END] :: Fixed - [BHPROJ-0025-715] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันตรวจสอบ) ให้ขึ้นตัวหนังสือแจ้งเตือน ***/

        previewCapturedImage();

    }

    private void bindProblem() {
        linearLayoutProblem.setVisibility(View.VISIBLE);

        textViewProblem.setText(data.problemInfo.ProblemName);
        textViewCause.setText(data.cause);


        complainPaperID = new TSRController().getAutoGenerateDocumentID(TSRController.DocumentGenType.Complain,
                BHPreference.SubTeamCode(), BHPreference.saleCode());


        textViewNoProblem.setText(complainPaperID); //เลขที่แจ้งปัญหา
        textViewDateProblem.setText(BHUtilities.dateFormat(dateProblem, "dd/MM/yyyy : HH:mm น.", new Locale("th"))); //วันที่แจ้งปัญหา

    }

    private void bindOtherOfferDetail() {
        linearLayoutAgreementDetails.setVisibility(View.VISIBLE);
        textViewAgreementDetails.setText(data.otherOfferDetail);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_confirm:
                save();
                break;
            default:
                break;
        }
    }

    private void save() {
        (new BackgroundProcess(activity) {

            SaleAuditInfo saleAuditInfo;
            ContractImageInfo contractImageInfo;

            List<SalePaymentPeriodInfo> sppList;
            List<SalePaymentPeriodInfo> newSppList;

            //List<AssignInfo> newAssignList;

            Date newDate = new Date();
            int monthPlusPlus = 0;


            @Override
            protected void before() {
                saleAuditInfo = new SaleAuditController().getSaleAuditByRefNo(data.refNo);
                saleAuditInfo.AuditDate = data.auditDate;
                saleAuditInfo.AuditorSaleCode = BHPreference.saleCode();
                saleAuditInfo.AuditorEmployeeID = BHPreference.employeeID();
                saleAuditInfo.AuditorTeamCode = BHPreference.teamCode();
                saleAuditInfo.IsPassFirstPayment = data.isPassFirstPayment;
                saleAuditInfo.IsPassInstall = data.isPassInstall;
                saleAuditInfo.HasOtherOffer = data.hasOtherOffer;
                saleAuditInfo.OtherOfferDetail = data.otherOfferDetail;
                if(data.problemInfo != null){
                    saleAuditInfo.IsPassSaleAudit = false;
                    saleAuditInfo.ComplainProblemID = data.problemInfo.ProblemID;
                    saleAuditInfo.ComplainDetail = data.cause;
                    saleAuditInfo.ComplainID = DatabaseHelper.getUUID();
                } else {
                    saleAuditInfo.IsPassSaleAudit = true;
                    saleAuditInfo.ComplainID = null;
                }
                saleAuditInfo.AppointmentPaymentDate = data.appointmentDate;
                saleAuditInfo.LastUpdateBy = BHPreference.employeeID();
                saleAuditInfo.LastUpdateDate = newDate;
                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                saleAuditInfo.SaleAuditEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                contractImageInfo = new ContractImageInfo();
                contractImageInfo.ImageID = data.imageID ;
                contractImageInfo.RefNo = data.refNo;
                contractImageInfo.ImageName = data.imageID + ".jpg";
                contractImageInfo.ImageTypeCode = ContractImageController.ImageType.SALEAUDIT.toString();

                sppList = new ArrayList<SalePaymentPeriodInfo>();
                newSppList = new ArrayList<SalePaymentPeriodInfo>();
                //newAssignList = new ArrayList<AssignInfo>();
                sppList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(data.refNo);

                for (SalePaymentPeriodInfo info : sppList) {
                    if (!info.PaymentComplete) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(data.appointmentDate);
                        c.add(Calendar.MONTH, monthPlusPlus);
                        monthPlusPlus++;


                        /*** [START] :: Fixed - [BHPROJ-0024-2045] :: [Android] วันที่ครบกำหนดชำระ PaymentDueDate ของงวดที่ 2-n ทางคุณไพฑูรย์ต้องการให้คิดจากวันที่มีการเก็บเงินงวดแรก บวก 1 เดือน ไปเรื่อยๆ จนถึง n งวด  ***/
                        //info.PaymentDueDate = c.getTime();
                        /*** [END] :: Fixed - [BHPROJ-0024-2045] :: [Android] วันที่ครบกำหนดชำระ PaymentDueDate ของงวดที่ 2-n ทางคุณไพฑูรย์ต้องการให้คิดจากวันที่มีการเก็บเงินงวดแรก บวก 1 เดือน ไปเรื่อยๆ จนถึง n งวด  ***/
                        info.PaymentAppointmentDate = c.getTime();
                        info.LastUpdateBy = BHPreference.employeeID();
                        info.LastUpdateDate = newDate;
                        newSppList.add(info);

                        /*if(saleAuditInfo.IsPassSaleAudit){
                            AssignInfo addAssign = new AssignInfo();
                            addAssign.AssignID = DatabaseHelper.getUUID();
                            addAssign.TaskType = AssignController.AssignTaskType.SalePaymentPeriod.toString();
                            addAssign.OrganizationCode = BHPreference.organizationCode();
                            addAssign.RefNo = data.refNo;
                            addAssign.AssigneeEmpID = BHPreference.employeeID();
                            addAssign.AssigneeTeamCode = BHPreference.teamCode();
                            addAssign.Order = 0;
                            addAssign.OrderExpect = 0;
                            addAssign.CreateDate = newDate;
                            addAssign.CreateBy = BHPreference.employeeID();
                            addAssign.LastUpdateDate = newDate;
                            addAssign.LastUpdateBy = BHPreference.employeeID();
                            addAssign.ReferenceID = info.SalePaymentPeriodID;
                            //addAssign.SyncedDate = newDate;
                            newAssignList.add(addAssign);
                        }*/
                    }
                }
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                TSRController.updateSaleAudit(saleAuditInfo, true);
                TSRController.addContractImage(contractImageInfo, true);

                if (newSppList.size() != 0) {
                    for (SalePaymentPeriodInfo info : newSppList) {
                        TSRController.updateSalePaymentPeriod(info, true);
                    }
                }

                /*if(newAssignList.size() != 0) {
                    for(AssignInfo info : newAssignList){
                        TSRController.addAssign(info, true);
                    }
                }*/

                if(data.problemInfo != null){
                    saveProblem(saleAuditInfo, saleAuditInfo.ComplainID);
                }

            }

            @Override
            protected void after() {
                activity.showView(new CheckCustomersMainFragment());
            }
        }).start();

    }

    private void saveProblem(final SaleAuditInfo saleAuditInfo, final String ComplainID) {
        (new BackgroundProcess(activity) {

            ComplainInfo complainREQUESTInfo;
            /*ComplainInfo complainAPPROVEDInfo;
            ComplainInfo complainCOMPLETEDInfo;*/

            AssignInfo assignInfo;

            Date newDate = new Date();

            @Override
            protected void before() {

                //REQUEST
                complainREQUESTInfo = new ComplainInfo();
                complainREQUESTInfo.ComplainID = ComplainID;
                complainREQUESTInfo.OrganizationCode = BHPreference.organizationCode();
                complainREQUESTInfo.RefNo = data.refNo;
                complainREQUESTInfo.Status = ComplainController.ComplainStatus.REQUEST.toString();
                complainREQUESTInfo.RequestProblemID = data.problemInfo.ProblemID;
                complainREQUESTInfo.RequestDetail = data.cause;
                complainREQUESTInfo.RequestDate = newDate;
                complainREQUESTInfo.RequestBy = BHPreference.employeeID();
                complainREQUESTInfo.RequestTeamCode = BHPreference.teamCode();
                complainREQUESTInfo.CreateDate = newDate;
                complainREQUESTInfo.CreateBy = BHPreference.employeeID();
                complainREQUESTInfo.LastUpdateDate = newDate;
                complainREQUESTInfo.LastUpdateBy = BHPreference.employeeID();
                complainREQUESTInfo.TaskType = ComplainController.TaskType.SaleAudit.toString();
                complainREQUESTInfo.ComplainPaperID = complainPaperID;
                complainREQUESTInfo.ReferenceID = saleAuditInfo.SaleAuditID;
                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                complainREQUESTInfo.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();


                /*//APPROVED
                complainAPPROVEDInfo = new ComplainInfo();
                complainAPPROVEDInfo.ComplainID = complainREQUESTInfo.ComplainID;
                complainAPPROVEDInfo.OrganizationCode = BHPreference.organizationCode();
                complainAPPROVEDInfo.RefNo = data.refNo;
                complainAPPROVEDInfo.Status = ComplainController.ComplainStatus.APPROVED.toString();
                complainAPPROVEDInfo.ApproveDetail = "";
                complainAPPROVEDInfo.ApprovedDate = newDate;
                complainAPPROVEDInfo.ApprovedBy = BHPreference.employeeID();
                complainAPPROVEDInfo.LastUpdateDate = newDate;
                complainAPPROVEDInfo.LastUpdateBy = BHPreference.employeeID();

                //COMPLETED
                complainCOMPLETEDInfo = new ComplainInfo();
                complainCOMPLETEDInfo.ComplainID = complainREQUESTInfo.ComplainID;
                complainCOMPLETEDInfo.OrganizationCode = BHPreference.organizationCode();
                complainCOMPLETEDInfo.RefNo = data.refNo;
                complainCOMPLETEDInfo.Status = ComplainController.ComplainStatus.COMPLETED.toString();
                complainCOMPLETEDInfo.ResultProblemID = data.problemInfo.ProblemID;
                complainCOMPLETEDInfo.ResultDetail = data.cause;
                complainCOMPLETEDInfo.EffectiveDate = newDate;
                complainCOMPLETEDInfo.EffectiveBy = BHPreference.employeeID();
                complainCOMPLETEDInfo.ComplainPaperID = complainPaperID;
                complainCOMPLETEDInfo.LastUpdateDate = newDate;
                complainCOMPLETEDInfo.LastUpdateBy = BHPreference.employeeID();*/

//                assignInfo = new AssignInfo();
//                assignInfo.AssignID = DatabaseHelper.getUUID();
//                assignInfo.TaskType = AssignController.AssignTaskType.Complain.toString();
//                assignInfo.OrganizationCode = BHPreference.organizationCode();
//                assignInfo.RefNo = data.refNo;
//                assignInfo.AssigneeEmpID = BHPreference.employeeID();
//                assignInfo.AssigneeTeamCode = BHPreference.teamCode();
//                assignInfo.Order = 0;
//                assignInfo.OrderExpect = 0;/**/
//                assignInfo.CreateDate = newDate;
//                assignInfo.CreateBy = BHPreference.employeeID();
//                assignInfo.LastUpdateDate = newDate;
//                assignInfo.LastUpdateBy = BHPreference.employeeID();
//                assignInfo.ReferenceID = complainREQUESTInfo.ComplainID;
//                assignInfo.SyncedDate = newDate;
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                TSRController.addComplainStatusREQUEST(complainREQUESTInfo, true);
                /*TSRController.updateComplainStatusAPPROVED(complainAPPROVEDInfo, true);
                TSRController.updateComplainStatusCOMPLETED(complainCOMPLETEDInfo, true);*/

//                TSRController.addAssign(assignInfo, true);
            }
        }).start();


    }


    private void previewCapturedImage() {
        try {
            imageViewAudit.setVisibility(View.VISIBLE);

            String part = fileUri.getPath();
            Bitmap bm = BHBitmap.decodeSampledBitmapFromImagePath(part, 500);
            Bitmap bitmap = BHBitmap.setRotateImageFromImagePath(part, bm);

            imageViewAudit.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }
}
