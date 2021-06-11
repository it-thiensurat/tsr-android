package th.co.thiensurat.fragments.sales.sales_quotation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHBitmap;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHValidator;
import th.co.bighead.utilities.ThaiNationalIDCard.BHThaiIDCard;
import th.co.bighead.utilities.ThaiNationalIDCard.Personal;
import th.co.bighead.utilities.ThaiNationalIDCard.ResultBHThaiIDCard;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.DistrictInfo;
import th.co.thiensurat.data.info.GenderInfo;
import th.co.thiensurat.data.info.MonthInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.PersonTypeCardInfo;
import th.co.thiensurat.data.info.PersonTypeInfo;
import th.co.thiensurat.data.info.PrefixInfo;
import th.co.thiensurat.data.info.ProvinceInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SubDistrictInfo;
import th.co.thiensurat.fragments.sales.GPSTracker;
import th.co.thiensurat.fragments.sales.SaleCheckIDCardFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.sales.preorder.New2SaleCustomerAddressInstallFragment_preorder;
import th.co.thiensurat.fragments.sales.sales_quotation.models.CustomerAPModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.QuotationWaitModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;
import static th.co.thiensurat.retrofit.api.client.DRINKO_BASE_UAT_URL;
import static th.co.thiensurat.retrofit.api.client.DRINKO_BASE_URL;
import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class New2SaleCustomerAddressCardFragment_sale_Q extends BHFragment  {

    private String STATUS_CODE = "02";
    private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static int MEDIA_TYPE_IMAGE = 1;
    private static String IMAGE_DIRECTORY_NAME = BHPreference.RefNo();


    private static String EMPID = BHPreference.employeeID();
    public  static String status="";


    private Uri fileUri;
    private static String Parth;
    private GPSTracker gps;

    private final String imageTypeCode = ContractImageController.ImageType.CUSTOMER.toString();
    private String imageID;
    public static int check_box_status=0;


   static public int select_read_card=0;

    private double latitude;
    private double longitude;


    public static class Data extends BHParcelable {
        //region For ChangeContract
        // For ChangeContract
//        public String selectedCauseName;
//        public ChangeContractInfo chgContractRequest;
//        public ChangeContractInfo chgContractApprove;
//        public ChangeContractInfo chgContractAction;
//        public AssignInfo assign;
//        public static ContractInfo oldContract;
//        public ContractInfo newContract;
//        public List<SalePaymentPeriodInfo> newSPPList;
//        public List<PaymentInfo> newPayment;
//
//        public DebtorCustomerInfo newDebtorCustomer;
//        public static AddressInfo newAddressIDCard;
//        public static AddressInfo newAddressPayment;
//        public static AddressInfo newAddressInstall;
//        public ContractImageInfo newContractImageInfo;
//        //endregion
//
//        public static DebtorCustomerInfo useDebtorCustomer;
//        public static AddressInfo useAddressIDCard;
//        public static AddressInfo useAddressPayment;
//        public static AddressInfo useAddressInstall;

        public String actionType = "";
        public List<get_product_sale_q> objectProduct;
        public List<CustomerAPModel> customerAPModelList = null;
        public List<QuotationWaitModel> quotationWaitModelList;
    }

    // For ChangeContract
    private Data data;

    private List<PrefixInfo> mPrefixList; //คำนำหน้าชื่อ (บุคคลธรรมดา, บุคคลต่างชาติ)
    private List<PrefixInfo> mPrefixCorporationList; //คำนำหน้า นิติบุคคล
    private List<ProvinceInfo> mProvinceList;
    private List<DistrictInfo> mDistrictList;
    private List<SubDistrictInfo> mSubDistrictList;
    private List<PersonTypeInfo> mPersonTypeList; //ประเภทบุคคล
    private List<GenderInfo> mGenderList; //เพศ
    private List<PersonTypeCardInfo> mPersonTypeCardList; //ประเภทบัตร
    private List<MonthInfo> mMonthList;


    //region Fragment ID
    @InjectView
    private LinearLayout linearLayoutHeadNumber;
    @InjectView
    private TextView txtNumber1,txt_s_time;
    @InjectView
    private TextView txtNumber2;
    @InjectView
    private TextView txtNumber3;
    @InjectView
    private TextView txtNumber4;
    @InjectView
    private TextView txtNumber5;

    @InjectView
    LinearLayout layout_result, layout_no_result, layout_contact;

    @InjectView
    AutoCompleteTextView autoCompleteCustomer, autoCompleteTextViewProvince;

    @InjectView
    Button btnSearch, buttonUseData;

    @InjectView
    TextView quotationNumber, quotationTaxNumber, quotationCustomer, quotationAddress, quotationPhone, quotationEmail;

    @InjectView
    Spinner spinnerType, spinnerDistrict, spinnerSubdistrict;

    @InjectView
    EditText editTextName, editTaxNumber, editTextAddressnumber, editTextMoo, editTextSoi, editTextRoad, editTextZipcode, editTextPhone, editTextEmail, editTextContactName, editTextContactPhone, editTextContactEmail;

    private static ContractInfo mainContractInfo;
    private static ContractImageInfo mainContractImageInfo;
    private static AddressInfo mainAddressInfo;
    private static DebtorCustomerInfo mainDebtorCustomerInfo;

    public static AddressInfo tmpAddressInfo;
    public static DebtorCustomerInfo tmpDebtorCustomerInfo;

    private boolean stringStatusCheckID; // สถานะการตรวจสอบบัตร
    private boolean checkSaveData;
    private boolean isAutoCompleteTextViewProvince; //ตรวจสอบการคลิกที่จังหวัด

    private static AddressInfo.AddressType addressType = AddressInfo.AddressType.AddressIDCard;

    @Override
    protected int titleID() {
        return R.string.title_sales_preorder;
    }

    @Override
    protected int fragmentID() {
//        return R.layout.fragment_new2_sale_customer_address_card;
        return R.layout.fragment_customer_address_quotion;
    }

    @Override
    protected int[] processButtons() {
//        return new int[]{R.string.button_back, R.string.button_add, R.string.button_next};
        return new int[]{R.string.button_back, R.string.button_add};
    }

    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    String install_datetime="";

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_next:
                if (checkSaveData) {
                    Product_sale_Q fm = BHFragment.newInstance(Product_sale_Q.class);
                    showNextView(fm);
                } else {
                    String title = "การจัดเก็บข้อมูล";
                    String message = "บันทึกจัดเก็บข้อมูลก่อน";
                    showWarningDialog(title, message);
                }
                break;
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_add:
                check();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();
        BHPreference bhPreference =new BHPreference();
        bhPreference.employeeID();

        /**DebtorCustomer**/
        SetUpDataForDebtorCustomer();
        SetUpSpinnerForDebtorCustomer();

        /**Address**/
        bindProvince();
        SetUpSpinnerForAddress();

        new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

            @Override
            public void onSuccess(BHPermissions bhPermissions) {
                gps = new GPSTracker(getActivity());
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                }
            }

            @Override
            public void onNotSuccess(BHPermissions bhPermissions) {
                bhPermissions.openAppSettings(getActivity());
            }

            @Override
            public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
                bhPermissions.showMessage(getActivity(), permissionType);
            }

        }, BHPermissions.PermissionType.LOCATION);
        /*** [END] :: Permission ***/

        if (data.actionType.equals("edit")) {
            bindEdit();
        }

//        switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
//            case Sale:
//                TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
//                txtNumber2.setBackgroundResource(R.drawable.circle_number_sale_color_red);
//                break;
//            case ChangeContract:
//            case EditContract:
//                linearLayoutHeadNumber.setVisibility(View.GONE);
//                break;
//            default:
//                break;
//        }

//        (new BackgroundProcess(activity) {
//            @Override
//            protected void before() {
//                SetUpSpinnerForAddress();
//
//                SetUpInputFilter();
//                mainContractInfo = null;
//                mainContractImageInfo = null;
//                mainAddressInfo = null;
//                mainDebtorCustomerInfo = null;
//            }
//
//            @Override
//            protected void calling() {
//                switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
//                    case Sale:
//                    case EditContract:
//                        mainContractInfo = TSRController.getContract(BHPreference.RefNo());
//                        mainContractImageInfo = TSRController.getContractImage(BHPreference.RefNo(), imageTypeCode);
//
//                        if (data != null) {
//                            if (data.useDebtorCustomer != null) {
//                                tmpDebtorCustomerInfo = data.useDebtorCustomer;
//                                data.useDebtorCustomer = null;
//                            }
//
//                            switch (Enum.valueOf(AddressInfo.AddressType.class, addressType.toString())) {
//                                case AddressIDCard:
//                                    if (data.useAddressIDCard != null) {
//                                        stringStatusCheckID = true;
//                                        tmpAddressInfo = data.useAddressIDCard;
//                                        data.useAddressIDCard = null;
//                                    }
//                                    break;
//                                case AddressPayment:
//                                    if (data.useAddressPayment != null) {
//                                        stringStatusCheckID = true;
//                                        tmpAddressInfo = data.useAddressPayment;
//                                        data.useAddressPayment = null;
//                                    }
//                                    break;
//                                case AddressInstall:
//                                    if (data.useAddressInstall != null) {
//                                        stringStatusCheckID = true;
//                                        tmpAddressInfo = data.useAddressInstall;
//                                        data.useAddressInstall = null;
//                                    }
//                                    break;
//                            }
//                        }
//
//                        if (tmpDebtorCustomerInfo == null) {
//                            if (mainContractInfo != null) {
//                                if (mainContractInfo.CustomerID != null && !mainContractInfo.CustomerID.equals("")) {
//                                    mainDebtorCustomerInfo = TSRController.getDebCustometByID(mainContractInfo.CustomerID);
//                                }
//                            }
//                        } else {
//                            mainDebtorCustomerInfo = tmpDebtorCustomerInfo;
//                            tmpDebtorCustomerInfo = null;
//                        }
//
//                        if (tmpAddressInfo == null) {
//                            mainAddressInfo = TSRController.getAddress(BHPreference.RefNo(), addressType);
//                            if (mainAddressInfo != null) {
//                                stringStatusCheckID = true;
//                                checkSaveData = true;
//                            }
//                        } else {
//                            mainAddressInfo = tmpAddressInfo;
//                            tmpAddressInfo = null;
//                        }
//                        break;
//                    case ChangeContract:
//                        mainContractInfo = TSRController.getContract(BHPreference.RefNo());
//                        //mainContractImageInfo = TSRController.getContractImage(BHPreference.RefNo(), imageTypeCode);
//                        if (data.newContractImageInfo != null) {
//                            mainContractImageInfo = data.newContractImageInfo;
//                        } else {
//                            mainContractImageInfo = null;
//                        }
//
//                        if (data != null) {
//                            if (data.newDebtorCustomer != null) {
//                                tmpDebtorCustomerInfo = data.newDebtorCustomer;
//                            } else if (data.useDebtorCustomer != null) {
//                                tmpDebtorCustomerInfo = data.useDebtorCustomer;
//                                data.useDebtorCustomer = null;
//                            }
//
//                            switch (Enum.valueOf(AddressInfo.AddressType.class, addressType.toString())) {
//                                case AddressIDCard:
//                                    if (data.newAddressIDCard != null) {
//                                        stringStatusCheckID = true;
//                                        tmpAddressInfo = data.newAddressIDCard;
//                                    } else if (data.useAddressIDCard != null) {
//                                        stringStatusCheckID = true;
//                                        tmpAddressInfo = data.useAddressIDCard;
//                                        data.useAddressIDCard = null;
//                                    }
//                                    break;
//                                case AddressPayment:
//                                    if (data.newAddressPayment != null) {
//                                        stringStatusCheckID = true;
//                                        tmpAddressInfo = data.newAddressPayment;
//                                    } else if (data.useAddressPayment != null) {
//                                        stringStatusCheckID = true;
//                                        tmpAddressInfo = data.useAddressPayment;
//                                        data.useAddressPayment = null;
//                                    }
//                                    break;
//                                case AddressInstall:
//                                    if (data.newAddressInstall != null) {
//                                        stringStatusCheckID = true;
//                                        tmpAddressInfo = data.newAddressInstall;
//                                    } else if (data.useAddressInstall != null) {
//                                        stringStatusCheckID = true;
//                                        tmpAddressInfo = data.useAddressInstall;
//                                        data.useAddressInstall = null;
//                                    }
//                                    break;
//                            }
//                        }
//
//                        if (tmpDebtorCustomerInfo == null) {
//                            if (mainContractInfo != null) {
//                                if (!mainContractInfo.CustomerID.equals("") && mainContractInfo.CustomerID != null) {
//                                    mainDebtorCustomerInfo = TSRController.getDebCustometByID(mainContractInfo.CustomerID);
//                                }
//                            }
//                        } else {
//                            mainDebtorCustomerInfo = tmpDebtorCustomerInfo;
//                            tmpDebtorCustomerInfo = null;
//                        }
//
//                        if (tmpAddressInfo == null) {
//                            mainAddressInfo = TSRController.getAddress(BHPreference.RefNo(), addressType);
//                            if (mainAddressInfo != null) {
//                                stringStatusCheckID = true;
//                            }
//                        } else {
//                            mainAddressInfo = tmpAddressInfo;
//                            tmpAddressInfo = null;
//                        }
//
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            protected void after() {
//                if (mainDebtorCustomerInfo != null) {
////                    bindDebtorCustomer();
//                }
//
//                if (mainAddressInfo != null) {
//                    bindAddress();
//                }
//
//                if (mainContractImageInfo != null) {
////                    bindContractImage();
//                }
//            }
//        }).start();
    }

//    private void bindDebtorCustomer() {
//        if (isImportThaiIDCard) {
//            setEnabledUIDebtorCustomerForImportThaiIDCard(false);
//        }
//        //ประเภทบุคคล
//        spinnerType.setSelection(getSpinnerType(mainDebtorCustomerInfo.CustomerType), true);
//
//        try {
//            switch (getType(mainDebtorCustomerInfo.CustomerType)) {
//                case PERSON://บุคคลธรรมดา
//                case FOREIGNER://บุคคลต่างชาติ
//                    //ประเภทบัตร
//                    spinnerType.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            spinnerTypeCard.setSelection(getSpinnerTypeCard(mainDebtorCustomerInfo.IDCardType));
//
//                            spinnerTypeCard.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    switch (getType(mainDebtorCustomerInfo.CustomerType)) {
//                                        case PERSON://บุคคลธรรมดา
//                                            //editTextIdentificationCard.setText(mainDebtorCustomerInfo.IDCard); //เลขที่บัตร
//
//                                            if (mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD) {
//                                                if (mainDebtorCustomerInfo.IDCard != null) {
//                                                    String strIdCard = mainDebtorCustomerInfo.IDCard.replace("-", "");
//                                                    String newStrIdCard = "";
//                                                    for (int i = 0; i < strIdCard.length(); i++) {
//                                                        if (String.valueOf(strIdCard.charAt(i)).matches("([0-9])")) {
//                                                            if (i == 1 || i == 5 || i == 10 || i == 12) {
//                                                                newStrIdCard += "-" + strIdCard.charAt(i);
//                                                            } else if (i < 13) {
//                                                                newStrIdCard += strIdCard.charAt(i);
//                                                            }
//                                                        } else {
//                                                            break;
//                                                        }
//                                                    }
//                                                    editTextIdentificationCard.setText(newStrIdCard);
//                                                } else {
//                                                    editTextIdentificationCard.setText(mainDebtorCustomerInfo.IDCard); //เลขที่บัตร
//                                                }
//                                            } else {
//                                                editTextIdentificationCard.setText(mainDebtorCustomerInfo.IDCard); //เลขที่บัตร
//                                            }
//                                            break;
//                                        case FOREIGNER://บุคคลต่างชาติ
//                                            editTextCardNo.setText(mainDebtorCustomerInfo.IDCard); //เลขที่บัตร
//                                            break;
//                                    }
//                                }
//                            });
//                        }
//                    });
//
//                    // คำนำหน้าชื่อ
//                    ArrayAdapter perfixAdapter = (ArrayAdapter) spinnerPerfix.getAdapter();
//                    int positionPrefixName = perfixAdapter.getPosition(mainDebtorCustomerInfo.PrefixName);
//                    spinnerPerfix.setSelection(positionPrefixName != -1 ? positionPrefixName : 0);
//                    editTextName.setText(mainDebtorCustomerInfo.CustomerName); //ชื่อ-สกุล
//
//                    if (mainDebtorCustomerInfo.Brithday != null) {
//                        final Calendar c = Calendar.getInstance();
//                        c.setTime(mainDebtorCustomerInfo.Brithday);
//                        // ปีเกิด
//                        ArrayAdapter yearAdapter = (ArrayAdapter) spinnerYear.getAdapter();
//                        spinnerYear.setSelection(yearAdapter.getPosition(String.valueOf(c.get(Calendar.YEAR) + 543)), true);
//                        // เดือนเกิด
//                        ArrayAdapter monthAdapter = (ArrayAdapter) spinnerMonth.getAdapter();
//                        spinnerMonth.setSelection(monthAdapter.getPosition(mMonthList.get((c.get(Calendar.MONTH) + 1)).monthName), true);
//                        spinnerYear.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                spinnerMonth.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // วันเกิด
//                                        ArrayAdapter dayAdapter = (ArrayAdapter) spinnerDate.getAdapter();
//                                        if(dayAdapter != null){
//                                            spinnerDate.setSelection(dayAdapter.getPosition(String.format("%02d", c.get(Calendar.DAY_OF_MONTH))));
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                    } else {
//                        spinnerYear.setSelection(mainDebtorCustomerInfo.logYear, true);
//                        spinnerMonth.setSelection(mainDebtorCustomerInfo.logMonth, true);
//                        spinnerYear.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                spinnerMonth.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // วันเกิด
//                                        spinnerDate.setSelection(mainDebtorCustomerInfo.logDay);
//                                    }
//                                });
//                            }
//                        });
//                    }
//
//                    // เพศ
//                    ArrayAdapter sexAdapter = (ArrayAdapter) spinnerSex.getAdapter();
//                    spinnerSex.setSelection(sexAdapter.getPosition(mainDebtorCustomerInfo.Sex));
//                    break;
//                case CORPORATION://นิติบุคคล
//                    editTextIdentificationNumber.setText(mainDebtorCustomerInfo.IDCard); //เลขประจำตัวผู้เสียภาษี
//                    // คำนำหน้าบริษัท
//                    ArrayAdapter perfixCorporationAdapter = (ArrayAdapter) spinnerPerfixCorporation.getAdapter();
//                    spinnerPerfixCorporation.setSelection(perfixCorporationAdapter.getPosition(mainDebtorCustomerInfo.PrefixName));
//
//                    editTextNameCorporation.setText(mainDebtorCustomerInfo.CompanyName); // ชื่อบริษัท
//                    editTextNameCommission.setText(mainDebtorCustomerInfo.AuthorizedName); // ชื่อกรรมการผู้มีอำนาจ
//                    editTextNameCardNoCommission.setText(mainDebtorCustomerInfo.AuthorizedIDCard); // เลขบัตรกรรมการผู้มีอำนาจ
//                    break;
//            }
//        }catch (Exception ex){}
//    }

//    private void bindAddress() {
//        if (isImportThaiIDCard) {
//            setEnabledUIAddressForImportThaiIDCard(false);
//        }
//        editTextAddressnumber.setText(mainAddressInfo.AddressDetail); // บ้านเลขที่
//        editTextMoo.setText(mainAddressInfo.AddressDetail2); // หมู่ที่
//        editTextSoi.setText(mainAddressInfo.AddressDetail3);// ซอย/ตรอก
//        editTextRoad.setText(mainAddressInfo.AddressDetail4); // ถนน
//
//        autoCompleteTextViewProvince.setText(getProvinceName(mainAddressInfo.ProvinceCode), true); // จังหวัด
//        autoCompleteTextViewProvince.post(new Runnable() {
//            @Override
//            public void run() {
//                // อำเภอ
//                spinnerDistrict.setSelection(getSpinnerDistrict(mainAddressInfo.DistrictCode), true);
//                spinnerDistrict.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        // ตำบล
//
//                        spinnerParish.setSelection(getSpinnerParish(mainAddressInfo.SubDistrictCode));
//                    }
//                });
//            }
//        });

//        try {
//            switch (getType(mainDebtorCustomerInfo.CustomerType)) {
//                case PERSON://บุคคลธรรมดา
//                case FOREIGNER://บุคคลต่างชาติ
//                    editTextPhone.setText(mainAddressInfo.TelHome);// เบอร์บ้าน
//                    editTextWorkPhone.setText(mainAddressInfo.TelOffice);// เบอร์ทีทำงาน
//                    editTextMobilePhone.setText(mainAddressInfo.TelMobile);// เบอร์มือถือ
//                    break;
//                case CORPORATION://นิติบุคคล
//                    editTextPhonecorporation1.setText(mainAddressInfo.TelHome);// เบอร์โทรศัพท์ 1
//                    editTextPhonecorporation2.setText(mainAddressInfo.TelOffice);// เบอร์โทรศัพท์ 2
//                    editTextFaxcorporation.setText(mainAddressInfo.TelMobile);// เบอร์แฟกซ์
//                    break;
//            }
//        } catch (Exception ex){
//
//        }
//        editTextEmail.setText(mainAddressInfo.EMail);// อีเมล์
//    }

    //region getData
//    private PersonTypeInfo.PersonTypeEnum getType(String CustomerType) {
//        for (PersonTypeInfo info : mPersonTypeList) {
//            if (info.PersonTypeCode.equals(CustomerType)) {
//                return info.PersonType;
//            }
//        }
//        return null;
//    }
//
//    private int getSpinnerType(String CustomerType) {
//        int position = -1;
//        if (mPersonTypeList != null) {
//            for (int i = 0; i < mPersonTypeList.size(); i++) {
//                if (mPersonTypeList.get(i).PersonTypeCode.equals(CustomerType)) {
//                    position = i;
//                    break;
//                }
//            }
//        }
//        return position;
//    }
//
//    private int getSpinnerTypeCard(String IDCardType) {
//        int position = -1;
//        if (mPersonTypeCardList != null) {
//            for (int i = 0; i < mPersonTypeCardList.size(); i++) {
//                if (mPersonTypeCardList.get(i).PersonTypeCard.toString().equals(IDCardType)) {
//                    position = i;
//                    break;
//                }
//            }
//        }
//        return position;
//    }
//
    private String getProvinceName(String ProvinceCode) {
        String str = "";
        if (mProvinceList != null) {
            for (ProvinceInfo info : mProvinceList) {
                if (info.ProvinceCode.equals(ProvinceCode)) {
                    str = info.ProvinceName;
                    break;
                }
            }
        }
        return str;
    }

    private int getSpinnerDistrict(String DistrictCode) {
        int position = -1;
        if (mDistrictList != null) {
            for (int i = 0; i < mDistrictList.size(); i++) {
                if (mDistrictList.get(i).DistrictCode.equals(DistrictCode)) {
                    position = i;
                    break;
                }
            }
        }
        return position;
    }

    private int getSpinnerParish(String SubDistrictCode) {
        int position = -1;
        if (mSubDistrictList != null) {
            for (int i = 0; i < mSubDistrictList.size(); i++) {
                if (mSubDistrictList.get(i).SubDistrictCode.equals(SubDistrictCode)) {
                    position = i;
                    break;
                }
            }
        }
        return position;
    }
//
//    private ProvinceInfo getProvinceInfo(String ProvinceName) {
//        for (ProvinceInfo info : mProvinceList) {
//            if (info.ProvinceName.equals(ProvinceName)) {
//                return info;
//            }
//        }
//        return null;
//    }
//    //endregion
//
    private void check() {
        try {
            if (validateDataForPerson()) {
                if (checkIDcard()) {
                    save();
                }
            }
        } catch (Exception ex){
            Log.e("Check form", ex.getLocalizedMessage());
        }
    }

    //region bindView
    private void bindViewPerson() {
        layout_contact.setVisibility(View.GONE);
    }

    private void bindViewCorporation() {
        layout_contact.setVisibility(View.VISIBLE);
    }

    private void bindViewForeigners() {
        layout_contact.setVisibility(View.GONE);
    }
    //endregion

    //region Address
    //region จังหวัด
    List<String> provinceList;
    void bindProvince() {
        List<ProvinceInfo> provinceoutput = TSRController.getProvinces();
        mProvinceList = new ArrayList<ProvinceInfo>();
        mProvinceList.add(new ProvinceInfo("", ""));
        mProvinceList.addAll(1, provinceoutput);

        provinceList = new ArrayList<String>();
        for (ProvinceInfo item : mProvinceList) {
            provinceList.add(item.ProvinceName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, provinceList);
        autoCompleteTextViewProvince.setThreshold(0);
        autoCompleteTextViewProvince.setAdapter(adapter);
    }

    private String getProvinceCode() {
        String provinceCode = "";
        String strProvince = autoCompleteTextViewProvince.getText().toString();
        for (ProvinceInfo info : mProvinceList) {
            if (info.ProvinceName.equals(strProvince)) {
                provinceCode = info.ProvinceCode;
                break;
            }
        }
        return provinceCode;
    }
    //endregion

    //region อำเภอ
    private void bindDistrict(final String provinceCode) {
        if (mDistrictList != null && spinnerDistrict.getSelectedItemPosition() > -1) {
            if (mDistrictList.get(0).ProvinceCode.equals(provinceCode)) {
                return;
            }
        }

        List<DistrictInfo> districtoutput = TSRController.getDistrict(provinceCode);
        mDistrictList = new ArrayList<DistrictInfo>();
        mDistrictList.add(new DistrictInfo("", "", provinceCode));
        mDistrictList.addAll(1, districtoutput);

        List<String> districtList = new ArrayList<String>();
        for (DistrictInfo item : mDistrictList) {
            districtList.add(item.DistrictName);
        }

        BHSpinnerAdapter<String> arrayprovince = new BHSpinnerAdapter<String>(activity, districtList);
        spinnerDistrict.setAdapter(arrayprovince);
    }
    //endregion

    //region ตำบล
    private void bindSubDistrict(final String districtCode) {
        if (mSubDistrictList != null && spinnerSubdistrict.getSelectedItemPosition() > -1) {
            if (mSubDistrictList.get(0).DistrictCode.equals(districtCode)) {
                return;
            }
        }

        List<SubDistrictInfo> subdistrictoutput = TSRController.getSubDistrict(districtCode);
        mSubDistrictList = new ArrayList<SubDistrictInfo>();
        mSubDistrictList.add(new SubDistrictInfo("", "", districtCode, ""));
        mSubDistrictList.addAll(1, subdistrictoutput);

        List<String> subDistrictList = new ArrayList<String>();
        for (SubDistrictInfo item : mSubDistrictList) {
            subDistrictList.add(item.SubDistrictName);
        }

        BHSpinnerAdapter<String> arrayprovince = new BHSpinnerAdapter<String>(activity, subDistrictList);
        spinnerSubdistrict.setAdapter(arrayprovince);
    }
    //endregion

    private String districtCode, subdistrictCode, zipcode;
    private void SetUpSpinnerForAddress() {
        //region Spinner จังหวัด
        autoCompleteTextViewProvince.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                // TODO Auto-generated method stub
                isAutoCompleteTextViewProvince = true;
                autoCompleteTextViewProvince.showDropDown();
                autoCompleteTextViewProvince.requestFocus();
                return false;
            }
        });

        autoCompleteTextViewProvince.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String provinceCode = getProvinceCode();
                if (!provinceCode.equals("")) {
                    bindDistrict(provinceCode);
                }
            }
        });

        autoCompleteTextViewProvince.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && isAutoCompleteTextViewProvince) {
                    isAutoCompleteTextViewProvince = false;
                    String provinceCode = getProvinceCode();
                    if (!provinceCode.equals("")) {
                        bindDistrict(provinceCode);
                    }
//                    else {
//                        autoCompleteTextViewProvince.setText("");
//                        spinnerDistrict.setAdapter(null);
//                        spinnerSubdistrict.setAdapter(null);
//                        editTextZipcode.setText("");
//                    }
                }
            }
        });
        //endregion

        /**อำเภอ**/
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!mDistrictList.get(position).DistrictCode.equals("")) {
                    districtCode = mDistrictList.get(position).DistrictCode;
                    bindSubDistrict(mDistrictList.get(position).DistrictCode);
                }
//                else {
//                    spinnerSubdistrict.setAdapter(null);
//                    editTextZipcode.setText("");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**ตำบล**/
        spinnerSubdistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!mSubDistrictList.get(position).SubDistrictCode.equals("")) {
                    subdistrictCode = mSubDistrictList.get(position).SubDistrictCode;
                    editTextZipcode.setText(mSubDistrictList.get(position).Postcode);
                }
//                else {
//                    editTextZipcode.setText("");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    //endregion

    //region DebtorCustomer
    private void SetUpDataForDebtorCustomer() {
        bindPersonType();
    }

    private String typeCode;
    private void SetUpSpinnerForDebtorCustomer() {

        /** ประเภทบุคคล **/
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                Toast.makeText(activity, "Type: " + mPersonTypeList.get(position).PersonTypeCode, Toast.LENGTH_LONG).show();
                switch (mPersonTypeList.get(position).PersonType) {
                    case PERSON:/** 0-บุคคลธรรมดา **/
                        typeCode = mPersonTypeList.get(position).PersonTypeCode;
                        bindViewPerson();
                        break;
                    case CORPORATION:/** 1-นิติบุคคล **/
                        select_read_card=0;
                        typeCode = mPersonTypeList.get(position).PersonTypeCode;
                        bindViewCorporation();
                        break;
                    case FOREIGNER:/** 2-บุคคลต่างชาติ **/
                        typeCode = mPersonTypeList.get(position).PersonTypeCode;
                        bindViewForeigners();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    boolean identificationCardOnKeyChange;
    private void bindPersonType() {
        mPersonTypeList = new ArrayList<PersonTypeInfo>(PersonTypeInfo.gets());

        List<String> personTypeList = new ArrayList<String>();
        for (PersonTypeInfo item : mPersonTypeList) {
            personTypeList.add(item.PersonTypeName);
        }

        BHSpinnerAdapter<String> arraycustomertype = new BHSpinnerAdapter<String>(activity, personTypeList);
        spinnerType.setAdapter(arraycustomertype);
    }

    //endregion

    //region ValidateData
    private boolean validateDataForPerson() {

        boolean ret = false;
        String title = "กรุณาป้อนข้อมูลให้ครบถ้วน";
        String message = "";

        //DebtorCustomer
        String idCard           = editTaxNumber.getText().toString(); //เลขที่บัตร
        String name             = editTextName.getText().toString(); //ชื่อ-สกุล
        //Address
        String addressNumber    = editTextAddressnumber.getText().toString(); // บ้านเลขที่
        String category         = editTextMoo.getText().toString(); // หมู่ที่
        String alley            = editTextSoi.getText().toString();// ซอย/ตรอก
        String road             = editTextRoad.getText().toString(); // ถนน
        String province         = autoCompleteTextViewProvince.getText().toString(); // จังหวัด
        String district         = ""; // อำเภอ
        String subDistrict      = ""; // ตำบล
        if (!province.equals("")) {
            district = spinnerDistrict.getSelectedItem().toString();
            if (!district.equals("")) {
                subDistrict = spinnerSubdistrict.getSelectedItem().toString();
            }
        }
        String zipCode          = editTextZipcode.getText().toString();// รหัสไปรษณีย์
        String phone            = editTextPhone.getText().toString();// เบอร์บ้าน
        String eMail            = editTextEmail.getText().toString();// อีเมล์
        String cName            = editTextContactName.getText().toString();
        String cPhone           = editTextContactPhone.getText().toString();
        String cEmail           = editTextContactEmail.getText().toString();

        if (idCard.equals("") || name.equals("") || addressNumber.equals("") ||// บ้านเลขที่
                category.equals("") || alley.equals("") || road.equals("") ||  province.equals("") ||// จังหวัด
                district.equals("") || subDistrict.equals("") ||  zipCode.equals("") || phone.equals("") || eMail.equals("") ) {

            String[] strEditText = {
                    idCard, //เลขที่บัตร
                    name, //ชื่อ-สกุล
                    addressNumber, // บ้านเลขที่
                    category, // หมู่ที่
                    alley, // ซอย/ตรอก
                    road,  // ถนน
                    province, // จังหวัด
                    district, // อำเภอ
                    subDistrict,  // ตำบล
                    zipCode, // รหัสไปรษณีย์
                    phone, // เบอร์บ้าน
                    eMail};// อีเมล์

            String[] strMessage = {"ชื่อ-สกุล", "บ้านเลขที่", "หมู่ที่", "ซอย/ตรอก", "ถนน", "จังหวัด", "อำเภอ/เขต", "ตำบล/แขวง",
                    "รหัสไปรษณีย์", "เบอร์โทร", "อีเมล์"};

            for (int i = 0; i < strEditText.length; i++) {
                if (strEditText[i].length() == 0) {
                    message += "กรุณาป้อนข้อมูล : " + strMessage[i];
                    if (i < (strEditText.length - 1)) {
                        message += "\n";
                    }
                }
            }
        } else {
            if (!name.contains(" ")) {
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลนามสกุล ในช่องชื่อ-สกุล");
            } else if (phone.replaceAll("-", "").length() < 9 && !phone.equals("-")) {
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์โทร ให้ครบ");
            }
        }

        if (message != "") {
            showWarningDialog(title, message);
        } else {
            ret = true;
        }
        return ret;
    }
    //endregion

//    private void SetUpInputFilter() {
//        //region เลขที่บัตร นิติบุคคล
//        InputFilter CardNoCommission = new InputFilter() {
//            @Override
//            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int cardnocommission, int i4) {
//                if (source.length() > 0) {
//                    if (!Character.isDigit(source.charAt(0)))
//                        return "";
//                    else {
//                        if (cardnocommission == 1 || cardnocommission == 6 || cardnocommission == 12 || cardnocommission == 15) {
//                            return "-" + source;
//                        } else if (cardnocommission == 16) {
//                            CheckIDcardCommission(source.toString());
//                            return source;
//                        } else if (cardnocommission >= 17) {
//                            CheckIDcardCommission("");
//                            return "";
//                        }
//                    }
//                }
//                return null;
//            }
//        };
//        editTextNameCardNoCommission.setFilters(new InputFilter[]{CardNoCommission});
//        //endregion
//
//        //region เบอร์มือถือ/เบอร์ที่ติดต่อได้
//        InputFilter MobilePhone = new InputFilter() {
//            @Override
//            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int mobilephone, int i4) {
//                String strMobilePhone = spanned.toString().replace("-", "");
//
//                if (strMobilePhone.length() < 10 && source.length() > 0) {
//                    if (!Character.isDigit(source.charAt(0)))
//                        return "";
//                    else {
//                        if (mobilephone == 2 || mobilephone == 7) {
//                            return "-" + source;
//                        } else if (mobilephone >= 12)
//                            return "";
//                    }
//
//                } else {
//                    return "";
//                }
//                return null;
//            }
//        };
////        editTextMobilePhone.setFilters(new InputFilter[]{MobilePhone});
//        //endregion
//
//        //region เบอร์ที่ทำงาน, เบอร์โทรศัพท์ 2, เบอร์แฟกซ์
//        InputFilter PhoneNumber = new InputFilter() {
//            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int phoneNumber, int i4) {
//                if (source.length() > 0) {
//                    if (i4 == 0 && source.equals(" ")) {//ตัดค่าว่างตัวแรก
//                        return "";
//                    } else {
//                        if (phoneNumber == 1 || phoneNumber == 6) {
//                            return "-" + source;
//                        }
//                    }
//                }
//                return null;
//            }
//        };
////        editTextWorkPhone.setFilters(new InputFilter[]{MobilePhone});
////        editTextPhonecorporation2.setFilters(new InputFilter[]{PhoneNumber});
////        editTextFaxcorporation.setFilters(new InputFilter[]{PhoneNumber});
//        //endregion
//
//        //region เบอร์โทรศัพท์ 1
//        InputFilter PhoneNumber1 = new InputFilter() {
//            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int phoneNumber, int i4) {
//                if (source.length() > 0) {
//                    if (!Character.isDigit(source.charAt(0)))
//                        return "";
//                    else {
//                        if (phoneNumber == 1 || phoneNumber == 6) {
//                            return "-" + source;
//                        }
//                    }
//                }
//                return null;
//            }
//        };
////        editTextPhonecorporation1.setFilters(new InputFilter[]{PhoneNumber1});
//        //endregion
//
//        //region เบอร์บ้าน
//        InputFilter PhoneNumberHome = new InputFilter() {
//            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int phoneNumber, int i4) {
//                if (source.length() > 0) {
//                    if (i4 == 0 && source.equals(" ")) {//ตัดค่าว่างตัวแรก
//                        return "";
//                    } else {
//                        if (String.valueOf(source.charAt(0)).matches("([0-9])") || (i4 == 0 && source.equals("-"))) {
//                            if (phoneNumber == 1 || phoneNumber == 6) {
//                                return "-" + source;
//                            }
//                        } else {
//                            return "";
//                        }
//                    }
//                }
//                return null;
//            }
//        };
////        editTextPhone.setFilters(new InputFilter[]{PhoneNumberHome});
//        //endregion
//
//        //region ชื่อ-สกุล, เลขที่บัตรบุคคลต่างชาติ, ชื่อบริษัท, ชื่อกรรมการผู้มีอำนาจ, บ้านเลขท, หมู่ที่, ซอย/ตรอก, ถนน
//        InputFilter TextValue = new InputFilter() {
//            @Override
//            public CharSequence filter(CharSequence source, int i, int i1, Spanned spanned, int i2, int i3) {
//                if (source.length() > 0) {
//                    if (i3 == 0 && source.equals(" ")) {//ตัดค่าว่างตัวแรก
//                        return "";
//                    }
//                    return source;
//                }
//                return null;
//            }
//        };
//        //endregion
//    }

    private boolean checkIDcard() {
        boolean ret = false;
        String idcard;

        switch (mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType) {
            case PERSON://บุคคลธรรมดา
                idcard = editTaxNumber.getText().toString().replaceAll("-", "");
                if (BHValidator.isValidCitizenID(idcard)) {
                    ret = true;
                } else {
                    String title = "รายละเอียดข้อมูลลูกค้า";
                    String message = "กรุณาตรวจสอบเลขประจำตัวผู้เสียภาษี!";
                    showWarningDialog(title, message);
                }
                break;
            case CORPORATION://นิติบุคคล
                idcard = editTaxNumber.getText().toString().replaceAll("-", "");
                if (BHValidator.isValidCitizenID(idcard)) {
                    ret = true;
                } else {
                    String title = "รายละเอียดข้อมูลลูกค้า";
                    String message = "กรุณาตรวจสอบเลขที่บัตร!";
                    showWarningDialog(title, message);
                }
                break;
            default:
                break;
        }
        return ret;
    }

    private void save() {
        try {
            Call call = null;
            Service request = null;
            Retrofit retrofit = null;
            BHLoading.show(activity);
            if (BHGeneral.SERVICE_MODE.toString() == "UAT") {
                retrofit = new Retrofit.Builder()
                        .baseUrl(GIS_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                request = retrofit.create(Service.class);
                if (data.actionType == "new") {
                    call = request.addAlpinesCustomerUAT(typeCode, editTextName.getText().toString(), editTaxNumber.getText().toString(), editTextAddressnumber.getText().toString(),
                            editTextMoo.getText().toString(), editTextSoi.getText().toString(), editTextRoad.getText().toString(), getProvinceCode(), districtCode, subdistrictCode,
                            editTextZipcode.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(), editTextContactName.getText().toString(),
                            editTextContactPhone.getText().toString(), editTextContactEmail.getText().toString(), BHPreference.employeeID(), data.actionType, "");
                } else if (data.actionType == "edit") {
                    call = request.addAlpinesCustomerUAT(typeCode, editTextName.getText().toString(), editTaxNumber.getText().toString(), editTextAddressnumber.getText().toString(),
                            editTextMoo.getText().toString(), editTextSoi.getText().toString(), editTextRoad.getText().toString(), getProvinceCode(), districtCode, subdistrictCode,
                            editTextZipcode.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(), editTextContactName.getText().toString(),
                            editTextContactPhone.getText().toString(), editTextContactEmail.getText().toString(), BHPreference.employeeID(), data.actionType, String.valueOf(data.customerAPModelList.get(0).getCustomerId()));
                }
            }


            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson=new Gson();
                    try {
                        Log.e("Response", String.valueOf(response));
                        Log.e("JSON body", String.valueOf(response.body()));
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        Log.e("Add customer", String.valueOf(jsonObject));
                        BHLoading.close();
                        if (jsonObject.getString("status").equals("SUCCESS")) {
                            Product_sale_Q.Data qData = new Product_sale_Q.Data();
                            qData.actionType = data.actionType;
                            qData.objectProduct = data.objectProduct;
                            qData.objectCustomer = jsonObject.getJSONObject("data");
                            qData.quotationWaitModelList = data.quotationWaitModelList;
                            Product_sale_Q fm = BHFragment.newInstance(Product_sale_Q.class, qData);
                            showNextView(fm);
                        } else {
                            String title = "พบข้อผิดพลาด";
                            String message = jsonObject.getString("message");
                            showWarningDialog(title, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data",e.getLocalizedMessage());
                        BHLoading.close();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("onFailure onVoid:",t.getLocalizedMessage());
                    BHLoading.close();
                }
            });
        } catch (Exception e) {
            Log.e("Exception onVoid",e.getLocalizedMessage());
            BHLoading.close();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelable("file_uri", fileUri);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
//        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {

    }

    private boolean isImportThaiIDCard = false;
    static public Personal mPersonal;

    private void bindEdit() {
        for (CustomerAPModel customerAPModel : data.customerAPModelList) {
            editTaxNumber.setText(customerAPModel.getCustomerTax());
            editTextName.setText(customerAPModel.getCustomerName());
            editTextAddressnumber.setText(customerAPModel.getCustomerAddr());
            editTextMoo.setText(String.valueOf(customerAPModel.getCustmerMoo()));
            editTextSoi.setText(customerAPModel.getCustomerSoi());
            editTextRoad.setText(customerAPModel.getCustomerRoad());
            autoCompleteTextViewProvince.setText(getProvinceName(String.valueOf(customerAPModel.getCustomerProvince())), false);
            spinnerDistrict.setSelection(getSpinnerDistrict(String.valueOf(customerAPModel.getCustomerDistrict())));
            spinnerSubdistrict.setSelection(getSpinnerParish(String.valueOf(customerAPModel.getCustomerSubdistrict())));
            editTextZipcode.setText(customerAPModel.getCustomerZipcode());
            editTextPhone.setText(customerAPModel.getCustomerPhone());
            editTextEmail.setText(customerAPModel.getCustomerEmail());
            editTextContactName.setText(customerAPModel.getCustomerContactName());
            editTextContactPhone.setText(customerAPModel.getCustomerContactEmail());
            editTextContactEmail.setText(customerAPModel.getCustomerContactEmail());
        }
    }
}
