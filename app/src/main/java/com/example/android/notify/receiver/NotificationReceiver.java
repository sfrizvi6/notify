package com.example.android.notify.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import com.example.android.notify.adapters.NotificationAdapter;
import com.example.android.notify.data.NotificationsDbHelper;
import com.example.android.notify.itemmodels.NotificationItemModel;
import com.example.android.notify.itemmodels.NotificationSubItemModel;
import com.example.android.notify.loader.ParseNotificationLoader;
import com.example.android.notify.utils.NotificationUpdateState;

import java.util.List;

public class NotificationReceiver extends BroadcastReceiver
    implements LoaderManager.LoaderCallbacks<NotificationSubItemModel> {

    private static final String TAG = NotificationReceiver.class.getSimpleName();
    private static final int PARSE_NOTIFICATION_LOADER_ID = 0;

    private Context mContext;
    private NotificationAdapter mAdapter;
    private NotificationsDbHelper mDbHelper;
    private List<NotificationItemModel> mData;
    private StatusBarNotification mStatusBarNotification;
    private LoaderManager mLoaderManager;

    public NotificationReceiver(Context context, LoaderManager loaderManager,
                                NotificationsDbHelper dbHelper,
                                List<NotificationItemModel> data,
                                NotificationAdapter adapter) {
        mContext = context;
        mLoaderManager = loaderManager;
        mDbHelper = dbHelper;
        mData = data;
        mAdapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mStatusBarNotification =
            (StatusBarNotification) intent.getExtras().get("com.example.notify.notification_created_event");
        if (mStatusBarNotification == null) {
            return;
        }
        mLoaderManager.initLoader(PARSE_NOTIFICATION_LOADER_ID,
                                  null,
                                  this);
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

    @NonNull
    @Override
    public Loader<NotificationSubItemModel> onCreateLoader(int id, @Nullable Bundle args) {
        return new ParseNotificationLoader(mContext, mStatusBarNotification, mDbHelper);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<NotificationSubItemModel> loader,
                               NotificationSubItemModel incomingNotification) {
        Log.e(TAG, updateNotificationSubCardIfNotificationPackageExists(incomingNotification).toString());
        mLoaderManager.destroyLoader(PARSE_NOTIFICATION_LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<NotificationSubItemModel> loader) {
        mData.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void updateNotificationFields(NotificationSubItemModel existingNotification,
                                          NotificationSubItemModel incomingNotification) {
        existingNotification.setTitle(incomingNotification.getTitle());
        existingNotification.setText(incomingNotification.getText());
        existingNotification.setTextLines(incomingNotification.getTextLines());
        existingNotification.setTimestamp(incomingNotification.getTimestamp());
    }
}
