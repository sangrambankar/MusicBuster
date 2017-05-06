package com.blakky.musicbuster.mvp.top.view;

import com.blakky.musicbuster.models.TopChartCollection;


/**
 * Created sangrambankar on 4/6/17.
 */
public interface ITopView {

    /**
     * Displays a ListView with a list of STracks Top 50
     * @param charts list of songs/audios.
     */
    void onSearchLoadedSuccess(final TopChartCollection charts);

    /**
     * Shows an ImageView and two TextViews
     * @param message An error from the server.
     */
    void onSearchLoadedFailure(final Throwable message);
}
