package com.example.notificationdeadline.dto.request;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserRequest implements Parcelable {
    private int id;
    @NonNull private String userName;
    @Nullable private String imageUrl;
    @Nullable private String description;
    @NonNull private String email;
    @Nullable private String phone;
    @Nullable private String birthday;

    public UserRequest() {}

    public UserRequest(@NonNull String userName, @Nullable String imageUrl, @Nullable String description, @NonNull String email, @Nullable String phone, @Nullable String birthday) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    public UserRequest(int id, @NonNull String userName, @Nullable String imageUrl, @Nullable String description, @NonNull String email, @Nullable String phone, @Nullable String birthday) {
        this.id = id;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    // Parcelable constructor
    protected UserRequest(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        email = in.readString();
        phone = in.readString();
        birthday = in.readString();
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
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(birthday);
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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
