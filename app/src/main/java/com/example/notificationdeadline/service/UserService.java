package com.example.notificationdeadline.service;

import android.content.Context;

import com.example.notificationdeadline.data.entity.UserEntity;
import com.example.notificationdeadline.dto.request.UserRequest;
import com.example.notificationdeadline.mapper.UserMapper;
import com.example.notificationdeadline.repository.UserRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService(Context context) {
        userRepository = new UserRepository(context);
    }

    public void saveUser(UserRequest request){
        if(request==null){
            throw  new IllegalArgumentException("Key cannot bey empty");
        }
        UserEntity userEntity = UserMapper.toEntity(request);
        userRepository.insertUser(userEntity);
    }

    public  void updateUser(UserRequest request){
        if(request==null){
            throw  new IllegalArgumentException("Key cannot bey empty");
        }
        UserEntity userEntity = UserMapper.toEntity(request);
        userRepository.UpdateUser(userEntity);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public UserEntity getUserById(int id) {
        return userRepository.getUserById(id);
    }
    public void initDefaultUser() {
        List<UserEntity> existingUsers = userRepository.getAllUsers();
        if (existingUsers == null || existingUsers.isEmpty()) {
            UserRequest defaultUser = new UserRequest();
            defaultUser.setEmail("example@gmail.com");
            defaultUser.setPhone("0000000000");
            defaultUser.setBirdday("30/05/2003");
            defaultUser.setDescription("hello my app");
            defaultUser.setUserName("example");
            defaultUser.setImageUrl("android.resource://com.example.notificationdeadline/drawable/logo");
            UserEntity userEntity = UserMapper.toEntity(defaultUser);
            // Thêm các giá trị mặc định nếu cần
            userRepository.insertUser(userEntity);
        }
    }

}
