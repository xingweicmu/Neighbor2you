package com.cmu.neighbor2you.ui;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.cmu.neighbor2you.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xing on 4/24/15.
 */
public class GcmIntentService extends IntentService {
    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    private int numMessages = 0;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

//                showToast(extras.getString("message"));
                displayNotification(extras.getString("message"));
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void displayNotification(final String message) {
        Log.i("Start", "notification");

      /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder =
                new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("New Message from Neighbor2you");
        mBuilder.setContentText(message);
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.notification_icon);
        mBuilder.setAutoCancel(true);

      /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);

      /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, PostedRequestActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PostedRequestActivity.class);

      /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(notificationID, mBuilder.build());
    }
}