package com.example.android.notify;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        notificationsRecyclerView.setAdapter(adapter);

        IntentFilter filter = new IntentFilter();
        filter.addAction("notification_created");
        registerReceiver(notificationReceiver, filter);
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
            ApplicationInfo appInfo = (ApplicationInfo) extras.get("android.appInfo");
            PackageManager packageManager = getApplicationContext().getPackageManager();
            String appName = packageManager.getApplicationLabel(appInfo).toString();
            String title = extras.getString("android.title");
            String text = extras.getString("android.text");
            CharSequence[] textLines = (CharSequence[]) extras.get("android.textLines");
            StringBuilder textLinesString = new StringBuilder();
            if (textLines != null) {
                for (int i = 0; i < textLines.length; i++) {
                    textLinesString.append(textLines[i]).append(i < textLines.length - 1 ? "\n" : "");
                }
            }
            int icon = extras.getInt("android.icon");
            NotificationItemModel notificationItemModel =
                new NotificationItemModel(context, statusBarNotification.getId(),
                                          appName,
                                          icon,
                                          packageName,
                                          title,
                                          text,
                                          statusBarNotification.getPostTime(),
                                          textLinesString.toString());

            // go through the list to see if the notification with that ID already exists
            // if so, update that notification to position 0
            int position = -1;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).id == notificationItemModel.id) {
                    position = i;
                }
            }
            if (position >= 0 && position < data.size()) {
                data.remove(position);
            }
            data.add(0, notificationItemModel);
            adapter.notifyDataSetChanged();
        }
    }
}
