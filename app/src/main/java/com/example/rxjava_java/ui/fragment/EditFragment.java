package com.example.rxjava_java.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.rxjava_java.MyApplication;
import com.example.rxjava_java.R;
import com.example.rxjava_java.data.source.local.model.User;
import com.example.rxjava_java.databinding.FragmentUpdateUserBinding;

import com.example.rxjava_java.ui.MainActivity;
import com.example.rxjava_java.ui.viewmodel.NavigationViewModel;
import com.example.rxjava_java.ui.viewmodel.UserViewModel;
import com.example.rxjava_java.ui.viewmodel.ViewModelFactoryHelper;
import com.example.rxjava_java.utils.SnackBarHelper;
import com.example.rxjava_java.utils.TypeNavigation;
import com.example.rxjava_java.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditFragment extends Fragment {
    private UserViewModel userViewModel;
    private NavigationViewModel navigationViewModel;
    private CompositeDisposable disposable;
    private FragmentUpdateUserBinding binding;
    private String uri;


    private final ActivityResultLauncher<Intent> launcherCamOrGall =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
                if (o.getResultCode() == RESULT_OK && o.getData() != null) {
                    uri = Objects.requireNonNull(o.getData().getData()).toString();
                    Glide.with(binding.getRoot())
                            .load(uri)
                            .error(R.drawable.ic_error)
                            .into(binding.includeUpdate.imgAvatarInclude);
                } else {
                    SnackBarHelper.getSnackbarNegative(requireContext(), binding.getRoot(), "Error!").show();
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (requireActivity() instanceof MainActivity)
            Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setTitle("UPDATE");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFields();
        setupEvents();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void setupEvents() {
        binding.includeUpdate.imgLibraryInclude.setOnClickListener(v -> openGallery());
        binding.includeUpdate.imgCameraInclude.setOnClickListener(v -> openCamera());
        binding.includeUpdate.btnCancelInclude.setOnClickListener(v -> requireActivity().onBackPressed());
        binding.includeUpdate.btnOkInclude.setOnClickListener(v -> setBtnOkClick());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupFields() {
        MyApplication myApplication = (MyApplication) requireActivity().getApplication();
        userViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactoryHelper(myApplication)).get(UserViewModel.class);
        navigationViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactoryHelper(myApplication)).get(NavigationViewModel.class);

        disposable = new CompositeDisposable();

        navigationViewModel.typeNav.observe(getViewLifecycleOwner(), typeNavigation -> {
            if (typeNavigation == TypeNavigation.TO_UPDATE && navigationViewModel.getUser() != null) {
                User user = navigationViewModel.getUser();
                binding.includeUpdate.edtAddressInclude.setText(user.getAddress());
                binding.includeUpdate.edtFullNameInclude.setText(user.getFullName());
                binding.includeUpdate.edtEmailInclude.setText(user.getEmail());

                binding.includeUpdate.tvLabel.setVisibility(View.INVISIBLE);
                binding.includeUpdate.imgBack.setVisibility(View.INVISIBLE);

                Glide.with(binding.getRoot())
                        .load(user.getUrl())
                        .error(requireActivity().getDrawable(R.drawable.ic_not_sp_image))
                        .into(binding.includeUpdate.imgAvatarInclude);
            }
        });
    }

    private void setBtnOkClick() {
        Utils.closeSoftKeyboard(requireContext(), binding.getRoot());
        String email = binding.includeUpdate.edtEmailInclude.getText().toString();
        String fullName = binding.includeUpdate.edtFullNameInclude.getText().toString();
        String address = binding.includeUpdate.edtAddressInclude.getText().toString();

        if (email.isEmpty() || fullName.isEmpty() || address.isEmpty()) {
            SnackBarHelper.getSnackbarNegative(requireContext(), binding.getRoot(), "Must fill in all text box!").show();
        } else {
            Log.d("SVU", email);
            User user = new User(navigationViewModel.getUser().getId(), fullName, email, address, uri);
            if (uri == null)
                user.setUrl(navigationViewModel.getUser().getUrl());
            disposable.add(
                    userViewModel.updateUser(user)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    () -> SnackBarHelper.getSnackbarPositive(requireContext(), binding.getRoot(), "Successfully!").show(),
                                    throwable -> {
                                        if (throwable instanceof SQLiteConstraintException)
                                            SnackBarHelper.getSnackbarNegative(requireContext(), binding.getRoot(), "The email is already exists!").show();
                                    })
            );
        }
    }

    private void openCamera() {
        ImagePicker.with(this)
                .cameraOnly()
                .crop(1, 1)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    launcherCamOrGall.launch(intent);
                    return null;
                });
    }

    private void openGallery() {
        ImagePicker.with(this)
                .galleryOnly()
                .crop(1, 1)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    launcherCamOrGall.launch(intent);
                    return null;
                });
    }
}

