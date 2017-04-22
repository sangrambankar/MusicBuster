package com.blakky.musicbuster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.music.MusicCoverView;
import com.blakky.musicbuster.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blakky.musicbuster.helpers.Constants.UPDATE_FOOTER;

/**
 * Created by sangrambankar on 4/21/17.
 */

public class PlayerActivity extends AppCompatActivity{

    @BindView(R.id.player_cover)
    private MusicCoverView mCoverView;

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

    @BindView(R.id.player_time)
    TextView mSongCurrentDuration;

    @BindView(R.id.player_duration)
    TextView mSongTotalDuration;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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

}
