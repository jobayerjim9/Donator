package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.Fragment.BloodFeedFragment;
import com.teamjhj.donator_247blood.Fragment.CommentBottomSheetFragment;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Objects;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private DatabaseReference postReference;
    private Context ctx;
    private ArrayList<NonEmergencyInfo> nonEmergencyInfos;

    public RequestAdapter(Context ctx, ArrayList<NonEmergencyInfo> nonEmergencyInfos) {
        this.ctx = ctx;
        this.nonEmergencyInfos = nonEmergencyInfos;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestViewHolder(LayoutInflater.from(ctx).inflate(R.layout.blood_feed_card, parent, false));
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
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeThePost(position);
                notifyDataSetChanged();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                unlikeThePost(position);
            }
        });
        holder.commentBloodFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = ((AppCompatActivity) ctx).getSupportFragmentManager();
                CommentBottomSheetFragment comments = new CommentBottomSheetFragment(nonEmergencyInfos.get(position));
                comments.show(manager, "Comments");
            }
        });
        String placeHolder = nonEmergencyInfos.get(position).getName();
        holder.nameBloodFeed.setText(placeHolder);
        placeHolder = nonEmergencyInfos.get(position).getReason();
        holder.reasonBloodFeed.setText(placeHolder);
        placeHolder = " " + nonEmergencyInfos.get(position).getDate() + "-" + nonEmergencyInfos.get(position).getMonth() + "-" + nonEmergencyInfos.get(position).getYear();
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
            placeHolder = " " + nonEmergencyInfos.get(position).getBags();

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
    }

    private void unlikeThePost(int position) {
        postReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfos.get(position).getYear() + "").child(nonEmergencyInfos.get(position).getMonth() + "").child(nonEmergencyInfos.get(position).getDate() + "").child(nonEmergencyInfos.get(position).getKey()).child("LikedPeople").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        postReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    BloodFeedFragment.getDataFromDatabase();
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
                    BloodFeedFragment.getDataFromDatabase();
                    Toast.makeText(ctx, "This Post Saved To Your Profile Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ctx, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        try {
            return nonEmergencyInfos.size();
        } catch (Exception e) {
            return 0;
        }
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView nameBloodFeed, reasonBloodFeed, dateBloodFeed, bagBloodFeed, bloodGroupLarge, bloodGroupSmall;
        LikeButton likeButton;
        ImageView commentBloodFeed, callBloodFeed, mapBloodFeed;

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

        }
    }
}
