package com.blakky.musicbuster.mvp.top.interactor;

import com.blakky.musicbuster.models.TopChartCollection;

/**
 * Created by sangrambankar on 4/6/17.
 */
public interface ITopInteractorFinishedListener {
    /**
     *
     * @param charts list of songs.
     */
    void onNetworkSuccess(final TopChartCollection charts);

    /**
     *
     * @param message An error from the server.
     */
    void onNetworkFailure(final Throwable message);
}
