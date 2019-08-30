package com.teamjhj.donator_247blood.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import com.teamjhj.donator_247blood.Activity.ChatActivity;
import com.teamjhj.donator_247blood.DataModel.AcceptingData;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.LiveBloodRequest;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;
import com.teamjhj.donator_247blood.DataModel.PendingDonationData;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.ApiClient;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedDonorAdapter extends RecyclerView.Adapter<AcceptedDonorAdapter.AcceptedDonorViewHolder> {
    private Context ctx;
    private ArrayList<UserProfile> donnersData;
    private ArrayList<Integer> radius;

    public AcceptedDonorAdapter(Context ctx, ArrayList<UserProfile> donnersData, ArrayList<Integer> radius) {
        this.ctx = ctx;
        this.donnersData = donnersData;
        this.radius = radius;
    }

    @NonNull
    @Override
    public AcceptedDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AcceptedDonorViewHolder(LayoutInflater.from(ctx).inflate(R.layout.accepted_donor_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedDonorViewHolder holder, int position) {
        try {
            String placeHolder;
            holder.nameOfDonnerAccepted.setText(donnersData.get(position).getName());
            placeHolder = "Within " + radius.get(position) + " km";
            holder.distanceTextAccepted.setText(placeHolder);
            holder.distanceBarAccepted.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            holder.distanceBarAccepted.setProgress(radius.get(position));
            holder.messageDonorAccepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ctx, ChatActivity.class);
                    i.putExtra("Name", donnersData.get(position).getName());
                    i.putExtra("uid", donnersData.get(position).getUid());
                    ctx.startActivity(i);
                }
            });
            holder.bloodRecievedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(ctx)
                            .setTitle("Received Blood From " + donnersData.get(position).getName())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    moveToArchive(position);
                                    sendNotification(donnersData.get(position));
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                }
            });
//            holder.bloodGroupOfDonner.setText(donnersData.get(position).getBloodGroup());
//            holder.contactOfDonner.setText(donnersData.get(position).getMobileNumber());
//            placeHolder = donnersData.get(position).getLastDonationDate() + "-" + donnersData.get(position).getLastDonationMonth() + "-" + donnersData.get(position).getLastDonationYear();
//            holder.lastDonationDateDonner.setText(placeHolder);
//            placeHolder = "Around " + AppData.getRadiusList().get(position) + " km";
//            holder.distanceDonor.setText(placeHolder);
//            placeHolder = "Call " + donnersData.get(position).getName();
//            holder.callButton.setText(placeHolder);
//            getLocationData(position, holder);
//            try {
//                if (donnersData.get(position).getPrivacy().contains("Private")) {
//                    holder.callButtonAccepted.setVisibility(View.GONE);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            holder.callButtonAccepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri u = Uri.parse("tel:" + donnersData.get(position).getMobileNumber());

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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendNotification(UserProfile userProfile) {
        try {
            Date date = Calendar.getInstance().getTime();

            DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Notifications").child(userProfile.getUid());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Log.e("UID", userProfile.getUid());
            //Log.e("Donor's Token",userProfile.getToken());
           // String tempToken = AppData.getUserProfile().getToken();
           // Log.e("MyToken", tempToken);
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

    private void moveToArchive(int position) {
        DatabaseReference liveRequest = FirebaseDatabase.getInstance().getReference("LiveRequest").child(FirebaseAuth.getInstance().getUid());
        ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Placing Your Request!");
        progressDialog.show();
        liveRequest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LiveBloodRequest liveBloodRequest = dataSnapshot.getValue(LiveBloodRequest.class);
                if (liveBloodRequest != null) {
                    DatabaseReference archiveRef = FirebaseDatabase.getInstance().getReference("RequestArchive").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    String key = archiveRef.push().getKey();
                    archiveRef.child(key).setValue(liveBloodRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                liveRequest.child("DonorsFound").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        AcceptingData acceptingData = new AcceptingData();
                                        acceptingData.setAccepted(true);
                                        acceptingData.setBloodRecieved(true);
                                        archiveRef.child(key).child("AcceptedDonor").child(Objects.requireNonNull(donnersData.get(position).getUid())).setValue(acceptingData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PendingDonationConfirmation").child(donnersData.get(position).getUid());
                                                    PendingDonationData pendingDonationData = new PendingDonationData(liveBloodRequest.getDate(), AppData.getUserProfile().getName());


                                                    databaseReference.push().setValue(pendingDonationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            liveRequest.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        progressDialog.dismiss();
                                                                        if (task.isSuccessful()) {

                                                                            Toast.makeText(ctx, "Placed Request For Donor Confirmation", Toast.LENGTH_LONG).show();
                                                                        } else {
                                                                            Toast.makeText(ctx, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                        //Toast.makeText(getContext(), "Request Cancelled Successfully", Toast.LENGTH_LONG).show();
                                                                    } else {
                                                                        Toast.makeText(ctx, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });


                                                }
                                            }
                                        });


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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public int getItemCount() {
        return donnersData.size();
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + ", " + obj.getLocality() + "-" + obj.getPostalCode();


            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();
            return add;
            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return "Error Loading location!";
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    class AcceptedDonorViewHolder extends RecyclerView.ViewHolder {
        TextView nameOfDonnerAccepted, distanceTextAccepted;
        ProgressBar distanceBarAccepted;
        ImageView callButtonAccepted, messageDonorAccepted;
        Button bloodRecievedButton;
        AcceptedDonorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfDonnerAccepted = itemView.findViewById(R.id.nameOfDonnerAccepted);
            distanceTextAccepted = itemView.findViewById(R.id.distanceTextAccepted);
            distanceBarAccepted = itemView.findViewById(R.id.distanceBarAccepted);
            callButtonAccepted = itemView.findViewById(R.id.callButtonAccepted);
            messageDonorAccepted = itemView.findViewById(R.id.messageDonorAccepted);
            bloodRecievedButton = itemView.findViewById(R.id.bloodRecievedButton);

        }
    }
}
