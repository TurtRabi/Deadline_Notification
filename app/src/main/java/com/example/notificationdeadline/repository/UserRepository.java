package com.example.notificationdeadline.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.UserEntity;

import java.util.List;

public class UserRepository {
    private final AppDatabase db;

    public UserRepository(Context context){
        db= Room.databaseBuilder(context,AppDatabase.class,"notification_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public void insertUser(UserEntity userEntity){
        db.userDao().insert(userEntity);
    }
    public  void UpdateUser(UserEntity userEntity){
        db.userDao().update(userEntity);
    }
    public List<UserEntity> getAllUsers() {
        return db.userDao().getAllUsers();
    }

    public UserEntity getUserById(int id) {
        return db.userDao().getUserById(id);
    }
}
