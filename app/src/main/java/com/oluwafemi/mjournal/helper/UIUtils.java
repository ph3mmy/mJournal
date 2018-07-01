package com.oluwafemi.mjournal.helper;

import android.support.v7.app.AppCompatActivity;

import com.oluwafemi.mjournal.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by femi on 7/1/18.
 */

public class UIUtils {

    public static String formatDateNoTime(long timeInMillis) {
        String newDate;
        Date mDate = new Date(timeInMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        newDate = sdf.format(mDate);
        return newDate;
    }

    public static String formatDateWithTime(long timeInMillis) {
        String newDate;
        Date mDate = new Date(timeInMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        newDate = sdf.format(mDate);
        return newDate;
    }

    public static String formatTimeFromLong(long timeInMillis) {
        String newDate;
        Date mDate = new Date(timeInMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        newDate = sdf.format(mDate);
        return newDate;
    }

    public static void setupToolbar(AppCompatActivity activity, String title) {
            if (activity != null) {
                activity.getSupportActionBar().setTitle(title);
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
    }

}
