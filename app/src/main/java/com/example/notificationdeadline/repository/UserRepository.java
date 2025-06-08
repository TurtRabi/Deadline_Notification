package com.example.notificationdeadline.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.UserEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRepository {
    private final AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public UserRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public void insertUser(final UserEntity userEntity) {
        executor.execute(() -> db.userDao().insert(userEntity));
    }

    public void updateUser(final UserEntity userEntity) {
        executor.execute(() -> db.userDao().update(userEntity));
    }

    public void deleteUser(final UserEntity userEntity) {
        executor.execute(() -> db.userDao().delete(userEntity));
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        return db.userDao().getAllUsers();
    }

    public LiveData<UserEntity> getUserById(int id) {
        return db.userDao().getUserById(id);
    }
}
