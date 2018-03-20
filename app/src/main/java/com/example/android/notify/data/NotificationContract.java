package com.example.android.notify.data;


import android.provider.BaseColumns;

public class NotificationContract {

    public static final class NotificationEntry implements BaseColumns {
        public static final String TABLE_NAME = "notifications";

        public static final String COLUMN_NOTIFICATION_ID = "notification_id";
        public static final String COLUMN_NOTIFICATION_CATEGORY = "notification_category";
        public static final String COLUMN_APP_NAME = "app_name";
        public static final String COLUMN_APP_ICON = "app_icon";
        public static final String COLUMN_APP_ICON_COLOR = "app_icon_color";
        public static final String COLUMN_APP_LARGE_ICON = "app_large_icon";
        public static final String COLUMN_APP_PACKAGE_NAME = "app_package_name";
        public static final String COLUMN_PENDING_INTENT = "pending_intent";
        public static final String COLUMN_GROUP_KEY = "group_key";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_TEXTLINES = "textlines";
    }
}
