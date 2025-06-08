package com.example.notificationdeadline.ui.NotificationDeadlineInfor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.repository.TaskRepository;
import com.example.notificationdeadline.service.TaskService;

import java.util.List;

public class DeadlineNotificationDetailViewModel extends AndroidViewModel {
    private final TaskService taskService;
    public DeadlineNotificationDetailViewModel(@NonNull Application application) {
        super(application);
        taskService = new TaskService(application.getApplicationContext());
    }
    public LiveData<List<TaskEntity>> getAllTaskByIdDeadline(int notificationId){
        return taskService.getTasksForNotification(notificationId);
    }
    public void deleteTask(TaskEntity taskEntity){
        taskService.deleteTask(taskEntity);
    }

    public void IsdoneTask(TaskEntity taskEntity){
        taskEntity.setDone(true);
        taskService.updateTask(taskEntity);
    }

    public  void saveTask(TaskEntity task, TaskRepository.OnInsertCallback callback){
        taskService.insertTask(task,callback);
    }

}