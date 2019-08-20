package com.teamjhj.donator_247blood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Adapter.ChatListAdapter;
import com.teamjhj.donator_247blood.DataModel.ChatData;

import java.util.ArrayList;

public class MessengerActivity extends AppCompatActivity {
    ArrayList<ChatData> chatData = new ArrayList<>();
    RecyclerView messengerRecycler;
    ChatListAdapter chatListAdapter;

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
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        data = dataSnapshot2.getValue(ChatData.class);
                    }
                    if (data != null) {
                        chatData.add(data);
                        chatListAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
