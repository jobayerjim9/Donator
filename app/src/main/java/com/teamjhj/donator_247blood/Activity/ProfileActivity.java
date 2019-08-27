package com.teamjhj.donator_247blood.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Adapter.ProfileTabAdapter;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    UserProfile userProfile;
    LinearLayout expandableLinear;
    private TextView nameProfileTextView, bloodGroupProfileTextView, lastDonationDateProfileTextView;
    private TabLayout profileTabLayout;
    private ViewPager profileViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameProfileTextView = findViewById(R.id.nameProfileTextView);
        expandableLinear = findViewById(R.id.expandableLinear);
        bloodGroupProfileTextView = findViewById(R.id.bloodGroupProfileTextView);
        lastDonationDateProfileTextView = findViewById(R.id.lastDonationDateProfileTextView);
        profileTabLayout = findViewById(R.id.profileTabLayout);
        profileViewPager = findViewById(R.id.profileViewPager);
        ImageView backButtonProfile = findViewById(R.id.backButtonProfile);
        backButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadData();
        ProfileTabAdapter profileTabAdapter = new ProfileTabAdapter(getSupportFragmentManager(), profileTabLayout.getTabCount());
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
