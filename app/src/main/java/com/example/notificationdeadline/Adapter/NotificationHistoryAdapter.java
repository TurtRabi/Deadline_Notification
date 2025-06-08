package com.example.notificationdeadline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.dto.Enum.NotificationHistoryEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationHistoryAdapter extends RecyclerView.Adapter<NotificationHistoryAdapter.NotificationHistoryViewHoder> {
    private final List<NotificationHistoryEntity> notificationHistoryEntityList = new ArrayList<>();
    private final Context context;
    private final OnNotificationHistoryItemOnclickListerner listener;

    // Chỉ truyền context và callback, không truyền list!
    public NotificationHistoryAdapter(Context context, OnNotificationHistoryItemOnclickListerner listener) {
        this.context = context;
        this.listener = listener;
    }

    public interface OnNotificationHistoryItemOnclickListerner {
        void onItemClick(NotificationHistoryEntity notification, int position, View view);
    }

    // Update list data qua method này!
    public void setData(List<NotificationHistoryEntity> list) {
        notificationHistoryEntityList.clear();
        if (list != null) notificationHistoryEntityList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationHistoryViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_history, parent, false);
        return new NotificationHistoryViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHistoryViewHoder holder, int position) {
        NotificationHistoryEntity entity = notificationHistoryEntityList.get(position);
        holder.title.setText(entity.getTitle());
        holder.description.setText(entity.getMessage());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String timeFormatted = sdf.format(new Date(entity.getSentTimeMillis()));
        holder.time.setText(timeFormatted);

        // Hiển thị icon enum nếu urlImage là số, ngược lại để default
        if (entity.getUrlImage() != null) {
            try {
                int pos = Integer.parseInt(entity.getUrlImage());
                NotificationHistoryEnum historyEnum = NotificationHistoryEnum.fromId(pos);
                if (historyEnum != null) {
                    holder.imageView.setImageResource(historyEnum.getIconResId());
                } else {
                    holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } catch (NumberFormatException e) {
                holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

        if (!entity.isRead()) {
            holder.imageView1.setVisibility(View.VISIBLE);
            Animation blinkAnim = AnimationUtils.loadAnimation(context, R.anim.blink);
            holder.imageView1.startAnimation(blinkAnim);
        } else {
            holder.imageView1.clearAnimation();
            holder.imageView1.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#F0F0F0F0"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(entity, position, holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationHistoryEntityList.size();
    }

    public static class NotificationHistoryViewHoder extends RecyclerView.ViewHolder {
        TextView title, description, time;
        ImageView imageView, imageView1;

        public NotificationHistoryViewHoder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_notification_history_title);
            description = itemView.findViewById(R.id.txt_notification_history_description);
            time = itemView.findViewById(R.id.txt_notification_history_date);
            imageView = itemView.findViewById(R.id.txt_notification_history_image);
            imageView1 = itemView.findViewById(R.id.icon_new_badge);
        }
    }
}
