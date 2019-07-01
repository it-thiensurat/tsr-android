package th.co.thiensurat.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.TransactionLogController;
import th.co.thiensurat.data.controller.UserController;
import th.co.thiensurat.data.info.TransactionLogInfo;
import th.co.thiensurat.service.data.AddUserDeviceLogInputInfo;
import th.co.thiensurat.service.data.AuthenticateInputInfo;
import th.co.thiensurat.service.data.AuthenticateOutputInfo;
import th.co.thiensurat.service.data.CheckSoapOutputInfo;

public class TimeOutLoginService extends IntentService {

    private static long waitingTime = 10;//นาที

    public TimeOutLoginService() {
        super("TimeOutLoginService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Bundle extra = intent.getExtras();
        if(extra.get("intentName") != null && extra.get("intentName").equals("SuspendService"))
        {
            if(BHPreference.isSuspendServiceNotice()) {
                Log.i("SuspendServiceNotice", "BEGIN Notice");
                extra = intent.getExtras();
                Boolean fourceLogout = extra.getBoolean("fourceLogout");

                /*** [START] :: Fixed - https://fabric.io/bighead5/android/apps/th.co.thiensurat/issues/5c539484f8b88c29633835c6 ***/
                /*if (fourceLogout) {
                    SuspendServiceLogout();
                } else {
                    NoticeSuspendservice();
                }*/
                try {
                    if (fourceLogout) {
                        SuspendServiceLogout();
                    } else {
                        NoticeSuspendservice();
                    }
                }catch (Exception ex){}
                /*** [END] :: Fixed - https://fabric.io/bighead5/android/apps/th.co.thiensurat/issues/5c539484f8b88c29633835c6 ***/


            }
        } else {
            TransactionService.synchronizeTransactions(new TransactionService.TransactionServiceHandler() {
                @Override
                protected void onFinish(Exception e) {
                    if (BHPreference.TimeOutLogin()) {
                        TransactionLogController controller = new TransactionLogController();
                        List<TransactionLogInfo> trans = controller.getUnSyncTransactionLogs();

                        if (trans != null && trans.size() > 0) {
                            startAlarm(getApplicationContext());
                        } else {
                            Log.i("TimeOutLogin", "BEGIN Logout");
                            logout(BHPreference.userID(), BHPreference.userDeviceId(), AddUserDeviceLogInputInfo.UserDeviceLogProcessType.ANDROID_LOGOUT_TIMEOUTLOGIN.toString());
                        }
                    } else  {
                        Log.i("TimeOutLogin", "END Logout");
                        cancelAlarm(getApplicationContext());
                        cancelAlarmSuspendServiceNotice(getApplicationContext());
                    }

                }
            });
        }
    }


    public static class SuspendServiceNoticeReceiver extends  WakefulBroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            SimpleDateFormat formatNoticeDate = new SimpleDateFormat("HH:mm:ss");
            String strNoticeDate = formatNoticeDate.format(new Date());//"2015-06-26 07:25:00";
           // if(strNoticeDate == "23:59:00") {
                Log.i("SuspendServiceNotice", "SuspendServiceNotice : --");
                BHPreference.setSuspendServiceNotice(true);
                ComponentName comp = new ComponentName(context.getPackageName(), TimeOutLoginService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
                Toast.makeText(context, "ระบบกำลังทำการ แจ้งเตือนการปิดการใช้งาน", Toast.LENGTH_SHORT).show();
           // }
        }
    }

    public static class TimeOutLoginReceiver extends WakefulBroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            Log.i("TimeOutLogin", "action:" + action);

            Bundle extras = intent.getExtras();
            boolean isTimeOutLogin = extras.getBoolean("TimeOutLogin");
            if(isTimeOutLogin) {
                BHPreference.setTimeOutLogin(isTimeOutLogin);
            } else {
                if (!BHGeneral.DEVELOPER_MODE
                        && !BHPreference.IsAdmin()
                        && !BHPreference.TimeOutLogin()
                        && BHPreference.userID() != null) {
                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strLoginDate = BHPreference.LogLogin();
                    String strCurrentDate = formatDate.format(new Date());
                    try {
                        Date loginDate = formatDate.parse(strLoginDate);
                        Date currentDate = formatDate.parse(strCurrentDate);

                        Calendar logoutDate = Calendar.getInstance();
                        logoutDate.setTime(loginDate);
                        logoutDate.add(Calendar.HOUR, BHGeneral.TIME_OUT_LOGIN);
                        //test
                        //logoutDate.add(Calendar.MINUTE, BHGeneral.TIME_OUT_LOGIN);

                        if ((loginDate.before(currentDate) || loginDate.compareTo(currentDate) == 0) && currentDate.before(logoutDate.getTime())) {

                        } else {
                            BHPreference.setTimeOutLogin(true);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(BHPreference.TimeOutLogin()){
                boolean connected = isInternetConnected(context);
                Log.i("TimeOutLogin", "isInternetConnected : " + String.valueOf(connected));
                if (connected) {
                    ComponentName comp = new ComponentName(context.getPackageName(), TimeOutLoginService.class.getName());
                    startWakefulService(context, (intent.setComponent(comp)));
                    Toast.makeText(context, "ระบบกำลังทำการ Logout", Toast.LENGTH_SHORT).show();
                } else {
                    cancelAlarm(context);
                    cancelAlarmSuspendServiceNotice(context);
                }
            } else {
                Log.i("TimeOutLogin", "TimeOutLogin : " + String.valueOf(BHPreference.TimeOutLogin()));
            }
        }
    }

    public static void startAlarm(Context context) {
        startAlarm(context, waitingTime);
    }

    public static void startAlarm(Context context, long time) {
        Log.i("TimeOutLogin", "start alarm");
        Intent alarm = new Intent(context, TimeOutLoginReceiver.class);
        alarm.putExtra("TimeOutLogin", false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, alarm, PendingIntent.FLAG_CANCEL_CURRENT);
        if (pendingIntent != null) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, getWaitingTime(time), pendingIntent);
        }
    }

    public static void startAlarmTimeOutLogin(Context context, long time) {
        Log.i("TimeOutLogin", "start alarm login");
        Intent alarm = new Intent(context, TimeOutLoginReceiver.class);
        alarm.putExtra("TimeOutLogin", true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, alarm, PendingIntent.FLAG_CANCEL_CURRENT);
        if (pendingIntent != null) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    public static void cancelAlarm(Context context) {
        Log.i("TimeOutLogin", "cancel alarm");
        Intent alarm = new Intent(context, TimeOutLoginReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, alarm, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            pendingIntent.cancel();
        }

        /*Intent alarm = new Intent(context, TimeOutLoginReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, alarm, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);*/

    }

    public static void startAlarmSuspendServiceNotice(Context context, long time) {

        Log.i("SuspendServiceNotice","start Suspend Service Notice");
        Intent alarm = new Intent(context, SuspendServiceNoticeReceiver.class);
        alarm.putExtra("intentName","SuspendService");
        if(BHPreference.isSuspendServiceNotice()) {
            alarm.putExtra("fourceLogout",true);
        } else  {
            alarm.putExtra("fourceLogout",false);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 110, alarm, PendingIntent.FLAG_CANCEL_CURRENT);
        if(pendingIntent != null) {
            AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    public  static void cancelAlarmSuspendServiceNotice(Context context)
    {
        Log.i("SuspendServiceNotice","Stop Suspend Service Notice");
        Intent alarm = new Intent(context, SuspendServiceNoticeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 110, alarm, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            pendingIntent.cancel();
            BHPreference.setSuspendServiceNotice(false);
        }
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private static long getWaitingTime(long time) {
        return System.currentTimeMillis() + time * 60 * 1000;
    }

    public boolean checkAppRunning(){
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            Log.i("TimeOutLogin", packageInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("TimeOutLogin", "error : " + e.getMessage());
        }

        if(packageInfo != null) {
            Log.i("TimeOutLogin", "Api Level : " + String.valueOf(Build.VERSION.SDK_INT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager mgr = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.AppTask> tasks = mgr.getAppTasks();
                for (ActivityManager.AppTask task : tasks) {
                    if (task.getTaskInfo().baseIntent.getComponent().getPackageName().equals(packageInfo.packageName)) {
                        Log.i("TimeOutLogin", "App is running");
                        return true;
                    }
                }
            } else {
                ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equalsIgnoreCase(packageInfo.packageName)) {
                    Log.i("TimeOutLogin", "App is running");
                    return true;
                }
            }
        }

        Log.i("TimeOutLogin", "App is not running");
        return false;


    }

    public void NoticeSuspendservice(){
        if(checkAppRunning()){
            MainActivity.activity.ServiceSuspendNotice();
        }
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strNoticeFourceLogoutTime = BHPreference.getLogNoticeTime();//"2015-06-26 07:30:00";
        try {
            Date noticeFourceLogoutTime = formatDate.parse(strNoticeFourceLogoutTime);
            cancelAlarmSuspendServiceNotice(getApplicationContext());
            Calendar NoticeCalendar = Calendar.getInstance();
            NoticeCalendar.setTime(noticeFourceLogoutTime);
            BHPreference.setSuspendServiceNotice(true);
            startAlarmSuspendServiceNotice(getApplicationContext(), NoticeCalendar.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void SuspendServiceLogout(){
        if(checkAppRunning()){
            MainActivity.activity.SuspendServiceLogout();
        } else {
            Calendar logoutDate = Calendar.getInstance();
            TimeOutLoginService.cancelAlarm(getApplicationContext());
            TimeOutLoginService.startAlarmTimeOutLogin(getApplicationContext(), logoutDate.getTimeInMillis());
        }
    }

    public void logout(String userName, String deviceID, String userDeviceLogProcessType) {
        if(checkAppRunning()) {
            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
        }

        try {
            Log.i("TimeOutLogin", "logoutMainActivity");
            logoutMainActivity(userName, deviceID, userDeviceLogProcessType);
        } catch (Exception e){
            Log.i("TimeOutLogin", "logoutOnBackground");
            logoutOnBackground(userName, deviceID, userDeviceLogProcessType);
        }
    }

    private void logoutMainActivity(String userName, String deviceID, String userDeviceLogProcessType){
        MainActivity.activity.checkLogout = true;
        BHPreference.setTimeOutLogin(true);
        ///MainActivity.activity.StopAllAction(userName, deviceID, userDeviceLogProcessType);
        MainActivity.activity.logout(userName, deviceID, userDeviceLogProcessType);
    }

    private void logoutOnBackground(String userName, String deviceID, String userDeviceLogProcessType){
        CheckSoapOutputInfo checkSoapOutput;

        AuthenticateInputInfo input;
        AuthenticateOutputInfo output = null;
        AddUserDeviceLogInputInfo inputAddUserDeviceLog;

        input = new AuthenticateInputInfo();
        input.UserName = userName;
        input.DeviceID = deviceID;

        /*** [START] :: Fixed - [BHPROJ-0026-3271] [Back-End] เพิ่ม Field VersionCode ใน ตาราง [UserDevice] เพื่อเก็บว่า ณ ขณะนั้นใช้งาน TSR Mobile App version อะไรอยู่ (เป็น VersionCode ที่อยู่ใน AndroidManifest.xml) ***/
        /*int VersionCode = 0;
        try {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            VersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
        /*** [END] :: Fixed - [BHPROJ-0026-3271] [Back-End] เพิ่ม Field VersionCode ใน ตาราง [UserDevice] เพื่อเก็บว่า ณ ขณะนั้นใช้งาน TSR Mobile App version อะไรอยู่ (เป็น VersionCode ที่อยู่ใน AndroidManifest.xml)   ***/

        inputAddUserDeviceLog = new AddUserDeviceLogInputInfo();
        inputAddUserDeviceLog.UserName = userName;
        inputAddUserDeviceLog.DeviceID = deviceID;
        inputAddUserDeviceLog.ProcessType = userDeviceLogProcessType;
        //inputAddUserDeviceLog.VersionCode = VersionCode;
        inputAddUserDeviceLog.VersionCode = BHPreference.appVersionCode();
        inputAddUserDeviceLog.AndroidApiLevel = BHPreference.androidApiLevel();

        try {
            checkSoapOutput = TSRController.checkSoap();
            if (checkSoapOutput != null && checkSoapOutput.ResultCode == 0) {
                output = TSRController.logout(input);

                if (output != null) {
                    if (output.ResultCode == 0) {
                        TSRController.addUserDeviceLog(inputAddUserDeviceLog);
                    }
                }
            }
        } catch (Exception e) {
            Log.i("TimeOutLogin", "Error");
        }

        if (output != null) {
            if (output.ResultCode == 0) {
                BHPreference.setLastloginID(BHPreference.userID());
                BHPreference.setServiceMode(null);
                BHPreference.setUserID(null);
                BHPreference.setEmployeeID(null);
                BHPreference.setUserNotAllowLogin(UserController.LoginType.ALLOW.toString());
                BHPreference.setTimeOutLogin(false);
            }
        }

        if (BHPreference.TimeOutLogin()) {
            TimeOutLoginService.startAlarm(getApplicationContext());
        } else {
            Log.i("TimeOutLogin", "END Logout");
            cancelAlarm(getApplicationContext());
            cancelAlarmSuspendServiceNotice(getApplicationContext());
        }
    }
}

