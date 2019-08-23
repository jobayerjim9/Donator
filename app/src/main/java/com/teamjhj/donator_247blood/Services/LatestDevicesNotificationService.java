package com.teamjhj.donator_247blood.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.teamjhj.donator_247blood.R;

public class LatestDevicesNotificationService extends ContextWrapper {
    private static final String Channel_Id = "com.teamjhj.donator_247blood";
    private static final String Channel_Name = "Donator";
    private NotificationManager manager;

    public LatestDevicesNotificationService(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(Channel_Id, Channel_Name, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }

        return manager;
    }


    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getLatest(String tittle, String body, PendingIntent pendingIntent, Uri sound) {
        return new Notification.Builder(getApplicationContext(), Channel_Id)
                .setContentIntent(pendingIntent)
                .setContentTitle(tittle)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_notification)
                .setSound(sound)
                .setAutoCancel(true)
                .setStyle(new Notification.BigTextStyle());
    }
}
