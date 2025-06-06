package com.example.notificationdeadline.dto.request;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserRequest implements Parcelable {
    private int id;
    private String userName;
    private String imageUrl;
    private String description;
    private String Email;
    private String phone;

    protected UserRequest(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        Email = in.readString();
        phone = in.readString();
        birdday = in.readString();
    }

    public static final Creator<UserRequest> CREATOR = new Creator<UserRequest>() {
        @Override
        public UserRequest createFromParcel(Parcel in) {
            return new UserRequest(in);
        }

        @Override
        public UserRequest[] newArray(int size) {
            return new UserRequest[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String birdday;

    public UserRequest(String userName, String imageUrl, String description, String email, String phone, String birdday) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
        Email = email;
        this.phone = phone;
        this.birdday = birdday;
    }

    public UserRequest(int id, String userName, String imageUrl, String description, String email, String phone, String birdday) {
        this.id = id;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
        Email = email;
        this.phone = phone;
        this.birdday = birdday;
    }

    public UserRequest() {
    }

    public String getEmail() {
        return Email;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserRequest(String userName, String imageUr, String description) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userName);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeString(Email);
        dest.writeString(phone);
        dest.writeString(birdday);
    }
}
