package com.example.notificationdeadline.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notificationdeadline.data.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(UserEntity user);

    @Delete
    void delete(UserEntity user);

    @Query("SELECT * FROM users")
    LiveData<List<UserEntity>> getAllUsers();

    @Query("SELECT * FROM users WHERE userId = :id")
    LiveData<UserEntity> getUserById(int id);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    LiveData<UserEntity> getUserByEmail(String email);
}
