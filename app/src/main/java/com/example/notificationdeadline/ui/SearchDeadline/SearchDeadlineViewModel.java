package com.example.notificationdeadline.ui.SearchDeadline;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.service.NotificationService;

import java.util.List;

public class SearchDeadlineViewModel extends AndroidViewModel {

    private final NotificationService notificationService;

    public SearchDeadlineViewModel(@NonNull Application application) {
        super(application);
        notificationService = new NotificationService(application.getApplicationContext());
    }

    public LiveData<List<NotificationEntity>> searchDeadlines(String keyword) {
        return notificationService.searchNotifications(keyword);
    }

    public LiveData<List<NotificationEntity>> searchDeadlines(String keyword, String tag) {
        if (tag != null && !tag.isEmpty()) {
            return notificationService.searchNotificationsByKeywordAndTag(keyword, tag);
        } else {
            return notificationService.searchNotifications(keyword);
        }
    }
}