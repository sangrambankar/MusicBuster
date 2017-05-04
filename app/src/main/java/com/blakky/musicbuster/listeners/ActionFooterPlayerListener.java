package com.blakky.musicbuster.listeners;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.blakky.musicbuster.activities.PlayerActivity;
import com.blakky.musicbuster.services.MediaPlayerService;
import com.blakky.musicbuster.views.FooterPlayerView;
import com.google.common.base.Preconditions;

import java.lang.ref.WeakReference;

import static com.blakky.musicbuster.helpers.Constants.UPDATE_FOOTER;

/**
 * Handles media player operations (Start and Pause) on the FooterPlayerView.
 */
public class ActionFooterPlayerListener implements FooterPlayerView.IFooterPlayerAction {

    private MediaPlayerService mService;
    private WeakReference<Activity> mActivity;

    public ActionFooterPlayerListener(@NonNull final MediaPlayerService service, final Activity mActivity){
        this.mService = Preconditions.checkNotNull(service, "Service cannot be null");
        this.mActivity = new WeakReference<>(mActivity);
    }

    @Override
    public void onStart() {
        mService.start();
    }

    @Override
    public void onPause() {
        mService.pause();
    }

    @Override
    public void onClick() {
        if(mActivity.get() != null){
           mActivity.get().startActivityForResult(new Intent(mActivity.get(), PlayerActivity.class), UPDATE_FOOTER);
        }
    }
}
