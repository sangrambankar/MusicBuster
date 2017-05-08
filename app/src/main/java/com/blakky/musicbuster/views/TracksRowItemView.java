package com.blakky.musicbuster.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blakky.musicbuster.R;
import com.blakky.musicbuster.helpers.Constants;
import com.blakky.musicbuster.helpers.DialogHelper;
import com.blakky.musicbuster.helpers.ServiceHelper;
import com.blakky.musicbuster.models.DTrack;
import com.blakky.musicbuster.models.ITrack;
import com.blakky.musicbuster.models.STrack;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class represents a row in the list of tracks
 */
public class TracksRowItemView extends RelativeLayout {

    @Nullable
    @BindView(R.id.track_image)
    ImageView mTrackImage;

    @Nullable
    @BindView(R.id.track_title)
    TextView mTrackTitle;

    @Nullable
    @BindView(R.id.more_button)
    ImageView mOptionsButton;

    private List<ITrack> mTracks;
    private static final int PLAY_TRACK = 0;
    private static final int DOWNLOAD_TRACK = 1;

    public TracksRowItemView(Context context) {
        super(context);
    }

    public TracksRowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeUI(context);
    }

    public TracksRowItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeUI(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TracksRowItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeUI(context);
    }

    private void initializeUI(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.tracks_row_item, this, true);
        ButterKnife.bind(this);
    }


    public static TracksRowItemView inflate(final ViewGroup parent, final Context context) {
        return (TracksRowItemView)
                ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.tracks_list_item, parent, false);
    }

    public void setCellValues(final ITrack track, final int position){
        setTrackImage(track.getImage(), track);
        setTrackTitle(track.getTitle());
        setOnClickOptions(position, track);
    }

    public void setTracks(final List<ITrack> tracks){
        if(tracks != null){
           this.mTracks = tracks;
        }
    }

    private void setTrackImage(final String urlOrPath, final ITrack track){
        Preconditions.checkNotNull(mTrackImage);
        if (!Strings.isNullOrEmpty(urlOrPath)) {
            if(track instanceof STrack){
                Picasso.with(getContext()).load(urlOrPath).placeholder(R.mipmap.ic_launcher).into(mTrackImage);
            }else if(track instanceof DTrack){
                mTrackImage.setImageBitmap(BitmapFactory.decodeFile(urlOrPath));
            }
        } else {
            mTrackImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher));
        }
    }

    private void setTrackTitle(final String name){
        Preconditions.checkNotNull(mTrackTitle);
        mTrackTitle.setText(name);
    }

    private void setOnClickOptions(final int position, final ITrack track){
        Preconditions.checkNotNull(mOptionsButton);
        if(track instanceof STrack){  // Search Tab
            mOptionsButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_more_vert_black_24dp, null));
            mOptionsButton.setColorFilter(R.color.colorPrimary);

            mOptionsButton.setOnClickListener(v ->
                    DialogHelper.showListItemsDialog(getContext(),
                    R.string.track_options, getStringIDs(),
                    getDrawableIDs(), getDialogListCallback(position))
            );

        }else if(track instanceof DTrack){ // Download Tab
            mOptionsButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.mipmap.ic_delete_grey, null));

            mOptionsButton.setOnClickListener(v ->
                    DialogHelper.confirmDialogAction(getContext(),
                    R.string.title_dialog, R.string.delete_track_content,
                    getPositiveCallback(position), getNegativeCallback())
            );
        }
    }

    private int[] getStringIDs(){
        return new int[]{R.string.to_play, R.string.to_download};

    }

    private int[] getDrawableIDs(){
        return new int[]{R.drawable.ic_play, R.drawable.ic_download};
    }

    private MaterialDialog.ListCallback getDialogListCallback(final int position){
        return (dialog, itemView, which, text) -> {
           switch (which){
               case PLAY_TRACK:
                   ServiceHelper.startService(new WeakReference<>((Activity) getContext()), position);
                   EventBus.getDefault().post(mTracks);
                   break;
               case DOWNLOAD_TRACK:
                   if (Build.VERSION.SDK_INT >= 23) {
                       if (getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                               == PackageManager.PERMISSION_GRANTED) {
                           /*DownloadHelper.DownloadTrack((STrack) mTracks.get(position), new WeakReference<>(getContext()),
                                   (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE));
                           SnackbarHelper.showMessage(this, R.string.start_download);*/

                       } else {
                           ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                       }
                   } else {
                       /*DownloadHelper.DownloadTrack((STrack) mTracks.get(position), new WeakReference<>(getContext()),
                               (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE));
                       SnackbarHelper.showMessage(this, R.string.start_download);*/
                   }
                   break;
           }
           dialog.dismiss();
        };
    }

    private MaterialDialog.SingleButtonCallback getPositiveCallback(final int position){
        return (dialog, which) -> {
            //FilesHelper.deleteTrack(mTracks.get(position).getMP3());
            //FilesHelper.deleteImage(mTracks.get(position).getJPG());
            EventBus.getDefault().post(Constants.REMOVE_TRACK);
        };
    }

    private MaterialDialog.SingleButtonCallback getNegativeCallback(){
        return (dialog, which) -> dialog.dismiss();
    }

}

