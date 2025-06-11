package com.example.notificationdeadline.ui.EditUser;

import static android.app.Activity.RESULT_OK;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.databinding.FragmentEditUserBinding;
import com.example.notificationdeadline.dto.request.UserRequest;

public class EditUserFragment extends Fragment {

    private EditUserViewModel mViewModel;
    private FragmentEditUserBinding binding;
    private String selectImage = null;
    private UserRequest user;

    public static EditUserFragment newInstance() {
        return new EditUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(EditUserViewModel.class);

        Toolbar toolbar = binding.updateToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        View rootView = binding.getRoot();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            View currentFocus = requireActivity().getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.requestRectangleOnScreen(new Rect(), true);
            }
        });

        // Nhận dữ liệu User
        Bundle args = getArguments();
        if (args != null) {
            user = args.getParcelable("user");
            if (user != null) {
                binding.editTextName.setText(user.getUserName());
                binding.editTextEmail.setText(user.getEmail());
                binding.editTextPhone.setText(user.getPhone());
                binding.editTextBirthday.setText(user.getBirthday());
                binding.editTextDescription.setText(user.getDescription());

                Glide.with(getContext())
                        .load(user.getImageUrl())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.priority_high_24px)
                        .into(binding.imageButton);

                selectImage = user.getImageUrl();
            }
        }

        binding.editIcon.setOnClickListener(v -> selectImage());
        binding.btnEditSave.setOnClickListener(v -> saveEditUser());

        return binding.getRoot();
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void saveEditUser() {
        String username = binding.editTextName.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String phone = binding.editTextPhone.getText().toString().trim();
        String birthday = binding.editTextBirthday.getText().toString().trim();
        String description = binding.editTextDescription.getText().toString().trim();

        // Validate cơ bản
        if (username.isEmpty()) {
            binding.editTextName.setError("Tên không được để trống");
            binding.editTextName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            binding.editTextEmail.setError("Email không được để trống");
            binding.editTextEmail.requestFocus();
            return;
        }
        if (selectImage == null) {
            selectImage = "android.resource://com.example.notificationdeadline/drawable/logo";
        }

        UserRequest request = new UserRequest(
                user != null ? user.getId() : 0, // lấy id từ user cũ nếu có
                username,
                selectImage,
                description,
                email,
                phone,
                birthday
        );
        mViewModel.updateUser(request);

        showSuccessDialogWithAutoDismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 100) {
                Uri imageUri = data.getData();
                selectImage = imageUri.toString();
                Glide.with(this).load(imageUri).into(binding.imageButton);
            }
        }
    }

    private void showSuccessDialogWithAutoDismiss() {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Thành công 🎉")
                .setMessage("Thông tin người dùng đã được cập nhật!")
                .setCancelable(false)
                .create();

        dialog.show();

        new Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                NavController nav = Navigation.findNavController(requireView());
                nav.navigateUp();
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavController navController = Navigation.findNavController(requireView());
            if (!navController.navigateUp()) {
                requireActivity().onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}
