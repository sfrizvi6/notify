package com.example.android.notify;

import android.content.Context;
import android.text.format.DateUtils;

public class NotificationItemModel {

    private static final String TAG = NotificationItemModel.class.getSimpleName();

    int id;
    String appName;
    int appIcon;
    String packageName;
    String title;
    String text;
    String timestamp;
    String textLines;

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
    }
}
