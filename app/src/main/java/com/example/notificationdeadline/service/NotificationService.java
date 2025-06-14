package com.example.notificationdeadline.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.mapper.NotificationMapper;
import com.example.notificationdeadline.repository.NotificationRepository;

import java.util.Calendar;
import java.util.List;

public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(Context context) {
        this.notificationRepository = new NotificationRepository(context);
    }

    public void addNotification(NotificationRequest request, NotificationRepository.OnInsertCallback callback) {
        NotificationEntity entity = NotificationMapper.toEntity(request);
        notificationRepository.insertNotification(entity, callback);
    }


    public void removeNotification(NotificationEntity notification) {
        notificationRepository.deleteNotification(notification);
    }

    public LiveData<List<NotificationEntity>> fetchAllNotifications() {
        return notificationRepository.getAllNotifications();
    }

    public LiveData<List<NotificationEntity>> fetchAllNotificationsByDay(int isSuccess) {
        Calendar calendar = Calendar.getInstance();

        // Set to 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

        // Set to 23:59:59
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endTime = calendar.getTimeInMillis();

        return notificationRepository.getAllNotification(startTime, endTime,isSuccess);
    }
    public LiveData<List<NotificationEntity>> fetchAllNotificationsByDay(long startTime, long endTime,int isSuccess) {
        return notificationRepository.getAllNotification(startTime, endTime,isSuccess);
    }


    public LiveData<List<NotificationEntity>> fetchAllNotificationsByStatus(int status) {
        return notificationRepository.getAllByStatus(status);
    }

    public LiveData<List<NotificationEntity>> fetchAllNotificationsByPriority(int priority) {
        return notificationRepository.getAllByPriority(priority);
    }

    public void updateSuccessDeadline(int id) {
        notificationRepository.updateSuccessDeadline(id);
    }
    public void updateNotSuccessDeadline(int id) {
        notificationRepository.updateNotSuccessDeadline(id);
    }

    public void updateStatus(int status, int id) {
        notificationRepository.updateStatus(status, id);
    }

    public List<NotificationEntity> fetchAllNotificationsByDay1() {
        Calendar calendar = Calendar.getInstance();

        // Set to 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

        // Set to 23:59:59
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endTime = calendar.getTimeInMillis();

        return notificationRepository.getAllNotification1(startTime, endTime,0);
    }

    public LiveData<List<NotificationEntity>> searchNotifications(String keyword) {
        return notificationRepository.searchNotifications(keyword);
    }
}
