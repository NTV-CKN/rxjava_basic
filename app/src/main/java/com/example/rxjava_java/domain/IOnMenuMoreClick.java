package com.example.rxjava_java.domain;

import android.view.MenuItem;

import com.example.rxjava_java.data.source.local.model.User;

public interface IOnMenuMoreClick {
        boolean menuClick(User user, MenuItem menuItem);
}
