package com.example.notificationdeadline.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notificationdeadline.data.entity.NotificationEntity;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    void insert(NotificationEntity notification);
    @Delete
    void delete(NotificationEntity notification);
    @Update
    void update(NotificationEntity notification);
    @Query("SELECT * FROM notifications ORDER BY timeMillis DESC ")
    LiveData<List<NotificationEntity>> getAll();
    @Query("SELECT * FROM notifications WHERE timeMillis BETWEEN :startTime AND :endTime AND isSuccess=0")
    List<NotificationEntity> getAllByDay(long startTime, long endTime);

    @Query("SELECT * FROM notifications WHERE status = :status")
    List<NotificationEntity> getAllByStatus(int status);

    @Query("SELECT * FROM notifications WHERE priority = :priority")
    List<NotificationEntity> getAllByPriority(int priority);
    @Query("UPDATE notifications SET status = 1 WHERE id = :id")
    void updateSuccessDeadline(int id);
    @Query("UPDATE notifications SET status = :status WHERE id = :id ")
    void updateStatus(int status,int id);

}
