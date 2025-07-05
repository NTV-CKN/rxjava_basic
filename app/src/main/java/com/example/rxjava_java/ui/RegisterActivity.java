package com.example.rxjava_java.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.rxjava_java.MyApplication;
import com.example.rxjava_java.R;
import com.example.rxjava_java.data.source.local.model.User;
import com.example.rxjava_java.databinding.ActivityRegisterBinding;
import com.example.rxjava_java.ui.viewmodel.UserViewModel;
import com.example.rxjava_java.ui.viewmodel.ViewModelFactoryHelper;
import com.example.rxjava_java.utils.SnackBarHelper;
import com.example.rxjava_java.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    private String uri;
    private ActivityRegisterBinding binding;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private UserViewModel viewModel;

    private final ActivityResultLauncher<Intent> launcherCamOrGall =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
                if (o.getResultCode() == RESULT_OK && o.getData() != null) {
                    uri = Objects.requireNonNull(o.getData().getData()).toString();
                    Glide.with(binding.getRoot())
                            .load(uri)
                            .error(R.drawable.ic_error)
                            .into(binding.includeRegister.imgAvatarInclude);
                } else {
                    SnackBarHelper.getSnackbarNegative(this, binding.getRoot(), "Error!").show();
                }
            });

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(
                this,
                new ViewModelFactoryHelper((MyApplication) getApplication())
        ).get(UserViewModel.class);
        binding.includeRegister.tvLabel.setText("REGISTER USER");
        setupEvents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void setupEvents() {
        binding.includeRegister.btnOkInclude.setOnClickListener(v -> btnOkClick());
        binding.includeRegister.btnCancelInclude.setOnClickListener(v -> onBackPressed());
        binding.includeRegister.imgBack.setOnClickListener(v -> onBackPressed());
        binding.includeRegister.imgCameraInclude.setOnClickListener(v -> openCamera());
        binding.includeRegister.imgLibraryInclude.setOnClickListener(v -> openGallery());
    }

    private void btnOkClick() {
        Utils.closeSoftKeyboard(this, binding.getRoot());
        String email = binding.includeRegister.edtEmailInclude.getText().toString();
        String fullName = binding.includeRegister.edtFullNameInclude.getText().toString();
        String address = binding.includeRegister.edtAddressInclude.getText().toString();

        if (email.isEmpty() || fullName.isEmpty() || address.isEmpty()) {
            SnackBarHelper.getSnackbarNegative(this, binding.getRoot(), "Must fill in all text box!").show();
        } else {
            User user = new User(0, fullName, email, address, uri);
            disposable.add(
                    viewModel.addUser(user)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    () -> SnackBarHelper.getSnackbarPositive(this, binding.getRoot(), "Successfully!").show(),
                                    throwable -> {
                                        if (throwable instanceof SQLiteConstraintException)
                                            SnackBarHelper.getSnackbarNegative(this, binding.getRoot(), "The email is already exists!").show();
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