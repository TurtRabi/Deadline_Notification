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
import android.widget.Toast;

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

import java.io.File;
import java.util.List;

public class SettingFragment extends Fragment {

    private SettingViewModel mViewModel;
    private FragmentSettingBinding binding;
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
                        AppDatabase db = Room.databaseBuilder(
                                requireContext(),
                                AppDatabase.class,
                                "notification_db"
                        ).build();

                        new Thread(() -> {
                            db.clearAllTables();
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(requireContext(), "Đã xoá toàn bộ dữ liệu", Toast.LENGTH_SHORT).show();
                                CustomMessageDialog dialog = CustomMessageDialog.newInstance(
                                        "Thành công",
                                        "Bạn đã xóa thành công!",
                                        R.drawable.ic_launcher_foreground,
                                        R.color.successColor
                                );
                                dialog.show(getParentFragmentManager(), "successDialog");

                                new android.os.Handler().postDelayed(() -> {
                                    requireActivity().finishAffinity();
                                }, 2000);
                            });
                        }).start();
                    }
                };

                binding.cardUserInfo.setOnClickListener(listener);
                binding.switchNotification.setOnClickListener(listener);
                binding.switchDarkMode.setOnClickListener(listener);
                binding.btnClearCache.setOnClickListener(listener);
            }
        });
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