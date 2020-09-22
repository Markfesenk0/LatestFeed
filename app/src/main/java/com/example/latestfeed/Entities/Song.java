package com.example.latestfeed.Entities;

import androidx.annotation.Nullable;

public class Song {

    private String title;
    private String artist;
    private String category;
    private String imgUrl;

    public Song() {
    }

    public Song(String title, String artist, String category, String imgUrl) {
        this.title = title;
        this.artist = artist;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        Song object = (Song) obj;
        return (this.getArtist() == object.getArtist()) && (this.getTitle() == object.getTitle());
    }
}
