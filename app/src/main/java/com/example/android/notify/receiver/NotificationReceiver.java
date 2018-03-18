package com.example.android.notify.receiver;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.format.DateUtils;
import android.util.Log;
import com.example.android.notify.adapters.NotificationAdapter;
import com.example.android.notify.data.NotificationsDbHelper;
import com.example.android.notify.itemmodels.NotificationItemModel;
import com.example.android.notify.itemmodels.NotificationSubItemModel;
import com.example.android.notify.utils.NotificationCategory;
import com.example.android.notify.utils.NotificationUpdateState;

import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationReceiver.class.getSimpleName();

    private NotificationAdapter mAdapter;
    private NotificationsDbHelper mDbHelper;
    private List<NotificationItemModel> mData;
    private static SQLiteDatabase mDb;

    public NotificationReceiver(SQLiteDatabase db,
                                NotificationsDbHelper dbHelper,
                                List<NotificationItemModel> data,
                                NotificationAdapter adapter) {
        mDb = db;
        mDbHelper = dbHelper;
        mData = data;
        mAdapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        StatusBarNotification statusBarNotification =
            (StatusBarNotification) intent.getExtras().get("com.example.notify.notification_created_event");
        if (statusBarNotification == null) {
            return;
        }
        Bundle extras = statusBarNotification.getNotification().extras;

        String packageName = statusBarNotification.getPackageName();
        if (packageName == null || packageName.equals("")) {
            return;
        }

        String groupKey = statusBarNotification.getGroupKey();
        ApplicationInfo appInfo = (ApplicationInfo) extras.get("android.appInfo");
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        String category = statusBarNotification.getNotification().category;
        String appName = packageManager.getApplicationLabel(appInfo).toString();
        PendingIntent deepLinkIntent = statusBarNotification.getNotification().contentIntent;
        String title = extras.getString("android.title");
            /*
             * TODO: Key android.text expected String but value was a android.text.SpannableString.
             * The default value <null> was returned.
             * java.lang.ClassCastException: android.text.SpannableString cannot be cast to java.lang.String
             */
        String text = String.valueOf(extras.get("android.text"));
        CharSequence[] textLines = (CharSequence[]) extras.get("android.textLines");

        StringBuilder textLinesString = new StringBuilder();
        if (textLines != null) {
            for (int i = 0; i < textLines.length; i++) {
                textLinesString.append(textLines[i]).append(i < textLines.length - 1 ? "\n" : "");
            }
        }
        int icon = extras.getInt("android.icon");
        Bitmap largeIcon = (Bitmap) extras.get("android.largeIcon");
        int notificationId = statusBarNotification.getId();
        String timestamp = DateUtils.formatDateTime(context,
                                                    statusBarNotification.getPostTime(),
                                                    DateUtils.FORMAT_SHOW_TIME);
        NotificationSubItemModel incomingNotification =
            new NotificationSubItemModel(context, notificationId,
                                         NotificationCategory.getCategory(category),
                                         appName,
                                         icon,
                                         largeIcon,
                                         packageName,
                                         deepLinkIntent,
                                         groupKey,
                                         title,
                                         text,
                                         timestamp,
                                         textLinesString.toString());
        mDbHelper.persistNotification(mDb, incomingNotification);
        Log.e(TAG, updateNotificationSubCardIfNotificationPackageExists(incomingNotification).toString());
    }

    public NotificationUpdateState updateNotificationSubCardIfNotificationPackageExists(NotificationSubItemModel incomingNotification) {
        // go through the list to see if the notification with that groupKey already exists
        // if so, update that notification to position 0
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).mGroupKey.equals(incomingNotification.mGroupKey)) {
                int position = i;
                NotificationItemModel existingNotification = mData.get(position);

                // also check if the parent notification is the same mId
                // if so update the relevant fields of parent notification
                // and then update the position of the parent notification in the mAdapter
                if (existingNotification.mId == incomingNotification.mId) {
                    updateNotificationFields(existingNotification, incomingNotification);
                    mData.remove(position);
                    mData.add(0, existingNotification);
                    mAdapter.notifyDataSetChanged();
                    return NotificationUpdateState.PARENT_NOTIFICATION_UPDATED;
                }

                // if the incoming notification is not the same ID as parent notification
                // check if it is one of the sub-notifications
                List<NotificationSubItemModel> subData = existingNotification.getSubData();
                for (int j = 0; j < subData.size(); j++) {
                    if (subData.get(j).mId == incomingNotification.mId) {
                        int subPosition = j;
                        NotificationSubItemModel existingSubNotification = subData.get(subPosition);

                        // if notification already exists then update the relevant fields
                        // and then update the subPosition of the sub-notification in the mAdapter
                        updateNotificationFields(existingSubNotification, incomingNotification);
                        existingNotification.removeSubNotificationData(subPosition);
                        existingNotification.addSubNotificationData(existingSubNotification);

                        // now update the parent notification's timestamp and location in mAdapter
                        existingNotification.setTimestamp(incomingNotification.getTimestamp());
                        mData.remove(position);
                        mData.add(0, existingNotification);
                        mAdapter.notifyDataSetChanged();
                        return NotificationUpdateState.SUB_NOTIFICATION_UPDATED;
                    }
                }

                // if incoming notification's parent notification exists but sub-notification doesn't
                // add notification to existingNotification's sub-notification list
                existingNotification.addSubNotificationData(incomingNotification);
                existingNotification.setTimestamp(incomingNotification.getTimestamp());
                mData.remove(position);
                mData.add(0, existingNotification);
                mAdapter.notifyDataSetChanged();
                return NotificationUpdateState.NEW_SUB_NOTIFICATION_ADDED;
            }
        }

        // if neither parent nor sub-notification exists
        // create a new parent notification and add it to top of the mAdapter
        NotificationItemModel newNotification = new NotificationItemModel(incomingNotification);
        mData.add(0, newNotification);
        mAdapter.notifyDataSetChanged();
        return NotificationUpdateState.NEW_PARENT_NOTIFICATION_ADDED;
    }

    private void updateNotificationFields(NotificationSubItemModel existingNotification,
                                          NotificationSubItemModel incomingNotification) {
        existingNotification.setTitle(incomingNotification.getTitle());
        existingNotification.setText(incomingNotification.getText());
        existingNotification.setTextLines(incomingNotification.getTextLines());
        existingNotification.setTimestamp(incomingNotification.getTimestamp());
    }
}
