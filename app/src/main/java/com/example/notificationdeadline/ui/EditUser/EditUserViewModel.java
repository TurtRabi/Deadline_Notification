package com.example.notificationdeadline.ui.EditUser;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.notificationdeadline.dto.request.UserRequest;
import com.example.notificationdeadline.service.UserService;

public class EditUserViewModel extends AndroidViewModel {
    private final UserService userService;

    public EditUserViewModel(@NonNull Application application) {
        super(application);
        this.userService = new UserService(application.getApplicationContext());
    }

    public void updateUser(UserRequest request) {
        userService.updateUser(request);
    }
}
