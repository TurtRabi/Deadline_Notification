package com.example.notificationdeadline.dto.Enum;

import com.example.notificationdeadline.R;

public enum NotificationHistoryEnum {
    NORMAL(0, "Bình thường", R.drawable.low_priority_24px),
    URGENT(1, "Khẩn cấp", R.drawable.priority_high_24px),
    CRITICAL(2, "Cực kỳ quan trọng", R.drawable.priority_high_very),
    HELLODAY(3, "xin chào buổi sáng", R.drawable.sentiment_excited_24px);

    private final int id;
    private final String description;
    private final int iconResId;

    NotificationHistoryEnum(int id, String description, int iconResId) {
        this.id = id;
        this.description = description;
        this.iconResId = iconResId;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getIconResId() {
        return iconResId;
    }
}
