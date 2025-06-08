package com.example.notificationdeadline.mapper;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.dto.response.NotificationResponse;

public class NotificationMapper {

    public static NotificationEntity toEntity(NotificationRequest request) {
        return new NotificationEntity(
                request.getTitle(),
                request.getContent(),
                request.getTime(),
                request.getStatus(),
                request.getPriority(),
                request.isSuccess()
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
                entity.isSuccess()
        );
    }


    public static NotificationRequest toRequest(NotificationEntity entity) {
        return new NotificationRequest(
                entity.getTitle(),
                entity.getMessage(),
                entity.getTimeMillis(),
                entity.getPriority(),
                entity.getStatus(),
                entity.isSuccess()
        );
    }
}
