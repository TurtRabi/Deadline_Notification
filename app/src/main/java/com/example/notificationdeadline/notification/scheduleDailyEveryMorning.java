package com.example.notificationdeadline.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;

import com.example.notificationdeadline.receiver.FixedTimeReceiver;

public class scheduleDailyEveryMorning {


    public static void scheduleDailyMidnight(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, FixedTimeReceiver.class);
        String title ="☀️ Chào ngày mới!";
        String message="Chúc bạn một ngày tràn đầy năng lượng và thành công!";

        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("priority",0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                1000,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

}
