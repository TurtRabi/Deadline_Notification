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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.AdapterView;

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
    private Switch switchIsRecurring;
    private Spinner spinnerRecurrenceType;
    private Spinner spinnerRecurrenceValue;

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

        switchIsRecurring = binding.switchIsRecurring;
        spinnerRecurrenceType = binding.spinnerRecurrenceType;
        spinnerRecurrenceValue = binding.spinnerRecurrenceValue;

        switchIsRecurring.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                spinnerRecurrenceType.setVisibility(View.VISIBLE);
                spinnerRecurrenceValue.setVisibility(View.VISIBLE);
            } else {
                spinnerRecurrenceType.setVisibility(View.GONE);
                spinnerRecurrenceValue.setVisibility(View.GONE);
            }
        });

        // Populate spinnerRecurrenceType
        ArrayAdapter<CharSequence> recurrenceTypeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.recurrence_type_list,
                android.R.layout.simple_spinner_item
        );
        recurrenceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecurrenceType.setAdapter(recurrenceTypeAdapter);

        spinnerRecurrenceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateRecurrenceValueSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

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

            boolean isRecurring = switchIsRecurring.isChecked();
            int recurrenceType = 0; // Default to None
            int recurrenceValue = 0; // Default to 0

            if (isRecurring) {
                recurrenceType = spinnerRecurrenceType.getSelectedItemPosition();
                // Get recurrence value based on type
                if (spinnerRecurrenceValue.getAdapter() != null && spinnerRecurrenceValue.getSelectedItem() != null) {
                    switch (recurrenceType) {
                        case 1: // Daily
                            recurrenceValue = 0; // Not applicable for daily
                            break;
                        case 2: // Weekly (Day of week)
                            recurrenceValue = spinnerRecurrenceValue.getSelectedItemPosition() + 1; // Calendar.SUNDAY is 1
                            break;
                        case 3: // Monthly (Day of month)
                            recurrenceValue = Integer.parseInt(spinnerRecurrenceValue.getSelectedItem().toString());
                            break;
                        case 4: // Yearly (Month of year)
                            recurrenceValue = spinnerRecurrenceValue.getSelectedItemPosition() + 1; // Month is 1-indexed
                            break;
                    }
                }
            }

            // Validate dữ liệu đầu vào
            if (title.isEmpty()) {
                edtTitle.setError("Tiêu đề không được để trống");
                edtTitle.requestFocus();
                return;
            }
            if (description.isEmpty()) {
                edtDescription.setError("Mô tả không được để trống");
                edtDescription.requestFocus();
                return;
            }

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
                notificationType = StatusEnum.DEADLINE.getValue(); // Sửa typo enum
            } else {
                notificationType = StatusEnum.OVERDEADLINE.getValue(); // Sửa typo enum nếu cần
            }

            mViewModel.addNotification(new NotificationRequest(
                    title, description, selectedDateMillis, priority, notificationType, isDone, isRecurring, recurrenceType, recurrenceValue
            ),id -> {
                if (isToday()) {
                    int requestCodeDeadline = (int) id;
                    int requestCode15Min = (int) (id * 1000 + 15);
                    int requestCode5Min = (int) (id * 1000 + 5);
                    int requestCodeOverdue = (int) (id * 1000 + 999);

                    long time15MinBefore = selectedDateMillis - 15 * 60 * 1000;
                    NotificationScheduler.scheduleFixedTimeNotification(
                            requireContext(),
                            time15MinBefore,
                            requestCode15Min,
                            title,
                            "Chỉ còn 15 phút nữa là đến hạn!",
                            priority
                    );
                    long time5MinBefore = selectedDateMillis - 5 * 60 * 1000;
                    NotificationScheduler.scheduleFixedTimeNotification(
                            requireContext(),
                            time5MinBefore,
                            requestCode5Min,
                            title,
                            "Chỉ còn 5 phút nữa là đến hạn!",
                            priority
                    );
                    NotificationScheduler.scheduleFixedTimeNotification(
                            requireContext(),
                            selectedDateMillis,
                            requestCodeDeadline,
                            title,
                            description,
                            priority
                    );
                    long timeOverdue = selectedDateMillis + 60 * 1000;
                    NotificationScheduler.scheduleFixedTimeNotification(
                            requireContext(),
                            timeOverdue,
                            requestCodeOverdue,
                            title,
                            "Bạn đã quá hạn deadline này!",
                            priority
                    );


                }
            });
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

    private void populateRecurrenceValueSpinner(int recurrenceType) {
        ArrayAdapter<String> adapter;
        switch (recurrenceType) {
            case 0: // None
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{});
                break;
            case 1: // Daily
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Hàng ngày"});
                break;
            case 2: // Weekly
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.days_of_week));
                break;
            case 3: // Monthly
                String[] daysOfMonth = new String[31];
                for (int i = 0; i < 31; i++) {
                    daysOfMonth[i] = String.valueOf(i + 1);
                }
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, daysOfMonth);
                break;
            case 4: // Yearly
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.months_of_year));
                break;
            default:
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{});
                break;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecurrenceValue.setAdapter(adapter);
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
        dialog.show(getParentFragmentManager(), "successDialog");

        new Handler().postDelayed(() -> {
            Dialog actualDialog = dialog.getDialog();
            if (actualDialog != null && actualDialog.isShowing()) {
                dialog.dismiss();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
