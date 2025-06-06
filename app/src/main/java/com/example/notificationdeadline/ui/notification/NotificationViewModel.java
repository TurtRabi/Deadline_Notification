package com.example.notificationdeadline.ui.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.service.NotificationHistoryService;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {
    private NotificationHistoryService notificationHistoryService;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
    }

    private NotificationHistoryService getNotificationHistoryService(){
        if(notificationHistoryService==null){
            notificationHistoryService = new NotificationHistoryService(getApplication().getApplicationContext());
        }
        return notificationHistoryService;
    }

    public List<NotificationHistoryEntity> getAllList(){
        return getNotificationHistoryService().getAllHistory();
    }
    public void updateIsReadNotification(int id){
        getNotificationHistoryService().markAsRead(id);
    }



}