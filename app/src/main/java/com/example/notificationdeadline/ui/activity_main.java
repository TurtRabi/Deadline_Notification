package com.example.notificationdeadline.ui;

import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.databinding.ActivityMainBinding;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.dto.request.SettingRequest;
import com.example.notificationdeadline.service.NotificationService;
import com.example.notificationdeadline.service.SettingService;
import com.example.notificationdeadline.service.UserService;
import com.google.android.material.badge.BadgeDrawable;
import android.os.Handler;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class activity_main extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserService service;
    private SettingService settingService;
    private NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notificationService = new NotificationService(this);
        updateAllDeadlineStatuses();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1002);
            }
        }

        binding.navView.setItemIconTintList(null);
        binding.navView.setItemBackgroundResource(android.R.color.transparent); // ✅ tắt nền chọn
        binding.navView.setItemRippleColor(null);
        settingService = new SettingService(this);


        settingService.getSetting("theme").observe(this,theme -> {
            if(theme ==null){
                settingService.saveSetting(new SettingRequest("theme", "1"));
            }
        });


        settingService.getSetting("theme").observe(this,settingEntity ->{
            String themeValue =(settingEntity !=null)? settingEntity.getValue():"1";
            if ("1".equals(themeValue)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });


        service = new UserService(this);
        service.initDefaultUser();


        Fragment existingFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (existingFragment == null) {
            NavHostFragment navHostFragment = NavHostFragment.create(R.navigation.mobile_navigation);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, navHostFragment)
                    .setPrimaryNavigationFragment(navHostFragment)
                    .commit();
        }


        binding.getRoot().post(() -> {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment_activity_main);
            if (navHostFragment != null) {
                NavController navController = navHostFragment.getNavController();
                binding.navView.setOnItemSelectedListener(item -> {
                    int desId = item.getItemId();

                    // Show loading overlay
                    binding.loadingOverlay.setVisibility(View.VISIBLE);

                    // Delay navigation by 1 second
                    new Handler().postDelayed(() -> {
                        for (int i = 0; i < binding.navView.getMenu().size(); i++) {
                            binding.navView.findViewById(binding.navView.getMenu().getItem(i).getItemId()).setScaleX(1f);
                            binding.navView.findViewById(binding.navView.getMenu().getItem(i).getItemId()).setScaleY(1f);
                        }


                        View iconView = binding.navView.findViewById(item.getItemId());
                        if (iconView != null) {
                            iconView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start();
                        }

                        if(navController.getCurrentDestination()!=null && navController.getCurrentDestination().getId() ==desId){
                            binding.loadingOverlay.setVisibility(View.GONE);
                            return;
                        }
                        NavOptions navOptions = new NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_in_right)
                                .setExitAnim(R.anim.slide_out_left)
                                .setPopEnterAnim(R.anim.slide_in_left)
                                .setPopExitAnim(R.anim.slide_out_right)
                                .build();
                        navController.navigate(desId,null,navOptions);

                        // Hide loading overlay after navigation
                        binding.loadingOverlay.setVisibility(View.GONE);
                    }, 1000); // 1 second delay

                    return true;
                });
            }
        });


        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;
            if (keypadHeight > screenHeight * 0.15) {
                binding.navView.setVisibility(View.GONE);
            } else {
                binding.navView.setVisibility(View.VISIBLE);
            }
        });


    }

    public void updateNotificationBadge(int unreadCount) {
        BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.notificationFragment);
        if (unreadCount > 0) {
            badge.setVisible(true);
            badge.setNumber(unreadCount);
        } else {
            badge.clearNumber();
            badge.setVisible(false);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                settingService.updateSetting(new SettingRequest("notification", "1"));
                Toast.makeText(this, "Bạn đã cấp quyền cho ứng dụng", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền cho ứng dụng", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 1002) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bạn đã cấp quyền gửi thông báo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền gửi thông báo để ứng dụng hoạt động.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateAllDeadlineStatuses() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<NotificationEntity> allNotifications = notificationService.getAllNotificationsSync();
            long currentTimeMillis = System.currentTimeMillis();

            for (NotificationEntity notification : allNotifications) {
                if (notification.isSuccess()) {
                    continue; // Skip if already marked as success
                }

                int newStatus;
                if (notification.getTime() < currentTimeMillis) {
                    newStatus = StatusEnum.OVERDEADLINE.getValue();
                } else {
                    long timeLeftMillis = notification.getTime() - currentTimeMillis;
                    long timeLeftHours = timeLeftMillis / (60 * 60 * 1000);

                    if (timeLeftHours < 1) {
                        newStatus = StatusEnum.DEADLINE.getValue();
                    } else if (timeLeftHours < 6) {
                        newStatus = StatusEnum.NEAR_DEADLINE.getValue();
                    } else if (timeLeftHours < 24) {
                        newStatus = StatusEnum.SOON.getValue();
                    } else {
                        newStatus = StatusEnum.UPCOMING.getValue();
                    }
                }
                // Only update if status has changed
                if (notification.getStatus() != newStatus) {
                    notificationService.updateStatus(newStatus, notification.getId());
                }
            }
        });
    }
}
