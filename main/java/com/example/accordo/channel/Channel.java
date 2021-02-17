package com.example.accordo.channel;

import com.example.accordo.post.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Channel {
    private final String title;
    private final boolean mine;


    public Channel(String title, boolean mine) {
        this.title = title;
        this.mine = mine;
    }

    public boolean isMine() {
        return mine;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", mine=" + mine +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return mine == channel.mine &&
                Objects.equals(title, channel.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, mine);
    }
}
