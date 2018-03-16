package com.example.android.notify.itemmodels;

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
                                 String title,
                                 String text,
                                 String timestamp,
                                 String textLines) {
        super(context, id, appName, appIcon, packageName, title, text, timestamp, textLines);
        subData = new ArrayList<>();
        subAdapter = new NotificationSubAdapter(subData);
    }

    public NotificationItemModel(NotificationSubItemModel notificationSubItemModel) {
        super(notificationSubItemModel.context,
              notificationSubItemModel.id,
              notificationSubItemModel.appName,
              notificationSubItemModel.appIcon,
              notificationSubItemModel.packageName,
              notificationSubItemModel.title,
              notificationSubItemModel.text,
              notificationSubItemModel.timestamp,
              notificationSubItemModel.textLines);
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
}
