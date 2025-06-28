package com.example.notificationdeadline.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.repository.NotificationHistoryRepository;

import java.util.Calendar;
import java.util.List;

public class NotificationHistoryService {
    private final NotificationHistoryRepository repository;

    public NotificationHistoryService(Context context) {
        repository = new NotificationHistoryRepository(context);
    }

    // Lưu lịch sử notification (chạy async)
    public void logNotification(String title, String message, boolean isSuccess, String urlImage) {
        NotificationHistoryEntity notification = new NotificationHistoryEntity(
                title,
                message,
                System.currentTimeMillis(),
                isSuccess,
                false,
                urlImage
        );
        repository.insert(notification);
    }

    public LiveData<List<NotificationHistoryEntity>> getAllHistory() {
        return repository.getAllNotifications();
    }

    public void markAsRead(int id) {
        repository.updateIsRead(id, true);
    }

    public void markAsUnread(int id) {
        repository.updateIsRead(id, false);
    }

    public LiveData<Integer> getUnreadCount() {
        return repository.countUnread();
    }

    public LiveData<List<NotificationHistoryEntity>> getTodayNotifications() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDay = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endOfDay = calendar.getTimeInMillis();

        return repository.getNotificationsByDay(startOfDay, endOfDay);
    }

    public void updateNotification(NotificationHistoryEntity notification) {
        repository.update(notification);
    }

    public void insertNotificationHistory(NotificationHistoryEntity notificationHistory) {
        repository.insert(notificationHistory);
    }

    public void clearAllHistory() {
        repository.deleteAll();
    }
}
