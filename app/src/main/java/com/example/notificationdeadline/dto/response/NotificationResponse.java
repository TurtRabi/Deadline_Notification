package com.example.notificationdeadline.dto.response;

public class NotificationResponse {
    private int id;
    private String title;
    private String content;
    private long time;
    private int priority;
    private int status;
    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public NotificationResponse() {
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public NotificationResponse(int id, String title, String content, long time, int priority, int status,boolean isSuccess) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.priority = priority;
        this.status = status;
        this.isSuccess=isSuccess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
