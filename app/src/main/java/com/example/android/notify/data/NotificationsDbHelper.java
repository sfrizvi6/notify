package com.example.android.notify.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.notify.data.NotificationContract.NotificationEntry;

public class NotificationsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notifications.db";
    private static final int DATABASE_VERSION = 1;

    public NotificationsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + NotificationEntry.TABLE_NAME + " (" +
            NotificationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NotificationEntry.COLUMN_NOTIFICATION_ID + " INTEGER NOT NULL, " +
            NotificationEntry.COLUMN_APP_NAME + " TEXT NOT NULL," +
            NotificationEntry.COLUMN_APP_ICON + " INTEGER NOT NULL, " +
            NotificationEntry.COLUMN_APP_LARGE_ICON + " BLOB, " +
            NotificationEntry.COLUMN_APP_PACKAGE_NAME + " TEXT NOT NULL, " +
            NotificationEntry.COLUMN_APP_PENDING_INTENT + " BLOB, " +
            NotificationEntry.COLUMN_APP_GROUP_KEY + " TEXT NOT NULL, " +
            NotificationEntry.COLUMN_APP_TITLE + " TEXT, " +
            NotificationEntry.COLUMN_APP_TEXT + " TEXT, " +
            NotificationEntry.COLUMN_APP_TIMESTAMP + " TEXT NOT NULL, " +
            NotificationEntry.COLUMN_APP_TEXTLINES + " TEXT, " +
                /*
                 * UNIQUE constraint on the date column to replace on conflict
                 * to ensure this table can only contain one notification
                 * entry per notificationId
                 */
            " UNIQUE (" + NotificationEntry.COLUMN_NOTIFICATION_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_NOTIFICATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotificationEntry.TABLE_NAME);
        onCreate(db);
    }
}
