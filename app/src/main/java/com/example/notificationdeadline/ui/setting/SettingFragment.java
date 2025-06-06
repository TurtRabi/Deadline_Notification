package com.example.notificationdeadline.ui.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.notificationdeadline.R;
import com.example.notificationdeadline.data.entity.UserEntity;
import com.example.notificationdeadline.databinding.FragmentSettingBinding;
import com.example.notificationdeadline.dto.request.UserRequest;
import com.example.notificationdeadline.ui.DashBoard.DashBoardFragment;
import com.example.notificationdeadline.ui.EditUser.EditUserFragment;

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
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setHasOptionsMenu(true);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        List<UserEntity> userEntityList = mViewModel.getAllListUser();
        if (!userEntityList.isEmpty()) {
            UserEntity user = userEntityList.get(0);
            binding.txtUsername.setText(user.UserName);
            binding.txtDescription.setText(user.Description);
            Glide.with(getContext())
                    .load(Uri.parse(user.ImageUrl))
                    .into(binding.imageButton);
            binding.txtEmail.setText(user.Email);
            binding.txtPhone.setText(user.phone);
            binding.txtBirtday.setText(user.birdday);
            binding.cardUserInfo.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("user",new UserRequest(user.userId,user.UserName,
                        user.ImageUrl,user.Description,user.Email,user.phone,user.birdday));

                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.editUserFragment2);
//                EditUserFragment fragment = new EditUserFragment();
//                fragment.setArguments(bundle);
//
//                requireActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment_activity_main,fragment)
//                        .addToBackStack(null)
//                        .commit();
            });


        }


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

    public void expand(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        view.setScaleY(0f);
        view.animate()
                .alpha(1f)
                .scaleY(1f)
                .setDuration(300)
                .setListener(null);
    }

    public void collapse(View view) {
        view.animate()
                .alpha(0f)
                .scaleY(0f)
                .setDuration(300)
                .withEndAction(() -> view.setVisibility(View.GONE));
    }

}