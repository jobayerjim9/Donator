package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.DataModel.AcceptingData;
import com.teamjhj.donator_247blood.DataModel.LiveBloodRequest;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmergencyArchiveAdapter extends RecyclerView.Adapter<EmergencyArchiveAdapter.EmergencyArchiveViewHolder> {
    FragmentManager fm;
    private Context ctx;
    private ArrayList<LiveBloodRequest> liveBloodRequests;

    public EmergencyArchiveAdapter(Context ctx, ArrayList<LiveBloodRequest> liveBloodRequests, FragmentManager fm) {
        this.ctx = ctx;
        this.liveBloodRequests = liveBloodRequests;
        this.fm = fm;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public EmergencyArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmergencyArchiveViewHolder(LayoutInflater.from(ctx).inflate(R.layout.emergency_archive_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyArchiveViewHolder holder, int position) {
        String placeHolder = liveBloodRequests.get(position).getDate().getDate() + "-" + (liveBloodRequests.get(position).getDate().getMonth() + 1) + "-" + (liveBloodRequests.get(position).getDate().getYear() + 1900);
        holder.dateEmergencyArchive.setText(placeHolder);
        holder.reasonEmergencyArchive.setText(liveBloodRequests.get(position).getReason());
        String blood1, blood2;
        if (liveBloodRequests.get(position).getBloodGroup().contains("A+")) {
            blood1 = "A+";
            blood2 = "A Positive";
        } else if (liveBloodRequests.get(position).getBloodGroup().contains("A-")) {
            blood1 = "A-";
            blood2 = "A Negative";
        } else if (liveBloodRequests.get(position).getBloodGroup().contains("AB+")) {
            blood1 = "AB+";
            blood2 = "AB Positive";
        } else if (liveBloodRequests.get(position).getBloodGroup().contains("AB-")) {
            blood1 = "AB-";
            blood2 = "AB Negative";
        } else if (liveBloodRequests.get(position).getBloodGroup().contains("B+")) {
            blood1 = "B+";
            blood2 = "B Positive";
        } else if (liveBloodRequests.get(position).getBloodGroup().contains("B-")) {
            blood1 = "B-";
            blood2 = "B Negative";
        } else if (liveBloodRequests.get(position).getBloodGroup().contains("O+")) {
            blood1 = "O+";
            blood2 = "O Positive";
        } else if (liveBloodRequests.get(position).getBloodGroup().contains("O-")) {
            blood1 = "O-";
            blood2 = "O Negative";
        } else {
            blood1 = "";
            blood2 = "";
        }
        try {
            Geocoder geocoder = new Geocoder(ctx);
            // LatLng pickedLocation = new LatLng(, );
            try {
                List<Address> addresses;
                addresses = geocoder.getFromLocation(liveBloodRequests.get(position).getLat(), liveBloodRequests.get(position).getLon(), 1);
                holder.locationEmergrncyArchive.setText(addresses.get(0).getAddressLine(0));
                //String add = addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.bloodGroupLargeEmergencyArchive.setText(blood1);
        holder.bloodGroupSmallEmergencyArchive.setText(blood2);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RequestArchive").child(FirebaseAuth.getInstance().getUid()).child(liveBloodRequests.get(position).getKey()).child("AcceptedDonor");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    AcceptingData acceptingData = dataSnapshot1.getValue(AcceptingData.class);
                    if (acceptingData != null) {
                        if (acceptingData.isBloodRecieved()) {
                            DatabaseReference profile = FirebaseDatabase.getInstance().getReference("UserProfile").child(dataSnapshot1.getKey());
                            profile.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                                    if (userProfile != null) {
                                        String placeHolder = "Blood Received From " + userProfile.getName();
                                        holder.bloodRecievedArchive.setText(placeHolder);

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
    }

    @Override
    public int getItemCount() {
        return liveBloodRequests.size();
    }

    class EmergencyArchiveViewHolder extends RecyclerView.ViewHolder {
        TextView dateEmergencyArchive, reasonEmergencyArchive, bloodGroupLargeEmergencyArchive, bloodGroupSmallEmergencyArchive, locationEmergrncyArchive, bloodRecievedArchive;

        EmergencyArchiveViewHolder(@NonNull View itemView) {
            super(itemView);
            dateEmergencyArchive = itemView.findViewById(R.id.dateEmergencyArchive);
            reasonEmergencyArchive = itemView.findViewById(R.id.reasonEmergencyArchive);
            bloodGroupLargeEmergencyArchive = itemView.findViewById(R.id.bloodGroupLargeEmergencyArchive);
            bloodGroupSmallEmergencyArchive = itemView.findViewById(R.id.bloodGroupSmallEmergencyArchive);
            bloodRecievedArchive = itemView.findViewById(R.id.bloodRecievedArchive);

            locationEmergrncyArchive = itemView.findViewById(R.id.locationEmergrncyArchive);

        }
    }
}
