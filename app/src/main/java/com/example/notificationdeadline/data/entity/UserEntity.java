package com.example.notificationdeadline.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    public UserEntity() {
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int userId;
    public String UserName;
    public String ImageUrl;
    public String Description;
    public String Email;
    public String phone;
    public String birdday;


    public UserEntity(String userName, String imageUrl, String description, String email, String phone, String birdday) {
        UserName = userName;
        ImageUrl = imageUrl;
        Description = description;
        Email = email;
        this.phone = phone;
        this.birdday = birdday;
    }
}
