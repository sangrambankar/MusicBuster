package com.blakky.musicbuster.mvp.top.interactor;

import android.util.Log;

import com.blakky.musicbuster.models.TopChartCollection;
import com.blakky.musicbuster.restclient.IRestClient;
import com.blakky.musicbuster.restclient.RestUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sangrambankar on 4/6/17.
 */
public class TopInteractor {
    /***********************************************************************************************
     - TopInteractor is the model that gets data(list of top tracks) from the server(Soundcloud API).
     ***********************************************************************************************/

    private final ITopInteractorFinishedListener mListener;
    private final IRestClient mRestClient;

    public TopInteractor(ITopInteractorFinishedListener listener) {
        this.mListener = listener;
        this.mRestClient = RestUtils.createRestClient();
    }

    public void loadTopTracks(String genre){
        mRestClient.getTopTracks(genre,RestUtils.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    topChartCollection -> {
                        //if successful, notifies to the presenter so that it can get a list of tracks.
                        filterItemsFromList(topChartCollection);
                        Log.d("chart",""+topChartCollection.getChartitems().get(0).getTrack().getTitle());


                    },

                    Throwable -> {
                        //if fails, gets the error message from the server and then notifies to the presenter.
                        mListener.onNetworkFailure(Throwable);
                    }
            );
    }

    // Filter those tracks that don't have either an image or stream url valid.
    private void filterItemsFromList(final TopChartCollection collectionSTrack){
        mListener.onNetworkSuccess(collectionSTrack);

    }

}
