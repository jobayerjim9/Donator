package com.teamjhj.donator_247blood.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.teamjhj.donator_247blood.Adapter.ChatListAdapter;
import com.teamjhj.donator_247blood.DataModel.ChatData;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MessengerActivity extends AppCompatActivity {
    ArrayList<ChatData> chatData = new ArrayList<>();
    RecyclerView messengerRecycler;
    ChatListAdapter chatListAdapter;
    LottieAnimationView lottieAnimationView;
    TextView no_message_Label;
    private ShimmerFrameLayout messengerShimmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Toolbar toolbar = findViewById(R.id.messengerToolbar);
        messengerShimmer = findViewById(R.id.messengerShimmer);
        messengerShimmer.startShimmer();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.d("MY UID", FirebaseAuth.getInstance().getUid());
        messengerRecycler = findViewById(R.id.messengerRecycler);
        lottieAnimationView = findViewById(R.id.no_messege);
        no_message_Label = findViewById(R.id.no_message_Label);
        messengerRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatListAdapter = new ChatListAdapter(chatData, this);
        messengerRecycler.setAdapter(chatListAdapter);
        DatabaseReference chatList = FirebaseDatabase.getInstance().getReference("MessengerChat").child(FirebaseAuth.getInstance().getUid());
        chatList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatData.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ChatData data = null;
                    try {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            data = dataSnapshot2.getValue(ChatData.class);
                            if (data != null) {
                                data.setMessegingKey(dataSnapshot2.getKey());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (data != null) {

                        chatData.add(data);

                        //chatListAdapter.notifyDataSetChanged();
                    }

                }

                Collections.sort(chatData, new Comparator<ChatData>() {
                    @Override
                    public int compare(ChatData chatData, ChatData t1) {
                        return chatData.getMessageTime().compareTo(t1.getMessageTime());
                        //return Long.compare(chatData.getDiffSec(), t1.getDiffSec());
                    }
                });
                Collections.reverse(chatData);
                if (chatData.isEmpty()) {
                    messengerShimmer.setVisibility(View.GONE);
                    messengerShimmer.stopShimmer();
                    no_message_Label.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                } else {
                    messengerShimmer.setVisibility(View.GONE);
                    messengerShimmer.stopShimmer();
                    no_message_Label.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                }

                chatListAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
