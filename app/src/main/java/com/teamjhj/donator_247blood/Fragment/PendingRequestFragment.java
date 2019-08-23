package com.teamjhj.donator_247blood.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Adapter.AcceptedDonorAdapter;
import com.teamjhj.donator_247blood.DataModel.AcceptingData;
import com.teamjhj.donator_247blood.DataModel.LiveBloodRequest;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingRequestFragment extends Fragment {
    LiveBloodRequest liveBloodRequest;
    TextView reasonBloodPending, acceptedDonorText, no_blood_request, datePendingPost;
    ArrayList<Integer> radius = new ArrayList<>();
    AcceptedDonorAdapter acceptedDonorAdapter;
    CardView pendingRequestCard;
    DatabaseReference liveRequest;
    AcceptingData acceptingData;
    private ArrayList<UserProfile> userProfiles = new ArrayList<>();
    private RecyclerView acceptedRecycler;

    public PendingRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pending_request, container, false);
        acceptedRecycler = v.findViewById(R.id.acceptedRecycler);
        reasonBloodPending = v.findViewById(R.id.reasonPendingRequest);
        no_blood_request = v.findViewById(R.id.no_blood_request);
        acceptedDonorText = v.findViewById(R.id.acceptedDonorText);
        pendingRequestCard = v.findViewById(R.id.pendingRequestCard);
        datePendingPost = v.findViewById(R.id.datePendingPost);
        acceptedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        acceptedDonorAdapter = new AcceptedDonorAdapter(getContext(), userProfiles, radius);
        acceptedRecycler.setAdapter(acceptedDonorAdapter);
        liveRequest = FirebaseDatabase.getInstance().getReference("LiveRequest").child(FirebaseAuth.getInstance().getUid());
        liveRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                liveBloodRequest = dataSnapshot.getValue(LiveBloodRequest.class);
                if (liveBloodRequest != null) {
                    Date date = liveBloodRequest.getDate();
                    String placeHolder = " " + date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear() + 1900);
                    datePendingPost.setText(placeHolder);
                    reasonBloodPending.setText(liveBloodRequest.getReason());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        liveRequest.child("DonorsFound").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                radius.clear();
                userProfiles.clear();
                if (dataSnapshot.exists()) {
                    no_blood_request.setVisibility(View.VISIBLE);
                    no_blood_request.setText("No Donor Respond!");
                    pendingRequestCard.setVisibility(View.VISIBLE);
                    acceptedDonorText.setVisibility(View.VISIBLE);
                } else {
                    no_blood_request.setVisibility(View.VISIBLE);
                    no_blood_request.setText("No Pending Blood Request!");
                    pendingRequestCard.setVisibility(View.GONE);
                    acceptedDonorText.setVisibility(View.GONE);
                }
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    acceptingData = dataSnapshot1.getValue(AcceptingData.class);
                    String key = dataSnapshot1.getKey();
                    if (key != null && acceptingData != null) {
                        if (acceptingData.isAccepted()) {
                            DatabaseReference profile = FirebaseDatabase.getInstance().getReference("UserProfile").child(key);
                            profile.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                                    if (userProfile != null) {
                                        userProfiles.add(userProfile);
                                        radius.add(acceptingData.getRadius());
                                        acceptedDonorAdapter.notifyDataSetChanged();
                                        no_blood_request.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button cancelRequestButton = v.findViewById(R.id.cancelRequestButton);
        cancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Cancel Request")
                        .setMessage("Are you sure you want to cancel this Request?")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                liveRequest.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            no_blood_request.setVisibility(View.VISIBLE);
                                            pendingRequestCard.setVisibility(View.GONE);
                                            acceptedDonorText.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), "Request Cancelled Successfully", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })

                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        return v;
    }

}
