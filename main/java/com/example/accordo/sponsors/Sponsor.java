package com.example.accordo.sponsors;

public class Sponsor {
    private final String url;
    private final String text;
    private final String image;


    public Sponsor(String url, String text, String image) {
        this.url = url;
        this.text = text;
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Sponsor{" +
                "url='" + url + '\'' +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
