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
import com.example.notificationdeadline.Adapter.FilterButtonAdapter;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.databinding.FragmentDashBoardBinding;
import com.example.notificationdeadline.ui.AddDeadline.AddDeadlineFragment;
import com.example.notificationdeadline.ui.SearchDeadline.SearchDeadlineFragment;
import com.example.notificationdeadline.ui.activity_main;

import java.util.Arrays;
import java.util.List;

public class DashBoardFragment extends Fragment {

    private DashBoardViewModel mViewModel;
    private RecyclerView recyclerView,recyclerView1,recyclerFilterButtons;
    private FragmentDashBoardBinding binding;
    private DeadlineAdapter adapter,adapter1;
    FilterButtonAdapter adapterFilter;

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
        recyclerView = binding.recyclerMoreDeadlines;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<NotificationEntity> allDeadlines =mViewModel.getAllList();
        adapter = new DeadlineAdapter();
       // hiêện deadline hom nay
        recyclerView1 = binding.recyclerTodayDeadlines;
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        List<NotificationEntity> list = mViewModel.getListNotificationByDay();
        adapter1 = new DeadlineAdapter(list);
        recyclerView1.setAdapter(adapter1);

        recyclerFilterButtons = binding.recyclerFilterButtons;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerFilterButtons.setLayoutManager(layoutManager);

        List<String> filters = Arrays.asList("Tất cả", "Ngày mai", "Sắp đến hạn", "Quá hạn");
        final String defaultFilter = "Tất cả";

        adapterFilter = new FilterButtonAdapter(filters, filter -> {
            List<NotificationEntity> filteredList;

            switch (filter) {
                case "Tất cả":
                    filteredList = mViewModel.getAllList();
                    break;
                case "Ngày mai":
                    filteredList = mViewModel.getDeadlinesForTomorrow();
                    break;
                case "Sắp đến hạn":
                    filteredList = mViewModel.getUpcomingDeadlines();
                    break;
                case "Quá hạn":
                    filteredList = mViewModel.getOverdueDeadlines();
                    break;
                default:
                    filteredList = mViewModel.getAllList();
                    break;
            }

            adapter.setData(filteredList);

        });


        recyclerFilterButtons.setAdapter(adapterFilter);


        adapter.setData(allDeadlines);
        recyclerView.setAdapter(adapter);
        adapterFilter.setSelectedFilter(defaultFilter);

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