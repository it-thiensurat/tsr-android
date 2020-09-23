package th.co.thiensurat.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.thiensurat.R;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;
import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class SurveyActivity extends Activity implements View.OnClickListener {

    private String contractNo;
    private String refNo;
    private String empId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        setUpView();
        loadSurvey();
    }

    private Button buttonSave;
    private ImageButton buttonBack;
    private EditText editTextHabitat, editTextCareer, editTextHobby, editTextSuggestion;
    private LinearLayout layoutHabitat, layoutCareer;
    private TextView homeStar, homeStatusStar, homeStatusOtherStar, homeTimeStar, jobStar, jobOtherStar, jobTimeStar, salaryStar;
    private Spinner spinnerMarry, spinnerHabitat, spinnerStatusLive, spinnerTimeLive, spinnerCareer, spinnerCareerTime, spinnerSalary, spinnerHobby, spinnerSuggestion;
    private void setUpView() {
        buttonSave          = (Button) findViewById(R.id.button_save);
        buttonBack          = (ImageButton) findViewById(R.id.button_back);

        editTextHabitat     = (EditText) findViewById(R.id.editTextHabitat);
        editTextCareer      = (EditText) findViewById(R.id.editTextCareer);
        editTextHobby       = (EditText) findViewById(R.id.editTextHobby);
        editTextSuggestion  = (EditText) findViewById(R.id.editTextSuggestion);

        layoutHabitat       = (LinearLayout) findViewById(R.id.layoutHabitat);
        layoutCareer        = (LinearLayout) findViewById(R.id.layoutCareer);

        layoutHabitat.setVisibility(View.GONE);
        layoutCareer.setVisibility(View.GONE);

        spinnerMarry        = (Spinner) findViewById(R.id.spinnerMary);
        spinnerHabitat      = (Spinner) findViewById(R.id.spinnerHabitat);
        spinnerStatusLive   = (Spinner) findViewById(R.id.spinnerStatusLive);
        spinnerTimeLive     = (Spinner) findViewById(R.id.spinnerTimeLive);
        spinnerCareer       = (Spinner) findViewById(R.id.spinnerCareer);
        spinnerCareerTime   = (Spinner) findViewById(R.id.spinnerCareerTime);
        spinnerSalary       = (Spinner) findViewById(R.id.spinnerSalary);
        spinnerHobby        = (Spinner) findViewById(R.id.spinnerHobby);
        spinnerSuggestion   = (Spinner) findViewById(R.id.spinnerSuggestion);

        homeStar                = (TextView) findViewById(R.id.homeStar);
        homeStatusStar          = (TextView) findViewById(R.id.homeStatusStar);
        homeStatusOtherStar     = (TextView) findViewById(R.id.homeStatusOtherStar);
        homeTimeStar            = (TextView) findViewById(R.id.homeTimeStar);
        jobStar                 = (TextView) findViewById(R.id.JobStar);
        jobOtherStar            = (TextView) findViewById(R.id.JobOtherStar);
        jobTimeStar             = (TextView) findViewById(R.id.JobTimeStar);
        salaryStar              = (TextView) findViewById(R.id.SalaryStar);

        homeStar.setVisibility(View.GONE);
        homeStatusStar.setVisibility(View.GONE);
        homeStatusOtherStar.setVisibility(View.GONE);
        homeTimeStar.setVisibility(View.GONE);
        jobStar.setVisibility(View.GONE);
        jobOtherStar.setVisibility(View.GONE);
        jobTimeStar.setVisibility(View.GONE);
        salaryStar.setVisibility(View.GONE);

        buttonBack.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        contractNo = getIntent().getStringExtra("CONTRACT_NUMBER");
        refNo = getIntent().getStringExtra("REFERRENCE_NUMBER");
        empId = getIntent().getStringExtra("EMPLOYEE_NUMBER");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.button_save:
                saveSurvey();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void saveSurvey() {
        String habitatOther = "", careerOther = "", hobbyOther, suggestionOther;
        habitatOther    = editTextHabitat.getText().toString();
        careerOther     = editTextCareer.getText().toString();
        hobbyOther      = editTextHobby.getText().toString();
        suggestionOther = editTextSuggestion.getText().toString();

        if (marryId > 0 && homeId > 0 && timeLiveId > 0 && jobId > 0 && jobTimeId > 0 && salaryId > 0) {
            if (homeId == 8 && "".equals(habitatOther) && jobId == 24 && "".equals(careerOther)) {
                AlertDialog.Builder setupAlert;
                setupAlert = new AlertDialog.Builder(SurveyActivity.this)
                        .setTitle("แจ้งเตือน")
                        .setMessage("กรุณาระบุกรณีเลือกอื่นๆ")
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                homeStatusOtherStar.setVisibility(View.VISIBLE);
                                jobOtherStar.setVisibility(View.VISIBLE);
                            }
                        });
                setupAlert.show();
            } else if (homeId == 8 && "".equals(habitatOther)) {
                AlertDialog.Builder setupAlert;
                setupAlert = new AlertDialog.Builder(SurveyActivity.this)
                        .setTitle("แจ้งเตือน")
                        .setMessage("กรุณาระบุที่อยู่อื่นๆ")
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                homeStatusOtherStar.setVisibility(View.VISIBLE);
                            }
                        });
                setupAlert.show();
            } else if (jobId == 24 && "".equals(careerOther)) {
                AlertDialog.Builder setupAlert;
                setupAlert = new AlertDialog.Builder(SurveyActivity.this)
                        .setTitle("แจ้งเตือน")
                        .setMessage("กรุณาระบุอาชีพอื่นๆ")
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                jobOtherStar.setVisibility(View.VISIBLE);
                            }
                        });
                setupAlert.show();
            } else {
                try {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Service request = retrofit.create(Service.class);
                    Call call = request.saveSurvey(refNo, contractNo, marryId, homeId, timeLiveId, jobId, jobTimeId, salaryId, empId, habitatOther, careerOther);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, retrofit2.Response response) {
                            Gson gson = new Gson();
                            try {
                                JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                                Log.e("save survey", String.valueOf(jsonObject));
                                JSONArray array = jsonObject.getJSONArray("data");
                                JSONObject obj = null;
                                for (int i = 0; i < array.length(); i++) {
                                    obj = array.getJSONObject(i);
                                    String status = obj.getString("StatusInsert");
                                    if ("SUCCESS".equals(status)) {
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        AlertDialog.Builder setupAlert;
                                        setupAlert = new AlertDialog.Builder(SurveyActivity.this)
                                                .setTitle("แจ้งเตือน")
                                                .setMessage("พบข้อผิดพลาดในการบันทึกข้อมูล")
                                                .setCancelable(false)
                                                .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        setupAlert.show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("JSONException", e.getLocalizedMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.e("data", "2");
                        }
                    });
                } catch (Exception e) {
                    Log.e("data", "3");
                }
            }
        } else {
            AlertDialog.Builder setupAlert;
            setupAlert = new AlertDialog.Builder(SurveyActivity.this)
                    .setTitle("แจ้งเตือน")
                    .setMessage("กรุณาตอบแบบสอบถามให้ครบทุกข้อ")
                    .setCancelable(false)
                    .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                            if (marryId == 0) {
                                homeStar.setVisibility(View.VISIBLE);
                            } else {
                                homeStar.setVisibility(View.GONE);
                            }

                            if (homeId == 0) {
                                homeStatusStar.setVisibility(View.VISIBLE);
                            } else {
                                homeStatusStar.setVisibility(View.GONE);
                            }

                            if (timeLiveId == 0) {
                                homeTimeStar.setVisibility(View.VISIBLE);
                            } else {
                                homeTimeStar.setVisibility(View.GONE);
                            }

                            if (jobId == 0) {
                                jobStar.setVisibility(View.VISIBLE);
                            } else {
                                jobStar.setVisibility(View.GONE);
                            }

                            if (jobTimeId == 0) {
                                jobTimeStar.setVisibility(View.VISIBLE);
                            } else {
                                jobTimeStar.setVisibility(View.GONE);
                            }

                            if (salaryId == 0) {
                                salaryStar.setVisibility(View.VISIBLE);
                            } else {
                                salaryStar.setVisibility(View.GONE);
                            }
                        }
                    });
            setupAlert.show();
        }
    }

    private void loadSurvey() {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GIS_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Service request = retrofit.create(Service.class);
                Call call = request.getSurvey();
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, retrofit2.Response response) {
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                            Log.e("jsonObject", String.valueOf(jsonObject));
                            if ("SUCCESS".equals(jsonObject.getString("status"))) {
                                parseJsonData(jsonObject.getJSONObject("data"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONException", e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("data", "2");
                    }
                });

            } catch (Exception e) {
                Log.e("data", "3");
            }
    }

    private int marryId     = 0;
    private int homeId      = 0;
    private int timeLiveId  = 0;
    private int jobId       = 0;
    private int jobTimeId   = 0;
    private int salaryId    = 0;

    private List<String> listMarry = new ArrayList<String>();
    private List<String> listHome = new ArrayList<String>();
    private List<String> listHomeTime = new ArrayList<String>();
    private List<String> listJob = new ArrayList<String>();
    private List<String> listJobTime = new ArrayList<String>();
    private List<String> listSalary = new ArrayList<String>();
    private void parseJsonData(JSONObject object) {
//        Log.e("data survey", String.valueOf(object));
        JSONArray marry = null;
        JSONArray home = null;
        JSONArray homeTime = null;
        JSONArray job = null;
        JSONArray jobTime = null;
        JSONArray salary = null;

        try {
            /**
             * Marry status
             */
            marry = object.getJSONArray("marryStatus");
            JSONArray finalMarry = marry;
            listMarry.add("เลือกสถานะภาพ");
            for(int i = 0; i < marry.length(); i++){
                listMarry.add(marry.getJSONObject(i).getString("CustormerStatusName"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listMarry);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMarry.setAdapter(adapter);
            spinnerMarry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
//                        Toast.makeText(getApplicationContext(), String.valueOf(i) + finalMarry.getJSONObject(i), Toast.LENGTH_LONG).show();
                        if (i > 0) {
                            marryId = finalMarry.getJSONObject(i - 1).getInt("id");
                        } else if (i == 0) {
                            marryId = 0;
                        }
//                        Toast.makeText(getApplicationContext(), finalMarry.getJSONObject(i - 1).getInt("id") + "" + listMarry.get(i), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(getApplicationContext(), "onNothingSelected", Toast.LENGTH_LONG).show();
                }
            });
            /**
             * End
             */

            /**
             * Home status
             */
            home = object.getJSONArray("homeStatus");
            JSONArray finalHome = home;
            listHome.add("เลือกสถานะที่อยู่อาศัย");
            for(int i = 0; i < home.length(); i++){
                listHome.add(home.getJSONObject(i).getString("ResidenceStatusName"));
            }
            ArrayAdapter<String> adapterHome = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listHome);
            adapterHome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHabitat.setAdapter(adapterHome);
            spinnerHabitat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (i > 0) {
                            homeId = finalHome.getJSONObject(i - 1).getInt("ID");
//                        Toast.makeText(getApplicationContext(), finalHome.getJSONObject(i - 1).getInt("ID") + "" + listHome.get(i), Toast.LENGTH_LONG).show();
                            if (homeId == 8) {
                                layoutHabitat.setVisibility(View.VISIBLE);
                            } else {
                                layoutHabitat.setVisibility(View.GONE);
                            }
                        } else if (i == 0) {
                            homeId = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            /**
             * End
             */

            /**
             * Home time
             */
            homeTime = object.getJSONArray("homeTime");
            JSONArray finalHomeTime = homeTime;
            listHomeTime.add("เลือกระยะเวลาที่อยู่อาศัย");
            for(int i = 0; i < homeTime.length(); i++){
                listHomeTime.add(homeTime.getJSONObject(i).getString("ResidenceTimeName"));
            }
            ArrayAdapter<String> adapterHomeTime = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listHomeTime);
            adapterHomeTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTimeLive.setAdapter(adapterHomeTime);
            spinnerTimeLive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (i > 0) {
                            timeLiveId = finalHomeTime.getJSONObject(i - 1).getInt("ID");
//                        Toast.makeText(getApplicationContext(), finalHomeTime.getJSONObject(i - 1).getInt("ID") + "" + listHomeTime.get(i), Toast.LENGTH_LONG).show();
                        } else if (i ==0) {
                            timeLiveId = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            /**
             * End
             */

            /**
             * Job
             */
            job = object.getJSONArray("job");
            JSONArray finalJob = job;
            listJob.add("เลือกอาชีพ");
            for(int i = 0; i < job.length(); i++){
                listJob.add(job.getJSONObject(i).getString("JobName"));
            }
            ArrayAdapter<String> adapterJob = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listJob);
            adapterJob.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCareer.setAdapter(adapterJob);
            spinnerCareer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (i > 0) {
                            jobId = finalJob.getJSONObject(i - 1).getInt("ID");
//                        Toast.makeText(getApplicationContext(), finalJob.getJSONObject(i - 1).getInt("ID") + "" + listJob.get(i), Toast.LENGTH_LONG).show();
                            if (jobId == 24) {
                                layoutCareer.setVisibility(View.VISIBLE);
                            } else {
                                layoutCareer.setVisibility(View.GONE);
                            }
                        } else if (i == 0) {
                            jobId = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            /**
             * End
             */

            /**
             * Job time
             */
            jobTime = object.getJSONArray("jobTime");
            JSONArray finalJobTime = jobTime;
            listJobTime.add("เลือกอายุงาน");
            for(int i = 0; i < jobTime.length(); i++){
                listJobTime.add(jobTime.getJSONObject(i).getString("JobTimeName"));
            }
            ArrayAdapter<String> adapterJobTime = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listJobTime);
            adapterJobTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCareerTime.setAdapter(adapterJobTime);
            spinnerCareerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (i > 0) {
                            jobTimeId = finalJobTime.getJSONObject(i - 1).getInt("ID");
//                        Toast.makeText(getApplicationContext(), finalJobTime.getJSONObject(i - 1).getInt("ID") + "" + listJobTime.get(i), Toast.LENGTH_LONG).show();
                        } else if (i == 0) {
                            jobTimeId = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            /**
             * End
             */

            /**
             * Salary
             */
            salary = object.getJSONArray("salary");
            JSONArray finalSalary = salary;
            listSalary.add("เลือกรายได้ต่อเดือน");
            for(int i = 0; i < salary.length(); i++){
                listSalary.add(salary.getJSONObject(i).getString("Salary"));
            }
            ArrayAdapter<String> adapterSalary = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listSalary);
            adapterSalary.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSalary.setAdapter(adapterSalary);
            spinnerSalary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (i > 0) {
                            salaryId = finalSalary.getJSONObject(i - 1).getInt("ID");
//                        Toast.makeText(getApplicationContext(), finalSalary.getJSONObject(i - 1).getInt("ID") + "" + listSalary.get(i), Toast.LENGTH_LONG).show();
                        } else if (i == 0) {
                            salaryId = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            /**
             * End
             */
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}