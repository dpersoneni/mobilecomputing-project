package com.example.accordo.post;

import android.graphics.Bitmap;

public class PostImage  extends  Post{


    private Bitmap content;

    public PostImage(String pid, String uid, String name, String type,  Bitmap content) {
        super(pid, uid, name, type);

        this.content = content;
    }

    public PostImage(String pid, String uid, String name, String type) {
        super(pid, uid, name, type);
    }

    public Bitmap getImageContent() {
        return content;
    }


    public void setContent(Bitmap content) {
        this.content = content;
    }

}
