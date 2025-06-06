package com.example.notificationdeadline.notification;

import android.content.Context;
import android.icu.util.Calendar;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class scheduleDailyGetAllData {
    public static void schedulerDailyGetDataEveryZeroTime(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long now = System.currentTimeMillis();
        long initialDelay = calendar.getTimeInMillis() - now;

        if (initialDelay < 0) {
            initialDelay += 24 * 60 * 60 * 1000;
        }

        PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(
                ScanDeadlineWorker.class,
                24, TimeUnit.HOURS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "DailyDeadlineWorker",
                ExistingPeriodicWorkPolicy.REPLACE,
                dailyWorkRequest);

    }
}
