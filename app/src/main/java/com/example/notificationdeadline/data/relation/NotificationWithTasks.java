package com.example.notificationdeadline.data.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;

import java.util.List;

public class NotificationWithTasks {
    @Embedded
    public NotificationEntity notification;

    @Relation(
            parentColumn = "id",
            entityColumn = "notificationId"
    )
    public List<TaskEntity> tasks;
}
