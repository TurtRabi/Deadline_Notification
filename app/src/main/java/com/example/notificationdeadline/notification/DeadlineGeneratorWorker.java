package com.example.notificationdeadline.notification;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;
import com.example.notificationdeadline.repository.NotificationRepository;
import com.example.notificationdeadline.data.AppDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeadlineGeneratorWorker extends Worker {

    private final NotificationRepository notificationRepository;
    private final AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DeadlineGeneratorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        db = AppDatabase.getInstance(context);
        notificationRepository = new NotificationRepository(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        executor.execute(() -> {
            List<RecurringDeadlineEntity> recurringDeadlines = db.recurringDeadlineDao().getAllActiveRecurringDeadlinesSync();
            if (recurringDeadlines == null || recurringDeadlines.isEmpty()) {
                return;
            }

            Calendar today = Calendar.getInstance();
            int currentDayOfWeek = today.get(Calendar.DAY_OF_WEEK); // Sunday = 1, Monday = 2, ..., Saturday = 7
            int currentDayOfMonth = today.get(Calendar.DAY_OF_MONTH);

            for (RecurringDeadlineEntity recurringDeadline : recurringDeadlines) {
                boolean shouldGenerate = false;
                switch (recurringDeadline.getRecurrenceType()) {
                    case 1:
                        shouldGenerate = true;
                        break;
                    case 2:
                        if (recurringDeadline.getDayOfWeek() == currentDayOfWeek) {
                            shouldGenerate = true;
                        }
                        break;
                    case 3:
                        if (recurringDeadline.getDayOfMonth() == currentDayOfMonth) {
                            shouldGenerate = true;
                        }
                        break;
                    case 4:
                        if (recurringDeadline.getMonth() == today.get(Calendar.MONTH) &&
                                recurringDeadline.getDayOfMonth() == today.get(Calendar.DAY_OF_MONTH)) {
                            shouldGenerate = true;
                        }
                        break;
                }

                if (shouldGenerate) {
                    // Parse time string (HH:mm) and set to today's date
                    String[] timeParts = recurringDeadline.getTime().split(":");
                    int hour = Integer.parseInt(timeParts[0]);
                    int minute = Integer.parseInt(timeParts[1]);

                    Calendar deadlineTime = Calendar.getInstance();
                    deadlineTime.set(Calendar.HOUR_OF_DAY, hour);
                    deadlineTime.set(Calendar.MINUTE, minute);
                    deadlineTime.set(Calendar.SECOND, 0);
                    deadlineTime.set(Calendar.MILLISECOND, 0);

                    NotificationEntity newNotification = new NotificationEntity();
                    newNotification.setTitle(recurringDeadline.getTitle());
                    newNotification.setMessage(recurringDeadline.getDescription());
                    newNotification.setPriority(recurringDeadline.getPriority());
                    newNotification.setTimeMillis(deadlineTime.getTimeInMillis());
                    newNotification.setRecurring(false); // Generated notifications are not recurring themselves
                    newNotification.setRecurring(false); // Not a recurring template
                    newNotification.setSuccess(false);
                    newNotification.setStatus(0);

                    NotificationEntity existingNotification = db.notificationDao().getNotificationByTitleAndTime(newNotification.getTitle(), newNotification.getTimeMillis());
                    if (existingNotification == null) {
                        notificationRepository.insertNotification(newNotification, null);
                    }
                }
            }
        });
        return Result.success();
    }
}
