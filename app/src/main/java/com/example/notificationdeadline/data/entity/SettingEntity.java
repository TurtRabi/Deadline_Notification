package com.example.notificationdeadline.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings")
public class SettingEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(defaultValue = "")
    private String key = "";

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String value = "";

    public SettingEntity() {}

    @Ignore
    public SettingEntity(@NonNull String key, @NonNull String value) {
        this.key = key;
        this.value = value;
    }

    // Getter & Setter
    @NonNull
    public String getKey() { return key; }
    public void setKey(@NonNull String key) { this.key = key; }

    @NonNull
    public String getValue() { return value; }
    public void setValue(@NonNull String value) { this.value = value; }
}
