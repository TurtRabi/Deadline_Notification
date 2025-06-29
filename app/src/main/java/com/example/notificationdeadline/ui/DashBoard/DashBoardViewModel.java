package com.example.notificationdeadline.ui.DashBoard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.service.NotificationHistoryService;
import com.example.notificationdeadline.service.NotificationService;
import com.example.notificationdeadline.service.RecurringDeadlineService;
import com.example.notificationdeadline.service.TaskService;

import java.util.Calendar;
import java.util.List;

public class DashBoardViewModel extends AndroidViewModel {
    private final NotificationHistoryService notificationHistoryService;
    private final NotificationService notificationService;
    private final RecurringDeadlineService recurringDeadlineService;
    private final TaskService taskService;

    private final MutableLiveData<List<NotificationEntity>> dailyDeadlines = new MutableLiveData<>();
    private final MutableLiveData<List<RecurringDeadlineEntity>> recurringDeadlines = new MutableLiveData<>();
    private final MutableLiveData<String> currentFilterType = new MutableLiveData<>();

    public DashBoardViewModel(@NonNull Application application) {
        super(application);
        notificationHistoryService = new NotificationHistoryService(application.getApplicationContext());
        notificationService = new NotificationService(application.getApplicationContext());
        recurringDeadlineService = new RecurringDeadlineService(application.getApplicationContext());
        taskService = new TaskService(application.getApplicationContext());

        // Set initial filter type
        currentFilterType.setValue("Deadline Hàng Ngày");
        // Load initial data
        applyDailyFilter("Tất cả");
    }

    public LiveData<List<NotificationEntity>> getDailyDeadlines() {
        return dailyDeadlines;
    }

    public LiveData<List<RecurringDeadlineEntity>> getRecurringDeadlines() {
        return recurringDeadlines;
    }

    public LiveData<String> getCurrentFilterType() {
        return currentFilterType;
    }

    private void applyDailyFilter(String filter) {
        switch (filter) {
            case "Tất cả":
                notificationService.fetchAllNotifications().observeForever(dailyDeadlines::setValue);
                break;
            case "Ngày mai":
                notificationService.fetchTomorrowDeadlines().observeForever(dailyDeadlines::setValue);
                break;
            case "Đến hạn":
                notificationService.fetchDueDeadlines().observeForever(dailyDeadlines::setValue);
                break;
            case "Quá hạn":
                notificationService.fetchOverdueDeadlines().observeForever(dailyDeadlines::setValue);
                break;
            case "Hoàn thành":
                notificationService.fetchCompletedDeadlines().observeForever(dailyDeadlines::setValue);
                break;
            default:
                notificationService.fetchAllNotifications().observeForever(dailyDeadlines::setValue);
                break;
        }
    }

    private void applyRecurringFilter(String filter) {
        switch (filter) {
            case "Tất cả":
                recurringDeadlineService.getAllRecurringDeadlines().observeForever(recurringDeadlines::setValue);
                break;
            case "Hàng ngày":
                recurringDeadlineService.getRecurringDeadlinesByType(1).observeForever(recurringDeadlines::setValue);
                break;
            case "Hàng tuần":
                recurringDeadlineService.getRecurringDeadlinesByType(2).observeForever(recurringDeadlines::setValue);
                break;
            case "Hàng tháng":
                recurringDeadlineService.getRecurringDeadlinesByType(3).observeForever(recurringDeadlines::setValue);
                break;
            case "Hàng năm":
                recurringDeadlineService.getRecurringDeadlinesByType(4).observeForever(recurringDeadlines::setValue);
                break;
            default:
                recurringDeadlineService.getAllRecurringDeadlines().observeForever(recurringDeadlines::setValue);
                break;
        }
    }

    public void setFilter(String mainFilter, String subFilter) {
        currentFilterType.setValue(mainFilter);
        if ("Deadline Cố Định".equals(mainFilter)) {
            applyRecurringFilter(subFilter);
        } else { // Default to "Deadline Hàng Ngày"
            applyDailyFilter(subFilter);
        }
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

    public LiveData<List<TaskEntity>> getTasksForNotification(int notificationId) {
        return taskService.getTasksForNotification(notificationId);
    }

    public void updateNotSuccessDeadline(int id, int status) {
        notificationService.updateStatus(status, id);
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
