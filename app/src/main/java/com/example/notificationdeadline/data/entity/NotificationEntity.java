package com.example.notificationdeadline.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "notifications",
        indices = @Index(value = {"time_millis"})
)
public class NotificationEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(defaultValue = "0")
    private int id;

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String title = "";

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String message = "";

    @ColumnInfo(name = "time_millis")
    private long timeMillis;

    @ColumnInfo(defaultValue = "0")
    private int status;

    @ColumnInfo(defaultValue = "0")
    private int priority;

    @ColumnInfo(defaultValue = "0")
    private boolean isSuccess;

    public NotificationEntity() {}

    @Ignore
    public NotificationEntity(@NonNull String title, @NonNull String message, long timeMillis) {
        this.title = title;
        this.message = message;
        this.timeMillis = timeMillis;
    }

    @Ignore
    public NotificationEntity(@NonNull String title, @NonNull String message, long timeMillis, int status, int priority, boolean isSuccess) {
        this.title = title;
        this.message = message;
        this.timeMillis = timeMillis;
        this.status = status;
        this.priority = priority;
        this.isSuccess = isSuccess;
    }

    public NotificationEntity(int id, @NonNull String title, @NonNull String message, long timeMillis, int status, int priority, boolean isSuccess) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timeMillis = timeMillis;
        this.status = status;
        this.priority = priority;
        this.isSuccess = isSuccess;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    @NonNull
    public String getMessage() { return message; }
    public void setMessage(@NonNull String message) { this.message = message; }

    public long getTimeMillis() { return timeMillis; }
    public void setTimeMillis(long timeMillis) { this.timeMillis = timeMillis; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }
}
