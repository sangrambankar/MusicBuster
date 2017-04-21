package com.blakky.musicbuster.mvp.search.view;

import com.blakky.musicbuster.models.STrack;

import java.util.List;


/**
 * Created sangrambankar on 4/6/17.
 */
public interface ISearchView {

    /**
     * Displays a ListView with a list of STracks
     * @param STracks list of songs/audios.
     */
    void onSearchLoadedSuccess(final List<STrack> STracks);

    /**
     * Shows an ImageView and two TextViews
     * @param message An error from the server.
     */
    void onSearchLoadedFailure(final Throwable message);
}
