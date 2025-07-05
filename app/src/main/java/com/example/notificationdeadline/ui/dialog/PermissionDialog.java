package com.example.notificationdeadline.ui.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.notificationdeadline.R;

public class PermissionDialog extends DialogFragment {

    private static final String ARG_MESSAGE = "message";

    public static PermissionDialog newInstance(String message) {
        PermissionDialog fragment = new PermissionDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_permission, null);
        TextView messageView = view.findViewById(R.id.permission_message);
        Button settingsButton = view.findViewById(R.id.settings_button);

        if (getArguments() != null) {
            messageView.setText(getArguments().getString(ARG_MESSAGE));
        }

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
            dismiss();
        });

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();
    }
}