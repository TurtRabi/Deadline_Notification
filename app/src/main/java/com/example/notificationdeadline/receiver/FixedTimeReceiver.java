package com.example.notificationdeadline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.notification.DeadlineNotifier;
import com.example.notificationdeadline.service.NotificationHistoryService;

public class FixedTimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        int id = intent.getIntExtra("id", (int) System.currentTimeMillis());
        int priority = intent.getIntExtra("priority", 0); // default là NORMAL

        NotificationEntity entity = new NotificationEntity();
        entity.setTitle(title);
        entity.setMessage(message);
        entity.setId(id);
        entity.setPriority(priority);

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

        DeadlineNotifier notifier = new DeadlineNotifier(context);
        notifier.show(entity, entity.getTitle(), DeadlineNotifier.HIGH_IMPORTANCE);
    }
}
