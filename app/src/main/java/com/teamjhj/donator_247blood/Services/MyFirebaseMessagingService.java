package com.teamjhj.donator_247blood.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.teamjhj.donator_247blood.Activity.LoadingScreenActivity;
import com.teamjhj.donator_247blood.Activity.MainActivity;
import com.teamjhj.donator_247blood.R;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("Token Updated", s);

        //updateTokenToDatabase(s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String tag="Nothing",tittle="Donator",body="Blank";
//        tag = remoteMessage.getNotification().getTag();
//        String tittle = remoteMessage.getNotification().getTitle();
//        //String tittle=remoteMessage.getData().get("tittle");
//        String body = remoteMessage.getNotification().getBody();
        if(remoteMessage.getData().size()>0)
        {

            tag = remoteMessage.getData().get("tag");
            tittle=remoteMessage.getData().get("title");
            body=remoteMessage.getData().get("body");
        }

        Log.d("FirebaseFuck",tag);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, LoadingScreenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mobile", tag);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int j = new Random().nextInt();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LatestDevicesNotificationService latest = new LatestDevicesNotificationService(this);
            Notification.Builder builder = latest.getLatest(tittle, body, pendingIntent, defaultSound);
            int i = 0;
            if (j > 0) {
                i = j;
            }

            latest.getManager().notify(i, builder.build());

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(tittle)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle());
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int i = 0;
            if (j > 0) {
                i = j;
            }
            assert notificationManager != null;
            notificationManager.notify(i, builder.build());
        }


    }


}