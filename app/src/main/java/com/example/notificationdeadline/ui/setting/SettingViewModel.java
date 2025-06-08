package com.example.notificationdeadline.ui.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.data.entity.UserEntity;
import com.example.notificationdeadline.dto.request.SettingRequest;
import com.example.notificationdeadline.service.SettingService;
import com.example.notificationdeadline.service.UserService;

import java.util.List;

public class SettingViewModel extends AndroidViewModel {

    private final UserService userService;
    private final SettingService settingService;

    public SettingViewModel(@NonNull Application application) {
        super(application);
        userService = new UserService(application.getApplicationContext());
        settingService = new SettingService(application.getApplicationContext());
    }

    public LiveData<List<UserEntity>> getAllListUser() {
        return userService.getAllUsers();
    }

    public LiveData<SettingEntity> getSetting(String key) {
        return settingService.getSetting(key);
    }

    public void updateSetting(String key, String value) {
        settingService.updateSetting(new SettingRequest(key, value));
    }
}
