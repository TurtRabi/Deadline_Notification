package com.example.notificationdeadline.ui.NotificationDeadlineInfor;

import static com.example.notificationdeadline.dto.Enum.StatusEnum.DEADLINE;
import static com.example.notificationdeadline.dto.Enum.StatusEnum.NEAR_DEADLINE;
import static com.example.notificationdeadline.dto.Enum.StatusEnum.OVERDEADLINE;
import static com.example.notificationdeadline.dto.Enum.StatusEnum.UPCOMING;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.databinding.FragmentDeadlineNotificationDetailBinding;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.dto.request.NotificationRequest;
import com.example.notificationdeadline.mapper.NotificationMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeadlineNotificationDetail extends Fragment {

    private DeadlineNotificationDetailViewModel mViewModel;
    private FragmentDeadlineNotificationDetailBinding binding;

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

            binding.tvStatusLabel.setText(status.getLabel());
            Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
            binding.viewStatusDot.startAnimation(blinkAnimation);


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
                default:
                    color = ContextCompat.getColor(getContext(), UPCOMING.getBackgroundId());
                    binding.tvStatusLabel.setText(UPCOMING.getLabel());

                    break;
            }
            binding.tvStatusLabel.setTextColor(color     );
            Drawable background = binding.viewStatusDot.getBackground();
            if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(color);
            }


        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btn_add_task){

                }else if(v.getId()==R.id.btn_complete_deadline){

                }else if(v.getId()==R.id.btn_delete_deadline);
            }
        };

        binding.btnAddTask.setOnClickListener(listener);
        binding.btnCompleteDeadline.setOnClickListener(listener);
        binding.btnDeleteDeadline.setOnClickListener(listener);
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

}