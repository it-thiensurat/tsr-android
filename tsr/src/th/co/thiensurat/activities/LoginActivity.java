package th.co.thiensurat.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import th.co.bighead.utilities.BHActivity;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.BaseController;
import th.co.thiensurat.data.controller.DatabaseManager;
import th.co.thiensurat.data.info.DeviceMenuInfo;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.PlatformVersionInfo;
import th.co.thiensurat.service.data.AuthenticateInputInfo;
import th.co.thiensurat.service.data.AuthenticateOutputInfo;
import th.co.thiensurat.service.data.CheckSoapOutputInfo;
import th.co.thiensurat.service.data.GetCurrentFortnightInputInfo;
import th.co.thiensurat.service.data.GetCurrentFortnightOutputInfo;
import th.co.thiensurat.service.data.GetDeviceMenusInputInfo;
import th.co.thiensurat.service.data.GetDeviceMenusOutputInfo;
import th.co.thiensurat.service.data.PlatformVersionOutputInfo;

public class LoginActivity extends BHActivity {
    private static final String TAG = "gcm";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9900;

    //CheckConnecting
    public static int timeOut = 20000;//20s

    TextView textView1;
    EditText txtUserName;
    Button btnLogin;
    //GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            /*boolean isUpdate = bundle.getBoolean("IsUpdate");
            if(isUpdate){
                showDialogUpdateApp();
            }*/

            PlatformVersionOutputInfo output = (PlatformVersionOutputInfo)bundle.get("PlatformVersionOutputInfo");
            if (output != null) {
                if (output.ResultCode == 0) {
                    showDialogUpdateApp(output.Info);
                }
            }

        }

        textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText(String.format("%s (%s)", getResources().getString(R.string.tsr_app_version), BHGeneral.SERVICE_MODE.toString()));
        // Check device for Play Services APK. If check succeeds, proceed with
        // GCM registration.
        if (checkPlayServices()) {
            //gcm = GoogleCloudMessaging.getInstance(this);
            String regID = BHPreference.userDeviceId();

            if (regID.isEmpty()) {
                registerInBackground(false);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        txtUserName = (EditText) findViewById(R.id.txtUserName);
        final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        txtUserName.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    txtPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtPassword.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);
                    btnLogin.performClick();
                    return true;
                }

                return false;
            }
        });

        //txtUserName.setText(BHGeneral.USER_ADMIN);
        //txtPassword.setText(BHGeneral.PASSWORD_ADMIN);

        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*** [START] :: Fixed - [BHPROJ-0016-1064] :: [Android-Auto-Full-Synch] เปิดให้ Auto Full-Synch แค่กรณีหลังจาก Login เสร็จแล้วเท่านั้น (ส่วน Full-Synch ตอนที่เปิด App. ให้ตัดทิ้งไปเลย)  ***/
                if (!BHGeneral.DEVELOPER_MODE) {
                    /*File oldFileDB = new File(DatabaseManager.getInstance().getDatabasePath());
                    if (oldFileDB.exists()) {
                        oldFileDB.delete();
                    }*/

                    new BaseController().removeDatabase();
                }
                /*** [END] :: Fixed - [BHPROJ-0016-1064] :: [Android-Auto-Full-Synch] เปิดให้ Auto Full-Synch แค่กรณีหลังจาก Login เสร็จแล้วเท่านั้น (ส่วน Full-Synch ตอนที่เปิด App. ให้ตัดทิ้งไปเลย) ***/

                String userName = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();

                if (userName.equals("") || password.equals("")) {
                    showMessage("Please input user name and password!");
                } else {
                    if (BHPreference.userDeviceId().isEmpty()) {
                        registerInBackground(true);
                    } else {
                        login(userName, password);
                    }
                }
            }
        });
    }

    private void login(final String userName, final String password) {
        if (isConnectingToInternet()) {
            (new AsyncTask<String, Void, Boolean>() {
                @Override
                protected void onPreExecute() {
                    BHLoading.show(LoginActivity.this);
                }

                @Override
                protected Boolean doInBackground(String... urls) {

                    boolean result = false;
                    try {
                        HttpGet httpGet = new HttpGet(urls[0]);
                        HttpClient client = new DefaultHttpClient();
                        //TimeOut 20s
                        HttpParams params = client.getParams();
                        HttpConnectionParams.setConnectionTimeout(params, timeOut);
                        HttpConnectionParams.setSoTimeout(params, timeOut);

                        HttpResponse response = client.execute(httpGet);

                        int statusCode = response.getStatusLine().getStatusCode();

                        if (statusCode == 200) {
                            result = true;
                        }

                    } catch (ClientProtocolException e) {

                    } catch (IOException e) {

                    }

                    return result;
                }

                protected void onPostExecute(Boolean result) {
                    if (!result) {
                        showWarningDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
                        BHLoading.close();
                    } else {

                        (new BackgroundProcess(LoginActivity.this) {

                            private CheckSoapOutputInfo  checkSoapOutput;
                            private AuthenticateInputInfo input;
                            private AuthenticateOutputInfo result;

                            private GetCurrentFortnightOutputInfo outputGetCurrentFortnight = null;

                            private FortnightInfo fortnight = null;
                            private GetDeviceMenusOutputInfo menus = null;

                            @Override
                            protected void before() {
                                // TODO Auto-generated method stub

                                input = new AuthenticateInputInfo();
                                input.UserName = userName;
                                input.Password = password;
                                input.DeviceID = BHPreference.userDeviceId();
                                input.VersionCode = BHPreference.appVersionCode();
                                input.AndroidDeviceID = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                                input.VersionName = BHPreference.appVersionName();
                                input.AndroidApiLevel = BHPreference.androidApiLevel();
                            }

                            @Override
                            protected void calling() {
                                // TODO Auto-generated method stub]
                                checkSoapOutput = TSRController.checkSoap();

                                if (checkSoapOutput != null && checkSoapOutput.ResultCode == 0) {
                                    result = TSRController.authenticate(input);

                                    // เพิ่มใหม่
                                    if(result != null) {
                                        if (result.ResultCode == 0) {
                                            GetCurrentFortnightInputInfo inputGetCurrentFortnight = new GetCurrentFortnightInputInfo();
                                            inputGetCurrentFortnight.OrganizationCode = result.Info.OrganizationCode;
                                            inputGetCurrentFortnight.ProcessType = ((result.Info.ProcessType == null) || (result.Info.ProcessType == "")) ? "Sale" : result.Info.ProcessType;         // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
                                            outputGetCurrentFortnight = TSRController.getCurrentFortnight(inputGetCurrentFortnight);

                                            GetDeviceMenusInputInfo deviceMenuInput = new GetDeviceMenusInputInfo();
                                            deviceMenuInput.EmployeeCode = result.Info.EmpID;
                                            menus = TSRController.getDeviceMenus(deviceMenuInput);
                                        }
                                    }
                                }
                            }

                            @Override
                            protected void after() {
                                // TODO Auto-generated method stub
                                if (result != null) {
                                    switch (result.ResultCode) {
                                        case 0:
//                                            YIM Move to BHPreference.initPreference
//                                            if ((outputGetCurrentFortnight != null) && (outputGetCurrentFortnight.ResultCode == 0)) {   // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
//                                                BHPreference.setFortnightYear(outputGetCurrentFortnight.Info.Year);
//                                                BHPreference.setFortnightNumber(outputGetCurrentFortnight.Info.FortnightNumber);
//                                            }

                                            if (menus != null) {
                                                BHPreference.setUserMenus(menus.Info);
                                            }

                                            if (result.Info != null) {
//                                              YIM Move to BHPreference.initPreference
//                                                BHPreference.setIsAdmin(false);
//                                                BHPreference.setTimeOutLogin(false);
//                                                BHPreference.setServiceMode(BHGeneral.SERVICE_MODE.toString());
//                                                BHPreference.setUserNotAllowLogin(UserController.LoginType.ALLOW.toString());
//                                                BHPreference.setUserID(result.Info.UserName);
//                                                BHPreference.setUserFullName(result.Info.UserFullName);
//                                                BHPreference.setOrganizationCode(result.Info.OrganizationCode);
//                                                BHPreference.setTeamCode(result.Info.TeamCode);
//                                                BHPreference.setSubTeamCode(result.Info.SubTeamCode);
//                                                BHPreference.setEmployeeID(result.Info.EmpID);
//                                                BHPreference.setDepartmentCode(result.Info.DepartmentCode);
//                                                BHPreference.setSubDepartmentCode(result.Info.SubDepartmentCode);
//                                                BHPreference.setSupervisorCode(result.Info.SupervisorCode);
//
//                                                String strSourceSystem = result.Info.SourceSystem;
//                                                BHPreference.setSourceSystem(strSourceSystem);
//                                                BHPreference.setSourceSystemName(result.Info.SourceSystemName);
//                                                BHPreference.setProcessTypeOfEmployee(result.Info.ProcessType);         // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
////                                                showMessage(BHPreference.processTypeOfEmployee());
//
//                                                List<UserInfo> userPositionList = result.Info.UserPosition;
//                                                if (userPositionList != null) {
//                                                    String strPositionCode = "";
//                                                    String strPositionName = "";
//                                                    String strSaleCode = "";
//                                                    String strCashCode = "";
//
//                                                    for (UserInfo userInfo : userPositionList) {
//                                                        strPositionCode += userInfo.PositionCode;
//                                                        strPositionCode += ",";
//
//                                                        strPositionName += userInfo.PositionName;
//                                                        strPositionName += ",";
//
//                                                        if (userInfo.SaleCode != null) {
//                                                            strSaleCode = userInfo.SaleCode;
//                                                        }
//                                                        if ((userInfo.SaleCode != null) && (strSourceSystem.equals("Credit"))) {
//                                                            strCashCode = userInfo.SaleCode;
//                                                        }
//                                                    }
//                                                    strPositionCode = strPositionCode.substring(0, strPositionCode.length() - 1);
//                                                    BHPreference.setPositionCode(strPositionCode);
//
//                                                    strPositionName = strPositionName.substring(0, strPositionName.length() - 1);
//                                                    BHPreference.setPositionName(strPositionName);
//
//                                                    BHPreference.setSaleCode(strSaleCode);
//                                                    BHPreference.setCashCode(strCashCode);
//                                                }
//                                                /*** [START] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ ***/
//                                                BHPreference.setDateFormatGenerateDocument(result.Info.DateFormatGenerateDocument);
//                                                BHPreference.setRunningNumberReceipt(result.Info.RunningNumberReceipt);
//                                                BHPreference.setRunningNumberChangeContract(result.Info.RunningNumberChangeContract);
//                                                BHPreference.setRunningNumberReturnProduct(result.Info.RunningNumberReturnProduct);
//                                                BHPreference.setRunningNumberImpoundProduct(result.Info.RunningNumberImpoundProduct);
//                                                BHPreference.setRunningNumberChangeProduct(result.Info.RunningNumberChangeProduct);
//                                                BHPreference.setRunningNumberComplain(result.Info.RunningNumberComplain);
//                                                /*** [END] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ   ***/

                                                // YIM use this method for move comment code
                                                BHPreference.setIsAdmin(false);
                                                BHPreference.initPreference(result.Info,outputGetCurrentFortnight);

                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                BHPreference.setLogLogin(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                                            } else {
                                                showWarningDialog("แจ้งเตือนการเข้าระบบ", "UserName Or Password ไม่ถูกต้อง");
                                            }
                                            break;
                                        case 4002:
                                            showWarningDialog("แจ้งเตือนการเข้าระบบ",
                                                    "ชื่อผู้ใช้งานนี้มีการใช้งานที่เครื่องอื่นแล้ว \nให้ทำการ Logout จากเครื่องที่ใช้งานในปัจจุบันก่อน หรือติดต่อเจ้าหน้าที่ดูแลระบบ");
                                            break;
                                        case 4010:
                                            showWarningDialog("แจ้งเตือนการเข้าระบบ","ระบบปิดการใช้งาน จะสามารถใช้งานได้อีกครั้งในเวลา 5.00 น.");
                                            break;
                                        case 4003:
                                            showWarningDialog("แจ้งเตือนการเข้าระบบ",
                                                    "คุณป้อนรหัสผ่านผิดเกิน 3 ครั้ง กรุณารออีก 3 นาที");
                                            btnLogin.setEnabled(false);
                                            final TextView textCounter = (TextView) findViewById(R.id.textCounter);
                                            // textCounter.setVisibility(View.VISIBLE);
                                            new CountDownTimer(180000, 1000) { // adjust the milliseconds here

                                                public void onTick(long millisUntilFinished) {
                                                    if (millisUntilFinished / 1000 > 60)
                                                        textCounter
                                                                .setText("Please wait : "
                                                                        + millisUntilFinished
                                                                        / 60000
                                                                        + "m "
                                                                        + (millisUntilFinished % 60000)
                                                                        / 1000 + "s");
                                                    else
                                                        textCounter
                                                                .setText("Please wait : "
                                                                        + millisUntilFinished
                                                                        / 1000 + "s");
                                                }

                                                // here you can have your logic to set
                                                // text to edittext

                                                public void onFinish() {
                                                    btnLogin.setEnabled(true);
                                                    textCounter.setText("");
                                                    // textCounter.setVisibility(View.INVISIBLE);
                                                }
                                            }.start();
                                            break;
                                        case 4004:
                                            showWarningDialog(
                                                    "แจ้งเตือนการเข้าระบบ",
                                                    "ชื่อผู้ใช้งานนี้ไม่มีสิทธิ์เข้าใช้งาน");
                                            break;
                                        case 4000:
                                            showWarningDialog(
                                                    "แจ้งเตือนการเข้าระบบ",
                                                    "ไม่พบผู้ใช้งานนี้ในระบบ ลองใหม่อีกครั้ง");
                                            break;

                                        case 9000: //Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead
                                            showDialogUpdateApp(result.Info.PlatformVersion);
                                            break;

                                        default:
                                            showWarningDialog("แจ้งเตือนการเข้าระบบ",
                                                    "ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง ลองใหม่อีกครั้ง");
                                            break;
                                    }
                                } else {
                                    showWarningDialog("แจ้งเตือนการเข้าระบบ",
                                            "ไม่สามารถเข้าสู่ระบบได้ กรุณาลองใหม่อีกครั้ง");
                                }
                            }
                        }).start();


                    }
                }

						/*protected void onPostExecute(Boolean result) {
                            if (!result) {
								showWarningDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
								btnLogin.setEnabled(true);
							} else {

								(new BackgroundProcess(LoginActivity.this) {

									private AuthenticateInputInfo input;
									private AuthenticateOutputInfo result;

									*//*** [START] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android ***//*
                                    private IsUserLoggedInInputInfo inputIsLoggedIn;
									private IsUserLoggedInOutputInfo resultIsLoggedIn;

									private UpdateUserLoggedInInputInfo inputUpdateLoggedIn;
									private UpdateUserLoggedInOutputInfo resultUpdateLoggedIn;
									*//*** [END] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android ***//*

									private GetCurrentFortnightOutputInfo outputGetCurrentFortnight = null;

									// เพิ่มใหม่
									private GetUserByUserNameInputInfo getInput;
									private GetUserByUserNameOutputInfo getOutput = null;

									*//*** Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***//*
									private AddUserDeviceLogInputInfo inputAddUserDeviceLog;

									private FortnightInfo fortnight = null;
									private GetDeviceMenusOutputInfo menus = null;

									@Override
									protected void before() {
										// TODO Auto-generated method stub
										input = new AuthenticateInputInfo();
										input.UserName = userName;
										input.Password = password;
										input.DeviceID = BHPreference.userDeviceId();

										*//*** [START] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android ***//*
										inputIsLoggedIn = new IsUserLoggedInInputInfo();
										inputIsLoggedIn.UserName = userName;
										inputIsLoggedIn.Password = password;
										inputIsLoggedIn.DeviceID = BHPreference.userDeviceId();

										inputUpdateLoggedIn = new UpdateUserLoggedInInputInfo();
										inputUpdateLoggedIn.UserName = userName;
										inputUpdateLoggedIn.Password = password;
										inputUpdateLoggedIn.DeviceID = BHPreference.userDeviceId();
										inputUpdateLoggedIn.LastLoginDate = new Date();    // Fixed - [BHPROJ-0020-1026] การบริหารจัดการผู้ใช้งาน วันที่ Login ล่าสุด แสดงไม่ถูกต้อง
										*//*** [END] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android ***//*

										*//*** [START] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***//*
										inputAddUserDeviceLog = new AddUserDeviceLogInputInfo();
										inputAddUserDeviceLog.UserName = userName;
										inputAddUserDeviceLog.DeviceID = BHPreference.userDeviceId();
										//inputAddUserDeviceLog.CreateDate = new Date();
										inputAddUserDeviceLog.ProcessType = AddUserDeviceLogInputInfo.UserDeviceLogProcessType.ANDROID_LOGIN.toString();
										*//*** [END] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***//*

										// เพิ่มใหม่
										getInput = new GetUserByUserNameInputInfo();
										getInput.UserName = userName;
									}

									@Override
									protected void calling() {
										// TODO Auto-generated method stub]
										result = TSRController.authenticate(input);

										// เพิ่มใหม่
										if (result.ResultCode == 0) {
											*//*** [START] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android ***//*
											resultIsLoggedIn = TSRController.isUserLoggedIn(inputIsLoggedIn);
											if (resultIsLoggedIn.ResultCode == 0) {
												if (!resultIsLoggedIn.Info) {
													resultUpdateLoggedIn = TSRController.updateUserLoggedIn(inputUpdateLoggedIn);
												}
											}
											*//*** [END] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android ***//*

											getOutput = TSRController.getUserByUserName(getInput);
											if (getOutput.Info != null) {
												*//*** [START] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***//*
												//inputAddUserDeviceLog.UserID = getOutput.Info.UserID;
												//inputAddUserDeviceLog.CreateBy = getOutput.Info.EmpID;
												TSRController.addUserDeviceLog(inputAddUserDeviceLog);
												*//*** [END] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***//*

												//fortnight = TSRController.getFortnight(getOutput.Info.OrganizationCode);
												GetCurrentFortnightInputInfo inputGetCurrentFortnight = new GetCurrentFortnightInputInfo();
												inputGetCurrentFortnight.OrganizationCode = getOutput.Info.OrganizationCode;
												outputGetCurrentFortnight = TSRController.getCurrentFortnight(inputGetCurrentFortnight);

												GetDeviceMenusInputInfo deviceMenuInput = new GetDeviceMenusInputInfo();
												deviceMenuInput.EmployeeCode = getOutput.Info.EmpID;
												menus = TSRController.getDeviceMenus(deviceMenuInput);
											}
										}

									}

									@Override
									protected void after() {
										// TODO Auto-generated method stub
										// if (result != null && getOutput != null) {
										btnLogin.setEnabled(true);
										if (result != null) {
											if (result.ResultCode == 0) {
												*//*if (fortnight != null) {
													BHPreference.setFortnightYear(fortnight.Year);
													BHPreference.setFortnightNumber(fortnight.FortnightNumber);
												}*//*
												if (outputGetCurrentFortnight != null) {
													BHPreference.setFortnightYear(outputGetCurrentFortnight.Info.Year);
													BHPreference.setFortnightNumber(outputGetCurrentFortnight.Info.FortnightNumber);
												}

												if (menus != null) {
													BHPreference.setUserMenus(menus.Info);
												}

												if (getOutput.Info != null) {
													BHPreference.setServiceMode(BHGeneral.SERVICE_MODE.toString());
													BHPreference.setUserID(getOutput.Info.UserName);
													BHPreference.setUserFullName(getOutput.Info.UserFullName);
													BHPreference.setOrganizationCode(getOutput.Info.OrganizationCode);
													// BHPreference.setTeamCode(getOutput.Info.EmployeeDetail.TeamCode);
													BHPreference.setTeamCode(getOutput.Info.TeamCode);
//									String aa = getOutput.Info.SubTeamCode;
													BHPreference.setSubTeamCode(getOutput.Info.SubTeamCode);

													BHPreference.setEmployeeID(getOutput.Info.EmpID);
													// BHPreference.setCashCode(getOutput.Info.CashCode);
													// BHPreference.setFortnightYear(getOutput.Info.FortnightYear);
													// BHPreference.setSaleCode(getOutput.Info.SaleCode);

													BHPreference.setDepartmentCode(getOutput.Info.DepartmentCode);
													BHPreference.setSubDepartmentCode(getOutput.Info.SubDepartmentCode);
													BHPreference.setSupervisorCode(getOutput.Info.SupervisorCode);

													String strSourceSystem = getOutput.Info.SourceSystem;
													BHPreference.setSourceSystem(strSourceSystem);
													BHPreference.setSourceSystemName(getOutput.Info.SourceSystemName);

													List<UserInfo> userPositionList = getOutput.Info.UserPosition;
													if (userPositionList != null) {
														String strPositionCode = "";
														String strPositionName = "";
														String strSaleCode = "";
														String strCashCode = "";

														for (UserInfo userInfo : userPositionList) {
															strPositionCode += userInfo.PositionCode;
															strPositionCode += ",";

															strPositionName += userInfo.PositionName;
															strPositionName += ",";

															if (userInfo.SaleCode != null) {
																strSaleCode = userInfo.SaleCode;
															}
															if ((userInfo.SaleCode != null) && (strSourceSystem.equals("Credit"))) {
																strCashCode = userInfo.SaleCode;
															}
														}
														strPositionCode = strPositionCode.substring(0, strPositionCode.length() - 1);
														BHPreference.setPositionCode(strPositionCode);

														strPositionName = strPositionName.substring(0, strPositionName.length() - 1);
														BHPreference.setPositionName(strPositionName);

														BHPreference.setSaleCode(strSaleCode);
														BHPreference.setCashCode(strCashCode);

													}    // if (userPositionList != null) {

													// try {
													// new BaseController().removeDatabase();
													// Thread.sleep(1000);
													// } catch (InterruptedException e) {
													// // TODO Auto-generated catch block
													// e.printStackTrace();
													// }

													//Timeout();

													Intent intent = new Intent(LoginActivity.this, MainActivity.class);
													startActivity(intent);
													finish();
													BHPreference.setLogLogin(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

												} else {
													showWarningDialog("แจ้งเตือนการเข้าระบบ", "UserName Or Password ไม่ถูกต้อง");
												}
											} else if (result.ResultCode == 4002) {
												showWarningDialog("แจ้งเตือนการเข้าระบบ",
														"ชื่อผู้ใช้งานนี้มีการใช้งานที่เครื่องอื่นแล้ว \nให้ทำการ Logout จากเครื่องที่ใช้งานในปัจจุบันก่อน หรือติดต่อเจ้าหน้าที่ดูแลระบบ");
											} else if (result.ResultCode == 4003) {
												showWarningDialog("แจ้งเตือนการเข้าระบบ",
														"คุณป้อนรหัสผ่านผิดเกิน 3 ครั้ง กรุณารออีก 3 นาที");
												btnLogin.setEnabled(false);
												final TextView textCounter = (TextView) findViewById(R.id.textCounter);
												// textCounter.setVisibility(View.VISIBLE);
												new CountDownTimer(180000, 1000) { // adjust the milliseconds here

													public void onTick(long millisUntilFinished) {
														if (millisUntilFinished / 1000 > 60)
															textCounter
																	.setText("Please wait : "
																			+ millisUntilFinished
																			/ 60000
																			+ "m "
																			+ (millisUntilFinished % 60000)
																			/ 1000 + "s");
														else
															textCounter
																	.setText("Please wait : "
																			+ millisUntilFinished
																			/ 1000 + "s");
													}

													// here you can have your logic to set
													// text to edittext

													public void onFinish() {
														btnLogin.setEnabled(true);
														textCounter.setText("");
														// textCounter.setVisibility(View.INVISIBLE);
													}
												}.start();
											} else if (result.ResultCode == 4004) {
												showWarningDialog(
														"แจ้งเตือนการเข้าระบบ",
														"ชื่อผู้ใช้งานนี้ไม่มีสิทธิเข้าใช้งาน");
											} else if (result.ResultCode == 4000) {
												showWarningDialog(
														"แจ้งเตือนการเข้าระบบ",
														"ไม่พบผู้ใช้งานนี้ในระบบ ลองใหม่อีกครั้ง");
											} else {
												showWarningDialog("แจ้งเตือนการเข้าระบบ",
														"ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง ลองใหม่อีกครั้ง");
											}
										} else {
											showWarningDialog("แจ้งเตือนการเข้าระบบ",
													"ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง ลองใหม่อีกครั้ง");
										}
									}
								}).start();


							}
						}*/
            }).execute(BHPreference.TSR_SERVICE_URL);
        } else {
            showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
            btnLogin.setEnabled(true);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it
     * doesn't, display a dialog that allows users to download the APK from the
     * Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }

            return false;
        }

        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground(boolean isAction) {
        /*** [START] :: FCM ***/
        /*new BackgroundProcess(LoginActivity.this) {
            @Override
            protected void calling() {
                //try {
                    *//*if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    }
                    String regID = gcm.register(BHPreference.GCM_SENDER_ID);*//*

                //FirebaseInstanceId.getInstance().getToken()

                    String regID = FirebaseInstanceId.getInstance().getId();
                    Log.i(TAG, "Device id " + regID);
                    BHPreference.setUserDeviceID(regID);
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
            }

            @Override
            protected void after() {
                if (showDialog) {
                    if (BHPreference.userDeviceId().isEmpty()) {
                        //showWarningDialog("แจ้งเตือน", "ไม่พบ Device ของเครื่อง กรุณาทำการ LOGIN ใหม่อีกครั้ง");
                        showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
                    } else {
                        btnLogin.callOnClick();
                    }
                }
            }

        }.start();*/

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        // Log and toast
//                        Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
//                    }
//                });
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.e("Updated Token", token);

                BHPreference.setUserDeviceID(token);
                if (isAction) {
                    btnLogin.callOnClick();
                }
            }

        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FCM Device Token", "Error calling getInstanceId " + e.getLocalizedMessage());

                if (isAction && BHPreference.userDeviceId().isEmpty()) {
                    showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
                }
            }
        });
        /*** [END] :: FCM ***/
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public DeviceMenuInfo getDeviceMenuAdmin(){
        DeviceMenuInfo info = new DeviceMenuInfo();
        info.MenuID = getString(R.string.main_menu_admin);
        info.MenuName = getString(R.string.main_menu_admin);
        info.MenuIndex = 99;

        return info;
    }

    /*private void Timeout() {
        int i = 8;
        Intent intent = new Intent(MainActivity.MyBroadcastReceiver.ACTION_NAME);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                + (i * 1000), pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + ((60*60*1000)*i), pendingIntent);
        showMessage("ระบบจะทำการ Logout อีก 8 ชั่วโมง");
    }*/


    /*private void showDialogUpdateApp(){
        AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(this)
                .setTitle("New Version")
                .setMessage("ตรวจพบ เวอร์ชั่นใหม่บน Play Store กรุณาอัพเดทแอพพลิเคชั่น")
                .setCancelable(false)
                .setNegativeButton(this.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            PackageManager pm = getPackageManager();
                            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=" + packageInfo.packageName));
                            startActivity(intent);
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.i("CheckAppVersion", e.getMessage());
                        }
                        dialog.cancel();
                    }
                });
        setupAlert.show();
    }*/


    /*private PackageInfo GetPackageInfo() {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);

            return packageInfo;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean IsValidateVersion(PlatformVersionInfo info) {
        try {
            if (info != null) {
                if (info.IsValidate) {
                    PackageInfo packageInfo = GetPackageInfo();
                    if (packageInfo != null) {
                        String currentVersionName = packageInfo.versionName;
                        String[] currentVersionArray = currentVersionName.split("\\.");

                        String serverVersionName = info.VersionName;
                        String[] serverVersionArray = serverVersionName.split("\\.");

                        if (currentVersionArray.length != 0 && serverVersionArray.length != 0) {
                            int resultCurrentVersion = CalculatingByAppVersion(currentVersionArray);
                            int resultServerVersion = CalculatingByAppVersion(serverVersionArray);

                            if (resultCurrentVersion < resultServerVersion) {
                                showDialogUpdateApp(info, packageInfo);
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private int CalculatingByAppVersion(String[] array) {
        try {
          if (array.length != 0) {
              int startDigit = 1;
              int sumResult = 0;


              for (int i = array.length - 1; i >= 0; i-- ) {

                  int number = Integer.parseInt(array[i]);
                  sumResult = (number * startDigit) + sumResult;
                  startDigit = startDigit * 100;
              }

              return sumResult;
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }*/

    private void showDialogUpdateApp(PlatformVersionInfo info){
        if (info != null && info.IsUpdate) {
            AlertDialog.Builder setupAlert;
            setupAlert = new AlertDialog.Builder(this)
                    .setTitle(info.Title)
                    .setMessage(info.Message)
                    .setCancelable(false)
                    .setNegativeButton(info.ButtonName, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=" + BHPreference.appPackageName()));
                            startActivity(intent);

                            dialog.cancel();
                        }
                    });
            setupAlert.show();
        }
    }
}
