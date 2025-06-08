package com.example.notificationdeadline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.dto.Enum.PriorityEnum;
import com.example.notificationdeadline.dto.Enum.StatusEnum;
import com.example.notificationdeadline.service.TaskService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DeadlineAdapter extends RecyclerView.Adapter<DeadlineAdapter.DeadlineViewHolder> {
    private final List<NotificationEntity> notificationEntityList = new ArrayList<>();
    private final Map<Integer, Integer> progressMap = new HashMap<>();
    private OnDeadlineOnClickListerner onDeadlineOnClickListerner;

    public DeadlineAdapter(OnDeadlineOnClickListerner onDeadlineOnClickListerner) {
        this.onDeadlineOnClickListerner=onDeadlineOnClickListerner;
    }

    public interface  OnDeadlineOnClickListerner{
        void onItemClickListerner(int position,NotificationEntity entity);
    }

    public void updateProgress(int notificationId, int progressPercent) {
        progressMap.put(notificationId, progressPercent);
        int index = -1;
        for (int i = 0; i < notificationEntityList.size(); i++) {
            if (notificationEntityList.get(i).getId() == notificationId) {
                index = i;
                break;
            }
        }
        if (index != -1) notifyItemChanged(index);
    }



    // Luôn update data qua setData, không truyền vào constructor
    public void setData(List<NotificationEntity> list) {
        notificationEntityList.clear();
        if (list != null) notificationEntityList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deadline, parent, false);
        return new DeadlineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeadlineViewHolder holder, int position) {
        NotificationEntity entity = notificationEntityList.get(position);
        holder.title.setText(entity.getTitle());
        holder.description.setText(entity.getMessage());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String timeFormatted = sdf.format(new Date(entity.getTimeMillis()));
        holder.time.setText(timeFormatted);

        PriorityEnum priorityEnum = PriorityEnum.fromValue(entity.getPriority());
        int iconRes = priorityEnum.getDrawableResId();
        holder.imageView.setImageResource(iconRes);
        holder.imageView.setTag(iconRes);


        int progressPercent = progressMap.getOrDefault(entity.getId(), 0);
        holder.progressBar.setMax(100);
        holder.progressBar.setProgress(progressPercent);

        StatusEnum statusEnum = StatusEnum.fromValue(entity.getStatus());
        holder.itemView.setBackgroundResource(statusEnum.getBackgroundResId());

        holder.itemView.setOnClickListener(v -> {
            if(onDeadlineOnClickListerner!=null){
                onDeadlineOnClickListerner.onItemClickListerner(position,entity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationEntityList.size();
    }

    public static class DeadlineViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, time;
        ImageView imageView;
        ProgressBar progressBar;

        public DeadlineViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_l_title);
            description = itemView.findViewById(R.id.txt_l_description);
            time = itemView.findViewById(R.id.txt_l_time);
            imageView = itemView.findViewById(R.id.txt_image);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
