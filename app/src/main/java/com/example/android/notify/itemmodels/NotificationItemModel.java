package com.example.android.notify.itemmodels;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import com.example.android.notify.adapters.NotificationSubAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationItemModel extends NotificationSubItemModel {

    private static final String TAG = NotificationItemModel.class.getSimpleName();

    private List<NotificationSubItemModel> subData;
    private NotificationSubAdapter subAdapter;

    public NotificationItemModel(Context context,
                                 int id,
                                 String appName,
                                 int appIcon,
                                 String packageName,
                                 String groupKey,
                                 PendingIntent pendingIntent,
                                 String title,
                                 String text,
                                 String timestamp,
                                 String textLines) {
        super(context,
              id,
              appName,
              appIcon,
              null,
              packageName,
              pendingIntent,
              groupKey,
              title,
              text,
              timestamp,
              textLines);
        subData = new ArrayList<>();
        subAdapter = new NotificationSubAdapter(subData);
    }

    public NotificationItemModel(NotificationSubItemModel notificationSubItemModel) {
        super(notificationSubItemModel.mContext,
              notificationSubItemModel.mId,
              notificationSubItemModel.appName,
              notificationSubItemModel.appIcon,
              notificationSubItemModel.largeIcon,
              notificationSubItemModel.packageName,
              notificationSubItemModel.pendingIntent,
              notificationSubItemModel.groupKey,
              notificationSubItemModel.getTitle(),
              notificationSubItemModel.getText(),
              notificationSubItemModel.getTimestamp(),
              notificationSubItemModel.getTextLines());
        subData = new ArrayList<>();
        subAdapter = new NotificationSubAdapter(subData);
    }

    public void addSubNotificationData(NotificationSubItemModel subNotificationItemModel) {
        subData.add(0, subNotificationItemModel);
        subAdapter.notifyDataSetChanged();
    }

    public void removeSubNotificationData(int position) {
        if (position < 0 && position >= subData.size()) {
            Log.e(TAG, "Sub-adapter position# " + position + " doesn't exist!");
        }
        subData.remove(position);
        subAdapter.notifyDataSetChanged();
    }

    public NotificationSubAdapter getSubAdapter() {
        return subAdapter;
    }

    public List<NotificationSubItemModel> getSubData() {
        return subData;
    }
}
