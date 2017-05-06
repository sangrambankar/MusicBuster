package com.blakky.musicbuster.models;

import com.blakky.musicbuster.helpers.Constants;

/**
 * Created by sangrambankar on 4/5/17.
 */

/**
 * Download Track (DTrack) is the model that represents a track that has been downloaded.
 */

public class DTrack implements ITrack{
    private String id;
    private String title;
    private String image;
    private String url;

    public DTrack(String title, String image, String pathUrl){
        this.title = title;
        this.image = image;
        this.url = pathUrl;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String getJPG(){
        return "/"+getTitle() + Constants.JPG;
    }

    @Override
    public String getMP3(){
        return "/"+getTitle() + Constants.MP3;
    }

}
