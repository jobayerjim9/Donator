package com.teamjhj.donator_247blood.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Adapter.BloodFeedAcceptedAdapter;
import com.teamjhj.donator_247blood.DataModel.AcceptingData;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodFeedRequestFragment extends DialogFragment {
    ArrayList<AcceptingData> acceptingData = new ArrayList<>();
    private Context ctx;
    private NonEmergencyInfo nonEmergencyInfo;
    private ImageView closeButtonBloodFeedRequest;
    private RecyclerView bloodFeedRequestRecycler;
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<UserProfile> userProfiles = new ArrayList<>();
    private BloodFeedAcceptedAdapter adapter;
    private TextView optInText;

    public BloodFeedRequestFragment(NonEmergencyInfo nonEmergencyInfo) {
        this.nonEmergencyInfo = nonEmergencyInfo;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_blood_feed_request, null);
        bloodFeedRequestRecycler = v.findViewById(R.id.bloodFeedRequestRecycler);
        closeButtonBloodFeedRequest = v.findViewById(R.id.closeButtonBloodFeedRequest);
        closeButtonBloodFeedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        optInText = v.findViewById(R.id.optInText);
        bloodFeedRequestRecycler.setLayoutManager(new LinearLayoutManager(ctx));
        adapter = new BloodFeedAcceptedAdapter(getContext(), userProfiles, nonEmergencyInfo, acceptingData);
        bloodFeedRequestRecycler.setAdapter(adapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfo.getYear() + "").child(nonEmergencyInfo.getMonth() + "").child(nonEmergencyInfo.getDate() + "").child(nonEmergencyInfo.getKey()).child("AcceptedRequest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfiles.clear();
                if (!dataSnapshot.exists()) {
                    Toast.makeText(ctx, "No Donor Opt In", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.exists()) {
                        AcceptingData data = dataSnapshot1.getValue(AcceptingData.class);
                        optInText.setText("Opt In Donors");
                        if (data != null) {
                            acceptingData.add(data);
                            String key = dataSnapshot1.getKey();
                            DatabaseReference profile = FirebaseDatabase.getInstance().getReference("UserProfile").child(key);
                            profile.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                                    if (userProfile != null) {
                                        Log.d("BloodFeedReq", userProfile.getName());
                                        userProfiles.add(userProfile);
                                        adapter.notifyDataSetChanged();
                                       // bloodFeedRequestRecycler.
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    } else {

                        dismiss();
                        optInText.setText("No Opt In Donors");
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        builder.setView(v);
        return builder.create();
    }
}
