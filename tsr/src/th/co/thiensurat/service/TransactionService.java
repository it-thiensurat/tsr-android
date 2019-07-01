package th.co.thiensurat.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DatabaseManager;
import th.co.thiensurat.data.controller.TransactionLogController;
import th.co.thiensurat.data.info.TransactionLogInfo;
import th.co.thiensurat.data.info.TransactionLogSkipInfo;
import th.co.thiensurat.data.info.UserDeviceInfo;
import th.co.thiensurat.service.data.AddTransactionLogSkipInputInfo;
import th.co.thiensurat.service.data.GenericOutputInfo;
import th.co.thiensurat.service.data.UserDeviceOutputInfo;
import th.co.thiensurat.service.data.UserInputInfo;

public class TransactionService extends IntentService {
    public static abstract class TransactionServiceHandler {
        protected abstract void onFinish(Exception e);

        protected void onStartProcess() {
        }

        protected void onProcess(int percent) {
        }

        protected void onStartUpdate() {
        }

        protected void onUpdate(int percent) {
        }
    }

    public static class TransactionReceiver extends WakefulBroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            boolean connected = isInternetConnected(context);
            // if (action.equals(Context.CONNECTIVITY_SERVICE)) {
            //
            // } else if (action.equals(TRANSACTION_SCHEDULE)) {
            // if (connected) {
            //
            // }
            // }

            Log.i("synchronized", "action:" + action);

            if (connected) {
                if (action != null && action.equals(Context.CONNECTIVITY_SERVICE)) {
                    cancelAlarm(context);
                }

                // Explicitly specify that GcmIntentService will handle the intent.
                ComponentName comp = new ComponentName(context.getPackageName(), TransactionService.class.getName());
                // Start the service, keeping the device awake while it is launching.

                try {
                    startWakefulService(context, (intent.setComponent(comp)));
                }
                catch (Exception ex){

                }

//				Intent service = new Intent(context, TransactionService.class);
//				context.startService(service);
            } else {
                cancelAlarm(context);

                /*** [START] :: Fixed - [BHPROJ-0024-3216] :: [LINE-25/07/2016][Android-MainActivity] หากในเครื่องมีข้อมูลค้างอยู่ ที่ไม่สามารถ sync ข้อมูลไปที่ server ได้ ให้ระบบเปลี่ยนแถบ   ***/
                MainActivity.checkTransactionLog();
                /*** [END] :: Fixed - [BHPROJ-0024-3216] :: [LINE-25/07/2016][Android-MainActivity] หากในเครื่องมีข้อมูลค้างอยู่ ที่ไม่สามารถ sync ข้อมูลไปที่ server ได้ ให้ระบบเปลี่ยนแถบ  ***/
            }

            //setResultCode(Activity.RESULT_OK);

            // Toast.makeText(context, intent.getAction(),
            // Toast.LENGTH_SHORT).show();
        }

        // boolean alarmRunning = (PendingIntent.getBroadcast(context, 0,
        // alarm, PendingIntent.FLAG_NO_CREATE) != null);
        // if (alarmRunning == false) {
        // PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
        // 0, alarm, 0);
        // AlarmManager alarmManager = (AlarmManager)
        // context.getSystemService(Context.ALARM_SERVICE);
        // alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        // SystemClock.elapsedRealtime(), 1800000, pendingIntent);
        // }
    }

    public static final String TRANSACTION_SCHEDULE = "th.co.thiensurat.service.TRANSACTION_SCHEDULE";
    private static long[] waitingTime = {0, 10};//นาที
    private static int currentWaitingTimeIndex = 0;

    public TransactionService() {
        super("TransactionService");
    }

    private static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
        // Intent service = new Intent(context, TransactionService.class);
        // if (networkInfo != null && networkInfo.isConnected()) {
        // context.startService(service);
        // } else {
        // context.stopService(service);
        // }
    }

    private static long getWaitingTime(long time) {
        return System.currentTimeMillis() + time * 60 * 1000;
    }

    private static PendingIntent getExistAlarm(Context context) {
        Intent alarm = new Intent(context, TransactionReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarm, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent;
    }

    private static void startAlarm(Context context, long time) {
        Log.i("synchronized", "start alarm");
        Intent alarm = new Intent(context, TransactionReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarm, PendingIntent.FLAG_CANCEL_CURRENT);
        if (pendingIntent != null) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, getWaitingTime(time), pendingIntent);
        }
    }

    private static void cancelAlarm(Context context) {
        Log.i("synchronized", "cancel alarm");
        Intent alarm = new Intent(context, TransactionReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarm, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            pendingIntent.cancel();
            //AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //am.cancel(pendingIntent);
        }
    }

    public static void registerSchedule(Context context) {
        Log.i("synchronized", "registerSchedule");
        currentWaitingTimeIndex = 0;
        startAlarm(context, waitingTime[currentWaitingTimeIndex]);
        currentWaitingTimeIndex++;
    }

    public static void stopService(Context context) {
        cancelAlarm(context);
        Intent service = new Intent(context, TransactionService.class);
        context.stopService(service);
    }

    public synchronized static void synchronizeTransactions(TransactionServiceHandler handler) {
        if (handler != null) {
            handler.onStartProcess();
        }

        TransactionLogController controller = new TransactionLogController();
        List<TransactionLogInfo> trans = controller.getUnSyncTransactionLogs();
        Exception error = null;
        if (trans != null && trans.size() > 0) {
            List<Integer> successTransactions = new ArrayList<Integer>();
            try {
                /*UserInputInfo user = new UserInputInfo();
                user.UserName = BHPreference.userID();
                user.DeviceID = BHPreference.userDeviceId();
                UserDeviceInfo device = TSRService.getUserDevice(user, false).Info;

                boolean isSkipTransactionsLog = device != null && ((device.DeviceID != null && !device.DeviceID.equals(user.DeviceID)) || (device.LastLoginDeviceID != null && !device.LastLoginDeviceID.equals(user.DeviceID)));
                if (isSkipTransactionsLog) {
                    for (TransactionLogInfo tran : trans) {
                        if (!tran.ServiceName.equals("AddTransactionLogSkip")) {
                            tran.SyncStatus = true;
                            tran.SyncDate = new Date();
                            controller.updateSyncTransactionLog(tran);

                            TransactionLogSkipInfo info = new TransactionLogSkipInfo();
                            info.TransactionLogSkipID = DatabaseHelper.getUUID();
                            info.TransactionID = tran.TransactionID;
                            info.ServiceName = tran.ServiceName;
                            info.ServiceInputName = tran.ServiceInputName;
                            info.ServiceInputType = tran.ServiceInputType;
                            info.ServiceOutputType = tran.ServiceOutputType;
                            info.ServiceInputData = tran.ServiceInputData;
                            info.TransactionDate = tran.TransactionDate;
                            info.SyncStatus = tran.SyncStatus;
                            info.SyncDate = tran.SyncDate;
                            info.CreateDate = new Date();
                            info.CreateBy = BHPreference.employeeID();
                            info.DeviceID = BHPreference.userDeviceId();
                            TSRService.addTransactionLogSkip(AddTransactionLogSkipInputInfo.from(info), true);
                        }
                    }
                    trans.clear();
                    trans = controller.getUnSyncTransactionLogs();
                }

                for (TransactionLogInfo tran : trans) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }

                    GenericOutputInfo result = (GenericOutputInfo) TSRService.synchronizeTransaction(tran);
                    switch (result.ResultCode) {
                        case 0: //Success
                            successTransactions.add(Integer.valueOf(tran.TransactionID));
                            break;
                        case 2627: //duplicate key
                            successTransactions.add(Integer.valueOf(tran.TransactionID));
                            break;
                        default: //Error
                            error = new Exception(result.ResultDescription);
                            break;
                    }

                    if (error != null) {
                        break;
                    }

                    if (handler != null) {
                        handler.onProcess((int) Math.ceil(100f / trans.size()));
                    }
                }*/

                UserInputInfo user = new UserInputInfo();
                user.UserName = BHPreference.userID();
                user.DeviceID = BHPreference.userDeviceId();
                UserDeviceOutputInfo userDeviceOutput = TSRService.getUserDevice(user, false);

                if (userDeviceOutput != null) {
                    switch (userDeviceOutput.ResultCode) {
                        case 0:
                            UserDeviceInfo device = userDeviceOutput.Info;
                            if (device.LastLoginDeviceID != null) {
                                BHPreference.setCurrentServerDate(device.CurrentServerDate);

                                boolean isSkipTransactionsLog = !device.LastLoginDeviceID.equals(user.DeviceID);
                                if (isSkipTransactionsLog) {
                                    for (TransactionLogInfo tran : trans) {
                                        if (!tran.ServiceName.equals("AddTransactionLogSkip")
                                                 && !tran.ServiceName.equals("AddTransactionLogBackup")) {
                                            tran.SyncStatus = true;
                                            tran.SyncDate = new Date();
                                            controller.updateSyncTransactionLog(tran);

                                            TransactionLogSkipInfo info = new TransactionLogSkipInfo();
                                            info.TransactionLogSkipID = DatabaseHelper.getUUID();
                                            info.TransactionID = tran.TransactionID;
                                            info.ServiceName = tran.ServiceName;
                                            info.ServiceInputName = tran.ServiceInputName;
                                            info.ServiceInputType = tran.ServiceInputType;
                                            info.ServiceOutputType = tran.ServiceOutputType;
                                            info.ServiceInputData = tran.ServiceInputData;
                                            info.TransactionDate = tran.TransactionDate;
                                            info.SyncStatus = tran.SyncStatus;
                                            info.SyncDate = tran.SyncDate;
                                            info.CreateDate = new Date();
                                            info.CreateBy = BHPreference.employeeID();
                                            info.DeviceID = BHPreference.userDeviceId();
                                            TSRService.addTransactionLogSkip(AddTransactionLogSkipInputInfo.from(info), true);
                                        }
                                    }
                                    trans.clear();
                                    trans = controller.getUnSyncTransactionLogs();
                                }

                                /*for (TransactionLogInfo tran : trans) {
                                    if (Thread.currentThread().isInterrupted()) {
                                        break;
                                    }

                                    GenericOutputInfo result = (GenericOutputInfo) TSRService.synchronizeTransaction(tran);
                                    switch (result.ResultCode) {
                                        case 0: //Success
                                            successTransactions.add(Integer.valueOf(tran.TransactionID));
                                            break;
                                        case 2627: //duplicate key
                                            successTransactions.add(Integer.valueOf(tran.TransactionID));
                                            break;
                                        default: //Error
                                            error = new Exception(result.ResultDescription);
                                            break;
                                    }

                                    if (error != null) {
                                        break;
                                    }

                                    if (handler != null) {
                                        handler.onProcess((int) Math.ceil(100f / trans.size()));
                                    }
                                }*/

                                for(int i=0; i<trans.size(); i++) {
                                    TransactionLogInfo tran = trans.get(i);
                                    if (Thread.currentThread().isInterrupted()) {
                                        break;
                                    }

                                    GenericOutputInfo result = (GenericOutputInfo) TSRService.synchronizeTransaction(tran);
                                    switch (result.ResultCode) {
                                        case 0: //Success
                                            successTransactions.add(Integer.valueOf(tran.TransactionID));
                                            break;
                                        case 2627: //duplicate key
                                            successTransactions.add(Integer.valueOf(tran.TransactionID));
                                            break;
                                        default: //Error
                                            error = new Exception(result.ResultDescription);
                                            break;
                                    }

                                    if (error != null) {
                                        break;
                                    }

                                    if (handler != null) {
                                        handler.onProcess((int) Math.ceil((100f / trans.size()) * i));
                                    }
                                }

                            }
                            break;

                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = e;
            }

            if (successTransactions.size() > 0) {
                if (handler != null) {
                    handler.onStartUpdate();
                }
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

                try {
                    db.beginTransaction();
                    /*for (Integer id : successTransactions) {
                        controller.updateSyncTransactionLog(id.intValue());
                        if (handler != null) {
                            handler.onUpdate((int) Math.ceil(100f / successTransactions.size()));
                        }
                    }*/
                    for(int i=0; i<successTransactions.size(); i++) {
                        Integer id = successTransactions.get(i);
                        controller.updateSyncTransactionLog(id.intValue());
                        if (handler != null) {
                            handler.onUpdate((int) Math.ceil((100f / successTransactions.size()) * i));
                        }
                    }

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    error = ex;
                } finally {
                    db.endTransaction();
                    DatabaseManager.getInstance().closeDatabase();
                }
            }
        }

        if (handler != null) {
            handler.onFinish(error);
        }

        /*** [START] :: Fixed - [BHPROJ-0024-3216] :: [LINE-25/07/2016][Android-MainActivity] หากในเครื่องมีข้อมูลค้างอยู่ ที่ไม่สามารถ sync ข้อมูลไปที่ server ได้ ให้ระบบเปลี่ยนแถบ   ***/
        MainActivity.checkTransactionLog();
        /*** [END] :: Fixed - [BHPROJ-0024-3216] :: [LINE-25/07/2016][Android-MainActivity] หากในเครื่องมีข้อมูลค้างอยู่ ที่ไม่สามารถ sync ข้อมูลไปที่ server ได้ ให้ระบบเปลี่ยนแถบ  ***/
    }

    private void testThread() {
        for (int ii = 0; ii < 10000000; ii++) {
            if (Thread.currentThread().isInterrupted()) {
                showMessage("Thread interrupted.");
                break;
            }
            try {
//				Thread.sleep(1000);
                showMessage("run: " + (ii + 1));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
//				Thread.currentThread().interrupt();
            }
        }
    }

    private boolean isRunning;
    private Thread backgroundThread;

    private void showMessage(final String message) {
        Log.i("synchronized", message);
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        synchronizeTransactions(new TransactionServiceHandler() {

            @Override
            protected void onFinish(Exception e) {
                // TODO Auto-generated method stub
                TransactionLogController controller = new TransactionLogController();
                List<TransactionLogInfo> trans = controller.getUnSyncTransactionLogs();
                if (trans != null && trans.size() > 0) {
                    //showMessage("BEGIN SYNCHRONIZE");
                    Log.i("synchronized", "BEGIN SYNCHRONIZE");
                    startAlarm(getApplicationContext(), waitingTime[currentWaitingTimeIndex]);
                    TransactionReceiver.completeWakefulIntent(intent);
                } else {
                    //showMessage("END SYNCHRONIZE");
                    Log.i("synchronized", "END SYNCHRONIZE");
                    cancelAlarm(getApplicationContext());

                }
            }
        });

    }
//
//    @Override
//	public void onCreate() {
//		// TODO Auto-generated method stub
//		super.onCreate();
//		isRunning = false;
//		backgroundThread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				showMessage("BEGIN SYNCHRONIZE");
//				synchronizeTransactions(new TransactionServiceHandler() {
//
//					@Override
//					protected void onFinish(Exception e) {
//						// TODO Auto-generated method stub
//						startAlarm(getApplicationContext());
//					}
//				});
//
//				showMessage("END SYNCHRONIZE");
//				stopSelf();
//			}
//		});
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		// TODO Auto-generated method stub
//		showMessage("START SYNCHRONIZE SERVICE");
//		if (!isRunning) {
//			isRunning = true;
//			backgroundThread.start();
//		}
//
//		return super.onStartCommand(intent, flags, startId);
//	}
//
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		backgroundThread.interrupt();
//		isRunning = false;
//		showMessage("STOP SYNCHRONIZE SERVICE");
//		super.onDestroy();
//	}

}
