package com.example.android.notify;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

public class NotificationItemModel {

    private static final String TAG = NotificationItemModel.class.getSimpleName();

    String id;
    String appName;
    int appIcon;
    String packageName;
    String title;
    String text;
    String timestamp;
    String textLines;

    public NotificationItemModel(Context context,
                                 String id,
                                 String appName,
                                 int appIcon,
                                 String packageName,
                                 String title,
                                 String text,
                                 long timestamp,
                                 String textLines) {
        if (id == null) {
            Log.e(TAG, "Notification data is incomplete!");
        }
        this.id = id;
        this.appName = appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
        this.title = title;
        this.text = text;
        this.timestamp = DateUtils.formatDateTime(context, timestamp, DateUtils.FORMAT_SHOW_TIME);
        this.textLines = textLines;
    }
}
