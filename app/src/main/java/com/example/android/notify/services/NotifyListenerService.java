package com.example.android.notify.services;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class NotifyListenerService extends android.service.notification.NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Notification service started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if (statusBarNotification == null) {
            return;
        }
        Log.i(TAG, "Notification Arrived:\nID: " + statusBarNotification.getId());
        Intent intent = new Intent("notification_created");

        // TODO: write custom Bundle de-/serialization because parcel data for
        // "android.wearable.EXTENSIONS" for Gmail notifications specifically causes crashes
        statusBarNotification.getNotification().extras.putString("android.wearable.EXTENSIONS", null);

        intent.putExtra("com.example.notify.notification_created_event", statusBarNotification);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {}
}