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
import com.teamjhj.donator_247blood.Adapter.EmergencyArchiveAdapter;
import com.teamjhj.donator_247blood.DataModel.LiveBloodRequest;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyRequestArchiveFragment extends Fragment {
    RecyclerView emergencyArchiveRecycler;
    EmergencyArchiveAdapter emergencyArchiveAdapter;
    TextView no_archived_request;
    private ShimmerFrameLayout emergencyArchiveShimmer;
    private ArrayList<LiveBloodRequest> liveBloodRequests = new ArrayList<>();
    private LottieAnimationView nothingFoundEmergencyArchive;
    public EmergencyRequestArchiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_emergency_request_archive, container, false);

        emergencyArchiveRecycler = v.findViewById(R.id.emergencyArchiveRecycler);
        no_archived_request = v.findViewById(R.id.no_archived_request);
        emergencyArchiveShimmer = v.findViewById(R.id.emergencyArchiveShimmer);
        nothingFoundEmergencyArchive = v.findViewById(R.id.nothingFoundEmergencyArchive);
        emergencyArchiveShimmer.startShimmer();
        emergencyArchiveAdapter = new EmergencyArchiveAdapter(getContext(), liveBloodRequests, getChildFragmentManager());
        emergencyArchiveRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        emergencyArchiveRecycler.setAdapter(emergencyArchiveAdapter);
        DatabaseReference liveArchive = FirebaseDatabase.getInstance().getReference("RequestArchive").child(FirebaseAuth.getInstance().getUid());
        liveArchive.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    LiveBloodRequest liveBloodRequest = dataSnapshot1.getValue(LiveBloodRequest.class);
                    if (liveBloodRequest != null) {
                        emergencyArchiveShimmer.stopShimmer();
                        emergencyArchiveShimmer.setVisibility(View.GONE);
                        nothingFoundEmergencyArchive.setVisibility(View.GONE);
                        liveBloodRequest.setKey(dataSnapshot1.getKey());
                        liveBloodRequests.add(liveBloodRequest);
                        emergencyArchiveAdapter.notifyDataSetChanged();
                    }
                }
                if (liveBloodRequests.isEmpty()) {
                    emergencyArchiveShimmer.stopShimmer();
                    emergencyArchiveShimmer.setVisibility(View.GONE);
                    no_archived_request.setVisibility(View.VISIBLE);
                    nothingFoundEmergencyArchive.setVisibility(View.VISIBLE);
                } else {
                    emergencyArchiveShimmer.stopShimmer();
                    emergencyArchiveShimmer.setVisibility(View.GONE);
                    no_archived_request.setVisibility(View.GONE);
                    nothingFoundEmergencyArchive.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

}
