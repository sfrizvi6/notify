package com.example.android.notify.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import com.example.android.notify.NotificationsActivity;
import com.example.android.notify.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class TestingUtils {

    public final Context mContext;

    public TestingUtils(Context context) {
        this.mContext = context;
    }

    public void createNotification(View v) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        Notification notificationCompatBuilder = new Notification.Builder(mContext).setContentTitle("Test Notification")
                                                                                   .setCategory("social")
                                                                                   .setContentText(
                                                                                      "Content for Test Notification")
                                                                                   .setSmallIcon(R.drawable.ic_launcher_background)
                                                                                   .setAutoCancel(true)
                                                                                   .setContentIntent(PendingIntent.getActivity(
                                                                                       mContext,
                                                                                      0,
                                                                                      new Intent(mContext,
                                                                                                 NotificationsActivity.class),
                                                                                      PendingIntent.FLAG_UPDATE_CURRENT))
                                                                                   .build();


        notificationManager.notify((int) System.currentTimeMillis(), notificationCompatBuilder);
    }

    public void createInboxStyleNotification(View v) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        Notification notification = new Notification.Builder(mContext)
            .setGroup("speculooooos")
            .setContentTitle("5 New mails from sfrizvi6@gmail.com")
            .setContentText("Hello from the other side!")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_background))
            .setCategory("email")
            .setStyle(new Notification.InboxStyle()
                          .addLine("foo: bar")
                          .addLine("blah: blah blah")
                          .setSummaryText("+3 more"))
            .setContentIntent(PendingIntent.getActivity(mContext,
                                                        0,
                                                        new Intent(mContext, NotificationsActivity.class),
                                                        PendingIntent.FLAG_UPDATE_CURRENT))
            .build();
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}
