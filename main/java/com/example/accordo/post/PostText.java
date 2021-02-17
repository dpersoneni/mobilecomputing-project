package com.example.accordo.post;

public class PostText extends Post {

    private String content;

    public PostText(String pid, String uid, String name, String type, String content) {
        super(pid, uid, name, type);

        this.content = content;
    }

    public String getTextContent() {
        return content;
    }
}
