package com.example.rxjava_java.data.source.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.rxjava_java.data.source.local.dao.UserDao;
import com.example.rxjava_java.data.source.local.model.User;

@Database(
        entities = {User.class},
        version = 1
)
public abstract class UserDB extends RoomDatabase {
    public abstract UserDao getUserDao();

    private static volatile UserDB instance;

    public static UserDB getInstance(Context context) {
        if (instance == null) {
            synchronized (UserDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context,
                            UserDB.class,
                            "user_db"
                    ).build();
                }
            }
        }
        return instance;
    }
}
