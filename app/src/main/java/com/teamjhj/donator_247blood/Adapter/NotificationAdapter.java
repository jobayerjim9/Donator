package com.teamjhj.donator_247blood.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Activity.MyRequestActivity;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.Fragment.BloodRequestDialog;
import com.teamjhj.donator_247blood.Fragment.CommentBottomSheetFragment;
import com.teamjhj.donator_247blood.Fragment.ViewPendingHistoryDialog;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context ctx;
    private ArrayList<NotificationData> notificationData;
    private ProgressDialog dialog;

    public NotificationAdapter(Context ctx, ArrayList<NotificationData> notificationData) {
        this.ctx = ctx;
        this.notificationData = notificationData;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(ctx).inflate(R.layout.notification_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        holder.headlineNotification.setText(notificationData.get(position).getTitle());
        holder.notificationDescription.setText(notificationData.get(position).getBody());

        holder.notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Notifications").child(FirebaseAuth.getInstance().getUid()).child(notificationData.get(position).getKey());
                notification.child("clicked").setValue(true);
                notificationData.get(position).setClicked(true);
                if (notificationData.get(position).getTag().contentEquals(FirebaseAuth.getInstance().getUid())) {
                    String tag = notificationData.get(position).getTag();


                    FragmentManager manager = ((AppCompatActivity) ctx).getSupportFragmentManager();
                    BloodRequestDialog bloodRequestDialog = new BloodRequestDialog(tag);
                    bloodRequestDialog.setCancelable(false);
                    bloodRequestDialog.show(manager, tag);
                        //notifyDataSetChanged();

                        //holder.notificationCard.setCardBackgroundColor(Color.WHITE);


                } else if (notificationData.get(position).getTag().contains("Comments")) {
                    dialog = new ProgressDialog(ctx);
                    dialog.setCancelable(true);
                    dialog.setMessage("Loading Comments!");
                    dialog.show();

                    loadComments(position);
                } else if (notificationData.get(position).getTag().contains("EmergencyRequest")) {
                    ctx.startActivity(new Intent(ctx, MyRequestActivity.class));
                } else if (notificationData.get(position).getTag().contains("BloodFeedReq")) {
                    Intent i = new Intent(ctx, MyRequestActivity.class);
                    i.putExtra("tabChange", true);
                    ctx.startActivity(i);
                } else if (notificationData.get(position).getTag().contains("PendingHistory")) {
                    FragmentManager manager = ((AppCompatActivity) ctx).getSupportFragmentManager();
                    ViewPendingHistoryDialog viewPendingHistoryDialog = new ViewPendingHistoryDialog();
                    viewPendingHistoryDialog.show(manager, "PendingHistory");
                }

            }
        });

        if (notificationData.get(position).isClicked()) {
            holder.notificationCard.setCardBackgroundColor(Color.WHITE);
        }
        try {
            Date d1 = Calendar.getInstance().getTime();
            Date d2 = notificationData.get(position).getDate();
            Log.d("Current Time", d1.getTime() + "");
            long diff = d1.getTime() - d2.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            Log.d("Time", diffDays + " " + diffHours);
            String placeHolder = "";
            if (diffMinutes < 1) {
                placeHolder = "Just Now!";
            } else if (diffMinutes > 0 && diffHours == 0) {
                placeHolder = diffMinutes + " Minutes Ago!";
            } else if (diffHours > 0 && diffDays == 0) {
                placeHolder = diffHours + " Hours Ago!";
            } else if (diffDays > 0) {
                placeHolder = diffDays + " Days Ago!";
            }
            holder.timeNotification.setText(placeHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadComments(int position) {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(notificationData.get(position).getYear() + "").child(notificationData.get(position).getMonth() + "").child(notificationData.get(position).getDay() + "").child(notificationData.get(position).getPostKey());
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NonEmergencyInfo nonEmergencyInfo = dataSnapshot.getValue(NonEmergencyInfo.class);
                dialog.dismiss();
                if (nonEmergencyInfo != null) {
                    nonEmergencyInfo.setKey(notificationData.get(position).getPostKey());
                    Log.e("Check Null", nonEmergencyInfo.getReason());
                    FragmentManager manager = ((AppCompatActivity) ctx).getSupportFragmentManager();
                    CommentBottomSheetFragment comments = new CommentBottomSheetFragment(nonEmergencyInfo);
                    comments.show(manager, "Comments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView headlineNotification, notificationDescription, timeNotification;
        CardView notificationCard;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            headlineNotification = itemView.findViewById(R.id.headlineNotification);
            notificationDescription = itemView.findViewById(R.id.notificationDescription);
            notificationCard = itemView.findViewById(R.id.notificationCard);
            timeNotification = itemView.findViewById(R.id.timeNotification);
        }
    }
}
