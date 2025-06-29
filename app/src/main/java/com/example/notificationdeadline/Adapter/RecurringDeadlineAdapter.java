package com.example.notificationdeadline.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.RecurringDeadlineEntity;

import java.util.ArrayList;
import java.util.List;

public class RecurringDeadlineAdapter extends RecyclerView.Adapter<RecurringDeadlineAdapter.ViewHolder> {
    private List<RecurringDeadlineEntity> recurringDeadlineList = new ArrayList<>();

    private final OnItemClickListener listener;
    private final OnEditClickListener editListener;
    private final OnDeleteClickListener deleteListener;

    public RecurringDeadlineAdapter(OnItemClickListener listener, OnEditClickListener editListener, OnDeleteClickListener deleteListener,List<RecurringDeadlineEntity> recurringDeadlineList) {
        this.listener = listener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
        this.recurringDeadlineList = recurringDeadlineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recurring_deadline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecurringDeadlineEntity recurringDeadline = recurringDeadlineList.get(position);
        holder.bind(recurringDeadline, listener, editListener, deleteListener);
    }

    @Override
    public int getItemCount() {
        return recurringDeadlineList.size();
    }

    public void setData(List<RecurringDeadlineEntity> recurringDeadlines) {
        this.recurringDeadlineList = recurringDeadlines;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, RecurringDeadlineEntity entity);
    }

    public interface OnEditClickListener {
        void onEditClick(int position, RecurringDeadlineEntity entity);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position, RecurringDeadlineEntity entity);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView timeTextView;
        private final Button editButton;
        private final Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_recurring_deadline_title);
            descriptionTextView = itemView.findViewById(R.id.tv_recurring_deadline_description);
            timeTextView = itemView.findViewById(R.id.tv_recurring_deadline_time);
            editButton = itemView.findViewById(R.id.btn_edit_recurring_deadline);
            deleteButton = itemView.findViewById(R.id.btn_delete_recurring_deadline);
        }

        public void bind(final RecurringDeadlineEntity recurringDeadline, final OnItemClickListener listener, final OnEditClickListener editListener, final OnDeleteClickListener deleteListener) {
            titleTextView.setText(recurringDeadline.getTitle());
            descriptionTextView.setText(recurringDeadline.getDescription());
            timeTextView.setText("Time: " + recurringDeadline.getTime() + " - Recurrence: " + getRecurrenceTypeString(recurringDeadline.getRecurrenceType()));
            itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), recurringDeadline));
            editButton.setOnClickListener(v -> editListener.onEditClick(getAdapterPosition(), recurringDeadline));
            deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(getAdapterPosition(), recurringDeadline));
        }

        private String getRecurrenceTypeString(int recurrenceType) {
            switch (recurrenceType) {
                case 1:
                    return "Daily";
                case 2:
                    return "Weekly";
                case 3:
                    return "Monthly";
                case 4:
                    return "Yearly";
                default:
                    return "";
            }
        }
    }
}

