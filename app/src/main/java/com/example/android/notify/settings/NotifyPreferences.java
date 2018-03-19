package com.example.android.notify.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.android.notify.R;

public class NotifyPreferences {

    public static String getNumDayToPersistNotifications(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForNumDaysPreference = context.getString(R.string.pref_days_key);
        String defaultNumDaysPreference = context.getString(R.string.day_two);
        return sharedPreferences.getString(keyForNumDaysPreference, defaultNumDaysPreference);
    }
}
