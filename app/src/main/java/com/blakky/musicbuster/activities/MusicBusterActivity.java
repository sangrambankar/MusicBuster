package com.blakky.musicbuster.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blakky.musicbuster.R;
import com.blakky.musicbuster.fragments.StreamFragment;
import com.blakky.musicbuster.fragments.TopFragment;
import com.blakky.musicbuster.helpers.NetworkHelper;
import com.blakky.musicbuster.helpers.ServiceHelper;
import com.blakky.musicbuster.helpers.ViewHelper;
import com.blakky.musicbuster.listeners.ActionFooterPlayerListener;
import com.blakky.musicbuster.models.ITrack;
import com.blakky.musicbuster.services.MediaPlayerConnection;
import com.blakky.musicbuster.views.FooterPlayerView;
import com.google.common.base.Optional;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicBusterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener {


    private MenuItem searchViewItem;
    private SearchView searchView;
    public static MediaPlayerConnection mServiceConnection;
    private String query;

    @Nullable
    @BindView(R.id.footer_player)
    FooterPlayerView mFooterPlayer;

    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper.addNoInternetView(findViewById(android.R.id.content),this);
        mServiceConnection = new MediaPlayerConnection();
        ServiceHelper.doBindService(new WeakReference<>(this), mServiceConnection);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        ServiceHelper.doUnBindService(new WeakReference<>(this), mServiceConnection);
        super.onStop();
    }


    /**
     * Makes the Footer Player {@link FooterPlayerView } visible at the bottom of the MainActivity's view
     * when the user clicks on an item of the Tracks List {@link  com.blakky.musicbuster.views.TracksListView}.
     * @param visible True if the user clicks.
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMakeFooterPlayerVisible(Boolean visible){
        ViewHelper.showOrHideView(Optional.fromNullable(mFooterPlayer), visible.booleanValue());
    }

    /**
     * Displays the track's image and title on the FooterPlayer and sets event listener
     * @param currentSTrack The track that the user has clicked on.
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusDisplayTrackOnFooterPlayer(ITrack currentSTrack){

        assert mFooterPlayer != null;
        mFooterPlayer.setCellValues(currentSTrack);
        mFooterPlayer.setFooterPlayerActionListener(new ActionFooterPlayerListener(mServiceConnection.getService(), this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_buster);
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.music_buster, menu);
        searchViewItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(this);


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
        if(query.trim() != "") {
            this.query = query;
            showFragments("stream");
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    private void showFragments(String tag){


        if(tag.equals("stream")) {
            FragmentManager manager = getSupportFragmentManager();
            StreamFragment newFragment = new StreamFragment();
            Bundle b = new Bundle();
            b.putString("query",query);
            newFragment.setArguments(b);

            manager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_left,
                                R.anim.slide_right,
                                R.anim.slide_left,
                                R.anim.slide_right)
                        .replace(R.id.fragment_container, newFragment, tag)
                        .show(newFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();

            searchView.setQuery("", true);
            searchView.setIconified(true);
            searchView.clearFocus();
            searchViewItem.collapseActionView();
        }else if(tag.equals("top")){
            FragmentManager manager = getSupportFragmentManager();
            TopFragment newFragment = new TopFragment();

            manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .replace(R.id.fragment_container, newFragment, tag)
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_top:
                showFragments("top");
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            default:
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
