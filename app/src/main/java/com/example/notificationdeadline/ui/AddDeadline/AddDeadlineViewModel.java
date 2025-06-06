package com.example.notificationdeadline.ui.AddDeadline;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.service.NotificationService;

public class AddDeadlineViewModel extends AndroidViewModel {
    private NotificationService notificationService;

    public AddDeadlineViewModel(@NonNull Application application){
        super(application);
    }

    private NotificationService getNotificationService() {
        if (notificationService == null) {
            notificationService = new NotificationService(getApplication().getApplicationContext());
        }
        return notificationService;
    }

    public void addNotification(NotificationRequest request){
        getNotificationService().addNotification(request);
    }
}
