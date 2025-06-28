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
    long insert(NotificationEntity notification);

    @Delete
    void delete(NotificationEntity notification);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(NotificationEntity notification);

    // Trả về LiveData để UI tự động update, hoặc để List nếu dùng trong background
    @Query("SELECT * FROM notifications")
    LiveData<List<NotificationEntity>> getAll();

    @Query("SELECT * FROM notifications WHERE time_millis BETWEEN :startTime AND :endTime")
    LiveData<List<NotificationEntity>> getAllByDay(long startTime, long endTime);

    @Query("SELECT * FROM notifications WHERE time_millis BETWEEN :startTime AND :endTime AND isSuccess =:isSuccess")
    List<NotificationEntity> getAllByDay1(long startTime, long endTime, int isSuccess);

    @Query("SELECT * FROM notifications WHERE time_millis BETWEEN :startTime AND :endTime AND isSuccess =:isSuccess")
    LiveData<List<NotificationEntity>> getAllByDay2(long startTime, long endTime, int isSuccess);

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

    @Transaction
    @Query("SELECT * FROM notifications WHERE title LIKE '%' || :keyword || '%' OR message LIKE '%' || :keyword || '%' ORDER BY time_millis DESC")
    LiveData<List<NotificationEntity>> searchNotifications(String keyword);

    @Query("SELECT * FROM notifications WHERE id = :id LIMIT 1")
    NotificationEntity getNotificationById(int id);

    @Query("SELECT * FROM notifications WHERE is_recurring = 1")
    LiveData<List<NotificationEntity>> getAllRecurringNotifications();

    @Query("SELECT * FROM notifications WHERE is_recurring = 1 AND recurrence_type = :recurrenceType")
    LiveData<List<NotificationEntity>> getRecurringNotificationsByType(int recurrenceType);

    @Query("SELECT * FROM notifications WHERE status = 4")
    LiveData<List<NotificationEntity>> getAllCompletedNotifications();

    @Query("SELECT * FROM notifications WHERE is_recurring = 1")
    LiveData<List<NotificationEntity>> getAllFixedDeadlines();

    @Query("SELECT * FROM notifications WHERE time_millis BETWEEN :startTime AND :endTime")
    LiveData<List<NotificationEntity>> getWeeklyDeadlines(long startTime, long endTime);

    @Query("SELECT * FROM notifications WHERE time_millis BETWEEN :startTime AND :endTime")
    LiveData<List<NotificationEntity>> getMonthlyDeadlines(long startTime, long endTime);

    @Query("SELECT * FROM notifications WHERE time_millis BETWEEN :startTime AND :endTime")
    LiveData<List<NotificationEntity>> getYearlyDeadlines(long startTime, long endTime);
}
