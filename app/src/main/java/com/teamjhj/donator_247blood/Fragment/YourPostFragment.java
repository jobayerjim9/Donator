package com.teamjhj.donator_247blood.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.teamjhj.donator_247blood.Adapter.OwnPostAdapter;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class YourPostFragment extends Fragment {
    static SwipeRefreshLayout swipeYourPost;
    static RecyclerView ownPostRecycler;
    static RecyclerView.LayoutManager layoutManager;
    static TextView notFoundOwnPost;
    static ArrayList<NonEmergencyInfo> nonEmergencyInfosData;
    static OwnPostAdapter ownPostAdapter;

    public YourPostFragment() {
        // Required empty public constructor
    }

    public static void refreshRecycler(ArrayList<NonEmergencyInfo> nonEmergencyInfos, Context ctx) {
        nonEmergencyInfosData.clear();
        if (nonEmergencyInfos.size() == 0) {
            notFoundOwnPost.setVisibility(View.VISIBLE);
        } else {
            nonEmergencyInfosData.addAll(nonEmergencyInfos);
            notFoundOwnPost.setVisibility(View.GONE);
        }
        ownPostAdapter.notifyDataSetChanged();
        if (swipeYourPost.isRefreshing()) {
            swipeYourPost.setRefreshing(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_your_post, container, false);
        nonEmergencyInfosData = new ArrayList<>();
        ownPostRecycler = v.findViewById(R.id.ownPostRecycler);
        layoutManager = new LinearLayoutManager(getContext());
        ownPostRecycler.setLayoutManager(layoutManager);
        ownPostAdapter = new OwnPostAdapter(getContext(), nonEmergencyInfosData);
        ownPostRecycler.setAdapter(ownPostAdapter);
        swipeYourPost = v.findViewById(R.id.swipeYourPost);
        notFoundOwnPost = v.findViewById(R.id.notFoundOwnPost);
        swipeYourPost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeYourPost.setColorScheme(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);
                BloodFeedFragment.getDataFromDatabase();
            }

        });

        return v;
    }


}
