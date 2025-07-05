package com.example.rxjava_java.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rxjava_java.MyApplication;
import com.example.rxjava_java.databinding.FragmentDeleteUserBinding;
import com.example.rxjava_java.ui.viewmodel.NavigationViewModel;
import com.example.rxjava_java.ui.viewmodel.UserViewModel;
import com.example.rxjava_java.ui.viewmodel.ViewModelFactoryHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DeleteDialogFragment extends DialogFragment {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private UserViewModel userViewModel;
    private NavigationViewModel navigationViewModel;
    private FragmentDeleteUserBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDeleteUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFields();
        setupEvent();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void setupFields() {
        MyApplication myApplication = (MyApplication) requireActivity().getApplication();
        userViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactoryHelper(myApplication)).get(UserViewModel.class);
        navigationViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactoryHelper(myApplication)).get(NavigationViewModel.class);
    }

    private void setupEvent() {
        binding.btnOkDelete.setOnClickListener(v ->
                //                                            SnackBarHelper.getSnackbarPositive(requireContext(), binding.getRoot().getRootView(), "Successfully!").show();
                disposable.add(
                        userViewModel.deleteUser(navigationViewModel.getUser())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                        this::dismiss,
                                        throwable -> {
                               //       SnackBarHelper.getSnackbarNegative(requireContext(), binding.getRoot(), "Error!").show();
                                            this.dismiss();
                                        }
                                )
                )
        );

        binding.btnCancelDelete.setOnClickListener(v -> this.dismiss());
    }
}
