package com.teamjhj.donator_247blood.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.teamjhj.donator_247blood.Activity.ChatActivity;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.Fragment.CommentBottomSheetFragment;
import com.teamjhj.donator_247blood.Fragment.SuccessfulDialog;
import com.teamjhj.donator_247blood.Fragment.UnsuccessfulDialog;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.ApiClient;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import in.shadowfax.proswipebutton.ProSwipeButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private DatabaseReference postReference;
    private Context ctx;
    private ArrayList<NonEmergencyInfo> nonEmergencyInfos;
    FragmentManager manager;
    int distanceFromUser = -1;
    double longitude,latitude;
    public RequestAdapter(Context ctx, ArrayList<NonEmergencyInfo> nonEmergencyInfos) {
        this.ctx = ctx;
        this.nonEmergencyInfos = nonEmergencyInfos;
        manager = ((AppCompatActivity) ctx).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestViewHolder(LayoutInflater.from(ctx).inflate(R.layout.blood_feed_card, parent, false));
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public int getItemCount() {
        try {
            return nonEmergencyInfos.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestViewHolder holder, final int position) {
        postReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfos.get(position).getYear() + "").child(nonEmergencyInfos.get(position).getMonth() + "").child(nonEmergencyInfos.get(position).getDate() + "").child(nonEmergencyInfos.get(position).getKey()).child("LikedPeople").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //nonEmergencyInfos.get(position).setLiked(true);
                    holder.likeButton.setLiked(true);
                } else {
                    holder.likeButton.setLiked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference nonEmergencyLive = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfos.get(position).getYear() + "").child(nonEmergencyInfos.get(position).getMonth() + "").child(nonEmergencyInfos.get(position).getDate() + "").child(nonEmergencyInfos.get(position).getKey()).child("AcceptedRequest").child(FirebaseAuth.getInstance().getUid());
        nonEmergencyLive.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    holder.accept_btn_blood_feed.setVisibility(View.GONE);
                    holder.acceptedText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try {
            //  int distanceFromUser=Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(ctx).getString("longitude", "defaultStringIfNothingFound"));
            SharedPreferences pref = ((AppCompatActivity) ctx).getPreferences(Context.MODE_PRIVATE);
            longitude = Double.parseDouble(pref.getString("longitude", null));
            latitude = Double.parseDouble(pref.getString("latitude", null));
            Log.e("SharedPref", longitude + "");
            distanceFromUser = (int) distance(latitude, longitude, nonEmergencyInfos.get(position).getLat(), nonEmergencyInfos.get(position).getLongt());

            holder.bloodFeedDistanceBar.setProgress(distanceFromUser);
            String placeHolder = "Within " + distanceFromUser + " K.m";
            holder.bloodFeedDistanceText.setText(placeHolder);
            holder.bloodFeedDistanceBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        } catch (Exception e) {
            holder.bloodFeedDistanceBar.setVisibility(View.GONE);
            holder.bloodFeedDistanceText.setVisibility(View.GONE);
            e.printStackTrace();
        }
        holder.accept_btn_blood_feed.setSwipeDistance((float) 0.2);
        holder.accept_btn_blood_feed.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {


            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. Perform your async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // task success! show TICK icon in ProSwipeButton

                        new AlertDialog.Builder(ctx)
                                .setTitle("Opt In?")
                                .setCancelable(false)
                                .setMessage("Are you sure you want to Opt In?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        nonEmergencyLive.child("accepted").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                distanceFromUser = (int) distance(latitude, longitude, nonEmergencyInfos.get(position).getLat(), nonEmergencyInfos.get(position).getLongt());
                                                Log.d("DistanceFromUser",distanceFromUser+"");
                                                nonEmergencyLive.child("radius").setValue(distanceFromUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            sendNotification(position);
                                                            holder.accept_btn_blood_feed.setVisibility(View.GONE);
                                                            holder.acceptedText.setVisibility(View.VISIBLE);
                                                            holder.acceptedText.setText("Opt In Successful");
                                                            holder.accept_btn_blood_feed.showResultIcon(true);

                                                            SuccessfulDialog successfulDialog=new SuccessfulDialog("Opt In Successful");
                                                            successfulDialog.show(manager,"SuccessfulDialog");

                                                        } else {
                                                            UnsuccessfulDialog unsuccessfulDialog=new UnsuccessfulDialog(task.getException().getLocalizedMessage());
                                                            unsuccessfulDialog.show(manager,"UnsuccessfulDialog");

                                                            holder.accept_btn_blood_feed.showResultIcon(false,true);
                                                        }
                                                    }
                                                });

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        holder.accept_btn_blood_feed.showResultIcon(false,true);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        // false if task failed


                    }
                }, 1000);
            }
        });

        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeThePost(position);
                //notifyDataSetChanged();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                unlikeThePost(position);
            }
        });
        holder.commentBloodFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentBottomSheetFragment comments = new CommentBottomSheetFragment(nonEmergencyInfos.get(position));
                comments.show(manager, "Comments");
            }
        });
        String placeHolder = nonEmergencyInfos.get(position).getName();
        holder.nameBloodFeed.setText(placeHolder);
        placeHolder = nonEmergencyInfos.get(position).getReason();
        holder.reasonBloodFeed.setText(placeHolder);
        placeHolder = nonEmergencyInfos.get(position).getDate() + "-" + nonEmergencyInfos.get(position).getMonth() + "-" + nonEmergencyInfos.get(position).getYear();
        holder.dateBloodFeed.setText(placeHolder);
        holder.callBloodFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri u = Uri.parse("tel:" + nonEmergencyInfos.get(position).getPhone());

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

        holder.mapBloodFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String parse = "geo:" + nonEmergencyInfos.get(position).getLat() + "," + nonEmergencyInfos.get(position).getLongt() + "?q=" + nonEmergencyInfos.get(position).getLat() + "," + nonEmergencyInfos.get(position).getLongt() + nonEmergencyInfos.get(position).getLocation();
                Uri gmmIntentUri = Uri.parse(parse);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(ctx.getPackageManager()) != null) {
                    ctx.startActivity(mapIntent);
                }
            }
        });
        String blood1, blood2;
        if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("A+")) {
            blood1 = "A+";
            blood2 = "A Positive";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("A-")) {
            blood1 = "A-";
            blood2 = "A Negative";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("AB+")) {
            blood1 = "AB+";
            blood2 = "AB Positive";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("AB-")) {
            blood1 = "AB-";
            blood2 = "AB Negative";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("B+")) {
            blood1 = "B+";
            blood2 = "B Positive";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("B-")) {
            blood1 = "B-";
            blood2 = "B Negative";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("O+")) {
            blood1 = "O+";
            blood2 = "O Positive";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("O-")) {
            blood1 = "O-";
            blood2 = "O Negative";
        } else {
            blood1 = "";
            blood2 = "";
        }

        holder.bloodGroupLarge.setText(blood1);
        holder.bloodGroupSmall.setText(blood2);
        try {
            placeHolder = nonEmergencyInfos.get(position).getBags();

            holder.bagBloodFeed.setText(placeHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        holder.nameRequest.setText(nonEmergencyInfos.get(position).getName());
//        holder.bloodRequest.setText(nonEmergencyInfos.get(position).getSelectedBloodGroup());
//        holder.mobileRequest.setText(nonEmergencyInfos.get(position).getPhone());
//        holder.locationRequest.setText(nonEmergencyInfos.get(position).getLocation());
//        String placeHolder = nonEmergencyInfos.get(position).getDate() + "-" + nonEmergencyInfos.get(position).getMonth() + "-" + nonEmergencyInfos.get(position).getYear();
//        holder.dateRequest.setText(placeHolder);
//        int hour = nonEmergencyInfos.get(position).getHour();
//        String amPm;
//        if (hour > 12) {
//            hour = hour - 12;
//            amPm = " PM";
//
//        } else {
//            amPm = " AM";
//        }
//        placeHolder = hour + " : " + nonEmergencyInfos.get(position).getMinute() + amPm;
//        holder.timeRequest.setText(placeHolder);
//        holder.reasonRequest.setText(nonEmergencyInfos.get(position).getReason());
//
//
//        holder.responseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (holder.likeButton.isLiked()) {
//                    unlikeThePost(position);
//                    holder.likeButton.setLiked(false);
//                } else {
//                    likeThePost(position);
//                    holder.likeButton.setLiked(true);
//                }
//            }
//        });
//
//
//        holder.popupMenuPosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu=new PopupMenu(ctx,holder.popupMenuPosts);
//                popupMenu.inflate(R.menu.posts_menu);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        if(menuItem.getItemId()==R.id.callDonor)
//                        {
//                            Uri u = Uri.parse("tel:" + nonEmergencyInfos.get(position).getPhone());
//
//                            // Create the intent and set the data for the
//                            // intent as the phone number.
//                            Intent i = new Intent(Intent.ACTION_DIAL, u);
//
//                            try {
//                                ctx.startActivity(i);
//                            } catch (SecurityException s) {
//
//                                Toast.makeText(ctx, s.getLocalizedMessage(), Toast.LENGTH_LONG)
//                                        .show();
//                            }
//                        }
//                        else if(menuItem.getItemId()==R.id.viewOnMap)
//                        {
//                            String parse = "geo:" + nonEmergencyInfos.get(position).getLat() + "," + nonEmergencyInfos.get(position).getLongt() + "?q=" + nonEmergencyInfos.get(position).getLat() + "," + nonEmergencyInfos.get(position).getLongt() + nonEmergencyInfos.get(position).getLocation();
//                            Uri gmmIntentUri = Uri.parse(parse);
//                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                            mapIntent.setPackage("com.google.android.apps.maps");
//                            if (mapIntent.resolveActivity(ctx.getPackageManager()) != null) {
//                                ctx.startActivity(mapIntent);
//                            }
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
        holder.messengerBloodFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DatabaseReference setViewed = FirebaseDatabase.getInstance().getReference("MessengerChat").child(FirebaseAuth.getInstance().getUid()).child(chatData.get(position).getUid()).child(chatData.get(position).getMessegingKey());
                //setViewed.child("viewed").setValue(true);
                Intent i = new Intent(ctx, ChatActivity.class);
                i.putExtra("Name", nonEmergencyInfos.get(position).getName());
                i.putExtra("uid", nonEmergencyInfos.get(position).getUid());
                ctx.startActivity(i);
            }
        });
    }

    private void unlikeThePost(int position) {
        postReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfos.get(position).getYear() + "").child(nonEmergencyInfos.get(position).getMonth() + "").child(nonEmergencyInfos.get(position).getDate() + "").child(nonEmergencyInfos.get(position).getKey()).child("LikedPeople").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        postReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // BloodFeedFragment.getDataFromDatabase();
                    Toast.makeText(ctx, "This Post Removed From Your Profile Successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void likeThePost(int position) {
        postReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfos.get(position).getYear() + "").child(nonEmergencyInfos.get(position).getMonth() + "").child(nonEmergencyInfos.get(position).getDate() + "").child(nonEmergencyInfos.get(position).getKey()).child("LikedPeople").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        postReference.child("Liked").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //BloodFeedFragment.getDataFromDatabase();
                    SuccessfulDialog successfulDialog=new SuccessfulDialog("This Post Saved To Your Profile Successfully");
                    successfulDialog.show(manager,"SuccessfulDialog");

                } else {
                    UnsuccessfulDialog unsuccessfulDialog=new UnsuccessfulDialog("Check Your Internet Connection");
                    unsuccessfulDialog.show(manager,"UnsuccessfulDialog");
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void sendNotification(int position) {
        try {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            String tempToken = AppData.getUserProfile().getToken();
            Log.e("MyToken", tempToken);
            String notificationMessage = AppData.getUserProfile().getName() + " Opt In On Your Blood Feed Post!";
            NotificationData notificationData = new NotificationData(notificationMessage, "Request Accepted!", "BloodFeedReq");
            Date date = Calendar.getInstance().getTime();
            notificationData.setDate(date);
            DatabaseReference database=FirebaseDatabase.getInstance().getReference("UserProfile").child(nonEmergencyInfos.get(position).getUid());
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                    if(userProfile!=null)
                    {
                        NotificationSender notificationSender = new NotificationSender(userProfile.getToken(), notificationData);
                        //NotificationSender notificationSender = new NotificationSender(tempToken, notificationData);
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView nameBloodFeed, reasonBloodFeed, dateBloodFeed, bagBloodFeed, bloodGroupLarge, bloodGroupSmall, bloodFeedDistanceText, acceptedText;
        LikeButton likeButton;
        ImageView commentBloodFeed, callBloodFeed, mapBloodFeed, messengerBloodFeed;
        ProgressBar bloodFeedDistanceBar;
        ProSwipeButton accept_btn_blood_feed;
        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentBloodFeed = itemView.findViewById(R.id.commentBloodFeed);
            nameBloodFeed = itemView.findViewById(R.id.nameBloodFeed);
            reasonBloodFeed = itemView.findViewById(R.id.reasonBloodFeed);
            dateBloodFeed = itemView.findViewById(R.id.dateBloodFeed);
            bagBloodFeed = itemView.findViewById(R.id.bagBloodFeed);
            callBloodFeed = itemView.findViewById(R.id.callBloodFeed);
            mapBloodFeed = itemView.findViewById(R.id.mapBloodFeed);
            bloodGroupLarge = itemView.findViewById(R.id.bloodGroupLarge);
            bloodGroupSmall = itemView.findViewById(R.id.bloodGroupSmall);
            bloodFeedDistanceBar = itemView.findViewById(R.id.bloodFeedDistanceBar);
            bloodFeedDistanceText = itemView.findViewById(R.id.bloodFeedDistanceText);
            messengerBloodFeed = itemView.findViewById(R.id.messengerBloodFeed);
            accept_btn_blood_feed = itemView.findViewById(R.id.accept_btn_blood_feed);
            acceptedText = itemView.findViewById(R.id.acceptedText);

        }
    }
}
