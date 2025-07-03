package com.example.rxjava_java.data.repository;

import com.example.rxjava_java.data.model.User;

import io.reactivex.rxjava3.core.Single;

public interface IUserUpdate {
    Single<Integer> updateUser(User user);
}
