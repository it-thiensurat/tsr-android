package th.co.thiensurat.fragments.credit.Audit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import th.co.bighead.utilities.BHBitmap;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.ProblemController;
import th.co.thiensurat.data.controller.SaleAuditController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.controller.SalePaymentPeriodPaymentController;
import th.co.thiensurat.data.info.AddressInfo;
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

public class CheckCustomersAuditFragment extends BHFragment {

    @InjectView private LinearLayout linearLayoutDiscount; //ส่วนลดเครื่องเทิร์น ไม่มีไม่ต้องแสดง
    @InjectView private LinearLayout linearLayoutAgreementDetails; //รายละเอียดข้อตกลง
    @InjectView private LinearLayout linearLayoutNoProblem; //ไม่มีการแจ้งปัญหา
    @InjectView private LinearLayout linearLayoutProblem; //มีการแจ้งปัญหา
    @InjectView private LinearLayout linearLayoutCause; //สาเหตุ

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
    @InjectView private EditText editTextAppointment; //วันที่นัดชำระ
    @InjectView private ImageButton imageButtonAppointment; //วันที่นัดชำระ

    @InjectView private EditText editTextAgreementDetails; //รายละเอียดข้อตกลง

    @InjectView private ImageView imageViewAudit; //ภาพถ่ายสินค้า

    @InjectView private Spinner spinnerProblem; //ปัญหา
    @InjectView private EditText editTextCause; //สาเหุต

    private ContractInfo contractInfo;
    private SaleAuditInfo saleAuditInfo;
    private Date auditDate;
    private Date lastFirstPaymentDate;  // Fixed - [BHPROJ-0025-745]
    private String firstSalePaymentPeriodId;
    private String nextPaymentPeriodNumber;
    Calendar calendar = Calendar.getInstance(new Locale("th"));
    private Date appointmentDate = new Date();

    private List<ProblemInfo> problemInfoList;
    private int positionProblem;

    //img
    private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static int MEDIA_TYPE_IMAGE = 1;
    private static String Parth;
    private static String IMAGE_DIRECTORY_NAME;
    private static String imageTypeCode;
    private static String imageID;
    private Uri fileUri;


    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_audit_check_customers_audit;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_next};
    }

    @Override
    protected int titleID() {
        return R.string.title_check_customers;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        if (contractInfo == null) {
            contractInfo = getContract(BHPreference.RefNo());
            saleAuditInfo = new SaleAuditController().getSaleAuditByRefNo(BHPreference.RefNo());

            if(saleAuditInfo.ComplainID != null && !saleAuditInfo.ComplainID.equals("") ) {
                ContractImageInfo contractImageInfo = new ContractImageController().getContractImage(BHPreference.RefNo(), ContractImageController.ImageType.SALEAUDIT.toString());
                setFolderImage();
                imageID = contractImageInfo.ImageID;
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                previewCapturedImage();


                bindRadio();
                appointmentDate = saleAuditInfo.AppointmentPaymentDate;
                editTextAppointment.setText(BHUtilities.dateFormat(appointmentDate, "dd/MM/yyyy"));
            }

        } else {
            previewCapturedImage();
        }



        auditDate = new Date();
        if (contractInfo != null) {

            showCustomerDetail();

            textViewAuditDate.setText(BHUtilities.dateFormat(auditDate, "dd/MM/yyyy : HH:mm น.", new Locale("th")));

            showPaymentDetails();

            //createRadioGroupPayment();
            //createRadioGroupUsageOrInstall();
            createRadioGroupOtherDeals();

            initialDateControl();

            initialPhotograph();

            bindProblem();

        } else {
            showDialog("แจ้งเตือน", "ไม่พบข้อมูลของสัญญานี้");
            showLastView();
        }


    }

    private void bindRadio() {

        //การชำระเงิน
        if (saleAuditInfo.IsPassFirstPayment) {
            radio_correct.setChecked(true);
        } else {
            radio_incorrect.setChecked(true);
        }

        //การใช้งาน/ติดตั้ง
        if (saleAuditInfo.IsPassInstall) {
            radio_successful.setChecked(true);
        } else {
            radio_not_successful.setChecked(true);
        }

        //ข้อตกลงอื่นๆ
        if (!saleAuditInfo.HasOtherOffer) {
            radio_not.setChecked(true);
        } else {
            radio_have.setChecked(true);
            bindOtherOfferDetail();
        }
    }

    private void bindOtherOfferDetail() {
        linearLayoutAgreementDetails.setVisibility(View.VISIBLE);
        editTextAgreementDetails.setText(saleAuditInfo.OtherOfferDetail);
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
        List<SalePaymentPeriodInfo> sppList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(BHPreference.RefNo());

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

        /*** [START] :: Fixed - [BHPROJ-0025-715] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันตรวจสอบ) ให้ขึ้นตัวหนังสือแจ้งเตือน ***/

        /*** [START] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/
                        /*
                        Date currentDate = new Date();
                        long diff = appointmentDate.getTime() - currentDate.getTime();
                        */
        long diff = appointmentDate.getTime() - lastFirstPaymentDate.getTime();
        /*** [END] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/

//                        long seconds = diff / 1000;
//                        long minutes = seconds / 60;
//                        long hours = minutes / 60;
//                        long days = hours / 24;
        long days = (diff / (24 * 60 * 60 * 1000)) + 1;
        Log.e("Difference: ", " days: " + days);
        if (days > 45) {
            editTextAppointment.setTextColor(Color.RED);
        } else {
            editTextAppointment.setTextColor(Color.BLACK);
        }
        /*** [END] :: Fixed - [BHPROJ-0025-715] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันตรวจสอบ) ให้ขึ้นตัวหนังสือแจ้งเตือน ***/

    }

    private void createRadioGroupPayment() {
        radioGroup_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_correct:
                        if (isConfirm()) {
                            showNoProblem();
                        }
                        break;
                    case R.id.radio_incorrect:
                        if (isComplain()) {
                            showProblem();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void createRadioGroupUsageOrInstall() {
        radioGroup_usage_or_install.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_successful:
                        if (isConfirm()) {
                            showNoProblem();
                        }
                        break;
                    case R.id.radio_not_successful:
                        if (isComplain()) {
                            showProblem();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void createRadioGroupOtherDeals() {
        radioGroup_other_deals.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_not:
                        noShowAgreementDetails();
                        break;
                    case R.id.radio_have:
                        showAgreementDetails();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initialDateControl() {
        // TODO Auto-generated method stub
        final DatePickerDialog.OnDateSetListener dpl = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Calendar todayWithoutTime = Calendar.getInstance();
                todayWithoutTime.set(Calendar.HOUR_OF_DAY, 0);
                todayWithoutTime.set(Calendar.MINUTE, 0);
                todayWithoutTime.set(Calendar.SECOND, 0);
                todayWithoutTime.set(Calendar.MILLISECOND, 0);
                if (view.isShown()) {
                    if (calendar.getTime().before(todayWithoutTime.getTime())) {
                        showDialog("แจ้งเตือน", "ห้ามเลือกวันที่นัดชำระย้อนหลัง");
                    } else {
                        appointmentDate = calendar.getTime();
                        editTextAppointment.setText(BHUtilities.dateFormat(appointmentDate, "dd/MM/yyyy"));

                        /*** [START] :: Fixed - [BHPROJ-0025-715] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันตรวจสอบ) ให้ขึ้นตัวหนังสือแจ้งเตือน ***/

                        /*** [START] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/
                        /*
                        Date currentDate = new Date();
                        long diff = appointmentDate.getTime() - currentDate.getTime();
                        */
                        long diff = appointmentDate.getTime() - lastFirstPaymentDate.getTime();
                        /*** [END] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/

//                        long seconds = diff / 1000;
//                        long minutes = seconds / 60;
//                        long hours = minutes / 60;
//                        long days = hours / 24;
                        long days = (diff / (24 * 60 * 60 * 1000)) + 1;
                        Log.e("Difference: ", " days: " + days);
                        if (days > 45) {
                            editTextAppointment.setTextColor(Color.RED);
                        } else {
                            editTextAppointment.setTextColor(Color.BLACK);
                        }
                        /*** [END] :: Fixed - [BHPROJ-0025-715] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันตรวจสอบ) ให้ขึ้นตัวหนังสือแจ้งเตือน ***/
                    }
                }
            }
        };

        imageButtonAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar calendar = Calendar.getInstance(new Locale("th"));
                calendar.setTime(appointmentDate);

                DatePickerDialog dpd = new DatePickerDialog(activity, dpl, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
    }

    private void initialPhotograph() {
        setFolderImage();

        imageViewAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageID != null) {
                    captureImage();
                } else {
                    imageID = DatabaseHelper.getUUID();
                    captureImage();
                }

            }
        });

    }

    private void setFolderImage() {
        BHStorage.FolderType F = BHStorage.FolderType.Picture;
        Parth = BHStorage.getFolder(F);

        IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
        imageTypeCode = ContractImageController.ImageType.SALEAUDIT.toString();

    }


    private void bindProblem() {

        /*** [START] :: Fixed - [BHPROJ-0025-744] :: [MoM-15/12/2015] [Android-ตรวจสอบลูกค้า] Spinner ที่แสดงปัญหา ประเภท Complain ให้กรองเฉพาะปัญหาของฝ่ายตรวจสอบ/เก็บเงิน ***/
//        problemInfoList = TSRController.getProblemByProblemType(BHPreference.organizationCode(), ProblemController.ProblemType.Complain.toString());
        problemInfoList = TSRController.getProblemByProblemTypeBySourceSystem(BHPreference.organizationCode(), ProblemController.ProblemType.Complain.toString() , BHPreference.sourceSystem());
        /*** [END] :: Fixed - [BHPROJ-0025-744] :: [MoM-15/12/2015] [Android-ตรวจสอบลูกค้า] Spinner ที่แสดงปัญหา ประเภท Complain ให้กรองเฉพาะปัญหาของฝ่ายตรวจสอบ/เก็บเงิน ***/

        List<String> problemList = new ArrayList<String>();
        problemList.add("");
        for (ProblemInfo item : problemInfoList) {
            problemList.add(item.ProblemName);
        }

        ArrayAdapter<String> arrayProblem = new ArrayAdapter<String>(activity, R.layout.spinner_item_problem, problemList);
        spinnerProblem.setAdapter(arrayProblem);

        spinnerProblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionProblem = position - 1;

                if (positionProblem < 0) {
                    linearLayoutCause.setVisibility(View.GONE);
                } else {
                    linearLayoutCause.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showNoProblem() {
        linearLayoutNoProblem.setVisibility(View.VISIBLE);
        linearLayoutProblem.setVisibility(View.GONE);

    }

    private void showProblem() {
        linearLayoutNoProblem.setVisibility(View.GONE);
        linearLayoutProblem.setVisibility(View.VISIBLE);

    }


    private void showAgreementDetails() {
        linearLayoutAgreementDetails.setVisibility(View.VISIBLE);
    }

    private void noShowAgreementDetails() {
        linearLayoutAgreementDetails.setVisibility(View.GONE);
    }


    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_next:

                String button_confirm = isRadioButton(); //checkRadioButtonAll
                if (button_confirm.equals("")) {

                    if (radio_have.isChecked()) {
                        String agreementDetails = editTextAgreementDetails.getText().toString();
                        if (agreementDetails.isEmpty() || agreementDetails.trim().isEmpty()) {
                            showDialog("แจ้งเตือน", "กรุณากรอกรายละเอียดข้อตกลง");
                            return;
                        }
                    }

                    if (editTextAppointment.getText().length() > 0) {
                        if (isTime()) {
                            if (isPicture()) {
                                next();
                            } else {
                                showDialog("แจ้งเตือน", "กรุณาถ่ายภาพสินค้า");
                            }
                        } else {
                            showDialog("แจ้งเตือน", "ห้ามเลือกวันที่นัดชำระย้อนหลัง");
                        }
                    } else {
                        showDialog("แจ้งเตือน", "กรุณาเลือกวันนัดชำระ");
                    }

                } else {
                    showDialog("แจ้งเตือน", button_confirm);
                }

                break;
            default:
                break;
        }
    }

    private void next() {

        //-- Fixed - [BHPROJ-0025-745]
        //CheckCustomersAuditConfirmFragment.Data data = new CheckCustomersAuditConfirmFragment.Data();
        final CheckCustomersAuditConfirmFragment.Data data = new CheckCustomersAuditConfirmFragment.Data();

        data.refNo = BHPreference.RefNo();
        data.auditDate = auditDate;
        data.isPassFirstPayment = radio_correct.isChecked();
        data.isPassInstall = radio_successful.isChecked();
        data.hasOtherOffer = radio_have.isChecked();

        if (radio_have.isChecked()) {
            data.otherOfferDetail = editTextAgreementDetails.getText().toString();
        } else {
            data.otherOfferDetail = "";
        }

        data.appointmentDate = appointmentDate;
        data.imageID = imageID;

        if(positionProblem < 0){
            data.problemInfo = null;
            data.cause = "";
        } else {
            data.problemInfo = problemInfoList.get(positionProblem);
            data.cause = editTextCause.getText().toString();
        }

        /*** [START] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/
        /*
        CheckCustomersAuditConfirmFragment fm = BHFragment.newInstance(CheckCustomersAuditConfirmFragment.class, data);
        showNextView(fm);
        */
        if (editTextAppointment.getCurrentTextColor() == Color.RED) {
            AlertDialog.Builder setupAlert;
            setupAlert = new AlertDialog.Builder(activity)
                    .setTitle("ยืนยันการทำรายการ")
                    .setMessage("ท่านกำหนดวันนัดชำระงวดต่อไปเกิน 45 วัน ต้องการทำรายการต่อหรือไม่?")
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            CheckCustomersAuditConfirmFragment fm = BHFragment.newInstance(CheckCustomersAuditConfirmFragment.class, data);
                            showNextView(fm);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            setupAlert.show();
        } else {
            CheckCustomersAuditConfirmFragment fm = BHFragment.newInstance(CheckCustomersAuditConfirmFragment.class, data);
            showNextView(fm);
        }
        /*** [END] :: Fixed - [BHPROJ-0025-745] :: กรณีที่มีการนัดเก็บเงินงวดที่ 2 เกิน 45 วัน (นับจากวันที่ชำระเงินงวดแรก) ให้ขึ้นตัวหนังสือสีแดง + แจ้งเตือนยืนยันการทำรายการอีกที ***/
    }

    private String isRadioButton() {
        String error = "";
        if (radio_correct.isChecked() || radio_incorrect.isChecked()) {
            if (radio_successful.isChecked() || radio_not_successful.isChecked()) {
                if (radio_not.isChecked() || radio_have.isChecked()) {

                } else {
                    error = "กรุณาเลือกข้อตกลงอื่นๆ";
                }
            } else {
                error = "กรุณาเลือกการใช้งาน/การติดตั้ง";
            }
        } else {
            error = "กรุณาเลือกการชำระเงิน";
        }
        return error;
    }

    private boolean isConfirm() {
        boolean confirm = false;
        /*if (radio_correct.isChecked() &&
                radio_successful.isChecked() &&
                radio_not.isChecked()) {
            confirm = true;
        }*/
        if (radio_correct.isChecked() &&
                radio_successful.isChecked()) {
            confirm = true;
        }
        return confirm;
    }

    private boolean isComplain() {
        boolean complain = false;
        /*if (radio_incorrect.isChecked() ||
                radio_not_successful.isChecked() ||
                radio_have.isChecked()) {
            complain = true;
        }*/
        if (radio_incorrect.isChecked() ||
                radio_not_successful.isChecked()) {
            complain = true;
        }
        return complain;
    }

    private boolean isPicture() {
        boolean isFilePicture = false;
        if (imageID != null) {
            File filePicture = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode + "/" + imageID + ".jpg");
            isFilePicture = filePicture.isFile();
        }

        return isFilePicture;
    }

    private boolean isTime(){
        boolean time = false;

        Calendar calendar = Calendar.getInstance(new Locale("th"));
        calendar.setTime(appointmentDate);

        Calendar todayWithoutTime = Calendar.getInstance();
        todayWithoutTime.set(Calendar.HOUR_OF_DAY, 0);
        todayWithoutTime.set(Calendar.MINUTE, 0);
        todayWithoutTime.set(Calendar.SECOND, 0);
        todayWithoutTime.set(Calendar.MILLISECOND, 0);

        if (todayWithoutTime.getTime().before(calendar.getTime())) {
            time = true;
        }
        return time;
    }


    private void captureImage() {

        /*** [START] :: Permission ***/
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/

        new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

            @Override
            public void onSuccess(BHPermissions bhPermissions) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                Uri photoURI = FileProvider.getUriForFile(getActivity(),getActivity().getApplicationContext().getPackageName() + ".provider", new File(fileUri.getPath()));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                previewCapturedImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showMessage("User cancelled image capture");
            } else {
                showMessage("Sorry! Failed to capture image");
            }
        }
    }

    private void previewCapturedImage() {
        try {
            imageViewAudit.setVisibility(View.VISIBLE);
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 8;
//			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
//					options);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;
            String part = fileUri.getPath();

            File imageFile = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode + "/" + imageID + ".jpg");
            if (!imageFile.exists()) {
                ContractImageInfo newContractImage = new ContractImageInfo();
                newContractImage.ImageID = imageID;
                newContractImage.RefNo = IMAGE_DIRECTORY_NAME;
                newContractImage.ImageName = imageID + ".jpg";
                newContractImage.ImageTypeCode = imageTypeCode;

                /*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
                //new MainActivity.LoadImageToImageView(newContractImage, imageViewAudit).execute(String.format("%s%s", BHPreference.TSR_IMAGE_URL, newContractImage.ImageName));
                new MainActivity.LoadImageToImageView(newContractImage, imageViewAudit).execute(String.format("%s%s", BHPreference.TSR_IMAGE_URL, newContractImage.ImageID));
                /*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

            } else {

                Bitmap bm = BHBitmap.decodeSampledBitmapFromImagePath(part, 500);
                Bitmap bitmap = BHBitmap.setRotateImageFromImagePath(part, bm);

                imageViewAudit.setImageBitmap(bitmap);
            }

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

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "" + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageID + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }
}
