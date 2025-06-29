package com.example.notificationdeadline.dto.Enum;

import com.example.notificationdeadline.R;

public enum NotificationHistoryEnum {
    NORMAL(0, "Bình thường", R.drawable.chill),
    URGENT(1, "Khẩn cấp", R.drawable.warm),
    CRITICAL(2, "Cực kỳ quan trọng", R.drawable.super_warm),
    HELLODAY(3, "Xin chào buổi sáng", R.drawable.hellomoning_removebg_preview);

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

    public static NotificationHistoryEnum fromId(int id) {
        for (NotificationHistoryEnum e : values()) {
            if (e.id == id) return e;
        }
        return NORMAL;
    }


    @Override
    public String toString() {
        return description;
    }
}
