package com.example.notificationdeadline.ui.DashBoard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
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

    private void applyDailyFilter(String filter) {
        switch (filter) {
            case "Tất cả":
                currentSource = notificationService.fetchAllNotifications();
                break;
            case "Ngày mai":
                currentSource = notificationService.fetchTomorrowDeadlines();
                break;
            case "Đến hạn":
                currentSource = notificationService.fetchDueDeadlines();
                break;
            case "Quá hạn":
                currentSource = notificationService.fetchOverdueDeadlines();
                break;
            case "Hoàn thành":
                currentSource = notificationService.fetchCompletedDeadlines();
                break;
            case "Tuần":
                currentSource = notificationService.fetchWeeklyDeadlines();
                break;
            case "Tháng":
                currentSource = notificationService.fetchMonthlyDeadlines();
                break;
            case "Năm":
                currentSource = notificationService.fetchYearlyDeadlines();
                break;
            default:
                currentSource = notificationService.fetchAllNotifications();
                break;
        }
    }

    private void applyRecurringFilter(String filter) {
        switch (filter) {
            case "Tất cả":
                currentSource = notificationService.fetchAllRecurringNotifications();
                break;
            case "Hàng ngày":
                currentSource = notificationService.fetchDailyRecurringDeadlines();
                break;
            case "Hàng tuần":
                currentSource = notificationService.fetchWeeklyRecurringDeadlines();
                break;
            case "Hàng tháng":
                currentSource = notificationService.fetchMonthlyRecurringDeadlines();
                break;
            case "Hàng năm":
                currentSource = notificationService.fetchYearlyRecurringDeadlines();
                break;
            default:
                currentSource = notificationService.fetchAllRecurringNotifications();
                break;
        }
    }

    public void setFilter(String mainFilter, String subFilter) {
        if (currentSource != null) {
            filteredList.removeSource(currentSource);
        }

        if ("Deadline Cố Định".equals(mainFilter)) {
            applyRecurringFilter(subFilter);
        } else { // Default to "Deadline Hàng Ngày"
            applyDailyFilter(subFilter);
        }
        filteredList.addSource(currentSource, filteredList::setValue);
    }
    public LiveData<List<NotificationEntity>> getAllDeadlines() {
        return notificationService.fetchAllNotifications();
    }

    public LiveData<Integer> getUnreadCount() {
        return notificationHistoryService.getUnreadCount();
    }

    public LiveData<List<NotificationEntity>> getListNotificationByDay() {

        return notificationService.fetchAllNotificationsByDay(0);
    }

    public LiveData<List<TaskEntity>> getTasksForNotification(int notificationId){
        return taskService.getTasksForNotification(notificationId);
    }

    public  void updateNotSuccessDeadline(int id,int status){
        notificationService.updateStatus(status,id);
        notificationService.updateNotSuccessDeadline(id);
    }

    public void checkUpdateStatus() {
        notificationService.fetchAllNotificationsByDay(0).observeForever(listNotification -> {
            if (listNotification != null && !listNotification.isEmpty()) {
                long now = System.currentTimeMillis();
                long sixHours = 6 * 60 * 60 * 1000;
                long tenHours = 10 * 60 * 60 * 1000;

                for (NotificationEntity entity : listNotification) {
                    long diff = entity.getTimeMillis() - now;
                    int notificationType;

                    if (diff > tenHours) {
                        notificationType = StatusEnum.UPCOMING.getValue();
                    } else if (diff > sixHours) {
                        notificationType = StatusEnum.NEAR_DEADLINE.getValue();
                    } else if (diff > 0) {
                        notificationType = StatusEnum.DEADLINE.getValue();
                    } else {
                        notificationType = StatusEnum.OVERDEADLINE.getValue();
                    }

                    if (entity.getStatus() != notificationType) {
                        notificationService.updateStatus(entity.getId(), notificationType);
                    }

                    if (notificationType == StatusEnum.OVERDEADLINE.getValue()) {
                        notificationService.updateSuccessDeadline(entity.getId());
                    } else {
                        notificationService.updateNotSuccessDeadline(entity.getId());
                    }
                }
            }
        });
    }

}
