package com.example.notificationdeadline.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.dto.Enum.PriorityEnum;
import com.example.notificationdeadline.ui.activity_main;

public class DeadlineNotifier {

    private final Context context;
    private static final String CHANNEL_ID = "deadline_channel";
    public static final int HIGH_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    public static final int MEDIUM_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
    public static final int LOW_IMPORTANCE = NotificationManager.IMPORTANCE_LOW;
    public  static final int OVER_IMPORTANCE = NotificationManager.IMPORTANCE_MAX;

    public DeadlineNotifier(Context context) {
        this.context = context;
    }

    public void show(NotificationEntity entity, String title, int importance) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo channel nếu cần (Android O+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel existingChannel = manager.getNotificationChannel(CHANNEL_ID);
            if (existingChannel == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Thông báo Deadline",
                        importance
                );
                channel.setDescription("Thông báo các deadline khẩn cấp hoặc gần tới");
                manager.createNotificationChannel(channel);
            }
        }

        // Intent để mở app khi bấm thông báo
        Intent intent = new Intent(context, activity_main.class);
        intent.putExtra("notification_id", entity.id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                (int) entity.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Lấy icon từ độ ưu tiên
        PriorityEnum priority = PriorityEnum.fromValue(entity.priority);
        int iconResId = priority != null ? priority.getDrawableResId() : R.drawable.ic_launcher_foreground;




        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(iconResId)
                .setContentTitle(title)
                .setContentText(entity.title + " - " + entity.message)
                .setPriority(importance)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager.notify((int) entity.id, builder.build());
    }
}
