package com.teamjhj.donator_247blood.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import com.facebook.shimmer.ShimmerFrameLayout;
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
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyRequestFragment extends Fragment {


    LiveBloodRequest liveBloodRequest;
    TextView reasonBloodPending, acceptedDonorText, no_blood_request, datePendingPost, bloodGroupLargePending, bloodGroupSmallPending;
    ArrayList<Integer> radius = new ArrayList<>();
    AcceptedDonorAdapter acceptedDonorAdapter;
    CardView pendingRequestCard;
    DatabaseReference liveRequest;
    AcceptingData acceptingData;
    private ArrayList<UserProfile> userProfiles = new ArrayList<>();
    private RecyclerView acceptedRecycler;
    private ShimmerFrameLayout emergencyRequestShimmer;

    public EmergencyRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_emergency_request, container, false);

        emergencyRequestShimmer = v.findViewById(R.id.emergencyRequestShimmer);
        emergencyRequestShimmer.startShimmer();
        acceptedRecycler = v.findViewById(R.id.acceptedRecycler);
        reasonBloodPending = v.findViewById(R.id.reasonPendingRequest);
        bloodGroupLargePending = v.findViewById(R.id.bloodGroupLargePending);
        bloodGroupSmallPending = v.findViewById(R.id.bloodGroupSmallPending);
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
                    String blood1, blood2;
                    Date date = liveBloodRequest.getDate();
                    String placeHolder = date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear() + 1900);
                    if (liveBloodRequest.getBloodGroup().contains("A+")) {
                        blood1 = "A+";
                        blood2 = "A Positive";
                    } else if (liveBloodRequest.getBloodGroup().contains("A-")) {
                        blood1 = "A-";
                        blood2 = "A Negative";
                    } else if (liveBloodRequest.getBloodGroup().contains("AB+")) {
                        blood1 = "AB+";
                        blood2 = "AB Positive";
                    } else if (liveBloodRequest.getBloodGroup().contains("AB-")) {
                        blood1 = "AB-";
                        blood2 = "AB Negative";
                    } else if (liveBloodRequest.getBloodGroup().contains("B+")) {
                        blood1 = "B+";
                        blood2 = "B Positive";
                    } else if (liveBloodRequest.getBloodGroup().contains("B-")) {
                        blood1 = "B-";
                        blood2 = "B Negative";
                    } else if (liveBloodRequest.getBloodGroup().contains("O+")) {
                        blood1 = "O+";
                        blood2 = "O Positive";
                    } else if (liveBloodRequest.getBloodGroup().contains("O-")) {
                        blood1 = "O-";
                        blood2 = "O Negative";
                    } else {
                        blood1 = "";
                        blood2 = "";
                    }
                    bloodGroupLargePending.setText(blood1);
                    bloodGroupSmallPending.setText(blood2);
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
                    emergencyRequestShimmer.stopShimmer();
                    emergencyRequestShimmer.setVisibility(View.GONE);
                    no_blood_request.setVisibility(View.VISIBLE);
                    no_blood_request.setText("No Donor Respond!");
                    pendingRequestCard.setVisibility(View.VISIBLE);
                    acceptedDonorText.setVisibility(View.VISIBLE);
                } else {
                    emergencyRequestShimmer.stopShimmer();
                    emergencyRequestShimmer.setVisibility(View.GONE);
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
                                        emergencyRequestShimmer.stopShimmer();
                                        emergencyRequestShimmer.setVisibility(View.GONE);
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
                                moveToArchive();

                            }
                        })

                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        return v;
    }

    private void moveToArchive() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Cancelling Your Request!");
        progressDialog.show();
        liveRequest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LiveBloodRequest liveBloodRequest = dataSnapshot.getValue(LiveBloodRequest.class);
                if (liveBloodRequest != null) {
                    DatabaseReference archiveRef = FirebaseDatabase.getInstance().getReference("RequestArchive").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    String key = archiveRef.push().getKey();
                    archiveRef.child(key).setValue(liveBloodRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                liveRequest.child("DonorsFound").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            AcceptingData acceptingData = dataSnapshot1.getValue(AcceptingData.class);
                                            if (acceptingData != null) {
                                                if (acceptingData.isAccepted()) {
                                                    archiveRef.child(key).child("AcceptedDonor").child(Objects.requireNonNull(dataSnapshot1.getKey())).setValue(acceptingData);
                                                }
                                            }
                                        }
                                        liveRequest.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    no_blood_request.setVisibility(View.VISIBLE);
                                                    pendingRequestCard.setVisibility(View.GONE);
                                                    acceptedDonorText.setVisibility(View.GONE);
                                                    //Toast.makeText(getContext(), "Request Cancelled Successfully", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
