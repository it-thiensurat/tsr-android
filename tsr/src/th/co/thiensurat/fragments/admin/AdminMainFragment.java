package th.co.thiensurat.fragments.admin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.List;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.UserController;
import th.co.thiensurat.data.info.DeviceMenuInfo;
import th.co.thiensurat.data.info.UserInfo;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.service.data.GetCurrentFortnightInputInfo;
import th.co.thiensurat.service.data.GetCurrentFortnightOutputInfo;
import th.co.thiensurat.service.data.GetDeviceMenusInputInfo;
import th.co.thiensurat.service.data.GetDeviceMenusOutputInfo;
import th.co.thiensurat.service.data.GetUserByUserNameInputInfo;
import th.co.thiensurat.service.data.GetUserByUserNameOutputInfo;

public class AdminMainFragment extends BHFragment {

    @InjectView
    EditText etUserName;
    public static int timeOut = 20000;//20s

    @Override
    protected int fragmentID() {
        return R.layout.fragment_admin_main;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_ok};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //etUserName.setText("BAA03");
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_ok:
                String userName = etUserName.getText().toString();
                login(userName);

                break;
        }
    }


    private void login(final String userName) {
        if (isConnectingToInternet()) {
            (new AsyncTask<String, Void, Boolean>() {
                @Override
                protected void onPreExecute() {
                    BHLoading.show(activity);
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

                        (new BackgroundProcess(activity) {

                            private GetUserByUserNameInputInfo input;
                            private GetUserByUserNameOutputInfo result = null;

                            private GetCurrentFortnightOutputInfo outputGetCurrentFortnight = null;
                            private GetDeviceMenusOutputInfo menus = null;

                            @Override
                            protected void before() {
                                // TODO Auto-generated method stub
                                input = new GetUserByUserNameInputInfo();
                                input.UserName = userName;
                            }

                            @Override
                            protected void calling() {
                                // TODO Auto-generated method stub]


                              //
                               // result = TSRController.getUserByUserName(input);
                                try {
                                    String DD= BHApplication.getInstance().getPrefManager().getPreferrence("select_p");
                                    if(DD.equals("Credit")) {
                                        result = TSRController.getUserByUserName2(input);

                                    }
                                    else if(DD.equals("Sale")) {
                                        result = TSRController.getUserByUserName3(input);

                                    }
                                    else {
                                        result = TSRController.getUserByUserName(input);
                                    }


                                }
                                catch (Exception ex){
                                    result = TSRController.getUserByUserName(input);

                                }

                                // เพิ่มใหม่
                                if (result.ResultCode == 0) {
                                    GetCurrentFortnightInputInfo inputGetCurrentFortnight = new GetCurrentFortnightInputInfo();
                                    inputGetCurrentFortnight.OrganizationCode = result.Info.OrganizationCode;
                                    inputGetCurrentFortnight.ProcessType = ((result.Info.ProcessType == null) || (result.Info.ProcessType == "")) ? "Sale" : result.Info.ProcessType;         // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
                                    outputGetCurrentFortnight = TSRController.getCurrentFortnight(inputGetCurrentFortnight);

                                    GetDeviceMenusInputInfo deviceMenuInput = new GetDeviceMenusInputInfo();

                                   // deviceMenuInput.EmployeeCode = result.Info.EmpID;
                                    deviceMenuInput.EmployeeCode = result.Info.EmpID+"_"+result.Info.SourceSystem;

                                    menus = TSRController.getDeviceMenus(deviceMenuInput);
                                }
                            }

                            @Override
                            protected void after() {
                                // TODO Auto-generated method stub
                                if (result.ResultCode == 0) {
//                                    YIM Move to BHPreference.initPreference
//                                    if ((outputGetCurrentFortnight != null)  && (outputGetCurrentFortnight.ResultCode == 0)) {  // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
//                                        BHPreference.setFortnightYear(outputGetCurrentFortnight.Info.Year);
//                                        BHPreference.setFortnightNumber(outputGetCurrentFortnight.Info.FortnightNumber);
//                                    }

                                    if (menus != null) {
                                        menus.Info.add(getDeviceMenuAdmin());

                                        BHPreference.setUserMenus(menus.Info);

                                        activity.menusInfo.clear();
                                        activity.menusInfo.addAll(BHPreference.getUserMenus());

                                        activity.mainMenuAdapter.refresh();
                                        activity.mainMenuAdapter.notifyDataSetChanged();
                                    }

                                    if (result.Info != null) {
//                                        YIM Move to BHPreference.initPreference
//                                        BHPreference.setServiceMode(BHGeneral.SERVICE_MODE.toString());
//                                        BHPreference.setUserNotAllowLogin(UserController.LoginType.ALLOW.toString());
//                                        BHPreference.setUserID(result.Info.UserName);
//                                        BHPreference.setUserFullName(result.Info.UserFullName);
//                                        BHPreference.setOrganizationCode(result.Info.OrganizationCode);
//                                        BHPreference.setTeamCode(result.Info.TeamCode);
//                                        BHPreference.setSubTeamCode(result.Info.SubTeamCode);
//                                        BHPreference.setEmployeeID(result.Info.EmpID);
//                                        BHPreference.setDepartmentCode(result.Info.DepartmentCode);
//                                        BHPreference.setSubDepartmentCode(result.Info.SubDepartmentCode);
//                                        BHPreference.setSupervisorCode(result.Info.SupervisorCode);
//
//                                        String strSourceSystem = result.Info.SourceSystem;
//                                        BHPreference.setSourceSystem(strSourceSystem);
//                                        BHPreference.setSourceSystemName(result.Info.SourceSystemName);
//                                        BHPreference.setProcessTypeOfEmployee(result.Info.ProcessType);         // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
////                                        showMessage(BHPreference.processTypeOfEmployee());
//
//                                        List<UserInfo> userPositionList = result.Info.UserPosition;
//                                        if (userPositionList != null) {
//                                            String strPositionCode = "";
//                                            String strPositionName = "";
//                                            String strSaleCode = "";
//                                            String strCashCode = "";
//
//                                            for (UserInfo userInfo : userPositionList) {
//                                                strPositionCode += userInfo.PositionCode;
//                                                strPositionCode += ",";
//
//                                                strPositionName += userInfo.PositionName;
//                                                strPositionName += ",";
//
//                                                if (userInfo.SaleCode != null) {
//                                                    strSaleCode = userInfo.SaleCode;
//                                                }
//                                                if ((userInfo.SaleCode != null) && (strSourceSystem.equals("Credit"))) {
//                                                    strCashCode = userInfo.SaleCode;
//                                                }
//                                            }
//                                            strPositionCode = strPositionCode.substring(0, strPositionCode.length() - 1);
//                                            BHPreference.setPositionCode(strPositionCode);
//
//                                            strPositionName = strPositionName.substring(0, strPositionName.length() - 1);
//                                            BHPreference.setPositionName(strPositionName);
//
//                                            BHPreference.setSaleCode(strSaleCode);
//                                            BHPreference.setCashCode(strCashCode);
//                                        }
//
//                                        /*** [START] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ ***/
//                                        BHPreference.setDateFormatGenerateDocument(result.Info.DateFormatGenerateDocument);
//                                        BHPreference.setRunningNumberReceipt(result.Info.RunningNumberReceipt);
//                                        BHPreference.setRunningNumberChangeContract(result.Info.RunningNumberChangeContract);
//                                        BHPreference.setRunningNumberReturnProduct(result.Info.RunningNumberReturnProduct);
//                                        BHPreference.setRunningNumberImpoundProduct(result.Info.RunningNumberImpoundProduct);
//                                        BHPreference.setRunningNumberChangeProduct(result.Info.RunningNumberChangeProduct);
//                                        BHPreference.setRunningNumberComplain(result.Info.RunningNumberComplain);
//                                        /*** [END] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ   ***/

                                        // YIM use this method for move comment code
                                        BHPreference.initPreference(result.Info,outputGetCurrentFortnight);

                                        startSynchronize();
                                    }
                                }
                            }
                        }).start();
                    }
                }
            }).execute(BHPreference.TSR_SERVICE_URL);
        } else {
            showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
        }
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

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

    private void startSynchronize() {
        TransactionService.stopService(activity);
        SynchronizeReceiver.getInstance().start();

        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
        request.master.syncFullRelated = true;

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);
    }

    private static class SynchronizeReceiver extends BroadcastReceiver {
        private static SynchronizeReceiver instance;

        private ProgressDialog dialog;
        private SynchronizeService.SynchronizeResult result;
        private boolean isProcessing;

        private SynchronizeReceiver() {
            dialog = new ProgressDialog(activity);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setTitle("");
            dialog.setMessage("");

            result = null;
            isProcessing = false;
        }

        public static SynchronizeReceiver getInstance() {
            if (instance == null) {
                instance = new SynchronizeReceiver();
                LocalBroadcastManager.getInstance(activity).registerReceiver(instance, new IntentFilter(SynchronizeService.SYNCHRONIZE_BROADCAST_ACTION));
            }

            return instance;
        }

        public void show() {
            if (isProcessing && !dialog.isShowing()) {
                dialog.show();
            }
        }

        private void start() {
            if (!isProcessing) {
                isProcessing = true;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        }

        private void stop() {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(this);
            isProcessing = false;
            dialog = null;
            result = null;
            instance = null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            if (intent != null) {
                result = intent.getParcelableExtra(SynchronizeService.SYNCHRONIZE_RESULT_DATA_KEY);
                dialog.setTitle(result.title);
                dialog.setMessage(result.message);
                dialog.setProgress(result.progress);

                if (result.progress >= 100) {
                    dialog.setProgress(100);
                }

                if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED || result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                    dialog.dismiss();
                    if (result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                        showWarningDialog(result.error);
                    } else {
                        if(result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED){
                            MainActivity.checkLogin = true;
                            //
                            final MainActivity.DownloadTask downloadTask = new MainActivity.DownloadTask(activity);
                            if(BHGeneral.isOpenDepartmentSignature){
                                String[] URL = {
                                        String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip"),
                                        String.format("%s/%s", BHPreference.TSR_SERVICE_URL, "GetSignatureImages")
                                };
                                downloadTask.execute(URL);
                            }else{
                                String URL = String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip");
                                downloadTask.execute(URL);
                            }

                            downloadTask.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    }
                    stop();
                }

            }
        }

    }

}
