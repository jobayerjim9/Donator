package com.teamjhj.donator_247blood.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Activity.ChatActivity;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.LiveBloodRequest;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.NotificationAPI;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import in.shadowfax.proswipebutton.ProSwipeButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodRequestDialog extends DialogFragment {
    ProSwipeButton proSwipeBtn;
    private Context ctx;
    private String key;
    private UserProfile userProfile;
    private LiveBloodRequest liveBloodRequest;
    private TextView headingBloodRequest, reasonBloodReq, bloodGroupLargeRequest, bloodGroupSmallRequest, distanceTextRequest;
    private ImageView callButtonLiveRequest, mapButtonLiveRequest, messengerButtonLiveRequest;
    private ProgressBar distanceBarRequest;
    private Button closeBloodRequest;

    public BloodRequestDialog(String key) {
        this.key = key;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_fragment_blood_request, null);
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getContext(), "Check Internet Connectivity", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        DatabaseReference liveRequest = FirebaseDatabase.getInstance().getReference("LiveRequest").child(key);

        headingBloodRequest = v.findViewById(R.id.nameBloodRequest);
        closeBloodRequest = v.findViewById(R.id.closeBloodRequest);
        reasonBloodReq = v.findViewById(R.id.reasonBloodReq);
        bloodGroupLargeRequest = v.findViewById(R.id.bloodGroupLargeRequest);
        bloodGroupSmallRequest = v.findViewById(R.id.bloodGroupSmallRequest);
        proSwipeBtn = v.findViewById(R.id.accept_btn);
        distanceBarRequest = v.findViewById(R.id.distanceBarRequest);
        distanceTextRequest = v.findViewById(R.id.distanceTextRequest);
        closeBloodRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        callButtonLiveRequest = v.findViewById(R.id.callButtonLiveRequest);
        mapButtonLiveRequest = v.findViewById(R.id.mapButtonLiveRequest);
        messengerButtonLiveRequest = v.findViewById(R.id.messengerButtonLiveRequest);
        DatabaseReference myPrfoile=FirebaseDatabase.getInstance().getReference("UserProfile").child(FirebaseAuth.getInstance().getUid());
        myPrfoile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                if(userProfile!=null)
                {
                    AppData.setUserProfile(userProfile);
                    setBloodGroup(userProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("UserProfile").child(key);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(UserProfile.class);
                if (userProfile != null) {
                    String placeHolder = userProfile.getName();
                    headingBloodRequest.setText(placeHolder);
                    callButtonLiveRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri u = Uri.parse("tel:" + userProfile.getMobileNumber());
                            // Create the intent and set the data for the
                            // intent as the phone number.
                            Intent i = new Intent(Intent.ACTION_DIAL, u);

                            try {
                                ctx.startActivity(i);
                            } catch (SecurityException s) {

                                Toast.makeText(ctx, s.getLocalizedMessage(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                    messengerButtonLiveRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(ctx, ChatActivity.class);
                            i.putExtra("Name", userProfile.getName());
                            i.putExtra("uid", userProfile.getUid());
                            startActivity(i);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        liveRequest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    try {
                        Toast.makeText(ctx, "Request Already Closed", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        dismiss();
                    }
                }
                liveBloodRequest = dataSnapshot.getValue(LiveBloodRequest.class);
                if (liveBloodRequest != null) {
                    reasonBloodReq.setText(liveBloodRequest.getReason());
                    mapButtonLiveRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", liveBloodRequest.getLat(), liveBloodRequest.getLon());
                            Intent intent = new Intent(Intent.CATEGORY_APP_MAPS, Uri.parse(uri));
                            ctx.startActivity(intent);
                        }
                    });
                    proSwipeBtn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
                        @Override
                        public void onSwipeConfirm() {
                            // user has swiped the btn. Perform your async operation now
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // task success! show TICK icon in ProSwipeButton
                                    liveRequest.child("DonorsFound").child(FirebaseAuth.getInstance().getUid()).child("accepted").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                sendNotification();
                                                proSwipeBtn.showResultIcon(true); // false if task failed
                                            } else {
                                                proSwipeBtn.showResultIcon(false);
                                                Toast.makeText(ctx, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            }, 1000);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        liveRequest.child("DonorsFound").child(FirebaseAuth.getInstance().getUid()).child("accepted").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean accept = dataSnapshot.getValue(Boolean.class);
                if (accept != null) {
                    if (accept) {
                        Toast.makeText(ctx, "Already Responded!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        liveRequest.child("DonorsFound").child(FirebaseAuth.getInstance().getUid()).child("radius").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer radius = dataSnapshot.getValue(Integer.class);
                if (radius != null) {
                    distanceBarRequest.setProgress(radius);
                    String placeholder = "Within " + radius + " K.m";
                    distanceTextRequest.setText(placeholder);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        builder.setView(v);
        return builder.create();
    }

    private void sendNotification() {
        try {
            ApiInterface apiInterface = NotificationAPI.getClient().create(ApiInterface.class);
            String tempToken = AppData.getUserProfile().getToken();
            Log.e("MyToken", tempToken);
            String notificationMessage = AppData.getUserProfile().getName() + " Accepted Your Emergency Blood Request!";
            NotificationData notificationData = new NotificationData(notificationMessage, "Request Accepted!", "EmergencyRequest");
            Date date = Calendar.getInstance().getTime();
            notificationData.setDate(date);
            if (userProfile!=null)
            {
                NotificationSender notificationSender = new NotificationSender(userProfile.getToken(), notificationData);
               // NotificationSender notificationSender = new NotificationSender(tempToken, notificationData);
                DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Notifications").child(userProfile.getUid());

                notification.push().setValue(notificationData);
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
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBloodGroup(UserProfile userProfile) {
        String blood1, blood2;
        if (userProfile.getBloodGroup().contains("A+")) {
            blood1 = "A+";
            blood2 = "A Positive";
        } else if (userProfile.getBloodGroup().contains("A-")) {
            blood1 = "A-";
            blood2 = "A Negative";
        } else if (userProfile.getBloodGroup().contains("AB+")) {
            blood1 = "AB+";
            blood2 = "AB Positive";
        } else if (userProfile.getBloodGroup().contains("AB-")) {
            blood1 = "AB-";
            blood2 = "AB Negative";
        } else if (userProfile.getBloodGroup().contains("B+")) {
            blood1 = "B+";
            blood2 = "B Positive";
        } else if (userProfile.getBloodGroup().contains("B-")) {
            blood1 = "B-";
            blood2 = "B Negative";
        } else if (userProfile.getBloodGroup().contains("O+")) {
            blood1 = "O+";
            blood2 = "O Positive";
        } else if (userProfile.getBloodGroup().contains("O-")) {
            blood1 = "O-";
            blood2 = "O Negative";
        } else {
            blood1 = "";
            blood2 = "";
        }
        bloodGroupLargeRequest.setText(blood1);
        bloodGroupSmallRequest.setText(blood2);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }
}
