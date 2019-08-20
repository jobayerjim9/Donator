package com.teamjhj.donator_247blood;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    ViewPager postViewPager;
    TabLayout postTabLayout;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        postViewPager = v.findViewById(R.id.postViewPager);
        postTabLayout = v.findViewById(R.id.postTabLayout);
        PostsTabAdapter postsTabAdapter = new PostsTabAdapter(getFragmentManager(), postTabLayout.getTabCount());
        postViewPager.setAdapter(postsTabAdapter);
        postTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                postViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        postViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(postTabLayout));
        return v;
    }

}
