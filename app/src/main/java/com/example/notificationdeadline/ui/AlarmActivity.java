package com.example.notificationdeadline.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notificationdeadline.R;

public class AlarmActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Các flag để bật sáng màn hình, luôn ở trên cùng
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
        // Nếu Android 8+ có thể cần thêm
        setShowWhenLocked(true);
        setTurnScreenOn(true);

        // Phát âm thanh báo thức hoặc rung ở đây
        // ...
    }
}