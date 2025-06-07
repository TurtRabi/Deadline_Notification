package com.example.notificationdeadline.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notificationdeadline.data.dao.NotificationDao;
import com.example.notificationdeadline.data.dao.NotificationHistoryDao;
import com.example.notificationdeadline.data.dao.SettingDao;
import com.example.notificationdeadline.data.dao.UserDao;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.data.entity.UserEntity;

@Database(entities = {NotificationEntity.class, SettingEntity.class, UserEntity.class, NotificationHistoryEntity.class},version = 6)
public abstract class AppDatabase  extends RoomDatabase {
    public abstract NotificationDao notificationDao();
    public  abstract SettingDao settingDao();
    public  abstract UserDao userDao();
    public abstract NotificationHistoryDao notificationHistoryDao();
}
