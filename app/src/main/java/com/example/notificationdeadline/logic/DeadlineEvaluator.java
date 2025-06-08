package com.example.notificationdeadline.logic;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.service.NotificationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeadlineEvaluator {

    private final NotificationService service;

    public static final int HIGH_ALERT = 2;
    public static final int MEDIUM_ALERT = 1;
    public static final int LOW_ALERT = 0;
    public static final int OVER_ALERT = -1;

    public DeadlineEvaluator(NotificationService service) {
        this.service = service;
    }

    public Map<Integer, List<NotificationEntity>> evaluateDeadlinesByUrgency() {
        long now = System.currentTimeMillis();
        long highThreshold = 6 * 60 * 60 * 1000;
        long mediumThreshold = 10 * 60 * 60 * 1000;
        long lowThreshold = 18 * 60 * 60 * 1000;

        List<NotificationEntity> all = service.fetchAllNotificationsByDay1();

        Map<Integer, List<NotificationEntity>> result = new HashMap<>();
        result.put(HIGH_ALERT, new ArrayList<>());
        result.put(MEDIUM_ALERT, new ArrayList<>());
        result.put(LOW_ALERT, new ArrayList<>());
        result.put(OVER_ALERT, new ArrayList<>());

        for (NotificationEntity entity : all) {
            long diff = entity.getTimeMillis() - now;

            if (diff < 0) {
                result.get(OVER_ALERT).add(entity);
            } else if (diff <= highThreshold) {
                result.get(HIGH_ALERT).add(entity);
            } else if (diff <= mediumThreshold) {
                result.get(MEDIUM_ALERT).add(entity);
            } else if (diff <= lowThreshold) {
                result.get(LOW_ALERT).add(entity);
            }

        }

        return result;
    }
}
