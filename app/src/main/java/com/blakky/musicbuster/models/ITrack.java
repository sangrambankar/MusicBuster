package com.blakky.musicbuster.models;

/**
 * Created by sangrambankar on 4/5/17.
 */

public interface ITrack {
    /**
     * Gets the Track's id that will be played.
     * @return The name of the track.
     */
    String getId();
    /**
     * Gets the Track's title that will be played.
     * @return The name of the track.
     */
    String getTitle();

    /**
     * Gets the Track's image that will be played.
     * @return Either a url or path.
     */
    String getImage();

    /**
     * @return Jpg format
     */
    String getJPG();

    /**
     * @return mp3 format
     */
    String getMP3();
}
