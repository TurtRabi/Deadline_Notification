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
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.databinding.FragmentNotificationBinding;
import com.example.notificationdeadline.ui.activity_main;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;
    private RecyclerView recyclerView;
    private FragmentNotificationBinding binding;
    private NotificationHistoryAdapter adapter;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Báo cho Fragment biết nó có menu riêng
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater,container,false);
        Toolbar toolbar = binding.notificationToolbar;

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        // Không cần gọi setHasOptionsMenu ở đây nữa
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.notification_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            showDeleteTaskConfirmDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteTaskConfirmDialog() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.bg_dialog_delete_task, null);

        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        dialogTitle.setText("Bạn có chắc chắn muốn xóa tất cả lịch sử thông báo không? Hành động này không thể hoàn tác.");

        Button btnCancel = view.findViewById(R.id.btn_cancel1);
        Button btnDelete = view.findViewById(R.id.btn_delete);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            mViewModel.clearAllHistory();
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        recyclerView = binding.notificationRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationHistoryAdapter(getContext(), (notification, position, view) -> {
            mViewModel.updateIsReadNotification(notification.getId());
            view.setBackgroundColor(android.graphics.Color.parseColor("#F0F0F0F0"));
            adapter.notifyItemChanged(position);
        });

        recyclerView.setAdapter(adapter);

        // Observe LiveData
        mViewModel.getAllList().observe(getViewLifecycleOwner(), list -> {
            adapter.setData(list);
            // Optional: hiện empty view nếu rỗng
            binding.emptyNotificationView.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
        });

        if (getActivity() instanceof activity_main) {
            mViewModel.getUnreadCount().observe(getViewLifecycleOwner(), count -> {
                ((activity_main) getActivity()).updateNotificationBadge(count);
            });
        }
    }
}