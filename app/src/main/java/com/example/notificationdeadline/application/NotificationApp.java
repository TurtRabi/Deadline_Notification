package com.example.notificationdeadline.application;

import android.app.Application;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

import com.example.notificationdeadline.notification.DeadlineGeneratorWorker;
import com.example.notificationdeadline.notification.DeadlineWorker;
import com.example.notificationdeadline.notification.scheduleDailyGetAllData;
import com.example.notificationdeadline.notification.scheduleDailyEveryMorning;

import java.util.concurrent.TimeUnit;

public class NotificationApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        // Chạy kiểm tra deadline mỗi 6 giờ
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                DeadlineWorker.class,
                6, TimeUnit.HOURS
        )
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "DeadlineCheck",
                        ExistingPeriodicWorkPolicy.KEEP,
                        workRequest
                );

        // Schedule DeadlineGeneratorWorker to run daily
        PeriodicWorkRequest dailyDeadlineGenerationRequest = new PeriodicWorkRequest.Builder(
                DeadlineGeneratorWorker.class,
                24, TimeUnit.HOURS // Run every 24 hours
        )
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "DailyDeadlineGeneration",
                        ExistingPeriodicWorkPolicy.REPLACE, // Use UPDATE to ensure it's always scheduled
                        dailyDeadlineGenerationRequest
                );

        // Gọi đúng tên phương thức scheduler
        scheduleDailyEveryMorning.scheduleDailyMidnight(this);
        scheduleDailyGetAllData.scheduleDailyGetDataAtMidnight(this);
    }
}