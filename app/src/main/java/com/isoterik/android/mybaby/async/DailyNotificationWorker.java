package com.isoterik.android.mybaby.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.isoterik.android.mybaby.MainActivity;
import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.utils.NotificationsUtil;

public class DailyNotificationWorker extends Worker
{
    public DailyNotificationWorker (@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() ->
        {
            NotificationsUtil.showDailyNotification(getApplicationContext());
        });
        return Result.success();
    }
}
