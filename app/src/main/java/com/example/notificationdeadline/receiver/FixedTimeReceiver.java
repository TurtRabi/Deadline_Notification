package com.example.notificationdeadline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.notification.DeadlineNotifier;
import com.example.notificationdeadline.service.NotificationHistoryService;
import com.example.notificationdeadline.service.NotificationService;
import com.example.notificationdeadline.dto.Enum.StatusEnum;

import android.os.Handler;
import android.os.Looper;

public class FixedTimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        int id = intent.getIntExtra("id", (int) System.currentTimeMillis());
        int originalId = intent.getIntExtra("original_id", -1);
        int priority = intent.getIntExtra("priority", 0); // default là NORMAL

        //Toast.makeText(context, "FixedTimeReceiver triggered for: " + title, Toast.LENGTH_LONG).show();

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

                // Launch FullScreenNotificationActivity on the main thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    Intent fullScreenIntent = new Intent(context, com.example.notificationdeadline.ui.FullScreenNotificationActivity.class);
                    fullScreenIntent.putExtra("title", title);
                    fullScreenIntent.putExtra("message", message);
                    fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(fullScreenIntent);
                });

            } else if (id == originalId * 1000 + 999) { // Check if it's the overdue notification
                NotificationService notificationService = new NotificationService(context);
                notificationService.updateStatus(StatusEnum.OVERDEADLINE.getValue(), originalId);
            }
        }).start();

        DeadlineNotifier notifier = new DeadlineNotifier(context);
        notifier.show(entity, entity.getTitle(), DeadlineNotifier.HIGH_IMPORTANCE);
    }
}
