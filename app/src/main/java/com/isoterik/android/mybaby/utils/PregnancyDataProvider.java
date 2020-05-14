package com.isoterik.android.mybaby.utils;

import android.content.Context;

import com.isoterik.android.mybaby.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class PregnancyDataProvider
{
    public static final int TOTAL_KICKS_INDEX = 0;
    public static final int TOTAL_KICKS_DURATION_INDEX = 1;
    public static final int TOTAL_KICKS_SESSIONS_INDEX = 2;
    public static final int TOTAL_KICKS_PER_SESSION_INDEX = 3;

    public static final int TOTAL_CONTRACTIONS_INDEX = 0;
    public static final int TOTAL_CONTRACTIONS_DURATION_INDEX = 1;
    public static final int TOTAL_CONTRACTIONS_REST_INDEX = 2;
    public static final int TOTAL_CONTRACTIONS_SESSIONS_INDEX = 3;
    public static final int TOTAL_CONTRACTIONS_PER_SESSION_INDEX = 4;

    public static int getPregnancyWeeks (Context context)
    {
        if (!PreferencesUtil.hasPregnancyTrackerData(context))
            return 1;

        Calendar now = Calendar.getInstance();
        Calendar lmpCalendar = PreferencesUtil.lmpDate(context);
        long diff = now.getTimeInMillis() - lmpCalendar.getTimeInMillis();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        int pregnancyWeeks = (int)(days / 7);

        return pregnancyWeeks;
    }

    public static int validatePregnancyWeeksIndex (int pregnancyWeeks)
    {
        int week = pregnancyWeeks - 1;
        if (week < 0)
            week = 0;
        if (week > 41)
            week = 41;

        return week;
    }

    public static Calendar getBabyDueDate (Context context)
    {
        Calendar lmpDate = PreferencesUtil.lmpDate(context);
        Calendar now = Calendar.getInstance();
        long diff = now.getTimeInMillis() - lmpDate.getTimeInMillis();
        int daysPassed = (int)TimeUnit.MILLISECONDS.toDays(diff);
        int remainingDays = Integer.parseInt(context.getString(R.string.max_pregnancy_days)) - daysPassed;
        long remainingDaysMillis = TimeUnit.DAYS.toMillis(remainingDays);

        long deliveryDateMillis = now.getTimeInMillis() + remainingDaysMillis;
        Calendar deliveryDate = Calendar.getInstance();
        deliveryDate.setTimeInMillis(deliveryDateMillis);

        return deliveryDate;
    }

    public static int getDaysToDelivery (Context context)
    {
        Calendar dueDate = getBabyDueDate(context);
        int days = (int)TimeUnit.MILLISECONDS.toDays(dueDate.getTimeInMillis());
        return days;
    }

    public static int[] getKicksStats (String[] kicksData)
    {
        int[] stats = new int[4];
        int totalKicks = 0;
        int totalDuration = 0;
        int totalSessions = kicksData.length;
        int kicksPerSession = 0;

        for (String kickData : kicksData)
        {
            String[] data = kickData.split(FileUtil.LINE_DATA_SEPARATOR);
            totalKicks += Integer.parseInt(data[3]);
            totalDuration += TimerUtil.stringDurationToSeconds(data[2]);
        }

        kicksPerSession =  (int)Math.ceil((float)totalKicks / (float)totalSessions);

        stats[TOTAL_KICKS_INDEX] = totalKicks;
        stats[TOTAL_KICKS_DURATION_INDEX] = totalDuration;
        stats[TOTAL_KICKS_PER_SESSION_INDEX] = kicksPerSession;
        stats[TOTAL_KICKS_SESSIONS_INDEX] = totalSessions;

        return stats;
    }

    public static int[] getContractionsStats (String[] contractionsData)
    {
        int[] stats = new int[5];
        int totalContractions = 0;
        int totalDuration = 0;
        int totalRest = 0;
        int totalSessions = contractionsData.length;
        int contractionsPerSession = 0;

        for (String kickData : contractionsData)
        {
            String[] data = kickData.split(FileUtil.LINE_DATA_SEPARATOR);
            totalContractions += Integer.parseInt(data[3]);
            totalDuration += TimerUtil.stringDurationToSeconds(data[2]);
            totalRest += TimerUtil.stringDurationToSeconds(data[4]);
        }

        contractionsPerSession =  (int)Math.ceil((float)totalContractions / (float)totalSessions);

        stats[TOTAL_CONTRACTIONS_INDEX] = totalContractions;
        stats[TOTAL_CONTRACTIONS_DURATION_INDEX] = totalDuration;
        stats[TOTAL_CONTRACTIONS_REST_INDEX] = totalRest;
        stats[TOTAL_CONTRACTIONS_PER_SESSION_INDEX] = contractionsPerSession;
        stats[TOTAL_CONTRACTIONS_SESSIONS_INDEX] = totalSessions;

        return stats;
    }
}































