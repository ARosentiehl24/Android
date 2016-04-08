package com.arrg.android.app.android.model;

import android.graphics.Bitmap;

public class Song {

    private Bitmap photoAlbum;
    private String artistName;
    private String nameOfTheSong;
    private String pathOfFile;

    public Song(String pathOfFile) {
        this.pathOfFile = pathOfFile;
    }

    public Song() {

    }

    public String getPathOfFile() {
        return pathOfFile;
    }

    public void setPathOfFile(String pathOfFile) {
        this.pathOfFile = pathOfFile;
    }

    public Bitmap getPhotoAlbum() {
        return photoAlbum;
    }

    public void setPhotoAlbum(Bitmap photoAlbum) {
        this.photoAlbum = photoAlbum;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getNameOfTheSong() {
        return nameOfTheSong;
    }

    public void setNameOfTheSong(String nameOfTheSong) {
        this.nameOfTheSong = nameOfTheSong;
    }
}
