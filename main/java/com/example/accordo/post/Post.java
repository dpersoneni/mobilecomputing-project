package com.example.accordo.post;

import android.graphics.Bitmap;

public abstract class Post {
    private String pid;
    private String uid;
    private String name;
    private String type;
    private Bitmap profilePicture;

//TODO attributo foto utente? O qua dentro o creare User

    public Post(String pid, String uid, String name, String type) {
        this.pid = pid;
        this.uid = uid;
        this.name = name;
        this.type = type;
    }


    public String getUid() {
        return uid;
    }


    public String getPid() {
        return pid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    @Override
    public String toString() {
        return "Post{" +
                "pid='" + pid + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }



}
