package com.example.notificationdeadline.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.notificationdeadline.data.entity.NotificationEntity;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long  insert(NotificationEntity notification);

    @Delete
    void delete(NotificationEntity notification);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(NotificationEntity notification);

    // Trả về LiveData để UI tự động update, hoặc để List nếu dùng trong background
    @Query("SELECT * FROM notifications")
    LiveData<List<NotificationEntity>> getAll();

    @Query("SELECT * FROM notifications WHERE time_millis BETWEEN :startTime AND :endTime AND isSuccess=0")
    LiveData<List<NotificationEntity>> getAllByDay(long startTime, long endTime);

    @Query("SELECT * FROM notifications WHERE time_millis BETWEEN :startTime AND :endTime AND isSuccess=0")
    List<NotificationEntity> getAllByDay1(long startTime, long endTime);

    @Query("SELECT * FROM notifications WHERE status = :status")
    LiveData<List<NotificationEntity>> getAllByStatus(int status);

    @Query("SELECT * FROM notifications WHERE priority = :priority")
    LiveData<List<NotificationEntity>> getAllByPriority(int priority);

    @Transaction
    @Query("UPDATE notifications SET isSuccess = 1 WHERE id = :id")
    void updateSuccessDeadline(int id);
    @Transaction
    @Query("UPDATE notifications SET isSuccess = 0 WHERE id = :id")
    void updateNotSuccessDeadline(int id);
    @Transaction
    @Query("UPDATE notifications SET status = :status WHERE id = :id ")
    void updateStatus(int status, int id);
}
