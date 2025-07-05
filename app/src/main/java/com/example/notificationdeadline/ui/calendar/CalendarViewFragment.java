package com.example.notificationdeadline.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationdeadline.Adapter.DeadlineAdapter;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.databinding.FragmentCalendarViewBinding;
import com.example.notificationdeadline.mapper.NotificationMapper;

import java.util.Calendar;
import java.util.List;

public class CalendarViewFragment extends Fragment {

    private CalendarViewViewModel mViewModel;
    private FragmentCalendarViewBinding binding;
    private DeadlineAdapter deadlineAdapter;

    public static CalendarViewFragment newInstance() {
        return new CalendarViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCalendarViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CalendarViewViewModel.class);

        // Setup Toolbar
        Toolbar toolbar = binding.calendarToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true); // Important to receive onOptionsItemSelected callbacks

        CalendarView calendarView = binding.calendarView;
        RecyclerView recyclerView = binding.recyclerViewDeadlines;

        deadlineAdapter = new DeadlineAdapter(new DeadlineAdapter.OnDeadlineOnClickListerner() {
            @Override
            public void onItemClickListerner(int position, NotificationEntity entity) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putParcelable("notificationEntity", NotificationMapper.toRequest(entity));
                navController.navigate(R.id.action_calendarViewFragment_to_deadlineNotificationDetail, bundle);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(deadlineAdapter);

        // Set initial date to today
        Calendar today = Calendar.getInstance();
        long startOfDay = getStartOfDayMillis(today);
        long endOfDay = getEndOfDayMillis(today);
        observeDeadlinesForDate(startOfDay, endOfDay);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth, 0, 0, 0);
                selectedDate.set(Calendar.MILLISECOND, 0);

                long start = getStartOfDayMillis(selectedDate);
                long end = getEndOfDayMillis(selectedDate);
                observeDeadlinesForDate(start, end);
            }
        });
    }

    private void observeDeadlinesForDate(long startTime, long endTime) {
        mViewModel.getDeadlinesForDate(startTime, endTime).observe(getViewLifecycleOwner(), newDeadlines -> {
            deadlineAdapter.setData(newDeadlines);
        });
    }

    private long getStartOfDayMillis(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private long getEndOfDayMillis(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
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
