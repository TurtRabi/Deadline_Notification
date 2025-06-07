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

        NotificationEntity entity = new NotificationEntity();
        entity.title = title;
        entity.message = message;
        entity.id = (int) System.currentTimeMillis();
        NotificationHistoryService notificationHistoryService = new NotificationHistoryService(context);
        notificationHistoryService.insertNotificationHistory(new
                NotificationHistoryEntity(title,message,System.currentTimeMillis(),true,false,String.valueOf(entity.priority)));

        DeadlineNotifier notifier = new DeadlineNotifier(context);
        notifier.show(entity, entity.title, DeadlineNotifier.HIGH_IMPORTANCE);

    }
}
