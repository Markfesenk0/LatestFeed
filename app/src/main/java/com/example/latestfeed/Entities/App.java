package com.example.latestfeed.Entities;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class App implements Serializable {
    private String title;
    private String artist;
    private String summary;
    private String price;
    private String imgUrl;

    public App() {
    }

    public App(String title, String artist, String summary, String price, String imgUrl) {
        this.title = title;
        this.artist = artist;
        this.summary = summary;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        App object = (App) obj;
        return (this.getArtist() == object.getArtist()) && (this.getTitle() == object.getTitle());
    }
}
