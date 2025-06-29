package com.example.notificationdeadline.mapper;

import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;
import com.example.notificationdeadline.dto.request.RecurringDeadlineRequest;

public class RecurringDeadlineMapper {

    public static RecurringDeadlineEntity toEntity(RecurringDeadlineRequest request) {

        RecurringDeadlineEntity entity = new RecurringDeadlineEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setPriority(request.getPriority());
        entity.setRecurrenceType(Integer.parseInt(request.getRecurrenceType()));
        entity.setTime(request.getTime());
        entity.setDayOfWeek(request.getDayOfWeek());
        entity.setDayOfMonth(request.getDayOfMonth());
        entity.setMonth(request.getMonth());
        entity.setActive(request.isActive());
        return entity;
    }
}
