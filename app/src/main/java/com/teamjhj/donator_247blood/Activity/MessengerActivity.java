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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MessengerActivity extends AppCompatActivity {
    ArrayList<ChatData> chatData = new ArrayList<>();
    RecyclerView messengerRecycler;
    ChatListAdapter chatListAdapter;
    LottieAnimationView lottieAnimationView;
    TextView no_message_Label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Toolbar toolbar = findViewById(R.id.messengerToolbar);
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
                        try {
                            Date d1 = Calendar.getInstance().getTime();
                            Date d2 = data.getMessageTime();
                            Log.d("Current Time", d1.getTime() + "");
                            long diff = d1.getTime() - d2.getTime();

                            long diffSeconds = diff / 1000 % 60;
                            long diffMinutes = diff / (60 * 1000) % 60;
                            long diffHours = diff / (60 * 60 * 1000) % 24;
                            long diffDays = diff / (24 * 60 * 60 * 1000);
                            Log.d("Time", diffMinutes + " " + diffHours);
                            if (diffMinutes == 0) {
                                data.setDiffSec(diffSeconds);
                            } else if (diffMinutes > 0 && diffHours == 0) {
                                data.setDiffSec(diffMinutes);

                            } else if (diffHours > 0 && diffDays == 0) {
                                data.setDiffSec(diffHours);
                            } else if (diffDays > 0) {
                                data.setDiffSec(diffDays);
                            }
                            Log.d("DiffMin", diffMinutes + " Mess " + data.getMessage());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        chatData.add(data);

                        //chatListAdapter.notifyDataSetChanged();
                    }

                }
                Collections.sort(chatData, new Comparator<ChatData>() {
                    @Override
                    public int compare(ChatData chatData, ChatData t1) {
                        return Long.compare(chatData.getDiffSec(), t1.getDiffSec());
                    }
                });
                //Collections.reverse(chatData);
                if (chatData.isEmpty()) {
                    no_message_Label.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                } else {
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
