package com.example.notificationdeadline.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;

import java.util.List;

@Dao
public interface NotificationHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NotificationHistoryEntity notification);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(NotificationHistoryEntity notification);

    @Query("UPDATE notification_history SET isRead = :isRead WHERE id = :id")
    int updateIsRead(int id, boolean isRead);

    @Query("SELECT * FROM notification_history ORDER BY sent_time_millis DESC")
    LiveData<List<NotificationHistoryEntity>> getAllNotifications();

    @Query("SELECT * FROM notification_history WHERE sent_time_millis BETWEEN :startTime AND :endTime ORDER BY sent_time_millis DESC")
    LiveData<List<NotificationHistoryEntity>> getNotificationsByDay(long startTime, long endTime);

    @Query("SELECT COUNT(*) FROM notification_history WHERE isRead = 0")
    LiveData<Integer> countUnreadNotifications();

    @Query("DELETE FROM notification_history")
    void deleteAll();

    @Query("SELECT * FROM notification_history")
    List<NotificationHistoryEntity> getAllList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NotificationHistoryEntity> historyEntities);
}
