package com.blakky.musicbuster.helpers;

import com.blakky.musicbuster.restclient.RestUtils;

/**
 * Created by sangrambankar on 4/5/17.
 */

public class Constants {

    public static final int ANDROID_VERSION = 23;
    public static final int OFFSET = 0;
    public static final int NUMBER_OF_ITEMS = 20;
    public static final int TRIGGER_FROM_END = 10;
    public static final String CREDENTIALS = "?client_id=" + RestUtils.API_KEY;
    public static final String TRACK_POSITION = "position";
    public static final boolean FOOTER_PLAYER_VISIBLE = true;
    public static final String TRACK_TITLE = "trackTitle";
    public static final String TRACK_IMAGE = "trackImage";
    public static final String CAN_I_SAVE_IT = "canIShowIt";
    public static final String DIR_IMAGES = "/Mp3 Tracks/images";
    public static final String DIR_TRACKS = "/Mp3 Tracks/tracks";
    public static final String JPG = ".jpg";
    public static final String MP3 = ".mp3";
    public static final int REMOVE_TRACK = 1;
    public static final String IS_PLAYING = "isPlaying";
    public static final int UPDATE_FOOTER = 1;
    public static final String TYPE = "text/plain";
    public static final String URL_APP = "https://play.google.com/store/apps/details?id=";


    /**
     * Indicates the state of Media Player within the service {@link com.blakky.musicbuster.services.MediaPlayerService}
     */
    public enum State{
        PREPARING, PlAYING, STOPPED, PAUSED
    }

    public enum Folder{
        TRACKS, IMAGES
    }
}
