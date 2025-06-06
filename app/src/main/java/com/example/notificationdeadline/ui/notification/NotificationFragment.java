package com.example.notificationdeadline.ui.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationdeadline.Adapter.NotificationHistoryAdapter;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.databinding.FragmentNotificationBinding;
import com.example.notificationdeadline.ui.activity_main;

public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;
    private RecyclerView recyclerView;
    private FragmentNotificationBinding binding;
    private NotificationHistoryAdapter adapter;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater,container,false);
        Toolbar toolbar = binding.notificationToolbar;

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        recyclerView = binding.notificationRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationHistoryAdapter(getContext(),mViewModel.getAllList(),(notification, position,view) -> {
            mViewModel.updateIsReadNotification(notification.id);
            view.setBackgroundColor(android.graphics.Color.parseColor("#F0F0F0F0"));
            adapter.notifyDataSetChanged();
        });

        recyclerView.setAdapter(adapter);


    }

}