package com.example.notificationdeadline.ui.AddDeadline;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.dto.request.RecurringDeadlineRequest;
import com.example.notificationdeadline.mapper.RecurringDeadlineMapper;
import com.example.notificationdeadline.repository.NotificationRepository;
import com.example.notificationdeadline.repository.RecurringDeadlineRepository;
import com.example.notificationdeadline.service.NotificationService;
import com.example.notificationdeadline.service.RecurringDeadlineService;

public class AddDeadlineViewModel extends AndroidViewModel {
    private final NotificationService notificationService;
    private final RecurringDeadlineService recurringDeadlineService;
    public AddDeadlineViewModel(@NonNull Application application){
        super(application);
        this.notificationService = new NotificationService(application.getApplicationContext());
        this.recurringDeadlineService = new RecurringDeadlineService(application.getApplicationContext());
    }

    public void addNotification(NotificationRequest request, NotificationRepository.OnInsertCallback callback){
        notificationService.addNotification(request,callback);
    }

    public interface OnInsertRecurringDeadlineCallback {
        void onRecurringDeadlineInserted(long id);
    }

    public void addRecurringDeadline(RecurringDeadlineRequest request, OnInsertRecurringDeadlineCallback callback) {

        recurringDeadlineService.addRecurringDeadline(RecurringDeadlineMapper.toEntity(request), new RecurringDeadlineRepository.OnInsertRecurringDeadlineCallback() {
            @Override
            public void onRecurringDeadlineInserted(long id) {
                callback.onRecurringDeadlineInserted(id);
            }
        });
    }
}
