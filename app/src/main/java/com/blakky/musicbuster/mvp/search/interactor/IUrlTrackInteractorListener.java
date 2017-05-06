package com.blakky.musicbuster.mvp.search.interactor;

import com.blakky.musicbuster.models.BufferTrack;

/**
 * Created by sangrambankar on 5/5/17.
 */

public interface IUrlTrackInteractorListener {

    /**
     *
     * @param bufferTrack url of song.
     */
    void onNetworkSuccess(final BufferTrack bufferTrack);

    /**
     *
     * @param message An error from the server.
     */
    void onNetworkFailure(final Throwable message);
}
