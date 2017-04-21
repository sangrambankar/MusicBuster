package com.blakky.musicbuster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blakky.musicbuster.R;
import com.blakky.musicbuster.adapters.TracksListAdapter;
import com.blakky.musicbuster.listeners.OnItemClickListener;
import com.blakky.musicbuster.listeners.TracksListScrollListener;
import com.blakky.musicbuster.models.STrack;
import com.blakky.musicbuster.mvp.search.presenter.SearchPresenter;
import com.blakky.musicbuster.mvp.search.view.ISearchView;
import com.blakky.musicbuster.views.TracksListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sangrambankar on 4/6/17.
 */

public class StreamFragment extends Fragment implements ISearchView{


    TracksListView mListSearchResults;

    private TracksListAdapter mListTracksAdapter;
    private TracksListScrollListener mListTrackScroll;
    private Unbinder mUnbinder;
    private String query = null;
    private SearchPresenter mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchPresenter(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
           query = bundle.getString("query");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        mListSearchResults = (TracksListView)view.findViewById(R.id.search_results);
        mUnbinder = ButterKnife.bind(this, view);

        searchQuery(this.query);


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
    public void onSearchLoadedSuccess(List<STrack> tracks) {
        Toast.makeText(getActivity(),"Q"+tracks.get(0).getTitle(),Toast.LENGTH_SHORT).show();
        if(mListTracksAdapter == null){
            mListTracksAdapter = new TracksListAdapter(getActivity(), tracks);
            populateList(mListTracksAdapter);
        }else{
            mListTracksAdapter.updateListTracks(tracks);
            setListViewListener();
        }
    }

    @Override
    public void onSearchLoadedFailure(Throwable message) {
      /*  if(mListTracksAdapter != null) mListTracksAdapter.clear();
        setNoSearchResultsVisibility(true);
        setBallSpinLoaderVisibility(false);*/
        Toast.makeText(getActivity(),"No search results for this keyword",Toast.LENGTH_SHORT).show();

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
        mListSearchResults.setOnDetectScrollListener(setScrollList());
    }


    private TracksListScrollListener setScrollList(){
        if(mListTrackScroll == null){
            mListTrackScroll = new TracksListScrollListener(this.query, mListTracksAdapter);
        } else{
            // Initialise variables with a new keyword to search for.
            mListTrackScroll.setNewSearch(this.query);
        }
        return mListTrackScroll;
    }

    public void searchQuery(String query){
        this.query = query;
        mPresenter.searchTrack(query);
    }
}
