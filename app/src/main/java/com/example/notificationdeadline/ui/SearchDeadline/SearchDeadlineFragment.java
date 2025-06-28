package com.example.notificationdeadline.ui.SearchDeadline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.notificationdeadline.Adapter.DeadlineAdapter;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.databinding.FragmentSearchDeadlineBinding;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.mapper.NotificationMapper;

public class SearchDeadlineFragment extends Fragment {

    private SearchDeadlineViewModel mViewModel;
    private FragmentSearchDeadlineBinding binding;
    private DeadlineAdapter adapter;
    private RecyclerView recyclerView;

    public static SearchDeadlineFragment newInstance() {
        return new SearchDeadlineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= FragmentSearchDeadlineBinding.inflate(inflater, container, false);
        Toolbar toolbar = binding.searchToolbar1;
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchDeadlineViewModel.class);

        recyclerView = binding.searchResultRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DeadlineAdapter((position, entity) -> openDetail(entity));
        recyclerView.setAdapter(adapter);

        // Initially show the empty state
        performSearch("");

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // If keyword is empty, show the empty view and clear the adapter
            adapter.setData(null); // Clear previous results
            binding.emptySearchView.setVisibility(View.VISIBLE);
            return;
        }

        mViewModel.searchDeadlines(keyword).observe(getViewLifecycleOwner(), list -> {
            adapter.setData(list);
            boolean empty = list == null || list.isEmpty();
            binding.emptySearchView.setVisibility(empty ? View.VISIBLE : View.GONE);
        });
    }

    private void openDetail(com.example.notificationdeadline.data.entity.NotificationEntity entity) {
        NavController navController = Navigation.findNavController(requireView());
        NotificationRequest request = NotificationMapper.toRequest(entity);
        Bundle bundle = new Bundle();
        bundle.putParcelable("notificationEntity", request);
        navController.navigate(R.id.action_to_deadline_notification_detail, bundle);
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