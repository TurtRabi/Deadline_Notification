package com.example.notificationdeadline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationdeadline.R;

import java.util.List;

public class FilterButtonAdapter extends RecyclerView.Adapter<FilterButtonAdapter.FilterViewHolder> {

    private List<String> filterOptions;
    private int selectedPosition = 0;
    private final OnFilterClickListener listener;

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

    public void setFilters(List<String> filters) {
        this.filterOptions = filters;
        selectedPosition = 0;
        notifyDataSetChanged();
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

        Context context = holder.itemView.getContext();
        boolean isSelected = (position == selectedPosition);

        // Đổi màu chữ và hiện line dưới nếu được chọn
        holder.button.setTextColor(ContextCompat.getColor(context, isSelected ? R.color.tab_selected : R.color.tab_unselected));
        holder.lineSelected.setVisibility(isSelected ? View.VISIBLE : View.INVISIBLE);

        holder.button.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;
            if (selectedPosition != adapterPosition) {
                int oldPosition = selectedPosition;
                selectedPosition = adapterPosition;
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
                if (listener != null) listener.onFilterClick(filterOptions.get(adapterPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterOptions.size();
    }

    public static class FilterViewHolder extends RecyclerView.ViewHolder {
        Button button;
        View lineSelected;
        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.btn_filter);
            lineSelected = itemView.findViewById(R.id.line_selected);
        }
    }
}
