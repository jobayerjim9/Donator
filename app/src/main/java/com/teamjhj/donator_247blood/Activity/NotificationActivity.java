package com.teamjhj.donator_247blood.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Collections;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView notificationRecycler;
    private ArrayList<NotificationData> notificationData = new ArrayList<>();
    private NotificationAdapter notificationAdapter;
    DatabaseReference notification;
    ShimmerFrameLayout shimmerNotification;
    TextView noNotification;
    LottieAnimationView no_notifications;
    private Button clearAllNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notification = FirebaseDatabase.getInstance().getReference("Notifications").child(FirebaseAuth.getInstance().getUid());
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
        shimmerNotification = findViewById(R.id.shimmerNotification);
        no_notifications = findViewById(R.id.no_notifications);
        noNotification = findViewById(R.id.noNotification);
        shimmerNotification.startShimmer();
        clearAllNotification = findViewById(R.id.clearAllNotification);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(this, notificationData);
        notificationRecycler.setAdapter(notificationAdapter);
        clearAllNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(NotificationActivity.this, "Notification Cleared Successfully!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(NotificationActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        getNotificationData();

    }

    private void getNotificationData() {


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
                        shimmerNotification.stopShimmer();
                        shimmerNotification.setVisibility(View.GONE);
                        no_notifications.setVisibility(View.GONE);
                        noNotification.setVisibility(View.GONE);
                        clearAllNotification.setVisibility(View.VISIBLE);
                    }
                }
                Collections.reverse(notificationData);
                if (notificationData.isEmpty()) {

                    shimmerNotification.stopShimmer();
                    shimmerNotification.setVisibility(View.GONE);
                    clearAllNotification.setVisibility(View.GONE);
                    no_notifications.setVisibility(View.VISIBLE);
                    noNotification.setVisibility(View.VISIBLE);
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
