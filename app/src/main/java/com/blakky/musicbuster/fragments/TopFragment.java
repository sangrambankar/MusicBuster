package com.blakky.musicbuster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blakky.musicbuster.R;
import com.blakky.musicbuster.models.STrack;
import com.blakky.musicbuster.mvp.top.presenter.TopPresenter;
import com.blakky.musicbuster.mvp.top.view.ITopView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sangrambankar on 5/4/17.
 */

public class TopFragment extends Fragment implements ITopView{

    private TopPresenter mPresenter;
    private Unbinder mUnbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TopPresenter(this);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter.searchTopTrack();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onSearchLoadedSuccess(List<STrack> tracks) {
        Toast.makeText(getActivity(),"tracks "+tracks.get(0).getTitle(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSearchLoadedFailure(Throwable message) {
        Toast.makeText(getActivity(),"message"+message,Toast.LENGTH_SHORT).show();
        Log.d("TopFragment",message.toString());
    }
}
