package com.example.android.notify.itemmodels;

import android.content.Context;

public class NotificationSubItemModel {

    private static final String TAG = NotificationSubItemModel.class.getSimpleName();

    final Context context;
    public final int id;
    public final String appName;
    public final int appIcon;
    public final String packageName;
    private String title;
    private String text;
    private String timestamp;
    private String textLines;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTextLines() {
        return textLines;
    }

    public void setTextLines(String textLines) {
        this.textLines = textLines;
    }

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
