package com.example.notificationdeadline.data.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;

import java.util.List;

public class NotificationWithTasks {
    @Embedded
    private NotificationEntity notification;

    @Relation(
            parentColumn = "id",
            entityColumn = "notificationId"
    )
    private List<TaskEntity> tasks;


    public NotificationWithTasks() {}


    public NotificationEntity getNotification() {
        return notification;
    }

    public void setNotification(NotificationEntity notification) {
        this.notification = notification;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
