package com.example.notificationdeadline.repository;

import android.content.Context;
import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecurringDeadlineRepository {
    private final AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public RecurringDeadlineRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public interface OnInsertRecurringDeadlineCallback {
        void onRecurringDeadlineInserted(long id);
    }

    public void insertRecurringDeadline(RecurringDeadlineEntity recurringDeadline, OnInsertRecurringDeadlineCallback callback) {
        executor.execute(() -> {
            long id = db.recurringDeadlineDao().insert(recurringDeadline);
            if (callback != null) {
                callback.onRecurringDeadlineInserted(id);
            }
        });
    }

    public void updateRecurringDeadline(RecurringDeadlineEntity recurringDeadline) {
        executor.execute(() -> db.recurringDeadlineDao().update(recurringDeadline));
    }

    public RecurringDeadlineEntity getRecurringDeadlineById(int id) {
        return db.recurringDeadlineDao().getRecurringDeadlineById(id);
    }

    public LiveData<List<RecurringDeadlineEntity>> getAllRecurringDeadlines() {
        return db.recurringDeadlineDao().getAll();
    }

    public LiveData<List<RecurringDeadlineEntity>> getRecurringDeadlinesByType(int type) {
        return db.recurringDeadlineDao().getRecurringDeadlinesByType(type);
    }
}
