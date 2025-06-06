package com.example.notificationdeadline.application;

import android.app.Application;


import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Constraints;


import com.example.notificationdeadline.notification.DeadlineWorker;
import com.example.notificationdeadline.notification.scheduleDailyGetAllData;
import com.example.notificationdeadline.notification.sheduleDailyEveryMoring;

import java.util.concurrent.TimeUnit;

public class notificationApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(DeadlineWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "DeadlineCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
        );

        sheduleDailyEveryMoring.scheduleDailyMidnight(this);
        scheduleDailyGetAllData.schedulerDailyGetDataEveryZeroTime(this);
    }
}
