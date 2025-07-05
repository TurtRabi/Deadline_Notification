package com.example.notificationdeadline.ui;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notificationdeadline.R;

public class FullScreenNotificationActivity extends AppCompatActivity {

    private Ringtone ringtone;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_notification);

        // Ensure the activity is shown on top of the lock screen and turns on the screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        TextView tvTitle = findViewById(R.id.tv_full_screen_title);
        TextView tvMessage = findViewById(R.id.tv_full_screen_message);
        Button btnDismiss = findViewById(R.id.btn_dismiss_full_screen);

        String title = getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message");

        tvTitle.setText(title);
        tvMessage.setText(message);

        // Play sound
        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notificationUri);
        if (ringtone != null) {
            ringtone.play();
        }

        // Vibrate
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] pattern = {0, 1000, 500, 1000}; // Start immediately, vibrate for 1s, pause for 0.5s, vibrate for 1s
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0)); // Repeat indefinitely
            } else {
                vibrator.vibrate(pattern, 0); // Repeat indefinitely
            }
        }

        btnDismiss.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}