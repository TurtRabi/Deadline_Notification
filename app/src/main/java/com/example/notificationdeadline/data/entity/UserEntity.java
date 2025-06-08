package com.example.notificationdeadline.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int userId;

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String userName = "";

    @Nullable
    @ColumnInfo(defaultValue = "")
    private String imageUrl = "";

    @Nullable
    @ColumnInfo(defaultValue = "")
    private String description = "";

    @NonNull
    @ColumnInfo(defaultValue = "")
    private String email = "";

    @Nullable
    @ColumnInfo(defaultValue = "")
    private String phone = "";

    @Nullable
    @ColumnInfo(defaultValue = "")
    private String birthday = "";

    public UserEntity() {}

    @Ignore
    public UserEntity(@NonNull String userName, @Nullable String imageUrl, @Nullable String description, @NonNull String email, @Nullable String phone, @Nullable String birthday) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    @Ignore
    public UserEntity(int userId, @NonNull String userName, @Nullable String imageUrl, @Nullable String description, @NonNull String email, @Nullable String phone, @Nullable String birthday) {
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    // Getter & Setter
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    @NonNull
    public String getUserName() { return userName; }
    public void setUserName(@NonNull String userName) { this.userName = userName; }

    @Nullable
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(@Nullable String imageUrl) { this.imageUrl = imageUrl; }

    @Nullable
    public String getDescription() { return description; }
    public void setDescription(@Nullable String description) { this.description = description; }

    @NonNull
    public String getEmail() { return email; }
    public void setEmail(@NonNull String email) { this.email = email; }

    @Nullable
    public String getPhone() { return phone; }
    public void setPhone(@Nullable String phone) { this.phone = phone; }

    @Nullable
    public String getBirthday() { return birthday; }
    public void setBirthday(@Nullable String birthday) { this.birthday = birthday; }
}
