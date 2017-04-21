package com.blakky.musicbuster.services;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MediaPlayerConnection implements ServiceConnection {
    /***********************************************************************************************
     * MediaPlayerConnection sets a communication between the main activity
     * {@link com.blakky.musicbuster.activities.MusicBusterActivity} and the service {@link MediaPlayerService}
     ************************************************************************************************/

    private MediaPlayerService mService;
    private boolean isBind;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MediaPlayerService.OnBinder mBinder = (MediaPlayerService.OnBinder) service;
        setService(mBinder.getService());
        setIsBind(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        setIsBind(false);
    }

    public MediaPlayerService getService() {
        return mService;
    }

    private void setService(MediaPlayerService mService) {
        this.mService = mService;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setIsBind(boolean isBind) {
        this.isBind = isBind;
    }
}
