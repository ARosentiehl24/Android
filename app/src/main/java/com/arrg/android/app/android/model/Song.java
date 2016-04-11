package com.arrg.android.app.android.model;

import android.graphics.Bitmap;

public class Song {

    private Boolean isPlaying;
    private Bitmap photoAlbum;
    private String artistName;
    private String nameOfTheSong;
    private String pathOfFile;

    public Song(String pathOfFile) {
        this.pathOfFile = pathOfFile;
        this.isPlaying = false;
    }

    public Song() {
        this.isPlaying = false;
    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
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

    public String getPathOfFile() {
        return pathOfFile;
    }

    public void setPathOfFile(String pathOfFile) {
        this.pathOfFile = pathOfFile;
    }
}
