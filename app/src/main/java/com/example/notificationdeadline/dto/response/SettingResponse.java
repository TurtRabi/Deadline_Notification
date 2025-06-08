package com.example.notificationdeadline.dto.response;

import androidx.annotation.NonNull;

public class SettingResponse {
    @NonNull
    private String key;
    @NonNull
    private String value;

    public SettingResponse() {}

    public SettingResponse(@NonNull String key, @NonNull String value) {
        this.key = key;
        this.value = value;
    }

    @NonNull
    public String getKey() {
        return key;
    }
    public void setKey(@NonNull String key) {
        this.key = key;
    }

    @NonNull
    public String getValue() {
        return value;
    }
    public void setValue(@NonNull String value) {
        this.value = value;
    }

    // Optional: Log/debug cho nhanh
    @Override
    public String toString() {
        return "SettingResponse{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
