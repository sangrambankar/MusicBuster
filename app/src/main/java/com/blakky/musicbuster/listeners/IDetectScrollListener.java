package com.blakky.musicbuster.listeners;


/**
 * Created by regulosarmiento on 17/06/16.
 */
public interface IDetectScrollListener {

    void onUpScrolling();

    /**
     * Loads more tracks and adds them to the adapter {@link }.
     * @param getLastVisiblePosition The last track visible.
     * @param totalItemCount the number of tracks available.
     */
    void onDownScrolling(final int getLastVisiblePosition, final int totalItemCount);
}
