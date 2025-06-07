package com.example.notificationdeadline.ui.EditUser;

import static android.app.Activity.RESULT_OK;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import com.example.notificationdeadline.service.UserService;

public class EditUserFragment extends Fragment {

    private EditUserViewModel mViewModel;

    private FragmentEditUserBinding binding;
    private String selectImage=null;
    public static EditUserFragment newInstance() {
        return new EditUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(inflater,container,false);
        Toolbar toolbar = binding.updateToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            UserRequest user = args.getParcelable("user");
            if (user != null) {
                binding.editTextName.setText(user.getUserName());
                binding.editTextEmail.setText(user.getEmail());
                binding.editTextPhone.setText(user.getPhone());
                binding.editTextBirthday.setText(user.getBirdday());
                binding.editTextDescription.setText(user.getDescription());

                Glide.with(getContext())
                        .load(user.getImageUrl())
                        .placeholder(R.drawable.logo) // tùy
                        .error(R.drawable.priority_high_24px)          // tùy
                        .into(binding.imageButton);



                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.getId()==R.id.editIcon){

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent,100);


                        }else if(v.getId() ==R.id.btn_edit_Save){
                            TextView usernameView = binding.editTextName;
                            TextView emailView =binding.editTextEmail;
                            TextView phoneView = binding.editTextPhone;
                            TextView birthdayView = binding.editTextBirthday;
                            TextView descriptionView = binding.editTextDescription;

                            // Lấy nội dung từ các TextView
                            String username = usernameView.getText().toString();
                            String email = emailView.getText().toString().replace("Email: ", "");
                            String phone = phoneView.getText().toString().replace("Số điện thoại: ", "");
                            String birthday = birthdayView.getText().toString().replace("Ngày sinh: ", "");
                            String description = descriptionView.getText().toString().replace("Mô tả: ", "");
                            if(selectImage==null){
                                selectImage="android.resource://com.example.notificationdeadline/drawable/logo";
                            }

                            UserRequest request = new UserRequest(user.getId(),username,selectImage,description,email,phone,birthday);
                            mViewModel.UpdateUser(request);
                            NavController nav = Navigation.findNavController(requireView());
                            nav.navigateUp();
                        }
                    }
                };

                binding.editIcon.setOnClickListener(listener);
                binding.btnEditSave.setOnClickListener(listener);

            }
        }


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditUserViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK &&data!=null){
            if(requestCode==100){
                Uri imageUri = data.getData();
                selectImage=imageUri.toString();
                Glide.with(this).load(imageUri).into(binding.imageButton);
            }
        }
    }


    private void showSuccessDialogWithAutoDismiss() {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Thành công 🎉")
                .setMessage("Deadline đã được thêm thành công!")
                .setCancelable(false)
                .create();

        dialog.show();

        new Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        }, 2000);
    }
}