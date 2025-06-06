package com.example.notificationdeadline.ui.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.notificationdeadline.data.entity.UserEntity;
import com.example.notificationdeadline.service.UserService;

import java.util.List;

public class SettingViewModel extends AndroidViewModel {

    private UserService userService;
    public SettingViewModel(@NonNull Application application) {
        super(application);
    }

    private UserService userService(){
        if(userService==null){
            userService=new UserService(getApplication().getApplicationContext());

        }
        return userService;
    }

    public List<UserEntity> getAllListUser(){
        return userService().getAllUsers();
    }


}