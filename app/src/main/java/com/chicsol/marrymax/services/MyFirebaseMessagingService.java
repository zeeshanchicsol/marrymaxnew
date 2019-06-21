package com.chicsol.marrymax.services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chicsol.marrymax.R;
import com.chicsol.marrymax.activities.ActivityLogin;
import com.chicsol.marrymax.other.MarryMax;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by zeeshan on 11/10/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private NotificationManager notificationManager;
    private static final String ADMIN_CHANNEL_ID ="admin_channel";

    @Override
    public void onNewToken(String token) {
        Log.e("mRefreshed", " : " + token);
      Log.w("Refreshed", " : " + token);
        // If you want to send messages to  application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        MarryMax marryMax=new MarryMax(null);
        marryMax.sendRegistrationToServer(token,getApplicationContext());
      //  createNotification(token);

    }

    public void createNotification(String token) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, ActivityLogin.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText(token).setSmallIcon(R.drawable.ic_reset_icon)
                .setContentIntent(pIntent)
                .addAction(R.drawable.ic_reset_icon, "Call", pIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        //   Log.e(TAG, "From: " + remoteMessage.getFrom());
        //      Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBodyLocalizationKey());


        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
     //   if (remoteMessage.getData().size() > 0) {
                     if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //Setting up Notification channels for android O and above
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels();
            }
            int notificationId = new Random().nextInt(60000);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)  //a resource for your custom small icon
                    .setContentTitle(remoteMessage.getData().get("title")) //the "title" value you sent in your notification
                    .setContentText(remoteMessage.getData().get("message")) //ditto
                    .setAutoCancel(true)  //dismisses the notification on click
                    .setSound(defaultSoundUri);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());




          /*  if ( Check if data needs to be processed by long running job  true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //     scheduleJob();
            } else {
                // Handle message within 10 seconds
                //     handleNow();
            }
        }*/
            // Check if message contains a notification payload.
           /* if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }*/
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
  }