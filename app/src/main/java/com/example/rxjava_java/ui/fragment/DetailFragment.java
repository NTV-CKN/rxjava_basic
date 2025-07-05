package com.example.rxjava_java.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.rxjava_java.MyApplication;
import com.example.rxjava_java.R;
import com.example.rxjava_java.data.source.local.model.User;
import com.example.rxjava_java.databinding.FragmentDetailUserBinding;
import com.example.rxjava_java.ui.MainActivity;
import com.example.rxjava_java.ui.viewmodel.NavigationViewModel;
import com.example.rxjava_java.ui.viewmodel.ViewModelFactoryHelper;
import com.example.rxjava_java.utils.TypeNavigation;

import java.util.Objects;

public class DetailFragment extends Fragment {
    private FragmentDetailUserBinding binding;
    private NavigationViewModel navigationViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailUserBinding.inflate(inflater, container, false);
        if (requireActivity() instanceof MainActivity)
            Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setTitle("DETAIL");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupViewModel() {
        MyApplication myApplication = (MyApplication) requireActivity().getApplication();
        navigationViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactoryHelper(myApplication)).get(NavigationViewModel.class);

        //setup user
        navigationViewModel.typeNav.observe(getViewLifecycleOwner(), typeNavigation -> {
            if (typeNavigation == TypeNavigation.TO_DETAIL && navigationViewModel.getUser() != null) {
                User user = navigationViewModel.getUser();

                binding.tvEmailDetail.setText(user.getEmail());
                binding.tvAddressDetail.setText(user.getAddress());
                binding.tvFullNameDetail.setText(user.getFullName());
                Glide.with(binding.getRoot())
                        .load(user.getUrl())
                        .error(requireActivity().getDrawable(R.drawable.ic_not_sp_image))
                        .into(binding.imgAvatarDetail);
            }
        });
        }
}
