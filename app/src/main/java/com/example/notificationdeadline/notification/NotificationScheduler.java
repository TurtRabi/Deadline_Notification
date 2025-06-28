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

    public static void scheduleFixedTimeNotification(Context context, long timeMillis, int requestCode, String title, String message, int priority) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Các mốc thời gian
        long time15MinBefore = timeMillis - 15 * 60 * 1000;
        long time5MinBefore = timeMillis - 5 * 60 * 1000;
        long timeOverdue = timeMillis + 5*60 * 1000;

        // Các request code riêng cho mỗi mốc
        int requestCodeDeadline = requestCode;                 // Đúng giờ
        int requestCode15Min = requestCode * 1000 + 15;        // Trước 15 phút
        int requestCode5Min = requestCode * 1000 + 5;          // Trước 5 phút
        int requestCodeOverdue = requestCode * 1000 + 999;     // Quá hạn 1 phút

        // Đặt từng pendingIntent riêng biệt cho mỗi mốc

        // 1. Đúng giờ deadline
        Intent intentDeadline = new Intent(context, FixedTimeReceiver.class);
        intentDeadline.putExtra("title", "⏰ Đến hạn rồi: " + title);
        intentDeadline.putExtra("message", message);
        intentDeadline.putExtra("id", requestCodeDeadline);
        intentDeadline.putExtra("priority", priority);
        PendingIntent piDeadline = PendingIntent.getBroadcast(context, requestCodeDeadline, intentDeadline, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 2. Trước 15 phút
        Intent intent15 = new Intent(context, FixedTimeReceiver.class);
        intent15.putExtra("title", "Sắp đến hạn: " + title);
        intent15.putExtra("message", "Còn 15 phút nữa đến hạn: " + message);
        intent15.putExtra("id", requestCode15Min);
        intent15.putExtra("priority", priority);
        PendingIntent pi15 = PendingIntent.getBroadcast(context, requestCode15Min, intent15, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 3. Trước 5 phút
        Intent intent5 = new Intent(context, FixedTimeReceiver.class);
        intent5.putExtra("title", "Sắp đến hạn: " + title);
        intent5.putExtra("message", "Còn 5 phút nữa đến hạn: " + message);
        intent5.putExtra("id", requestCode5Min);
        intent5.putExtra("priority", priority);
        PendingIntent pi5 = PendingIntent.getBroadcast(context, requestCode5Min, intent5, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 4. Quá hạn 1 phút
        Intent intentOverdue = new Intent(context, FixedTimeReceiver.class);
        intentOverdue.putExtra("title", "❗ Quá hạn: " + title);
        intentOverdue.putExtra("message", "Bạn đã quá hạn: " + message);
        intentOverdue.putExtra("id", requestCodeOverdue);
        intentOverdue.putExtra("priority", priority);
        PendingIntent piOverdue = PendingIntent.getBroadcast(context, requestCodeOverdue, intentOverdue, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Đặt lịch cho từng mốc
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    Toast.makeText(context, "Bạn cần cấp quyền Lịch chính xác (Exact Alarm) để nhận thông báo đúng giờ.", Toast.LENGTH_LONG).show();
                    Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(settingsIntent);
                    return;
                }
            }

            if (time15MinBefore > System.currentTimeMillis())
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time15MinBefore, pi15);

            if (time5MinBefore > System.currentTimeMillis())
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time5MinBefore, pi5);

            if (timeMillis > System.currentTimeMillis())
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMillis, piDeadline);

            if (timeOverdue > System.currentTimeMillis())
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeOverdue, piOverdue);
        }
    }


    public static void cancelAllScheduledNotifications(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int[] requestCodes = {
                requestCode,
                requestCode * 1000 + 15,
                requestCode * 1000 + 5,
                requestCode * 1000 + 999
        };

        for (int code : requestCodes) {
            Intent intent = new Intent(context, FixedTimeReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            if (alarmManager != null) alarmManager.cancel(pi);
        }
    }


}
