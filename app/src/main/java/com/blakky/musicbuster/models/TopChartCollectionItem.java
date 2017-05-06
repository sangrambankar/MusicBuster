package com.blakky.musicbuster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sangrambankar on 5/5/17.
 */

public class TopChartCollectionItem {
    @SerializedName("track")
    private STrack track;
    @SerializedName("score")
    private String score;

    public STrack getTrack() {
        return track;
    }

    public void setTrack(STrack track) {
        this.track = track;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
