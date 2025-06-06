package com.example.notificationdeadline.data.entity;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "notifications")
public class NotificationEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public  int id;
    public String title;
    public String message;
    public long timeMillis;
    public  int status;
    public int priority;
    public boolean isSuccess;

    public NotificationEntity() {
    }

    public NotificationEntity(String title, String message, long timeMillis) {
        this.title = title;
        this.message = message;
        this.timeMillis = timeMillis;

    }

    public NotificationEntity(String title, String message, long timeMillis, int status, int priority,boolean isSuccess) {
        this.title = title;
        this.message = message;
        this.timeMillis = timeMillis;
        this.status = status;
        this.priority = priority;
        this.isSuccess= isSuccess;
    }
}
