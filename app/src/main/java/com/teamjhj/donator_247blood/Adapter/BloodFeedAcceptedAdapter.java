package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.DataModel.AcceptingData;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;
import com.teamjhj.donator_247blood.DataModel.PendingDonationData;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.Fragment.BloodFeedFragment;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.ApiClient;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodFeedAcceptedAdapter extends RecyclerView.Adapter<BloodFeedAcceptedAdapter.BloodFeedAcceptedViewHolder> {

    private Context ctx;
    private ArrayList<UserProfile> userProfiles;
    private NonEmergencyInfo nonEmergencyInfo;
    private ArrayList<AcceptingData> acceptingData;

    public BloodFeedAcceptedAdapter(Context ctx, ArrayList<UserProfile> userProfiles, NonEmergencyInfo nonEmergencyInfo, ArrayList<AcceptingData> acceptingData) {
        this.acceptingData = acceptingData;
        this.ctx = ctx;
        this.userProfiles = userProfiles;
        this.nonEmergencyInfo = nonEmergencyInfo;

    }

    @NonNull
    @Override
    public BloodFeedAcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BloodFeedAcceptedViewHolder(LayoutInflater.from(ctx).inflate(R.layout.blood_feed_accepted_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BloodFeedAcceptedViewHolder holder, int position) {
        DatabaseReference post = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfo.getYear() + "").child(nonEmergencyInfo.getMonth() + "").child(nonEmergencyInfo.getDate() + "").child(nonEmergencyInfo.getKey());
        post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.nameOfDonnerAcceptedFeed.setText(userProfiles.get(position).getName());
        if (acceptingData.get(position).getRadius() != -1) {
            holder.distanceBarAcceptedFeed.setProgress(acceptingData.get(position).getRadius());
            String placeHolder = "Within " + acceptingData.get(position).getRadius() + " K.m";
            holder.distanceTextAcceptedFeed.setText(placeHolder);
        } else {
            holder.distanceBarAcceptedFeed.setVisibility(View.GONE);
            holder.distanceTextAcceptedFeed.setVisibility(View.GONE);
        }
        holder.callButtonAcceptedFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri u = Uri.parse("tel:" + userProfiles.get(position).getMobileNumber());

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
        holder.bloodRecievedButtonFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeRequest(position);


            }
        });
    }

    private void sendNotification(UserProfile userProfile) {
        try {
            Date date = Calendar.getInstance().getTime();

            DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Notifications").child(userProfile.getUid());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            //Log.e("UID", userProfile.getUid());
            //Log.e("Donor's Token",userProfile.getToken());
            //String tempToken = AppData.getUserProfile().getToken();
            //Log.e("MyToken", tempToken);
            String notificationMessage = AppData.getUserProfile().getName() + " Confirmed That He Received Blood From You" + "\nTap To Update";
            NotificationData notificationData = new NotificationData(notificationMessage, "Update Your History", "PendingHistory");


            notificationData.setDate(date);
            notification.push().setValue(notificationData);
            NotificationSender notificationSender = new NotificationSender(userProfile.getToken(), notificationData);
            //NotificationSender notificationSender = new NotificationSender(tempToken, notificationData);
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

    private void placeRequest(int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfo.getYear() + "").child(nonEmergencyInfo.getMonth() + "").child(nonEmergencyInfo.getDate() + "").child(nonEmergencyInfo.getKey()).child("AcceptedRequest").child(userProfiles.get(position).getUid());
        databaseReference.child("bloodRecieved").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference post = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfo.getYear() + "").child(nonEmergencyInfo.getMonth() + "").child(nonEmergencyInfo.getDate() + "").child(nonEmergencyInfo.getKey());
                    post.child("closed").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Date date = new Date();
                            date.setDate(nonEmergencyInfo.getDate());
                            date.setMonth(nonEmergencyInfo.getMonth());
                            date.setYear(nonEmergencyInfo.getYear());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PendingDonationConfirmation").child(userProfiles.get(position).getUid());
                            PendingDonationData pendingDonationData = new PendingDonationData(date, AppData.getUserProfile().getName());


                            databaseReference.push().setValue(pendingDonationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sendNotification(userProfiles.get(position));
                                    BloodFeedFragment.getDataFromDatabase();
                                    userProfiles.clear();
                                    notifyDataSetChanged();
                                    Toast.makeText(ctx, "Placed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class BloodFeedAcceptedViewHolder extends RecyclerView.ViewHolder {
        TextView nameOfDonnerAcceptedFeed, distanceTextAcceptedFeed;
        ImageView callButtonAcceptedFeed, messageDonorAcceptedFeed;
        Button bloodRecievedButtonFeed;
        ProgressBar distanceBarAcceptedFeed;

        public BloodFeedAcceptedViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfDonnerAcceptedFeed = itemView.findViewById(R.id.nameOfDonnerAcceptedFeed);
            distanceTextAcceptedFeed = itemView.findViewById(R.id.distanceTextAcceptedFeed);
            callButtonAcceptedFeed = itemView.findViewById(R.id.callButtonAcceptedFeed);
            messageDonorAcceptedFeed = itemView.findViewById(R.id.messageDonorAcceptedFeed);
            bloodRecievedButtonFeed = itemView.findViewById(R.id.bloodRecievedButtonFeed);
            distanceBarAcceptedFeed = itemView.findViewById(R.id.distanceBarAcceptedFeed);
        }
    }
}
