package com.example.latestfeed.Entities;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Song implements Serializable {

    private String title;
    private String artist;
    private String category;
    private String imgUrl;
    private String previewUrl;
    private String songUrl;

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

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
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
