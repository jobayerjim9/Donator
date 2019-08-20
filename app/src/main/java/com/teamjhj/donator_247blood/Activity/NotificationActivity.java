package com.teamjhj.donator_247blood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Adapter.NotificationAdapter;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView notificationRecycler;
    private ArrayList<NotificationData> notificationData = new ArrayList<>();
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar notificationToolbar = findViewById(R.id.notificationToolbar);
        setSupportActionBar(notificationToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        notificationToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        notificationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        notificationRecycler = findViewById(R.id.notificationRecycler);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(this, notificationData);
        notificationRecycler.setAdapter(notificationAdapter);
        getNotificationData();

    }

    private void getNotificationData() {

        DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Notifications").child(FirebaseAuth.getInstance().getUid());
        notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationData.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    NotificationData data = dataSnapshot1.getValue(NotificationData.class);
                    if (data != null) {
                        data.setKey(dataSnapshot1.getKey());
                        notificationData.add(data);
                        notificationAdapter.notifyDataSetChanged();
                    }
                }
                Collections.reverse(notificationData);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
