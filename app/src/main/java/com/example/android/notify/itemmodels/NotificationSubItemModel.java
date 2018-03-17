package com.example.android.notify.itemmodels;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;

public class NotificationSubItemModel {

    private static final String TAG = NotificationSubItemModel.class.getSimpleName();

    final Context mContext;
    public final int mId;
    public final String mAppName;
    public final int mAppIcon;
    public final Bitmap mLargeIcon;
    public final String mPackageName;
    public final PendingIntent mPendingIntent;
    public final String mGroupKey;
    private String mTitle;
    private String mText;
    private String mTimestamp;
    private String mTextLines;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        this.mTimestamp = timestamp;
    }

    public String getTextLines() {
        return mTextLines;
    }

    public void setTextLines(String textLines) {
        this.mTextLines = textLines;
    }

    public NotificationSubItemModel(Context context,
                                    int id,
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
        this.mContext = context;
        this.mId = id;
        this.mAppName = appName;
        this.mAppIcon = appIcon;
        this.mLargeIcon = largeIcon;
        this.mPackageName = packageName;
        this.mPendingIntent = pendingIntent;
        this.mGroupKey = groupKey;
        this.mTitle = title;
        this.mText = text;
        this.mTimestamp = timestamp;
        this.mTextLines = textLines;
    }
}
