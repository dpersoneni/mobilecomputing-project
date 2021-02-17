package com.example.accordo.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class User {
    @PrimaryKey @NonNull
    private String uid;

    @ColumnInfo(name = "pversion")
    private String pversion;

    @ColumnInfo(name = "picture")
    private String picture;

    public User(@NonNull String uid, String pversion, String picture) {
        this.uid = uid;
        this.pversion = pversion;
        this.picture = picture;
    }

    public String getUid() {
        return uid;
    }


    public String getPversion() {
        return pversion;
    }

    public String getPicture() {
        return picture;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public void setPversion(String pversion) {
        this.pversion = pversion;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isUser(String uid) {
        return this.uid.equals(uid);
    }

    public boolean samePVersion(String pversion) {
        return this.pversion.equals(pversion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return uid.equals(user.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
