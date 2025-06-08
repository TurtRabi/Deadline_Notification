package com.example.notificationdeadline.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.repository.TaskRepository;

import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(Context context) {
        this.taskRepository = new TaskRepository(context);
    }


    public void insertTask(TaskEntity task, TaskRepository.OnInsertCallback callback) {
        taskRepository.insertTask(task, callback);
    }


    public void updateTask(TaskEntity task) {
        taskRepository.updateTask(task);
    }


    public void deleteTask(TaskEntity task) {
        taskRepository.deleteTask(task);
    }


    public LiveData<List<TaskEntity>> getTasksForNotification(int notificationId) {
        return taskRepository.getTasksForNotification(notificationId);
    }


    public LiveData<List<TaskEntity>> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public LiveData<List<TaskEntity>> getTasksByStatus(boolean isDone) {
        return taskRepository.getTasksByStatus(isDone);
    }
}
