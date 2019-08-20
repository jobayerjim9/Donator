package com.teamjhj.donator_247blood;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    private Context ctx;
    private ArrayList<CommentsData> commentsData;

    public CommentsAdapter(Context ctx, ArrayList<CommentsData> commentsData) {
        this.ctx = ctx;
        this.commentsData = commentsData;
    }


    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentsViewHolder(LayoutInflater.from(ctx).inflate(R.layout.comments_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
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

    public class CommentsViewHolder extends RecyclerView.ViewHolder {
        private TextView comments_name, comments_message, comments_time;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            comments_name = itemView.findViewById(R.id.comments_name);
            comments_message = itemView.findViewById(R.id.comments_message);
            comments_time = itemView.findViewById(R.id.comments_time);

        }
    }
}
