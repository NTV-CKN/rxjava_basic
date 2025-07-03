package com.example.rxjava_java.data.repository;

import com.example.rxjava_java.data.dao.UserDao;
import com.example.rxjava_java.data.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public class UserRepositoryImpl extends UserUpdateRepositoryImpl implements IUserRepository {
    public UserRepositoryImpl(UserDao userDao) {
        super(userDao);
    }

    @Override
    public Flowable<List<User>> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public Observable<List<User>> findUserByFullName(String keyName) {
        return userDao.findUserByFullName(keyName);
    }

    @Override
    public Completable deleteUser(User user) {
        return userDao.deleteUser(user);
    }

    @Override
    public Completable insertUser(User user) {
        return userDao.insertUser(user);
    }
}
