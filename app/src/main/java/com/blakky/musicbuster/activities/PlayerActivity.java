package com.blakky.musicbuster.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.music.MusicCoverView;
import com.blakky.musicbuster.R;
import com.blakky.musicbuster.helpers.Constants;
import com.blakky.musicbuster.helpers.TimeHelper;
import com.blakky.musicbuster.models.DTrack;
import com.blakky.musicbuster.models.ITrack;
import com.blakky.musicbuster.models.STrack;
import com.blakky.musicbuster.views.ProgressView;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.egslava.blurredview.BlurredImageView;

import static com.blakky.musicbuster.helpers.Constants.UPDATE_FOOTER;

/**
 * Created by sangrambankar on 4/21/17.
 */

public class PlayerActivity extends AppCompatActivity{

    @BindView(R.id.player_cover)
    MusicCoverView mCoverView;

    @BindView(R.id.player_background)
    BlurredImageView mPlayerbackground;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.player_fab)
    FloatingActionButton mPlay;

    @BindView(R.id.player_repeat)
    ImageView mRepeatButton;

    @BindView(R.id.player_shuffle)
    ImageView mShuffleButton;

    @BindView(R.id.player_title)
    TextView mTrackTitle;

    @BindView(R.id.player_artist)
    TextView mArtistName;

    @BindView(R.id.player_time)
    TextView mSongCurrentDuration;

    @BindView(R.id.player_duration)
    TextView mSongTotalDuration;

    @BindView(R.id.player_progress)
    ProgressView mSongProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        mCoverView.setCallbacks(new MusicCoverView.Callbacks() {
            @Override
            public void onMorphEnd(MusicCoverView coverView) {
                animateMusicCover();
            }

            @Override
            public void onRotateEnd(MusicCoverView coverView) {
                animateMusicCover();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setCellValues();
        seekBarProgress();
        setUpToolbar();
    }
    @Override
    protected void onPause(){
        super.onPause();
        setResult(UPDATE_FOOTER);
        EventBus.getDefault().post(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack());
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @OnClick(R.id.player_repeat)
    public void onRepeat(){
        MusicBusterActivity.mServiceConnection.getService().onRepeatSong();
        if(MusicBusterActivity.mServiceConnection.getService().isRepeatSong()){
            mRepeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
            mShuffleButton.setImageResource(R.mipmap.ic_shuffle_grey600_48dp);
            //SnackbarHelper.showMessage(mRepeatButton, R.string.repeat_on);
        }else{
            mRepeatButton.setImageResource(R.mipmap.ic_repeat_grey600_48dp);
            //SnackbarHelper.showMessage(mRepeatButton, R.string.repeat_off);
        }
    }

    @OnClick(R.id.player_fab)
    public void onPlay(){
        if(MusicBusterActivity.mServiceConnection.getService().mState == Constants.State.PlAYING){
            MusicBusterActivity.mServiceConnection.getService().pause();
            mPlay.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_arrow_white_24dp));
            mCoverView.stop();
        }else{
            MusicBusterActivity.mServiceConnection.getService().start();
            mPlay.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_arrow_white_24dp));
            mCoverView.start();
            seekBarProgress();
        }
    }

    @OnClick(R.id.player_forward)
    public void onNext(){
        MusicBusterActivity.mServiceConnection.getService().nextSong();
        setCellValues();
        setUpToolbar();
    }


    @OnClick(R.id.player_rewind)
    public void onPrevious(){
        MusicBusterActivity.mServiceConnection.getService().backSong();
        setCellValues();
        setUpToolbar();
    }


    @OnClick(R.id.player_shuffle)
    public void onShuffle(){
        MusicBusterActivity.mServiceConnection.getService().onShuffleSong();
        if(MusicBusterActivity.mServiceConnection.getService().isShuffleSong()){
            mShuffleButton.setImageResource(R.mipmap.ic_shuffle_black_48dp);
            mRepeatButton.setImageResource(R.mipmap.ic_repeat_grey600_48dp);
            //SnackbarHelper.showMessage(mShuffleButton, R.string.shuffle_on);
        }else{
            mShuffleButton.setImageResource(R.mipmap.ic_shuffle_grey600_48dp);
            //SnackbarHelper.showMessage(mShuffleButton, R.string.shuffle_off);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusUpdateValues(ITrack currentSTrack){
        setCellValues();
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setCellValues(){
        setTrackImage(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack().getImage(),
                MusicBusterActivity.mServiceConnection.getService().getCurrentTrack());
        setTrackTitle(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack().getTitle());
        setIcons();
        setTrackArtist(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack());
        mCoverView.start();
    }
    private void setTrackImage(final String urlOrPath, final ITrack track){
        Preconditions.checkNotNull(mCoverView);
        if (!Strings.isNullOrEmpty(urlOrPath)) {
            if(track instanceof STrack){
                Picasso.with(getApplicationContext()).load(urlOrPath).fit().centerInside().placeholder(R.drawable.album_cover_daft_punk).into(mCoverView);
            }else if(track instanceof DTrack){
                mCoverView.setImageBitmap(BitmapFactory.decodeFile(urlOrPath));
            }
        } else {
            mCoverView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.album_cover_daft_punk));
        }
    }

    private void setTrackTitle(final String name){
        Preconditions.checkNotNull(mTrackTitle);
        mTrackTitle.setText(name);
        mTrackTitle.setSelected(true);
    }

    private void setTrackArtist(final ITrack track){
        Preconditions.checkNotNull(mArtistName);
        if(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack() instanceof STrack){
            mArtistName.setText(((STrack) MusicBusterActivity.mServiceConnection.getService().getCurrentTrack()).getGenre());
        }else{
            mArtistName.setText("");
        }
    }

    private void setIcons(){
        if( MusicBusterActivity.mServiceConnection.getService().mState == Constants.State.PlAYING){
            mPlay.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pause_white_24dp));
        }else{
            mPlay.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_arrow_white_24dp));
        }

        if(MusicBusterActivity.mServiceConnection.getService().isShuffleSong()){
            mShuffleButton.setImageResource(R.drawable.ic_shuffle_white_24dp);
        }else{
            mShuffleButton.setImageResource(R.mipmap.ic_shuffle_grey600_48dp);
        }

        if(MusicBusterActivity.mServiceConnection.getService().isRepeatSong()){
            mRepeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
        }else{
            mRepeatButton.setImageResource(R.mipmap.ic_repeat_grey600_48dp);
        }
    }


    private int getDuration(){
        int mDuraction = 0;
        if(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack() instanceof STrack){
            if(((STrack) MusicBusterActivity.mServiceConnection.getService().getCurrentTrack()).getDuration() != null){
                mDuraction = Integer.parseInt(((STrack) MusicBusterActivity.mServiceConnection.getService().getCurrentTrack()).getDuration());
            }
        }else{
            mDuraction = MusicBusterActivity.mServiceConnection.getService().mediaPlayer().getDuration();
        }
        return mDuraction;
    }

    private void seekBarProgress(){
        startPlayProgressUpdater();
        mSongProgressBar.setProgress(0);
        mSongProgressBar.setMax(getDuration());
        mSongTotalDuration.setText(new TimeHelper(getDuration()).toString());
    }

    private void updateTime() {
        mSongCurrentDuration.setText(new TimeHelper(MusicBusterActivity.mServiceConnection.getService().mediaPlayer().getCurrentPosition()).toString());
    }

    private final Handler handler = new Handler();
    private void startPlayProgressUpdater() {
        if(MusicBusterActivity.mServiceConnection.getService() != null ){
            mSongProgressBar.setProgress(MusicBusterActivity.mServiceConnection.getService().mediaPlayer().getCurrentPosition());
            updateTime();
            if (MusicBusterActivity.mServiceConnection.getService().mState == Constants.State.PlAYING) {
                Runnable notification = this::startPlayProgressUpdater;
                handler.postDelayed(notification,1000);
            }
        }
    }

    private void animateMusicCover() {
        if (MusicBusterActivity.mServiceConnection.getService().mState == Constants.State.PlAYING) {
            mCoverView.start();
        } else {
            mCoverView.stop();
        }
    }
}
