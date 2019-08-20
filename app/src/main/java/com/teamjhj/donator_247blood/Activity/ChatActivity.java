package com.teamjhj.donator_247blood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Adapter.MessagingAdapter;
import com.teamjhj.donator_247blood.AppData;
import com.teamjhj.donator_247blood.DataModel.ChatData;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatRecycler;
    MessagingAdapter messagingAdapter;
    ArrayList<ChatData> chatData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String name = getIntent().getStringExtra("Name");
        String uidOfReciever = getIntent().getStringExtra("uid");
        TextView nameChat = findViewById(R.id.nameChat);
        nameChat.setText(name);
        EditText enterMessageChat = findViewById(R.id.enterMessageChat);
        ImageView sendButtonChat = findViewById(R.id.sendButtonChat);
        ImageView backChat = findViewById(R.id.backChat);
        backChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        chatRecycler = findViewById(R.id.chatRecycler);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        messagingAdapter = new MessagingAdapter(this, chatData);
        chatRecycler.setAdapter(messagingAdapter);
        DatabaseReference myChatRef = FirebaseDatabase.getInstance().getReference("MessengerChat").child(FirebaseAuth.getInstance().getUid()).child(uidOfReciever);
        myChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatData.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ChatData data = dataSnapshot1.getValue(ChatData.class);
                    chatData.add(data);
                    messagingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sendButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = enterMessageChat.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Please Enter Your Message!", Toast.LENGTH_LONG).show();
                } else {
                    enterMessageChat.setText("");
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("MessengerChat").child(FirebaseAuth.getInstance().getUid()).child(uidOfReciever);
                    ChatData chatData = new ChatData(AppData.getUserProfile().getName(), name, message, true, false, uidOfReciever);
                    myRef.push().setValue(chatData);
                    DatabaseReference recieverRef = FirebaseDatabase.getInstance().getReference("MessengerChat").child(uidOfReciever).child(FirebaseAuth.getInstance().getUid());
                    chatData = new ChatData(name, AppData.getUserProfile().getName(), message, false, true, FirebaseAuth.getInstance().getUid());
                    recieverRef.push().setValue(chatData);
                }
            }
        });

    }
}
