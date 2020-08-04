package com.enricot44.publast.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.enricot44.publast.R;
import com.enricot44.publast.view.activity.MainActivity;

public class RememberToDrink extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent notificationCancelIntent = new Intent(this, NotificationSwipeReceiver.class);
        PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(this, 0, notificationCancelIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Remember To Drink!")
                .setContentText(input)
                .setSmallIcon(R.drawable.trinko_notification_icon)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingCancelIntent)
                .setOngoing(false)
                .build();
        startForeground(100, notification);

        if (intent.getAction() != null && intent.getAction().equals("StopService")) {
            stopForeground(true);
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            serviceChannel.setDescription("Remember to drink");
            serviceChannel.enableLights(true);
            serviceChannel.setLightColor(Color.BLUE);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
