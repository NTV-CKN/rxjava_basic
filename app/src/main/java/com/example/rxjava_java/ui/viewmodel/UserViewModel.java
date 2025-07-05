package com.example.rxjava_java.ui.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rxjava_java.data.repository.UserRepositoryImpl;
import com.example.rxjava_java.data.source.local.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class UserViewModel extends ViewModel {
    private final UserRepositoryImpl userRepository;
    private final MutableLiveData<List<User>> _users = new MutableLiveData<>();
    public LiveData<List<User>> users = _users;

    public UserViewModel(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    //Handle UserViewModel
    public void postUsers(List<User> users) {
        _users.postValue(users);
    }

    //Handle user dao
    public Flowable<List<User>> getAllUsers() {
        return userRepository.getAllUser();
    }

    public Completable addUser(User user) {
        return userRepository.insertUser(user);
    }

    public Completable deleteUser(User user) {
        return userRepository.deleteUser(user);
    }

    public Completable updateUser(User user) {
        return userRepository.updateUser(user);
    }
    public Single<User> findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }
    public Observable<List<User>> findUserByFullName(String fullName) {
        return userRepository.findUserByFullName(fullName);
    }
}
