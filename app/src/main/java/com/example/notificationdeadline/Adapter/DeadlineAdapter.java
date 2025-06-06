package com.example.notificationdeadline.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.dto.Enum.PriorityEnum;
import com.example.notificationdeadline.dto.Enum.StatusEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DeadlineAdapter extends RecyclerView.Adapter<DeadlineAdapter.DeadlineViewHoder> {
    List<NotificationEntity> notificationEntityList = new ArrayList<>();

    public DeadlineAdapter() {
    }
    public void setData(List<NotificationEntity> list) {
        this.notificationEntityList = list;
        notifyDataSetChanged();
    }


    public DeadlineAdapter(List<NotificationEntity> notificationEntityList) {
        this.notificationEntityList = notificationEntityList;
    }

    @NonNull
    @Override
    public DeadlineViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deadline,parent,false);
        return  new DeadlineViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeadlineViewHoder holder, int position) {
        NotificationEntity entity = notificationEntityList.get(position);
        holder.title.setText(entity.title);
        holder.description.setText(entity.message);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String timeFormatted = sdf.format(new Date(entity.timeMillis));
        holder.time.setText(timeFormatted);

        PriorityEnum priorityEnum = PriorityEnum.fromValue(entity.priority);
        int iconRes = priorityEnum.getDrawableResId();
        holder.imageView.setImageResource(iconRes);
        holder.imageView.setTag(iconRes);
        StatusEnum statusEnum = StatusEnum.fromValue(entity.status);
        holder.itemView.setBackgroundResource(statusEnum.getBackgroundResId());


    }

    @Override
    public int getItemCount() {
        return notificationEntityList.size();
    }

    public static  class DeadlineViewHoder extends  RecyclerView.ViewHolder{
        TextView title,description,time;
        ImageView imageView;

        public DeadlineViewHoder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_l_title);
            description = itemView.findViewById(R.id.txt_l_description);
            time = itemView.findViewById(R.id.txt_l_time);
            imageView = itemView.findViewById(R.id.txt_image);

        }
    }
}
