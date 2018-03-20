package com.example.android.notify.loader;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import com.example.android.notify.data.NotificationContract.NotificationEntry;
import com.example.android.notify.data.NotificationsDbHelper;
import com.example.android.notify.itemmodels.NotificationSubItemModel;
import com.example.android.notify.utils.NotificationCategory;

import java.util.ArrayList;
import java.util.List;

public class RecreateNotificationLoader extends AsyncTaskLoader<List<NotificationSubItemModel>> {

    private NotificationsDbHelper mDbHelper;

    public RecreateNotificationLoader(Context context, NotificationsDbHelper dbHelper) {
        super(context);
        mDbHelper = dbHelper;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NotificationSubItemModel> loadInBackground() {
        Cursor cursor = mDbHelper.queryAllNotifications();

        if (cursor == null) {
            return null;
        }

        List<NotificationSubItemModel> notificationsToRecreateList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_NOTIFICATION_ID));
            String category = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_NOTIFICATION_CATEGORY));
            String appName = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_APP_NAME));
            int icon = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_APP_ICON));
            int color = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_APP_ICON_COLOR));
            byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(NotificationEntry.COLUMN_APP_LARGE_ICON));
            Bitmap largeIcon = null;
            if (imageByteArray != null) {
                largeIcon = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            }
            String packageName = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_APP_PACKAGE_NAME));
            // TODO: figure pendingIntent out; pending intent is not supposed to persist outside of the OS process
            // because PendingIntent enclose a Binder object, they cannot be simply written to a parcel and marshalled
            String groupKey = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_GROUP_KEY));
            String title = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TITLE));
            String text = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TEXT));
            String timestamp = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TIMESTAMP));
            String textLinesString = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TEXTLINES));
            textLinesString = textLinesString == null || textLinesString.length() < 1 || textLinesString.equals("null")
                              ? ""
                              : textLinesString;

            notificationsToRecreateList.add(new NotificationSubItemModel(getContext(),
                                                                         id,
                                                                         NotificationCategory.getCategory(category),
                                                                         appName,
                                                                         icon,
                                                                         color,
                                                                         largeIcon,
                                                                         packageName,
                                                                         null,
                                                                         groupKey,
                                                                         title,
                                                                         text,
                                                                         timestamp,
                                                                         textLinesString));
        }
        return notificationsToRecreateList;
    }
}
