package com.example.notificationdeadline.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.NotificationEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NotificationRepository {
    private final AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public NotificationRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public void insertNotification(NotificationEntity notification, OnInsertCallback callback) {
        executor.execute(() -> {
            long id = db.notificationDao().insert(notification); // giờ lấy được id
            if (callback != null) {
                callback.onInsert(id);
            }
        });
    }

    // NotificationRepository
    public NotificationEntity getNotificationById(int id) {
        return db.notificationDao().getNotificationById(id);
    }


    public interface OnInsertCallback {
        void onInsert(long id);
    }


    public void deleteNotification(final NotificationEntity notification) {
        executor.execute(() -> db.notificationDao().delete(notification));
    }

    public void updateNotification(final NotificationEntity notification) {
        executor.execute(() -> db.notificationDao().update(notification));
    }

    public void updateSuccessDeadline(final int id) {
        executor.execute(() -> db.notificationDao().updateSuccessDeadline(id));
    }

    public void updateNotSuccessDeadline(final int id) {
        executor.execute(() -> db.notificationDao().updateNotSuccessDeadline(id));
    }

    public void updateStatus(final int status, final int id) {
        executor.execute(() -> db.notificationDao().updateStatus(status, id));
    }

    public LiveData<List<NotificationEntity>> getAllNotifications() {
        return db.notificationDao().getAll();
    }

    public LiveData<List<NotificationEntity>> getAllNotification(long startTime, long endTime, int isSuccess) {
        return db.notificationDao().getAllByDay2(startTime, endTime, isSuccess);
    }

    public List<NotificationEntity> getAllNotification1(long startTime, long endTime, int isSuccess) {
        return db.notificationDao().getAllByDay1(startTime, endTime, isSuccess);
    }

    public LiveData<List<NotificationEntity>> getAllByStatus(int status) {
        return db.notificationDao().getAllByStatus(status);
    }

    public LiveData<List<NotificationEntity>> getAllByPriority(int priority) {
        return db.notificationDao().getAllByPriority(priority);
    }

    public LiveData<List<NotificationEntity>> searchNotifications(String keyword) {
        return db.notificationDao().searchNotifications(keyword);
    }

    public LiveData<List<NotificationEntity>> getAllRecurringNotifications() {
        return db.notificationDao().getAllRecurringNotifications();
    }

    public LiveData<List<NotificationEntity>> getRecurringNotificationsByType(int recurrenceType) {
        return db.notificationDao().getRecurringNotificationsByType(recurrenceType);
    }

    public LiveData<List<NotificationEntity>> getAllCompletedNotifications() {
        return db.notificationDao().getAllCompletedNotifications();
    }

    public LiveData<List<NotificationEntity>> getAllFixedDeadlines() {
        return db.notificationDao().getAllFixedDeadlines();
    }
}