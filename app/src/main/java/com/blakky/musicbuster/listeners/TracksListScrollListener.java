package com.blakky.musicbuster.listeners;

import android.util.Log;

import com.blakky.musicbuster.adapters.TracksListAdapter;
import com.blakky.musicbuster.helpers.Constants;
import com.blakky.musicbuster.restclient.IRestClient;
import com.blakky.musicbuster.restclient.RestUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TracksListScrollListener implements IDetectScrollListener {
    /************************************************************************************************************************
     * TracksListScrollListener calls the API {@link IRestClient } to get more tracks {@link com.blakky.musicbuster.models.STrack }
     * and adds them to the list when the user scrolls down the listView {@link com.blakky.musicbuster.views.TracksListView }
     ***********************************************************************************************************************/

    private int mPreviousLimit;
    private int mNextLimit = -1;
    private String mKeyword;
    private final TracksListAdapter mTrackListAdapter;
    private final IRestClient mRestClient = RestUtils.createRestClient();

    public TracksListScrollListener(final String keyword, final TracksListAdapter adapter){
        this.mKeyword = keyword;
        this.mPreviousLimit = Constants.OFFSET;
        this.mTrackListAdapter = adapter;
    }

    @Override
    public void onUpScrolling() {/**Not used**/}

    @Override
    public void onDownScrolling(int getLastVisiblePosition, int totalItemCount) {
        if (totalItemCount - getLastVisiblePosition <= Constants.TRIGGER_FROM_END) {
            if (mPreviousLimit != mNextLimit) {
                mPreviousLimit += Constants.NUMBER_OF_ITEMS;
                loadMoreTracks(mPreviousLimit);
                mNextLimit = mPreviousLimit; // To avoid multiple calls.
            }
        }
    }

    private void loadMoreTracks(final int nextTracks){
        mRestClient.getTracks(mKeyword, nextTracks, RestUtils.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tracks -> {
                            mTrackListAdapter.addAll(tracks);
                            mTrackListAdapter.notifyDataSetChanged();
                            mNextLimit++; // To satisfy the condition above (mPreviousLimit != mNextLimit)
                        },

                        Throwable -> {
                            Log.d("ScrollListener", "Can't load more tracks!");
                        }
                );
    }


    public void setNewSearch(String keyword) {
        initialiseCounter();
        initialiseNewKeyword(keyword);
    }

    private void initialiseCounter(){
        this.mNextLimit = - 1;
        this.mPreviousLimit = 0;
    }

    private void initialiseNewKeyword(String mKeyword){
        this.mKeyword = mKeyword;
    }
}
