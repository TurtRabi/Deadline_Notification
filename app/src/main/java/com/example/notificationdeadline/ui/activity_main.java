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


        binding.navView.setItemIconTintList(null);

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

                    for (int i = 0; i < binding.navView.getMenu().size(); i++) {
                        binding.navView.findViewById(binding.navView.getMenu().getItem(i).getItemId()).setScaleX(1f);
                        binding.navView.findViewById(binding.navView.getMenu().getItem(i).getItemId()).setScaleY(1f);
                    }


                    View iconView = binding.navView.findViewById(item.getItemId());
                    if (iconView != null) {
                        iconView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start();
                    }

                    if(navController.getCurrentDestination()!=null && navController.getCurrentDestination().getId() ==desId){
                        return true;
                    }
                    NavOptions navOptions = new NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build();
                    navController.navigate(desId,null,navOptions);
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

        checkUpdateStatus();
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
        }
    }

    public void checkUpdateStatus(){

        notificationService.fetchAllNotificationsByDay().observe(this,listNotification ->{
            if(!listNotification.isEmpty()&&listNotification.size()!=0){
                for(NotificationEntity entity: listNotification){
                    long now = System.currentTimeMillis();
                    long diff = entity.getTimeMillis() - now;

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
                    notificationService.updateStatus(entity.getId(),notificationType);

                }
            }
        });

    }
}
