package com.example.notificationdeadline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.notification.DeadlineNotifier;
import com.example.notificationdeadline.service.NotificationHistoryService;
import com.example.notificationdeadline.service.NotificationService;
import com.example.notificationdeadline.dto.Enum.StatusEnum;

public class FixedTimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        int id = intent.getIntExtra("id", (int) System.currentTimeMillis());
        int originalId = intent.getIntExtra("original_id", -1);
        int priority = intent.getIntExtra("priority", 0); // default là NORMAL

        NotificationEntity entity = new NotificationEntity();
        entity.setTitle(title);
        entity.setMessage(message);
        entity.setId(originalId != -1 ? originalId : id);
        entity.setPriority(priority);

        new Thread(() -> {
            NotificationHistoryService notificationHistoryService = new NotificationHistoryService(context);
            notificationHistoryService.insertNotificationHistory(
                    new NotificationHistoryEntity(
                            title,
                            message,
                            System.currentTimeMillis(),
                            true,
                            false,
                            String.valueOf(priority) // Lưu lại icon đúng mapping Enum nếu cần
                    ));

            // Update status only for the main deadline notification or overdue notification
            if (id == originalId) {
                NotificationService notificationService = new NotificationService(context);
                notificationService.updateStatus(StatusEnum.DEADLINE.getValue(), originalId);
            } else if (id == originalId * 1000 + 999) { // Check if it's the overdue notification
                NotificationService notificationService = new NotificationService(context);
                notificationService.updateStatus(StatusEnum.OVERDEADLINE.getValue(), originalId);
            }
        }).start();

        DeadlineNotifier notifier = new DeadlineNotifier(context);
        notifier.show(entity, entity.getTitle(), DeadlineNotifier.HIGH_IMPORTANCE);
    }
}
