package com.example.accordo.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE uid== :uid")
    User getUser(String uid);

    @Query("SELECT pversion FROM user WHERE uid== :uid")
    String getPictureVersion(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);


/*
    @Query("UPDATE user SET pversion=:pversion, picture=:picture  WHERE uid == :uid")
    int updateUser(String uid, String pversion, String picture);
*/
}
