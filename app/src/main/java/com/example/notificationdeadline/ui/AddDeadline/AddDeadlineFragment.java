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
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.databinding.FragmentAddDeadlineBinding;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.dto.request.RecurringDeadlineRequest;
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
                populateRecurrenceValueSpinner(spinnerRecurrenceType.getSelectedItemPosition());
            } else {
                spinnerRecurrenceType.setVisibility(View.GONE);
                spinnerRecurrenceValue.setVisibility(View.GONE);
            }
        });

        ArrayAdapter<CharSequence> recurrenceTypeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.recurrence_type_list,
                android.R.layout.simple_spinner_item
        );
        recurrenceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecurrenceType.setAdapter(recurrenceTypeAdapter);

        populateRecurrenceValueSpinner(spinnerRecurrenceType.getSelectedItemPosition());

        spinnerRecurrenceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateRecurrenceValueSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

        final int[] selectedYear = {Calendar.getInstance().get(Calendar.YEAR)};
        final int[] selectedMonth = {Calendar.getInstance().get(Calendar.MONTH)};
        final int[] selectedDay = {Calendar.getInstance().get(Calendar.DAY_OF_MONTH)};
        final int[] hour = {timePicker.getHour()};
        final int[] minute = {timePicker.getMinute()};

        timePicker.setOnTimeChangedListener((view1, hourOfDay, minute1) -> {
            hour[0] = hourOfDay;
            minute[0] = minute1;
            updateSelectedDateMillis(selectedYear[0], selectedMonth[0], selectedDay[0], hour[0], minute[0]);
        });

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedYear[0] = year;
            selectedMonth[0] = month;
            selectedDay[0] = dayOfMonth;
            updateSelectedDateMillis(year, month, dayOfMonth, hour[0], minute[0]);
        });

        updateSelectedDateMillis(selectedYear[0], selectedMonth[0], selectedDay[0], hour[0], minute[0]);

        binding.btnSave.setOnClickListener(v -> {
            EditText edtTitle = binding.txtTitle;
            EditText edtDescription = binding.txtDescription;

            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

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

            int priority = binding.spinnerPriority.getSelectedItemPosition();
            boolean isRecurring = switchIsRecurring.isChecked();

            if (isRecurring) {
                int recurrenceType = spinnerRecurrenceType.getSelectedItemPosition();
                int recurrenceTypeString = 0;
                int dayOfWeek = 0;
                int dayOfMonth = 0;
                int month = 0;

                if (recurrenceType == 0) {
                    CustomMessageDialog.newInstance("Lỗi", "Vui lòng chọn một loại lặp lại.", R.drawable.delete_24px, R.color.errorColor).show(getParentFragmentManager(), "errorDialog");
                    return;
                }

                switch (recurrenceType) {
                    case 1:
                        recurrenceTypeString = 1;
                        break;
                    case 2:
                        recurrenceTypeString = 2;
                        dayOfWeek = spinnerRecurrenceValue.getSelectedItemPosition() + 1;
                        break;
                    case 3:
                        recurrenceTypeString = 3;
                        try {
                            dayOfMonth = Integer.parseInt(spinnerRecurrenceValue.getSelectedItem().toString());
                        } catch (NumberFormatException e) {
                            CustomMessageDialog.newInstance("Lỗi", "Ngày trong tháng không hợp lệ.", R.drawable.delete_24px, R.color.errorColor).show(getParentFragmentManager(), "errorDialog");
                            return;
                        }
                        break;
                    case 4:
                        recurrenceTypeString = 4;
                        month = spinnerRecurrenceValue.getSelectedItemPosition();
                        CustomMessageDialog.newInstance("Thông báo", "Chức năng lặp lại hàng năm đang được phát triển.", R.drawable.info_24px, R.color.colorPrimary).show(getParentFragmentManager(), "infoDialog");
                        return;
                }

                RecurringDeadlineRequest request = new RecurringDeadlineRequest(
                        title, description, priority,
                        recurrenceTypeString,
                        String.format("%02d:%02d", hour[0], minute[0]),
                        dayOfWeek, dayOfMonth, month,
                        true
                );
                mViewModel.addRecurringDeadline(request,id -> {});
                if(isToday()){
                    int status = calculateStatus(selectedDateMillis);
                    NotificationRequest request1 = new NotificationRequest(
                            title, description, selectedDateMillis,
                            priority, status, false, true, recurrenceTypeString, 0
                    );
                    mViewModel.addNotification(request1,id->{

                    });
                }

            } else {
                int status = calculateStatus(selectedDateMillis);
                NotificationRequest request = new NotificationRequest(
                        title, description, selectedDateMillis,
                        priority, status, false, false, 0, 0
                );
                mViewModel.addNotification(request, id -> {
                    // Schedule logic here
                });
            }
            showSuccessDialogWithAutoDismiss();
        });
    }

    private void updateSelectedDateMillis(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        selectedDateMillis = calendar.getTimeInMillis();
        Log.d("Calendar", "\uD83D\uDCCB Ngày giờ được chọn: " + new Date(selectedDateMillis));
    }

    private void populateRecurrenceValueSpinner(int recurrenceType) {
        ArrayAdapter<String> adapter;
        spinnerRecurrenceValue.setAdapter(null);

        switch (recurrenceType) {
            case 0:
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{});
                break;
            case 1:
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Hàng ngày"});
                break;
            case 2:
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.days_of_week));
                break;
            case 3:
                String[] daysOfMonth = new String[31];
                for (int i = 0; i < 31; i++) {
                    daysOfMonth[i] = String.valueOf(i + 1);
                }
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, daysOfMonth);
                break;
            case 4:
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

    private int calculateStatus(long deadlineMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        long timeLeftMillis = deadlineMillis - currentTimeMillis;
        long timeLeftHours = timeLeftMillis / (60 * 60 * 1000);

        if (timeLeftMillis <= 0) {
            return StatusEnum.OVERDEADLINE.getValue();
        } else if (timeLeftHours < 1) {
            return StatusEnum.DEADLINE.getValue();
        } else if (timeLeftHours < 6) {
            return StatusEnum.NEAR_DEADLINE.getValue();
        } else if (timeLeftHours < 24) {
            return StatusEnum.SOON.getValue();
        } else {
            return StatusEnum.UPCOMING.getValue();
        }
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
