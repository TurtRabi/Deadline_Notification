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
import com.example.notificationdeadline.data.entity.NotificationEntity;
import com.example.notificationdeadline.data.entity.NotificationHistoryEntity;
import com.example.notificationdeadline.dto.Enum.NotificationHistoryEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationHistoryAdapter extends RecyclerView.Adapter<NotificationHistoryAdapter.NotificationHistoryViewHoder> {
    List<NotificationHistoryEntity> notificationHistoryEntityList = new ArrayList<>();
    private Context context;
    private OnNotificationHistoryItemOnclickListerner listerner;
    public NotificationHistoryAdapter(Context context,List<NotificationHistoryEntity> notificationHistoryEntityList,OnNotificationHistoryItemOnclickListerner listerner) {
        this.context =context;
        this.notificationHistoryEntityList = notificationHistoryEntityList;
        this.listerner = listerner;
    }

    public interface OnNotificationHistoryItemOnclickListerner{
        void onItemClick(NotificationHistoryEntity notification,int position,View view);
    }

    public NotificationHistoryAdapter() {
    }

    public  void setData(List<NotificationHistoryEntity>list){
        this.notificationHistoryEntityList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationHistoryViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_history,parent,false);
        return new NotificationHistoryViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHistoryViewHoder holder, int position) {
        NotificationHistoryEntity entity = notificationHistoryEntityList.get(position);
        holder.title.setText(entity.title);
        holder.description.setText(entity.message);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String timeFormatted = sdf.format(new Date(entity.sentTimeMillis));
        holder.time.setText(timeFormatted);

        if(entity.urlImage != null) {
            try {
                int pos = Integer.parseInt(entity.urlImage);

                // Tìm enum theo id
                NotificationHistoryEnum historyEnum = null;
                for (NotificationHistoryEnum item : NotificationHistoryEnum.values()) {
                    if (item.getId() == pos) {
                        historyEnum = item;
                        break;
                    }
                }

                if(historyEnum != null) {
                    holder.imageView.setImageResource(historyEnum.getIconResId());
                } else {

                    holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();

                holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }


        if (!entity.isRead) {
            holder.imageView1.setVisibility(View.VISIBLE);
            Animation blinkAnim = AnimationUtils.loadAnimation(context, R.anim.blink);
            holder.imageView1.startAnimation(blinkAnim);
        } else {
            holder.imageView1.clearAnimation();
            holder.imageView1.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#F0F0F0F0"));

        }

        holder.itemView.setOnClickListener(v -> {
            if(listerner!=null){
                listerner.onItemClick(entity,position,holder.itemView);
            }
        });


    }

    @Override
    public int getItemCount() {
        return notificationHistoryEntityList.size();
    }

    public static class NotificationHistoryViewHoder extends RecyclerView.ViewHolder{

        TextView title,description,time;
        ImageView imageView,imageView1;
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
