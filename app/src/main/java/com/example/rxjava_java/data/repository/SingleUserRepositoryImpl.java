package com.example.rxjava_java.data.repository;

import com.example.rxjava_java.data.source.local.dao.UserDao;
import com.example.rxjava_java.data.source.local.model.User;

import io.reactivex.rxjava3.core.Single;

public class SingleUserRepositoryImpl implements ISingleUserRepository {
    protected UserDao userDao;

    public SingleUserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Single<User> findUserById(long id) {
        return userDao.findUserById(id);
    }
}
