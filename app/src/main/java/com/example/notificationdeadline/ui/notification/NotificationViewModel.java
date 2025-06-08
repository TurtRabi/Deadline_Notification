package com.example.notificationdeadline.ui.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.service.NotificationHistoryService;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {
    private final NotificationHistoryService notificationHistoryService;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notificationHistoryService = new NotificationHistoryService(application.getApplicationContext());
    }

    public LiveData<List<NotificationHistoryEntity>> getAllList() {
        return notificationHistoryService.getAllHistory();
    }

    public void updateIsReadNotification(int id) {
        notificationHistoryService.markAsRead(id);
    }
}
