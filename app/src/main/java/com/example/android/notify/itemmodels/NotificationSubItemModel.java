package com.example.android.notify.itemmodels;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;

public class NotificationSubItemModel {

    private static final String TAG = NotificationSubItemModel.class.getSimpleName();

    final Context mContext;
    public final int mId;
    public final String appName;
    public final int appIcon;
    public final Bitmap largeIcon;
    public final String packageName;
    public final PendingIntent pendingIntent;
    public final String groupKey;
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

    public NotificationSubItemModel(Context mContext,
                                    int mId,
                                    String appName,
                                    int appIcon,
                                    Bitmap largeIcon,
                                    String packageName,
                                    PendingIntent pendingIntent,
                                    String groupKey,
                                    String title,
                                    String text,
                                    String timestamp,
                                    String textLines) {
        this.mContext = mContext;
        this.mId = mId;
        this.appName = appName;
        this.appIcon = appIcon;
        this.largeIcon = largeIcon;
        this.packageName = packageName;
        this.pendingIntent = pendingIntent;
        this.groupKey = groupKey;
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
        this.textLines = textLines;
    }
}
