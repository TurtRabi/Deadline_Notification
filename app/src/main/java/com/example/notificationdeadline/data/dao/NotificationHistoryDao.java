package com.example.notificationdeadline.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;

import java.util.List;

@Dao
public interface NotificationHistoryDao {

    @Insert
    long insert(NotificationHistoryEntity notification);

    @Update
    int update(NotificationHistoryEntity notification);


    @Query("UPDATE notification_history SET isRead = :isRead WHERE id = :id")
    int updateIsRead(int id, boolean isRead);
    @Query("SELECT * FROM notification_history ORDER BY sentTimeMillis DESC")
    List<NotificationHistoryEntity> getAllNotifications();
    @Query("SELECT * FROM notification_history WHERE sentTimeMillis BETWEEN :startTime AND :endTime ORDER BY sentTimeMillis DESC")
    List<NotificationHistoryEntity> getNotificationsByDay(long startTime, long endTime);
    @Query("SELECT COUNT(*) FROM notification_history WHERE isRead = 0")
    int countUnreadNotifications();
}
