package com.example.android.notify;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import com.example.android.notify.adapters.NotificationAdapter;
import com.example.android.notify.itemmodels.NotificationItemModel;
import com.example.android.notify.itemmodels.NotificationSubItemModel;
import com.example.android.notify.utils.NotificationUpdateState;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private static final String TAG = NotificationsActivity.class.getSimpleName();

    private RecyclerView notificationsRecyclerView;
    private NotificationAdapter adapter;
    private NotificationReceiver notificationReceiver;
    private List<NotificationItemModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationReceiver = new NotificationReceiver();

        initNotificationListRecyclerView();

        IntentFilter filter = new IntentFilter();
        filter.addAction("notification_created");

        // Using LocalBroadcastManager instead of Context to registerReceiver and sendBroadcasts
        // to avoid exception: android.app.RemoteServiceException: can't deliver broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, filter);
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

    private void initNotificationListRecyclerView() {
        data = new ArrayList<>();
        adapter = new NotificationAdapter(data);
        notificationsRecyclerView = findViewById(R.id.notification_list);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        notificationsRecyclerView.setAdapter(adapter);
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
            Bitmap largeIcon = (Bitmap) extras.get("android.largeIcon");
            NotificationSubItemModel incomingNotification =
                new NotificationSubItemModel(context, statusBarNotification.getId(),
                                             appName,
                                             icon,
                                             largeIcon,
                                             packageName,
                                             title,
                                             text,
                                             DateUtils.formatDateTime(context,
                                                                      statusBarNotification.getPostTime(),
                                                                      DateUtils.FORMAT_SHOW_TIME),
                                             textLinesString.toString());
            Log.e(TAG, updateNotificationSubCardIfNotificationPackageExists(incomingNotification).toString());
        }

        private NotificationUpdateState updateNotificationSubCardIfNotificationPackageExists(NotificationSubItemModel incomingNotification) {
            // go through the list to see if the notification with that packageName already exists
            // if so, update that notification to position 0
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).packageName.equals(incomingNotification.packageName)) {
                    int position = i;
                    NotificationItemModel existingNotification = data.get(position);

                    // also check if the parent notification is the same id
                    // if so update the relevant fields of parent notification
                    // and then update the position of the parent notification in the adapter
                    if (existingNotification.id == incomingNotification.id) {
                        updateNotificationFields(existingNotification, incomingNotification);
                        data.remove(position);
                        data.add(0, existingNotification);
                        adapter.notifyDataSetChanged();
                        return NotificationUpdateState.PARENT_NOTIFICATION_UPDATED;
                    }

                    // if the incoming notification is not the same ID as parent notification
                    // check if it is one of the sub-notifications
                    List<NotificationSubItemModel> subData = existingNotification.getSubData();
                    for (int j = 0; j < subData.size(); j++) {
                        if (subData.get(j).id == incomingNotification.id) {
                            int subPosition = j;
                            NotificationSubItemModel existingSubNotification = subData.get(subPosition);

                            // if notification already exists then update the relevant fields
                            // and then update the subPosition of the sub-notification in the adapter
                            updateNotificationFields(existingSubNotification, incomingNotification);
                            existingNotification.removeSubNotificationData(subPosition);
                            existingNotification.addSubNotificationData(existingSubNotification);

                            // now update the parent notification's timestamp and location in adapter
                            existingNotification.setTimestamp(incomingNotification.getTimestamp());
                            data.remove(position);
                            data.add(0, existingNotification);
                            adapter.notifyDataSetChanged();
                            return NotificationUpdateState.SUB_NOTIFICATION_UPDATED;
                        }
                    }

                    // if incoming notification's parent notification exists but sub-notification doesn't
                    // add notification to existingNotification's sub-notification list
                    existingNotification.addSubNotificationData(incomingNotification);
                    existingNotification.setTimestamp(incomingNotification.getTimestamp());
                    data.remove(position);
                    data.add(0, existingNotification);
                    adapter.notifyDataSetChanged();
                    return NotificationUpdateState.NEW_SUB_NOTIFICATION_ADDED;
                }
            }

            // if neither parent nor sub-notification exists
            // create a new parent notification and add it to top of the adapter
            NotificationItemModel newNotification = new NotificationItemModel(incomingNotification);
            data.add(0, newNotification);
            adapter.notifyDataSetChanged();
            return NotificationUpdateState.NEW_PARENT_NOTIFICATION_ADDED;
        }

        private void updateNotificationFields(NotificationSubItemModel existingNotification, NotificationSubItemModel incomingNotification) {
            existingNotification.setTitle(incomingNotification.getTitle());
            existingNotification.setText(incomingNotification.getText());
            existingNotification.setTextLines(incomingNotification.getTextLines());
            existingNotification.setTimestamp(incomingNotification.getTimestamp());
        }
    }
}
