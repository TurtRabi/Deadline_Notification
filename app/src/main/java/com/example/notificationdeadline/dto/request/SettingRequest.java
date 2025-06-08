package com.example.notificationdeadline.dto.request;

import androidx.annotation.NonNull;

public class SettingRequest {
    @NonNull
    private String key;
    @NonNull
    private String value;

    public SettingRequest() {}

    public SettingRequest(@NonNull String key, @NonNull String value) {
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

    // Tuỳ chọn - dễ debug
    @Override
    public String toString() {
        return "SettingRequest{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
