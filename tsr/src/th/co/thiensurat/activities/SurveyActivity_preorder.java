package th.co.thiensurat.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.fragments.sales.preorder.models.Get_data_api1;
import th.co.thiensurat.fragments.sales.preorder.models.Get_data_api2;
import th.co.thiensurat.fragments.sales.preorder.models.Get_data_api3;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;
import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class SurveyActivity_preorder extends Activity  {





    Button bnt_save;
    RadioGroup radio_G1,radio_G2,radio_G3;
    Spinner sp1,sp2,sp3;
    EditText et1,et2,et3,et4,et5;
    ImageView bb_datetime,bb_datetime2,image_time;
    ImageButton button_back;
    LinearLayout li11,li22;
    TextView txt_s_time,et6;


    String radio_G1_S="",radio_G2_S="",radio_G3_S="";
    String DD1="",DD2="",DD3="",ET1="",ET2="",ET3="",ET4="",ET5="",ET6="",ET6_2="",TT="",Amount="";
    static String MODE="";

    DatePickerDialog datePicker ;




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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_preorder);


        txt_s_time= (TextView) findViewById(R.id.txt_s_time);
        bnt_save  = (Button) findViewById(R.id.bnt_save);
        radio_G1  = (RadioGroup) findViewById(R.id.radio_G1);
        radio_G2  = (RadioGroup) findViewById(R.id.radio_G2);
        radio_G3  = (RadioGroup) findViewById(R.id.radio_G3);

        sp1  = (Spinner) findViewById(R.id.sp1);
        sp2  = (Spinner) findViewById(R.id.sp2);
        sp3  = (Spinner) findViewById(R.id.sp3);

        et1  = (EditText) findViewById(R.id.et1);
        et2  = (EditText) findViewById(R.id.et2);
        et3  = (EditText) findViewById(R.id.et3);
        et4  = (EditText) findViewById(R.id.et4);
        et5  = (EditText) findViewById(R.id.et5);
        et6= (TextView) findViewById(R.id.et6);
        bb_datetime= (ImageView) findViewById(R.id.bb_datetime);
        bb_datetime2= (ImageView) findViewById(R.id.bb_datetime2);
        image_time= (ImageView) findViewById(R.id.image_time);
        button_back= (ImageButton) findViewById(R.id.button_back);
        li11= (LinearLayout) findViewById(R.id.li11);
        li22= (LinearLayout) findViewById(R.id.li22);


        MODE=  BHGeneral.SERVICE_MODE.toString();




        getDataApi1s=new ArrayList<>();
        getDataApi2s=new ArrayList<>();
        getDataApi3s=new ArrayList<>();
        get_balance();
        get_api_1();
        get_api_2();
        get_api_3();
        get_et();
        more_new();
        get_et();

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        bb_datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(SurveyActivity_preorder.this, new DatePickerDialog.OnDateSetListener() {
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



        bb_datetime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(SurveyActivity_preorder.this, new DatePickerDialog.OnDateSetListener() {
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
                        date3_X=String.valueOf(year)+"-"+month_s+"-"+dayOfMonth_s;
                        date3_s2_X=dayOfMonth_s+"-"+month_s+"-"+String.valueOf(year+543);
                    //    ET6_2  =String.valueOf(year)+"-"+dayOfMonth_s+"-"+month_s;
                        ET6_2=dayOfMonth_s+"-"+month_s+"-"+String.valueOf(year+543);

                        //et4.setText(date3_s);
//Log.e("date3_s",date3_s);



                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String   dateBeforeString= df.format(Calendar.getInstance().getTime());
                        String dateAfterString =date3_X;




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
                            getCountOfDays2(dateBeforeString,dateAfterString);
                        }
                        catch (Exception rr){

                        }


                    }


                }, yy, mm, dd);
                datePicker.show();

            }
        });





        image_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SurveyActivity_preorder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String S_selectedHour="";
                        String S_selectedMinute="";

                        if(selectedHour<9){
                            S_selectedHour="0"+String.valueOf(selectedHour);
                        }
                        else {
                            S_selectedHour=String.valueOf(selectedHour);
                        }



                        if(selectedMinute<9){
                            S_selectedMinute="0"+String.valueOf(selectedMinute);
                        }
                        else {
                            S_selectedMinute=String.valueOf(selectedMinute);
                        }



                        TT=S_selectedHour+":"+S_selectedMinute;
                        Log.e("timetime", S_selectedHour+":"+S_selectedMinute+":00:000");
                        txt_s_time.setText(S_selectedHour+":"+S_selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        bnt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_et();
                if((radio_G1_S.equals("null"))|radio_G1_S.isEmpty()){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้เลือก ผู้รับเงินงวด 1 \nที่เหลือ!")
                            .show();
                } else if((radio_G2_S.equals("null"))|radio_G2_S.isEmpty()){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้เลือก ผู้ทำสัญญา!")
                            .show();
                } else if(ET6.equals("null")|ET6.isEmpty()){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ ระบุวันที่ลูกค้าสะดวกนัดติดตั้ง!")
                            .show();
                }
                else if(TT.equals("null")|TT.isEmpty()){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ ระบุเวลาลูกค้าสะดวกนัดติดตั้ง!")
                            .show();
                }
                else if(DD1.equals("กรุณาเลือก")){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ เลือกข้อมูลน้ำ!")
                            .show();
                } else if(DD2.equals("กรุณาเลือก")){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ เลือกปัญหาน้ำ!")
                            .show();
                } else if((DD2.equals("อื่นๆ (ระบุ)"))&((ET5.equals("null"))|(ET5.isEmpty()))){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ ระบุอื่นๆ!")
                            .show();
                } else if((radio_G3_S.equals("null"))|radio_G3_S.isEmpty()){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้เลือก ผู้จัดส่งสินค้า!")
                            .show();
                } else if((radio_G3_S.equals("ทีมขายจัดส่งสินค้าเอง"))&((ET4.equals("null"))|(ET4.isEmpty()))){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ ระบุวันที่!")
                            .show();
                } else if((DD3.equals("กรุณาเลือก"))&(radio_G3_S.equals("คลังจัดส่งสินค้า"))){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ เลือกที่จัดส่ง!")
                            .show();
                } else if((et2.getText().toString().equals("null"))|et2.getText().toString().isEmpty()){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ ใส่เบอร์ลูกค้า!")
                            .show();
                } else if(!cHECK_number_0.equals("0")){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            //    .setTitleText("เบอร์ลูกค้า!")
                            .setContentText("เบอร์โทรต้องขึ้นต้นด้วย 0!")
                            .show();
                } else if(length_phone!=12){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            //    .setTitleText("เบอร์ลูกค้า!")
                            .setContentText("เบอร์โทรไม่ครบ 10 หลัก!")
                            .show();
                } else  if((et3.getText().toString().equals("null"))|et3.getText().toString().isEmpty()){
                    new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ข้อมูลยังไม่ครบ!")
                            .setContentText("ยังไม่ได้ กรอบข้อมูลที่ควรทราบอื่นๆ!")
                            .show();
                } else {
                    insert_api(BHPreference.RefNo(),BHPreference.employeeID(),radio_G1_S,ET1,radio_G2_S,DD1,DD2,radio_G3_S,ET4,DD3,ET2,ET3,ET5,ET6_2);
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


    float dayCount,dayCount2;
    String date3_s2="",date3_s="",date3_s2_X="",date3_X="";
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
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("ขออภัย!")
                    .setContentText("ไม่สามารถเลือกย้อนหลังได้!")
                    .show();
        } else {
            et4.setText(date3_s2);
        }
        return ("" + (int) dayCount + " Days");
    }
    public String getCountOfDays2(String createdDateString, String expireDateString) {



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

        dayCount2 = 0-((float) diff / (24 * 60 * 60 * 1000));
        Log.e("dayCount", String.valueOf((int) dayCount2));

        if((int) dayCount2>0){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("ขออภัย!")
                    .setContentText("ไม่สามารถเลือกย้อนหลังได้!")
                    .show();
        } else {
            et6.setText(date3_s2_X);
        }
        return ("" + (int) dayCount2 + " Days");
    }
    void get_et(){

        String FF = et1.getText().toString();
        if((FF.equals("0"))|(FF.isEmpty())){
            ET1="0";
        }
        else {
            ET1=et1.getText().toString();
        }
        ET2 = et2.getText().toString();
        ET3 = et3.getText().toString();
        ET4 = et4.getText().toString();
        ET5 = et5.getText().toString();
        ET6 = et6.getText().toString();


        length_phone = ET2.length();
        try {
            cHECK_number_0 = ET2.substring(0, 1);
        } catch (Exception ex){

        }
    }

    void  more_new(){
        radio_G1.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener ( )
                                              {
                                                  public void onCheckedChanged ( RadioGroup group, int checkedId )
                                                  {
                                                      RadioButton checkedRadio = ( RadioButton )findViewById ( checkedId );
                                                      radio_G1_S  = checkedRadio.getText ().toString ();
                                                  }
                                              }
        );

        radio_G2.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener ( )
                                              {
                                                  public void onCheckedChanged ( RadioGroup group, int checkedId )
                                                  {
                                                      RadioButton checkedRadio = ( RadioButton ) findViewById ( checkedId );
                                                      radio_G2_S  = checkedRadio.getText ().toString ();
                                                  }
                                              }
        );
        radio_G3.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener ( )
                                              {
                                                  public void onCheckedChanged ( RadioGroup group, int checkedId )
                                                  {
                                                      RadioButton checkedRadio = ( RadioButton ) findViewById ( checkedId );
                                                      radio_G3_S  = checkedRadio.getText ().toString ();

                                                      if(radio_G3_S.equals("ทีมขายจัดส่งสินค้าเอง")){
                                                          li11.setVisibility(View.VISIBLE);
                                                          li22.setVisibility(View.GONE);
                                                      }
                                                      else {
                                                          li11.setVisibility(View.GONE);
                                                          li22.setVisibility(View.VISIBLE);
                                                      }


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
                adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, array2);
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
                adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, array2);
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
                adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, array2);
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







    private void insert_api(String Refno,String Empid,String FirstPeriodPayBy,String FirstPeriodPayAmount,String ContractBy,String WaterInfo,String WaterProblem,String ShippingBy,String ShippingDate,String ShippingTo,String TelnoCus,String InstallDetails,String WaterProblemMore,String DateInstall){


        Log.e("TelnoCus",TelnoCus);


      try {
            Log.e("URL_api","https://tssm.thiensurat.co.th/api/api-addBookingInfrom.php"+"?Refno="+Refno+"&Empid="+Empid+"&FirstPeriodPayBy="+URLEncoder.encode(FirstPeriodPayBy, "UTF-8")+"&FirstPeriodPayAmount="+URLEncoder.encode(FirstPeriodPayAmount, "UTF-8")+
                    "&ContractBy="+URLEncoder.encode(ContractBy, "UTF-8")+"&WaterInfo="+URLEncoder.encode(WaterInfo, "UTF-8")+"&WaterProblem="+URLEncoder.encode(WaterProblem, "UTF-8")+"&ShippingBy="+URLEncoder.encode(ShippingBy, "UTF-8")+"&ShippingDate="+ShippingDate+"&ShippingTo="+URLEncoder.encode(ShippingTo, "UTF-8")+"&TelnoCus="+TelnoCus+"&InstallDetails="+URLEncoder.encode(InstallDetails, "UTF-8")+"&DateInstall="+URLEncoder.encode(DateInstall+" "+TT, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        try {
            jsonArrayRequest = new JsonArrayRequest("https://tssm.thiensurat.co.th/api/api-addBookingInfrom.php"+"?Refno="+Refno+"&Empid="+Empid+"&FirstPeriodPayBy="+ URLEncoder.encode(FirstPeriodPayBy, "UTF-8")+"&FirstPeriodPayAmount="+URLEncoder.encode(FirstPeriodPayAmount, "UTF-8")+
                    "&ContractBy="+ URLEncoder.encode(ContractBy, "UTF-8")+"&WaterInfo="+URLEncoder.encode(WaterInfo, "UTF-8")+"&WaterProblem="+URLEncoder.encode(WaterProblem, "UTF-8")+"&ShippingBy="+URLEncoder.encode(ShippingBy, "UTF-8")+"&ShippingDate="+ShippingDate+"&ShippingTo="+URLEncoder.encode(ShippingTo, "UTF-8")+"&TelnoCus="+TelnoCus+"&InstallDetails="+URLEncoder.encode(InstallDetails, "UTF-8")+"&WaterProblemMore="+URLEncoder.encode(WaterProblemMore, "UTF-8")+"&DateInstall="+URLEncoder.encode(DateInstall+" "+TT, "UTF-8"),


                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                /*            try {
                                new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("บันทึกข้อมูล!")
                                        .setContentText("เรียบร้อย!")
                                        .show();
                            }
                            catch (Exception EX){

                            }*/

                            setResult(RESULT_OK);
                            finish();

                            // showDialog("บันทึกข้อมูล","เรียบร้อย");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //showDialog("บันทึกข้อมูล","เรียบร้อย");

                            setResult(RESULT_OK);
                            finish();

        /*                    try {
                                new SweetAlertDialog(SurveyActivity_preorder.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("บันทึกข้อมูล!")
                                        .setContentText("เรียบร้อย!")
                                        .show();
                            }
                            catch (Exception EX){

                            }*/
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            try {
                requestQueue = Volley.newRequestQueue(this);
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