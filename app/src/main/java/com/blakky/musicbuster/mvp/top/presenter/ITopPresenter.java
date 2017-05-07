package com.blakky.musicbuster.mvp.top.presenter;

/**
 * Created by sangrambankar on 4/6/17.
 */
public interface ITopPresenter {

    /** Calls the API to search for the top charts that the user has typed.
     *  Song or Artist that users put in the search view.
     */
    void searchTopTrack(String genre);
}
