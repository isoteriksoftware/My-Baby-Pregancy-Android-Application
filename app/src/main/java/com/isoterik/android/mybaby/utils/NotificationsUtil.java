package com.isoterik.android.mybaby.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import com.isoterik.android.mybaby.PregnancyTrackerActivity;
import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.async.BabyDueNotificationWorker;
import com.isoterik.android.mybaby.async.DailyNotificationWorker;
import com.isoterik.android.mybaby.async.WeeklyNotificationWorker;

import java.util.concurrent.TimeUnit;

public class NotificationsUtil
{
    public static final int DAILY_PENDING_INTENT_ID = 4559;
    public static final int WEEKLY_PENDING_INTENT_ID = 4569;
    public static final int BABY_DUE_PENDING_INTENT_ID = 4579;

    public static final String NOTIFICATION_CHANNEL_ID = "My Baby Notification Channel ID";
    public static final String NOTIFICATION_CHANNEL_NAME = "My Baby Notifications";
    public static final String NOTIFICATION_CHANNEL_DESC = "Notifications from the My Baby Application";

    public static final String DAILY_NOTIFICATION_WORKER_NAME = "DAILY NOTIFICATION WORK";
    public static final String WEEKLY_NOTIFICATION_WORKER_NAME = "WEEKLY NOTIFICATION WORK";
    public static final String BABY_DUE_NOTIFICATION_WORKER_NAME = "BABY DUE NOTIFICATION WORK";

    public static NotificationCompat.Builder buildNotification (Context context, PendingIntent pendingIntent)
    {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_woman_96dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_woman_140dp))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColorized(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setDefaults(Notification.DEFAULT_ALL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(NOTIFICATION_CHANNEL_DESC);
            notificationManagerCompat.createNotificationChannel(channel);
        }

        return builder;
    }

    public static void showDailyNotification (Context context)
    {
        String title = context.getString(R.string.daily_notification_title);
        String message = context.getString(R.string.daily_notification_text);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Intent intent = new Intent(context, PregnancyTrackerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, DAILY_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = buildNotification(context, pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        notificationManagerCompat.notify((int)(System.currentTimeMillis()/1000), builder.build());
    }

    public static void showWeeklyNotification (Context context)
    {
        int weeks = PregnancyDataProvider.getPregnancyWeeks(context);
        String[] weeklyPregnancyUpdates = context.getResources().getStringArray(R.array.pregnancy_week_data_array);
        String title = String.format(context.getString(R.string.weekly_notification_title_format), weeks);
        String message = weeklyPregnancyUpdates[PregnancyDataProvider.validatePregnancyWeeksIndex(weeks)];

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Intent intent = new Intent(context, PregnancyTrackerActivity.class);
        intent.putExtra(Misc.EXTRA_CURRENT_TAB, 2);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, WEEKLY_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = buildNotification(context, pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        notificationManagerCompat.notify((int)(System.currentTimeMillis()/1000), builder.build());
    }

    public static void showBabyDueNotification (Context context)
    {
        String title = context.getString(R.string.baby_due_notification_title);
        String message = context.getString(R.string.baby_due_notification_text);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Intent intent = new Intent(context, PregnancyTrackerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, BABY_DUE_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = buildNotification(context, pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        notificationManagerCompat.notify((int)(System.currentTimeMillis()/1000), builder.build());
    }

    public static <T extends Worker> void  startPeriodicNotificationTask (Context context, Class<T> workerClass, int waitDuration, TimeUnit timeUnit,
                                                                      String workerTagName)
    {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(workerClass, waitDuration, timeUnit)
                .setInitialDelay(waitDuration, timeUnit)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(workerTagName, ExistingPeriodicWorkPolicy.KEEP, workRequest);
    }

    public static void startDailyNotificationTask (Context context)
    {
        startPeriodicNotificationTask(context, DailyNotificationWorker.class, 24, TimeUnit.HOURS, DAILY_NOTIFICATION_WORKER_NAME);
    }

    public static void startWeeklyNotificationTask (Context context)
    {
        startPeriodicNotificationTask(context, WeeklyNotificationWorker.class, 7, TimeUnit.DAYS, WEEKLY_NOTIFICATION_WORKER_NAME);
    }

    public static void startBabyDueNotificationTask (Context context)
    {
        startPeriodicNotificationTask(context, BabyDueNotificationWorker.class, PregnancyDataProvider.getDaysToDelivery(context),
                TimeUnit.DAYS, BABY_DUE_NOTIFICATION_WORKER_NAME);
    }

    public static void cancelAllNotificationTasks (Context context)
    {
        WorkManager.getInstance(context).cancelAllWork();
    }

    public static void reset (Context context)
    {
        cancelAllNotificationTasks(context);
        startWeeklyNotificationTask(context);
        startBabyDueNotificationTask(context);
    }
}
























