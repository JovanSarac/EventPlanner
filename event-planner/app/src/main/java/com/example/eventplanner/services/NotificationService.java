package com.example.eventplanner.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.HomeActivity;
import com.example.eventplanner.activities.UserInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    String TAG="NestoSeDesilo";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String title="";
        String body="";
        String topic="";

        Bundle bundle = remoteMessage.toIntent().getExtras();
        for (String key : bundle.keySet()) {
            if(key.equals("title")){
                Object value = bundle.get(key);
                if(value==null)return;
                title=value.toString();
            }
            else if(key.equals("body")){
                Object value = bundle.get(key);
                if(value==null)return;
                body=value.toString();
            }
            else if(key.equals("topic")){
                Object value = bundle.get(key);
                if(value==null)return;
                topic=value.toString();
            }
        }
        Log.d(TAG, "Message Notification Body: " + body);
        String notificationTitle = title;
        String notificationBody = body;
        String notifictionTopic = topic;

        sendNotification(notificationTitle, notificationBody, notifictionTopic);
        Log.d(TAG, "Usao u if");


    }
    private void sendNotification(String messageTitle, String messageBody, String messageTopic) {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();

        if (user != null) {

            if(user.getDisplayName().equals("PUPV")){
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_IMMUTABLE);
                String channelId = user.getUid();
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notificationBuilder;
                if(messageTitle.equals("Update on your report")){
                    notificationBuilder = new NotificationCompat.Builder(this, channelId + "Topic");
                    notificationBuilder.setContentTitle(messageTitle);
                    notificationBuilder.setContentText(messageBody);
                    notificationBuilder.setSmallIcon(R.drawable.ic_android);
                    notificationBuilder.setAutoCancel(true);
                    notificationBuilder.setSound(defaultSoundUri);
                }
                else{
                    notificationBuilder = new NotificationCompat.Builder(this, channelId)
                                    .setSmallIcon(R.drawable.ic_add)
                                    .setContentTitle(messageTitle)
                                    .setContentText(messageBody)
                                    .setAutoCancel(true)
                                    .setSound(defaultSoundUri)
                                    .setContentIntent(pendingIntent);
                }


                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId + "Topic",
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }
            else if(user.getDisplayName().equals("ADMIN")){
                if(messageTitle.equals("New company report") || messageTitle.equals("New user report")) {
                    String channelId = "AdminChannel";
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
                    builder.setContentTitle(messageTitle);
                    builder.setContentText(messageBody);
                    builder.setSmallIcon(R.drawable.ic_android);
                    builder.setAutoCancel(true);
                    builder.setSound(defaultSoundUri);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("AdminChannel", "AdminChannel", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(channel);
                    }

                    notificationManager.notify(0, builder.build());
                }
            }
        }

    }

}