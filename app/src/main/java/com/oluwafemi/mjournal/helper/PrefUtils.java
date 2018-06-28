package com.oluwafemi.mjournal.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {

    private static final String TAG = "PrefUtils";

    public static final String LOGGED_USER_ID = "logged_id";

    public static void setLoggedUserId (Context context, String userId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(LOGGED_USER_ID, userId).apply();
    }

    public static String getLoggedUserId(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(LOGGED_USER_ID, null);
    }

}
