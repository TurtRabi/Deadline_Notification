package com.example.notificationdeadline.notification;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.logic.DeadlineEvaluator;
import com.example.notificationdeadline.service.NotificationHistoryService;
import com.example.notificationdeadline.service.NotificationService;

import java.util.List;
import java.util.Map;

public class DeadlineWorker extends Worker {

    private final DeadlineEvaluator evaluator;
    private final DeadlineNotifier notifier;
    private final NotificationHistoryService notificationHistoryService;
    private final NotificationService notificationService;

    public DeadlineWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.evaluator = new DeadlineEvaluator(new NotificationService(context));
        this.notifier = new DeadlineNotifier(context);
        this.notificationService = new NotificationService(context);
        this.notificationHistoryService = new NotificationHistoryService(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Map<Integer, List<NotificationEntity>> alerts = evaluator.evaluateDeadlinesByUrgency();
        Log.d("DeadlineWorker", "Doing work to check deadlines...");

        // KHẨN CẤP (dưới 6 tiếng)
        List<NotificationEntity> highAlerts = alerts.get(DeadlineEvaluator.HIGH_ALERT);
        if (highAlerts != null) {
            for (NotificationEntity entity : highAlerts) {
                notificationService.updateStatus(entity.getId(), StatusEnum.DEADLINE.getValue()); // Sửa enum
                notifier.show(entity, "🚨 KHẨN CẤP: " + entity.getTitle(), DeadlineNotifier.HIGH_IMPORTANCE);
                notificationHistoryService.insertNotificationHistory(
                        new NotificationHistoryEntity("🚨 KHẨN CẤP: " + entity.getTitle(), entity.getMessage(), System.currentTimeMillis(), true, false, "2"));
            }
        }

        // Cận kề deadline (6–10 tiếng)
        List<NotificationEntity> mediumAlerts = alerts.get(DeadlineEvaluator.MEDIUM_ALERT);
        if (mediumAlerts != null) {
            for (NotificationEntity entity : mediumAlerts) {
                notificationService.updateStatus(entity.getId(), StatusEnum.NEAR_DEADLINE.getValue());
                notifier.show(entity, "📌 Cận kề deadline: " + entity.getTitle(), DeadlineNotifier.MEDIUM_IMPORTANCE);
                notificationHistoryService.insertNotificationHistory(
                        new NotificationHistoryEntity("📌 Cận kề deadline: " + entity.getTitle(), entity.getMessage(), System.currentTimeMillis(), true, false, "1"));
            }
        }

        // Sắp đến hạn (10-24 tiếng)
        List<NotificationEntity> lowAlerts = alerts.get(DeadlineEvaluator.LOW_ALERT);
        if (lowAlerts != null) {
            for (NotificationEntity entity : lowAlerts) {
                notificationService.updateStatus(entity.getId(), StatusEnum.UPCOMING.getValue());
                notifier.show(entity, "Deadline: " + entity.getTitle() + " ⏳ Sắp đến hạn rồi nè!", DeadlineNotifier.LOW_IMPORTANCE);
                notificationHistoryService.insertNotificationHistory(
                        new NotificationHistoryEntity("Deadline: " + entity.getTitle() + " ⏳ Sắp đến hạn rồi nè!", entity.getMessage(), System.currentTimeMillis(), true, false, "0"));
            }
        }

        // Quá hạn
        List<NotificationEntity> overAlerts = alerts.get(DeadlineEvaluator.OVER_ALERT);
        if (overAlerts != null) {
            for (NotificationEntity entity : overAlerts) {
                notificationService.updateStatus(entity.getId(), StatusEnum.OVERDEADLINE.getValue()); // Sửa enum
                notifier.show(entity, "⛔ Quá hạn! " + entity.getTitle(), DeadlineNotifier.OVER_IMPORTANCE);
                notificationHistoryService.insertNotificationHistory(
                        new NotificationHistoryEntity("⛔ Quá hạn! " + entity.getTitle(), entity.getMessage(), System.currentTimeMillis(), true, false, "3"));
            }
        }

        Log.d("DeadlineWorker", "🕒 Worker đang chạy");
        return Result.success();
    }
}
