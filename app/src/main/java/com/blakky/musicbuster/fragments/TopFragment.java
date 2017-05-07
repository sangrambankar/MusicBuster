package com.blakky.musicbuster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blakky.musicbuster.R;
import com.blakky.musicbuster.adapters.ViewPagerAdapter;
import com.blakky.musicbuster.fragments.top.TopAllFragment;
import com.blakky.musicbuster.helpers.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sangrambankar on 5/4/17.
 */

public class TopFragment extends Fragment{


    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private Unbinder mUnbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(newInstance("soundcloud:genres:all-music"), "All Genre");
        adapter.addFrag(newInstance("soundcloud:genres:electronic"), "Electronic");
        adapter.addFrag(newInstance("soundcloud:genres:disco"), "Disco");
        adapter.addFrag(newInstance("soundcloud:genres:rock"), "Rock");
        adapter.addFrag(newInstance("soundcloud:genres:metal"), "Metal");
        adapter.addFrag(newInstance("soundcloud:genres:pop"), "Pop");
        adapter.addFrag(newInstance("soundcloud:genres:techno"), "Techno");
        adapter.addFrag(newInstance("soundcloud:genres:trance"), "Trance");
        adapter.addFrag(newInstance("soundcloud:genres:trap"), "Trap");
        adapter.addFrag(newInstance("soundcloud:genres:hiphoprap"), "HipHop");

        viewPager.setAdapter(adapter);
    }

    public static TopAllFragment newInstance(String genre)
    {
        TopAllFragment fragment = new TopAllFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_GENRE, genre);
        fragment.setArguments(bundle);
        return fragment ;
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


}
