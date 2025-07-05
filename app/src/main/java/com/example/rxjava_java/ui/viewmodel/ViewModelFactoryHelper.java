package com.example.rxjava_java.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.rxjava_java.MyApplication;


public class ViewModelFactoryHelper implements ViewModelProvider.Factory {
    private final MyApplication myApplication;

    public ViewModelFactoryHelper(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class))
            return (T) new UserViewModel(myApplication.getUserRepository());
        if (modelClass.isAssignableFrom(NavigationViewModel.class))
            return (T) new NavigationViewModel(myApplication.getSingleUserRepository());
        throw new IllegalArgumentException("Don't know modelClass");
    }
}
