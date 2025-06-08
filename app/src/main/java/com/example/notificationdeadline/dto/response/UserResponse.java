package com.example.notificationdeadline.dto.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserResponse {
    @NonNull
    private String userName;
    @Nullable
    private String imageUrl;
    @Nullable
    private String description;
    @NonNull
    private String email;
    @Nullable
    private String phone;
    @Nullable
    private String birthday;

    public UserResponse() {}

    public UserResponse(@NonNull String userName, @Nullable String imageUrl, @Nullable String description,
                        @NonNull String email, @Nullable String phone, @Nullable String birthday) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    // Getter & Setter
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

    // Optional: Debug
    @Override
    public String toString() {
        return "UserResponse{" +
                "userName='" + userName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
