package com.example.notificationdeadline.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.NotificationEntity;

import java.util.List;

public class NotificationRepository {
    private final AppDatabase db;

    public NotificationRepository(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class,"notification_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public  void insertNotification(NotificationEntity notification){
        db.notificationDao().insert(notification);
    }
    public void deleteNotification(NotificationEntity notification){
        db.notificationDao().delete(notification);
    }

    public LiveData<List<NotificationEntity>> getAllNotifications(){
        return  db.notificationDao().getAll();
    }

    public void updateNotification(NotificationEntity notification){
        db.notificationDao().update(notification);
    }
    public  List<NotificationEntity> getAllNotification(long startTime, long endTime){
        return db.notificationDao().getAllByDay(startTime,endTime);
    }

    public List<NotificationEntity> getAllByStatus(int status){
        return db.notificationDao().getAllByStatus(status);
    }

    public List<NotificationEntity> getAllByPriority(int priority){
        return  db.notificationDao().getAllByPriority(priority);
    }
    public void updateSuccessDeadline(int id){
        db.notificationDao().updateSuccessDeadline(id);
    }
    public void updateStatus(int status,int id){
        db.notificationDao().updateStatus(status,id);
    }
}
