package com.example.notificationdeadline.mapper;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.UserEntity;

import com.example.notificationdeadline.dto.request.UserRequest;
import com.example.notificationdeadline.dto.response.UserResponse;

public class UserMapper {
    public static UserEntity toEntity(UserRequest request){
        return new UserEntity(request.getId(),request.getUserName(), request.getImageUrl(),request.getDescription(), request.getEmail(), request.getPhone(), request.getBirdday());
    }
    public static UserEntity toEntity1(UserRequest request){
        return new UserEntity(request.getUserName(), request.getImageUrl(),request.getDescription(), request.getEmail(), request.getPhone(), request.getBirdday());
    }

    public  static UserResponse toResponse(UserEntity user){
        return new UserResponse(user.UserName, user.ImageUrl,user.Description, user.Email, user.phone, user.birdday);
    }
}
