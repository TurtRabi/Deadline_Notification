package com.example.notificationdeadline.ui.DashBoard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.service.NotificationHistoryService;
import com.example.notificationdeadline.service.NotificationService;

import java.util.List;

public class DashBoardViewModel extends AndroidViewModel {
    private NotificationHistoryService notificationHistoryService;
    private NotificationService notificationService;
    public DashBoardViewModel(@NonNull Application application) {
        super(application);
    }
    private NotificationService getNotificationService() {
        if (notificationService == null) {
            notificationService = new NotificationService(getApplication().getApplicationContext());
        }
        return notificationService;
    }
    private NotificationHistoryService getNotificationHistoryService(){
        if(notificationHistoryService==null){
            notificationHistoryService = new NotificationHistoryService(getApplication().getApplicationContext());
        }
        return notificationHistoryService;
    }

    public LiveData<List<NotificationEntity>> getAllList(){
        return getNotificationService().fectAllNotifications();
    }
    public int getUnreadCount(){
        return  getNotificationHistoryService().getUnreadCount();
    }
}