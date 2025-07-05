package com.example.notificationdeadline.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.dao.NotificationDao;
import com.example.notificationdeadline.data.dao.TaskDao;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodayDeadlineDialog extends DialogFragment {

    private static final String PREF_DISMISS_TODAY_DATE = "dismiss_today_date";
    private static boolean dismissForSession = false;

    private TextView tvCompletedDeadlines;
    private TextView tvPendingDeadlines;
    private TextView tvOverdueDeadlines;
    private Button btnDismissToday;
    private Button btnDismissSession;

    private AppDatabase db;
    private NotificationDao notificationDao;
    private TaskDao taskDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public static TodayDeadlineDialog newInstance() {
        return new TodayDeadlineDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_today_deadline_summary, null);

        tvCompletedDeadlines = view.findViewById(R.id.tv_completed_deadlines);
        tvPendingDeadlines = view.findViewById(R.id.tv_pending_deadlines);
        tvOverdueDeadlines = view.findViewById(R.id.tv_overdue_deadlines);
        btnDismissToday = view.findViewById(R.id.btn_dismiss_today);
        btnDismissSession = view.findViewById(R.id.btn_dismiss_session);

        db = AppDatabase.getInstance(requireContext());
        notificationDao = db.notificationDao();
        taskDao = db.taskDao();

        loadDeadlineSummary();

        btnDismissToday.setOnClickListener(v -> {
            setDismissToday();
            dismiss();
        });

        btnDismissSession.setOnClickListener(v -> {
            dismissForSession = true;
            dismiss();
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    private void loadDeadlineSummary() {
        executor.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long startTime = calendar.getTimeInMillis();

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            long endTime = calendar.getTimeInMillis();

            // Get all notifications for today
            List<NotificationEntity> todayNotifications = notificationDao.getAllByDay1(startTime, endTime, 0); // Assuming 0 means not success

            int completedCount = 0;
            int pendingCount = 0;
            int overdueCount = 0;

            long currentTime = System.currentTimeMillis();

            for (NotificationEntity notification : todayNotifications) {
                // For simplicity, assuming isSuccess in NotificationEntity means completed
                // If tasks are involved, you'd need to query tasks for this notification
                if (notification.isSuccess()) {
                    completedCount++;
                } else if (notification.getTimeMillis() < currentTime) {
                    overdueCount++;
                } else {
                    pendingCount++;
                }
            }

            final int finalCompletedCount = completedCount;
            final int finalPendingCount = pendingCount;
            final int finalOverdueCount = overdueCount;

            requireActivity().runOnUiThread(() -> {
                tvCompletedDeadlines.setText("Đã hoàn thành: " + finalCompletedCount);
                tvPendingDeadlines.setText("Chưa hoàn thành: " + finalPendingCount);
                tvOverdueDeadlines.setText("Quá hạn: " + finalOverdueCount);
            });
        });
    }

    private void setDismissToday() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = preferences.edit();
        Calendar calendar = Calendar.getInstance();
        // Save today's date (day, month, year) to dismiss until next day
        editor.putInt("dismiss_day", calendar.get(Calendar.DAY_OF_MONTH));
        editor.putInt("dismiss_month", calendar.get(Calendar.MONTH));
        editor.putInt("dismiss_year", calendar.get(Calendar.YEAR));
        editor.apply();
    }

    public static boolean shouldShowDialog(Context context) {
        if (dismissForSession) {
            return false;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int dismissedDay = preferences.getInt("dismiss_day", -1);
        int dismissedMonth = preferences.getInt("dismiss_month", -1);
        int dismissedYear = preferences.getInt("dismiss_year", -1);

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        // If dismissed date is today, don't show
        if (dismissedDay == currentDay && dismissedMonth == currentMonth && dismissedYear == currentYear) {
            return false;
        }
        return true;
    }

    public static void resetDismissForSession() {
        dismissForSession = false;
    }

    public interface TodayDeadlineDialogListener {
        void onDialogDismissed();
    }

    private TodayDeadlineDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TodayDeadlineDialogListener) {
            listener = (TodayDeadlineDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TodayDeadlineDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDialogDismissed();
        }
    }
}
