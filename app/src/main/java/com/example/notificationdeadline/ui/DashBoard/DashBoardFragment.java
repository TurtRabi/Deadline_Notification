package com.example.notificationdeadline.ui.DashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.notificationdeadline.Adapter.DeadlineAdapter;
import com.example.notificationdeadline.Adapter.FilterButtonAdapter;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.databinding.FragmentDashBoardBinding;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.mapper.NotificationMapper;
import com.example.notificationdeadline.ui.AddDeadline.AddDeadlineFragment;
import com.example.notificationdeadline.ui.SearchDeadline.SearchDeadlineFragment;
import com.example.notificationdeadline.ui.activity_main;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Arrays;
import java.util.List;

public class DashBoardFragment extends Fragment {

    private DashBoardViewModel mViewModel;
    private RecyclerView recyclerView,recyclerView1,recyclerFilterButtons;
    private FragmentDashBoardBinding binding;
    private DeadlineAdapter adapter,adapter1;
    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    FilterButtonAdapter adapterFilter;

    public static DashBoardFragment newInstance() {
        return new DashBoardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDashBoardBinding.inflate(inflater, container, false);
        Toolbar toolbar = binding.homeToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        NestedScrollView nestedScrollView = binding.nestedScrollView;
        AppBarLayout appBarLayout = binding.appBarLayout;

        // Đặt toolbar ẩn lúc đầu và alpha bằng 0
        appBarLayout.setVisibility(View.VISIBLE);
        appBarLayout.setAlpha(1f);

        // Biến trạng thái toolbar hiện hay ẩn
        final boolean[] isAppBarVisible = {false};

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) {
                    // Scroll lên - ẩn Toolbar nếu đang hiển thị
                    if (isAppBarVisible[0]) {
                        appBarLayout.animate()
                                .alpha(0f)
                                .setDuration(300)
                                .withEndAction(() -> {
                                    appBarLayout.setVisibility(View.GONE);
                                    isAppBarVisible[0] = false;
                                })
                                .start();
                    }
                } else if (scrollY > oldScrollY) {
                    // Scroll xuống - hiện Toolbar nếu đang ẩn
                    if (!isAppBarVisible[0]) {
                        appBarLayout.setVisibility(View.VISIBLE);
                        appBarLayout.animate()
                                .alpha(1f)
                                .setDuration(300)
                                .start();
                        isAppBarVisible[0] = true;
                    }
                }
            }
        });

        binding.getRoot().setOnClickListener(v -> {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                // Double tap detected, hiện app bar
                if (!isAppBarVisible[0]) {
                    appBarLayout.setVisibility(View.VISIBLE);
                    appBarLayout.animate()
                            .alpha(1f)
                            .setDuration(300)
                            .start();
                    isAppBarVisible[0] = true;
                }
            }
            lastClickTime = clickTime;
        });

        setHasOptionsMenu(true);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashBoardViewModel.class);

        recyclerView = binding.recyclerMoreDeadlines;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DeadlineAdapter((position, entity) -> {
            IntentDescriptionDeadlineTask(entity);
        });
        recyclerView.setAdapter(adapter);

        recyclerView1 = binding.recyclerTodayDeadlines;
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter1 = new DeadlineAdapter((position, entity) -> {
            IntentDescriptionDeadlineTask(entity);
        });
        recyclerView1.setAdapter(adapter1);

        mViewModel.getFilteredList().observe(getViewLifecycleOwner(), notificationEntityList -> {
            boolean empty = (notificationEntityList == null || notificationEntityList.isEmpty());
            binding.emptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
            for(NotificationEntity entity: notificationEntityList){
                mViewModel.getTasksForNotification(entity.getId()).observe(getViewLifecycleOwner(), tasks -> {
                    if(tasks.size()!=0||!tasks.isEmpty()){
                        int doneCount = 0;
                        if (tasks != null && !tasks.isEmpty()) {
                            for (TaskEntity task : tasks) {
                                if (task.isDone()) doneCount++;
                            }
                            int progress = (int) (100.0 * doneCount / tasks.size());
                            adapter.updateProgress(entity.getId(), progress);
                        } else {
                            adapter.updateProgress(entity.getId(), 0);
                        }
                    }
                });
            }
            adapter.setData(notificationEntityList);
        });

        mViewModel.getListNotificationByDay().observe(getViewLifecycleOwner(), todayList -> {
            boolean emptyToday = (todayList == null || todayList.isEmpty());
            binding.emptyTodayView.setVisibility(emptyToday ? View.VISIBLE : View.GONE);
            for(NotificationEntity entity: todayList){
                mViewModel.getTasksForNotification(entity.getId()).observe(getViewLifecycleOwner(), tasks -> {
                    if(tasks.size()!=0||!tasks.isEmpty()){
                        int doneCount = 0;
                        if (tasks != null && !tasks.isEmpty()) {
                            for (TaskEntity task : tasks) {
                                if (task.isDone()) doneCount++;
                            }
                            int progress = (int) (100.0 * doneCount / tasks.size());
                            adapter1.updateProgress(entity.getId(), progress);
                        } else {
                            adapter1.updateProgress(entity.getId(), 0);
                        }
                    }
                });
            }
            adapter1.setData(todayList);
        });

        recyclerFilterButtons = binding.recyclerFilterButtons;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerFilterButtons.setLayoutManager(layoutManager);

        List<String> filters = Arrays.asList("Tất cả", "Ngày mai", "Sắp đến hạn", "Quá hạn", "Hoàn thành");
        final String defaultFilter = "Tất cả";

        adapterFilter = new FilterButtonAdapter(filters, filter -> {
            // KHÔNG clearData nữa!
            mViewModel.setFilter(filter);
        });
        recyclerFilterButtons.setAdapter(adapterFilter);
        adapterFilter.setSelectedFilter(defaultFilter);
        mViewModel.setFilter(defaultFilter);

        if (getActivity() instanceof activity_main) {
            mViewModel.getUnreadCount().observe(getViewLifecycleOwner(), count -> {
                ((activity_main) getActivity()).updateNotificationBadge(count);
            });
        }
    }


    private void IntentDescriptionDeadlineTask(NotificationEntity entity) {
        NavController navController = Navigation.findNavController(requireView());
        Bundle bundle = new Bundle();
        NotificationRequest request = NotificationMapper.toRequest(entity);
        bundle.putParcelable("notificationEntity", request);
        navController.navigate(R.id.action_to_deadline_notification_detail,bundle);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu_toolbar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id ==R.id.action_search){
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_to_find_deadline);
            return true;
        }else if( id == R.id.action_add){
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_to_add_deadline);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}