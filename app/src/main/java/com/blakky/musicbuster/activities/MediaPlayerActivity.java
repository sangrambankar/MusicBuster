package com.blakky.musicbuster.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blakky.musicbuster.R;
import com.blakky.musicbuster.helpers.Constants.State;
import com.blakky.musicbuster.helpers.TimeHelper;
import com.blakky.musicbuster.models.DTrack;
import com.blakky.musicbuster.models.ITrack;
import com.blakky.musicbuster.models.STrack;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.versionCode;
import static android.R.attr.versionName;
import static com.blakky.musicbuster.helpers.Constants.UPDATE_FOOTER;


public class MediaPlayerActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.track_image)
    ImageView mTrackImage;

    @BindView(R.id.button_play)
    ImageView mPlay;

    @BindView(R.id.button_repeat)
    ImageView mRepeatButton;

    @BindView(R.id.button_shuffle)
    ImageView mShuffleButton;

    @BindView(R.id.track_title)
    TextView mTrackTitle;

    @BindView(R.id.artist_name)
    TextView mArtistName;

    @BindView(R.id.songCurrentDuration)
    TextView mSongCurrentDuration;

    @BindView(R.id.songProgressBar)
    SeekBar mSongProgressBar;

    @BindView(R.id.songTotalDuration)
    TextView mSongTotalDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        ButterKnife.bind(this);
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
        setUpToolbar();
        seekBarProgress();
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

    @OnClick(R.id.button_repeat)
    public void onRepeat(){
        MusicBusterActivity.mServiceConnection.getService().onRepeatSong();
        if(MusicBusterActivity.mServiceConnection.getService().isRepeatSong()){
            mRepeatButton.setImageResource(R.mipmap.ic_repeat_black_48dp);
            mShuffleButton.setImageResource(R.mipmap.ic_shuffle_grey600_48dp);
            //SnackbarHelper.showMessage(mRepeatButton, R.string.repeat_on);
        }else{
            mRepeatButton.setImageResource(R.mipmap.ic_repeat_grey600_48dp);
            //SnackbarHelper.showMessage(mRepeatButton, R.string.repeat_off);
        }
    }


    @OnClick(R.id.button_previous)
    public void onPrevious(){
        MusicBusterActivity.mServiceConnection.getService().backSong();
        setCellValues();
    }

    @OnClick(R.id.button_play)
    public void onPlay(){
        if(MusicBusterActivity.mServiceConnection.getService().mState == State.PlAYING){
            MusicBusterActivity.mServiceConnection.getService().pause();
            mPlay.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_av_play_arrow));
        }else{
            MusicBusterActivity.mServiceConnection.getService().start();
            mPlay.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_av_pause));
        }
    }

    @OnClick(R.id.button_next)
    public void onNext(){
        MusicBusterActivity.mServiceConnection.getService().nextSong();
        setCellValues();
    }

    @OnClick(R.id.button_shuffle)
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

    private void setIcons(){
        if( MusicBusterActivity.mServiceConnection.getService().mState == State.PlAYING){
            mPlay.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_av_pause));
        }else{
            mPlay.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_av_play_arrow));
        }

        if(MusicBusterActivity.mServiceConnection.getService().isShuffleSong()){
            mShuffleButton.setImageResource(R.mipmap.ic_shuffle_black_48dp);
        }else{
            mShuffleButton.setImageResource(R.mipmap.ic_shuffle_grey600_48dp);
        }

        if(MusicBusterActivity.mServiceConnection.getService().isRepeatSong()){
            mRepeatButton.setImageResource(R.mipmap.ic_repeat_black_48dp);
        }else{
            mRepeatButton.setImageResource(R.mipmap.ic_repeat_grey600_48dp);
        }
    }

    private void setCellValues(){
        setTrackImage(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack().getImage(),
                MusicBusterActivity.mServiceConnection.getService().getCurrentTrack());
        setTrackTitle(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack().getTitle());
        setIcons();
        setTrackArtist(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack());
    }

    private void setTrackImage(final String urlOrPath, final ITrack track){
        Preconditions.checkNotNull(mTrackImage);
        if (!Strings.isNullOrEmpty(urlOrPath)) {
            if(track instanceof STrack){
                Picasso.with(getApplicationContext()).load(urlOrPath).placeholder(R.mipmap.ic_launcher).into(mTrackImage);
            }else if(track instanceof DTrack){
                mTrackImage.setImageBitmap(BitmapFactory.decodeFile(urlOrPath));
            }
        } else {
            mTrackImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_launcher));
        }
    }

    private void setTrackTitle(final String name){
        Preconditions.checkNotNull(mTrackTitle);
        mTrackTitle.setText(name);
    }

    private void setTrackArtist(final ITrack track){
        Preconditions.checkNotNull(mArtistName);
        if(MusicBusterActivity.mServiceConnection.getService().getCurrentTrack() instanceof STrack){
            mArtistName.setText(((STrack) MusicBusterActivity.mServiceConnection.getService().getCurrentTrack()).getGenre());
        }else{
            mArtistName.setText("");
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
            if (MusicBusterActivity.mServiceConnection.getService().mState == State.PlAYING) {
                Runnable notification = this::startPlayProgressUpdater;
                handler.postDelayed(notification,1000);
            }
        }
    }

}
