package com.example.android.notify.data;


import android.provider.BaseColumns;

public class NotificationContract {

    public static final class NotificationEntry implements BaseColumns {
        public static final String TABLE_NAME = "notifications";

        public static final String COLUMN_NOTIFICATION_ID = "notification_id";
        public static final String COLUMN_APP_NAME = "app_name";
        public static final String COLUMN_APP_ICON = "app_icon";
        public static final String COLUMN_APP_LARGE_ICON = "app_large_icon";
        public static final String COLUMN_APP_PACKAGE_NAME = "app_package_name";
        public static final String COLUMN_APP_PENDING_INTENT = "app_pending_intent";
        public static final String COLUMN_APP_GROUP_KEY = "app_group_key";
        public static final String COLUMN_APP_TITLE = "app_title";
        public static final String COLUMN_APP_TEXT = "app_text";
        public static final String COLUMN_APP_TIMESTAMP = "app_timestamp";
        public static final String COLUMN_APP_TEXTLINES = "app_textlines";
    }
}
