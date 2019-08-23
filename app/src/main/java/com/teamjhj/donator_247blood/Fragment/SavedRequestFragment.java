package com.teamjhj.donator_247blood.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DatabaseReference;
import com.teamjhj.donator_247blood.Adapter.RequestAdapter;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedRequestFragment extends Fragment {
    static RecyclerView savedRecycler;
    static SwipeRefreshLayout swipeSavedPost;
    static TextView notFoundSaved;
    static ArrayList<NonEmergencyInfo> nonEmergencyInfosData = new ArrayList<>();
    static RequestAdapter requestAdapter;
    private int date, year, month;
    private DatabaseReference databaseReference;

    public SavedRequestFragment() {
        // Required empty public constructor

    }

    public static void updateData(ArrayList<NonEmergencyInfo> nonEmergencyInfos, Context ctx) {
        try {
            nonEmergencyInfosData.clear();
            if (nonEmergencyInfos.size() == 0) {
                notFoundSaved.setVisibility(View.VISIBLE);
            } else {
                nonEmergencyInfosData.addAll(nonEmergencyInfos);
                notFoundSaved.setVisibility(View.GONE);
            }
            try {
                requestAdapter.notifyDataSetChanged();
                if (swipeSavedPost.isRefreshing()) {
                    swipeSavedPost.setRefreshing(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_request, container, false);
        nonEmergencyInfosData = new ArrayList<>();
        savedRecycler = v.findViewById(R.id.savedRecycler);
        savedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        requestAdapter = new RequestAdapter(getContext(), nonEmergencyInfosData);
        savedRecycler.setAdapter(requestAdapter);
        swipeSavedPost = v.findViewById(R.id.swipeSavedPost);
        notFoundSaved = v.findViewById(R.id.notFoundSaved);
        swipeSavedPost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeSavedPost.setColorScheme(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);
                BloodFeedFragment.getDataFromDatabase();
            }

        });
        return v;
    }


}
