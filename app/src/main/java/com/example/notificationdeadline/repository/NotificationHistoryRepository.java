package com.example.notificationdeadline.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;

import java.util.List;

public class NotificationHistoryRepository {
    private final AppDatabase db;
    public NotificationHistoryRepository(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class,"notification_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
    public long insert(NotificationHistoryEntity notification) {
        return db.notificationHistoryDao().insert(notification);
    }

    public int update(NotificationHistoryEntity notification) {
        return db.notificationHistoryDao().update(notification);
    }

    public int updateIsRead(int id, boolean isRead) {
        return db.notificationHistoryDao().updateIsRead(id, isRead);
    }

    public List<NotificationHistoryEntity> getAllNotifications() {
        return db.notificationHistoryDao().getAllNotifications();
    }

    public List<NotificationHistoryEntity> getNotificationsByDay(long startTime, long endTime) {
        return db.notificationHistoryDao().getNotificationsByDay(startTime, endTime);
    }

    public int countUnread() {
        return db.notificationHistoryDao().countUnreadNotifications();
    }
}
