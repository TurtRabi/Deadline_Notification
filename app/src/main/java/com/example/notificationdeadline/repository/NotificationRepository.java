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

    public void updateNotification(final NotificationEntity newNotification) {
        executor.execute(() -> {
            NotificationEntity existingNotification = db.notificationDao().getNotificationById(newNotification.getId());
            if (existingNotification != null) {
                // Update all fields except timeMillis
                existingNotification.setTitle(newNotification.getTitle());
                existingNotification.setMessage(newNotification.getMessage());
                existingNotification.setStatus(newNotification.getStatus());
                existingNotification.setPriority(newNotification.getPriority());
                existingNotification.setSuccess(newNotification.isSuccess());
                existingNotification.setRecurring(newNotification.isRecurring());
                existingNotification.setRecurrenceType(newNotification.getRecurrenceType());
                existingNotification.setRecurrenceValue(newNotification.getRecurrenceValue());
                existingNotification.setCategory(newNotification.getCategory());
                existingNotification.setTags(newNotification.getTags());
                existingNotification.setCustomSoundUri(newNotification.getCustomSoundUri());
                existingNotification.setDayOfWeek(newNotification.getDayOfWeek());
                existingNotification.setDayOfMonth(newNotification.getDayOfMonth());
                existingNotification.setMonth(newNotification.getMonth());
                existingNotification.setYear(newNotification.getYear());

                db.notificationDao().update(existingNotification);
            }
        });
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

    public List<NotificationEntity> getAllNotificationsSync() {
        return db.notificationDao().getAllList();
    }

    public LiveData<List<NotificationEntity>> getAllNotification(long startTime, long endTime, int isSuccess) {
        return db.notificationDao().getAllByDay2(startTime, endTime, isSuccess);
    }

    public LiveData<List<NotificationEntity>> getAllNotification(long startTime, long endTime) {
        return db.notificationDao().getAllByDay(startTime, endTime);
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

    public LiveData<List<NotificationEntity>> searchNotificationsByKeywordAndTag(String keyword, String tag) {
        return db.notificationDao().searchNotificationsByKeywordAndTag(keyword, tag);
    }

    

    public LiveData<List<NotificationEntity>> getAllCompletedNotifications() {
        return db.notificationDao().getAllCompletedNotifications();
    }

    public LiveData<List<NotificationEntity>> getAllFixedDeadlines() {
        return db.notificationDao().getAllFixedDeadlines();
    }

    public LiveData<List<NotificationEntity>> getNotificationsByCategory(String category) {
        return db.notificationDao().getNotificationsByCategory(category);
    }

    public LiveData<List<NotificationEntity>> getNotificationsByTag(String tag) {
        return db.notificationDao().getNotificationsByTag(tag);
    }

    public LiveData<List<NotificationEntity>> getNotificationsByCategoryAndTag(String category, String tag) {
        return db.notificationDao().getNotificationsByCategoryAndTag(category, tag);
    }
}