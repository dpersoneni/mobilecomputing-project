package com.example.accordo.database;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ImageContentDao {
    @Query("SELECT * FROM ImageContent WHERE pid== :pid")
    ImageContent getImageContent(String pid);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImageContent(ImageContent imageContent);

}
