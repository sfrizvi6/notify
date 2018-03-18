package com.example.android.notify.loader;


import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.AsyncTaskLoader;
import android.text.format.DateUtils;
import com.example.android.notify.data.NotificationsDbHelper;
import com.example.android.notify.itemmodels.NotificationSubItemModel;
import com.example.android.notify.utils.NotificationCategory;

public class ParseNotificationLoader extends AsyncTaskLoader<NotificationSubItemModel> {

    private static final String TAG = ParseNotificationLoader.class.getSimpleName();

    private StatusBarNotification mStatusBarNotification;
    private NotificationsDbHelper mDbHelper;

    public ParseNotificationLoader(Context context,
                                   StatusBarNotification statusBarNotification,
                                   NotificationsDbHelper dbHelper) {
        super(context);
        mStatusBarNotification = statusBarNotification;
        mDbHelper = dbHelper;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public NotificationSubItemModel loadInBackground() {
        Bundle extras = mStatusBarNotification.getNotification().extras;

        String packageName = mStatusBarNotification.getPackageName();
        if (packageName == null || packageName.equals("")) {
            return null;
        }

        String groupKey = mStatusBarNotification.getGroupKey();
        ApplicationInfo appInfo = (ApplicationInfo) extras.get("android.appInfo");
        PackageManager packageManager = getContext().getApplicationContext().getPackageManager();
        String category = mStatusBarNotification.getNotification().category;
        String appName = packageManager.getApplicationLabel(appInfo).toString();
        PendingIntent deepLinkIntent = mStatusBarNotification.getNotification().contentIntent;
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
        int notificationId = mStatusBarNotification.getId();
        String timestamp = DateUtils.formatDateTime(getContext(),
                                                    mStatusBarNotification.getPostTime(),
                                                    DateUtils.FORMAT_SHOW_TIME);
        return new NotificationSubItemModel(getContext(), notificationId,
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
    }

    public void deliverResult(NotificationSubItemModel parsedNotificationSubItemModel) {
        mDbHelper.persistNotification(parsedNotificationSubItemModel);
        super.deliverResult(parsedNotificationSubItemModel);
    }
}
