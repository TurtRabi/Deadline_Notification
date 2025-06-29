package com.example.notificationdeadline.dto.request;

public class RecurringDeadlineRequest {
    private String title;
    private String description;
    private int priority;
    private int recurrenceType;
    private String time;
    private int dayOfWeek;
    private int dayOfMonth;
    private int month;
    private boolean isActive;

    public RecurringDeadlineRequest(String title, String description, int priority, int recurrenceType, String time, int dayOfWeek, int dayOfMonth, int month, boolean isActive) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.recurrenceType = recurrenceType;
        this.time = time;
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.isActive = isActive;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getRecurrenceType() {
        return String.valueOf(recurrenceType);
    }

    public String getTime() {
        return time;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public boolean isActive() {
        return isActive;
    }
}
