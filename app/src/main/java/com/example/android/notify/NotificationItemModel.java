package com.example.android.notify;

import android.content.Context;
import android.text.format.DateUtils;

public class NotificationItemModel {

    String id;
    int imageId;
    String packageName;
    String title;
    String text;
    String imageUrl;
    String timestamp;

    public NotificationItemModel(Context context,
                                 String id,
                                 int imageId,
                                 String packageName,
                                 String title,
                                 String text,
                                 String imageUrl,
                                 long timestamp) {
        if (id == null) {
            throw new IllegalArgumentException("Notification data is incomplete!");
        }
        this.id = id;
        this.imageId = imageId;
        this.packageName = packageName;
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.timestamp = DateUtils.formatDateTime(context, timestamp, DateUtils.FORMAT_SHOW_TIME);
    }
}
