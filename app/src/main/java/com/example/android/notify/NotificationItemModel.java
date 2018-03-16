package com.example.android.notify;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotificationItemModel {

    private static final String TAG = NotificationItemModel.class.getSimpleName();

    public final int id;
    public final String appName;
    public final int appIcon;
    public final String packageName;
    public final String title;
    public final String text;
    public final String timestamp;
    public final String textLines;

    private List<NotificationItemModel> subData;
    private NotificationSubAdapter subAdapter;

    public NotificationItemModel(Context context,
                                 int id,
                                 String appName,
                                 int appIcon,
                                 String packageName,
                                 String title,
                                 String text,
                                 long timestamp,
                                 String textLines) {
        this.id = id;
        this.appName = appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
        this.title = title;
        this.text = text;
        this.timestamp = DateUtils.formatDateTime(context, timestamp, DateUtils.FORMAT_SHOW_TIME);
        this.textLines = textLines;
        subData = new ArrayList<>();
        subAdapter = new NotificationSubAdapter(subData);
    }

    public void addSubNotificationData(NotificationItemModel subNotificationItemModel) {
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
