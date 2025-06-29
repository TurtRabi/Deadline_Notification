package com.example.notificationdeadline.service;

import android.content.Context;
import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;
import com.example.notificationdeadline.repository.RecurringDeadlineRepository;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecurringDeadlineService {
    private final RecurringDeadlineRepository repository;

    public RecurringDeadlineService(Context context) {
        this.repository = new RecurringDeadlineRepository(context);
    }

    public void addRecurringDeadline(RecurringDeadlineEntity recurringDeadline, RecurringDeadlineRepository.OnInsertRecurringDeadlineCallback callback) {
        repository.insertRecurringDeadline(recurringDeadline, callback);
    }

    public void updateRecurringDeadline(RecurringDeadlineEntity recurringDeadline) {
        repository.updateRecurringDeadline(recurringDeadline);
    }

    public RecurringDeadlineEntity getRecurringDeadlineById(int id) {
        return repository.getRecurringDeadlineById(id);
    }

    public LiveData<List<RecurringDeadlineEntity>> getAllRecurringDeadlines() {
        return repository.getAllRecurringDeadlines();
    }

    public LiveData<List<RecurringDeadlineEntity>> getRecurringDeadlinesByType(int type) {
        return repository.getRecurringDeadlinesByType(type);
    }
}
