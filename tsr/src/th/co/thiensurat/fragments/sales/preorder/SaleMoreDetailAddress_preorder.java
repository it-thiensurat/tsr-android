package th.co.thiensurat.fragments.sales.preorder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.SurveyActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.CareerInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.HabitatTypeInfo;
import th.co.thiensurat.data.info.HobbyInfo;
import th.co.thiensurat.data.info.SuggestionInfo;
import th.co.thiensurat.fragments.sales.EditContractsMainFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.sales.preorder.models.Get_data_api1;
import th.co.thiensurat.fragments.sales.preorder.models.Get_data_api2;
import th.co.thiensurat.fragments.sales.preorder.models.Get_data_api3;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;

public class SaleMoreDetailAddress_preorder extends BHFragment {

    @InjectView
    EditText editTextHabitat, editTextCareer, editTextHobby, editTextSuggestion, editTextReferencePersonName, editTextReferencePersonTelephone;
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
    Spinner spinnerHabitat, spinnerStatusLive, spinnerCareer, spinnerHobby, spinnerSuggestion;

    @InjectView
    Button btnSurvery,bnt_save;

    @InjectView
    RadioGroup radio_G1,radio_G2,radio_G3;

    @InjectView
    Spinner sp1,sp2,sp3;

    @InjectView
    EditText et1,et2,et3,et4,et5;

    @InjectView
    ImageView bb_datetime;


    String radio_G1_S="",radio_G2_S="",radio_G3_S="";
    String DD1="",DD2="",DD3="",ET1="",ET2="",ET3="",ET4="",ET5="",Amount="";
    static String MODE="";

    DatePickerDialog datePicker ;


    private List<HabitatTypeInfo> HabitatTypeList;
    private List<CareerInfo> CareerList;
    private List<HobbyInfo> HobbyList;
    private List<SuggestionInfo> SuggestionList;
    private DebtorCustomerInfo debcustomer;
    private ContractInfo contract;

    private String HabitatCode, HabitatDetail;
    private String CareerCode, CareerDetail;
    private String HobbyCode, HobbyDetail;
    private String SuggestionCode, SuggestionDetail;

    public static String FRAGMENT_SALE_MOREDETAIL = "sale_more_detail_fragment";




    List<Get_data_api1> getDataApi1s;
    List<Get_data_api2> getDataApi2s;
    List<Get_data_api3> getDataApi3s;


    JsonArrayRequest jsonArrayRequest;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue requestQueue;

    String check_save="";


    int length_phone;
    //int length = et2.getText().length();
    String cHECK_number_0 ="";

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.caption_customer_detail_more;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_more_detail_address_preorder;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        //return new int[]{R.string.button_back, R.string.button_end};
        return new int[]{R.string.button_end};

    }


    @Override
    public void onResume() {
        super.onResume();
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                if (!BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
                    TSRController.updateStatusCode(BHPreference.RefNo(), "12");
                }
                else {
                    TSRController.updateStatusCode(BHPreference.RefNo(), "12");

                }
            }
        }.start();
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            linearLayoutHeadNumber.setVisibility(View.GONE);
        }

        txtNumber1.setText("...");
        txtNumber2.setText("8");
        txtNumber3.setText("9");
        txtNumber4.setText("10");
        txtNumber5.setText("11");
        txtNumber5.setBackgroundResource(R.drawable.circle_number_sale_color_red);
        MODE=  BHGeneral.SERVICE_MODE.toString();


        Log.e("vvvv","final");
        BindingSpinner();
        GetContractData();

        btnSurvery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyPage();
            }
        });




        getDataApi1s=new ArrayList<>();
        getDataApi2s=new ArrayList<>();
        getDataApi3s=new ArrayList<>();
        get_balance();
        get_api_1();
        get_api_2();
        get_api_3();
        get_et();
        more_new();

        bb_datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date2_s = "";
                        String month_s ="";
                        String dayOfMonth_s ="";
                        if(dayOfMonth<9){
                            dayOfMonth_s="0"+String.valueOf(dayOfMonth);
                        }
                        else {
                            dayOfMonth_s=String.valueOf(dayOfMonth);
                        }
                        if(month<9){
                            month_s="0"+String.valueOf(month+1);
                        }
                        else {
                            month_s=String.valueOf(month+1);
                        }
                        date2_s= dayOfMonth_s+"-"+month_s+"-"+String.valueOf(year);
                        date3_s=String.valueOf(year)+"-"+month_s+"-"+dayOfMonth_s;
                        date3_s2=dayOfMonth_s+"-"+month_s+"-"+String.valueOf(year+543);

                        //et4.setText(date3_s);
//Log.e("date3_s",date3_s);



                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String   dateBeforeString= df.format(Calendar.getInstance().getTime());
                        String dateAfterString =date3_s;




                        //Parsing the date
                        LocalDate dateBefore = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            try {
                                dateBefore = LocalDate.parse(dateBeforeString);
                            }
                            catch (Exception ex){

                            }

                        }
                        LocalDate dateAfter = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            try {
                                dateAfter = LocalDate.parse(dateAfterString);
                            }
                            catch (Exception ex){

                            }

                        }

                        //calculating number of days in between

                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                //  noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                            }
                            // Log.e("noOfDaysBetween", String.valueOf(noOfDaysBetween));
                            getCountOfDays(dateBeforeString,dateAfterString);
                        }
                        catch (Exception rr){

                        }


                    }


                }, yy, mm, dd);
                datePicker.show();

            }
        });

        bnt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_et();




                if((radio_G1_S.equals("null"))|radio_G1_S.isEmpty()){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้เลือก ผู้รับเงินงวด 1 \nที่เหลือ!")
                            .show();
                }
                else if((radio_G2_S.equals("null"))|radio_G2_S.isEmpty()){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้เลือก ผู้ทำสัญญา!")
                            .show();
                }
                else if(DD1.equals("กรุณาเลือก")){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ เลือกข้อมูลน้ำ!")
                            .show();
                }
                else if(DD2.equals("กรุณาเลือก")){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ เลือกปัญหาน้ำ!")
                            .show();
                }
                else if((DD2.equals("อื่นๆ (ระบุ)"))&((ET5.equals("null"))|(ET5.isEmpty()))){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ ระบุอื่นๆ!")
                            .show();
                }


                else if((radio_G3_S.equals("null"))|radio_G3_S.isEmpty()){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้เลือก ผู้จัดส่งสินค้า!")
                            .show();
                }
                else if((radio_G3_S.equals("ทีมขายจัดส่งสินค้าเอง"))&((ET4.equals("null"))|(ET4.isEmpty()))){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ ระบุวันที่!")
                            .show();
                }
                else if((DD3.equals("กรุณาเลือก"))&(radio_G3_S.equals("คลังจัดส่งสินค้า"))){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ เลือกที่จัดส่ง!")
                            .show();
                }

                else if((ET2.equals("null"))|ET2.isEmpty()){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ ใส่เบอร์ลูกค้า!")
                            .show();
                }
                else if(!cHECK_number_0.equals("0")){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            //    .setTitleText("เบอร์ลูกค้า!")
                            .setContentText("เบอร์โทรต้องขึ้นต้นด้วย 0!")
                            .show();
                }
                else if(length_phone!=12){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        //    .setTitleText("เบอร์ลูกค้า!")
                            .setContentText("เบอร์โทรไม่ครบ 10 หลัก!")
                            .show();
                }
                else  if((ET3.equals("null"))|ET3.isEmpty()){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ กรอบข้อมูลที่ควรทราบอื่นๆ!")
                            .show();
                }

                else {

                    //   get_et();
                    insert_api(BHPreference.RefNo(),BHPreference.employeeID(),radio_G1_S,ET1,radio_G2_S,DD1,DD2,radio_G3_S,ET4,DD3,ET2,ET3,ET5);
                    BHApplication.getInstance().getPrefManager().setPreferrence("chaeck_save", "1");

                }

            }
        });







        et2.setFilters(new InputFilter[]{MobilePhone});





    }
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

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_end:


             //   UpDateDebCustomer();



                try {
                    String DD= BHApplication.getInstance().getPrefManager().getPreferrence("chaeck_save");
                    if(DD.equals("1")){
                        UpDateDebCustomer();
                        BHApplication.getInstance().getPrefManager().setPreferrence("chaeck_save", "0");

                    }
                    else {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("ผิดพลาด!")
                                .setContentText("ยังไม่ได้ บันทึกข้อมูล!")
                                .show();
                    }

                }
                catch (Exception ex){
                   // UpDateDebCustomer();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ผิดพลาด!")
                            .setContentText("ยังไม่ได้ บันทึกข้อมูล!")
                            .show();
                }


                break;
/*            case R.string.button_back:
                showLastView();
                break;*/
            default:
                break;
        }
    }

    float dayCount;
    String date3_s="",date3_s2="";
    public String getCountOfDays(String createdDateString, String expireDateString) {



        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }



        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        dayCount = 0-((float) diff / (24 * 60 * 60 * 1000));
        Log.e("dayCount", String.valueOf((int) dayCount));







        if((int) dayCount>0){

            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("ขออภัย!")
                    .setContentText("ไม่สามารถเลือกย้อนหลังได้!")
                    .show();












        }



        else {

            et4.setText(date3_s2);


        }



        return ("" + (int) dayCount + " Days");
    }


    private void BindingSpinner() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            List<HabitatTypeInfo> HabitatTypeoutput = new ArrayList<HabitatTypeInfo>();
            List<CareerInfo> Careeroutput = new ArrayList<CareerInfo>();
            List<HobbyInfo> Hobbyoutput = new ArrayList<HobbyInfo>();
            List<SuggestionInfo> Suggestionoutput = new ArrayList<SuggestionInfo>();

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                HabitatTypeoutput = getHabitatType();
                Careeroutput = getCareer();
                Hobbyoutput = getHobby();
                Suggestionoutput = getSuggestion();
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub

                /** (1) Habitat-Type : ประเภทที่อยู่อาศัย **/
                if (HabitatTypeoutput != null) {
                    HabitatTypeList = HabitatTypeoutput;
                    List<String> HabitatType = new ArrayList<String>();
                    HabitatType.add(String.format(" "));
                    for (HabitatTypeInfo item : HabitatTypeList) {
                        HabitatType.add(String.format(item.HabitatTypeName));

                    }

                    BHSpinnerAdapter<String> arrayHabitatType = new BHSpinnerAdapter<String>(activity, HabitatType);
                    spinnerHabitat.setAdapter(arrayHabitatType);
                    spinnerHabitat.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                                HabitatCode = HabitatTypeList.get(position - 1).HabitatTypeCode;
                                HabitatDetail = HabitatTypeList.get(position - 1).HabitatTypeName;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                }

                /** (2) Career : อาชีพ **/
                if (Careeroutput != null) {
                    CareerList = Careeroutput;
                    List<String> Career = new ArrayList<String>();
                    Career.add(String.format("  "));
                    for (CareerInfo item : CareerList) {
                        Career.add(String.format(item.CareerName));
                    }
                    BHSpinnerAdapter<String> arrayCareer = new BHSpinnerAdapter<String>(activity, Career);
                    spinnerCareer.setAdapter(arrayCareer);
                    spinnerCareer.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                                CareerCode = CareerList.get(position - 1).CareerCode;
                                CareerDetail = CareerList.get(position - 1).CareerName;

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                }

                /** (3) Hobby : งานอดิเรก **/
                if (Hobbyoutput != null) {
                    HobbyList = Hobbyoutput;
                    List<String> Hobby = new ArrayList<String>();
                    Hobby.add(String.format(" "));
                    for (HobbyInfo item : HobbyList) {
                        Hobby.add(String.format(item.HobbyName));
                    }

                    BHSpinnerAdapter<String> arraryHobby = new BHSpinnerAdapter<String>(activity, Hobby);
                    spinnerHobby.setAdapter(arraryHobby);
                    spinnerHobby.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                                HobbyCode = HobbyList.get(position - 1).HobbyCode;
                                HobbyDetail = HobbyList.get(position - 1).HobbyName;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });
                }

                /** (4) Suggestion : ข้อเสนอแนะ **/
                if (Suggestionoutput != null) {
                    SuggestionList = Suggestionoutput;
                    List<String> Suggestion = new ArrayList<String>();
                    Suggestion.add(String.format(" "));
                    for (SuggestionInfo item : SuggestionList) {
                        Suggestion.add(String.format(item.SuggestionName));
                    }

                    BHSpinnerAdapter<String> arraySuggestion = new BHSpinnerAdapter<String>(activity, Suggestion);
                    spinnerSuggestion.setAdapter(arraySuggestion);
                    spinnerSuggestion.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                                SuggestionCode = SuggestionList.get(position - 1).SuggestionCode;
                                SuggestionDetail = SuggestionList.get(position - 1).SuggestionName;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });
                }

            }
        }).start();

        /** (5) สถานะผู้อาศัย (Array-Value: เจ้าของ / ผู้อยู่อาศัย / ผู้เช่า) **/
        spinnerStatusLive.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                ((TextView) parent.getChildAt(0)).setTextSize(20);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void GetContractData() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                contract = getContract(BHPreference.RefNo());
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (contract != null) {
                    BindingDebCustomer();
                    checkHasSurvey();
                }
            }
        }).start();
    }

    private void BindingDebCustomer() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                debcustomer = getDebCustometByID(contract.CustomerID);
            }

            @Override
            protected void after() {

                if (debcustomer != null) {

                    /** Get specific data for selecting spinner **/
                    (new BackgroundProcess(activity) {
                        HabitatTypeInfo habitatTypeInfo = new HabitatTypeInfo();
                        CareerInfo careerInfo = new CareerInfo();
                        HobbyInfo hobbyInfo = new HobbyInfo();
                        SuggestionInfo suggestionInfo = new SuggestionInfo();

                        @Override
                        protected void calling() {
                            // TODO Auto-generated method stub
                            habitatTypeInfo = getHabitatTypeByHabitatTypeCode(isnull(debcustomer.HabitatTypeCode, ""));
                            careerInfo = getCareerByCareerCode(isnull(debcustomer.CareerCode, ""));
                            hobbyInfo = getHobbyByHobbyCode(isnull(debcustomer.HobbyCode, ""));
                            suggestionInfo = getSuggestionBySuggestionCode(isnull(debcustomer.SuggestionCode, ""));
                        }

                        @Override
                        protected void after() {
                            /** บุคคลอ้างอิง **/
                            //ชื่อ-นามสกุล
                            if (debcustomer.ReferencePersonName != null) {
                                editTextReferencePersonName.setText(debcustomer.ReferencePersonName);
                            }

                            //เบอร์โทรติดต่อ
                            if (debcustomer.ReferencePersonTelephone != null) {
                                editTextReferencePersonTelephone.setText(debcustomer.ReferencePersonTelephone);
                            }

                            /** (1) Habitat-Type : ประเภทที่อยู่อาศัย **/
                            ArrayAdapter spinnerHabitatAdapter = (ArrayAdapter) spinnerHabitat.getAdapter();
                            if (debcustomer.HabitatTypeCode != null) {
                                if (habitatTypeInfo != null) {
                                    int spinnerHabitatPosition = spinnerHabitatAdapter.getPosition(habitatTypeInfo.HabitatTypeName);
                                    spinnerHabitat.setSelection(spinnerHabitatPosition);
                                }
                            }
                            //อื่นๆ
                            if (debcustomer.HabitatDetail != null) {
                                editTextHabitat.setText(debcustomer.HabitatDetail);
                            }

                            /** (2) Career : อาชีพ **/
                            ArrayAdapter spinnerCareerAdapter = (ArrayAdapter) spinnerCareer.getAdapter();
                            if (debcustomer.CareerCode != null) {
                                if (careerInfo != null) {
                                    int spinnerCareerPosition = spinnerCareerAdapter.getPosition(careerInfo.CareerName);
                                    spinnerCareer.setSelection(spinnerCareerPosition);
                                }
                            }
                            //อื่นๆ
                            if (debcustomer.CareerDetail != null) {
                                editTextCareer.setText(debcustomer.CareerDetail);
                            }

                            /** (3) Hobby : งานอดิเรก **/
                            ArrayAdapter spinnerHobbyAdapter = (ArrayAdapter) spinnerHobby.getAdapter();
                            if (debcustomer.HobbyCode != null) {
                                if (hobbyInfo != null) {
                                    int spinnerHobbyPosition = spinnerHobbyAdapter.getPosition(hobbyInfo.HobbyName);
                                    spinnerHobby.setSelection(spinnerHobbyPosition);
                                }
                            }
                            //อื่นๆ
                            if (debcustomer.HobbyDetail != null) {
                                editTextHobby.setText(debcustomer.HobbyDetail);
                            }

                            /** (4) Suggestion : ข้อเสนอแนะ **/
                            ArrayAdapter spinnerSuggestionAdapter = (ArrayAdapter) spinnerSuggestion.getAdapter();
                            if (debcustomer.SuggestionCode != null) {
                                if (suggestionInfo != null) {
                                    int spinnerSuggestionPosition = spinnerSuggestionAdapter.getPosition(suggestionInfo.SuggestionName);
                                    spinnerSuggestion.setSelection(spinnerSuggestionPosition);
                                }
                            }
                            //อื่นๆ
                            if (debcustomer.SuggestionDetail != null) {
                                editTextSuggestion.setText(debcustomer.SuggestionDetail);
                            }

                            /** (5) สถานะผู้อาศัย (Array-Value: เจ้าของ / ผู้อยู่อาศัย / ผู้เช่า) **/
                            ArrayAdapter spinnerStatusLiveAdapter = (ArrayAdapter) spinnerStatusLive.getAdapter();
                            if (debcustomer.OccupyType != null) {
                                int spinnerStatusLivePosition = spinnerStatusLiveAdapter.getPosition(debcustomer.OccupyType);
                                spinnerStatusLive.setSelection(spinnerStatusLivePosition);
                            }

                        }    // after for selecting spinner
                    }).start();
                }    // if (debcustomer != null)
            }    // after for get contract data
        }).start();
    }

    public String isnull(String str, String strReturn) {

        if (str != null) {
            return str;
        }

        return strReturn;

    }

    private void UpDateDebCustomer() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            DebtorCustomerInfo debcustomerReferencePerson = new DebtorCustomerInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub
                //debcustomer.CustomerID = BHPreference.CustomerID();
                /*debcustomer.HabitatTypeCode = HabitatCode;
				debcustomer.HabitatDetail = HabitatDetail;
				debcustomer.OccupyType = spinnerStatusLive.getSelectedItem().toString();
				debcustomer.CareerCode = CareerCode;
				debcustomer.CareerDetail = CareerDetail;
				debcustomer.HobbyCode = HobbyCode;
				debcustomer.HobbyDetail = HobbyDetail;
				debcustomer.IsUsedProduct = "0";
				debcustomer.UsedProductModelID = "";
				debcustomer.SuggestionCode = SuggestionCode;
				debcustomer.SuggestionDetail = SuggestionDetail;*/

//                debcustomer.HabitatTypeCode = HabitatCode;
//                debcustomer.HabitatDetail = editTextHabitat.getText().toString();
//                debcustomer.OccupyType = spinnerStatusLive.getSelectedItem().toString();
//                debcustomer.CareerCode = CareerCode;
//                debcustomer.CareerDetail = editTextCareer.getText().toString();
//                debcustomer.HobbyCode = HobbyCode;
//                debcustomer.HobbyDetail = editTextHobby.getText().toString();
//                debcustomer.IsUsedProduct = false;

                /**
                 * Edit by Teerayut Klinsanga
                 */
                debcustomer.HabitatTypeCode = "";
                debcustomer.HabitatDetail   = "";
                debcustomer.OccupyType      = "";
                debcustomer.CareerCode      = "";
                debcustomer.CareerDetail    = "";
                debcustomer.HobbyCode       = "";
                debcustomer.HobbyDetail     = "";
                debcustomer.IsUsedProduct   = false;
                /**
                 * End
                 */

              //  debcustomer.UsedProductModelID = "";



                try {
                    if (debcustomer.UsedProductModelID.equals("VIP")) {

                        // checkBoxvip.se(false);
                        debcustomer.UsedProductModelID = "VIP";// ชื่อกรรมการผู้มีอำนาจ
                    } else {
                        debcustomer.UsedProductModelID = "";// ชื่อกรรมการผู้มีอำนาจ

                    }
                }
                catch (Exception ex){

                }




        /*        if(status.equals("OK")){
                    //cust.OrganizationCode = "3";// ชื่อกรรมการผู้มีอำนาจ

                    if(check_box_status==1){
                        debcustomer.UsedProductModelID = "VIP";// ชื่อกรรมการผู้มีอำนาจ

                    }
                    else {
                        debcustomer.UsedProductModelID = "";// ชื่อกรรมการผู้มีอำนาจ

                    }


                }
                else {
                    debcustomer.UsedProductModelID = "";// ชื่อกรรมการผู้มีอำนาจ

                    // cust.OrganizationCode = BHPreference.organizationCode();

                }*/



                debcustomer.SuggestionCode = SuggestionCode;
                debcustomer.SuggestionDetail = editTextSuggestion.getText().toString();

                /*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
                debcustomerReferencePerson.CustomerID = debcustomer.CustomerID;
                debcustomerReferencePerson.OrganizationCode = debcustomer.OrganizationCode;
                debcustomerReferencePerson.LastUpdateBy = BHPreference.employeeID();
                debcustomerReferencePerson.LastUpdateDate = new Date();
                debcustomerReferencePerson.ReferencePersonName = editTextReferencePersonName.getText().toString();
                debcustomerReferencePerson.ReferencePersonTelephone = editTextReferencePersonTelephone.getText().toString();
                /*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                // saveDebtorCustomer(debcustomer);
                saveCustomerData(debcustomer, true);
                updateReferencePerson(debcustomerReferencePerson, true);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
                    activity.showView(new EditContractsMainFragment());
                } else {
                    saveStatusCode();
                }
            }

        }).start();
    }

    private void saveStatusCode() {
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                TSRController.updateStatusCode(BHPreference.RefNo(), "13");
            }

            @Override
            protected void after() {
                showLastView(FRAGMENT_SALE_MOREDETAIL);
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333) {
            checkHasSurvey();
        }
    }

    /**
     * Edit by Teerayut Klinsanga
     * 05/10/2020
     */

    private void SurveyPage() {
        Intent intent = new Intent(getContext(), SurveyActivity.class);
        intent.putExtra("REFERRENCE_NUMBER", contract.RefNo);
        intent.putExtra("CONTRACT_NUMBER", contract.CONTNO);
        intent.putExtra("EMPLOYEE_NUMBER", contract.SaleEmployeeCode);
        startActivityForResult(intent, 333);
    }

    private void checkHasSurvey() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.checkQuestion(contract.CONTNO);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Log.e("Survey contno", contract.CONTNO);
                    Gson gson=new Gson();
                    try {
                        JSONObject jsonObject=new JSONObject(gson.toJson(response.body()));
                        Log.e("json question: ",jsonObject.toString());
                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONObject obj = null;
                        for (int i = 0; i < array.length(); i++) {
                            obj = array.getJSONObject(i);
                            String status = obj.getString("Status");
                            Log.e("Question status", status);
                            if ("Error".equals(status)) {
                                btnSurvery.setVisibility(View.VISIBLE);
                            } else {
                                btnSurvery.setVisibility(View.GONE);
                            }
                        }
//                        layoutSurvey.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data","22");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("onFailure question:",t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            Log.e("Exception question",e.getLocalizedMessage());
        }
    }

    /**
     * End
     */














    void  get_et(){
        ET1=et1.getText().toString();
        ET2=et2.getText().toString();
        ET3=et3.getText().toString();
        ET4=et4.getText().toString();
        ET5=et5.getText().toString();

        length_phone = et2.getText().length();
        try {
            cHECK_number_0 = ET2.substring(0, 1);

        }
        catch (Exception ex){

        }
    }

    void  more_new(){
        radio_G1.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener ( )
                                              {
                                                  public void onCheckedChanged ( RadioGroup group, int checkedId )
                                                  {
                                                      RadioButton checkedRadio = ( RadioButton ) getActivity().findViewById ( checkedId );
                                                      radio_G1_S  = checkedRadio.getText ().toString ();
                                                  }
                                              }
        );

        radio_G2.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener ( )
                                              {
                                                  public void onCheckedChanged ( RadioGroup group, int checkedId )
                                                  {
                                                      RadioButton checkedRadio = ( RadioButton ) getActivity().findViewById ( checkedId );
                                                      radio_G2_S  = checkedRadio.getText ().toString ();
                                                  }
                                              }
        );
        radio_G3.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener ( )
                                              {
                                                  public void onCheckedChanged ( RadioGroup group, int checkedId )
                                                  {
                                                      RadioButton checkedRadio = ( RadioButton ) getActivity().findViewById ( checkedId );
                                                      radio_G3_S  = checkedRadio.getText ().toString ();
                                                  }
                                              }
        );


    }



    public  void get_balance() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call=null;
            if(MODE.equals("UAT")){
                call = request.checkBookingFirstPeriodAmount_UAT(BHPreference.RefNo());

            }
            else {
                call = request.checkBookingFirstPeriodAmount(BHPreference.RefNo());

            }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));

                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data_get_balance(jsonObject.getJSONArray("data"));

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                }
            });

        } catch (Exception e) {

        }
    }

    public  void get_api_1() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call=null;
            // if(MODE.equals("UAT")){
            call = request.get_api_1();

            //  }
            // else {
            //     call = request.get_ProductSerialNumber_by_preorder_setting();

            //  }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));

                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data_get_api_1(jsonObject.getJSONArray("data"));

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                }
            });

        } catch (Exception e) {

        }
    }

    public  void get_api_2() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call=null;
            // if(MODE.equals("UAT")){
            call = request.get_api_2();

            //  }
            // else {
            //     call = request.get_ProductSerialNumber_by_preorder_setting();

            //  }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));

                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data_get_api_2(jsonObject.getJSONArray("data"));

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                }
            });

        } catch (Exception e) {

        }
    }

    public  void get_api_3() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call=null;
            // if(MODE.equals("UAT")){
            call = request.get_api_3();

            //  }
            // else {
            //     call = request.get_ProductSerialNumber_by_preorder_setting();

            //  }

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));

                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data_get_api_3(jsonObject.getJSONArray("data"));

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                }
            });

        } catch (Exception e) {

        }
    }
    public  void JSON_PARSE_DATA_AFTER_WEBCALL_load_data_get_balance(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {

            final Get_data_api1 GetDataAdapter2 = new Get_data_api1();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Amount=json.getString("Amount");




            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

        et1.setText(Amount);



    }


    public  void JSON_PARSE_DATA_AFTER_WEBCALL_load_data_get_api_1(JSONArray array) {
        getDataApi1s.clear();

        for (int i = 0; i < array.length(); i++) {

            final Get_data_api1 GetDataAdapter2 = new Get_data_api1();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                GetDataAdapter2.setWater(json.getString("water"));




            } catch (JSONException e) {

                e.printStackTrace();
            }
            getDataApi1s.add(GetDataAdapter2);
            // value=GetDataAdapter2.getProblemName();
        }
        String[] array2 = new String[getDataApi1s.size()];
        String[] array3 = new String[getDataApi1s.size()];
        int i;
        ArrayAdapter<String> adapter = null ;

        for ( i = 0; i < getDataApi1s.size(); i++) {
            final Get_data_api1 contact = getDataApi1s.get(i);
            array2[i]= contact.getWater();

            try {
                adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2);
                //adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2);
            }
            catch (Exception ex){

            }

        }



        sp1.setAdapter(adapter);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //item = parent.getItemAtPosition(position).toString();

                //MyApplication.getInstance().getPrefManager().setPreferrence("item", item);

                final Get_data_api1 contact = getDataApi1s.get(position);
                DD1  = contact.getWater();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public  void JSON_PARSE_DATA_AFTER_WEBCALL_load_data_get_api_2(JSONArray array) {
        getDataApi2s.clear();

        for (int i = 0; i < array.length(); i++) {

            final Get_data_api2 GetDataAdapter2 = new Get_data_api2();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                GetDataAdapter2.setProblem(json.getString("problem"));




            } catch (JSONException e) {

                e.printStackTrace();
            }
            getDataApi2s.add(GetDataAdapter2);
            // value=GetDataAdapter2.getProblemName();
        }
        String[] array2 = new String[getDataApi2s.size()];
        String[] array3 = new String[getDataApi2s.size()];
        int i;
        ArrayAdapter<String> adapter = null ;

        for ( i = 0; i < getDataApi2s.size(); i++) {
            final Get_data_api2 contact = getDataApi2s.get(i);
            array2[i]= contact.getProblem();

            try {
                adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2);
                //adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2);
            }
            catch (Exception ex){

            }

        }



        sp2.setAdapter(adapter);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //item = parent.getItemAtPosition(position).toString();

                //MyApplication.getInstance().getPrefManager().setPreferrence("item", item);

                final Get_data_api2 contact = getDataApi2s.get(position);
                DD2  = contact.getProblem();

                if(DD2.equals("อื่นๆ (ระบุ)")){
                    et5.setVisibility(View.VISIBLE);
                }
                else {
                    et5.setVisibility(View.GONE);

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public  void JSON_PARSE_DATA_AFTER_WEBCALL_load_data_get_api_3(JSONArray array) {
        getDataApi3s.clear();

        for (int i = 0; i < array.length(); i++) {

            final Get_data_api3 GetDataAdapter2 = new Get_data_api3();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                GetDataAdapter2.setAddrees(json.getString("addrees"));




            } catch (JSONException e) {

                e.printStackTrace();
            }
            getDataApi3s.add(GetDataAdapter2);
            // value=GetDataAdapter2.getProblemName();
        }
        String[] array2 = new String[getDataApi3s.size()];
        String[] array3 = new String[getDataApi3s.size()];
        int i;
        ArrayAdapter<String> adapter = null ;

        for ( i = 0; i < getDataApi3s.size(); i++) {
            final Get_data_api3 contact = getDataApi3s.get(i);
            array2[i]= contact.getAddrees();

            try {
                adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2);
                //adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2);
            }
            catch (Exception ex){

            }

        }



        sp3.setAdapter(adapter);

        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //item = parent.getItemAtPosition(position).toString();

                //MyApplication.getInstance().getPrefManager().setPreferrence("item", item);

                final Get_data_api3 contact = getDataApi3s.get(position);
                DD3  = contact.getAddrees();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }







    private void insert_api(String Refno,String Empid,String FirstPeriodPayBy,String FirstPeriodPayAmount,String ContractBy,String WaterInfo,String WaterProblem,String ShippingBy,String ShippingDate,String ShippingTo,String TelnoCus,String InstallDetails,String WaterProblemMore){


        Log.e("TelnoCus",TelnoCus);


/*      try {
            Log.e("URL_api","https://tssm.thiensurat.co.th/api/api-addBookingInfrom.php"+"?Refno="+Refno+"&Empid="+Empid+"&FirstPeriodPayBy="+URLEncoder.encode(FirstPeriodPayBy, "UTF-8")+"&FirstPeriodPayAmount="+URLEncoder.encode(FirstPeriodPayAmount, "UTF-8")+
                    "&ContractBy="+URLEncoder.encode(ContractBy, "UTF-8")+"&WaterInfo="+URLEncoder.encode(WaterInfo, "UTF-8")+"&WaterProblem="+URLEncoder.encode(WaterProblem, "UTF-8")+"&ShippingBy="+URLEncoder.encode(ShippingBy, "UTF-8")+"&ShippingDate="+ShippingDate+"&ShippingTo="+URLEncoder.encode(ShippingTo, "UTF-8")+"&TelnoCus="+TelnoCus+"&InstallDetails="+URLEncoder.encode(InstallDetails, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/



        try {
            jsonArrayRequest = new JsonArrayRequest("https://tssm.thiensurat.co.th/api/api-addBookingInfrom.php"+"?Refno="+Refno+"&Empid="+Empid+"&FirstPeriodPayBy="+URLEncoder.encode(FirstPeriodPayBy, "UTF-8")+"&FirstPeriodPayAmount="+URLEncoder.encode(FirstPeriodPayAmount, "UTF-8")+
                    "&ContractBy="+ URLEncoder.encode(ContractBy, "UTF-8")+"&WaterInfo="+URLEncoder.encode(WaterInfo, "UTF-8")+"&WaterProblem="+URLEncoder.encode(WaterProblem, "UTF-8")+"&ShippingBy="+URLEncoder.encode(ShippingBy, "UTF-8")+"&ShippingDate="+ShippingDate+"&ShippingTo="+URLEncoder.encode(ShippingTo, "UTF-8")+"&TelnoCus="+TelnoCus+"&InstallDetails="+URLEncoder.encode(InstallDetails, "UTF-8")+"&WaterProblemMore="+URLEncoder.encode(WaterProblemMore, "UTF-8"),


                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("บันทึกข้อมูล!")
                                    .setContentText("เรียบร้อย!")
                                    .show();

                            // showDialog("บันทึกข้อมูล","เรียบร้อย");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //showDialog("บันทึกข้อมูล","เรียบร้อย");
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("บันทึกข้อมูล!")
                                    .setContentText("เรียบร้อย!")
                                    .show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            try {
                requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(jsonArrayRequest);

                int MY_SOCKET_TIMEOUT_MS=10000;

                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            }
            catch (RuntimeException ex){

            }
        }
        catch (OutOfMemoryError EX){

        }



    }
}
