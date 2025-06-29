package com.example.notificationdeadline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notificationdeadline.R;

public class PrioritySpinnerAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] priorities;
    private final int[] icons = {
            R.drawable.chill,      // Bình thường
            R.drawable.warm,     // Khẩn cấp
            R.drawable.super_warm      // Rất khẩn cấp
    };

    public PrioritySpinnerAdapter(Context context, String[] priorities) {
        super(context, R.layout.item_priority_spinner, priorities);
        this.context = context;
        this.priorities = priorities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, convertView, parent);
    }

    private View createCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_priority_spinner, parent, false);

        TextView text = convertView.findViewById(R.id.priority_text);
        ImageView icon = convertView.findViewById(R.id.priority_icon);

        text.setText(priorities[position]);
        if (position < icons.length) {
            icon.setImageResource(icons[position]);
        } else {
            icon.setImageResource(R.drawable.low_priority_24px); // fallback default
        }

        return convertView;
    }
}
