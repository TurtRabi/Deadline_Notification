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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.databinding.ActivityMainBinding;
import com.example.notificationdeadline.service.UserService;
import com.google.android.material.badge.BadgeDrawable;

public class activity_main extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        permisstion();
        service = new UserService(this);
        service.initDefaultUser();

        // Gắn fragment bằng code
        Fragment existingFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (existingFragment == null) {
            NavHostFragment navHostFragment = NavHostFragment.create(R.navigation.mobile_navigation);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, navHostFragment)
                    .setPrimaryNavigationFragment(navHostFragment)
                    .commit();

        }


        // Dùng navController sau khi fragment đã gắn
        binding.getRoot().post(() -> {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment_activity_main);
            if (navHostFragment != null) {
                NavController navController = navHostFragment.getNavController();
                NavigationUI.setupWithNavController(binding.navView, navController);
            }
        });

        // Ẩn/hiện BottomNavView khi mở bàn phím
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
        BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.notificationFragment); // ID item chuông
        if (unreadCount > 0) {
            badge.setVisible(true);
            badge.setNumber(unreadCount);
        } else {
            badge.clearNumber();
            badge.setVisible(false);
        }
    }

    public void permisstion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"bạn đã cấp quyên cho ứng dụng",Toast.LENGTH_LONG).show();
            } else {
                finish();
            }
        }
    }
}