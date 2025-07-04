package com.example.notificationdeadline.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;
import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.data.entity.UserEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackupRestoreManager {

    private static final String TAG = "BackupRestoreManager";
    private static final String BACKUP_FILE_NAME = "deadline_backup.json";
    private final AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Gson gson;

    public BackupRestoreManager(Context context) {
        db = AppDatabase.getInstance(context);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public interface BackupRestoreCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public void backupData(BackupRestoreCallback callback) {
        executor.execute(() -> {
            try {
                // Fetch all data from database
                List<NotificationEntity> notifications = db.notificationDao().getAllList();
                List<NotificationHistoryEntity> notificationHistory = db.notificationHistoryDao().getAllList();
                List<RecurringDeadlineEntity> recurringDeadlines = db.recurringDeadlineDao().getAllList();
                List<SettingEntity> settings = db.settingDao().getAllList();
                List<TaskEntity> tasks = db.taskDao().getAllList();
                List<UserEntity> users = db.userDao().getAllList();

                // Create a data holder object
                BackupData backupData = new BackupData(
                        notifications,
                        notificationHistory,
                        recurringDeadlines,
                        settings,
                        tasks,
                        users
                );

                // Convert to JSON
                String json = gson.toJson(backupData);

                // Write to file
                File backupFile = new File(Environment.getExternalStorageDirectory(), BACKUP_FILE_NAME);
                try (FileWriter writer = new FileWriter(backupFile)) {
                    writer.write(json);
                }
                callback.onSuccess();
            } catch (Exception e) {
                Log.e(TAG, "Backup failed", e);
                callback.onFailure("Backup failed: " + e.getMessage());
            }
        });
    }

    public void restoreData(BackupRestoreCallback callback) {
        executor.execute(() -> {
            try {
                File backupFile = new File(Environment.getExternalStorageDirectory(), BACKUP_FILE_NAME);
                if (!backupFile.exists()) {
                    callback.onFailure("Backup file not found.");
                    return;
                }

                // Read from file
                StringBuilder jsonBuilder = new StringBuilder();
                try (FileReader reader = new FileReader(backupFile)) {
                    int character;
                    while ((character = reader.read()) != -1) {
                        jsonBuilder.append((char) character);
                    }
                }
                String json = jsonBuilder.toString();

                // Convert JSON to data holder object
                Type type = new TypeToken<BackupData>() {}.getType();
                BackupData backupData = gson.fromJson(json, type);

                // Clear existing data and insert new data
                db.clearAllTables(); // This will clear all tables

                // Insert data in correct order to respect foreign key constraints
                if (backupData.users != null) db.userDao().insertAll(backupData.users);
                if (backupData.settings != null) db.settingDao().insertAll(backupData.settings);
                if (backupData.notifications != null) db.notificationDao().insertAll(backupData.notifications);
                if (backupData.recurringDeadlines != null) db.recurringDeadlineDao().insertAll(backupData.recurringDeadlines);
                if (backupData.tasks != null) db.taskDao().insertAll(backupData.tasks);
                if (backupData.notificationHistory != null) db.notificationHistoryDao().insertAll(backupData.notificationHistory);

                callback.onSuccess();
            } catch (Exception e) {
                Log.e(TAG, "Restore failed", e);
                callback.onFailure("Restore failed: " + e.getMessage());
            }
        });
    }

    // Data holder class for backup
    private static class BackupData {
        List<NotificationEntity> notifications;
        List<NotificationHistoryEntity> notificationHistory;
        List<RecurringDeadlineEntity> recurringDeadlines;
        List<SettingEntity> settings;
        List<TaskEntity> tasks;
        List<UserEntity> users;

        public BackupData(List<NotificationEntity> notifications, List<NotificationHistoryEntity> notificationHistory, List<RecurringDeadlineEntity> recurringDeadlines, List<SettingEntity> settings, List<TaskEntity> tasks, List<UserEntity> users) {
            this.notifications = notifications;
            this.notificationHistory = notificationHistory;
            this.recurringDeadlines = recurringDeadlines;
            this.settings = settings;
            this.tasks = tasks;
            this.users = users;
        }
    }
}
