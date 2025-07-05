package com.example.rxjava_java;

import android.app.Application;

import com.example.rxjava_java.data.repository.SingleUserRepositoryImpl;
import com.example.rxjava_java.data.repository.UserRepositoryImpl;
import com.example.rxjava_java.data.repository.UserUpdateRepositoryImpl;
import com.example.rxjava_java.data.source.local.dao.UserDao;
import com.example.rxjava_java.data.source.local.database.UserDB;

public class MyApplication extends Application {
    private SingleUserRepositoryImpl singleUserRepository;
    private UserUpdateRepositoryImpl userUpdateRepository;
    private UserRepositoryImpl userRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        UserDao userDao = UserDB.getInstance(getApplicationContext()).getUserDao();
        singleUserRepository = new SingleUserRepositoryImpl(userDao);
        userUpdateRepository = new UserUpdateRepositoryImpl(userDao);
        userRepository = new UserRepositoryImpl(userDao);
    }

    public SingleUserRepositoryImpl getSingleUserRepository() {
        return singleUserRepository;
    }

    public UserUpdateRepositoryImpl getUserUpdateRepository() {
        return userUpdateRepository;
    }

    public UserRepositoryImpl getUserRepository() {
        return userRepository;
    }
}
