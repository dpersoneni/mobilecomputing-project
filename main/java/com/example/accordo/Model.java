package com.example.accordo;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.accordo.channel.Channel;
import com.example.accordo.database.ImageContent;
import com.example.accordo.post.Post;
import com.example.accordo.database.User;
import com.example.accordo.post.PostImage;
import com.example.accordo.sponsors.Sponsor;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Model {
    private List<Channel> channels;
    private List<Post> posts;
    private List<User> users;
    private HashMap<String, String> userToCheck;
    private HashMap<String, String> imageToCheck;
    private Set<ImageContent> images;

    private List<Sponsor> sponsors;

    private static Model instance = new Model();

    private Model() {
        channels = new ArrayList<>();
        posts = new ArrayList<>();
        users = new ArrayList<>();
        userToCheck = new HashMap<>() ; //chiavi uid, pversion
        imageToCheck = new HashMap<>();   //pid, position
        images = new HashSet<>();

        sponsors = new ArrayList<>();

    }


    public static synchronized Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public void addChannel(Channel c) {
            channels.add(c);
    }

    public void addPostToModel(Post post) {
        posts.add(post);
        addUserPictureToPost(post);
    }


    private void addUserPictureToPost(Post p) {
        User user = getUser(p.getUid());
        if (user != null) {
            p.setProfilePicture(ImageController.fromBaseToBitmap(user.getPicture()));
        }
    }

    private void addUserPictureToPosts(User user) {
        Bitmap bitmap = ImageController.fromBaseToBitmap(user.getPicture());

        for (Post post : posts) {
            if (post.getUid().equals(user.getUid())) {
                post.setProfilePicture(bitmap);
            }
        }
    }


    public Channel getChannel(int position) {
        return channels.get(position);
    }

    public String getTitleChannel(int position) {
        return channels.get(position).getTitle();
    }

    public  int getChannelsSize() {
        return channels.size();
    }

    public Post getPost(int position) { return posts.get(position);}

    public int getPostsSize() {
        return posts.size();
    }





    public void printAllPosts() {
        for (int i = 0; i < posts.size(); i++ ) {
            Log.d("Danilo",  posts.get(i).toString());
        }
    }
    public void printAllChannels() {
        for (int i = 0; i < channels.size(); i++ ) {
            Log.d("Danilo",  channels.get(i).toString());
        }
    }

    public void printPost(Post post) {
        Log.d("DaniloPost", post.toString());
    }

    public void clearChannel() {
       posts.clear();
       userToCheck.clear();
       imageToCheck.clear();
    }


    public void clearChannels() {
        channels.clear();
    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getUser(String uid) {
        int p = getUserPosition(uid);
        for (User u : users) {
            if (p >=  0) {
                return users.get(p);
            }
        }
        return null;
    }

    public void setUser(User user) {
        int p = getUserPosition(user.getUid());
        if (p >= 0) {
            users.remove(p);
        }
        users.add(user);
        addUserPictureToPosts(user);
    }

    private int getUserPosition(String uid) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).isUser(uid)) {
                return i;
            }
        }
        return -1;
    }

    public void setImageContent(ImageContent imageContent) {
        images.add(imageContent);
    }

    public void setImageToPost(String image, int position) {
        Bitmap bitmap = ImageController.fromBaseToBitmap(image);
        ((PostImage )posts.get(position)).setContent(bitmap);

    }

    public ImageContent getImageContent(String pid) {
        for (ImageContent imageContent : images) {
            if (imageContent.getPid().equals(pid)) {
                return imageContent;
            }
        }
        return null;
    }


    public Sponsor getSponsor(int position) {
        return sponsors.get(position);
    }

    public int getSponsorSize() {
        return sponsors.size();
    }

    public void addSponsor(Sponsor s) {
        sponsors.add(s);
    }

    public void printAllSponsors() {
        for (int i = 0; i < sponsors.size(); i++ ) {
            Log.d("Danilo",  sponsors.get(i).toString());
        }
    }
}
