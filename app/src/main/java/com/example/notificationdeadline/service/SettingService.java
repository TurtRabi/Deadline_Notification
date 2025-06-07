package com.example.notificationdeadline.service;

import android.content.Context;

import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.dto.request.SettingRequest;
import com.example.notificationdeadline.mapper.SettingMapper;
import com.example.notificationdeadline.repository.SettingRepository;

public class SettingService {
    private final SettingRepository settingRepository;

    public SettingService(Context context) {
        this.settingRepository = new SettingRepository(context);
    }

    public  void saveSetting(SettingRequest request){
        if(request.getKey()==null||request.getKey().isEmpty()){
            throw  new IllegalArgumentException("Key cannot bey empty");
        }

        SettingEntity entity = SettingMapper.toEntity(request);
        settingRepository.saveSetting(entity.key,entity.value);
    }

    public void updateSetting(SettingRequest request){
        if(request.getKey()==null||request.getKey().isEmpty()){
            throw  new IllegalArgumentException("Key cannot bey empty");
        }
        SettingEntity entity = SettingMapper.toEntity(request);

        settingRepository.UpdateSetting(entity.key, entity.value);
    }

    public SettingEntity getSetting(String key){
        return settingRepository.getSetting(key);
    }
}
