package com.example.android.notify;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotifyListenerService extends android.service.notification.NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
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
        intent.putExtra("notification_created_event", statusBarNotification);
        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {}
}