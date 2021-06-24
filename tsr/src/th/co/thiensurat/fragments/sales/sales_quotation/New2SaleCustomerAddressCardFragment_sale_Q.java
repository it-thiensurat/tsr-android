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
        public List<QuotationWaitModel> quotationWaitModelList = new ArrayList<>();
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
    private List<CustomerAPModel> allCustomerList;


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
    private TextView taxLabel, nameLabel, lastnameLabel;

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
    EditText editTextName, editTextLastName, editTaxNumber, editTextAddressnumber, editTextMoo, editTextSoi, editTextRoad, editTextZipcode, editTextPhone, editTextEmail, editTextContactName, editTextContactPhone, editTextContactEmail;

    private static ContractInfo mainContractInfo;
    private static ContractImageInfo mainContractImageInfo;
    private static AddressInfo mainAddressInfo;
    private static DebtorCustomerInfo mainDebtorCustomerInfo;

    public static AddressInfo tmpAddressInfo;
    public static DebtorCustomerInfo tmpDebtorCustomerInfo;

    private boolean stringStatusCheckID; // สถานะการตรวจสอบบัตร
    private boolean checkSaveData;
    private boolean isAutoCompleteTextViewProvince; //ตรวจสอบการคลิกที่จังหวัด
    private boolean isAutoCompleteTextViewCustomer;

    private static AddressInfo.AddressType addressType = AddressInfo.AddressType.AddressIDCard;

    @Override
    protected int titleID() {
        return R.string.title_sales_quotation;
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

        getAllCustomerList();

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
    }

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
        taxLabel.setText("เลขบัตรฯ");
        nameLabel.setText("ชื่อ");
        lastnameLabel.setText("นามสกุล");
        layout_contact.setVisibility(View.GONE);
    }

    private void bindViewCorporation() {
        nameLabel.setText("ชื่อบริษัท");
        taxLabel.setText("เลขที่ผู้เสียภาษี");
        lastnameLabel.setText("สาขา");
        layout_contact.setVisibility(View.VISIBLE);
    }

    private void bindViewForeigners() {
        nameLabel.setText("ชื่อ");
        taxLabel.setText("เลขพาสปอร์ต");
        lastnameLabel.setText("นามสกุล");
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

        Log.e("Sub district", data.actionType + ": " +String.valueOf(mSubDistrictList));

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
        String lastname         = editTextLastName.getText().toString(); //นามสกุล หรือสาขา
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

        if (typeCode.equals("1")) {
            //นิติบุคคล
            if (idCard.equals("") || name.equals("") || lastname.equals("") || addressNumber.equals("") ||// บ้านเลขที่
                    category.equals("") || alley.equals("") || road.equals("") ||  province.equals("") ||// จังหวัด
                    district.equals("") || subDistrict.equals("") ||  zipCode.equals("") || phone.equals("") || eMail.equals("") ||
                    cName.equals("") || cPhone.equals("") || cEmail.equals("")) {

                String[] strEditText = {
                        idCard, //เลขที่บัตร
                        name, //ชื่อ
                        lastname, //สาขา
                        addressNumber, // บ้านเลขที่
                        category, // หมู่ที่
                        alley, // ซอย/ตรอก
                        road,  // ถนน
                        province, // จังหวัด
                        district, // อำเภอ
                        subDistrict,  // ตำบล
                        zipCode, // รหัสไปรษณีย์
                        phone, // เบอร์บ้าน
                        eMail,
                        cName,
                        cPhone,
                        cEmail
                };// อีเมล์

                String[] strMessage = {"เลขที่ผู้เสียภาษี", "ชื่อบริษัท", "สาขา", "บ้านเลขที่", "หมู่ที่", "ซอย/ตรอก", "ถนน", "จังหวัด", "อำเภอ/เขต", "ตำบล/แขวง",
                        "รหัสไปรษณีย์", "เบอร์โทร", "อีเมล์", "ชื่อผู้ติดต่อ", "เบอร์ผู้ติดต่อ", "อีเมลผู้ติดต่อ"};

                for (int i = 0; i < strEditText.length; i++) {
                    if (strEditText[i].length() == 0) {
                        message += "กรุณาป้อนข้อมูล : " + strMessage[i];
                        if (i < (strEditText.length - 1)) {
                            message += "\n";
                        }
                    }
                }
            } else {
                if (!Character.toString(phone.charAt(0)).equals("0") || !Character.toString(cPhone.charAt(0)).equals("0")) {
                    showWarningDialog("คำเตือน", "เบอร์มือถือไม่ถูกต้อง");
                } else {
                    if (BHValidator.isEmailValid(eMail) || BHValidator.isEmailValid(cEmail)) {
                        ret = true;
                    } else {
                        showWarningDialog("คำเตือน", "Email ไม่ถูกต้อง");
                    }
                }
            }
        } else {
            if (idCard.equals("") || name.equals("") || lastname.equals("") || addressNumber.equals("") ||// บ้านเลขที่
                    category.equals("") || alley.equals("") || road.equals("") ||  province.equals("") ||// จังหวัด
                    district.equals("") || subDistrict.equals("") ||  zipCode.equals("") || phone.equals("") || eMail.equals("") ) {

                String[] strEditText = {
                        idCard, //เลขที่บัตร
                        name, //ชื่อ
                        lastname, //นามสกุล
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

                String[] strMessage = {"เลขที่บัตรฯ", "ชื่อ", "นามสกุล", "บ้านเลขที่", "หมู่ที่", "ซอย/ตรอก", "ถนน", "จังหวัด", "อำเภอ/เขต", "ตำบล/แขวง",
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
                if (!Character.toString(phone.charAt(0)).equals("0")) {
                    showWarningDialog("คำเตือน", "เบอร์มือถือไม่ถูกต้อง");
                } else {
                    if (BHValidator.isEmailValid(eMail)) {
                        ret = true;
                    } else {
                        showWarningDialog("คำเตือน", "Email ไม่ถูกต้อง");
                    }
                }
            }
        }

        if (message != "") {
            showWarningDialog(title, message);
        }
        return ret;
    }
    //endregion

    private void SetUpInputFilter() {
//        //region เลขที่บัตร นิติบุคคล
        InputFilter CardNoCommission = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int cardnocommission, int i4) {
                if (source.length() > 0) {
                    if (!Character.isDigit(source.charAt(0)))
                        return "";
                    else {
                        if (cardnocommission == 1 || cardnocommission == 6 || cardnocommission == 12 || cardnocommission == 15) {
                            return "-" + source;
                        } else if (cardnocommission == 16) {
                            CheckIDcardCommission(source.toString());
                            return source;
                        } else if (cardnocommission >= 17) {
                            CheckIDcardCommission("");
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        editTaxNumber.setFilters(new InputFilter[]{CardNoCommission});
//        //endregion
//
//        //region เบอร์มือถือ/เบอร์ที่ติดต่อได้
        InputFilter MobilePhone = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int mobilephone, int i4) {
                String strMobilePhone = spanned.toString().replace("-", "");

                if (strMobilePhone.length() < 10 && source.length() > 0) {
                    if (!Character.isDigit(source.charAt(0)))
                        return "";
                    else {
                        if (mobilephone == 2 || mobilephone == 7) {
                            return "-" + source;
                        } else if (mobilephone >= 12)
                            return "";
                    }

                } else {
                    return "";
                }
                return null;
            }
        };
        editTextPhone.setFilters(new InputFilter[]{MobilePhone});
        editTextContactPhone.setFilters(new InputFilter[]{MobilePhone});
    }

    private void CheckIDcardCommission(String id) {
        String idcard = editTaxNumber.getText().toString().replaceAll("-", "");
        idcard = idcard + id;
        boolean checkidcard = BHValidator.isValidCitizenID(idcard);

        if (checkidcard)
            stringStatusCheckID = true;
        else
            showMessage("รหัสบัตรนิติบุคคลไม่ถูกต้อง");
    }

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
                    call = request.addAlpinesCustomerUAT(typeCode, editTextName.getText().toString(), editTextLastName.getText().toString(), editTaxNumber.getText().toString(), editTextAddressnumber.getText().toString(),
                            editTextMoo.getText().toString(), editTextSoi.getText().toString(), editTextRoad.getText().toString(), getProvinceCode(), districtCode, subdistrictCode,
                            editTextZipcode.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(), editTextContactName.getText().toString(),
                            editTextContactPhone.getText().toString(), editTextContactEmail.getText().toString(), BHPreference.employeeID(), data.actionType, "");
                } else if (data.actionType == "edit") {
                    call = request.addAlpinesCustomerUAT(typeCode, editTextName.getText().toString(), editTextLastName.getText().toString(), editTaxNumber.getText().toString(), editTextAddressnumber.getText().toString(),
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
            bindSubDistrict(String.valueOf(customerAPModel.getCustomerDistrict()));
            spinnerType.setSelection(customerAPModel.getCustomerType());
            editTaxNumber.setText(customerAPModel.getCustomerTax());
            editTextName.setText(customerAPModel.getCustomerName());
            editTextLastName.setText(customerAPModel.getCustomerLastname());
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
            editTextContactPhone.setText(customerAPModel.getCustomerContactPhone());
            editTextContactEmail.setText(customerAPModel.getCustomerContactEmail());
        }
    }

    private void getAllCustomerList() {
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
                call = request.getCustomerListUAT("");
            }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson=new Gson();
                    try {
                        Log.e("Response", String.valueOf(response));
                        Log.e("JSON body", String.valueOf(response.body()));
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        if (jsonObject.getString("status").equals("SUCCESS")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            CustomerAPModel customerAPModel;
                            allCustomerList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                customerAPModel = new CustomerAPModel();
                                customerAPModel.setCustomerId(object.getInt("APCUS_ID"));
                                customerAPModel.setCustomerName(object.getString("APCUS_NAME"));
                                try {
                                    if (object.getInt("APCUS_TYPE") == 1) {
                                        customerAPModel.setCustomerLastname(object.getString("APCUS_BRANCH"));
                                    } else {
                                        customerAPModel.setCustomerLastname(object.getString("APCUS_LASTNAME"));
                                    }
                                } catch (Exception e) {
                                    customerAPModel.setCustomerLastname("");
                                }

                                try {
                                    customerAPModel.setLastQuotation(object.getString("APQ_ID"));
                                } catch (Exception e) {
                                    customerAPModel.setLastQuotation("");
                                }

                                customerAPModel.setCustomerTax(object.getString("APCUS_IDCARD"));
                                customerAPModel.setCustomerType(object.getInt("APCUS_TYPE"));
                                customerAPModel.setCustomerAddr(object.getString("APCUS_ADDR"));
                                customerAPModel.setCustmerMoo(object.getInt("APCUS_MOO"));
                                customerAPModel.setCustomerSoi(object.getString("APCUS_SOI"));
                                customerAPModel.setCustomerRoad(object.getString("APCUS_ROAD"));
                                customerAPModel.setCustomerProvince(object.getInt("APCUS_PROVINCE_ID"));
                                customerAPModel.setCustomerDistrict(object.getInt("APCUS_DISTRICT_ID"));
                                customerAPModel.setCustomerSubdistrict(object.getInt("APCUS_SUBDISTRICT_ID"));
                                customerAPModel.setCustomerZipcode(object.getString("APCUS_ZIPCODE"));
                                customerAPModel.setCustomerPhone(object.getString("APCUS_PHONE"));
                                customerAPModel.setCustomerEmail(object.getString("APCUS_EMAIL"));
                                customerAPModel.setCustomerContactName(object.getString("APCUS_CONTACT_NAME"));
                                customerAPModel.setCustomerContactPhone(object.getString("APCUS_CONTACT_PHONE"));
                                customerAPModel.setCustomerContactEmail(object.getString("APCUS_CONTACT_EMAIL"));

                                allCustomerList.add(customerAPModel);
                            }

                            bindCustomer();
                        }
                        Log.e("Get customer", String.valueOf(jsonObject));
                        BHLoading.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data",e.getLocalizedMessage());
                        BHLoading.close();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("onFailure Get customer:",t.getLocalizedMessage());
                    BHLoading.close();
                }
            });
        } catch (Exception e) {
            Log.e("Exception Get customer",e.getLocalizedMessage());
            BHLoading.close();
        }
    }

    List<String> customerList;
    private void bindCustomer() {
        customerList = new ArrayList<String>();
        for (CustomerAPModel item : allCustomerList) {
            customerList.add(item.getCustomerName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, customerList);
        autoCompleteCustomer.setThreshold(0);
        autoCompleteCustomer.setAdapter(adapter);

        autoCompleteCustomer.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                // TODO Auto-generated method stub
                isAutoCompleteTextViewCustomer = true;
                autoCompleteCustomer.showDropDown();
                autoCompleteCustomer.requestFocus();
                return false;
            }
        });

        autoCompleteCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (autoCompleteCustomer.getText().toString().equals("")) {
                    layout_no_result.setVisibility(View.VISIBLE);
                    layout_result.setVisibility(View.GONE);
                    buttonUseData.setVisibility(View.GONE);
                }
            }
        });

        autoCompleteCustomer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && isAutoCompleteTextViewCustomer) {
                    isAutoCompleteTextViewCustomer = false;
                }
            }
        });

        autoCompleteCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = allCustomerList.get(position).getCustomerName();
//                Toast.makeText(activity, "Customer " + name, Toast.LENGTH_LONG).show();
                bindLastQuotation(allCustomerList.get(position));
            }
        });
    }

    private void bindLastQuotation(CustomerAPModel customerAPModel) {
        if (!customerAPModel.getLastQuotation().equals("")) {
            layout_no_result.setVisibility(View.GONE);
            layout_result.setVisibility(View.VISIBLE);
            quotationNumber.setText(customerAPModel.getLastQuotation());
            quotationTaxNumber.setText(customerAPModel.getCustomerTax());
            if (customerAPModel.getCustomerType() == 1) {
                quotationCustomer.setText(customerAPModel.getCustomerName() + " (" + customerAPModel.getCustomerLastname() + ")");
            } else {
                quotationCustomer.setText(customerAPModel.getCustomerName() + " " + customerAPModel.getCustomerLastname());
            }
            quotationAddress.setText(customerAPModel.getCustomerAddr() + " " + customerAPModel.getCustmerMoo()  + " " +  customerAPModel.getCustomerSoi() + " " + customerAPModel.getCustomerRoad() +
                    "\n" + customerAPModel.getCustomerSubdistrict() + " " + customerAPModel.getCustomerDistrict() + "\n" + getProvinceName(String.valueOf(customerAPModel.getCustomerProvince())) + " " + customerAPModel.getCustomerZipcode());
            quotationPhone.setText(customerAPModel.getCustomerPhone());
            quotationEmail.setText(customerAPModel.getCustomerEmail());

            buttonUseData.setVisibility(View.VISIBLE);
            buttonUseData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.customerAPModelList = new ArrayList<>();
                    data.customerAPModelList.add(customerAPModel);
                    bindEdit();
                }
            });
        } else {
            layout_no_result.setVisibility(View.VISIBLE);
            layout_result.setVisibility(View.GONE);
            buttonUseData.setVisibility(View.VISIBLE);
            buttonUseData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.customerAPModelList = new ArrayList<>();
                    data.customerAPModelList.add(customerAPModel);
                    bindEdit();
                }
            });
        }
    }
}
