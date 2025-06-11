package com.example.notificationdeadline.ui.NotificationDeadlineInfor;

import static com.example.notificationdeadline.dto.Enum.StatusEnum.DEADLINE;
import static com.example.notificationdeadline.dto.Enum.StatusEnum.NEAR_DEADLINE;
import static com.example.notificationdeadline.dto.Enum.StatusEnum.OVERDEADLINE;
import static com.example.notificationdeadline.dto.Enum.StatusEnum.SUCCESS;
import static com.example.notificationdeadline.dto.Enum.StatusEnum.UPCOMING;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationdeadline.Adapter.TaskAdapter;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.TaskEntity;
import com.example.notificationdeadline.databinding.FragmentDeadlineNotificationDetailBinding;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.mapper.NotificationMapper;
import com.example.notificationdeadline.ui.dialog.CustomMessageDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeadlineNotificationDetail extends Fragment {

    private DeadlineNotificationDetailViewModel mViewModel;
    private FragmentDeadlineNotificationDetailBinding binding;
    private TaskAdapter adapter;
    private RecyclerView recyclerView;

    public static DeadlineNotificationDetail newInstance() {
        return new DeadlineNotificationDetail();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDeadlineNotificationDetailBinding.inflate(inflater,container,false);
        Toolbar toolbar = binding.notificationDetailToolbar;
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);



        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeadlineNotificationDetailViewModel.class);
        if(getArguments()!=null){
            NotificationRequest notificationRequest= (NotificationRequest) getArguments().get("notificationEntity");
            NotificationEntity notification = NotificationMapper.toEntity(notificationRequest);
            binding.tvNotificationTitle.setText(notification.getTitle());
            binding.tvNotificationDescription.setText(notification.getMessage());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(new Date(notification.getTimeMillis()));
            binding.tvNotificationDeadline.setText("Đến hạn: " + formattedDate);

            StatusEnum status = StatusEnum.fromValue(notification.getStatus());
            Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
            binding.viewStatusDot.startAnimation(blinkAnimation);


            adapter = new TaskAdapter(
                    (view, taskEntity) -> {

                        showDeleteTaskConfirmDialog(taskEntity);
            },
                    (view, taskEntity) -> {

                        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.gray));
                        TextView textView = view.findViewById(R.id.text_task_name);
                        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        mViewModel.IsdoneTask(taskEntity);
                    }
            );

            mViewModel.getAllTaskByIdDeadline(notification.getId()).observe(getViewLifecycleOwner(), tasks -> {
                if(tasks==null) return;
                int doneCount = 0;
                for (TaskEntity task : tasks) {
                    if (task.isDone()) doneCount++;
                }

                if (doneCount == tasks.size() && doneCount!=0) {

                    mViewModel.updateSuccessDeadline(notification.getId());
                }
                else if(!notification.isSuccess()){
                    long now = System.currentTimeMillis();
                    long diff = notification.getTimeMillis() - now;

                    long sixHours = 6 * 60 * 60 * 1000;
                    long tenHours = 10 * 60 * 60 * 1000;

                    int notificationType;
                    if (diff > tenHours) {
                        notificationType = StatusEnum.UPCOMING.getValue();
                    } else if (diff > sixHours) {
                        notificationType = StatusEnum.NEAR_DEADLINE.getValue();
                    } else if (diff > 0) {
                        notificationType = StatusEnum.DEADLINE.getValue();
                    } else {
                        notificationType = StatusEnum.OVERDEADLINE.getValue();
                    }
                    mViewModel.updateNotSuccessDeadline(notification.getId(),notificationType);
                }

            });

            mViewModel.getAllTaskByIdDeadline(notification.getId()).observe(getViewLifecycleOwner(), tasks->{
                adapter.setData(tasks);
            });
            recyclerView = binding.recyclerTaskList;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);


            int color;
            switch (status) {
                case UPCOMING:
                    color = ContextCompat.getColor(getContext(), UPCOMING.getBackgroundId());
                    binding.tvStatusLabel.setText(UPCOMING.getLabel());
                    break;
                case NEAR_DEADLINE:
                    color = ContextCompat.getColor(getContext(), NEAR_DEADLINE.getBackgroundId());
                    binding.tvStatusLabel.setText(NEAR_DEADLINE.getLabel());

                    break;
                case DEADLINE:
                    color = ContextCompat.getColor(getContext(), DEADLINE.getBackgroundId());
                    binding.tvStatusLabel.setText(DEADLINE.getLabel());

                    break;
                case OVERDEADLINE:
                    color = ContextCompat.getColor(getContext(), OVERDEADLINE.getBackgroundId());
                    binding.tvStatusLabel.setText(OVERDEADLINE.getLabel());
                    break;
                case SUCCESS:
                    color = ContextCompat.getColor(getContext(), SUCCESS.getBackgroundId());
                    binding.tvStatusLabel.setText(SUCCESS.getLabel());
                    break;
                default:
                    color = ContextCompat.getColor(getContext(), UPCOMING.getBackgroundId());
                    binding.tvStatusLabel.setText(UPCOMING.getLabel());
                    break;
            }
            binding.tvStatusLabel.setTextColor(color);
            Drawable background = binding.viewStatusDot.getBackground();
            if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(color);
            }

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId()==R.id.btn_add_task){
                        showAddTaskDialog(notification);
                    }else if(v.getId()==R.id.btn_complete_deadline){

                        showSuccessConfirmDialog(notification);

                    }else if(v.getId()==R.id.btn_delete_deadline){
                        showDeleteConfirmDialog(notification);

                    }
                }
            };
            if(notification.isSuccess()){
                binding.btnCompleteDeadline.setActivated(false);
                binding.btnCompleteDeadline.setClickable(false);
                binding.btnCompleteDeadline.setFocusable(false);
            }

            binding.btnAddTask.setOnClickListener(listener);
            binding.btnCompleteDeadline.setOnClickListener(listener);
            binding.btnDeleteDeadline.setOnClickListener(listener);

        }

    }

    private void showDeleteConfirmDialog(NotificationEntity notification) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.bg_dialog_delete_task, null);

        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        dialogTitle.setText("Bạn có chắc muốn xóa?");

        Button btnCancel = view.findViewById(R.id.btn_cancel1);
        Button btnDelete = view.findViewById(R.id.btn_delete);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            mViewModel.removeNotification(notification,requireContext());
            dialog.dismiss();
            NavController navController = Navigation.findNavController(requireView());
            if (!navController.navigateUp()) {
                requireActivity().onBackPressed();
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }

    private void showSuccessConfirmDialog(NotificationEntity notification) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.bg_dialog_delete_task, null);

        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        dialogTitle.setText("Bạn có chắc muốn hoàn thành?");

        Button btnCancel = view.findViewById(R.id.btn_cancel1);
        Button btnDelete = view.findViewById(R.id.btn_delete);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            mViewModel.updateSuccessDeadline(notification.getId());
            dialog.dismiss();
            NavController navController = Navigation.findNavController(requireView());
            if (!navController.navigateUp()) {
                requireActivity().onBackPressed();
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }
    private void showDeleteTaskConfirmDialog(TaskEntity taskEntity) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.bg_dialog_delete_task, null);

        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        dialogTitle.setText("Bạn có chắc muốn xóa?");

        Button btnCancel = view.findViewById(R.id.btn_cancel1);
        Button btnDelete = view.findViewById(R.id.btn_delete);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            mViewModel.deleteTask(taskEntity);
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }


    private void showAddTaskDialog(NotificationEntity notification) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.bg_dialog_add_task, null);
        TextView textView = view.findViewById(R.id.dialogTitle);
        textView.setText("Tạo một công việc mới");

        EditText input = view.findViewById(R.id.et_task_name);
        Button btnAdd = view.findViewById(R.id.btn_add);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnAdd.setOnClickListener(v -> {
            String taskName = input.getText().toString().trim();
            if (taskName.isEmpty()) {
                input.setError("Vui lòng nhập tên công việc");
                input.requestFocus();
            } else {
                int notificationId = notification.getId();
                TaskEntity task = new TaskEntity();
                task.setNotificationId(notificationId);
                task.setContent(taskName);
                task.setDone(false);


                long now = System.currentTimeMillis();
                long diff = notification.getTimeMillis() - now;

                long sixHours = 6 * 60 * 60 * 1000;
                long tenHours = 10 * 60 * 60 * 1000;

                int notificationType;
                if (diff > tenHours) {
                    notificationType = StatusEnum.UPCOMING.getValue();
                } else if (diff > sixHours) {
                    notificationType = StatusEnum.NEAR_DEADLINE.getValue();
                } else if (diff > 0) {
                    notificationType = StatusEnum.DEADLINE.getValue();
                } else {
                    notificationType = StatusEnum.OVERDEADLINE.getValue();
                }
                mViewModel.updateNotSuccessDeadline(notification.getId(),notificationType);

                mViewModel.saveTask(task, id -> {
                    requireActivity().runOnUiThread(() -> {
                        // Hoặc dùng View.post()
                        binding.getRoot().postDelayed(() -> {
                            showSuccessDialogWithAutoDismiss();
                        }, 1000);
                    });
                });



                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavController navController = Navigation.findNavController(requireView());
            if (!navController.navigateUp()) {
                requireActivity().onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSuccessDialogWithAutoDismiss() {
        CustomMessageDialog dialog = CustomMessageDialog.newInstance(
                "Task đã được tạo thành công!",
                "Task ",
                R.drawable.ic_launcher_foreground,
                R.color.successColor
        );



        dialog.show(getParentFragmentManager(), "successDialog");

        new Handler().postDelayed(() -> {
            Dialog actualDialog = dialog.getDialog();
            if (actualDialog != null && actualDialog.isShowing()) {
                dialog.dismiss();
            }
        }, 2000);
    }


}