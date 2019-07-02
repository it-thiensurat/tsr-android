package th.co.thiensurat.fragments.sales;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHBitmap;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
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

public class New2SaleCustomerAddressCardFragment extends BHFragment {

    private String STATUS_CODE = "03";
    private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static int MEDIA_TYPE_IMAGE = 1;
    private static String IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
    private Uri fileUri;
    private static String Parth;
    private GPSTracker gps;

    private final String imageTypeCode = ContractImageController.ImageType.CUSTOMER.toString();
    private String imageID;


   static public int select_read_card=0;

    private double latitude;
    private double longitude;

    public static class Data extends BHParcelable {
        //region For ChangeContract
        // For ChangeContract
        public String selectedCauseName;
        public ChangeContractInfo chgContractRequest;
        public ChangeContractInfo chgContractApprove;
        public ChangeContractInfo chgContractAction;
        public AssignInfo assign;
        public static ContractInfo oldContract;
        public ContractInfo newContract;
        public List<SalePaymentPeriodInfo> newSPPList;
        public List<PaymentInfo> newPayment;

        public DebtorCustomerInfo newDebtorCustomer;
        public static AddressInfo newAddressIDCard;
        public static AddressInfo newAddressPayment;
        public static AddressInfo newAddressInstall;
        public ContractImageInfo newContractImageInfo;
        //endregion

        public static DebtorCustomerInfo useDebtorCustomer;
        public static AddressInfo useAddressIDCard;
        public static AddressInfo useAddressPayment;
        public static AddressInfo useAddressInstall;
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
    ImageView imageViewPerson; // รูปประจำตัว
    @InjectView
    public Spinner spinnerType; // บุคคลธรรมดา/นิติบุคคล/ต่างชาติ
    @InjectView
    TextView textViewIdentificationCard; // บัตรประชาชน/ใบขับขี่/บัตรข้าราชการ
    @InjectView
    public EditText editTextIdentificationCard; // บัตรประชาชน
    @InjectView
    ImageButton imageButtoncheckIDCard; // ตรวจสอบบัตร
    @InjectView
    TextView textViewIdentificationNumber; // เลขประจำตัวผู้เสียภาษี
    @InjectView
    public EditText editTextIdentificationNumber; // เลขประจำตัวผู้เสียภาษี
    @InjectView
    TextView textViewTypeCard; // ประเภทบัตรบุคคลต่างชาติ
    @InjectView
    public Spinner spinnerTypeCard; // ประเภทบัตรต่างด่าว/พาสปอร์ต
    @InjectView
    LinearLayout linearLayoutCardNo; // ช่องใส่เลขที่บัตร
    @InjectView
    TextView textViewCardNo; // เลขที่บัตรบุคคลต่างชาติ
    @InjectView
    public EditText editTextCardNo; // เลขที่บัตรบุคคลต่างชาติ
    @InjectView
    LinearLayout linearLayoutPerson; // รายละเอียดบุคคลธรรมดา/ต่างชาติ
    @InjectView
    public Spinner spinnerPerfix; // คำนำหน้าชื่อบุคคลธรรมดา/ต่างชาติ
    @InjectView
    public EditText editTextName; // ชื่อ-สกุลบุคคลธรรมดา/ต่างชาติ
    @InjectView
    public Spinner spinnerDate; // วันเกิดบุคคลธรรมดา/ต่างชาติ
    @InjectView
    public Spinner spinnerMonth; // เดือนเกิดบุคคลธรรมดา/ต่างชาติ
    @InjectView
    public Spinner spinnerYear; // ปีเกิดบุคคลธรรมดา/ต่างชาติ
    @InjectView
    public Spinner spinnerSex; // เพศบุคคลธรรมดา/ต่างชาติ
    @InjectView
    public EditText editTextAge; // อายุบุคคลธรรมดา/ต่างชาติ
    @InjectView
    LinearLayout linearLayoutCorporation; // รายละเอียดนิติบุคคล
    @InjectView
    public Spinner spinnerPerfixCorporation; // คำนำหน้าบริษัท
    @InjectView
    public TextView textViewNameCorporation; // ชื่อบริษัท หรือ ชื่อห้างหุ้นส่วน
    @InjectView
    public EditText editTextNameCorporation; // ชื่อบริษัท
    @InjectView
    public EditText editTextNameCommission; // ชื่อกรรมการผู้มีอำนาจ
    @InjectView
    public EditText editTextNameCardNoCommission; // เลขบัตรกรรมการผู้มีอำนาจ
    @InjectView
    TextView textViewHeadAddress; // หัวข้อชื่อ(ที่อยู่ตามบัตร/ที่เก็บเงิน/ที่ติดตั้ง)
    @InjectView
    EditText editTextAddressnumber; // บ้านเลขที่
    @InjectView
    EditText editTextCategory; // หมู่ที่
    @InjectView
    EditText editTextAlley; // ซอย
    @InjectView
    EditText editTextRoad; // ถนน
    @InjectView
    Spinner spinnerParish; // ตำบล
    @InjectView
    Spinner spinnerDistrict; // อำเภอ
    @InjectView
    AutoCompleteTextView autoCompleteTextViewProvince;//จังหวัด
    @InjectView
    EditText editTextZipcode; // รหัสไปรษณีย์
    @InjectView
    LinearLayout linearLayoutcorporationPhone; // รายละเอียดเบอร์นิติบุคคล
    @InjectView
    EditText editTextPhonecorporation1; // เบอร์โทรที่1
    @InjectView
    EditText editTextPhonecorporation2; // เบอร์โทรที่2
    @InjectView
    EditText editTextFaxcorporation; // เบอร์แฟกช์
    @InjectView
    LinearLayout linearLayoutPhone; // รายละเอียดเบอร์โทรบุคคลธรรมาดา
    @InjectView
    EditText editTextPhone; // เบอร์บ้าน
    @InjectView
    EditText editTextWorkPhone; // เบอร์ทีทำงาน
    @InjectView
    EditText editTextMobilePhone; // เบอร์มือถือ
    @InjectView
    EditText editTextEmail; // อีเมล์
    //endregion

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
        return R.string.title_sales;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_new2_sale_customer_address_card;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_add, R.string.button_next};
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_next:
                if (checkSaveData) {
                    New2SaleCustomerAddressInstallFragment.Data data1 = new New2SaleCustomerAddressInstallFragment.Data();

                    if (data != null) {
                        if (data.useAddressInstall != null) {
                            data1.useAddressInstall = data.useAddressInstall;
                        }

                        if (data.useAddressPayment != null) {
                            data1.useAddressPayment = data.useAddressPayment;
                        }
                    }

                    if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract.toString())) {
                        data1.selectedCauseName = data.selectedCauseName;
                        data1.chgContractRequest = data.chgContractRequest;
                        data1.chgContractApprove = data.chgContractApprove;
                        data1.chgContractAction = data.chgContractAction;
                        data1.assign = data.assign;
                        data1.oldContract = data.oldContract;
                        data1.newContract = data.newContract;
                        data1.newSPPList = data.newSPPList;
                        data1.newPayment = data.newPayment;

                        data1.newDebtorCustomer = data.newDebtorCustomer;
                        data1.newAddressIDCard = data.newAddressIDCard;
                        data1.newAddressInstall = data.newAddressInstall;
                        data1.newAddressPayment = data.newAddressPayment;
                        data1.newContractImageInfo = data.newContractImageInfo;
                    }

                    New2SaleCustomerAddressInstallFragment fm = BHFragment.newInstance(New2SaleCustomerAddressInstallFragment.class, data1);
                    showNextView(fm);

                    //showNextView(new New2SaleCustomerAddressInstallFragment());
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
                if (stringStatusCheckID) {
                    check();
                } else {
                    String title = "รายละเอียดข้อมูลลูกค้า";
                    String message = "";
                    switch (Enum.valueOf(PersonTypeInfo.PersonTypeEnum.class, mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType.toString())) {

                        case PERSON:
                            message = "กรุณาตรวจสอบ" + textViewIdentificationCard.getText().toString() + "!";
                            break;
                        case CORPORATION:
                            message = "กรุณาตรวจสอบเลขประจำตัวผู้เสียภาษี!";
                            break;
                        case FOREIGNER:
                            message = "กรุณาตรวจสอบ" + textViewCardNo.getText().toString() + "!";
                            break;
                    }
                    showWarningDialog(title, message);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();

        /*** [START] :: Permission ***/
        /*gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }*/

        Log.e("moo","2");
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

        switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
            case Sale:
                TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
                txtNumber3.setBackgroundResource(R.drawable.circle_number_sale_color_red);
                break;
            case ChangeContract:
            case EditContract:
                linearLayoutHeadNumber.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        (new BackgroundProcess(activity) {
            @Override
            protected void before() {
                /**DebtorCustomer**/
                SetUpDataForDebtorCustomer();
                SetUpSpinnerForDebtorCustomer();

                /**Address**/
                bindProvince();
                SetUpSpinnerForAddress();

                SetUpInputFilter();

                mainContractInfo = null;
                mainContractImageInfo = null;
                mainAddressInfo = null;
                mainDebtorCustomerInfo = null;
            }

            @Override
            protected void calling() {
                switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
                    case Sale:
                    case EditContract:
                        mainContractInfo = TSRController.getContract(BHPreference.RefNo());
                        mainContractImageInfo = TSRController.getContractImage(BHPreference.RefNo(), imageTypeCode);

                        if (data != null) {
                            if (data.useDebtorCustomer != null) {
                                tmpDebtorCustomerInfo = data.useDebtorCustomer;
                                data.useDebtorCustomer = null;
                            }

                            switch (Enum.valueOf(AddressInfo.AddressType.class, addressType.toString())) {

                                case AddressIDCard:
                                    if (data.useAddressIDCard != null) {
                                        stringStatusCheckID = true;
                                        tmpAddressInfo = data.useAddressIDCard;
                                        data.useAddressIDCard = null;
                                    }
                                    break;
                                case AddressPayment:
                                    if (data.useAddressPayment != null) {
                                        stringStatusCheckID = true;
                                        tmpAddressInfo = data.useAddressPayment;
                                        data.useAddressPayment = null;
                                    }
                                    break;
                                case AddressInstall:
                                    if (data.useAddressInstall != null) {
                                        stringStatusCheckID = true;
                                        tmpAddressInfo = data.useAddressInstall;
                                        data.useAddressInstall = null;
                                    }
                                    break;
                            }
                        }


                        if (tmpDebtorCustomerInfo == null) {
                            if (mainContractInfo != null) {
                                if (mainContractInfo.CustomerID != null && !mainContractInfo.CustomerID.equals("")) {
                                    mainDebtorCustomerInfo = TSRController.getDebCustometByID(mainContractInfo.CustomerID);
                                }
                            }
                        } else {
                            mainDebtorCustomerInfo = tmpDebtorCustomerInfo;
                            tmpDebtorCustomerInfo = null;
                        }

                        if (tmpAddressInfo == null) {
                            mainAddressInfo = TSRController.getAddress(BHPreference.RefNo(), addressType);
                            if (mainAddressInfo != null) {
                                stringStatusCheckID = true;
                                checkSaveData = true;
                            }
                        } else {
                            mainAddressInfo = tmpAddressInfo;
                            tmpAddressInfo = null;
                        }
                        break;
                    case ChangeContract:
                        mainContractInfo = TSRController.getContract(BHPreference.RefNo());
                        //mainContractImageInfo = TSRController.getContractImage(BHPreference.RefNo(), imageTypeCode);
                        if (data.newContractImageInfo != null) {
                            mainContractImageInfo = data.newContractImageInfo;
                        } else {
                            mainContractImageInfo = null;
                        }


                        if (data != null) {
                            if (data.newDebtorCustomer != null) {
                                tmpDebtorCustomerInfo = data.newDebtorCustomer;
                            } else if (data.useDebtorCustomer != null) {
                                tmpDebtorCustomerInfo = data.useDebtorCustomer;
                                data.useDebtorCustomer = null;
                            }

                            switch (Enum.valueOf(AddressInfo.AddressType.class, addressType.toString())) {

                                case AddressIDCard:
                                    if (data.newAddressIDCard != null) {
                                        stringStatusCheckID = true;
                                        tmpAddressInfo = data.newAddressIDCard;
                                    } else if (data.useAddressIDCard != null) {
                                        stringStatusCheckID = true;
                                        tmpAddressInfo = data.useAddressIDCard;
                                        data.useAddressIDCard = null;
                                    }
                                    break;
                                case AddressPayment:
                                    if (data.newAddressPayment != null) {
                                        stringStatusCheckID = true;
                                        tmpAddressInfo = data.newAddressPayment;
                                    } else if (data.useAddressPayment != null) {
                                        stringStatusCheckID = true;
                                        tmpAddressInfo = data.useAddressPayment;
                                        data.useAddressPayment = null;
                                    }
                                    break;
                                case AddressInstall:
                                    if (data.newAddressInstall != null) {
                                        stringStatusCheckID = true;
                                        tmpAddressInfo = data.newAddressInstall;
                                    } else if (data.useAddressInstall != null) {
                                        stringStatusCheckID = true;
                                        tmpAddressInfo = data.useAddressInstall;
                                        data.useAddressInstall = null;
                                    }
                                    break;
                            }
                        }


                        if (tmpDebtorCustomerInfo == null) {
                            if (mainContractInfo != null) {
                                if (!mainContractInfo.CustomerID.equals("") && mainContractInfo.CustomerID != null) {
                                    mainDebtorCustomerInfo = TSRController.getDebCustometByID(mainContractInfo.CustomerID);
                                }
                            }
                        } else {
                            mainDebtorCustomerInfo = tmpDebtorCustomerInfo;
                            tmpDebtorCustomerInfo = null;
                        }

                        if (tmpAddressInfo == null) {
                            mainAddressInfo = TSRController.getAddress(BHPreference.RefNo(), addressType);
                            if (mainAddressInfo != null) {
                                stringStatusCheckID = true;
                            }
                        } else {
                            mainAddressInfo = tmpAddressInfo;
                            tmpAddressInfo = null;
                        }

                        break;
                    default:
                        break;
                }
            }

            @Override
            protected void after() {
                if (mainDebtorCustomerInfo != null) {
                    bindDebtorCustomer();
                }

                if (mainAddressInfo != null) {
                    bindAddress();
                }

                if (mainContractImageInfo != null) {
                    bindContractImage();
                }
            }
        }).start();


    }

    private void bindDebtorCustomer() {
        if (isImportThaiIDCard) {
            setEnabledUIDebtorCustomerForImportThaiIDCard(false);
        }

        //ประเภทบุคคล
        spinnerType.setSelection(getSpinnerType(mainDebtorCustomerInfo.CustomerType), true);

        try {
            switch (getType(mainDebtorCustomerInfo.CustomerType)) {
                case PERSON://บุคคลธรรมดา
                case FOREIGNER://บุคคลต่างชาติ
                    //ประเภทบัตร
                    spinnerType.post(new Runnable() {
                        @Override
                        public void run() {
                            spinnerTypeCard.setSelection(getSpinnerTypeCard(mainDebtorCustomerInfo.IDCardType));

                            spinnerTypeCard.post(new Runnable() {
                                @Override
                                public void run() {
                                    switch (getType(mainDebtorCustomerInfo.CustomerType)) {
                                        case PERSON://บุคคลธรรมดา
                                            //editTextIdentificationCard.setText(mainDebtorCustomerInfo.IDCard); //เลขที่บัตร

                                            if (mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD) {
                                                if (mainDebtorCustomerInfo.IDCard != null) {
                                                    String strIdCard = mainDebtorCustomerInfo.IDCard.replace("-", "");
                                                    String newStrIdCard = "";
                                                    for (int i = 0; i < strIdCard.length(); i++) {
                                                        if (String.valueOf(strIdCard.charAt(i)).matches("([0-9])")) {
                                                            if (i == 1 || i == 5 || i == 10 || i == 12) {
                                                                newStrIdCard += "-" + strIdCard.charAt(i);
                                                            } else if (i < 13) {
                                                                newStrIdCard += strIdCard.charAt(i);
                                                            }
                                                        } else {
                                                            break;
                                                        }
                                                    }
                                                    editTextIdentificationCard.setText(newStrIdCard);
                                                } else {
                                                    editTextIdentificationCard.setText(mainDebtorCustomerInfo.IDCard); //เลขที่บัตร
                                                }
                                            } else {
                                                editTextIdentificationCard.setText(mainDebtorCustomerInfo.IDCard); //เลขที่บัตร
                                            }
                                            break;
                                        case FOREIGNER://บุคคลต่างชาติ
                                            editTextCardNo.setText(mainDebtorCustomerInfo.IDCard); //เลขที่บัตร
                                            break;
                                    }
                                }
                            });
                        }
                    });


                    // คำนำหน้าชื่อ
                    ArrayAdapter perfixAdapter = (ArrayAdapter) spinnerPerfix.getAdapter();
                    int positionPrefixName = perfixAdapter.getPosition(mainDebtorCustomerInfo.PrefixName);
                    spinnerPerfix.setSelection(positionPrefixName != -1 ? positionPrefixName : 0);

                    editTextName.setText(mainDebtorCustomerInfo.CustomerName); //ชื่อ-สกุล

                    if (mainDebtorCustomerInfo.Brithday != null) {
                        final Calendar c = Calendar.getInstance();
                        c.setTime(mainDebtorCustomerInfo.Brithday);

                        // ปีเกิด
                        ArrayAdapter yearAdapter = (ArrayAdapter) spinnerYear.getAdapter();
                        spinnerYear.setSelection(yearAdapter.getPosition(String.valueOf(c.get(Calendar.YEAR) + 543)), true);

                        // เดือนเกิด
                        ArrayAdapter monthAdapter = (ArrayAdapter) spinnerMonth.getAdapter();
                        spinnerMonth.setSelection(monthAdapter.getPosition(mMonthList.get((c.get(Calendar.MONTH) + 1)).monthName), true);

                        spinnerYear.post(new Runnable() {
                            @Override
                            public void run() {
                                spinnerMonth.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // วันเกิด
                                        ArrayAdapter dayAdapter = (ArrayAdapter) spinnerDate.getAdapter();
                                        if(dayAdapter != null){
                                            spinnerDate.setSelection(dayAdapter.getPosition(String.format("%02d", c.get(Calendar.DAY_OF_MONTH))));
                                        }
                                    }
                                });
                            }
                        });

                    } else {
                        spinnerYear.setSelection(mainDebtorCustomerInfo.logYear, true);
                        spinnerMonth.setSelection(mainDebtorCustomerInfo.logMonth, true);
                        spinnerYear.post(new Runnable() {
                            @Override
                            public void run() {
                                spinnerMonth.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // วันเกิด
                                        spinnerDate.setSelection(mainDebtorCustomerInfo.logDay);
                                    }
                                });
                            }
                        });
                    }

                    // เพศ
                    ArrayAdapter sexAdapter = (ArrayAdapter) spinnerSex.getAdapter();
                    spinnerSex.setSelection(sexAdapter.getPosition(mainDebtorCustomerInfo.Sex));
                    break;

                case CORPORATION://นิติบุคคล

                    editTextIdentificationNumber.setText(mainDebtorCustomerInfo.IDCard); //เลขประจำตัวผู้เสียภาษี

                    // คำนำหน้าบริษัท
                    ArrayAdapter perfixCorporationAdapter = (ArrayAdapter) spinnerPerfixCorporation.getAdapter();
                    spinnerPerfixCorporation.setSelection(perfixCorporationAdapter.getPosition(mainDebtorCustomerInfo.PrefixName));

                    editTextNameCorporation.setText(mainDebtorCustomerInfo.CompanyName); // ชื่อบริษัท
                    editTextNameCommission.setText(mainDebtorCustomerInfo.AuthorizedName); // ชื่อกรรมการผู้มีอำนาจ
                    editTextNameCardNoCommission.setText(mainDebtorCustomerInfo.AuthorizedIDCard); // เลขบัตรกรรมการผู้มีอำนาจ
                    break;
            }
        }catch (Exception ex){}
    }

    private void bindAddress() {
        if (isImportThaiIDCard) {
            setEnabledUIAddressForImportThaiIDCard(false);
        }

        editTextAddressnumber.setText(mainAddressInfo.AddressDetail); // บ้านเลขที่
        editTextCategory.setText(mainAddressInfo.AddressDetail2); // หมู่ที่
        editTextAlley.setText(mainAddressInfo.AddressDetail3);// ซอย/ตรอก
        editTextRoad.setText(mainAddressInfo.AddressDetail4); // ถนน


        autoCompleteTextViewProvince.setText(getProvinceName(mainAddressInfo.ProvinceCode), true); // จังหวัด

        autoCompleteTextViewProvince.post(new Runnable() {
            @Override
            public void run() {
                // อำเภอ
                spinnerDistrict.setSelection(getSpinnerDistrict(mainAddressInfo.DistrictCode), true);

                spinnerDistrict.post(new Runnable() {
                    @Override
                    public void run() {
                        // ตำบล

                        spinnerParish.setSelection(getSpinnerParish(mainAddressInfo.SubDistrictCode));
                    }
                });
            }
        });

        switch (getType(mainDebtorCustomerInfo.CustomerType)) {
            case PERSON://บุคคลธรรมดา
            case FOREIGNER://บุคคลต่างชาติ
                editTextPhone.setText(mainAddressInfo.TelHome);// เบอร์บ้าน
                editTextWorkPhone.setText(mainAddressInfo.TelOffice);// เบอร์ทีทำงาน
                editTextMobilePhone.setText(mainAddressInfo.TelMobile);// เบอร์มือถือ
                break;
            case CORPORATION://นิติบุคคล
                editTextPhonecorporation1.setText(mainAddressInfo.TelHome);// เบอร์โทรศัพท์ 1
                editTextPhonecorporation2.setText(mainAddressInfo.TelOffice);// เบอร์โทรศัพท์ 2
                editTextFaxcorporation.setText(mainAddressInfo.TelMobile);// เบอร์แฟกซ์
                break;

        }
        editTextEmail.setText(mainAddressInfo.EMail);// อีเมล์

    }

    //region getData
    private PersonTypeInfo.PersonTypeEnum getType(String CustomerType) {
        for (PersonTypeInfo info : mPersonTypeList) {
            if (info.PersonTypeCode.equals(CustomerType)) {
                return info.PersonType;
            }
        }
        return null;
    }

    private int getSpinnerType(String CustomerType) {
        int position = -1;
        if (mPersonTypeList != null) {
            for (int i = 0; i < mPersonTypeList.size(); i++) {
                if (mPersonTypeList.get(i).PersonTypeCode.equals(CustomerType)) {
                    position = i;
                    break;
                }
            }
        }
        return position;
    }

    private int getSpinnerTypeCard(String IDCardType) {
        int position = -1;
        if (mPersonTypeCardList != null) {
            for (int i = 0; i < mPersonTypeCardList.size(); i++) {
                if (mPersonTypeCardList.get(i).PersonTypeCard.toString().equals(IDCardType)) {
                    position = i;
                    break;
                }
            }
        }
        return position;
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

    private ProvinceInfo getProvinceInfo(String ProvinceName) {
        for (ProvinceInfo info : mProvinceList) {
            if (info.ProvinceName.equals(ProvinceName)) {
                return info;
            }
        }
        return null;
    }
    //endregion

    private void check() {
        switch (mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType) {
            case PERSON:/** 0-บุคคลธรรมดา **/
                switch (mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard) {
                    case IDCARD://บัตรประชาชน
                    case DRIVINGLICENSE://ใบขับขี่
                        if (validateDataForPerson()) {
                            if (checkIDcard()) {
                                save();
                            }
                        }
                        break;
                    case OFFICIALCARD://บัตรข้าราชการ
                        if (validateDataForPerson()) {
                            save();
                        }
                        break;
                    default:
                        showWarningDialog("กรุณาป้อนข้อมูลให้ครบถ้วน", "กรุณาเลือกประเภทบัตร");
                        break;
                }
                break;
            case CORPORATION:/** 1-นิติบุคคล **/
                if (validateDataForCorporation()) {
                    if (checkIdentificationNumber()) {
                        if (checkIDcard()) {
                            save();
                        }
                    }
                }
                break;
            case FOREIGNER:/** 2-บุคคลต่างชาติ **/
                switch (mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard) {
                    case PASSPORT: //หนังสือเดินทาง
                    case OUTLANDER: //บัตรต่างด้าว
                        if (validateDataForForeigner()) {
                            save();
                        }
                        break;
                    default:
                        showWarningDialog("กรุณาป้อนข้อมูลให้ครบถ้วน", "กรุณาเลือกประเภทบัตร");
                        break;
                }
                break;
            default:
                break;
        }
    }

    //region bindView
    private void bindViewPerson() {
        textViewIdentificationCard.setVisibility(View.VISIBLE);
        editTextIdentificationCard.setVisibility(View.VISIBLE);
        textViewIdentificationNumber.setVisibility(View.GONE);
        editTextIdentificationNumber.setVisibility(View.GONE);
        linearLayoutCardNo.setVisibility(View.GONE);
        linearLayoutPerson.setVisibility(View.VISIBLE);
        linearLayoutCorporation.setVisibility(View.GONE);
        textViewTypeCard.setVisibility(View.VISIBLE);
        spinnerTypeCard.setVisibility(View.VISIBLE);
        textViewHeadAddress.setText("ที่อยู่ตามบัตร");
        linearLayoutcorporationPhone.setVisibility(View.GONE);
        linearLayoutPhone.setVisibility(View.VISIBLE);
        //imageButtoncheckIDCard.setVisibility(View.VISIBLE);
        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            imageButtoncheckIDCard.setVisibility(View.GONE);
        } else {
            imageButtoncheckIDCard.setVisibility(View.VISIBLE);
        }
    }

    private void bindViewCorporation() {
        textViewIdentificationCard.setVisibility(View.GONE);
        editTextIdentificationCard.setVisibility(View.GONE);
        textViewIdentificationNumber.setVisibility(View.VISIBLE);
        editTextIdentificationNumber.setVisibility(View.VISIBLE);
        linearLayoutPerson.setVisibility(View.GONE);
        linearLayoutCorporation.setVisibility(View.VISIBLE);
        linearLayoutCardNo.setVisibility(View.GONE);
        textViewTypeCard.setVisibility(View.GONE);
        spinnerTypeCard.setVisibility(View.GONE);
        textViewHeadAddress.setText("ที่ตั้งบริษัท");
        linearLayoutcorporationPhone.setVisibility(View.VISIBLE);
        linearLayoutPhone.setVisibility(View.GONE);
        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            imageButtoncheckIDCard.setVisibility(View.GONE);
        } else {
            imageButtoncheckIDCard.setVisibility(View.VISIBLE);
        }
    }

    private void bindViewForeigners() {
        textViewIdentificationCard.setVisibility(View.GONE);
        editTextIdentificationCard.setVisibility(View.GONE);
        textViewIdentificationNumber.setVisibility(View.GONE);
        editTextIdentificationNumber.setVisibility(View.GONE);
        textViewTypeCard.setVisibility(View.VISIBLE);
        spinnerTypeCard.setVisibility(View.VISIBLE);
        linearLayoutCardNo.setVisibility(View.VISIBLE);
        linearLayoutPerson.setVisibility(View.VISIBLE);
        linearLayoutCorporation.setVisibility(View.GONE);
        textViewHeadAddress.setText("ที่อยู่ตามบัตร");
        linearLayoutcorporationPhone.setVisibility(View.GONE);
        linearLayoutPhone.setVisibility(View.VISIBLE);
        //imageButtoncheckIDCard.setVisibility(View.GONE);
        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            imageButtoncheckIDCard.setVisibility(View.GONE);
        } else {
            imageButtoncheckIDCard.setVisibility(View.VISIBLE);
        }
    }
    //endregion

    //region Address
    //region จังหวัด
    void bindProvince() {
        List<ProvinceInfo> provinceoutput = TSRController.getProvinces();
        mProvinceList = new ArrayList<ProvinceInfo>();
        mProvinceList.add(new ProvinceInfo("", ""));
        mProvinceList.addAll(1, provinceoutput);

        List<String> provinceList = new ArrayList<String>();
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
        if (mSubDistrictList != null && spinnerParish.getSelectedItemPosition() > -1) {
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
        spinnerParish.setAdapter(arrayprovince);
    }
    //endregion

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
                    } else {
                        autoCompleteTextViewProvince.setText("");
                        spinnerDistrict.setAdapter(null);
                        spinnerParish.setAdapter(null);
                        editTextZipcode.setText("");
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
                    bindSubDistrict(mDistrictList.get(position).DistrictCode);

                } else {
                    spinnerParish.setAdapter(null);
                    editTextZipcode.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**ตำบล**/
        spinnerParish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!mSubDistrictList.get(position).SubDistrictCode.equals("")) {
                    editTextZipcode.setText(mSubDistrictList.get(position).Postcode);
                } else {
                    editTextZipcode.setText("");
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
        bindPersonType(); //ประเภทบุคคล
        bindPrefix(); //คำนำหน้าชื่อ (บุคคลธรรมดา, บุคคลต่างชาติ)
        bindPrefixCorporation();//คำนำหน้า นิติบุคคล
        bindSex();//เพศ
        bindYear();//ปีเกิด
        bindMonth();//เดือนเกิด
    }

    private void SetUpSpinnerForDebtorCustomer() {
        /** ถ่ายรูป **/
        imageViewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BHStorage.FolderType F = BHStorage.FolderType.Picture;
                Parth = BHStorage.getFolder(F);
                if (mainContractImageInfo == null && imageID == null) {
                    imageID = DatabaseHelper.getUUID();
                } else {
                    if (mainContractImageInfo != null) {
                        imageID = mainContractImageInfo.ImageID.toString();
                    }
                }
                captureImage();
            }
        });


        /** ค้นหาและตรวจสอบ ID **/
        imageButtoncheckIDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
                    String idcard = editTextIdentificationCard.getText().toString().replaceAll("-", "");
                    if (BHValidator.isValidCitizenID(idcard)) {
                        showMessage(spinnerTypeCard.getSelectedItem().toString() + "ถูกต้อง");
                        //showMessage("รหัสบัตรประชาชนถูกต้อง");
                        stringStatusCheckID = true;
                    } else {
                        //showMessage("รายละเอียดข้อมูลลูกค้า รหัสบัตรประชาชนไม่ถูกต้อง");
                        showMessage("รายละเอียดข้อมูลลูกค้า " + spinnerTypeCard.getSelectedItem().toString() + "ไม่ถูกต้อง");
                    }
                } else {
                    CheckIDcard(true);
                }

            }
        });

        /** ประเภทบุคคล **/
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                switch (mPersonTypeList.get(position).PersonType) {
                    case PERSON:/** 0-บุคคลธรรมดา **/
                        bindViewPerson();
                        break;
                    case CORPORATION:/** 1-นิติบุคคล **/
                        select_read_card=0;
                        imageButtoncheckIDCard.setImageResource(android.R.drawable.ic_menu_search);
                        bindViewCorporation();
                        break;
                    case FOREIGNER:/** 2-บุคคลต่างชาติ **/
                        bindViewForeigners();
                        break;
                    default:
                        break;
                }
                bindPersonTypeCard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** ประเภทบัตร **/
        spinnerTypeCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                setUIForImportThaiIDCard();

                switch (mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType) {
                    case PERSON:/** 0-บุคคลธรรมดา **/
                        //imageButtoncheckIDCard.setVisibility(View.VISIBLE);
                        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
                            imageButtoncheckIDCard.setVisibility(View.GONE);
                        } else {
                            imageButtoncheckIDCard.setVisibility(View.VISIBLE);
                        }

                        if (!spinnerTypeCard.getSelectedItem().toString().equals("")) {
                            textViewIdentificationCard.setText(mPersonTypeCardList.get(position).PersonTypeCardName);
                        }

                        if (mPersonTypeCardList.get(position).PersonTypeCard != PersonTypeCardInfo.PersonTypeCardEnum.OFFICIALCARD
                                && mPersonTypeCardList.get(position).PersonTypeCard != PersonTypeCardInfo.PersonTypeCardEnum.DRIVINGLICENSE) {//ไม่เท่ากับบัตรข้าราชการและบัตรใบขับขี่

                            if (!mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCardName.equals("")
                                    && mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD
                                    && BHGeneral.ID_CARD_MODE) {
                                imageButtoncheckIDCard.setImageResource(R.drawable.ic_import_id_card);
                                //select_read_card=1;
                            } else {
                                select_read_card=0;
                                imageButtoncheckIDCard.setImageResource(android.R.drawable.ic_menu_search);

                            }

                            InputFilter IDcard = new InputFilter() {
                                @Override
                                public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int idcard, int i4) {
                                    if (source.length() > 0) {
                                        if (!Character.isDigit(source.charAt(0)))
                                            return "";
                                        else {
                                            if (idcard == 1 || idcard == 6 || idcard == 12 || idcard == 15) {
                                                return "-" + source;
                                            } else if (idcard >= 17)
                                                return "";
                                        }
                                    }
                                    return null;
                                }
                            };
                            editTextIdentificationCard.setFilters(new InputFilter[]{IDcard});
                            editTextIdentificationCard.setInputType(InputType.TYPE_CLASS_PHONE);

                            String strIdCard = editTextIdentificationCard.getText().toString().replace("-", "");
                            String newStrIdCard = "";
                            for (int i = 0; i < strIdCard.length(); i++) {
                                if (String.valueOf(strIdCard.charAt(i)).matches("([0-9])")) {
                                    if (i == 1 || i == 5 || i == 10 || i == 12) {
                                        newStrIdCard += "-" + strIdCard.charAt(i);
                                    } else if (i < 13) {
                                        newStrIdCard += strIdCard.charAt(i);
                                    }
                                } else {
                                    break;
                                }
                            }
                            editTextIdentificationCard.setText(newStrIdCard);
                        } else {
                            select_read_card=0;
                            imageButtoncheckIDCard.setImageResource(android.R.drawable.ic_menu_search);
                            InputFilter TextValue = new InputFilter() {
                                @Override
                                public CharSequence filter(CharSequence source, int i, int i1, Spanned spanned, int i2, int i3) {
                                    if (source.length() > 0) {
                                        if (source.equals(" ")) {//ตัดค่าว่าง
                                            return "";
                                        } else {
                                            if (String.valueOf(source.charAt(0)).matches("([0-9-])")) {
                                                return source;
                                            }
                                        }
                                        return "";
                                    }
                                    return null;
                                }
                            };
                            editTextIdentificationCard.setFilters(new InputFilter[]{TextValue});

                            //editTextIdentificationCard.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                            //editTextIdentificationCard.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                            editTextIdentificationCard.setInputType(InputType.TYPE_CLASS_PHONE);
                        }
                        break;
                    case CORPORATION:/** 1-นิติบุคคล **/
                        select_read_card=0;
                        imageButtoncheckIDCard.setImageResource(android.R.drawable.ic_menu_search);
                        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
                            imageButtoncheckIDCard.setVisibility(View.GONE);
                        } else {
                            imageButtoncheckIDCard.setVisibility(View.VISIBLE);
                        }
                        break;
                    case FOREIGNER:/** 2-บุคคลต่างชาติ **/
                        select_read_card=0;
                        imageButtoncheckIDCard.setImageResource(android.R.drawable.ic_menu_search);
                        //imageButtoncheckIDCard.setVisibility(View.VISIBLE);
                        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
                            imageButtoncheckIDCard.setVisibility(View.GONE);
                        } else {
                            imageButtoncheckIDCard.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** คำนำหน้าชื่อ (บุคคลธรรมดา, บุคคลต่างชาติ) **/
        spinnerPerfix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String perfixName = spinnerPerfix.getSelectedItem().toString();
                ArrayAdapter sexAdapter = (ArrayAdapter) spinnerSex.getAdapter();

                select_read_card=0;
               // Log.e("XXX",perfixName);
/*                switch (perfixName) {
                    case "":
                        spinnerSex.setEnabled(false);
                        spinnerSex.setSelection(sexAdapter.getPosition(""));
                        break;
                    case "นาย":
                    case "MR.":
                        spinnerSex.setEnabled(false);
                        spinnerSex.setSelection(sexAdapter.getPosition("ชาย"));
                        break;
                    case "นางสาว":
                    case "น.ส.":
                    case "นาง":
                    case "Miss":
                    case "Mrs.":
                    case "Ms.":
                        spinnerSex.setEnabled(false);
                        spinnerSex.setSelection(sexAdapter.getPosition("หญิง"));
                        break;
                    default:



                        spinnerSex.setEnabled(true);
                        break;
                }*/

                if (isImportThaiIDCard
                        && mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType == PersonTypeInfo.PersonTypeEnum.PERSON
                        && mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD) {
                    // เพศ
                    if (mainDebtorCustomerInfo != null ) {
                        spinnerSex.setEnabled(false);
                        spinnerSex.setSelection(sexAdapter.getPosition(mainDebtorCustomerInfo.Sex));

                        select_read_card=1;
                       // showDialog("2222","2222");
                    }
                } else {
                    switch (perfixName) {
                        case "":
                            spinnerSex.setEnabled(false);
                            spinnerSex.setSelection(sexAdapter.getPosition(""));
                            break;
                        case "นาย":
                        case "MR.":
                            spinnerSex.setEnabled(false);
                            spinnerSex.setSelection(sexAdapter.getPosition("ชาย"));
                            break;
                        case "นางสาว":
                        case "น.ส.":
                        case "นาง":
                        case "Miss":
                        case "Mrs.":
                        case "Ms.":
                            spinnerSex.setEnabled(false);
                            spinnerSex.setSelection(sexAdapter.getPosition("หญิง"));
                            break;
                        default:
                            spinnerSex.setEnabled(true);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** คำนำหน้า นิติบุคคล **/
        spinnerPerfixCorporation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!spinnerPerfixCorporation.getSelectedItem().toString().equals("")) {
                    textViewNameCorporation.setText("ชื่อ" + mPrefixCorporationList.get(position).PrefixName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** ปีเกิด **/
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (spinnerYear.getSelectedItem().toString().equals("") || spinnerMonth.getSelectedItem().toString().equals("")) {
                    spinnerDate.setAdapter(null);
                } else {
                    bindDay();
                }
                calculateAge();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** เดือนเกิด **/
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (spinnerYear.getSelectedItem().toString().equals("") || spinnerMonth.getSelectedItem().toString().equals("")) {
                    spinnerDate.setAdapter(null);
                } else {
                    bindDay();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       /* *//** วันเกิด **//*
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        if (!BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            editTextIdentificationCard.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (!identificationCardOnKeyChange
                            && mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType == PersonTypeInfo.PersonTypeEnum.PERSON
                            && mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD) {
                        identificationCardOnKeyChange = true;
                    }
                    return false;
                }
            });

            if (BHGeneral.ID_CARD_MODE) {
                editTextIdentificationCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus && identificationCardOnKeyChange
                                && mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType == PersonTypeInfo.PersonTypeEnum.PERSON
                                && mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD) {


                            CheckIDcard(false);
                            identificationCardOnKeyChange = false;
                        }
                    }
                });
            }
        }
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

    private void bindPersonTypeCard() {
        mPersonTypeCardList = new ArrayList<PersonTypeCardInfo>(PersonTypeCardInfo.gets(true, mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType));

        List<String> personTypeCardList = new ArrayList<String>();
        for (PersonTypeCardInfo item : mPersonTypeCardList) {
            personTypeCardList.add(item.PersonTypeCardName);
        }

        BHSpinnerAdapter<String> arraygender = new BHSpinnerAdapter<String>(activity, personTypeCardList);
        spinnerTypeCard.setAdapter(arraygender);
    }

    private void bindPrefix() {
        List<PrefixInfo> prefixoutput = TSRController.getPrefixes();

        mPrefixList = new ArrayList<PrefixInfo>();
        mPrefixList.add(new PrefixInfo("", ""));
        for (PrefixInfo item : prefixoutput) {
            mPrefixList.add(new PrefixInfo(item.PrefixCode, item.PrefixName));
        }

        List<String> prefixList = new ArrayList<String>();
        for (PrefixInfo item : mPrefixList) {
            prefixList.add(item.PrefixName);
        }

        BHSpinnerAdapter<String> arrayprefix = new BHSpinnerAdapter<String>(activity, prefixList);
        spinnerPerfix.setAdapter(arrayprefix);
    }

    private void bindPrefixCorporation() {
        mPrefixCorporationList = new ArrayList<PrefixInfo>(PrefixInfo.getsCorporation(true));

        List<String> prefixCorpList = new ArrayList<String>();
        for (PrefixInfo item : mPrefixCorporationList) {
            prefixCorpList.add(item.PrefixName);
        }

        BHSpinnerAdapter<String> arraygender = new BHSpinnerAdapter<String>(activity, prefixCorpList);
        spinnerPerfixCorporation.setAdapter(arraygender);
    }

    private void bindSex() {
        mGenderList = new ArrayList<GenderInfo>(GenderInfo.gets(true));

        List<String> genderList = new ArrayList<String>();
        for (GenderInfo item : mGenderList) {
            genderList.add(item.GenderName);
        }

        BHSpinnerAdapter<String> arraygender = new BHSpinnerAdapter<String>(activity, genderList);
        spinnerSex.setAdapter(arraygender);
    }

    private void bindYear() {
        Calendar c = Calendar.getInstance();
        String[] year = new String[90];
        year[0] = "";
        for (int x = 1; x < year.length; x++) {
            year[x] = c.get(Calendar.YEAR) + 526 - x + "";
        }
        BHSpinnerAdapter<String> arrayyear = new BHSpinnerAdapter<String>(activity, year);
        spinnerYear.setAdapter(arrayyear);
    }

    private void bindMonth() {
        MonthInfo[] listmonth = MonthInfo.gets();
        mMonthList = new ArrayList<MonthInfo>();
        mMonthList.add(new MonthInfo(0, ""));
        for (int i = 0; i < listmonth.length; i++) {
            mMonthList.add(new MonthInfo(i + 1, listmonth[i].toString().trim()));
        }

        List<String> monthList = new ArrayList<String>();
        for (MonthInfo item : mMonthList) {
            monthList.add(item.monthName);
        }

        BHSpinnerAdapter<String> arraymonth = new BHSpinnerAdapter<String>(activity, monthList);
        spinnerMonth.setAdapter(arraymonth);
    }

    private void bindDay() {
        int positionSelected = spinnerDate.getSelectedItemPosition();

        String[] day;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, spinnerMonth.getSelectedItemPosition() - 1);
        c.set(Calendar.YEAR, ((Integer.valueOf(spinnerYear.getSelectedItem().toString())) - 543));
        int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        day = new String[daysInMonth + 1];
        day[0] = "";

        for (int i = 0; i < daysInMonth; i++) {
            day[i + 1] = String.format("%02d", (i + 1));
        }
        BHSpinnerAdapter<String> arrayday = new BHSpinnerAdapter<String>(activity, day);
        spinnerDate.setAdapter(arrayday);

        if (positionSelected > -1) {
            if (positionSelected >= day.length) {
                spinnerDate.setSelection(0);
            } else {
                spinnerDate.setSelection(positionSelected);
            }
        }
    }

    private void calculateAge() {
        Calendar c = Calendar.getInstance();
        if (spinnerYear.getSelectedItem().toString().equals("")) {
            editTextAge.setText("");
        } else {
            editTextAge.setText(String.valueOf(c.get(Calendar.YEAR) - (Integer.valueOf(spinnerYear.getSelectedItem().toString()) - 543)));
        }
    }
    //endregion

    //region ValidateData
    private boolean validateDataForPerson() {

        boolean ret = false;
        String title = "กรุณาป้อนข้อมูลให้ครบถ้วน";
        String message = "";

        //DebtorCustomer
        String typeCard = spinnerTypeCard.getSelectedItem().toString(); //ประเภทบัตร
        String idCard = editTextIdentificationCard.getText().toString(); //เลขที่บัตร
        String prefixName = spinnerPerfix.getSelectedItem().toString(); // คำนำหน้าชื่อ
        String name = editTextName.getText().toString(); //ชื่อ-สกุล
        String year = spinnerYear.getSelectedItem().toString(); // ปีเกิด
        String month = spinnerMonth.getSelectedItem().toString(); // เดือนเกิด
        String day = "";// วันเกิด
        if (!year.equals("") && !month.equals("")) {
            day = spinnerDate.getSelectedItem().toString();
        }
        String sex = spinnerSex.getSelectedItem().toString(); // เพศ
        String age = editTextAge.getText().toString(); //อายุ

        //Address
        String addressNumber = editTextAddressnumber.getText().toString(); // บ้านเลขที่
        String category = editTextCategory.getText().toString(); // หมู่ที่
        String alley = editTextAlley.getText().toString();// ซอย/ตรอก
        String road = editTextRoad.getText().toString(); // ถนน
        String province = autoCompleteTextViewProvince.getText().toString(); // จังหวัด
        String district = ""; // อำเภอ
        String subDistrict = ""; // ตำบล
        if (!province.equals("")) {
            district = spinnerDistrict.getSelectedItem().toString();
            if (!district.equals("")) {
                subDistrict = spinnerParish.getSelectedItem().toString();
            }
        }
        String zipCode = editTextZipcode.getText().toString();// รหัสไปรษณีย์
        String phone = editTextPhone.getText().toString();// เบอร์บ้าน
        String workPhone = editTextWorkPhone.getText().toString();// เบอร์ทีทำงาน
        String mobilePhone = editTextMobilePhone.getText().toString();// เบอร์มือถือ
        String eMail = editTextEmail.getText().toString();// อีเมล์

        if (typeCard.equals("") || //ประเภทบัตร
                idCard.equals("") ||//เลขที่บัตร
                prefixName.equals("") ||// คำนำหน้าชื่อ
                name.equals("") ||//ชื่อ-สกุล
                year.equals("") ||// ปีเกิด
                month.equals("") ||// เดือนเกิด
                day.equals("") ||// วันเกิด
                sex.equals("") ||// เพศ
                age.equals("") ||//อายุ
                addressNumber.equals("") ||// บ้านเลขที่
                category.equals("") ||// หมู่ที่
                alley.equals("") ||// ซอย/ตรอก
                road.equals("") || // ถนน
                province.equals("") ||// จังหวัด
                district.equals("") ||// อำเภอ
                subDistrict.equals("") || // ตำบล
                zipCode.equals("") ||// รหัสไปรษณีย์
                phone.equals("") ||// เบอร์บ้าน
                workPhone.equals("") ||// เบอร์ทีทำงาน
                mobilePhone.equals("") ||// เบอร์มือถือ
                eMail.equals(""))// อีเมล์
        {
            String[] strEditText = {typeCard,  //ประเภทบัตร
                    idCard, //เลขที่บัตร
                    prefixName, // คำนำหน้าชื่อ
                    name, //ชื่อ-สกุล
                    year, // ปีเกิด
                    month, // เดือนเกิด
                    day, // วันเกิด
                    sex, // เพศ
                    age, //อายุ
                    addressNumber, // บ้านเลขที่
                    category, // หมู่ที่
                    alley, // ซอย/ตรอก
                    road,  // ถนน
                    province, // จังหวัด
                    district, // อำเภอ
                    subDistrict,  // ตำบล
                    zipCode, // รหัสไปรษณีย์
                    phone, // เบอร์บ้าน
                    workPhone, // เบอร์ทีทำงาน
                    mobilePhone,// เบอร์มือถือ
                    eMail};// อีเมล์


            String[] strMessage = {"ประเภทบัตร", textViewIdentificationCard.getText().toString(), "คำนำหน้าชื่อ", "ชื่อ-สกุล", "ปีเกิด", "เดือนเกิด", "วันเกิด", "เพศ", "อายุ",
                    "บ้านเลขที่", "หมู่ที่", "ซอย/ตรอก", "ถนน", "จังหวัด", "อำเภอ/เขต", "ตำบล/แขวง", "รหัสไปรษณีย์", "เบอร์บ้าน", "เบอร์ที่ทำงาน", "เบอร์มือถือ/เบอร์ที่ติดต่อได้", "อีเมล์"};

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
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์บ้าน ให้ครบ");
            } else if (workPhone.replaceAll("-", "").length() < 9 && !workPhone.equals("-")) {
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์ที่ทำงาน ให้ครบ");
            } else if (mobilePhone.replaceAll("-", "").length() != 10) {
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์โทรมือถือให้ครบ 10 หลัก");
            } else {
                String getEditTextPhone = Character.toString(phone.charAt(0));
                String getEditTextWorkPhone = Character.toString(workPhone.charAt(0));

                if (!getEditTextPhone.equals("0") && !phone.equals("-")) {
                    showWarningDialog("คำเตือน", "เบอร์บ้านไม่ถูกต้อง");
                } else if (!getEditTextWorkPhone.equals("0") && !workPhone.equals("-")) {
                    showWarningDialog("คำเตือน", "เบอร์ที่ทำงานไม่ถูกต้อง");
                } else {
                    String getphone = Character.toString(mobilePhone.charAt(0));
                    if (!getphone.equals("0")) {
                        showWarningDialog("คำเตือน", "เบอร์มือถือไม่ถูกต้อง");
                    } else {
                        if (BHValidator.isEmailValid(eMail) || eMail.equals("-")) {
                            ret = true;
                        } else {
                            showWarningDialog("คำเตือน", "Email ไม่ถูกต้อง");
                        }
                    }
                }
            }
        }

        if (message != "") {
            showWarningDialog(title, message);
        }
        return ret;
    }

    private boolean validateDataForForeigner() {

        boolean ret = false;
        String title = "กรุณาป้อนข้อมูลให้ครบถ้วน";
        String message = "";

        //DebtorCustomer
        String typeCard = spinnerTypeCard.getSelectedItem().toString(); //ประเภทบัตร
        String idCard = editTextCardNo.getText().toString(); //เลขที่บัตรบุคคลต่างชาติ
        String prefixName = spinnerPerfix.getSelectedItem().toString(); // คำนำหน้าชื่อ
        String name = editTextName.getText().toString(); //ชื่อ-สกุล
        String year = spinnerYear.getSelectedItem().toString(); // ปีเกิด
        String month = spinnerMonth.getSelectedItem().toString(); // เดือนเกิด
        String day = "";// วันเกิด
        if (!year.equals("") && !month.equals("")) {
            day = spinnerDate.getSelectedItem().toString();
        }
        String sex = spinnerSex.getSelectedItem().toString(); // เพศ
        String age = editTextAge.getText().toString(); //อายุ

        //Address
        String addressNumber = editTextAddressnumber.getText().toString(); // บ้านเลขที่
        String category = editTextCategory.getText().toString(); // หมู่ที่
        String alley = editTextAlley.getText().toString();// ซอย/ตรอก
        String road = editTextRoad.getText().toString(); // ถนน
        String province = autoCompleteTextViewProvince.getText().toString(); // จังหวัด
        String district = ""; // อำเภอ
        String subDistrict = ""; // ตำบล
        if (!province.equals("")) {
            district = spinnerDistrict.getSelectedItem().toString();
            if (!district.equals("")) {
                subDistrict = spinnerParish.getSelectedItem().toString();
            }
        }
        String zipCode = editTextZipcode.getText().toString();// รหัสไปรษณีย์
        String phone = editTextPhone.getText().toString();// เบอร์บ้าน
        String workPhone = editTextWorkPhone.getText().toString();// เบอร์ทีทำงาน
        String mobilePhone = editTextMobilePhone.getText().toString();// เบอร์มือถือ
        String eMail = editTextEmail.getText().toString();// อีเมล์

        if (typeCard.equals("") || //ประเภทบัตร
                idCard.equals("") ||//เลขที่บัตร
                prefixName.equals("") ||// คำนำหน้าชื่อ
                name.equals("") ||//ชื่อ-สกุล
                year.equals("") ||// ปีเกิด
                month.equals("") ||// เดือนเกิด
                day.equals("") ||// วันเกิด
                sex.equals("") ||// เพศ
                age.equals("") ||//อายุ
                addressNumber.equals("") ||// บ้านเลขที่
                category.equals("") ||// หมู่ที่
                alley.equals("") ||// ซอย/ตรอก
                road.equals("") || // ถนน
                province.equals("") ||// จังหวัด
                district.equals("") ||// อำเภอ
                subDistrict.equals("") || // ตำบล
                zipCode.equals("") ||// รหัสไปรษณีย์
                phone.equals("") ||// เบอร์บ้าน
                workPhone.equals("") ||// เบอร์ทีทำงาน
                mobilePhone.equals("") ||// เบอร์มือถือ
                eMail.equals(""))// อีเมล์
        {
            String[] strEditText = {typeCard,  //ประเภทบัตร
                    idCard, //เลขที่บัตร
                    prefixName, // คำนำหน้าชื่อ
                    name, //ชื่อ-สกุล
                    year, // ปีเกิด
                    month, // เดือนเกิด
                    day, // วันเกิด
                    sex, // เพศ
                    age, //อายุ
                    addressNumber, // บ้านเลขที่
                    category, // หมู่ที่
                    alley, // ซอย/ตรอก
                    road,  // ถนน
                    province, // จังหวัด
                    district, // อำเภอ
                    subDistrict,  // ตำบล
                    zipCode, // รหัสไปรษณีย์
                    phone, // เบอร์บ้าน
                    workPhone, // เบอร์ทีทำงาน
                    mobilePhone,// เบอร์มือถือ
                    eMail};// อีเมล์


            String[] strMessage = {"ประเภทบัตร", "เลขที่บัตร", "คำนำหน้าชื่อ", "ชื่อ-สกุล", "ปีเกิด", "เดือนเกิด", "วันเกิด", "เพศ", "อายุ",
                    "บ้านเลขที่", "หมู่ที่", "ซอย/ตรอก", "ถนน", "จังหวัด", "อำเภอ/เขต", "ตำบล/แขวง", "รหัสไปรษณีย์", "เบอร์บ้าน", "เบอร์ที่ทำงาน", "เบอร์มือถือ/เบอร์ที่ติดต่อได้", "อีเมล์"};

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
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์บ้าน ให้ครบ");
            } else if (workPhone.replaceAll("-", "").length() < 9 && !workPhone.equals("-")) {
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์ที่ทำงาน ให้ครบ");
            } else if (mobilePhone.replaceAll("-", "").length() != 10) {
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์โทรมือถือให้ครบ 10 หลัก");
            } else {
                String getEditTextPhone = Character.toString(phone.charAt(0));
                String getEditTextWorkPhone = Character.toString(workPhone.charAt(0));

                if (!getEditTextPhone.equals("0") && !phone.equals("-")) {
                    showWarningDialog("คำเตือน", "เบอร์บ้านไม่ถูกต้อง");
                } else if (!getEditTextWorkPhone.equals("0") && !workPhone.equals("-")) {
                    showWarningDialog("คำเตือน", "เบอร์ที่ทำงานไม่ถูกต้อง");
                } else {
                    String getphone = Character.toString(mobilePhone.charAt(0));
                    if (!getphone.equals("0")) {
                        showWarningDialog("คำเตือน", "เบอร์มือถือไม่ถูกต้อง");
                    } else {
                        if (BHValidator.isEmailValid(eMail) || eMail.equals("-")) {
                            ret = true;
                        } else {
                            showWarningDialog("คำเตือน", "Email ไม่ถูกต้อง");
                        }
                    }
                }
            }
        }

        if (message != "") {
            showWarningDialog(title, message);
        }
        return ret;
    }

    private boolean validateDataForCorporation() {

        boolean ret = false;
        String title = "กรุณาป้อนข้อมูลให้ครบถ้วน";
        String message = "";


        //DebtorCustomer
        String identificationNumber = editTextIdentificationNumber.getText().toString(); //เลขประจำตัวผู้เสียภาษี
        String perfixCorporation = spinnerPerfixCorporation.getSelectedItem().toString(); // คำนำหน้าบริษัท
        String nameCorporation = editTextNameCorporation.getText().toString(); // ชื่อบริษัท
        String nameCommission = editTextNameCommission.getText().toString(); // ชื่อกรรมการผู้มีอำนาจ
        String nameCardNoCommission = editTextNameCardNoCommission.getText().toString(); // เลขบัตรกรรมการผู้มีอำนาจ

        //Address
        String addressNumber = editTextAddressnumber.getText().toString(); // บ้านเลขที่
        String category = editTextCategory.getText().toString(); // หมู่ที่
        String alley = editTextAlley.getText().toString();// ซอย/ตรอก
        String road = editTextRoad.getText().toString(); // ถนน
        String province = autoCompleteTextViewProvince.getText().toString(); // จังหวัด
        String district = ""; // อำเภอ
        String subDistrict = ""; // ตำบล
        if (!province.equals("")) {
            district = spinnerDistrict.getSelectedItem().toString();
            if (!district.equals("")) {
                subDistrict = spinnerParish.getSelectedItem().toString();
            }
        }
        String zipCode = editTextZipcode.getText().toString();// รหัสไปรษณีย์
        String phonecorporation1 = editTextPhonecorporation1.getText().toString(); // เบอร์โทรที่1
        String phonecorporation2 = editTextPhonecorporation2.getText().toString(); // เบอร์โทรที่2
        String faxcorporation = editTextFaxcorporation.getText().toString(); // เบอร์แฟกช์
        String eMail = editTextEmail.getText().toString();// อีเมล์

        if (identificationNumber.equals("") || //เลขประจำตัวผู้เสียภาษี
                perfixCorporation.equals("") || // คำนำหน้าบริษัท
                nameCorporation.equals("") || // ชื่อบริษัท
                nameCommission.equals("") || // ชื่อกรรมการผู้มีอำนาจ
                nameCardNoCommission.equals("") || // เลขบัตรกรรมการผู้มีอำนาจ
                addressNumber.equals("") ||// บ้านเลขที่
                category.equals("") ||// หมู่ที่
                alley.equals("") ||// ซอย/ตรอก
                road.equals("") || // ถนน
                province.equals("") ||// จังหวัด
                district.equals("") ||// อำเภอ
                subDistrict.equals("") || // ตำบล
                zipCode.equals("") ||// รหัสไปรษณีย์
                phonecorporation1.equals("") || // เบอร์โทรที่1
                phonecorporation2.equals("") || // เบอร์โทรที่2
                faxcorporation.equals("") || // เบอร์แฟกช์
                eMail.equals(""))// อีเมล์
        {
            String[] strEditText = {identificationNumber, //เลขประจำตัวผู้เสียภาษี
                    perfixCorporation, // คำนำหน้าบริษัท
                    nameCorporation, // ชื่อบริษัท
                    nameCommission, // ชื่อกรรมการผู้มีอำนาจ
                    nameCardNoCommission,// เลขบัตรกรรมการผู้มีอำนาจ
                    addressNumber,// บ้านเลขที่
                    category,// หมู่ที่
                    alley,// ซอย/ตรอก
                    road, // ถนน
                    province,// จังหวัด
                    district,// อำเภอ
                    subDistrict, // ตำบล
                    zipCode,// รหัสไปรษณีย์
                    phonecorporation1, // เบอร์โทรที่1
                    phonecorporation2, // เบอร์โทรที่2
                    faxcorporation, // เบอร์แฟกช์
                    eMail};// อีเมล์


            String[] strMessage = {"เลขประจำตัวผู้เสียภาษี", "คำนำหน้า", textViewNameCorporation.getText().toString(), "ชื่อกรรมการผู้มีอำนาจ", "เลขบัตรกรรมการผู้มีอำนาจ",
                    "บ้านเลขที่", "หมู่ที่", "ซอย/ตรอก", "ถนน", "จังหวัด", "อำเภอ/เขต", "ตำบล/แขวง", "รหัสไปรษณีย์", "เบอร์โทรศัพท์ 1", "เบอร์โทรศัพท์ 2", "เบอร์แฟกช์", "อีเมล์"};

            for (int i = 0; i < strEditText.length; i++) {
                if (strEditText[i].length() == 0) {
                    message += "กรุณาป้อนข้อมูล : " + strMessage[i];
                    if (i < (strEditText.length - 1)) {
                        message += "\n";
                    }
                }
            }
        } else {
            if (phonecorporation1.replaceAll("-", "").length() < 9) {
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์โทรศัพท์1 ให้ครบ");
            } else if (phonecorporation2.replaceAll("-", "").length() < 9 && !phonecorporation2.equals("-")) {
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์โทรศัพท์2 ให้ครบ");
            } else if (faxcorporation.replaceAll("-", "").length() < 9 && !faxcorporation.equals("-")) {
                showWarningDialog("คำเตือน", "กรุณาป้อนข้อมูลเบอร์แฟกซ์ ให้ครบ");
            } else {
                String getphone1 = Character.toString(phonecorporation1.charAt(0));
                String getphone2 = Character.toString(phonecorporation2.charAt(0));
                String getFax = Character.toString(faxcorporation.charAt(0));

                if (!getphone1.equals("0")) {
                    showWarningDialog("คำเตือน", "เบอร์โทรศัพท์1 ไม่ถูกต้อง");
                } else if (!getphone2.equals("0") && !phonecorporation2.equals("-")) {
                    showWarningDialog("คำเตือน", "เบอร์โทรศัพท์2 ไม่ถูกต้อง");
                } else if (!getFax.equals("0") && !faxcorporation.equals("-")) {
                    showWarningDialog("คำเตือน", "เบอร์แฟกซ์ ไม่ถูกต้อง");
                } else {
                    if (BHValidator.isEmailValid(eMail) || eMail.equals("-")) {
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
        //region เลขที่บัตร นิติบุคคล
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
        editTextNameCardNoCommission.setFilters(new InputFilter[]{CardNoCommission});
        //endregion

        //region เบอร์มือถือ/เบอร์ที่ติดต่อได้
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
        editTextMobilePhone.setFilters(new InputFilter[]{MobilePhone});
        //endregion

        //region เบอร์ที่ทำงาน, เบอร์โทรศัพท์ 2, เบอร์แฟกซ์
        InputFilter PhoneNumber = new InputFilter() {
            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int phoneNumber, int i4) {
                if (source.length() > 0) {
                    if (i4 == 0 && source.equals(" ")) {//ตัดค่าว่างตัวแรก
                        return "";
                    } else {
                        if (phoneNumber == 1 || phoneNumber == 6) {
                            return "-" + source;
                        }
                    }
                }
                return null;
            }
        };
        editTextWorkPhone.setFilters(new InputFilter[]{PhoneNumber});
        editTextPhonecorporation2.setFilters(new InputFilter[]{PhoneNumber});
        editTextFaxcorporation.setFilters(new InputFilter[]{PhoneNumber});
        //endregion

        //region เบอร์โทรศัพท์ 1
        InputFilter PhoneNumber1 = new InputFilter() {
            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int phoneNumber, int i4) {
                if (source.length() > 0) {
                    if (!Character.isDigit(source.charAt(0)))
                        return "";
                    else {
                        if (phoneNumber == 1 || phoneNumber == 6) {
                            return "-" + source;
                        }
                    }
                }
                return null;
            }
        };
        editTextPhonecorporation1.setFilters(new InputFilter[]{PhoneNumber1});
        //endregion

        //region เบอร์บ้าน
        InputFilter PhoneNumberHome = new InputFilter() {
            public CharSequence filter(CharSequence source, int i1, int i2, Spanned spanned, int phoneNumber, int i4) {
                if (source.length() > 0) {
                    if (i4 == 0 && source.equals(" ")) {//ตัดค่าว่างตัวแรก
                        return "";
                    } else {
                        if (String.valueOf(source.charAt(0)).matches("([0-9])") || (i4 == 0 && source.equals("-"))) {
                            if (phoneNumber == 1 || phoneNumber == 6) {
                                return "-" + source;
                            }
                        } else {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        editTextPhone.setFilters(new InputFilter[]{PhoneNumberHome});
        //endregion

        //region ชื่อ-สกุล, เลขที่บัตรบุคคลต่างชาติ, ชื่อบริษัท, ชื่อกรรมการผู้มีอำนาจ, บ้านเลขท, หมู่ที่, ซอย/ตรอก, ถนน
        InputFilter TextValue = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int i, int i1, Spanned spanned, int i2, int i3) {
                if (source.length() > 0) {
                    if (i3 == 0 && source.equals(" ")) {//ตัดค่าว่างตัวแรก
                        return "";
                    }
                    return source;
                }
                return null;
            }
        };
        editTextName.setFilters(new InputFilter[]{TextValue}); //ชื่อ-สกุล

        editTextCardNo.setFilters(new InputFilter[]{TextValue}); //เลขที่บัตรบุคคลต่างชาติ

        editTextNameCorporation.setFilters(new InputFilter[]{TextValue}); // ชื่อบริษัท
        editTextNameCommission.setFilters(new InputFilter[]{TextValue}); // ชื่อกรรมการผู้มีอำนาจ

        editTextAddressnumber.setFilters(new InputFilter[]{TextValue}); // บ้านเลขที่
        editTextCategory.setFilters(new InputFilter[]{TextValue}); // หมู่ที่
        editTextAlley.setFilters(new InputFilter[]{TextValue});// ซอย/ตรอก
        editTextRoad.setFilters(new InputFilter[]{TextValue}); // ถนน
        //endregion
    }

    private void CheckIDcardCommission(String id) {
        String idcard = editTextNameCardNoCommission.getText().toString().replaceAll("-", "");
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
                idcard = editTextIdentificationCard.getText().toString().replaceAll("-", "");
                if (BHValidator.isValidCitizenID(idcard)) {
                    ret = true;
                } else {
                    String title = "รายละเอียดข้อมูลลูกค้า";
                    String message = "กรุณาตรวจสอบ" + spinnerTypeCard.getSelectedItem().toString() + "!";
                    //showNoticeDialogBox(title, message);
                    showWarningDialog(title, message);
                }
                break;
            case CORPORATION://นิติบุคคล
                idcard = editTextNameCardNoCommission.getText().toString().replaceAll("-", "");
                if (BHValidator.isValidCitizenID(idcard)) {
                    ret = true;
                } else {
                    String title = "รายละเอียดข้อมูลลูกค้า";
                    String message = "กรุณาตรวจสอบเลขที่บัตร!";
                    //showNoticeDialogBox(title, message);
                    showWarningDialog(title, message);
                }
                break;
            default:
                break;
        }
        return ret;
    }

    private boolean checkIdentificationNumber() {
        boolean ret = false;
        if (editTextIdentificationNumber.length() == 13) {
            ret = true;
        } else {
            String title = "รายละเอียดข้อมูลลูกค้า";
            String message = "กรุณาตรวจสอบเลขประจำตัวผู้เสียภาษี!";
            //showNoticeDialogBox(title, message);
            showWarningDialog(title, message);
        }
        return ret;
    }


    private void save() {
        (new BackgroundProcess(activity) {
            DebtorCustomerInfo cust = new DebtorCustomerInfo();
            AddressInfo address = new AddressInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub

                //region DebtorCustomer

                switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
                    case Sale:
                    case EditContract:
                        ContractInfo contractInfo = TSRController.getContract(BHPreference.RefNo());
                        if (contractInfo != null) {
                            if (contractInfo.CustomerID != null && !contractInfo.CustomerID.equals("")) {
                                cust.CustomerID = contractInfo.CustomerID;
                            } else {
                                cust.CustomerID = DatabaseHelper.getUUID();
                            }
                        }
                        break;
                    case ChangeContract:
                        if (data.newContract.CustomerID == null) {
                            cust.CustomerID = DatabaseHelper.getUUID();
                        } else {
                            cust.CustomerID = data.newContract.CustomerID;
                        }
                        break;
                }

                cust.OrganizationCode = BHPreference.organizationCode();
                Calendar c = Calendar.getInstance();
                PrefixInfo prefixInfo = new PrefixInfo();
                switch (mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType) {

                    case PERSON://บุคคลธรรมดา
                        prefixInfo = mPrefixList.get(spinnerPerfix.getSelectedItemPosition());
                        cust.PrefixCode = prefixInfo.PrefixCode;// คำนำหน้าชื่อ
                        cust.PrefixName = prefixInfo.PrefixName;// คำนำหน้าชื่อ
                        cust.CustomerName = editTextName.getText().toString();//ชื่อ-สกุล
                        cust.CustomerType = mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonTypeCode;//ประเภทบุคคล
                        cust.IDCardType = mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard.toString();//ประเภทบัตร
                        cust.IDCard = editTextIdentificationCard.getText().toString();//เลขที่บัตร
                        cust.CompanyName = null;// ชื่อบริษัท
                        cust.AuthorizedName = null;// ชื่อกรรมการผู้มีอำนาจ

                   /*     if(select_read_card==1){
                           // showDialog("1111","1111");
                            cust.AuthorizedIDCard = mPersonal.getIssueDate()+"#"+mPersonal.getExpireDate(); // เลขบัตรกรรมการผู้มีอำนาจxcxaCsaxcsCs
                        }
                        else {
                          //  showDialog("2222","2222");
                            cust.AuthorizedIDCard = null; // เลขบัตรกรรมการผู้มีอำนาจ
                        }*/



                        if(select_read_card==1){
                            try {
                                cust.AuthorizedIDCard = mPersonal.getIssueDate()+"#"+mPersonal.getExpireDate(); // เลขบัตรกรรมการผู้มีอำนาจxcxaCsaxcsCs
                            }
                            catch (Exception ex){

                            }

                        }
                        else {
                            cust.AuthorizedIDCard = null; // เลขบัตรกรรมการผู้มีอำนาจ
                        }

                        c.set(Calendar.MONTH, spinnerMonth.getSelectedItemPosition() - 1);// เดือนเกิด
                        c.set(Calendar.YEAR, ((Integer.valueOf(spinnerYear.getSelectedItem().toString())) - 543));// ปีเกิด
                        c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(spinnerDate.getSelectedItem().toString()));// วันเกิด
                        c.set(Calendar.HOUR, 0);
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        c.set(Calendar.HOUR_OF_DAY, 0);
                        cust.Brithday = c.getTime();

                        cust.Sex = spinnerSex.getSelectedItem().toString();// เพศ
                        break;

                    case CORPORATION://นิติบุคคล
                        prefixInfo = mPrefixCorporationList.get(spinnerPerfixCorporation.getSelectedItemPosition());
                        cust.PrefixCode = prefixInfo.PrefixCode;// คำนำหน้าบริษัท
                        cust.PrefixName = prefixInfo.PrefixName;// คำนำหน้าบริษัท
                        cust.CustomerName = null;//ชื่อ-สกุล
                        cust.CustomerType = mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonTypeCode;//ประเภทบุคคล
                        cust.IDCardType = null;//ประเภทบัตร
                        cust.IDCard = editTextIdentificationNumber.getText().toString();//เลขประจำตัวผู้เสียภาษี
                        cust.CompanyName = editTextNameCorporation.getText().toString(); // ชื่อบริษัท
                        cust.AuthorizedName = editTextNameCommission.getText().toString(); // ชื่อกรรมการผู้มีอำนาจ
                        cust.AuthorizedIDCard = editTextNameCardNoCommission.getText().toString(); // เลขบัตรกรรมการผู้มีอำนาจ

                        c.set(Calendar.HOUR, 0);
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        c.set(Calendar.HOUR_OF_DAY, 0);
                        cust.Brithday = c.getTime();

                        cust.Sex = null;// เพศ
                        break;

                    case FOREIGNER://บุคคลต่างชาติ
                        prefixInfo = mPrefixList.get(spinnerPerfix.getSelectedItemPosition());
                        cust.PrefixCode = prefixInfo.PrefixCode;// คำนำหน้าชื่อ
                        cust.PrefixName = prefixInfo.PrefixName;// คำนำหน้าชื่อ
                        cust.CustomerName = editTextName.getText().toString();//ชื่อ-สกุล
                        cust.CustomerType = mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonTypeCode;//ประเภทบุคคล
                        cust.IDCardType = mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard.toString();//ประเภทบัตร
                        cust.IDCard = editTextCardNo.getText().toString();//เลขที่บัตรบุคคลต่างชาติ
                        cust.CompanyName = null;// ชื่อบริษัท
                        cust.AuthorizedName = null;// ชื่อกรรมการผู้มีอำนาจ
                        cust.AuthorizedIDCard = null; // เลขบัตรกรรมการผู้มีอำนาจ

                        c.set(Calendar.MONTH, spinnerMonth.getSelectedItemPosition() - 1);// เดือนเกิด
                        c.set(Calendar.YEAR, ((Integer.valueOf(spinnerYear.getSelectedItem().toString())) - 543));// ปีเกิด
                        c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(spinnerDate.getSelectedItem().toString()));// วันเกิด
                        c.set(Calendar.HOUR, 0);
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        c.set(Calendar.HOUR_OF_DAY, 0);
                        cust.Brithday = c.getTime();

                        cust.Sex = spinnerSex.getSelectedItem().toString();// เพศ
                        break;
                }

                cust.DebtStatus = 1;/**1 = ลูกหนี้ (ยังชำระเงินไม่เสร็จสิ้น) / 2 = ลูกค้า (ไม่มีหนี้คงค้างกับบริษัท)**/

                if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
                    DebtorCustomerInfo customerInfo = getDebCustometByID(cust.CustomerID);
                    cust.HabitatTypeCode = customerInfo.HabitatTypeCode;// No use
                    cust.HabitatDetail = customerInfo.HabitatDetail;
                    cust.OccupyType = customerInfo.OccupyType;
                    cust.CareerCode = customerInfo.CareerCode;
                    cust.CareerDetail = customerInfo.CareerDetail;
                    cust.HobbyCode = customerInfo.HobbyCode;
                    cust.HobbyDetail = customerInfo.CareerDetail;
                    cust.IsUsedProduct = customerInfo.IsUsedProduct;
                    cust.UsedProductModelID = customerInfo.UsedProductModelID;
                    cust.SuggestionCode = customerInfo.SuggestionCode;
                    cust.SuggestionDetail = customerInfo.SuggestionDetail;
                    cust.CreateDate = customerInfo.CreateDate;
                    cust.CreateBy = customerInfo.CreateBy;
                    cust.LastUpdateDate = new Date();
                    cust.LastUpdateBy = BHPreference.employeeID();
                } else {
                    cust.HabitatTypeCode = "";// No use
                    cust.HabitatDetail = "";
                    cust.OccupyType = "";
                    cust.CareerCode = "";
                    cust.CareerDetail = "";
                    cust.HobbyCode = "";
                    cust.HobbyDetail = "";
                    cust.IsUsedProduct = false;
                    cust.UsedProductModelID = "";
                    cust.SuggestionCode = "";
                    cust.SuggestionDetail = "";
                    cust.CreateDate = new Date();
                    cust.CreateBy = BHPreference.employeeID();
                    cust.LastUpdateDate = new Date();
                    cust.LastUpdateBy = BHPreference.employeeID();
                }
                //endregion

                //region Address
                address.RefNo = BHPreference.RefNo();
                address.AddressTypeCode = addressType.toString();// "AddressIDCard";
                address.AddressDetail = editTextAddressnumber.getText().toString();
                address.AddressDetail2 = editTextCategory.getText().toString();
                address.AddressDetail3 = editTextAlley.getText().toString();
                address.AddressDetail4 = editTextRoad.getText().toString();


                ProvinceInfo province = getProvinceInfo(autoCompleteTextViewProvince.getText().toString());
                if (province != null) {
                    address.ProvinceCode = province.ProvinceCode;
                    address.ProvinceName = province.ProvinceName;
                }

                DistrictInfo district = mDistrictList.get(spinnerDistrict.getSelectedItemPosition());
                address.DistrictCode = district.DistrictCode;
                address.DistrictName = district.DistrictName;
                SubDistrictInfo subDistrict = mSubDistrictList.get(spinnerParish.getSelectedItemPosition());
                address.SubDistrictCode = subDistrict.SubDistrictCode;
                address.SubDistrictName = subDistrict.SubDistrictName;
                address.Zipcode = editTextZipcode.getText().toString();

                address.Latitude = latitude;
                address.Longitude = longitude;
                address.AddressInputMethod = "";

                switch (mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType) {
                    case PERSON://บุคคลธรรมดา
                    case FOREIGNER://บุคคลต่างชาติ
                        address.TelHome = editTextPhone.getText().toString();
                        address.TelOffice = editTextWorkPhone.getText().toString();
                        address.TelMobile = editTextMobilePhone.getText().toString();
                        break;
                    case CORPORATION://นิติบุคคล
                        address.TelHome = editTextPhonecorporation1.getText().toString();
                        address.TelOffice = editTextPhonecorporation2.getText().toString();
                        address.TelMobile = editTextFaxcorporation.getText().toString();
                        break;
                }

                address.EMail = editTextEmail.getText().toString();
                address.CreateDate = new Date();
                address.LastUpdateDate = new Date();
                //endregion

                //region Contract
                mainContractInfo.RefNo = BHPreference.RefNo();
                mainContractInfo.CustomerID = cust.CustomerID;

                String strStatusCode = "";
                switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
                    case Sale:
                        strStatusCode = "03";
                        break;
                    case ChangeContract:
                    case EditContract:
                        strStatusCode = mainContractInfo.StatusCode;
                        break;
                }
                mainContractInfo.StatusCode = strStatusCode;
                mainContractInfo.todate = new Date();
                mainContractInfo.LastUpdateBy = BHPreference.employeeID();
                mainContractInfo.LastUpdateDate = new Date();
                //endregion
            }

            @Override
            protected void calling() {
                switch (Enum.valueOf(SaleFirstPaymentChoiceFragment.ProcessType.class, BHPreference.ProcessType())) {
                    case Sale:
                    case EditContract:
                        checkSaveData = true;
                        saveCustomerData(cust, address, mainContractInfo, true);
                        break;
                    case ChangeContract:
                        checkSaveData = true;
                        data.newContract.CustomerID = cust.CustomerID;
                        data.newDebtorCustomer = cust;

                        address.RefNo = data.newContract.RefNo;
                        switch (Enum.valueOf(AddressInfo.AddressType.class, addressType.toString())) {

                            case AddressIDCard:
                                data.newAddressIDCard = address;
                                break;
                            case AddressPayment:
                                data.newAddressPayment = address;
                                break;
                            case AddressInstall:
                                data.newAddressInstall = address;
                                break;
                        }
                        //tmpCustomer = cust;
                        //tmpAddressIDCard = address;
                        break;
                }
            }

            @Override
            protected void after() {
                switch (Enum.valueOf(AddressInfo.AddressType.class, addressType.toString())) {

                    case AddressIDCard:
                        data.useDebtorCustomer = null;
                        data.useAddressIDCard = null;
                        break;
                    case AddressPayment:
                        New2SaleCustomerAddressInstallFragment.Data.useAddressPayment = null;
                        New2SaleCustomerAddressCardFragment.Data.useAddressPayment = null;
                        break;
                    case AddressInstall:
                        New2SaleCustomerAddressCardFragment.Data.useAddressInstall = null;
                        break;
                }

                String title = "รายละเอียดข้อมูลลูกค้า";
                String message = "บันทึกข้อมูลเสร็จเรียบร้อย";
                showWarningDialog(title, message);

            }
        }).start();
    }

    private void CheckIDcard(boolean isImportThaiIDCard) {
        PersonTypeInfo personType = mPersonTypeList.get(spinnerType.getSelectedItemPosition());
        PersonTypeCardInfo personTypeCard = mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition());
        String idcard;
        String cardTypeName;
        switch (Enum.valueOf(PersonTypeInfo.PersonTypeEnum.class, personType.PersonType.toString())) {

            case PERSON:/** 0-บุคคลธรรมดา **/
                if (!mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCardName.equals("")
                        && mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD
                        && isImportThaiIDCard
                        && BHGeneral.ID_CARD_MODE) {
                    //showDialog("4444","4444");
                    importThaiIDCard();
                } else {

                   // showDialog("3333","3333");
                    idcard = editTextIdentificationCard.getText().toString().replaceAll("-", "");
                    cardTypeName = textViewIdentificationCard.getText().toString();    // บัตรประชาชน/ใบขับขี่/บัตรข้าราชการ
                    boolean checkidcard = BHValidator.isValidCitizenID(idcard);

                    if (!mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCardName.equals("")) {
                        if (checkidcard == true || mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.OFFICIALCARD) {
                            //บัตรข้าราชการ
                            stringStatusCheckID = true;
                            SaleCheckIDCardFragment.Data data1 = new SaleCheckIDCardFragment.Data();
                            data1.idCard = editTextIdentificationCard.getText().toString();
                            data1.cardTypeName = cardTypeName;
                            data1.personType = personType.PersonTypeCode;
                            data1.personTypeCard = personTypeCard.PersonTypeCard.toString();

                            data1.tmpDebtorCustomer = saveLogDebtorCustomer();
                            data1.tmpAddress = saveLogAddress();

                            //changeContract
                            if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract.toString())) {

                                data1.selectedCauseName = data.selectedCauseName;
                                data1.chgContractRequest = data.chgContractRequest;
                                data1.chgContractApprove = data.chgContractApprove;
                                data1.chgContractAction = data.chgContractAction;
                                data1.assign = data.assign;
                                data1.oldContract = data.oldContract;
                                data1.newContract = data.newContract;
                                data1.newSPPList = data.newSPPList;
                                data1.newPayment = data.newPayment;

                                data1.newContractImageInfo = data.newContractImageInfo;
                            }

                            SaleCheckIDCardFragment fm = BHFragment.newInstance(SaleCheckIDCardFragment.class, data1);
                            //showNextView(fm);

                            if (mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD
                                || mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.DRIVINGLICENSE) {

                                String title = "รายละเอียดข้อมูลลูกค้า";
                                //String message = "รหัสบัตรประชาชนไม่ถูกต้อง";
                                String message = spinnerTypeCard.getSelectedItem().toString() + "ถูกต้อง";
                                showWarningDialog(title, message);
                            } else if (mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.OFFICIALCARD){
                                if (!idcard.equals("")) {
                                    String title = "รายละเอียดข้อมูลลูกค้า";
                                    String message = "ข้อมูลเรียบร้อย";
                                    showWarningDialog(title, message);
                                } else {
                                    String title = "รายละเอียดข้อมูลลูกค้า";
                                    String message = "กรุณาใส่" + spinnerTypeCard.getSelectedItem().toString();
                                    showWarningDialog(title, message);
                                }
                            }
                        } else {
                            String title = "รายละเอียดข้อมูลลูกค้า";
                            //String message = "รหัสบัตรประชาชนไม่ถูกต้อง";
                            String message = spinnerTypeCard.getSelectedItem().toString() + "ไม่ถูกต้อง";
                            showWarningDialog(title, message);
                        }
                    } else {
                        String title = "รายละเอียดข้อมูลลูกค้า";
                        String message = "กรุณาเลือกประเภทบัตร";
                        showWarningDialog(title, message);
                    }
                }
                break;
            case CORPORATION: /** 1-นิติบุคคล **/
                idcard = editTextIdentificationNumber.getText().toString();
                cardTypeName = textViewIdentificationNumber.getText().toString();    // เลขประจำตัวผู้เสียภาษี

                if (!idcard.equals("")) {
                    if (checkIdentificationNumber()) {
                        if (checkIDcard()) {
                            stringStatusCheckID = true;

                            SaleCheckIDCardFragment.Data data1 = new SaleCheckIDCardFragment.Data();
                            data1.idCard = idcard;
                            data1.cardTypeName = cardTypeName;
                            data1.personType = personType.PersonTypeCode;
                            data1.personTypeCard = null;

                            data1.tmpDebtorCustomer = saveLogDebtorCustomer();
                            data1.tmpAddress = saveLogAddress();

                            //changeContract
                            if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract.toString())) {

                                data1.selectedCauseName = data.selectedCauseName;
                                data1.chgContractRequest = data.chgContractRequest;
                                data1.chgContractApprove = data.chgContractApprove;
                                data1.chgContractAction = data.chgContractAction;
                                data1.assign = data.assign;
                                data1.oldContract = data.oldContract;
                                data1.newContract = data.newContract;
                                data1.newSPPList = data.newSPPList;
                                data1.newPayment = data.newPayment;

                                data1.newContractImageInfo = data.newContractImageInfo;
                            }

                            SaleCheckIDCardFragment fm = BHFragment.newInstance(SaleCheckIDCardFragment.class, data1);
                            //showNextView(fm);

                            String title = "รายละเอียดข้อมูลลูกค้า";
                            String message = "เลขที่บัตรถูกต้อง";
                            //showNoticeDialogBox(title, message);
                            showWarningDialog(title, message);
                        }
                    }
                } else {
                    String title = "รายละเอียดข้อมูลลูกค้า";
                    String message = "กรุณาใส่เลขประจำตัวผู้เสียภาษี";
                    showWarningDialog(title, message);
                }
                break;
            case FOREIGNER:/** 2-บุคคลต่างชาติ **/
                idcard = editTextCardNo.getText().toString();
                cardTypeName = textViewCardNo.getText().toString();    // เลขที่บัตรบุคคลต่างชาติ

                if (!mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCardName.equals("")) {
                    if (!idcard.equals("")) {
                        stringStatusCheckID = true;

                        SaleCheckIDCardFragment.Data data1 = new SaleCheckIDCardFragment.Data();
                        data1.idCard = idcard;
                        data1.cardTypeName = cardTypeName;
                        data1.personType = personType.PersonTypeCode;
                        data1.personTypeCard = personTypeCard.PersonTypeCard.toString();

                        data1.tmpDebtorCustomer = saveLogDebtorCustomer();
                        data1.tmpAddress = saveLogAddress();

                        //changeContract
                        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract.toString())) {

                            data1.selectedCauseName = data.selectedCauseName;
                            data1.chgContractRequest = data.chgContractRequest;
                            data1.chgContractApprove = data.chgContractApprove;
                            data1.chgContractAction = data.chgContractAction;
                            data1.assign = data.assign;
                            data1.oldContract = data.oldContract;
                            data1.newContract = data.newContract;
                            data1.newSPPList = data.newSPPList;
                            data1.newPayment = data.newPayment;

                            data1.newContractImageInfo = data.newContractImageInfo;
                        }

                        SaleCheckIDCardFragment fm = BHFragment.newInstance(SaleCheckIDCardFragment.class, data1);
                        //showNextView(fm);

                        String title = "รายละเอียดข้อมูลลูกค้า";
                        String message = "ข้อมูลเรียบร้อย";
                        showWarningDialog(title, message);

                    } else {
                        String title = "รายละเอียดข้อมูลลูกค้า";
                        String message = "กรุณาใส่เลขที่บัตร";
                        showWarningDialog(title, message);
                    }
                } else {
                    String title = "รายละเอียดข้อมูลลูกค้า";
                    String message = "กรุณาเลือกประเภทบัตร";
                    showWarningDialog(title, message);
                }
                break;
        }
    }

    private DebtorCustomerInfo saveLogDebtorCustomer() {

        DebtorCustomerInfo cust = new DebtorCustomerInfo();

        //region DebtorCustomer

        Calendar c = Calendar.getInstance();
        PrefixInfo prefixInfo = new PrefixInfo();
        switch (mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType) {

            case PERSON://บุคคลธรรมดา
                prefixInfo = mPrefixList.get(spinnerPerfix.getSelectedItemPosition());
                cust.PrefixCode = prefixInfo.PrefixCode;// คำนำหน้าชื่อ
                cust.PrefixName = prefixInfo.PrefixName;// คำนำหน้าชื่อ
                cust.CustomerName = editTextName.getText().toString();//ชื่อ-สกุล
                cust.CustomerType = mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonTypeCode;//ประเภทบุคคล
                cust.IDCardType = mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard.toString();//ประเภทบัตร
                cust.IDCard = editTextIdentificationCard.getText().toString();//เลขที่บัตร
                cust.CompanyName = null;// ชื่อบริษัท
                cust.AuthorizedName = null;// ชื่อกรรมการผู้มีอำนาจ

                if(select_read_card==1){
                    try {
                        cust.AuthorizedIDCard = mPersonal.getIssueDate()+"#"+mPersonal.getExpireDate(); // เลขบัตรกรรมการผู้มีอำนาจxcxaCsaxcsCs
                    }
                    catch (Exception ex){

                    }

                }
                else {
                    cust.AuthorizedIDCard = null; // เลขบัตรกรรมการผู้มีอำนาจ
                }

               // cust.AuthorizedIDCard = null; // เลขบัตรกรรมการผู้มีอำนาจ

                if (spinnerYear.getSelectedItemPosition() > 0 && spinnerMonth.getSelectedItemPosition() > 0 && spinnerDate.getSelectedItemPosition() > 0) {
                    c.set(Calendar.MONTH, spinnerMonth.getSelectedItemPosition() - 1);// เดือนเกิด
                    c.set(Calendar.YEAR, ((Integer.valueOf(spinnerYear.getSelectedItem().toString())) - 543));// ปีเกิด
                    c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(spinnerDate.getSelectedItem().toString()));// วันเกิด
                    c.set(Calendar.HOUR, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    cust.Brithday = c.getTime();
                } else {
                    cust.Brithday = null;
                    cust.logYear = spinnerYear.getSelectedItemPosition();
                    cust.logMonth = spinnerMonth.getSelectedItemPosition();
                    cust.logDay = spinnerDate.getSelectedItemPosition();
                }


                cust.Sex = spinnerSex.getSelectedItem().toString();// เพศ
                break;

            case CORPORATION://นิติบุคคล
                prefixInfo = mPrefixCorporationList.get(spinnerPerfixCorporation.getSelectedItemPosition());
                cust.PrefixCode = prefixInfo.PrefixCode;// คำนำหน้าบริษัท
                cust.PrefixName = prefixInfo.PrefixName;// คำนำหน้าบริษัท
                cust.CustomerName = null;//ชื่อ-สกุล
                cust.CustomerType = mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonTypeCode;//ประเภทบุคคล
                cust.IDCardType = null;//ประเภทบัตร
                cust.IDCard = editTextIdentificationNumber.getText().toString();//เลขประจำตัวผู้เสียภาษี
                cust.CompanyName = editTextNameCorporation.getText().toString(); // ชื่อบริษัท
                cust.AuthorizedName = editTextNameCommission.getText().toString(); // ชื่อกรรมการผู้มีอำนาจ
                cust.AuthorizedIDCard = editTextNameCardNoCommission.getText().toString(); // เลขบัตรกรรมการผู้มีอำนาจ

                cust.Brithday = null;

                cust.Sex = null;// เพศ
                break;

            case FOREIGNER://บุคคลต่างชาติ
                prefixInfo = mPrefixList.get(spinnerPerfix.getSelectedItemPosition());
                cust.PrefixCode = prefixInfo.PrefixCode;// คำนำหน้าชื่อ
                cust.PrefixName = prefixInfo.PrefixName;// คำนำหน้าชื่อ
                cust.CustomerName = editTextName.getText().toString();//ชื่อ-สกุล
                cust.CustomerType = mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonTypeCode;//ประเภทบุคคล
                cust.IDCardType = mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard.toString();//ประเภทบัตร
                cust.IDCard = editTextCardNo.getText().toString();//เลขที่บัตรบุคคลต่างชาติ
                cust.CompanyName = null;// ชื่อบริษัท
                cust.AuthorizedName = null;// ชื่อกรรมการผู้มีอำนาจ
                cust.AuthorizedIDCard = null; // เลขบัตรกรรมการผู้มีอำนาจ

                if (spinnerYear.getSelectedItemPosition() > 0 && spinnerMonth.getSelectedItemPosition() > 0 && spinnerDate.getSelectedItemPosition() > 0) {
                    c.set(Calendar.MONTH, spinnerMonth.getSelectedItemPosition() - 1);// เดือนเกิด
                    c.set(Calendar.YEAR, ((Integer.valueOf(spinnerYear.getSelectedItem().toString())) - 543));// ปีเกิด
                    c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(spinnerDate.getSelectedItem().toString()));// วันเกิด
                    c.set(Calendar.HOUR, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    cust.Brithday = c.getTime();
                } else {
                    cust.Brithday = null;
                    cust.logYear = spinnerYear.getSelectedItemPosition();
                    cust.logMonth = spinnerMonth.getSelectedItemPosition();
                    cust.logDay = spinnerDate.getSelectedItemPosition();
                }

                cust.Sex = spinnerSex.getSelectedItem().toString();// เพศ
                break;
        }
        //endregion

        return cust;
    }

    private AddressInfo saveLogAddress() {

        AddressInfo address = new AddressInfo();

        //region Address
        address.AddressDetail = editTextAddressnumber.getText().toString();
        address.AddressDetail2 = editTextCategory.getText().toString();
        address.AddressDetail3 = editTextAlley.getText().toString();
        address.AddressDetail4 = editTextRoad.getText().toString();


        ProvinceInfo province = getProvinceInfo(autoCompleteTextViewProvince.getText().toString());
        if (province != null) {
            address.ProvinceCode = province.ProvinceCode;
            address.ProvinceName = province.ProvinceName;
        }

        if (spinnerDistrict.getSelectedItemPosition() > -1) {
            DistrictInfo district = mDistrictList.get(spinnerDistrict.getSelectedItemPosition());
            address.DistrictCode = district.DistrictCode;
            address.DistrictName = district.DistrictName;
        } else {
            address.DistrictCode = null;
            address.DistrictName = null;
        }

        if (spinnerParish.getSelectedItemPosition() > -1) {
            SubDistrictInfo subDistrict = mSubDistrictList.get(spinnerParish.getSelectedItemPosition());
            address.SubDistrictCode = subDistrict.SubDistrictCode;
            address.SubDistrictName = subDistrict.SubDistrictName;
            address.Zipcode = editTextZipcode.getText().toString();
        } else {
            address.SubDistrictCode = null;
            address.SubDistrictName = null;
            address.Zipcode = null;
        }

        switch (mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType) {
            case PERSON://บุคคลธรรมดา
            case FOREIGNER://บุคคลต่างชาติ
                address.TelHome = editTextPhone.getText().toString();
                address.TelOffice = editTextWorkPhone.getText().toString();
                address.TelMobile = editTextMobilePhone.getText().toString();
                break;
            case CORPORATION://นิติบุคคล
                address.TelHome = editTextPhonecorporation1.getText().toString();
                address.TelOffice = editTextPhonecorporation2.getText().toString();
                address.TelMobile = editTextFaxcorporation.getText().toString();
                break;
        }

        address.EMail = editTextEmail.getText().toString();
        //endregion

        return address;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract.toString()) && data.newContract != null) {
                    IMAGE_DIRECTORY_NAME = data.newContract.RefNo;
                }
                previewCapturedImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showMessage("User cancelled image capture");
            } else {
                showMessage("Sorry! Failed to capture image");
            }
        }
    }

    private void bindContractImage() {
        if (mainContractImageInfo != null) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 5;
                BHStorage.FolderType F = BHStorage.FolderType.Picture;
                Parth = BHStorage.getFolder(F);
                imageID = mainContractImageInfo.ImageID.toString();
                //File imageFile = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode + "/" + imageID + ".jpg");
                File imageFile = new File(Parth + "/" + mainContractImageInfo.RefNo + "/" + imageTypeCode + "/" + imageID + ".jpg");

                if (!imageFile.exists()) {

                    /*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
                    //new MainActivity.LoadImageToImageView(mainContractImageInfo, imageViewPerson).execute(String.format("%s%s", BHPreference.TSR_IMAGE_URL, mainContractImageInfo.ImageName));
                    new MainActivity.LoadImageToImageView(mainContractImageInfo, imageViewPerson).execute(String.format("%s%s", BHPreference.TSR_IMAGE_URL, mainContractImageInfo.ImageID));
                    /*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

                } else {

                    String part = imageFile.getPath();
                    Bitmap bm = BHBitmap.decodeSampledBitmapFromImagePath(part, 500);
                    Bitmap bitmap = BHBitmap.setRotateImageFromImagePath(part, bm);

                    imageViewPerson.setImageBitmap(bitmap);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            if (fileUri != null && fileUri.getPath() != null) {
                previewCapturedImage();
            }
        }
    }


    private void captureImage() {
        /*** [START] :: Permission ***/

        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, (data != null && data.newContract != null) ? data.newContract : new ContractInfo());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/

        new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

            @Override
            public void onSuccess(BHPermissions bhPermissions) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, (data != null && data.newContract != null) ? data.newContract : new ContractInfo());
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

    private void previewCapturedImage() {
        try {
            imageViewPerson.setVisibility(View.VISIBLE);

            String part = fileUri.getPath();
            Bitmap bm = BHBitmap.decodeSampledBitmapFromImagePath(part, 500);
            Bitmap bitmap = BHBitmap.setRotateImageFromImagePath(part, bm);

            imageViewPerson.setImageBitmap(bitmap);
            if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract.toString())) {
                if (imageID != null) {
                    mainContractImageInfo = new ContractImageInfo();
                    mainContractImageInfo.ImageID = imageID;
                    if (data.newContract.RefNo != null) {
                        mainContractImageInfo.RefNo = data.newContract.RefNo;
                    }
                    mainContractImageInfo.ImageName = imageID + ".jpg";
                    mainContractImageInfo.ImageTypeCode = imageTypeCode;
                    mainContractImageInfo.SyncedDate = new Date();

                    data.newContractImageInfo = mainContractImageInfo;
                }
            } else {
                AddContractImage();
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

    public Uri getOutputMediaFileUri(int type, ContractInfo newContract) {
        return Uri.fromFile(getOutputMediaFile(type, newContract));
    }

    private File getOutputMediaFile(int type, ContractInfo newContract) {
        String IMAGE_DIRECTORY_NAME_TEMP = IMAGE_DIRECTORY_NAME;
        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract.toString())) {
            if (newContract != null && newContract.RefNo != null) {
                IMAGE_DIRECTORY_NAME_TEMP = newContract.RefNo;
            }
        } else {
            IMAGE_DIRECTORY_NAME_TEMP = BHPreference.RefNo();
        }
        File mediaStorageDir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME_TEMP + "/" + imageTypeCode);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME_TEMP, "" + IMAGE_DIRECTORY_NAME_TEMP + " directory");
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

    private void AddContractImage() {
        (new BackgroundProcess(activity) {
            ContractImageInfo input = new ContractImageInfo();

            @Override
            protected void before() {
                input.ImageID = imageID;
                input.RefNo = BHPreference.RefNo();
                input.ImageName = imageID + ".jpg";
                input.ImageTypeCode = imageTypeCode;
                input.SyncedDate = new Date();
            }

            @Override
            protected void calling() {
                addContractImage(input, true);
            }
        }).start();
    }






    private boolean isImportThaiIDCard = false;
    static public Personal mPersonal;

    private void setEnabledUIDebtorCustomerForImportThaiIDCard(boolean isEnabled) {
        //spinnerType.setEnabled(isEnabled);//ประเภทบุคคล
        //spinnerTypeCard.setEnabled(isEnabled);//ประเภทบัตร
        editTextIdentificationCard.setEnabled(isEnabled);//บัตรประชาชน
        select_read_card=1;
        //showDialog("okok","okok");
        if (mainDebtorCustomerInfo != null && mainDebtorCustomerInfo.PrefixName.equals("")) {
            spinnerPerfix.setEnabled(true);//คำนำหน้าชื่อ
        } else {
            spinnerPerfix.setEnabled(isEnabled);//คำนำหน้าชื่อ
        }

        editTextName.setEnabled(isEnabled);//ชื่อ-สกุล
        spinnerYear.setEnabled(isEnabled);//ปีเกิด
        spinnerMonth.setEnabled(isEnabled);//เดือนเกิด
        spinnerDate.setEnabled(isEnabled);//วันเกิด

        if (isImportThaiIDCard
                && mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType == PersonTypeInfo.PersonTypeEnum.PERSON
                && mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD) {
            if (mainDebtorCustomerInfo != null ) {
                spinnerSex.setEnabled(false);//เพศ
            }
        }else {
            String perfixName = spinnerPerfix.getSelectedItem().toString();

            switch (perfixName) {
                case "":
                case "นาย":

                    Log.e("asasa","4444");
                    break;
                case "MR.":
                case "นางสาว":
                case "น.ส.":
                case "นาง":
                case "Miss":
                case "Mrs.":
                case "Ms.":
                    spinnerSex.setEnabled(false);
                    break;
                default:
                    Log.e("asasa","5555");
                    spinnerSex.setEnabled(true);
                    break;
            }
        }


        imageViewPerson.setEnabled(isEnabled);//ถ่ายรูป
    }

    private void setEnabledUIAddressForImportThaiIDCard(boolean isEnabled) {
        editTextAddressnumber.setEnabled(isEnabled);//บ้านเลขที่
        editTextCategory.setEnabled(isEnabled);//หมู่ที่
        editTextAlley.setEnabled(isEnabled);//ซอย/ตรอก
        editTextRoad.setEnabled(isEnabled);//ถนน

        autoCompleteTextViewProvince.setEnabled(isEnabled);//จังหวัด
        spinnerDistrict.setEnabled(isEnabled);//อำเภอ/เขต
        spinnerParish.setEnabled(isEnabled);//ตำบล/แขวง
    }

    private void setUIForImportThaiIDCard() {

        if (isImportThaiIDCard
                && mPersonTypeList.get(spinnerType.getSelectedItemPosition()).PersonType == PersonTypeInfo.PersonTypeEnum.PERSON
                && mPersonTypeCardList.get(spinnerTypeCard.getSelectedItemPosition()).PersonTypeCard == PersonTypeCardInfo.PersonTypeCardEnum.IDCARD) {
            setDebtorCustomerForImportThaiIDCard();
            setAddressForImportThaiIDCard();
            setImageForImportThaiIDCard();
        } else if (isImportThaiIDCard) {
            setEnabledUIDebtorCustomerForImportThaiIDCard(true);
            setEnabledUIAddressForImportThaiIDCard(true);
        }
    }

    private void setDebtorCustomerForImportThaiIDCard() {
        if (mPersonal != null) {
            //ตรวจสอบบัตรประชชน
            stringStatusCheckID = true;

            if (mainDebtorCustomerInfo == null) {
                mainDebtorCustomerInfo = new DebtorCustomerInfo();
            }

            mainDebtorCustomerInfo.CustomerType = "0";//บุคคลธรรมดา
            mainDebtorCustomerInfo.IDCardType = "IDCARD";//บัตรประชาชน
            mainDebtorCustomerInfo.IDCard = mPersonal.getIDCard();

            PrefixInfo prefixInfo = TSRController.getPrefixeByPrefixName(mPersonal.getTHPrefix());
            if (prefixInfo != null) {
                mainDebtorCustomerInfo.PrefixName = mPersonal.getTHPrefix();
            } else {
                mainDebtorCustomerInfo.PrefixName = "";
            }

            mainDebtorCustomerInfo.CustomerName = mPersonal.getTHFullname();
            mainDebtorCustomerInfo.Sex = mPersonal.getGender();
            mainDebtorCustomerInfo.Brithday = mPersonal.getDateOfBirth();

            bindDebtorCustomer();
        }
    }

    private void setAddressForImportThaiIDCard() {
        if (mPersonal != null) {
            if (mainAddressInfo == null) {
                mainAddressInfo = new AddressInfo();
            }

            String houseNo = mPersonal.getAddresHouseNo();//บ้านเลขที่
            if (houseNo != null) {
                mainAddressInfo.AddressDetail = houseNo;
            } else {
                mainAddressInfo.AddressDetail = "-";
            }

            String villageNo = mPersonal.getAddresVillageNo();//หมู่ที่
            if (villageNo != null) {
                mainAddressInfo.AddressDetail2 =  villageNo.replaceFirst("หมู่ที่", "").trim();
            } else {
                mainAddressInfo.AddressDetail2 = "-";
            }

            //ซอย/ตรอก
            String soi = mPersonal.getAddresSoi();//ซอย
            if (soi != null) {
                mainAddressInfo.AddressDetail3 =  soi.replaceFirst("ซอย", "").trim();
            } else {

                String lane = mPersonal.getAddresLane();//ตรอก
                if (lane != null) {
                    mainAddressInfo.AddressDetail3 =  soi.replaceFirst("ตรอก", "").trim();
                } else {
                    mainAddressInfo.AddressDetail3 = "-";
                }
            }

            String road = mPersonal.getAAddresRoad();//ถนน
            if (road != null) {
                mainAddressInfo.AddressDetail4 =  road.replaceFirst("ถนน", "").trim();
            } else {
                mainAddressInfo.AddressDetail4 = "-";
            }

            String province = mPersonal.getAddresProvince();//จังหวัด
            if (province != null) {
                ProvinceInfo provinceInfo = TSRController.getProvinceByProvinceName(province.replaceFirst("จังหวัด", "").trim());
                if (provinceInfo != null) {
                    mainAddressInfo.ProvinceCode = provinceInfo.ProvinceCode;

                    String amphur = mPersonal.getAddresAmphur();//อำเภอ/เขต
                    if (amphur != null) {
                        DistrictInfo districtInfo = TSRController.getDistrictByProvinceCodeAndDistrictName(provinceInfo.ProvinceCode, amphur.replaceFirst("อำเภอ", "").trim().replaceFirst("เขต", "").trim());
                        if (districtInfo != null) {
                            mainAddressInfo.DistrictCode = districtInfo.DistrictCode;


                            String tambol = mPersonal.getAddresTambol();//ตำบล/แขวง
                            if (tambol != null) {
                                SubDistrictInfo subDistrictInfo = TSRController.getSubDistrictByDistrictCodeAndSubDistrictName(districtInfo.DistrictCode, tambol.replaceFirst("ตำบล", "").trim().replaceFirst("แขวง", "").trim());
                                if (subDistrictInfo != null) {
                                    mainAddressInfo.SubDistrictCode = subDistrictInfo.SubDistrictCode;
                                }
                            }

                        }
                    }

                }
            }

            bindAddress();
        }
    }

    private void setImageForImportThaiIDCard() {
        if (mPersonal != null) {
            Bitmap photoBitmap = mPersonal.PhotoBitmap();
            if (photoBitmap != null) {

                BHStorage.FolderType F = BHStorage.FolderType.Picture;
                Parth = BHStorage.getFolder(F);
                if (mainContractImageInfo == null && imageID == null) {
                    imageID = DatabaseHelper.getUUID();
                } else {
                    if (mainContractImageInfo != null && mainContractImageInfo.ImageID != null) {
                        imageID = mainContractImageInfo.ImageID;
                    } else {
                        imageID = DatabaseHelper.getUUID();
                    }
                }


                if (mainContractImageInfo == null) {
                    mainContractImageInfo = new ContractImageInfo();

                    if (imageID != null) {
                        mainContractImageInfo.ImageID = imageID;
                        mainContractImageInfo.RefNo = BHPreference.RefNo();
                        mainContractImageInfo.ImageName = imageID + ".jpg";
                        mainContractImageInfo.ImageTypeCode = imageTypeCode;
                        mainContractImageInfo.SyncedDate = new Date();
                    }
                }

                File file = getOutputMediaFile(MEDIA_TYPE_IMAGE, (data != null && data.newContract != null) ? data.newContract : null);
                if (file != null) {
                    FileOutputStream fout;
                    try {
                        fout = new FileOutputStream(file);
                        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                        fout.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //previewCapturedImage();
                AddContractImage();
                bindContractImage();

            }
        }
    }

    private void importThaiIDCard() {

        new BHThaiIDCard( activity, new ResultBHThaiIDCard() {
            @Override
            public void onSuccess(BHThaiIDCard bhThaiIDCard, Personal personal) {
                super.onSuccess( bhThaiIDCard, personal );
                //เมื่อมีการใช้ข้อมูลในบัตรปิด UI ไม่ให้แก้ไข
                isImportThaiIDCard = true;

                mPersonal = personal;
                setDebtorCustomerForImportThaiIDCard();
                setAddressForImportThaiIDCard();
                setImageForImportThaiIDCard();
            }

            @Override
            public void onNotSuccess(BHThaiIDCard bhThaiIDCard, BHThaiIDCard.ResultNotSuccess result) {
                super.onNotSuccess( bhThaiIDCard, result );

                switch (result) {
                    case ConnectNoDevice:
                        showWarningDialog( "แจ้งเตือน", "กรุณาตรวจสอบการเชื่อมต่อเครื่องอ่านบัตร" );
                        break;
                    case PermissionDenied:
                        showWarningDialog( "แจ้งเตือน", String.format( "กรุณาอนุญาติให้ %s เข้าถึงปอุกรณ์ USB ก่อนใช้งาน", getResources().getString(R.string.tsr_app_name)));
                        break;
                    case NoCard:
                        showWarningDialog( "แจ้งเตือน", "กรุณาตรวจสอบการเชื่อมต่อบัตรประชาชน" );
                        break;
                    case ErrorPowerOn:
                    case ErrorNewAPDUThailandIdCardType:
                    case ErrorDataPersonal:
                        showWarningDialog( "เกิดข้อผิดพลาดในการรเชื่อมต่อ", "กรุณาถอดเครื่องอ่านบัตร แล้วทำการเชื่อมต่อใหม่อีกครั้ง" );
                        break;
                }
            }
        } );
    }
}
