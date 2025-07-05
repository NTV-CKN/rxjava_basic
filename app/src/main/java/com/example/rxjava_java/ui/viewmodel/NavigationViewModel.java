package com.example.rxjava_java.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rxjava_java.data.repository.SingleUserRepositoryImpl;
import com.example.rxjava_java.data.source.local.model.User;
import com.example.rxjava_java.utils.TypeNavigation;

import io.reactivex.rxjava3.core.Single;

public class NavigationViewModel extends ViewModel {
    private final SingleUserRepositoryImpl singleUserRepository;
    private User user;

    private final MutableLiveData<TypeNavigation> _typeNav = new MutableLiveData<>();
    public LiveData<TypeNavigation> typeNav = _typeNav;

    public NavigationViewModel(SingleUserRepositoryImpl singleUserRepository) {
        this.singleUserRepository = singleUserRepository;
    }

    public Single<User> findUserById(long id) {
        return singleUserRepository.findUserById(id);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void postNav(TypeNavigation typeNavigation) {
        _typeNav.postValue(typeNavigation);
    }
}
