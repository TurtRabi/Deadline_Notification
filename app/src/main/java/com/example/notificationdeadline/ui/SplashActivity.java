package com.example.notificationdeadline.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.notificationdeadline.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 5000; // 3 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageGif = findViewById(R.id.imageGifSplash);
        Glide.with(this).asGif().load(R.drawable.haicau).into(imageGif);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, activity_main.class));
            finish();
        }, SPLASH_DURATION);
    }
}
