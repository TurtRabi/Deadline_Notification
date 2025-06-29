package com.example.notificationdeadline.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;

import java.util.List;

@Dao
public interface RecurringDeadlineDao {
    @Insert
    long insert(RecurringDeadlineEntity recurringDeadline);

    @Update
    void update(RecurringDeadlineEntity recurringDeadline);

    @Query("SELECT * FROM recurring_deadlines WHERE isActive = 1")
    List<RecurringDeadlineEntity> getAllActiveRecurringDeadlinesSync();

    @Query("SELECT * FROM recurring_deadlines WHERE id = :id")
    RecurringDeadlineEntity getRecurringDeadlineById(int id);

    @Query("SELECT * FROM recurring_deadlines")
    LiveData<List<RecurringDeadlineEntity>> getAll();

    @Query("SELECT * FROM recurring_deadlines WHERE recurrenceType = :type")
    LiveData<List<RecurringDeadlineEntity>> getRecurringDeadlinesByType(int type);
}
