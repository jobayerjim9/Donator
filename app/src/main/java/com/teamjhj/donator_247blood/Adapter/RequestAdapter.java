package com.teamjhj.donator_247blood.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.teamjhj.donator_247blood.Activity.ChatActivity;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.DistanceApiData;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.Fragment.CommentBottomSheetFragment;
import com.teamjhj.donator_247blood.Fragment.SuccessfulDialog;
import com.teamjhj.donator_247blood.Fragment.UnsuccessfulDialog;
import com.teamjhj.donator_247blood.Fragment.ViewProfileBottomSheet;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.GoogleDistanceAPI;
import com.teamjhj.donator_247blood.RestApi.NotificationAPI;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private FusedLocationProviderClient fusedLocationProviderClient;
    FragmentManager manager;
    double distanceFromUser=-1;
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
        try {
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
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        new Boom(holder.callBloodFeed);
        new Boom(holder.messengerBloodFeed);
        new Boom(holder.commentBloodFeed);
        new Boom(holder.mapBloodFeed);
        GoogleMap thisMap = holder.mapCurrent;
        if(thisMap != null) {
            LatLng location=new LatLng(nonEmergencyInfos.get(position).getLat(),nonEmergencyInfos.get(position).getLongt());
            thisMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
            thisMap.addMarker(new MarkerOptions().position(location));

            // Set the map type back to normal.
            thisMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }
        holder.shareButtonBloodFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.shareMessage.setVisibility(View.VISIBLE);
                Bitmap image=getBitmapFromView(holder.bloodFeedCard);
                holder.shareMessage.setVisibility(View.GONE);
                //image=addWaterMark(image);
                Uri shareUri=saveImageExternal(image);
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, shareUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/png");
                ctx.startActivity(intent);
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

        DatabaseReference totalCount = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfos.get(position).getYear() + "").child(nonEmergencyInfos.get(position).getMonth() + "").child(nonEmergencyInfos.get(position).getDate() + "").child(nonEmergencyInfos.get(position).getKey()).child("AcceptedRequest");
        totalCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0)
                {
                    String placeHolder=dataSnapshot.getChildrenCount()+" People Respond";
                    holder.totalRespondTextView.setText(placeHolder);
                }
                else
                {
                    holder.totalRespondTextView.setText("0 People Respond!");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        distanceFromUser =nonEmergencyInfos.get(position).getDistamceFromUser();
            //  int distanceFromUser=Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(ctx).getString("longitude", "defaultStringIfNothingFound"));
        nonEmergencyInfos.get(position).setDistamceFromUser(distanceFromUser);

        holder.bloodFeedDistanceBar.setProgress((int)distanceFromUser);
        String placeHolder = "Within " + (int)distanceFromUser+" K.m ";
        holder.bloodFeedDistanceText.setText(placeHolder);
        holder.bloodFeedDistanceBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        holder.accept_btn_blood_feed.setSwipeDistance((float) 0.2);
        holder.accept_btn_blood_feed.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {


            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. Perform your async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // task success! show TICK icon in ProSwipeButton
                        Log.d("DistanceFromUser", nonEmergencyInfos.get(position).getDistamceFromUser() + "");
                        new AlertDialog.Builder(ctx)
                                .setTitle("Opt In?")
                                .setCancelable(false)
                                .setMessage("Are you sure you want to Opt In?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        nonEmergencyLive.child("accepted").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                    //Log.d("DistanceFromUser", distanceFromUser + "");
                                                    nonEmergencyLive.child("radius").setValue(nonEmergencyInfos.get(position).getDistamceFromUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                sendNotification(position);
                                                                holder.accept_btn_blood_feed.setVisibility(View.GONE);
                                                                holder.acceptedText.setVisibility(View.VISIBLE);
                                                                holder.acceptedText.setText("Opt In Successful");
                                                                holder.accept_btn_blood_feed.showResultIcon(true);

                                                                SuccessfulDialog successfulDialog = new SuccessfulDialog("Opt In Successful");
                                                                successfulDialog.show(manager, "SuccessfulDialog");

                                                            } else {
                                                                UnsuccessfulDialog unsuccessfulDialog = new UnsuccessfulDialog(task.getException().getLocalizedMessage());
                                                                unsuccessfulDialog.show(manager, "UnsuccessfulDialog");

                                                                holder.accept_btn_blood_feed.showResultIcon(false, true);
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
                Log.e("PostId",nonEmergencyInfos.get(position).getKey());
                CommentBottomSheetFragment comments = new CommentBottomSheetFragment(nonEmergencyInfos.get(position));
                comments.show(manager, "Comments");
            }
        });
        placeHolder = nonEmergencyInfos.get(position).getName();
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
//                String parse = "geo:" + nonEmergencyInfos.get(position).getLat() + "," + nonEmergencyInfos.get(position).getLongt() + "?q=" + nonEmergencyInfos.get(position).getLat() + "," + nonEmergencyInfos.get(position).getLongt() + nonEmergencyInfos.get(position).getLocation();
////                Uri gmmIntentUri = Uri.parse(parse);
////                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
////                mapIntent.setPackage("com.google.android.apps.maps");
////                if (mapIntent.resolveActivity(ctx.getPackageManager()) != null) {
////                    ctx.startActivity(mapIntent);
////                }
                ViewProfileBottomSheet viewProfileBottomSheet=new ViewProfileBottomSheet(nonEmergencyInfos.get(position).getUid());
                viewProfileBottomSheet.show(manager,"ViewProfile");
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
            ApiInterface apiInterface = NotificationAPI.getClient().create(ApiInterface.class);
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

    class RequestViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        TextView shareMessage,totalRespondTextView,nameBloodFeed, reasonBloodFeed, dateBloodFeed, bagBloodFeed, bloodGroupLarge, bloodGroupSmall, bloodFeedDistanceText, acceptedText;
        LikeButton likeButton;
        ImageView commentBloodFeed, callBloodFeed, mapBloodFeed, messengerBloodFeed,shareButtonBloodFeed;
        ProgressBar bloodFeedDistanceBar;
        ProSwipeButton accept_btn_blood_feed;
        MapView bloodFeedMap;
        GoogleMap mapCurrent;
        CardView bloodFeedCard;
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
            shareButtonBloodFeed = itemView.findViewById(R.id.shareButtonBloodFeed);
            bloodFeedCard = itemView.findViewById(R.id.bloodFeedCard);
            totalRespondTextView = itemView.findViewById(R.id.totalRespondTextView);
            shareMessage = itemView.findViewById(R.id.shareMessage);
            bloodFeedMap =(MapView) itemView.findViewById(R.id.bloodFeedMap);
            if (bloodFeedMap != null) {
                // Initialise the MapView
                bloodFeedMap.onCreate(null);
                bloodFeedMap.onResume();
                // Set the map ready callback to receive the GoogleMap object
                bloodFeedMap.getMapAsync(this);
            }

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(ctx);
            mapCurrent = googleMap;
            notifyDataSetChanged();

        }
    }
    @Override
    public void onViewRecycled(RequestViewHolder holder)
    {
        // Cleanup MapView here?
        if (holder.mapCurrent != null)
        {
            holder.mapCurrent.clear();
            holder.mapCurrent.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }
    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }
    private Uri saveImageExternal(Bitmap image) {
        //TODO - Should be processed in another thread
        Uri uri = null;
        try {
            File file = new File(ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            Log.d("Tag", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }
    private Bitmap addWaterMark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Bitmap waterMark = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logo_red);
        canvas.drawBitmap(waterMark, 0, 0, null);

        return result;
    }

}
