package com.example.clarni.myapplication;

import android.provider.MediaStore;



public class AlbumsModel {

    private String name;
    private String artist;
    private String url;
    private String image;

    public AlbumsModel(String name, String singer, String url, String image) {
        this.name = name;
        this.artist = singer;
        this.url = url;
        this.image = image;
    }

    public AlbumsModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String singer) {
        this.artist = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
