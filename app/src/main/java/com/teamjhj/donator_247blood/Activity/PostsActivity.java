package com.teamjhj.donator_247blood.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.teamjhj.donator_247blood.Adapter.RequestAdapter;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.Fragment.BloodFeedFragment;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {

    static RecyclerView savedRecycler;
    static SwipeRefreshLayout swipeSavedPost;
    static TextView notFoundSaved;
    static ArrayList<NonEmergencyInfo> nonEmergencyInfosData = new ArrayList<>();
    static RequestAdapter requestAdapter;
    private static ShimmerFrameLayout postShimmer;
    private int date, year, month;
    private DatabaseReference databaseReference;

    public static void updateData(ArrayList<NonEmergencyInfo> nonEmergencyInfos) {

        try {
            nonEmergencyInfosData.clear();
            postShimmer.stopShimmer();
            postShimmer.setVisibility(View.GONE);
            if (nonEmergencyInfos.size() == 0) {
                notFoundSaved.setVisibility(View.VISIBLE);
            } else {
                notFoundSaved.setVisibility(View.GONE);
                nonEmergencyInfosData.addAll(nonEmergencyInfos);

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        nonEmergencyInfosData = new ArrayList<>();
        savedRecycler = findViewById(R.id.savedRecycler);
        postShimmer = findViewById(R.id.postShimmer);
        postShimmer.startShimmer();
        savedRecycler.setLayoutManager(new LinearLayoutManager(this));
        requestAdapter = new RequestAdapter(this, nonEmergencyInfosData);
        savedRecycler.setAdapter(requestAdapter);
        swipeSavedPost = findViewById(R.id.swipeSavedPost);
        notFoundSaved = findViewById(R.id.notFoundSaved);
        swipeSavedPost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeSavedPost.setColorScheme(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);
                postShimmer.startShimmer();
                BloodFeedFragment.getDataFromDatabase();
            }

        });
        BloodFeedFragment.getDataFromDatabase();
        ImageView postsBackButton = findViewById(R.id.postsBackButton);
        postsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
