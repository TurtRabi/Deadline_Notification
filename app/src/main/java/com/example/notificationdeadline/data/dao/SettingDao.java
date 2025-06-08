package com.example.notificationdeadline.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.notificationdeadline.data.entity.SettingEntity;

@Dao
public interface SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(SettingEntity setting);

    @Query("SELECT * FROM settings WHERE `key` = :key")
    LiveData<SettingEntity> getSetting(String key);

    @Query("UPDATE settings SET value = :value WHERE `key` = :key")
    void updateValueByKey(String key, String value);


    @Query("DELETE FROM settings WHERE `key` = :key")
    void deleteByKey(String key);


    @Query("SELECT * FROM settings")
    LiveData<java.util.List<SettingEntity>> getAllSettings();
}
