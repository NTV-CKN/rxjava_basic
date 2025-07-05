package com.example.rxjava_java.domain.strategy_sort;

import com.example.rxjava_java.data.source.local.model.User;

import java.util.List;

public interface ISortStrategy {
    void sortListUser(List<User> users);
}
