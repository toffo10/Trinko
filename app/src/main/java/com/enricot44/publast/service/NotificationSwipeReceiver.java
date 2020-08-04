package com.enricot44.publast.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class NotificationSwipeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, RememberToDrink.class);
        intent.setAction("StopService");
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
