package com.example.notificationdeadline.dto.response;

public class UserResponse {
    private String userName;
    private String imageUrl;
    private String description;
    public String Email;
    public String phone;
    public String birdday;

    public String getEmail() {
        return Email;
    }

    public UserResponse(String userName, String imageUrl, String description, String email, String phone, String birdday) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
        Email = email;
        this.phone = phone;
        this.birdday = birdday;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirdday() {
        return birdday;
    }

    public void setBirdday(String birdday) {
        this.birdday = birdday;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserResponse(String userName, String imageUrl ,String description) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description= description;
    }
}
