package com.example.rxjava_java.data.repository;

import com.example.rxjava_java.data.source.local.model.User;

import io.reactivex.rxjava3.core.Completable;

public interface IUserUpdate {
    Completable updateUser(User user);
}
