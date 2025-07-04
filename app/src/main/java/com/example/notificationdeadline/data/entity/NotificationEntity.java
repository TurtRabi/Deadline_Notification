package com.example.notificationdeadline.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "notifications",
        indices = @Index(value = {"time_millis"})
)
public class NotificationEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(defaultValue = "0")
    private int id;

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String title = "";

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String message = "";

    @ColumnInfo(name = "time_millis")
    private long timeMillis;

    @ColumnInfo(defaultValue = "0")
    private int status;

    @ColumnInfo(defaultValue = "0")
    private int priority;

    @ColumnInfo(defaultValue = "0")
    private boolean isSuccess;

    @ColumnInfo(name = "is_recurring", defaultValue = "0")
    private boolean isRecurring;

    @ColumnInfo(name = "recurrence_type", defaultValue = "0")
    private int recurrenceType; // 0: None, 1: Daily, 2: Weekly, 3: Monthly, 4: Yearly

    @ColumnInfo(name = "recurrence_value", defaultValue = "0")
    private int recurrenceValue; // Day of week for weekly, day of month for monthly, etc.

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String category = "";

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String tags = "";

    @Nullable
    @ColumnInfo(name = "custom_sound_uri")
    private String customSoundUri;

    @ColumnInfo(name = "day_of_week", defaultValue = "0")
    private int dayOfWeek;

    @ColumnInfo(name = "day_of_month", defaultValue = "0")
    private int dayOfMonth;

    @ColumnInfo(name = "month", defaultValue = "0")
    private int month;

    @ColumnInfo(name = "year", defaultValue = "0")
    private int year;

    public NotificationEntity() {}

    @Ignore
    public NotificationEntity(@NonNull String title, @NonNull String message, long timeMillis) {
        this.title = title;
        this.message = message;
        this.timeMillis = timeMillis;
    }

    @Ignore
    public NotificationEntity(@NonNull String title, @NonNull String message, long timeMillis, int status, int priority, boolean isSuccess, boolean isRecurring, int recurrenceType, int recurrenceValue, @NonNull String category, @NonNull String tags, @Nullable String customSoundUri, int dayOfWeek, int dayOfMonth, int month, int year) {
        this.title = title;
        this.message = message;
        this.timeMillis = timeMillis;
        this.status = status;
        this.priority = priority;
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

    @Ignore
    public NotificationEntity(int id, @NonNull String title, @NonNull String message, long timeMillis, int status, int priority, boolean isSuccess, boolean isRecurring, int recurrenceType, int recurrenceValue, @NonNull String category, @NonNull String tags, @Nullable String customSoundUri, int dayOfWeek, int dayOfMonth, int month, int year) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timeMillis = timeMillis;
        this.status = status;
        this.priority = priority;
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

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    @NonNull
    public String getMessage() { return message; }
    public void setMessage(@NonNull String message) { this.message = message; }

    public long getTimeMillis() { return timeMillis; }
    public void setTimeMillis(long timeMillis) { this.timeMillis = timeMillis; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public int getRecurrenceType() { return recurrenceType; }
    public void setRecurrenceType(int recurrenceType) { this.recurrenceType = recurrenceType; }

    public int getRecurrenceValue() { return recurrenceValue; }
    public void setRecurrenceValue(int recurrenceValue) { this.recurrenceValue = recurrenceValue; }

    @NonNull
    public String getCategory() { return category; }
    public void setCategory(@NonNull String category) { this.category = category; }

    @NonNull
    public String getTags() { return tags; }
    public void setTags(@NonNull String tags) { this.tags = tags; }

    @Nullable
    public String getCustomSoundUri() { return customSoundUri; }
    public void setCustomSoundUri(@Nullable String customSoundUri) { this.customSoundUri = customSoundUri; }

    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public int getDayOfMonth() { return dayOfMonth; }
    public void setDayOfMonth(int dayOfMonth) { this.dayOfMonth = dayOfMonth; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
