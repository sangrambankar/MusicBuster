package com.blakky.musicbuster.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blakky.musicbuster.R;
import com.blakky.musicbuster.helpers.Constants;
import com.blakky.musicbuster.helpers.SharedPreferenceHelper;
import com.blakky.musicbuster.models.ITrack;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * View that is showed like a Player at the bottom of the MainActivity's view.
 */
public class FooterPlayerView extends RelativeLayout {

    @Nullable
    @BindView(R.id.track_image)
    ImageView mTrackImage;

    @Nullable
    @BindView(R.id.track_title)
    TextView mTrackTitle;

    @Nullable
    @BindView(R.id.play)
    ImageView mPlayImage;

    public FooterPlayerView(Context context) { super(context); }

    public FooterPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()){
            initialiseUI(context);
        }
    }

    public FooterPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode()){
            initialiseUI(context);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FooterPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(!isInEditMode()){
            initialiseUI(context);
        }
    }

    private void initialiseUI(final Context context){
        LayoutInflater.from(context).inflate(R.layout.footer_player, this, true);
        ButterKnife.bind(this);
    }

    public void setCellValues(final ITrack track){
        setTrackTitle(track.getTitle());
        setTrackImage(track.getImage());
        setEventListeners();
    }

    public void setTrackTitle(final String title){
        Preconditions.checkNotNull(mTrackTitle);
        mTrackTitle.setText(title);
    }

    public void setTrackImage(final String urlOrPath){
        Preconditions.checkNotNull(mTrackImage);
        if (!Strings.isNullOrEmpty(urlOrPath)) {
            if(urlOrPath.contains(Constants.DIR_IMAGES)){
                mTrackImage.setImageBitmap(BitmapFactory.decodeFile(urlOrPath));
            }else{
                Picasso.with(getContext()).load(urlOrPath)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(mTrackImage);
            }
        } else {
            mTrackImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher));
        }
    }

    private void setEventListeners(){
        Preconditions.checkNotNull(mPlayImage);
        mPlayImage.setOnClickListener(v -> {
            if(mListener != null){
               if(SharedPreferenceHelper.getBooleanValueOfKey(getContext(),Constants.IS_PLAYING)){
                   mListener.onPause();
                   SharedPreferenceHelper.saveKeyValuePairBoolean(getContext(),Constants.IS_PLAYING, false);
                   mPlayImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_av_play_arrow));
               }else{
                   mListener.onStart();
                   SharedPreferenceHelper.saveKeyValuePairBoolean(getContext(),Constants.IS_PLAYING, true);
                   mPlayImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_av_pause));
               }
            }
        });

        if(SharedPreferenceHelper.getBooleanValueOfKey(getContext(),Constants.IS_PLAYING)){
            mPlayImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_av_pause));
        }else{
            mPlayImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_av_play_arrow));
        }

        this.setOnClickListener(v -> mListener.onClick());
    }

    public interface IFooterPlayerAction{
        void onStart();
        void onPause();
        void onClick();
    }

    private IFooterPlayerAction mListener;

    public void setFooterPlayerActionListener(IFooterPlayerAction listener){
        this.mListener = listener;
    }
}
