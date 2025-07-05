package com.example.notificationdeadline.ui.setting;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.media.RingtoneManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.Manifest;

import com.bumptech.glide.Glide;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.AppDatabase;
import com.example.notificationdeadline.data.entity.SettingEntity;
import com.example.notificationdeadline.data.entity.UserEntity;
import com.example.notificationdeadline.databinding.FragmentSettingBinding;
import com.example.notificationdeadline.dto.request.SettingRequest;
import com.example.notificationdeadline.dto.request.UserRequest;
import com.example.notificationdeadline.ui.DashBoard.DashBoardFragment;
import com.example.notificationdeadline.ui.EditUser.EditUserFragment;
import com.example.notificationdeadline.ui.dialog.CustomMessageDialog;
import com.example.notificationdeadline.util.BackupRestoreManager;

import java.io.File;
import java.util.List;

public class SettingFragment extends Fragment {

    private SettingViewModel mViewModel;
    private FragmentSettingBinding binding;
    private BackupRestoreManager backupRestoreManager;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;
    private ActivityResultLauncher<Intent> soundPickerLauncher;
    private Button btnSelectSoundSetting;
    private TextView tvSelectedSoundUriSetting;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentSettingBinding.inflate(inflater,container,false);
        Toolbar toolbar = binding.settingToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        //((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setHasOptionsMenu(true);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        backupRestoreManager = new BackupRestoreManager(requireContext());

        btnSelectSoundSetting = binding.btnSelectSoundSetting;
        tvSelectedSoundUriSetting = binding.tvSelectedSoundUriSetting;

        // Initialize sound picker launcher
        soundPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    saveCustomSoundUri(uri.toString());
                    tvSelectedSoundUriSetting.setText("Âm thanh đã chọn: " + uri.getLastPathSegment());
                    tvSelectedSoundUriSetting.setVisibility(View.VISIBLE);
                } else {
                    saveCustomSoundUri(null); // Clear custom sound
                    tvSelectedSoundUriSetting.setVisibility(View.GONE);
                }
            }
        });

        // Load and display custom sound URI
        loadCustomSoundUri();

        // Observe user list
        mViewModel.getAllListUser().observe(getViewLifecycleOwner(), userEntityList -> {
            if (userEntityList != null && !userEntityList.isEmpty()) {
                UserEntity user = userEntityList.get(0);
                binding.txtUsername.setText(user.getUserName());
                binding.txtDescription.setText(user.getDescription());
                Glide.with(getContext())
                        .load(user.getImageUrl())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.priority_high_24px)
                        .into(binding.imageButton);
                binding.txtEmail.setText(user.getEmail());
                binding.txtPhone.setText(user.getPhone());
                binding.txtBirthday.setText(user.getBirthday());

                // Observe theme setting
                mViewModel.getSetting("theme").observe(getViewLifecycleOwner(), theme -> {
                    binding.switchDarkMode.setChecked(theme != null && "0".equals(theme.getValue()));
                });

                // Observe notification setting
                mViewModel.getSetting("notification").observe(getViewLifecycleOwner(), notification -> {
                    binding.switchNotification.setChecked(notification != null && "1".equals(notification.getValue()));
                });

                // Listeners
                View.OnClickListener listener = v -> {
                    if (v.getId() == R.id.card_user_info) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("user", new UserRequest(user.getUserId(), user.getUserName(),
                                user.getImageUrl(), user.getDescription(), user.getEmail(), user.getPhone(), user.getBirthday()));

                        NavController navController = Navigation.findNavController(requireView());
                        navController.navigate(R.id.action_to_editUser, bundle);
                    } else if (v.getId() == R.id.switchDarkMode) {
                        boolean isChecked = ((Switch) v).isChecked();

                        if(isChecked){
                            mViewModel.updateSetting("theme","0");

                        }else {
                            mViewModel.updateSetting("theme","1");
                        }

                        requireActivity().recreate();
                    } else if (v.getId() == R.id.switchNotification) {
                        boolean isChecked = ((Switch) v).isChecked();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (isChecked) {
                                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);

                                } else {
                                    enableNotifications();
                                }
                            } else {
                                Toast.makeText(requireContext(), "Bạn có thể tắt thông báo trong cài đặt ứng dụng", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                                mViewModel.updateSetting("notification","0");

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, requireContext().getApplicationInfo().uid);
                                }

                                startActivity(intent);

                                disableNotifications();
                            }
                        } else {
                            if (isChecked) {
                                enableNotifications();

                            } else {
                                disableNotifications();
                            }
                        }
                    } else if (v.getId() == R.id.btnClearCache) {
                        showDeleteTaskConfirmDialog();
                    } else if (v.getId() == R.id.btnBackupData) {
                        checkStoragePermissionAndBackup();
                    } else if (v.getId() == R.id.btnRestoreData) {
                        checkStoragePermissionAndRestore();
                    }
                };

                binding.cardUserInfo.setOnClickListener(listener);
                binding.switchNotification.setOnClickListener(listener);
                binding.switchDarkMode.setOnClickListener(listener);
                binding.btnClearCache.setOnClickListener(listener);
                binding.btnBackupData.setOnClickListener(listener);
                binding.btnRestoreData.setOnClickListener(listener);
                binding.btnSelectSoundSetting.setOnClickListener(v -> {
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Chọn âm thanh thông báo");
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, getCustomSoundUri());
                    soundPickerLauncher.launch(intent);
                });
            }
        });



    }

    private void saveCustomSoundUri(String uri) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("custom_notification_sound", uri);
        editor.apply();
    }

    private Uri getCustomSoundUri() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String uriString = preferences.getString("custom_notification_sound", null);
        if (uriString != null) {
            return Uri.parse(uriString);
        } else {
            return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
    }

    private void loadCustomSoundUri() {
        Uri uri = getCustomSoundUri();
        if (uri != null && !uri.equals(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))) {
            tvSelectedSoundUriSetting.setText("Âm thanh đã chọn: " + uri.getLastPathSegment());
            tvSelectedSoundUriSetting.setVisibility(View.VISIBLE);
        } else {
            tvSelectedSoundUriSetting.setVisibility(View.GONE);
        }
    }

    private void checkStoragePermissionAndBackup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                performBackup();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
            }
        } else {
            performBackup();
        }
    }

    private void checkStoragePermissionAndRestore() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                performRestore();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
            }
        } else {
            performRestore();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, retry the operation
                if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    performBackup();
                } else if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    performRestore();
                }
            } else {
                Toast.makeText(requireContext(), "Quyền truy cập bộ nhớ bị từ chối.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void performBackup() {
        backupRestoreManager.backupData(new BackupRestoreManager.BackupRestoreCallback() {
            @Override
            public void onSuccess() {
                requireActivity().runOnUiThread(() -> {
                    CustomMessageDialog.newInstance("Thành công", "Sao lưu dữ liệu thành công!", R.drawable.finishdeadline, R.color.successColor).show(getParentFragmentManager(), "successDialog");
                });
            }

            @Override
            public void onFailure(String message) {
                requireActivity().runOnUiThread(() -> {
                    CustomMessageDialog.newInstance("Lỗi", "Sao lưu dữ liệu thất bại: " + message, R.drawable.delete_24px, R.color.errorColor).show(getParentFragmentManager(), "errorDialog");
                });
            }
        });
    }

    private void performRestore() {
        backupRestoreManager.restoreData(new BackupRestoreManager.BackupRestoreCallback() {
            @Override
            public void onSuccess() {
                requireActivity().runOnUiThread(() -> {
                    CustomMessageDialog.newInstance("Thành công", "Khôi phục dữ liệu thành công!", R.drawable.finishdeadline, R.color.successColor).show(getParentFragmentManager(), "successDialog");
                    // Optionally, restart the app to reflect changes
                    new android.os.Handler().postDelayed(() -> {
                        requireActivity().recreate();
                    }, 1000);
                });
            }

            @Override
            public void onFailure(String message) {
                requireActivity().runOnUiThread(() -> {
                    CustomMessageDialog.newInstance("Lỗi", "Khôi phục dữ liệu thất bại: " + message, R.drawable.delete_24px, R.color.errorColor).show(getParentFragmentManager(), "errorDialog");
                });
            }
        });
    }

    private void showDeleteTaskConfirmDialog() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.bg_dialog_delete_task, null);

        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        dialogTitle.setText("Bạn chắc chắn muốn xoá toàn bộ bộ nhớ chứ?");
        Button btnCancel = view.findViewById(R.id.btn_cancel1);
        Button btnDelete = view.findViewById(R.id.btn_delete);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            AppDatabase db = Room.databaseBuilder(
                    requireContext(),
                    AppDatabase.class,
                    "app_db"
            ).build();

            new Thread(() -> {
                db.clearAllTables();
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Đã xoá toàn bộ dữ liệu", Toast.LENGTH_SHORT).show();
                    CustomMessageDialog dialog1 = CustomMessageDialog.newInstance(
                            "Thành công",
                            "Bạn đã xóa thành công!",
                            R.drawable.delete_removebg_preview,
                            R.color.successColor
                    );
                    dialog1.show(getParentFragmentManager(), "successDialog");

                    new android.os.Handler().postDelayed(() -> {
                        requireActivity().finishAffinity();
                    }, 2000);
                });
            }).start();

            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }





    private void enableNotifications() {
        Toast.makeText(getContext(), "Thông báo đã được bật", Toast.LENGTH_SHORT).show();
    }

    private void disableNotifications() {
        Toast.makeText(getContext(), "Thông báo đã được tắt", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.setting_menu_toolbar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_share){
            Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
            return true;
        } else if(id == android.R.id.home){
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.dashBoardFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    mViewModel.updateSetting("notification", "1");
                    enableNotifications();
                } else {
                    if (isNotificationPermissionPermanentlyDenied()) {
                        Toast.makeText(requireContext(), "Bạn đã từ chối vĩnh viễn quyền thông báo.\nHãy bật lại trong phần Cài đặt ứng dụng.", Toast.LENGTH_LONG).show();

                        // Mở màn hình Cài đặt ứng dụng
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
                        Toast.makeText(requireContext(), "Từ chối quyền thông báo", Toast.LENGTH_SHORT).show();
                    }
                }
            });



    private boolean isNotificationPermissionPermanentlyDenied() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS);
    }

}