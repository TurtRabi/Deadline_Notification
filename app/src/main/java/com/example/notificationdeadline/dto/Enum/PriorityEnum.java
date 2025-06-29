package com.example.notificationdeadline.dto.Enum;

import com.example.notificationdeadline.R;  // import R để lấy drawable

public enum PriorityEnum {
    NORMAL(0, "Bình thường", R.drawable.chill),
    URGENT(1, "Khẩn cấp", R.drawable.warm),
    CRITICAL(2, "Cực kỳ quan trọng", R.drawable.super_warm);

    private final int value;
    private final String label;
    private final int drawableResId;

    PriorityEnum(int value, String label, int drawableResId) {
        this.value = value;
        this.label = label;
        this.drawableResId = drawableResId;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public static PriorityEnum fromValue(int value) {
        for (PriorityEnum priority : values()) {
            if (priority.value == value) {
                return priority;
            }
        }
        return NORMAL;
    }


    @Override
    public String toString() {
        return label;
    }
}
