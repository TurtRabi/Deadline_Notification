package com.example.notificationdeadline.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.SettingEntity;

public class SettingRepository {
    private final AppDatabase db;

    public SettingRepository(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class,"notification_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public void saveSetting(String key,String value){
        db.settingDao().upsert(new SettingEntity(key,value));
    }

    public SettingEntity getSetting(String key){
        return db.settingDao().getSetting(key);
    }
}

