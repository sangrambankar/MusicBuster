package com.blakky.musicbuster.mvp.search.presenter;

/**
 * Created by sangrambankar on 4/6/17.
 */
public interface ISearchPresenter {

    /** Calls the API to search for the keyword that the user has typed.
     * @param keyword Song or Artist that users put in the search view.
     */
    void searchTrack(final String keyword);
}
