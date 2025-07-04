package com.example.notificationdeadline.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notificationdeadline.data.entity.TaskEntity;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTask(TaskEntity task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntity task);

    @Delete
    void deleteTask(TaskEntity task);

    @Query("SELECT * FROM tasks WHERE notificationId = :notificationId")
    LiveData<List<TaskEntity>> getTasksForNotification(int notificationId);


    @Query("SELECT * FROM tasks")
    LiveData<List<TaskEntity>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE isDone = :isDone")
    LiveData<List<TaskEntity>> getTasksByStatus(boolean isDone);

    @Query("SELECT * FROM tasks")
    List<TaskEntity> getAllList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TaskEntity> tasks);
}
