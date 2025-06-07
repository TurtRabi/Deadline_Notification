package com.example.notificationdeadline.ui.DashBoard;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.databinding.FragmentDashBoardBinding;
import com.example.notificationdeadline.ui.AddDeadline.AddDeadlineFragment;
import com.example.notificationdeadline.ui.SearchDeadline.SearchDeadlineFragment;
import com.example.notificationdeadline.ui.activity_main;

import java.util.List;

public class DashBoardFragment extends Fragment {

    private DashBoardViewModel mViewModel;
    private RecyclerView recyclerView;
    private FragmentDashBoardBinding binding;
    private DeadlineAdapter adapter;

    public static DashBoardFragment newInstance() {
        return new DashBoardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDashBoardBinding.inflate(inflater,container,false);
        Toolbar toolbar = binding.homeToolbar;
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashBoardViewModel.class);
        recyclerView = binding.listDeadlineNotidication;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DeadlineAdapter();
        recyclerView.setAdapter(adapter);
        mViewModel.getAllList().observe(getViewLifecycleOwner(), new Observer<List<NotificationEntity>>() {
            @Override
            public void onChanged(List<NotificationEntity> notificationEntityList) {
                adapter.setData(notificationEntityList);
            }
        });

        if(getActivity() instanceof activity_main){
            ((activity_main) getActivity()).updateNotificationBadge(mViewModel.getUnreadCount());
        }
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