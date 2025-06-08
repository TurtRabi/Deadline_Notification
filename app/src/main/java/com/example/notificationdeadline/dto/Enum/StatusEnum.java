package com.example.notificationdeadline.dto.Enum;

import com.example.notificationdeadline.R;

public enum StatusEnum {
    UPCOMING(0, "Chưa đến hạn", R.drawable.bg_status_upcoming,R.color.color_upcoming),
    NEAR_DEADLINE(1, "Cận kề deadline", R.drawable.bg_status_near,R.color.color_near_deadline),
    DEADLINE(2, "Đến hạn", R.drawable.bg_status_overdue,R.color.color_deadline),
    OVERDEADLINE(3, "Quá hạn", R.drawable.bg_status_overdue_super,R.color.color_overdeadline);

    private final int value;
    private final String label;
    private final int backgroundResId;
    private final int backgroundId;

    StatusEnum(int value, String label, int backgroundResId,int backgroundId) {
        this.value = value;
        this.label = label;
        this.backgroundResId = backgroundResId;
        this.backgroundId = backgroundId;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public int getBackgroundId() {
        return backgroundId;
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
