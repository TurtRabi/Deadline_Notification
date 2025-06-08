package com.example.notificationdeadline.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tasks",
        foreignKeys = @ForeignKey(
                entity = NotificationEntity.class,
                parentColumns = "id",
                childColumns = "notificationId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = "notificationId")
)
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int taskId;

    @NonNull
    private int notificationId;

    @ColumnInfo(defaultValue = "")
    @Nullable
    private String content = "";

    @ColumnInfo(defaultValue = "0")
    private boolean isDone;

    public TaskEntity(int notificationId, @Nullable String content, boolean isDone) {
        this.notificationId = notificationId;
        this.content = content;
        this.isDone = isDone;
    }

    @Ignore
    public TaskEntity() {}

    // Getter & Setter
    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    @Nullable
    public String getContent() { return content; }
    public void setContent(@Nullable String content) { this.content = content; }

    public boolean isDone() { return isDone; }
    public void setDone(boolean done) { isDone = done; }
}
