package com.example.notificationdeadline.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.dto.request.SettingRequest;
import com.example.notificationdeadline.mapper.SettingMapper;
import com.example.notificationdeadline.repository.SettingRepository;

public class SettingService {
    private final SettingRepository settingRepository;

    public SettingService(Context context) {
        this.settingRepository = new SettingRepository(context);
    }

    public void saveSetting(SettingRequest request) {
        if (request.getKey() == null || request.getKey().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
        SettingEntity entity = SettingMapper.toEntity(request);
        settingRepository.saveSetting(entity.getKey(), entity.getValue());
    }

    public void updateSetting(SettingRequest request) {
        if (request.getKey() == null || request.getKey().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
        SettingEntity entity = SettingMapper.toEntity(request);
        settingRepository.updateSetting(entity.getKey(), entity.getValue());
    }

    public LiveData<SettingEntity> getSetting(String key) {
        return settingRepository.getSetting(key);
    }
}
