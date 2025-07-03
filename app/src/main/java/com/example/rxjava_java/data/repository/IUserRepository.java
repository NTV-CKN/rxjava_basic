package com.example.rxjava_java.data.repository;

import com.example.rxjava_java.data.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public interface IUserRepository {
    Flowable<List<User>> getAllUser();
    Observable<List<User>> findUserByFullName(String keyName);
    Completable deleteUser(User user);
    Completable insertUser(User user);
}
