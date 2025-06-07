package com.example.notificationdeadline.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tasks",
        foreignKeys = @ForeignKey(
                entity = NotificationEntity.class,
                parentColumns = "id",
                childColumns = "notificationId",
                onDelete = ForeignKey.CASCADE
        )
)
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int taskId;

    @NonNull
    public int notificationId;

    public String content;
    public boolean isDone;

    public TaskEntity(int notificationId, String content, boolean isDone) {
        this.notificationId = notificationId;
        this.content = content;
        this.isDone = isDone;
    }
}