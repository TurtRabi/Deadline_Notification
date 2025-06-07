package com.example.notificationdeadline.ui.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.data.entity.UserEntity;
import com.example.notificationdeadline.dto.request.SettingRequest;
import com.example.notificationdeadline.service.SettingService;
import com.example.notificationdeadline.service.UserService;

import java.util.List;

public class SettingViewModel extends AndroidViewModel {

    private UserService userService;
    private SettingService settingService;
    public SettingViewModel(@NonNull Application application) {
        super(application);
    }

    private UserService userService(){
        if(userService==null){
            userService=new UserService(getApplication().getApplicationContext());

        }
        return userService;
    }

    private SettingService getSettingService(){
        if(settingService==null){
            settingService = new SettingService(getApplication().getApplicationContext());
        }
        return  settingService;
    }

    public List<UserEntity> getAllListUser(){
        return userService().getAllUsers();
    }

    public SettingEntity getSeting(String key){
        return getSettingService().getSetting(key);
    }
    public void UpdateSetting(String key ,String value){
        getSettingService().updateSetting(new SettingRequest(key,value));
    }

}