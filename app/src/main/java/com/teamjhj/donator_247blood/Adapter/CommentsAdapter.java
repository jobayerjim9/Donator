package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.teamjhj.donator_247blood.DataModel.CommentsData;
import com.teamjhj.donator_247blood.Fragment.ViewProfileBottomSheet;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    private Context ctx;
    private ArrayList<CommentsData> commentsData;
    private FragmentManager manager;
    public CommentsAdapter(Context ctx, ArrayList<CommentsData> commentsData) {
        this.ctx = ctx;
        this.commentsData = commentsData;
        manager = ((AppCompatActivity) ctx).getSupportFragmentManager();
    }


    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentsViewHolder(LayoutInflater.from(ctx).inflate(R.layout.comments_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("UserProfilePicture").child(commentsData.get(position).getUid());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(holder.commentsProfilePicture);
            }
        });
        holder.commentsProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewProfileBottomSheet viewProfileBottomSheet=new ViewProfileBottomSheet(commentsData.get(position).getUid());
                viewProfileBottomSheet.show(manager,"ViewProfile");
            }
        });
        holder.comments_name.setText(commentsData.get(position).getName());
        holder.comments_message.setText(commentsData.get(position).getMessage());
        try {
            Date d1 = Calendar.getInstance().getTime();
            Date d2 = commentsData.get(position).getCommentsDate();
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
            holder.comments_time.setText(placeHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        Log.d("Size", commentsData.size() + "");
        return commentsData.size();

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class CommentsViewHolder extends RecyclerView.ViewHolder {
        private TextView comments_name, comments_message, comments_time;
        private ImageView commentsProfilePicture;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            comments_name = itemView.findViewById(R.id.comments_name);
            comments_message = itemView.findViewById(R.id.comments_message);
            comments_time = itemView.findViewById(R.id.comments_time);
            commentsProfilePicture = itemView.findViewById(R.id.commentsProfilePicture);

        }
    }
}
