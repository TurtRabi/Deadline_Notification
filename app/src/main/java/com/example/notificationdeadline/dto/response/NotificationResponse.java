package com.example.notificationdeadline.dto.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotificationResponse {
    private int id;
    @NonNull
    private String title;
    @NonNull
    private String content;
    private long time;
    private int priority;
    private int status;
    private boolean isSuccess;
    private boolean isRecurring;
    private int recurrenceType;
    private int recurrenceValue;

    public NotificationResponse() {}

    public NotificationResponse(int id, @NonNull String title, @NonNull String content, long time, int priority, int status, boolean isSuccess, boolean isRecurring, int recurrenceType, int recurrenceValue) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.priority = priority;
        this.status = status;
        this.isSuccess = isSuccess;
        this.isRecurring = isRecurring;
        this.recurrenceType = recurrenceType;
        this.recurrenceValue = recurrenceValue;
    }

    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    @NonNull
    public String getContent() { return content; }
    public void setContent(@NonNull String content) { this.content = content; }

    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }

    public int getRecurrenceType() { return recurrenceType; }
    public void setRecurrenceType(int recurrenceType) { this.recurrenceType = recurrenceType; }

    public int getRecurrenceValue() { return recurrenceValue; }
    public void setRecurrenceValue(int recurrenceValue) { this.recurrenceValue = recurrenceValue; }

    // Optional: dễ debug log
    @Override
    public String toString() {
        return "NotificationResponse{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", content='" + content + "'" +
                ", time=" + time +
                ", priority=" + priority +
                ", status=" + status +
                ", isSuccess=" + isSuccess +
                ", isRecurring=" + isRecurring +
                ", recurrenceType=" + recurrenceType +
                ", recurrenceValue=" + recurrenceValue +
                '}';
    }
}
