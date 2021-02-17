package com.example.accordo.database;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class ImageContent {
    @PrimaryKey
    @NonNull
    private String pid;

    @ColumnInfo(name = "content")
    private String  content;


    public ImageContent(@NonNull String pid, String content) {
        this.pid = pid;
        this.content = content;
    }

    @NonNull
    public String getPid() {
        return pid;
    }

    public void setPid(@NonNull String pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageContent)) return false;
        ImageContent that = (ImageContent) o;
        return pid.equals(that.pid) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, content);
    }
}
