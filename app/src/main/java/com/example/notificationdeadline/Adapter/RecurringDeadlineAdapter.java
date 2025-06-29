package com.example.notificationdeadline.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public RecurringDeadlineAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deadline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecurringDeadlineEntity recurringDeadline = recurringDeadlineList.get(position);
        holder.bind(recurringDeadline, listener);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.txt_l_title);
            descriptionTextView = itemView.findViewById(R.id.txt_l_description);
            timeTextView = itemView.findViewById(R.id.txt_l_time);
        }

        public void bind(final RecurringDeadlineEntity recurringDeadline, final OnItemClickListener listener) {
            titleTextView.setText(recurringDeadline.getTitle());
            descriptionTextView.setText(recurringDeadline.getDescription());
            timeTextView.setText("Hàng " + getRecurrenceTypeString(recurringDeadline.getRecurrenceType()));
            itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), recurringDeadline));
        }

        private String getRecurrenceTypeString(int recurrenceType) {
            switch (recurrenceType) {
                case 1:
                    return "Ngày";
                case 2:
                    return "Tuần";
                case 3:
                    return "Tháng";
                case 4:
                    return "Năm";
                default:
                    return "";
            }
        }
    }
}

