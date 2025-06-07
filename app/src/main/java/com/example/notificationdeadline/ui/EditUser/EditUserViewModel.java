package com.example.notificationdeadline.ui.EditUser;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.notificationdeadline.dto.request.UserRequest;
import com.example.notificationdeadline.service.UserService;

public class EditUserViewModel extends AndroidViewModel {
    private UserService userService;
    public EditUserViewModel(@NonNull Application application) {
        super(application);
    }
    public UserService getUserService(){
        if(userService==null){
            userService = new UserService(getApplication().getApplicationContext());
        }
        return userService;

    }

    public  void UpdateUser(UserRequest request){
        getUserService().updateUser(request);
    }
}