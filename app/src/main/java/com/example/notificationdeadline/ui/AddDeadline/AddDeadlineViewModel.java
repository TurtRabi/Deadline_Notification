package com.example.notificationdeadline.ui.AddDeadline;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.service.NotificationService;

public class AddDeadlineViewModel extends AndroidViewModel {
    private final NotificationService notificationService;

    public AddDeadlineViewModel(@NonNull Application application){
        super(application);
        this.notificationService = new NotificationService(application.getApplicationContext());
    }

    public void addNotification(NotificationRequest request){
        notificationService.addNotification(request);
    }
}
