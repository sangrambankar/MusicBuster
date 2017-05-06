package com.blakky.musicbuster.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.blakky.musicbuster.helpers.Constants;
import com.blakky.musicbuster.helpers.Constants.State;
import com.blakky.musicbuster.models.BufferTrack;
import com.blakky.musicbuster.models.ITrack;
import com.blakky.musicbuster.mvp.search.interactor.IUrlTrackInteractorListener;
import com.blakky.musicbuster.mvp.search.interactor.SearchInteractor;
import com.google.common.base.Preconditions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;
import java.util.Random;
/**
 * Service that handles Media Player and performs operations like play, pause, release, etc.
 */
public class MediaPlayerService extends Service implements OnPreparedListener, OnCompletionListener, OnErrorListener, IUrlTrackInteractorListener {

    private List<ITrack> mTracks;
    private int mPosition = -1;
    private Thread mThread;
    private final IBinder mBinder = new OnBinder();
    private MediaPlayer mPlayer;
    public State mState = State.PREPARING;
    private Boolean isRepeat = false;
    private Boolean isShuffle = false;

    /**
     * Class that allows the main activity {@link com.blakky.musicbuster.activities.MusicBusterActivity}
     * to access this service {@link MediaPlayerService} and its public methods.
     */
    public class OnBinder extends Binder {
        MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    /**
     * Gets the whole list of tracks of a query when the user wants to play a track.
     * @param tracks List of tracks
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBusGetTracks(@Nullable final List<ITrack> tracks) {
      if(tracks != null){
          mTracks = tracks;
      }
    }

    /**
     * Gets the track's position that will be played when the user clicks on it.
     * @param intent The intent sent it from {@link com.blakky.musicbuster.listeners.OnItemClickListener}
     */
    private void setUpTrackToPlay(Intent intent){
     //   if(mPosition != Integer.parseInt(intent.getStringExtra(Constants.TRACK_POSITION))){
            mPosition = Integer.parseInt(intent.getStringExtra(Constants.TRACK_POSITION));
            playTrack(mPosition);
      //  }

        if(mPosition != -1){
            // if mPosition is different to -1 that means there's a track which is playing,
            // so shows the FooterPlayerView when the user clicks on a item or the device orientation changes.
            EventBus.getDefault().post(mTracks.get(mPosition));
        }
    }


    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        releaseMediaPlayer();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            setUpTrackToPlay(intent);
        }
        return START_NOT_STICKY; // Do not recreate the service if it gets killed.
    }

    @Override
    public IBinder onBind(Intent intent) { return mBinder; }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        EventBus.getDefault().post(mTracks.get(mPosition));
        mState = State.PlAYING;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (isRepeat) {
            playTrack(mPosition);
        }else if(isShuffle) {
            Random r = new Random();
            mPosition = r.nextInt(mTracks.size());
            playTrack(mPosition);
        }else{
            nextSong();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private void playTrack(final int position){
        SearchInteractor interactor = new SearchInteractor(this);
        interactor.getURLTrack(mTracks.get(position).getId());
    }

    @Override
    public void onNetworkSuccess(BufferTrack bufferTrack) {
        terminatePreviousThread();
        mThread = new Thread(){ // To make sure it's not doing too much work on the Main Thread.
            @Override
            public void run() {
                super.run();
                try {
                    createMediaPlayerIfNeeded();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayer.setDataSource(bufferTrack.getHttp_mp3_128_url());
                    mPlayer.prepareAsync(); // Prepares, but does not block the UI thread.
                    EventBus.getDefault().post(Constants.FOOTER_PLAYER_VISIBLE);
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }

    @Override
    public void onNetworkFailure(Throwable message) {

    }


    public ITrack getCurrentTrack(){
        if(mTracks == null){
            return null;
        }
        return mTracks.get(mPosition);
    }

    public boolean canISaveData(){
        return (mPlayer != null);
    }

    public void nextSong(){
        try{
            if(mTracks != null && !mTracks.isEmpty()){
                if(mPosition < mTracks.size()-1){
                    mPosition++;
                    playTrack(mPosition);
                }else{
                    mPosition = 0;
                    playTrack(mPosition);
                }
            }
        }catch (IndexOutOfBoundsException e){
            mPosition = 0;
            playTrack(mPosition);
        }
    }

    public void backSong(){
        if(mTracks != null && !mTracks.isEmpty()){
            if(mPosition > 0){
                mPosition--;
                playTrack(mPosition);
            }else{
                mPosition = mTracks.size() -1;
                playTrack(mPosition);
            }
        }
    }

    public void onRepeatSong(){
        if(isRepeat){
            isRepeat = false;
        }else{
            isRepeat  = true;
            isShuffle = false;
        }
    }

    public void onShuffleSong(){
        if(isShuffle){
            isShuffle = false;
        }else{
            isShuffle = true;
            isRepeat  = false;
        }
    }

    public int getCurrentPosition(){
        if(mTracks == null){
            return -1;
        }
        return mPosition;
    }

    /**
     * Stops the current thread that is running, in order to start a new one later on.
     */
    private void terminatePreviousThread(){
        if(mThread != null && mThread.isAlive()){
            mThread.interrupt();
        }
    }

    private void createMediaPlayerIfNeeded(){
        if(mPlayer == null){
            mPlayer = new MediaPlayer();
            mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
        }else {
            mPlayer.reset();
        }
    }

    public void pause(){
        if((mPlayer != null)){
            mPlayer.pause();
            mState = State.PAUSED;
        }
    }

    public void start(){
        if((mPlayer != null)){
            mPlayer.start();
            mState = State.PlAYING;
        }
    }

    private void releaseMediaPlayer(){
        if(mPlayer != null){
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

    public boolean isRepeatSong(){
        return isRepeat;
    }

    public boolean isShuffleSong(){
        return isShuffle;
    }

    public MediaPlayer mediaPlayer(){
        return Preconditions.checkNotNull(mPlayer, "MediaPlayer cannot be null");
    }
}

