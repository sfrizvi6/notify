package com.example.android.notify;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView notificationsRecyclerView;
    private NotificationAdapter adapter;
    private NotificationReceiver notificationReceiver;
    private List<NotificationItemModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        data = new ArrayList<>();

        notificationReceiver = new NotificationReceiver();
        adapter = new NotificationAdapter(data);
        notificationsRecyclerView = findViewById(R.id.notification_list);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationsRecyclerView.setAdapter(adapter);

        IntentFilter filter = new IntentFilter();
        filter.addAction("notification_created");
        registerReceiver(notificationReceiver, filter);

        Intent intent = new Intent(this, NotifyListenerService.class);
        this.startService(intent);
    }

    public void createNotification(View v) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(this);
        notificationCompatBuilder.setContentTitle("Test Notification");
        notificationCompatBuilder.setContentText("Content for Test Notification");
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        notificationCompatBuilder.setAutoCancel(true);
        notificationManager.notify((int) System.currentTimeMillis(), notificationCompatBuilder.build());
    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            StatusBarNotification statusBarNotification =
                (StatusBarNotification) intent.getExtras().get("notification_created_event");
            if (statusBarNotification == null) {
                return;
            }
            Bundle extras = statusBarNotification.getNotification().extras;

            String packageName = statusBarNotification.getPackageName();
            if (packageName == null || packageName.equals("")) {
                return;
            }
            String title = extras.getString("android.title");
            String text = extras.getString("android.text");
            int icon = extras.getInt("android.icon");
            NotificationItemModel notificationItemModel =
                new NotificationItemModel(context, String.valueOf(statusBarNotification.getId()),
                                          icon,
                                          packageName,
                                          title,
                                          text,
                                          null,
                                          statusBarNotification.getPostTime());
            data.add(notificationItemModel);
            adapter.notifyDataSetChanged();
        }
    }
}
