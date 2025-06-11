package com.example.notificationdeadline.Adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>   {

    private List<TaskEntity> listTask = new ArrayList<>();
    private  OnDeleteClickListerner onDeleteClickListerner;
    private OnCheckBoxClickListerner onCheckBoxClickListerner;


    public TaskAdapter(OnDeleteClickListerner onDeleteClickListerner, OnCheckBoxClickListerner onCheckBoxClickListerner) {
        this.onDeleteClickListerner = onDeleteClickListerner;
        this.onCheckBoxClickListerner = onCheckBoxClickListerner;
    }

    public  void  setData(List<TaskEntity> list){
        listTask.clear();
        if(list!=null){
            listTask.addAll(list);
        }
        notifyDataSetChanged();
    }

    public interface  OnCheckBoxClickListerner{
        void onCheckBokOnclickListerner(View view,TaskEntity taskEntity);
    }

    public interface  OnDeleteClickListerner{
        void onDeleteOnclickListerner(View view,TaskEntity taskEntity);
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false);
        return  new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskEntity taskEntity = listTask.get(position);
        holder.taskName.setText(taskEntity.getContent());

        if(taskEntity.isDone()){
            holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
            // Lấy TextView trong view và set gạch ngang
            TextView textView = holder.itemView.findViewById(R.id.text_task_name);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
            holder.checkBox.setFocusable(false);
        }else{
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            TextView textView = holder.itemView.findViewById(R.id.text_task_name);
            textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setChecked(false);
            holder.checkBox.setClickable(true);
            holder.checkBox.setFocusable(true);
        }

        holder.checkBox.setOnClickListener(v -> {
            if(onCheckBoxClickListerner!=null){
                onCheckBoxClickListerner.onCheckBokOnclickListerner(holder.itemView,taskEntity);
            }
        });

        holder.imageButton.setOnClickListener(v -> {
            if(onDeleteClickListerner!=null){
                onDeleteClickListerner.onDeleteOnclickListerner(holder.itemView,taskEntity);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView taskName;
        ImageButton imageButton;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_task_done);
            taskName = itemView.findViewById(R.id.text_task_name);
            imageButton = itemView.findViewById(R.id.button_delete_task);
        }
    }
}
