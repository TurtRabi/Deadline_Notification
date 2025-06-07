package com.example.notificationdeadline.ui.AddDeadline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.notificationdeadline.Adapter.PrioritySpinnerAdapter;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.databinding.FragmentAddDeadlineBinding;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.notification.NotificationScheduler;
import com.example.notificationdeadline.ui.dialog.CustomMessageDialog;

import java.util.Calendar;
import java.util.Date;

public class AddDeadlineFragment extends Fragment {

    private AddDeadlineViewModel mViewModel;
    private FragmentAddDeadlineBinding binding;
    private long selectedDateMillis = 0;

    public static AddDeadlineFragment newInstance() {
        return new AddDeadlineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddDeadlineBinding.inflate(inflater, container, false);
        Toolbar toolbar = binding.addToolbard;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        Spinner spinner = binding.spinnerPriority;
        String[] priorities = getResources().getStringArray(R.array.priority_list);
        PrioritySpinnerAdapter adapter = new PrioritySpinnerAdapter(requireContext(), priorities);
        spinner.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(AddDeadlineViewModel.class);

        CalendarView calendarView = binding.selectdayCalendar;
        TimePicker timePicker = binding.selectTimePicker;

        calendarView.setMinDate(System.currentTimeMillis());

        // Biến lưu ngày/giờ tạm
        final int[] selectedYear = {Calendar.getInstance().get(Calendar.YEAR)};
        final int[] selectedMonth = {Calendar.getInstance().get(Calendar.MONTH)};
        final int[] selectedDay = {Calendar.getInstance().get(Calendar.DAY_OF_MONTH)};
        final int[] hour = {timePicker.getHour()};
        final int[] minute = {timePicker.getMinute()};

        // Đồng bộ thời gian khi thay đổi giờ
        timePicker.setOnTimeChangedListener((view1, hourOfDay, minute1) -> {
            hour[0] = hourOfDay;
            minute[0] = minute1;
            updateSelectedDateMillis(selectedYear[0], selectedMonth[0], selectedDay[0], hour[0], minute[0]);
        });

        // Đồng bộ thời gian khi thay đổi ngày
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedYear[0] = year;
            selectedMonth[0] = month;
            selectedDay[0] = dayOfMonth;
            updateSelectedDateMillis(year, month, dayOfMonth, hour[0], minute[0]);
        });

        // Thiết lập mặc định khi view load
        updateSelectedDateMillis(selectedYear[0], selectedMonth[0], selectedDay[0], hour[0], minute[0]);

        binding.btnSave.setOnClickListener(v -> {
            EditText edtTitle = binding.txtTitle;
            EditText edtDescription = binding.txtDescription;

            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            int priority = binding.spinnerPriority.getSelectedItemPosition();
            boolean isDone = false;

            long now = System.currentTimeMillis();
            long diff = selectedDateMillis - now;

            long sixHours = 6 * 60 * 60 * 1000;
            long tenHours = 10 * 60 * 60 * 1000;

            int notificationType;
            if (diff > tenHours) {
                notificationType = StatusEnum.UPCOMING.getValue();
            } else if (diff > sixHours) {
                notificationType = StatusEnum.NEAR_DEADLINE.getValue();
            } else if (diff > 0) {
                notificationType = StatusEnum.DEADlINE.getValue();
            } else {
                notificationType = StatusEnum.OVERDEALINE.getValue();
            }

            if (isToday()) {
                int requestCode = (int) (selectedDateMillis / 1000);
                NotificationScheduler.scheduleFixedTimeNotification(
                        requireContext(), selectedDateMillis, requestCode, title, description
                );
            }

            mViewModel.addNotification(new NotificationRequest(
                    title, description, selectedDateMillis, priority, notificationType, isDone
            ));
            showSuccessDialogWithAutoDismiss();
        });
    }

    private void updateSelectedDateMillis(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        selectedDateMillis = calendar.getTimeInMillis();
        Log.d("Calendar", "🗓️ Ngày giờ được chọn: " + new Date(selectedDateMillis));
    }

    private boolean isToday() {
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.setTimeInMillis(selectedDateMillis);

        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);

        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);

        return selectedDateMillis >= todayStart.getTimeInMillis()
                && selectedDateMillis <= todayEnd.getTimeInMillis();
    }

    private void showSuccessDialogWithAutoDismiss() {

        CustomMessageDialog dialog = CustomMessageDialog.newInstance(
                "Thành công 🎉",
                "Deadline đã được thêm thành công!",
                R.drawable.ic_launcher_foreground,
                R.color.successColor
        );
        dialog.show(getParentFragmentManager(),"successDialog");



        new Handler().postDelayed(() -> {
            Dialog actualDialog = dialog.getDialog();
            if (actualDialog.isShowing()) {
                dialog.dismiss();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        }, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
