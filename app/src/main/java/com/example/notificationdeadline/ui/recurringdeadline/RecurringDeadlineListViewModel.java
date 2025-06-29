package com.example.notificationdeadline.ui.recurringdeadline;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;
import com.example.notificationdeadline.service.RecurringDeadlineService;

import java.util.List;

public class RecurringDeadlineListViewModel extends AndroidViewModel {

    private final RecurringDeadlineService recurringDeadlineService;

    public RecurringDeadlineListViewModel(@NonNull Application application) {
        super(application);
        recurringDeadlineService = new RecurringDeadlineService(application.getApplicationContext());
    }

    public LiveData<List<RecurringDeadlineEntity>> getAllRecurringDeadlines() {
        return recurringDeadlineService.getAllRecurringDeadlines();
    }

    public void updateRecurringDeadline(RecurringDeadlineEntity recurringDeadline) {
        recurringDeadlineService.updateRecurringDeadline(recurringDeadline);
    }

    public void deleteRecurringDeadline(RecurringDeadlineEntity recurringDeadline) {
        recurringDeadlineService.deleteRecurringDeadline(recurringDeadline);
    }
}
