package com.blakky.musicbuster.mvp.top.presenter;

import android.support.annotation.NonNull;

import com.blakky.musicbuster.models.STrack;
import com.blakky.musicbuster.models.TopChartCollection;
import com.blakky.musicbuster.mvp.top.interactor.ITopInteractorFinishedListener;
import com.blakky.musicbuster.mvp.top.interactor.TopInteractor;
import com.blakky.musicbuster.mvp.top.view.ITopView;
import com.google.common.base.Preconditions;

import java.lang.ref.WeakReference;


/**
 * Created by sangrambankar on 4/6/17.
 */
public class TopPresenter implements ITopPresenter, ITopInteractorFinishedListener {
    /******************************************************************************************
      SearchPresenter retrieves data {@link STrack} from the model {@link TopInteractor } and
      notifies the view {@link com.blakky.musicbuster.fragments.StreamFragment } to display it.
     ******************************************************************************************/

    private final TopInteractor mInteractor;
    private final WeakReference<ITopView> mView;

    public TopPresenter(@NonNull final ITopView view){
       this.mView = new WeakReference<>(view);
       this.mInteractor = new TopInteractor(this);
    }

    @Override
    public void searchTopTrack(String genre) {
        mInteractor.loadTopTracks(genre);
    }

    @Override
    public void onNetworkSuccess(@NonNull final TopChartCollection chart) {
        Preconditions.checkNotNull(chart, "List of STracks cannot be null");
        if(mView.get() != null) mView.get().onSearchLoadedSuccess(chart);
    }

    @Override
    public void onNetworkFailure(final Throwable message) {
        if(mView.get() != null) mView.get().onSearchLoadedFailure(message);
    }
}
