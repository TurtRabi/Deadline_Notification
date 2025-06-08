package com.example.notificationdeadline.mapper;

import com.example.notificationdeadline.data.entity.UserEntity;
import com.example.notificationdeadline.dto.request.UserRequest;
import com.example.notificationdeadline.dto.response.UserResponse;

public class UserMapper {

    public static UserEntity toEntity(UserRequest request) {
        if (request == null) return null;
        return new UserEntity(
                request.getId(),
                request.getUserName(),
                request.getImageUrl(),
                request.getDescription(),
                request.getEmail(),
                request.getPhone(),
                request.getBirthday()
        );
    }

    // Chuyển UserRequest thành UserEntity (cập nhật có id)
    public static UserEntity toEntityWithId(UserRequest request) {
        if (request == null) return null;
        return new UserEntity(
                request.getId(),
                request.getUserName(),
                request.getImageUrl(),
                request.getDescription(),
                request.getEmail(),
                request.getPhone(),
                request.getBirthday()
        );
    }


    public static UserResponse toResponse(UserEntity user) {
        if (user == null) return null;
        return new UserResponse(
                user.getUserName(),
                user.getImageUrl(),
                user.getDescription(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthday()
        );
    }
}
