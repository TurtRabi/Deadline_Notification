package com.example.notificationdeadline.mapper;

import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.dto.request.SettingRequest;
import com.example.notificationdeadline.dto.response.SettingResponse;

public class SettingMapper {
    public  static SettingEntity toEntity(SettingRequest request){
        return new SettingEntity(request.getKey(),request.getValue());
    }

    public static SettingResponse toResponse(SettingEntity entity){
        if(entity==null) return null;
        return new SettingResponse(entity.key,entity.value);
    }
}
