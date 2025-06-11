package com.example.notificationdeadline.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.example.notificationdeadline.receiver.FixedTimeReceiver;

public class NotificationScheduler {

    public static void scheduleFixedTimeNotification(Context context, long timeMillis, int requestCode, String title, String message,int priority) {
        Intent intent = new Intent(context, FixedTimeReceiver.class);
        intent.putExtra("title", "⏰ Đến hạn rồi: " + title);
        intent.putExtra("message", message);
        intent.putExtra("id", requestCode);
        intent.putExtra("priority", priority);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
                } else {
                    Toast.makeText(context, "Bạn cần cấp quyền Lịch chính xác (Exact Alarm) để nhận thông báo đúng giờ.", Toast.LENGTH_LONG).show();
                    Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(settingsIntent);
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
            }
        }
    }

    public static void cancelScheduledNotification(Context context, int requestCode) {
        Intent intent = new Intent(context, FixedTimeReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

}
