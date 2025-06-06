package com.example.notificationdeadline.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification_history")
public class NotificationHistoryEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String title;
    public String message;
    public long sentTimeMillis;
    public boolean isSuccess;
    public boolean isRead;
    public String urlImage;


    public NotificationHistoryEntity(String title, String message, long sentTimeMillis, boolean isSuccess, boolean isRead, String urlImage) {
        this.title = title;
        this.message = message;
        this.sentTimeMillis = sentTimeMillis;
        this.isSuccess = isSuccess;
        this.isRead = isRead;
        this.urlImage = urlImage;
    }

    @Ignore
    public NotificationHistoryEntity(String title, String message, long sentTimeMillis, boolean isSuccess) {
        this(title, message, sentTimeMillis, isSuccess, false, null);
    }


    @Ignore
    public NotificationHistoryEntity() {
    }
}
