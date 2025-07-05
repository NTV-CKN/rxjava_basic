package com.example.rxjava_java.domain.strategy_sort;

import com.example.rxjava_java.data.source.local.model.User;
import com.example.rxjava_java.utils.Utils;

import java.util.List;

public class SortByNameAscImpl implements ISortStrategy {
    @Override
    public void sortListUser(List<User> users) {
        users.sort((o1, o2) -> Utils.splitGetName(o1.getFullName()).compareTo(Utils.splitGetName(o2.getFullName())));
    }
}
