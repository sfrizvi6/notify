package com.example.android.notify.utils;


import android.app.Notification;
import android.util.Log;

public enum NotificationCategory {

    CALL(Notification.CATEGORY_CALL),
    MESSAGE(Notification.CATEGORY_MESSAGE),
    EMAIL(Notification.CATEGORY_EMAIL),
    EVENT(Notification.CATEGORY_EVENT),
    PROMO(Notification.CATEGORY_PROMO),
    ALARM(Notification.CATEGORY_ALARM),
    PROGRESS(Notification.CATEGORY_PROGRESS),
    SOCIAL(Notification.CATEGORY_SOCIAL),
    ERROR(Notification.CATEGORY_ERROR),
    TRANSPORT(Notification.CATEGORY_TRANSPORT),
    SYSTEM(Notification.CATEGORY_SYSTEM),
    SERVICE(Notification.CATEGORY_SERVICE),
    RECOMMENDATION(Notification.CATEGORY_RECOMMENDATION),
    STATUS(Notification.CATEGORY_STATUS),
    REMINDER(Notification.CATEGORY_REMINDER),
    NONE("none");


    private String mCategory;

    NotificationCategory(String category) {
        mCategory = category;
    }

    @Override
    public String toString() {
        return mCategory;
    }

    public static NotificationCategory getCategory(String category) {
        try {
            return NotificationCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            Log.e(NotificationCategory.class.getSimpleName(), "No such category: " + category + "\n" + e.getMessage());
            return NONE;
        }
    }
}
