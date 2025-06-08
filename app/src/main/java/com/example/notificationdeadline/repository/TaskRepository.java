package com.example.notificationdeadline.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.dao.TaskDao;
import com.example.notificationdeadline.data.entity.TaskEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRepository {
    private final AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TaskRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }


    public void insertTask(TaskEntity task, OnInsertCallback callback) {
        executor.execute(() -> {
            long id = db.taskDao().insertTask(task);
            if (callback != null) {
                callback.onInsert(id);
            }
        });
    }


    public void updateTask(TaskEntity task) {
        executor.execute(() -> db.taskDao().updateTask(task));
    }


    public void deleteTask(TaskEntity task) {
        executor.execute(() -> db.taskDao().deleteTask(task));
    }

    public LiveData<List<TaskEntity>> getTasksForNotification(int notificationId) {
        return db.taskDao().getTasksForNotification(notificationId);
    }


    public LiveData<List<TaskEntity>> getAllTasks() {
        return db.taskDao().getAllTasks();
    }


    public LiveData<List<TaskEntity>> getTasksByStatus(boolean isDone) {
        return db.taskDao().getTasksByStatus(isDone);
    }

    public interface OnInsertCallback {
        void onInsert(long id);
    }
}
