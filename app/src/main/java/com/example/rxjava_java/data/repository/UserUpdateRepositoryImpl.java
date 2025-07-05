package com.example.rxjava_java.data.repository;

import com.example.rxjava_java.data.source.local.dao.UserDao;
import com.example.rxjava_java.data.source.local.model.User;

import io.reactivex.rxjava3.core.Completable;

public class UserUpdateRepositoryImpl extends SingleUserRepositoryImpl implements IUserUpdate {
    public UserUpdateRepositoryImpl(UserDao userDao) {
        super(userDao);
    }

    @Override
    public Completable updateUser(User user) {
        return userDao.updateUser(user);
    }
}
