package com.example.notificationdeadline.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.notificationdeadline.R;

public class CustomMessageDialog extends DialogFragment {

    private String title;
    private String message;
    private int iconResId;
    private int buttonColorResId;
    private Runnable onOkCallback;

    public static CustomMessageDialog newInstance(String title,
                                                  String message,
                                                  int iconResId,
                                                  int buttonColorResId) {
        CustomMessageDialog dialog = new CustomMessageDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putInt("iconResId", iconResId);
        args.putInt("buttonColorResId", buttonColorResId);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_message, null);

        if (getArguments() != null) {
            title           = getArguments().getString("title");
            message         = getArguments().getString("message");
            iconResId       = getArguments().getInt("iconResId");
            buttonColorResId= getArguments().getInt("buttonColorResId");
        }

        ImageView iconView   = view.findViewById(R.id.dialogIcon);
        TextView  titleView  = view.findViewById(R.id.dialogTitle);
        TextView  msgView    = view.findViewById(R.id.dialogMessage);

        iconView.setImageResource(iconResId);
        titleView.setText(title);
        msgView.setText(message);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(false)
                .create();

        // Thiết lập background trong suốt cho dialog
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        return dialog;
    }

}
