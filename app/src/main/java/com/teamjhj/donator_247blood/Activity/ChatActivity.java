package com.teamjhj.donator_247blood.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.teamjhj.donator_247blood.Adapter.MessagingAdapter;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.ChatData;
import com.teamjhj.donator_247blood.DataModel.MessengerInfoData;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.NotificationAPI;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecycler;
    private MessagingAdapter messagingAdapter;
    private ArrayList<ChatData> chatData = new ArrayList<>();
    private String message, placeHolder = " ";
    private TextView statusChat, no_chat_Label;
    private LottieAnimationView no_chat;
    private ShimmerFrameLayout chatShimmer;
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
        chatShimmer = findViewById(R.id.chatShimmer);
        chatShimmer.startShimmer();
        no_chat_Label = findViewById(R.id.no_chat_Label);
        no_chat = findViewById(R.id.no_chat);
        statusChat = findViewById(R.id.statusChat);

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
                    chatRecycler.scrollToPosition(chatData.size() - 1);
                }
                Collections.sort(chatData, new Comparator<ChatData>() {
                    @Override
                    public int compare(ChatData chatData, ChatData t1) {
                        return chatData.getMessageTime().compareTo(t1.getMessageTime());
                    }
                });
                //Collections.reverse(chatData);
                chatShimmer.stopShimmer();
                chatShimmer.setVisibility(View.GONE);
                messagingAdapter.notifyDataSetChanged();
                if (chatData.isEmpty()) {
                    no_chat.setVisibility(View.VISIBLE);
                    no_chat_Label.setVisibility(View.VISIBLE);
                } else {
                    no_chat.setVisibility(View.GONE);
                    no_chat_Label.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sendButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = enterMessageChat.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Enter Your Message First!", Toast.LENGTH_LONG).show();
                } else {
                    Date date = Calendar.getInstance().getTime();
                    enterMessageChat.setText("");
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("MessengerChat").child(FirebaseAuth.getInstance().getUid()).child(uidOfReciever);
                    ChatData chatData = new ChatData(AppData.getUserProfile().getName(), name, message, true, false, uidOfReciever, date, true, FirebaseAuth.getInstance().getUid());
                    myRef.push().setValue(chatData);
                    DatabaseReference recieverRef = FirebaseDatabase.getInstance().getReference("MessengerChat").child(uidOfReciever).child(FirebaseAuth.getInstance().getUid());
                    chatData = new ChatData(name, AppData.getUserProfile().getName(), message, false, true, FirebaseAuth.getInstance().getUid(), date, false, FirebaseAuth.getInstance().getUid());
                    recieverRef.push().setValue(chatData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference recieverProfile = FirebaseDatabase.getInstance().getReference("UserProfile").child(uidOfReciever);
                                recieverProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                                        if (userProfile != null) {
                                            sendNotification(userProfile);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    });
                }
            }
        });

        DatabaseReference chatPerson = FirebaseDatabase.getInstance().getReference("MessengerChat").child(uidOfReciever);
        chatPerson.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MessengerInfoData messengerInfoData = dataSnapshot.getValue(MessengerInfoData.class);
                if (messengerInfoData != null) {
                    try {
                        Date d1 = Calendar.getInstance().getTime();
                        Date d2 = messengerInfoData.getDate();
                        Log.d("Current Time", d1.getTime() + "");
                        long diff = d1.getTime() - d2.getTime();
                        long diffSeconds = diff / 1000 % 60;
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        Log.d("Time", diffDays + " " + diffHours);
                        placeHolder = "";
                        if (diffMinutes < 3 && diffHours == 0 && diffDays == 0) {
                            placeHolder = "Online";
                        } else if (diffHours == 0 && diffDays == 0) {
                            placeHolder = diffMinutes + " Minutes Ago!";
                        } else if (diffHours > 0 && diffDays == 0) {
                            placeHolder = diffHours + " Hours Ago!";
                        } else if (diffDays > 0) {
                            placeHolder = diffDays + " Days Ago!";
                        }
                        statusChat.setText(placeHolder);
                    } catch (Exception e) {
                        statusChat.setText("");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(UserProfile userProfile) {
        try {
            //   notification = FirebaseDatabase.getInstance().getReference("Notifications").child(FirebaseAuth.getInstance().getUid());


            ApiInterface apiInterface = NotificationAPI.getClient().create(ApiInterface.class);
            Log.e("UID", userProfile.getUid());
            //Log.e("Donor's Token",userProfile.getToken());
            String tempToken = AppData.getUserProfile().getToken();
            Log.e("MyToken", tempToken);
            //String notificationMessage = message;
            NotificationData notificationData = new NotificationData(message, AppData.getUserProfile().getName(), "Messenger");
            Date date = Calendar.getInstance().getTime();
            notificationData.setDate(date);
            // notification.push().setValue(notificationData);
            NotificationSender notificationSender = new NotificationSender(userProfile.getToken(), notificationData);
           // NotificationSender notificationSender = new NotificationSender(tempToken, notificationData);
            Call<ResponseBody> bodyCall = apiInterface.sendNotification(notificationSender);
            bodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("Response Code", response.code() + "");
                    Log.e("Error MEssage", response.message());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
