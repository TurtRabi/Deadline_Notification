package com.example.notificationdeadline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.notificationdeadline.notification.DeadlineWorker;
import com.example.notificationdeadline.notification.sheduleDailyEveryMoring;

import java.util.concurrent.TimeUnit;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Re-enqueue WorkManager khi máy khởi động lại
            PeriodicWorkRequest workRequest =
                    new PeriodicWorkRequest.Builder(DeadlineWorker.class, 15, TimeUnit.MINUTES)
                            .build();

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    "DeadlineCheck",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
            );
            sheduleDailyEveryMoring.scheduleDailyMidnight(context);
        }
    }
}