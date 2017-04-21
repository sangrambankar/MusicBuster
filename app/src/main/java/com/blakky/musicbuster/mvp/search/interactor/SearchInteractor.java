package com.blakky.musicbuster.mvp.search.interactor;

import com.blakky.musicbuster.helpers.Constants;
import com.blakky.musicbuster.models.STrack;
import com.blakky.musicbuster.restclient.IRestClient;
import com.blakky.musicbuster.restclient.RestUtils;
import com.google.common.base.Strings;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by sangrambankar on 4/6/17.
 */
public class SearchInteractor {
    /***********************************************************************************************
     - SearchInteractor is the model that gets data(list of tracks) from the server(Soundcloud API).
     ***********************************************************************************************/

    private final ISearchInteractorFinishedListener mListener;
    private final IRestClient mRestClient;

    public SearchInteractor(ISearchInteractorFinishedListener listener) {
        this.mListener = listener;
        this.mRestClient = RestUtils.createRestClient();
    }

    public void loadTracks(final String keyword){
        mRestClient.getTracks(keyword, Constants.OFFSET, RestUtils.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    tracks -> {
                        //if successful, notifies to the presenter so that it can get a list of tracks.
                        filterItemsFromList(tracks);

                    },

                    Throwable -> {
                        //if fails, gets the error message from the server and then notifies to the presenter.
                        mListener.onNetworkFailure(Throwable);
                    }
            );
    }

    // Filter those tracks that don't have either an image or stream url valid.
    private void filterItemsFromList(final List<STrack> trackList){
        Observable.from(trackList)
            .filter(item -> !Strings.isNullOrEmpty(item.getArtwork_url()) && !Strings.isNullOrEmpty(item.getStream_url()))
            .toList().subscribe(
                items -> {
                    mListener.onNetworkSuccess(items);
                }
             );
    }

}
