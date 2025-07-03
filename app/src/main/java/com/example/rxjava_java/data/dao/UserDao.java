package com.example.rxjava_java.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rxjava_java.data.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {
    @Insert
    Completable insertUser(User user);

    @Delete
    Completable deleteUser(User user);

    @Update
    Single<Integer> updateUser(User user);

    @Query(
            "select * from users where id = :id"
    )
    Single<User> findUserById(long id);

    @Query(
            "select * from users"
    )
    Flowable<List<User>> getAllUser();

    @Query(
            "select * from users where full_name like '%' + :keyName + '%' "
    )
    Observable<List<User>> findUserByFullName(String keyName);
}
