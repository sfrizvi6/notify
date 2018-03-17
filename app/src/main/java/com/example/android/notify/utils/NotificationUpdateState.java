package com.example.android.notify.utils;

public enum NotificationUpdateState {

    PARENT_NOTIFICATION_UPDATED("Parent notification updated"),
    SUB_NOTIFICATION_UPDATED("Sub notification updated"),
    NEW_SUB_NOTIFICATION_ADDED("New sub-notification added to an existing parent"),
    NEW_PARENT_NOTIFICATION_ADDED("New parent notification added");

    private String mUpdateStatus;

    NotificationUpdateState(String updateStatus) {
        mUpdateStatus = updateStatus;
    }

    @Override
    public String toString() {
        return mUpdateStatus;
    }
}