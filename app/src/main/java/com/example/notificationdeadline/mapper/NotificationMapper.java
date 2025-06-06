package com.example.notificationdeadline.mapper;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.dto.response.NotificationResponse;

public class NotificationMapper {

    public  static NotificationEntity toEntity(NotificationRequest request){
        NotificationEntity entity = new NotificationEntity(request.getTitle(),request.getContent(),request.getTime(), request.getStatus(),request.getPriority(),request.isSuccess());
        return entity;
    }

    public  static NotificationResponse toResponse(NotificationEntity entity){
        return new NotificationResponse(entity.id,entity.title,entity.message,entity.timeMillis,entity.status, entity.priority,entity.isSuccess);
    }
}
