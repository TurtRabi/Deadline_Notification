package com.example.notificationdeadline.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification_history")
public class NotificationHistoryEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String title = "";

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String message = "";

    @ColumnInfo(name = "sent_time_millis")
    private long sentTimeMillis;

    @ColumnInfo(defaultValue = "0")
    private boolean isSuccess;

    @ColumnInfo(defaultValue = "0")
    private boolean isRead;

    @Nullable
    private String urlImage;

    public NotificationHistoryEntity(@NonNull String title, @NonNull String message, long sentTimeMillis, boolean isSuccess, boolean isRead, @Nullable String urlImage) {
        this.title = title;
        this.message = message;
        this.sentTimeMillis = sentTimeMillis;
        this.isSuccess = isSuccess;
        this.isRead = isRead;
        this.urlImage = urlImage;
    }

    @Ignore
    public NotificationHistoryEntity(@NonNull String title, @NonNull String message, long sentTimeMillis, boolean isSuccess) {
        this(title, message, sentTimeMillis, isSuccess, false, null);
    }

    @Ignore
    public NotificationHistoryEntity() {}

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    @NonNull
    public String getMessage() { return message; }
    public void setMessage(@NonNull String message) { this.message = message; }

    public long getSentTimeMillis() { return sentTimeMillis; }
    public void setSentTimeMillis(long sentTimeMillis) { this.sentTimeMillis = sentTimeMillis; }

    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    @Nullable
    public String getUrlImage() { return urlImage; }
    public void setUrlImage(@Nullable String urlImage) { this.urlImage = urlImage; }
}
