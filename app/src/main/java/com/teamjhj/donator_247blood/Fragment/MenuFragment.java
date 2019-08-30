package com.teamjhj.donator_247blood.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Activity.MessengerActivity;
import com.teamjhj.donator_247blood.Activity.MyRequestActivity;
import com.teamjhj.donator_247blood.Activity.NotificationActivity;
import com.teamjhj.donator_247blood.Activity.PostsActivity;
import com.teamjhj.donator_247blood.Activity.ProfileActivity;
import com.teamjhj.donator_247blood.Activity.RequestArchiveActivity;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    FragmentTransaction ft;
    private CardView viewProfileMenu, emergencyRequestMenu, notificationMenu, postsMenu, messengerMenu, archiveMenu, historyRequestMenu;
    private LinearLayout menuLayout;
    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        viewProfileMenu = v.findViewById(R.id.viewProfileMenu);
        TextView name_menu = v.findViewById(R.id.name_menu);
        DatabaseReference profile= FirebaseDatabase.getInstance().getReference("UserProfile").child(FirebaseAuth.getInstance().getUid());
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                if(userProfile!=null)
                {
                    AppData.setUserProfile(userProfile);
                    name_menu.setText(userProfile.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        name_menu.setText(AppData.getUserProfile().getName());
        emergencyRequestMenu = v.findViewById(R.id.emergencyRequestMenu);
        notificationMenu = v.findViewById(R.id.notificationMenu);
        postsMenu = v.findViewById(R.id.postsMenu);
        messengerMenu = v.findViewById(R.id.messengerMenu);
        menuLayout = v.findViewById(R.id.menuLayout);
        historyRequestMenu = v.findViewById(R.id.historyRequestMenu);
        archiveMenu = v.findViewById(R.id.archiveMenu);

        viewProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });
        archiveMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RequestArchiveActivity.class));
            }
        });
        emergencyRequestMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyRequestActivity.class));
            }
        });
        notificationMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NotificationActivity.class));
            }
        });
        postsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostsActivity.class));
            }
        });
        messengerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MessengerActivity.class));
            }
        });
        historyRequestMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPendingHistoryDialog viewPendingHistoryDialog = new ViewPendingHistoryDialog();
                viewPendingHistoryDialog.show(getChildFragmentManager(), "PendingHistory");
            }
        });


        return v;
    }

}
