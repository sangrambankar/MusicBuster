package com.blakky.musicbuster.listeners;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import com.blakky.musicbuster.adapters.TracksListAdapter;
import com.blakky.musicbuster.helpers.Constants;
import com.blakky.musicbuster.helpers.ServiceHelper;
import com.blakky.musicbuster.helpers.SharedPreferenceHelper;
import com.blakky.musicbuster.models.ITrack;
import com.google.common.base.Preconditions;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;


public class OnItemClickListener implements AdapterView.OnItemClickListener{

    private WeakReference<Activity> mActivity;

    public OnItemClickListener(@NonNull final Activity activity){
        this.mActivity = Preconditions.checkNotNull(new WeakReference<>(activity), "Activity cannot be null");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sendTracksAndPositionToService(((TracksListAdapter)parent.getAdapter()).getTracks(), position);
        SharedPreferenceHelper.saveKeyValuePairBoolean(mActivity.get(), Constants.IS_PLAYING, true);
    }

    public void sendTracksAndPositionToService(final List<ITrack> tracks, final int position){
        ServiceHelper.startService(mActivity, position);
        EventBus.getDefault().post(tracks); // Send all my tracks to the service.
    }
}
