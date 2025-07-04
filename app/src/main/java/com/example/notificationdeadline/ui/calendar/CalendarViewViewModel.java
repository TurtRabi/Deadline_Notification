package com.example.notificationdeadline.ui.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.service.NotificationService;

import java.util.List;

public class CalendarViewViewModel extends AndroidViewModel {
    private final NotificationService notificationService;

    public CalendarViewViewModel(@NonNull Application application) {
        super(application);
        notificationService = new NotificationService(application.getApplicationContext());
    }

    public LiveData<List<NotificationEntity>> getDeadlinesForDate(long startTime, long endTime) {
        return notificationService.fetchAllNotificationsByDay(startTime, endTime, 0); // Assuming 0 means not success
    }
}
