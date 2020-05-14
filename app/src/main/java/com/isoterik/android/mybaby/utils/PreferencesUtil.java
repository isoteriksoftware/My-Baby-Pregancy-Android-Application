package com.isoterik.android.mybaby.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.isoterik.android.mybaby.R;

import java.util.Calendar;

public class PreferencesUtil
{
    public static boolean hasPregnancyTrackerData (Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(R.string.has_pregnancy_tracker_data_key), false);
    }

    public static void hasPregnancyTrackerData (Context context, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(context.getString(R.string.has_pregnancy_tracker_data_key), value)
                .apply();
    }

    public static void lmpDate (Context context, Calendar calendar)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        sharedPreferences.edit().putInt(context.getString(R.string.lmp_year_key), year)
                .putInt(context.getString(R.string.lmp_month_key), month)
                .putInt(context.getString(R.string.lmp_day_key), day)
                .apply();
    }

    public static Calendar lmpDate (Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);

        int year = sharedPreferences.getInt(context.getString(R.string.lmp_year_key), 0);
        int month = sharedPreferences.getInt(context.getString(R.string.lmp_month_key), 0);
        int day = sharedPreferences.getInt(context.getString(R.string.lmp_day_key), 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }

    public static void babyDue (Context context, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(context.getString(R.string.baby_due_key), value)
                .apply();
    }

    public static boolean babyDue (Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(R.string.baby_due_key), false);
    }

    public static boolean firstTimeLaunched (Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(R.string.first_time_launched_key), true);
    }

    public static void firstTimeLaunched (Context context, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(context.getString(R.string.first_time_launched_key), value)
                .apply();
    }

    public static boolean firstBabyKickCounting (Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(R.string.first_baby_kick_counting_key), true);
    }

    public static void firstBabyKickCounting (Context context, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(context.getString(R.string.first_baby_kick_counting_key), value)
                .apply();
    }

    public static boolean firstContractionTiming (Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(R.string.first_contraction_timing_key), true);
    }

    public static void firstContractionTiming (Context context, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(context.getString(R.string.first_contraction_timing_key), value)
                .apply();
    }

    public static boolean firstPregnancyTrackingInitial (Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(R.string.first_pregnancy_tracking_initial_key), true);
    }

    public static void firstPregnancyTrackingInitial (Context context, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(context.getString(R.string.first_pregnancy_tracking_initial_key), value)
                .apply();
    }

    public static boolean firstPregnancyTrackingAfterLMP (Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(R.string.first_pregnancy_tracking_after_lmp_key), true);
    }

    public static void firstPregnancyTrackingAfterLMP (Context context, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(context.getString(R.string.first_pregnancy_tracking_after_lmp_key), value)
                .apply();
    }
}




























