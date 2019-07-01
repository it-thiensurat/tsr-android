//package th.co.thiensurat.service;
//
//import android.app.Activity;
//import android.app.IntentService;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.PowerManager;
//import android.support.v4.content.WakefulBroadcastReceiver;
//import android.util.Log;
//
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//import th.co.bighead.utilities.BHGeneral;
//import th.co.bighead.utilities.BHPreference;
//import th.co.thiensurat.R;
//import th.co.thiensurat.activities.MainActivity;
//import th.co.thiensurat.business.controller.TSRController;
//import th.co.thiensurat.data.controller.ContractController;
//import th.co.thiensurat.data.controller.GCMProdStkAndContractController;
//import th.co.thiensurat.data.controller.ProductStockController;
//import th.co.thiensurat.data.controller.UserController;
//import th.co.thiensurat.data.info.ContractInfo;
//import th.co.thiensurat.data.info.GCMProdStkAndContractInfo;
//import th.co.thiensurat.data.info.ProductStockInfo;
//import th.co.thiensurat.data.info.TransactionLogInfo;
//import th.co.thiensurat.service.data.AddUserDeviceLogInputInfo;
//import th.co.thiensurat.service.data.UpdateGCMProdStkAndContractInputInfo;
//
///**
// * Created by Annop on 19/12/2557.
// */
//public class GCMService extends IntentService {
//
//    public enum GCMServiceType {
//        IMPOUND_REQUEST, IMPOUND_APPROVED, LOGOUT_USER_INACTIVE_FORCE_LOGIN_FALSE, GCM_PRODUCTSTOCK_AND_CONTRACT
//    }
//
//    public static class GCMReceiver extends WakefulBroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Explicitly specify that GcmIntentService will handle the intent.
//            ComponentName comp = new ComponentName(context.getPackageName(), GCMService.class.getName());
//            // Start the service, keeping the device awake while it is launching.
//            startWakefulService(context, (intent.setComponent(comp)));
//            setResultCode(Activity.RESULT_OK);
//        }
//    }
//
//    public static final int NOTIFICATION_ID = 1;
//    private static final String TAG = "GCM";
//
//    public GCMService() {
//        super("GcmIntentService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//        // The getMessageType() intent parameter must be the intent you received
//        // in your BroadcastReceiver.
//        String messageType = gcm.getMessageType(intent);
//
//        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
//            /*
//             * Filter messages based on message type. Since it is likely that GCM
//             * will be extended in the future with new message types, just ignore
//             * any message types you're not interested in, or that you don't
//             * recognize.
//             */
////            if (GoogleCloudMessaging.
////                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
////                sendNotification("Send error: " + extras.toString());
////            } else if (GoogleCloudMessaging.
////                    MESSAGE_TYPE_DELETED.equals(messageType)) {
////                sendNotification("Deleted messages on server: " +
////                        extras.toString());
////                // If it's a regular GCM message, do some work.
////            } else
//            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                // This loop represents the service doing some work.
////                for (int i = 0; i < 5; i++) {
////                    Log.i(TAG, "Working... " + (i + 1)
////                            + "/5 @ " + SystemClock.elapsedRealtime());
////                    try {
////                        Thread.sleep(500);
////                    } catch (InterruptedException e) {
////                    }
////                }
////                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
//                // Post notification of received message.
//                String type = extras.getString("messageType");
//                String productSerialNo = extras.getString("productSerialNo");
//                String title = null;
//                String message = null;
//
//                if (type != null) {
//                    /*if (type.equals("IMPOUND_REQUEST")) {
//                        title = "มีการร้องขอการถอดเครื่อง";
//                        message = String.format("หมายเลขเครื่อง: %s", productSerialNo);
//                    } else if (type.equals("IMPOUND_APPROVED")) {
//                        title = "การขอถอดเครื่องได้รับอนุมัติ";
//                        message = String.format("หมายเลขเครื่อง: %s", productSerialNo);
//                    } else if (type.equals(GCMServiceType.LOGOUT_USER_INACTIVE_FORCE_LOGIN_FALSE.toString())) {
//                        final String userID = extras.getString("userID");
//                        final String userName = extras.getString("userName");
//                        final String deviceID = extras.getString("deviceID");
//                        final String serviceMode = extras.getString("SERVICE_MODE");
//
//                        if(deviceID.equals(BHPreference.userDeviceId()) && serviceMode.equals(BHGeneral.SERVICE_MODE.toString())){
//                            BHPreference.setUserNotAllowLogin(UserController.LoginType.NOT_ALLOW.toString());
//                            List<TransactionLogInfo> trInfo = TSRController.getTransactionLogBySyncStatus(false);
//                            if(MainActivity.activity != null){
//                                if (trInfo == null) {
//                                    Log.e("GCMService", "Logout1");
//                                    MainActivity.activity.logout(userName, deviceID, AddUserDeviceLogInputInfo.UserDeviceLogProcessType.GCM_LOGOUT.toString());
//                                } else {
//                                    TransactionService.synchronizeTransactions(new TransactionService.TransactionServiceHandler() {
//                                        @Override
//                                        protected void onFinish(Exception e) {
//                                            Log.e("GCMService", "Logout2");
//                                            MainActivity.activity.logout(userName, deviceID, AddUserDeviceLogInputInfo.UserDeviceLogProcessType.GCM_LOGOUT.toString());
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    }*/
//
//
//                    switch (Enum.valueOf(GCMService.GCMServiceType.class, type)) {
//                        case IMPOUND_REQUEST:
//                            title = "มีการร้องขอการถอดเครื่อง";
//                            message = String.format("หมายเลขเครื่อง: %s", productSerialNo);
//                            break;
//                        case IMPOUND_APPROVED:
//                            title = "การขอถอดเครื่องได้รับอนุมัติ";
//                            message = String.format("หมายเลขเครื่อง: %s", productSerialNo);
//                            break;
//                        case LOGOUT_USER_INACTIVE_FORCE_LOGIN_FALSE:
//                            final String userID = extras.getString("userID");
//                            final String userName = extras.getString("userName");
//                            final String deviceID = extras.getString("deviceID");
//                            final String serviceMode = extras.getString("SERVICE_MODE");
//
//                            if (deviceID.equals(BHPreference.userDeviceId()) && serviceMode.equals(BHGeneral.SERVICE_MODE.toString())) {
//                                BHPreference.setUserNotAllowLogin(UserController.LoginType.NOT_ALLOW.toString());
//                                List<TransactionLogInfo> trInfo = TSRController.getTransactionLogBySyncStatus(false);
//                                if (MainActivity.activity != null) {
//                                    if (trInfo == null) {
//                                        Log.e("GCMService", "Logout1");
//                                        MainActivity.activity.logout(userName, deviceID, AddUserDeviceLogInputInfo.UserDeviceLogProcessType.GCM_LOGOUT.toString());
//                                    } else {
//                                        TransactionService.synchronizeTransactions(new TransactionService.TransactionServiceHandler() {
//                                            @Override
//                                            protected void onFinish(Exception e) {
//                                                if (e == null) {
//                                                    Log.e("GCMService", "Logout2");
//                                                    MainActivity.activity.logout(userName, deviceID, AddUserDeviceLogInputInfo.UserDeviceLogProcessType.GCM_LOGOUT.toString());
//                                                }
//                                            }
//                                        });
//                                    }
//                                }
//                            }
//                            break;
//                        case GCM_PRODUCTSTOCK_AND_CONTRACT:
//                            String gCMProdStkAndContractID = extras.getString("gCMProdStkAndContractID");
//                            String productSerialNumber = extras.getString("productSerialNumber");
//                            String organizationCode = extras.getString("organizationCode");
//                            String newSubTeamCode = extras.getString("newSubTeamCode");
//                            String newTeamCode = extras.getString("newTeamCode");
//                            String refNo = extras.getString("refNo");
//                            String SERVICE_MODE = extras.getString("SERVICE_MODE");
//
//                            if (SERVICE_MODE.equals(BHGeneral.SERVICE_MODE.toString())) {
//                                ProductStockInfo productStockInfo = new ProductStockController().getProductStockByProductSerialNumber(organizationCode, productSerialNumber);
//                                if (productStockInfo != null) {
//                                    if (!productStockInfo.TeamCode.equals(newSubTeamCode)) {
//                                        GCMProdStkAndContractInfo gcmProdStkAndContractInfo = new GCMProdStkAndContractInfo();
//                                        gcmProdStkAndContractInfo.GCMProdStkAndContractID = gCMProdStkAndContractID;
//                                        gcmProdStkAndContractInfo.ProductSerialNumber = productSerialNumber;
//                                        gcmProdStkAndContractInfo.OrganizationCode = organizationCode;
//                                        gcmProdStkAndContractInfo.ProdStkStatus = productStockInfo.Status;
//                                        //gcmProdStkAndContractInfo.ProductSerialNumber = productSerialNumber;
//                                        //gcmProdStkAndContractInfo.NewSubTeamCode = newSubTeamCode;
//                                        //gcmProdStkAndContractInfo.NewTeamCode = newTeamCode;
//
//                                        switch (Enum.valueOf(ProductStockController.ProductStockStatus.class, productStockInfo.Status)) {
//                                            case WAIT:
//                                            case OVER:
//                                            case CHECKED:
//                                                new ProductStockController().deleteProductStockBySerialNumber(organizationCode, productSerialNumber);
//                                                break;
//                                            case SOLD:
//                                                new ProductStockController().deleteProductStockBySerialNumber(organizationCode, productSerialNumber);
//
//                                                if (refNo.equals("")) {
//                                                    ContractInfo contractInfo = new ContractController().getContractByProductSerialNumber(organizationCode, productSerialNumber);
//                                                    if (contractInfo != null) {
//                                                        if(!contractInfo.SaleTeamCode.equals(newTeamCode)){
//                                                            new ContractController().updateContractForGCM(false, contractInfo.OrganizationCode, contractInfo.RefNo);
//
//                                                            gcmProdStkAndContractInfo.RefNo = contractInfo.RefNo;
//                                                            gcmProdStkAndContractInfo.LastUpdateDate = new Date();
//                                                            gcmProdStkAndContractInfo.LastUpdateBy = BHPreference.employeeID();
//                                                            UpdateGCMProdStkAndContractInputInfo updateGCMProdStkAndContractInputInfo = UpdateGCMProdStkAndContractInputInfo.from(gcmProdStkAndContractInfo);
//                                                            TSRService.updateGCMProdStkAndContract(updateGCMProdStkAndContractInputInfo, true);
//                                                        }
//                                                    }
//                                                } else {
//                                                    new ContractController().updateContractForGCM(false, organizationCode, refNo);
//                                                    gcmProdStkAndContractInfo.RefNo = refNo;
//                                                }
//                                                break;
//                                        }
//
//                                       if(gcmProdStkAndContractInfo.RefNo != null){
//                                           GCMProdStkAndContractInfo checkGCMProdStkAndContract =  new GCMProdStkAndContractController().getGCMProdStkAndContractByGCMProdStkAndContractID(gcmProdStkAndContractInfo.GCMProdStkAndContractID);
//
//                                           if(checkGCMProdStkAndContract == null){
//                                               new GCMProdStkAndContractController().addGCMProdStkAndContract(gcmProdStkAndContractInfo);
//                                           } else {
//                                               new GCMProdStkAndContractController().updateGCMProdStkAndContract(gcmProdStkAndContractInfo);
//                                           }
//                                       }
//                                    }
//                                }
//                            }
//                            break;
//                    }
//                }
//
//                if (title != null) {
//                    sendNotification(title, message);
//                }
//                Log.i(TAG, "Received: " + extras.toString());
//            }
//        }
//        // Release the wake lock provided by the WakefulBroadcastReceiver.
//        GCMReceiver.completeWakefulIntent(intent);
//        PowerManager pm = ((PowerManager) getSystemService(POWER_SERVICE));
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
//        wl.acquire(1000);
//    }
//
//    // Put the message into a notification and post it.
//    // This is just one simple example of what you might choose to do with
//    // a GCM message.
//    private void sendNotification(String title, String message) {
//        NotificationManager mNotificationManager = (NotificationManager)
//                this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);
//
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Notification n = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle(title)
//                .setStyle(new Notification.BigTextStyle()
//                        .bigText(message))
//                .setContentText(message)
//                .setSound(alarmSound)
//                .setContentIntent(contentIntent)
//                .setTicker(message)
//                .build();
//
//        mNotificationManager.notify(new Random().nextInt(), n);
//    }
//
//}
