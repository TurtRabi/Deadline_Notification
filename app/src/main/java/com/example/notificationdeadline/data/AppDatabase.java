package com.example.notificationdeadline.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.notificationdeadline.data.dao.NotificationDao;
import com.example.notificationdeadline.data.dao.NotificationHistoryDao;
import com.example.notificationdeadline.data.dao.RecurringDeadlineDao;
import com.example.notificationdeadline.data.dao.SettingDao;
import com.example.notificationdeadline.data.dao.TaskDao;
import com.example.notificationdeadline.data.dao.UserDao;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;
import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.data.entity.UserEntity;

@Database(
        entities = {
                NotificationEntity.class,
                SettingEntity.class,
                UserEntity.class,
                NotificationHistoryEntity.class,
                TaskEntity.class,
                RecurringDeadlineEntity.class
        },
        version = 19,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    // DAO
    public abstract NotificationDao notificationDao();
    public abstract SettingDao settingDao();
    public abstract UserDao userDao();
    public abstract NotificationHistoryDao notificationHistoryDao();
    public abstract TaskDao taskDao();
    public abstract RecurringDeadlineDao recurringDeadlineDao();

    // Singleton instance
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
