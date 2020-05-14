package com.isoterik.android.mybaby.utils;

import android.os.Handler;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimerUtil implements Runnable
{
    private boolean isTimerRunning;
    private boolean isPaused;

    private long startTime;
    private Calendar startDate;

    private Handler handler;

    private TimerListener listener;

    public TimerUtil (TimerListener listener)
    {
        this.listener = listener;
        handler = new Handler();
    }

    @Override
    public void run()
    {
        long millis = System.currentTimeMillis() - startTime;
        long seconds = (millis / 1000);
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds = seconds % 60;
        minutes = minutes % 60;

        listener.onUpdateTimer(millis, hours, minutes, seconds);
        handler.postDelayed(this, 500);
    }

    public long getStartTime()
    {
        return startTime;
    }

    public TimerUtil start()
    {
        if (isTimerRunning)
            return this;

        isTimerRunning = true;
        startDate = Calendar.getInstance();
        startTime = System.currentTimeMillis();
        handler.postDelayed(this, 0);

        return this;
    }

    public void pause()
    {
        if (!isTimerRunning)
            return;

        isPaused = true;
        handler.removeCallbacks(this);
    }

    public void resume()
    {
        if (!isTimerRunning)
            return;

        isPaused = false;
        handler.postDelayed(this, 0);
    }

    public void stop()
    {
        if (!isTimerRunning)
            return;

        handler.removeCallbacks(this);
        isTimerRunning = false;
        startTime = 0;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public boolean isTimerRunning()
    { return isTimerRunning; }

    public boolean isPaused()
    { return isPaused; }

    public static String formatDurationSeconds (long longSeconds)
    {
        int seconds =  (int)(longSeconds);
        int minutes = (int)(longSeconds / 60);
        int hours = (int)(longSeconds / 60 / 60);

        if (hours > 0)
            return  hours + "hrs";
        else if (minutes > 0)
            return minutes + "min";
        else
            return seconds + "s";
    }

    public static String formatDuration (long timeMillis)
    {
        long longSeconds = timeMillis / 1000;
        return formatDurationSeconds(longSeconds);
    }

    public static int stringDurationToSeconds (String duration)
    {
        if (duration.endsWith("hrs"))
        {
            int hours = Integer.parseInt(duration.replace("hrs", ""));
            return (int)TimeUnit.HOURS.toSeconds(hours);
        }
        else if (duration.endsWith("min"))
        {
            int min = Integer.parseInt(duration.replace("min", ""));
            return (int)TimeUnit.MINUTES.toSeconds(min);
        }
        else
        {
            int seconds = Integer.parseInt(duration.replace("s", ""));
            return seconds;
        }
    }
    public static String forceTwoDigits (int digits)
    {
        String strDigits = String.valueOf(digits);

        if (strDigits.length() < 2)
            strDigits = "0" + strDigits;

        return  strDigits;
    }

    public static interface TimerListener
    {
        public void onUpdateTimer (long millis, long hours, long minutes, long seconds);
    }
}
