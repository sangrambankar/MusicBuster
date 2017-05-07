package com.blakky.musicbuster.fragments.top;

/**
 * Created by sangrambankar on 5/5/17.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blakky.musicbuster.R;
import com.blakky.musicbuster.adapters.TracksListAdapter;
import com.blakky.musicbuster.helpers.Constants;
import com.blakky.musicbuster.listeners.OnItemClickListener;
import com.blakky.musicbuster.models.STrack;
import com.blakky.musicbuster.models.TopChartCollection;
import com.blakky.musicbuster.mvp.top.presenter.TopPresenter;
import com.blakky.musicbuster.mvp.top.view.ITopView;
import com.blakky.musicbuster.views.TracksListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TopAllFragment extends Fragment implements ITopView{


    private String genre;


    public TopAllFragment() {

    }

    private TopPresenter mPresenter;
    private Unbinder mUnbinder;

    TracksListView mListSearchResults;

    private TracksListAdapter mListTracksAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TopPresenter(this);
        genre = getArguments().getString(Constants.EXTRA_GENRE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        mListSearchResults = (TracksListView)view.findViewById(R.id.search_results);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter.searchTopTrack(genre);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        rePopulateList();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onSearchLoadedSuccess(TopChartCollection chartCollection) {
        List<STrack> tracks = new ArrayList<>();
        for (int i=0;i<chartCollection.getChartitems().size();i++){
            STrack st = chartCollection.getChartitems().get(i).getTrack();
            tracks.add(st);
        }

        if(mListTracksAdapter == null){
            mListTracksAdapter = new TracksListAdapter(getActivity(), tracks);
            populateList(mListTracksAdapter);
        }else{
            mListTracksAdapter.updateListTracks(tracks);
            setListViewListener();
        }
    }
    private void populateList(final TracksListAdapter adapter){
        assert mListSearchResults != null;
        mListSearchResults.setAdapter(adapter);
        setListViewListener();
    }

    private void rePopulateList(){
        if(mListTracksAdapter != null){
            assert mListSearchResults != null;
            mListSearchResults.setAdapter(mListTracksAdapter);
            setListViewListener();
        }
    }

    private void setListViewListener(){
        assert mListSearchResults != null;
        mListSearchResults.setOnItemClickListener(new OnItemClickListener(getActivity()));
    }

    @Override
    public void onSearchLoadedFailure(Throwable message) {
        Log.d("TopFragment",message.toString());
    }
}
