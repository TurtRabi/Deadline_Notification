package com.example.notificationdeadline.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificationHistoryRepository {
    private final AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public NotificationHistoryRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public void insert(final NotificationHistoryEntity notification) {
        executor.execute(() -> db.notificationHistoryDao().insert(notification));
    }

    public void update(final NotificationHistoryEntity notification) {
        executor.execute(() -> db.notificationHistoryDao().update(notification));
    }

    public void updateIsRead(final int id, final boolean isRead) {
        executor.execute(() -> db.notificationHistoryDao().updateIsRead(id, isRead));
    }

    public LiveData<List<NotificationHistoryEntity>> getAllNotifications() {
        return db.notificationHistoryDao().getAllNotifications();
    }

    public LiveData<List<NotificationHistoryEntity>> getNotificationsByDay(long startTime, long endTime) {
        return db.notificationHistoryDao().getNotificationsByDay(startTime, endTime);
    }

    public LiveData<Integer> countUnread() {
        return db.notificationHistoryDao().countUnreadNotifications();
    }

    public void deleteAll() {
        executor.execute(() -> db.notificationHistoryDao().deleteAll());
    }
}
