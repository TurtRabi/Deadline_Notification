package com.example.notificationdeadline.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.SettingEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingRepository {
    private final AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public SettingRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public void saveSetting(String key, String value) {
        executor.execute(() -> db.settingDao().upsert(new SettingEntity(key, value)));
    }

    public LiveData<SettingEntity> getSetting(String key) {
        return db.settingDao().getSetting(key);
    }

    public void updateSetting(String key, String value) {
        executor.execute(() -> db.settingDao().updateValueByKey(key, value));
    }
}
