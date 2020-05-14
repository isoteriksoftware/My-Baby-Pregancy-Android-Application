package com.isoterik.android.mybaby.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.isoterik.android.mybaby.utils.FileUtil;
import com.isoterik.android.mybaby.utils.NotificationsUtil;
import com.isoterik.android.mybaby.utils.PreferencesUtil;

public class BabyDueNotificationWorker extends Worker
{
    public BabyDueNotificationWorker (@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                NotificationsUtil.showBabyDueNotification(getApplicationContext());
                PreferencesUtil.babyDue(getApplicationContext(), true);
                PreferencesUtil.hasPregnancyTrackerData(getApplicationContext(), false);
                NotificationsUtil.cancelAllNotificationTasks(getApplicationContext());
                FileUtil.reset(getApplicationContext());
            }
        }; asyncTask.execute();
        return Result.success();
    }
}
