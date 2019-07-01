package th.co.thiensurat.service.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.SplashActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());



        if (BHPreference.userID() != null && BHPreference.serviceMode().equals(BHGeneral.SERVICE_MODE.toString())) {
            // Check if message contains a data payload.
            Map<String, String> data = remoteMessage.getData();
            if (data.size() > 0) {
                Log.d(TAG, "Message data payload: " + data);

                setNotification(data);
                /*if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob(remoteMessage.getData());
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/
            }

            // Check if message contains a notification payload.
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            if (notification != null) {
                Log.d(TAG, "Message Notification Body: " + notification.getBody());
                sendNotification(notification.getTitle(), notification.getBody(), data);
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob(Map<String, String> data) {
        Bundle bundle = getBundle(data);

        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("tsr-job-fcm")
                .setExtras(bundle)
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        BHPreference.setUserDeviceID(token);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param message FCM message body received.
     */
    private void sendNotification(String title, String message, Map<String, String> data) {
        Bundle bundle = getBundle(data);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = new Random().nextInt(Integer.MAX_VALUE);
        //notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    private Bundle getBundle(Map<String, String> data) {
        Bundle bundle = new Bundle();
        if (data != null && data.size() > 0) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }

    public enum FCMServiceType {
        IMPOUND_REQUEST, IMPOUND_APPROVED, LOGOUT_USER_INACTIVE_FORCE_LOGIN_FALSE, GCM_PRODUCTSTOCK_AND_CONTRACT
    }

    private void setNotification(Map<String, String> data) {
        try {
            if (data != null && data.size() > 0) {

                String type = data.get("messageType");
                /*String title = null;
                String message = null;
                String productSerialNo = data.get("productSerialNo");*/

                if (type != null) {
                    switch (Enum.valueOf(MyFirebaseMessagingService.FCMServiceType.class, type)) {
                        /*case IMPOUND_REQUEST:
                            if (productSerialNo != null) {
                                title = "มีการร้องขอการถอดเครื่อง";
                                message = String.format("หมายเลขเครื่อง: %s", productSerialNo);
                                sendNotification(title, message);
                            }
                            break;

                        case IMPOUND_APPROVED:
                            if (productSerialNo != null) {
                                title = "การขอถอดเครื่องได้รับอนุมัติ";
                                message = String.format("หมายเลขเครื่อง: %s", productSerialNo);
                                sendNotification(title, message);
                            }
                            break;*/

                        case LOGOUT_USER_INACTIVE_FORCE_LOGIN_FALSE:
                        case GCM_PRODUCTSTOCK_AND_CONTRACT:
                            scheduleJob(data);
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error setNotification: " + ex.getMessage());
        }
    }


}