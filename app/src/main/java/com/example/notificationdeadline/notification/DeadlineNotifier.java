package com.example.notificationdeadline.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.dto.Enum.PriorityEnum;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.service.NotificationService;
import com.example.notificationdeadline.ui.activity_main;

public class DeadlineNotifier {

    private final Context context;
    private static final String CHANNEL_ID = "deadline_channel";
    private static NotificationService notificationService;

    public static final int HIGH_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    public static final int MEDIUM_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
    public static final int LOW_IMPORTANCE = NotificationManager.IMPORTANCE_LOW;
    public static final int OVER_IMPORTANCE = NotificationManager.IMPORTANCE_MAX;

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
                        HIGH_IMPORTANCE
                );
                channel.setDescription("Thông báo các deadline khẩn cấp hoặc gần tới");
                //Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.my_custom_sound);
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                channel.setSound(soundUri, audioAttributes);
                manager.createNotificationChannel(channel);
            }
        }

        // Intent để mở app khi bấm thông báo
        Intent intent = new Intent(context, activity_main.class);
        intent.putExtra("notification_id", entity.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                (int) entity.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        notificationService = new NotificationService(context);
        NotificationEntity current = notificationService.getNotificationById(entity.getId()); // Sử dụng phương thức đồng bộ
        if (current != null) {
            int status = current.getStatus();
            boolean isSuccess = current.isSuccess();

            if (status != StatusEnum.SUCCESS.getValue() && status!= StatusEnum.OVERDEADLINE.getValue()) {
                // Nếu chưa hoàn thành, thì tăng cấp trạng thái
                status = Math.min(status + 1, StatusEnum.OVERDEADLINE.getValue());
            } else {
                // Nếu đã hoàn thành, kiểm tra isSuccess
                if (isSuccess ==false) {
                    status = StatusEnum.OVERDEADLINE.getValue();
                }else{
                    status = StatusEnum.SUCCESS.getValue();
                }
            }

            notificationService.updateStatus(status, current.getId());
        }



        PriorityEnum priority = PriorityEnum.fromValue(entity.getPriority());
        int iconResId = priority != null ? priority.getDrawableResId() : R.drawable.ic_launcher_foreground;

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(iconResId)
                .setContentTitle(title)
                .setContentText(entity.getTitle() + " - " + entity.getMessage())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(entity.getTitle() + " - " + entity.getMessage()))
                .setPriority(importance)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager.notify((int) entity.getId(), builder.build());
    }
}
