package com.example.rxjava_java.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.rxjava_java.MyApplication;
import com.example.rxjava_java.R;
import com.example.rxjava_java.databinding.ActivityMainBinding;
import com.example.rxjava_java.ui.fragment.DeleteDialogFragment;
import com.example.rxjava_java.ui.fragment.DetailFragment;
import com.example.rxjava_java.ui.fragment.EditFragment;
import com.example.rxjava_java.ui.fragment.HomeFragment;
import com.example.rxjava_java.ui.viewmodel.NavigationViewModel;
import com.example.rxjava_java.ui.viewmodel.UserViewModel;
import com.example.rxjava_java.ui.viewmodel.ViewModelFactoryHelper;
import com.example.rxjava_java.utils.TypeNavigation;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavigationViewModel navigationViewModel;

    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawStatusAndNavBar(R.color.blue_700);
        setSupportActionBar(binding.toolbar2);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_white));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupEventToolbarClick();
        setupViewModels();
        setupEventNavigation();
    }

    @Override
    public void onBackPressed() {
        //TODO: caution
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (navigationViewModel.typeNav.getValue() == TypeNavigation.TO_HOME)
            super.onBackPressed();
        super.onBackPressed();
    }

    private void setupEventToolbarClick() {
        binding.toolbar2.setNavigationOnClickListener(v -> onBackPressed());
    }


    private void setupViewModels() {
        MyApplication myApplication = (MyApplication) getApplication();

        new ViewModelProvider(this, new ViewModelFactoryHelper(myApplication)).get(UserViewModel.class);
        navigationViewModel = new ViewModelProvider(this, new ViewModelFactoryHelper(myApplication)).get(NavigationViewModel.class);
    }

    private void setupEventNavigation() {
        navigationViewModel.typeNav.observe(this, typeNavigation -> {
            if (navigationViewModel.getUser() != null) {
                if (TypeNavigation.TO_DELETE == typeNavigation) {
                    new DeleteDialogFragment().show(getSupportFragmentManager(), null);
                } else
                    openFragment(typeNavigation);
            }
        });
    }

    private void openFragment(TypeNavigation typeNavigation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        switch (typeNavigation) {
            case TO_HOME: {
                Log.d("SVU", "TO_HOME");
                setTitle(getString(R.string.app_name));
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                fragment = new HomeFragment();
                break;
            }
            case TO_DETAIL: {
                Log.d("SVU", "TO_DETAIL");
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                setTitle("DETAIL");
                fragment = new DetailFragment();
                break;
            }
            case TO_UPDATE: {
                Log.d("SVU", "TO_UPDATE");
                setTitle("UPDATE");
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                fragment = new EditFragment();
                break;
            }
        }

        assert fragment != null;
        transaction
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainerView.getId(), fragment)
                .addToBackStack(null)
                .commit();
    }


    private void drawStatusAndNavBar(int color) {
        //if (Build.VERSION.SDK_INT >= 21 && bool) {//=>if the app support api under 21
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, color));
        window.setNavigationBarColor(ContextCompat.getColor(this, color));
        //  }
    }
}