package com.example.notificationdeadline.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notificationdeadline.data.entity.TaskEntity;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long insertTask(TaskEntity task);

    @Update
    void updateTask(TaskEntity task);

    @Delete
    void deleteTask(TaskEntity task);

    @Query("SELECT * FROM tasks WHERE notificationId = :notificationId")
    List<TaskEntity> getTasksForNotification(int notificationId);
}
