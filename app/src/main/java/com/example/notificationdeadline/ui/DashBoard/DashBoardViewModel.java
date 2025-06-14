package com.example.notificationdeadline.ui.DashBoard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

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
    private final MediatorLiveData<List<NotificationEntity>> filteredList = new MediatorLiveData<>();
    private LiveData<List<NotificationEntity>> currentSource = null;



    public DashBoardViewModel(@NonNull Application application) {
        super(application);
        notificationHistoryService = new NotificationHistoryService(application.getApplicationContext());
        notificationService = new NotificationService(application.getApplicationContext());
        taskService = new TaskService(application.getApplicationContext());
    }

    public LiveData<List<NotificationEntity>> getFilteredList() {
        return filteredList;
    }

    public void setFilter(String filter) {
        if (currentSource != null) {
            filteredList.removeSource(currentSource);
        }
        switch (filter) {
            case "Tất cả":
                currentSource = getAllList();
                break;
            case "Ngày mai":
                currentSource = getDeadlinesForTomorrow();
                break;
            case "Sắp đến hạn":
                currentSource = getUpcomingDeadlines();
                break;
            case "Quá hạn":
                currentSource = getOverdueDeadlines();
                break;
            case "Hoàn thành":
                currentSource = geFinishDeadlines();
                break;
            default:
                currentSource = getAllList();
                break;
        }
        filteredList.addSource(currentSource, filteredList::setValue);
    }
    public LiveData<List<NotificationEntity>> getAllList() {
        return notificationService.fetchAllNotifications();
    }

    public LiveData<Integer> getUnreadCount() {
        return notificationHistoryService.getUnreadCount();
    }

    public LiveData<List<NotificationEntity>> getListNotificationByDay() {
        return notificationService.fetchAllNotificationsByDay(0);
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

        return notificationService.fetchAllNotificationsByDay(startOfTomorrow, endOfTomorrow,0);
    }

    public LiveData<List<NotificationEntity>> getUpcomingDeadlines() {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        long threeDaysLater = calendar.getTimeInMillis();
        return notificationService.fetchAllNotificationsByDay(now, threeDaysLater,0);
    }

    public LiveData<List<NotificationEntity>> getOverdueDeadlines() {
        long now = System.currentTimeMillis();
        return notificationService.fetchAllNotificationsByDay(0, now - 1,1);
    }

    public LiveData<List<TaskEntity>> getTasksForNotification(int notificationId){
        return taskService.getTasksForNotification(notificationId);
    }

    public LiveData<List<NotificationEntity>> geFinishDeadlines(){
        return notificationService.fetchAllNotificationsByStatus(4);
    }
    public  void updateNotSuccessDeadline(int id,int status){
        notificationService.updateStatus(status,id);
        notificationService.updateNotSuccessDeadline(id);
    }
}
