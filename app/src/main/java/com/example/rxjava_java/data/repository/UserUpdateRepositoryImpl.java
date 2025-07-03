package com.example.rxjava_java.data.repository;

import com.example.rxjava_java.data.dao.UserDao;
import com.example.rxjava_java.data.model.User;

import io.reactivex.rxjava3.core.Single;

public class UserUpdateRepositoryImpl extends SingleUserRepositoryImpl implements IUserUpdate {
    public UserUpdateRepositoryImpl(UserDao userDao) {
        super(userDao);
    }

    @Override
    public Single<Integer> updateUser(User user) {
        return userDao.updateUser(user);
    }
}
