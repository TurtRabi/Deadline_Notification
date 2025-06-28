package com.example.notificationdeadline.mapper;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.dto.response.NotificationResponse;

public class NotificationMapper {

    public static NotificationEntity toEntity(NotificationRequest request) {
        return new NotificationEntity(
                request.getId(),
                request.getTitle(),
                request.getContent(),
                request.getTime(),
                request.getStatus(),
                request.getPriority(),
                request.isSuccess(),
                request.isRecurring(),
                request.getRecurrenceType(),
                request.getRecurrenceValue()
        );
    }

    public static NotificationResponse toResponse(NotificationEntity entity) {
        return new NotificationResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getTimeMillis(),
                entity.getPriority(),
                entity.getStatus(),
                entity.isSuccess(),
                entity.isRecurring(),
                entity.getRecurrenceType(),
                entity.getRecurrenceValue()
        );
    }


    public static NotificationRequest toRequest(NotificationEntity entity) {
        return new NotificationRequest(
                entity.getId(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getTimeMillis(),
                entity.getPriority(),
                entity.getStatus(),
                entity.isSuccess(),
                entity.isRecurring(),
                entity.getRecurrenceType(),
                entity.getRecurrenceValue()
        );
    }
}
