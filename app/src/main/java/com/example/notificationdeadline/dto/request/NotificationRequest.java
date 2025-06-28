package com.example.notificationdeadline.dto.request;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotificationRequest implements Parcelable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public NotificationRequest() {}

    public NotificationRequest(@NonNull String title, @NonNull String content, long time, int priority, int status, boolean isSuccess, boolean isRecurring, int recurrenceType, int recurrenceValue) {
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

    public NotificationRequest(@NonNull int id, @NonNull String title, @NonNull String content, long time, int priority, int status, boolean isSuccess, boolean isRecurring, int recurrenceType, int recurrenceValue) {
        this.id=id;
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

    protected NotificationRequest(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        time = in.readLong();
        priority = in.readInt();
        status = in.readInt();
        isSuccess = in.readByte() != 0;
        isRecurring = in.readByte() != 0;
        recurrenceType = in.readInt();
        recurrenceValue = in.readInt();
    }

    public static final Creator<NotificationRequest> CREATOR = new Creator<NotificationRequest>() {
        @Override
        public NotificationRequest createFromParcel(Parcel in) {
            return new NotificationRequest(in);
        }

        @Override
        public NotificationRequest[] newArray(int size) {
            return new NotificationRequest[size];
        }
    };

    // Getter & Setter
    public boolean isSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean success) {
        isSuccess = success;
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
    @NonNull
    public String getTitle() {
        return title;
    }
    public void setTitle(@NonNull String title) {
        this.title = title;
    }
    @NonNull
    public String getContent() {
        return content;
    }
    public void setContent(@NonNull String content) {
        this.content = content;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public boolean isRecurring() {
        return isRecurring;
    }
    public void setRecurring(boolean recurring) {
        isRecurring = recurring;}

    public int getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(int recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public int getRecurrenceValue() {
        return recurrenceValue;
    }

    public void setRecurrenceValue(int recurrenceValue) {
        this.recurrenceValue = recurrenceValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(time);
        dest.writeInt(priority);
        dest.writeInt(status);
        dest.writeByte((byte) (isSuccess ? 1 : 0));
        dest.writeByte((byte) (isRecurring ? 1 : 0));
        dest.writeInt(recurrenceType);
        dest.writeInt(recurrenceValue);
    }
}
