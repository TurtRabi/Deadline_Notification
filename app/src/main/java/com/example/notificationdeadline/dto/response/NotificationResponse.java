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
    private String category;
    private String tags;
    private String customSoundUri;
    private int dayOfWeek;
    private int dayOfMonth;
    private int month;
    private int year;

    public NotificationResponse() {}

    public NotificationResponse(int id, @NonNull String title, @NonNull String content, long time, int priority, int status, boolean isSuccess, boolean isRecurring, int recurrenceType, int recurrenceValue, String category, String tags, String customSoundUri, int dayOfWeek, int dayOfMonth, int month, int year) {
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
        this.category = category;
        this.tags = tags;
        this.customSoundUri = customSoundUri;
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
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

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getCustomSoundUri() { return customSoundUri; }
    public void setCustomSoundUri(String customSoundUri) { this.customSoundUri = customSoundUri; }

    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public int getDayOfMonth() { return dayOfMonth; }
    public void setDayOfMonth(int dayOfMonth) { this.dayOfMonth = dayOfMonth; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

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
                ", category='" + category + "'" +
                ", tags='" + tags + "'" +
                ", customSoundUri='" + customSoundUri + "'" +
                ", dayOfWeek=" + dayOfWeek +
                ", dayOfMonth=" + dayOfMonth +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
