package com.example.notificationdeadline.ui.DashBoard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.service.NotificationHistoryService;
import com.example.notificationdeadline.service.NotificationService;
import com.example.notificationdeadline.service.TaskService;

import java.util.Calendar;
import java.util.List;

public class DashBoardViewModel extends AndroidViewModel {
    private final NotificationHistoryService notificationHistoryService;
    private final NotificationService notificationService;
    private final TaskService taskService;

    public DashBoardViewModel(@NonNull Application application) {
        super(application);
        notificationHistoryService = new NotificationHistoryService(application.getApplicationContext());
        notificationService = new NotificationService(application.getApplicationContext());
        taskService = new TaskService(application.getApplicationContext());
    }

    public LiveData<List<NotificationEntity>> getAllList() {
        return notificationService.fetchAllNotifications();
    }

    public LiveData<Integer> getUnreadCount() {
        return notificationHistoryService.getUnreadCount();
    }

    public LiveData<List<NotificationEntity>> getListNotificationByDay() {
        return notificationService.fetchAllNotificationsByDay();
    }

    public LiveData<List<NotificationEntity>> getDeadlinesForTomorrow() {
        Calendar calendar = Calendar.getInstance();
        // Lấy thời gian bắt đầu ngày mai
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfTomorrow = calendar.getTimeInMillis();

        // Kết thúc ngày mai
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endOfTomorrow = calendar.getTimeInMillis();

        return notificationService.fetchAllNotificationsByDay(startOfTomorrow, endOfTomorrow);
    }

    public LiveData<List<NotificationEntity>> getUpcomingDeadlines() {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        long threeDaysLater = calendar.getTimeInMillis();
        return notificationService.fetchAllNotificationsByDay(now, threeDaysLater);
    }

    public LiveData<List<NotificationEntity>> getOverdueDeadlines() {
        long now = System.currentTimeMillis();
        return notificationService.fetchAllNotificationsByDay(0, now - 1);
    }

    public LiveData<List<TaskEntity>> getTasksForNotification(int notificationId){
        return taskService.getTasksForNotification(notificationId);
    }

    public LiveData<List<NotificationEntity>> geFinishDeadlines(){
        return notificationService.fetchAllNotificationsByStatus(4);
    }

}
