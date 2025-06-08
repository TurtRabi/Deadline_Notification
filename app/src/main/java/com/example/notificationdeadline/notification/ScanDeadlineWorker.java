package com.example.notificationdeadline.notification;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.service.NotificationService;

import java.util.List;

public class ScanDeadlineWorker extends Worker {
    public ScanDeadlineWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            NotificationService notificationService = new NotificationService(getApplicationContext());
            List<NotificationEntity> notificationEntityList = notificationService.fetchAllNotificationsByDay1();
            if (notificationEntityList != null) {
                for (NotificationEntity notification : notificationEntityList) {
                    NotificationScheduler.scheduleFixedTimeNotification(
                            getApplicationContext(),
                            notification.getTimeMillis(),
                            notification.getId(),
                            notification.getTitle(),
                            notification.getMessage()
                    );
                }
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
