package com.blakky.musicbuster.mvp.search.interactor;

import com.blakky.musicbuster.models.STrack;

import java.util.List;

/**
 * Created by sangrambankar on 4/6/17.
 */
public interface ISearchInteractorFinishedListener {
    /**
     *
     * @param STracks list of songs.
     */
    void onNetworkSuccess(final List<STrack> STracks);

    /**
     *
     * @param message An error from the server.
     */
    void onNetworkFailure(final Throwable message);
}
