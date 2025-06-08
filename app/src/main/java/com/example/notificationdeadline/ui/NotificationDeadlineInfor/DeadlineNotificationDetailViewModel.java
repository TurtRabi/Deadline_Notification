package com.example.notificationdeadline.ui.NotificationDeadlineInfor;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.notification.NotificationScheduler;
import com.example.notificationdeadline.repository.TaskRepository;
import com.example.notificationdeadline.service.NotificationService;
import com.example.notificationdeadline.service.TaskService;

import java.util.List;

public class DeadlineNotificationDetailViewModel extends AndroidViewModel {
    private final TaskService taskService;
    private final NotificationService notificationService;
    public DeadlineNotificationDetailViewModel(@NonNull Application application) {
        super(application);
        taskService = new TaskService(application.getApplicationContext());
        notificationService = new NotificationService(application.getApplicationContext());
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
    public  void updateSuccessDeadline(int id){
        notificationService.updateStatus(4,id);
        notificationService.updateSuccessDeadline(id);
    }
    public  void updateNotSuccessDeadline(int id,int status){
        notificationService.updateStatus(status,id);
        notificationService.updateSuccessDeadline(id);
    }

    public void removeNotification(NotificationEntity notification, Context context) {
        NotificationScheduler.cancelScheduledNotification(context, notification.getId());
        notificationService.removeNotification(notification);
    }
}