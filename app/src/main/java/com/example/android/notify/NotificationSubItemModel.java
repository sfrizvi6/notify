package com.example.android.notify;

import android.content.Context;

public class NotificationSubItemModel {

    private static final String TAG = NotificationSubItemModel.class.getSimpleName();

    public final Context context;
    public final int id;
    public final String appName;
    public final int appIcon;
    public final String packageName;
    public final String title;
    public final String text;
    public final String timestamp;
    public final String textLines;

    public NotificationSubItemModel(Context context,
                                    int id,
                                    String appName,
                                    int appIcon,
                                    String packageName,
                                    String title,
                                    String text,
                                    String timestamp,
                                    String textLines) {
        this.context = context;
        this.id = id;
        this.appName = appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
        this.textLines = textLines;
    }
}
