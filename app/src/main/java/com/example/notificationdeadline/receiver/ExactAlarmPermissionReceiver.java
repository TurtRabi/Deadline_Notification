package com.example.notificationdeadline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.notificationdeadline.notification.NotificationScheduler;

public class ExactAlarmPermissionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.app.action.SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED".equals(intent.getAction())) {
            long timeMillis = intent.getLongExtra("timeMillis", -1);
            int requestCode = intent.getIntExtra("requestCode", -1);
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");
            int priority = intent.getIntExtra("priority", -1);

            if (timeMillis != -1 && requestCode != -1) {
                NotificationScheduler.scheduleFixedTimeNotification(context, timeMillis, requestCode, title, message, priority);
            }
        }
    }
}