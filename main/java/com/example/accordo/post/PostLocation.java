package com.example.accordo.post;

public class PostLocation extends Post{

    private Double latitude;
    private Double longitude;

    public PostLocation(String pid, String uid, String name, String type, Double latitude, Double longitude) {
        super(pid, uid, name, type);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
