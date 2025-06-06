package com.example.notificationdeadline.dto.Enum;

import com.example.notificationdeadline.R;

public enum StatusEnum {
    UPCOMING(0, "Chưa đến hạn", R.drawable.bg_status_upcoming),
    NEAR_DEADLINE(1, "Cận kề deadline", R.drawable.bg_status_near),
    DEADlINE(2, "Đến hạn", R.drawable.bg_status_overdue),
    OVERDEALINE(3, "Quá hạn", R.drawable.bg_status_overdue_super);


    private final int value;
    private final String label;
    private final int backgroundResId;

    StatusEnum(int value, String label, int backgroundResId) {
        this.value = value;
        this.label = label;
        this.backgroundResId = backgroundResId;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public static StatusEnum fromValue(int value) {
        for (StatusEnum status : values()) {
            if (status.value == value) return status;
        }
        return UPCOMING;
    }
}