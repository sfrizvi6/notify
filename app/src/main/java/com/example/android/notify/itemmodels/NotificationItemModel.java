package com.example.android.notify.itemmodels;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import com.example.android.notify.adapters.NotificationSubAdapter;
import com.example.android.notify.utils.NotificationCategory;

import java.util.ArrayList;
import java.util.List;

public class NotificationItemModel extends NotificationSubItemModel {

    private static final String TAG = NotificationItemModel.class.getSimpleName();

    private List<NotificationSubItemModel> mSubData;
    private NotificationSubAdapter mSubAdapter;

    public NotificationItemModel(Context context,
                                 int id,
                                 String category,
                                 String appName,
                                 int appIcon,
                                 int color,
                                 String packageName,
                                 String groupKey,
                                 PendingIntent pendingIntent,
                                 String title,
                                 String text,
                                 String timestamp,
                                 CharSequence[] textLines) {
        super(context,
              id,
              NotificationCategory.getCategory(category),
              appName,
              appIcon,
              color,
              null,
              packageName,
              pendingIntent,
              groupKey,
              title,
              text,
              timestamp,
              textLines);
        mSubData = new ArrayList<>();
        mSubAdapter = new NotificationSubAdapter(mSubData);
    }

    public NotificationItemModel(NotificationSubItemModel notificationSubItemModel) {
        super(notificationSubItemModel.mContext,
              notificationSubItemModel.mId,
              notificationSubItemModel.mCategory,
              notificationSubItemModel.mAppName,
              notificationSubItemModel.mAppIcon,
              notificationSubItemModel.mColor,
              notificationSubItemModel.mLargeIcon,
              notificationSubItemModel.mPackageName,
              notificationSubItemModel.mPendingIntent,
              notificationSubItemModel.mGroupKey,
              notificationSubItemModel.getTitle(),
              notificationSubItemModel.getText(),
              notificationSubItemModel.getTimestamp(),
              notificationSubItemModel.getTextLines());
        mSubData = new ArrayList<>();
        mSubAdapter = new NotificationSubAdapter(mSubData);
    }

    public void addSubNotificationData(NotificationSubItemModel subNotificationItemModel) {
        mSubData.add(0, subNotificationItemModel);
        mSubAdapter.notifyDataSetChanged();
    }

    public void removeSubNotificationData(int position) {
        if (position < 0 && position >= mSubData.size()) {
            Log.e(TAG, "Sub-adapter position# " + position + " doesn't exist!");
        }
        mSubData.remove(position);
        mSubAdapter.notifyDataSetChanged();
    }

    public NotificationSubAdapter getSubAdapter() {
        return mSubAdapter;
    }

    public List<NotificationSubItemModel> getSubData() {
        return mSubData;
    }
}
