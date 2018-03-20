package com.example.android.notify.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import com.example.android.notify.data.NotificationContract.NotificationEntry;
import com.example.android.notify.itemmodels.NotificationSubItemModel;

import static com.example.android.notify.data.NotificationContract.NotificationEntry.TABLE_NAME;

public class NotificationsDbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDb;

    private static final String SQL_CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
        NotificationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        NotificationEntry.COLUMN_NOTIFICATION_ID + " INTEGER NOT NULL, " +
        NotificationEntry.COLUMN_NOTIFICATION_CATEGORY + " TEXT NOT NULL, " +
        NotificationEntry.COLUMN_APP_NAME + " TEXT NOT NULL," +
        NotificationEntry.COLUMN_APP_ICON + " INTEGER NOT NULL, " +
        NotificationEntry.COLUMN_APP_ICON_COLOR + " INTEGER NOT NULL, " +
        NotificationEntry.COLUMN_APP_LARGE_ICON + " BLOB, " +
        NotificationEntry.COLUMN_APP_PACKAGE_NAME + " TEXT NOT NULL, " +
        NotificationEntry.COLUMN_PENDING_INTENT + " TEXT, " +
        NotificationEntry.COLUMN_GROUP_KEY + " TEXT NOT NULL, " +
        NotificationEntry.COLUMN_TITLE + " TEXT, " +
        NotificationEntry.COLUMN_TEXT + " TEXT, " +
        NotificationEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL, " +
        NotificationEntry.COLUMN_TEXTLINES + " BLOB, " +
                /*
                 * UNIQUE constraint on the date column to replace on conflict
                 * to ensure this table can only contain one notification
                 * entry per notificationId
                 */
        " UNIQUE (" + NotificationEntry.COLUMN_NOTIFICATION_ID + ") ON CONFLICT REPLACE);";

    private static final String DATABASE_NAME = "notifications.db";
    private static final int DATABASE_VERSION = 1;

    public NotificationsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTIFICATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void persistNotification(NotificationSubItemModel notificationSubItemModel) {
        ContentValues notificationDbContent = new ContentValues();
        notificationDbContent.put(NotificationEntry.COLUMN_NOTIFICATION_ID, notificationSubItemModel.mId);
        notificationDbContent.put(NotificationEntry.COLUMN_NOTIFICATION_CATEGORY,
                                  notificationSubItemModel.mCategory.toString());
        notificationDbContent.put(NotificationEntry.COLUMN_APP_NAME, notificationSubItemModel.mAppName);
        notificationDbContent.put(NotificationEntry.COLUMN_APP_ICON, notificationSubItemModel.mAppIcon);
        notificationDbContent.put(NotificationEntry.COLUMN_APP_ICON_COLOR, notificationSubItemModel.mColor);
        notificationDbContent.put(NotificationEntry.COLUMN_APP_LARGE_ICON,
                                  DbUtils.getBitmapAsByteArray(notificationSubItemModel.mLargeIcon));
        notificationDbContent.put(NotificationEntry.COLUMN_APP_PACKAGE_NAME, notificationSubItemModel.mPackageName);
        Parcel parcel = Parcel.obtain();
        if (notificationSubItemModel.mPendingIntent != null) {
            notificationSubItemModel.mPendingIntent.writeToParcel(parcel, 0);
        }
        notificationDbContent.put(NotificationEntry.COLUMN_PENDING_INTENT, parcel.createByteArray());
        notificationDbContent.put(NotificationEntry.COLUMN_GROUP_KEY, notificationSubItemModel.mGroupKey);
        notificationDbContent.put(NotificationEntry.COLUMN_TITLE, notificationSubItemModel.getTitle());
        notificationDbContent.put(NotificationEntry.COLUMN_TEXT, notificationSubItemModel.getText());
        notificationDbContent.put(NotificationEntry.COLUMN_TIMESTAMP, notificationSubItemModel.getTimestamp());
        notificationDbContent.put(NotificationEntry.COLUMN_TEXTLINES,
                                  DbUtils.getCharSequenceAsByteArray(notificationSubItemModel.getTextLines()));
        mDb.insert(TABLE_NAME, null, notificationDbContent);
    }

    public Cursor queryAllNotifications() {
        return mDb.query(TABLE_NAME, null, null, null, null, null, null);
    }

    // TODO: remove; for testing only
    public void cleanInitDb() {
        mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        mDb.execSQL(SQL_CREATE_NOTIFICATIONS_TABLE);
    }
}
