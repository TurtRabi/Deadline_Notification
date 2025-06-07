package com.example.notificationdeadline.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationdeadline.R;

import java.util.List;

public class FilterButtonAdapter extends RecyclerView.Adapter<FilterButtonAdapter.FilterViewHolder> {

    private List<String> filterOptions;
    private int selectedPosition = 0;
    private OnFilterClickListener listener;

    public interface OnFilterClickListener {
        void onFilterClick(String filter);
    }

    public FilterButtonAdapter(List<String> filterOptions, OnFilterClickListener listener) {
        this.filterOptions = filterOptions;
        this.listener = listener;
        if (!filterOptions.isEmpty()) {
            selectedPosition = 0;
        }
    }

    public void setSelectedFilter(String filter) {
        int newPosition = filterOptions.indexOf(filter);
        if (newPosition != -1 && newPosition != selectedPosition) {
            int oldPosition = selectedPosition;
            selectedPosition = newPosition;
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
        }
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter_button, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        String filter = filterOptions.get(position);
        holder.button.setText(filter);

        // Highlight nút được chọn
        if (position == selectedPosition) {
            holder.button.setBackgroundColor(Color.GREEN);
            holder.button.setTextColor(Color.WHITE);
        } else {
            holder.button.setBackgroundColor(Color.LTGRAY);
            holder.button.setTextColor(Color.BLACK);
        }

        holder.button.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            if (selectedPosition != adapterPosition) {
                int oldPosition = selectedPosition;
                selectedPosition = adapterPosition;
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
                listener.onFilterClick(filterOptions.get(adapterPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterOptions.size();
    }

    public static class FilterViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.btn_filter);
        }
    }
}
