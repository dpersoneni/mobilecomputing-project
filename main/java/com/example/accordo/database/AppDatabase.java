 package com.example.accordo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

 @Database(entities = {User.class, ImageContent.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ImageContentDao imageContentDao();
}
