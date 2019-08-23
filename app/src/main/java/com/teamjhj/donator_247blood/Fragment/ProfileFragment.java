package com.teamjhj.donator_247blood.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Activity.MessengerActivity;
import com.teamjhj.donator_247blood.Activity.NotificationActivity;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    UserProfile userProfile;
    private TextView nameProfileTextView, bloodGroupProfileTextView, lastDonationDateProfileTextView;
    private TabLayout profileTabLayout;
    private ViewPager profileViewPager;
    private CardView messenger_card, notification_card;
    private ImageView messenger_card_image, notification_card_image;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        nameProfileTextView = v.findViewById(R.id.nameProfileTextView);
        bloodGroupProfileTextView = v.findViewById(R.id.bloodGroupProfileTextView);
        lastDonationDateProfileTextView = v.findViewById(R.id.lastDonationDateProfileTextView);
        profileTabLayout = v.findViewById(R.id.profileTabLayout);
        profileViewPager = v.findViewById(R.id.profileViewPager);
        messenger_card = v.findViewById(R.id.messenger_card);
        notification_card = v.findViewById(R.id.notification_card);
        messenger_card_image = v.findViewById(R.id.messenger_card_image);
        notification_card_image = v.findViewById(R.id.notification_card_image);
        messenger_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MessengerActivity.class));
            }
        });
        notification_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
        });
        notification_card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
        });
        loadData();
        ProfileTabAdapter profileTabAdapter = new ProfileTabAdapter(getFragmentManager(), profileTabLayout.getTabCount());
        profileViewPager.setAdapter(profileTabAdapter);
        profileTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                profileViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        profileViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(profileTabLayout));

        return v;
    }

    private void loadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(UserProfile.class);
                if (userProfile != null) {
                    AppData.setUserProfile(userProfile);
                    String setter;
                    nameProfileTextView.setText(userProfile.getName());
                    setter = userProfile.getBloodGroup();
                    bloodGroupProfileTextView.setText(setter);
                    if (userProfile.getLastDonationYear() == -1) {
                        setter = "Not Donated Yet!";
                    } else {
                        setter = userProfile.getLastDonationDate() + "-" + userProfile.getLastDonationMonth() + "-" + userProfile.getLastDonationYear();
                    }
                    lastDonationDateProfileTextView.setText(setter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
