package com.example.notificationdeadline.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.dto.request.SettingRequest;
import com.example.notificationdeadline.service.NotificationService;
import com.example.notificationdeadline.service.SettingService;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 5000;

    private SettingService settingService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        settingService = new SettingService(this);

        settingService.getSetting("notification").observe(this,notification ->{
            if(notification==null){
                settingService.saveSetting(new SettingRequest("notification", "0"));
            }
        } );
        permisstion();

        ImageView imageGif = findViewById(R.id.imageGifSplash);
        Glide.with(this).asGif().load(R.drawable.haicau).into(imageGif);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, activity_main.class));
            finish();
        }, SPLASH_DURATION);
    }


    public void permisstion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001);
            }else{
                settingService.updateSetting(new SettingRequest("notification", "1"));
            }
        }
    }



}
