package com.teamjhj.donator_247blood.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Adapter.BloodFeedArchiveAdapter;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodFeedRequestArchiveFragment extends Fragment {

    ArrayList<NonEmergencyInfo> nonEmergencyInfos = new ArrayList<>();
    private ShimmerFrameLayout feedArchiveShimmer;
    private TextView no_archive_blood_feed;
    private LottieAnimationView nothingFoundFeedArchive;
    public BloodFeedRequestArchiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blood_feed_request_archive, container, false);
        RecyclerView bloodFeedArchiveRecycler = v.findViewById(R.id.bloodFeedArchiveRecycler);
        feedArchiveShimmer = v.findViewById(R.id.feedArchiveShimmer);
        nothingFoundFeedArchive = v.findViewById(R.id.nothingFoundFeedArchive);
        feedArchiveShimmer.startShimmer();
        bloodFeedArchiveRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        BloodFeedArchiveAdapter bloodFeedArchiveAdapter = new BloodFeedArchiveAdapter(getContext(), nonEmergencyInfos);
        bloodFeedArchiveRecycler.setAdapter(bloodFeedArchiveAdapter);
        DatabaseReference bloodFeed = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests");
        bloodFeed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nonEmergencyInfos.clear();
                for (DataSnapshot year : dataSnapshot.getChildren()) {
                    for (DataSnapshot month : year.getChildren()) {

                        for (DataSnapshot day : month.getChildren()) {
                            for (DataSnapshot post : day.getChildren()) {
                                NonEmergencyInfo nonEmergencyInfo = post.getValue(NonEmergencyInfo.class);
                                if (nonEmergencyInfo != null) {
                                    if (nonEmergencyInfo.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                                        nonEmergencyInfo.setKey(post.getKey());
                                        nonEmergencyInfos.add(nonEmergencyInfo);
                                        feedArchiveShimmer.stopShimmer();
                                        feedArchiveShimmer.setVisibility(View.GONE);
                                        nothingFoundFeedArchive.setVisibility(View.GONE);
                                        bloodFeedArchiveAdapter.notifyDataSetChanged();
                                    }
                                }


                            }
                        }
                    }


                }
                if (nonEmergencyInfos.isEmpty()) {
                    nothingFoundFeedArchive.setVisibility(View.VISIBLE);
                    no_archive_blood_feed.setVisibility(View.VISIBLE);
                    feedArchiveShimmer.stopShimmer();
                    feedArchiveShimmer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

}
