package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.teamjhj.donator_247blood.DataModel.ChatData;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.MessagingViewHolder> {
    private Context ctx;
    private ArrayList<ChatData> chatData;

    public MessagingAdapter(Context ctx, ArrayList<ChatData> chatData) {
        this.ctx = ctx;
        this.chatData = chatData;
    }

    @NonNull
    @Override
    public MessagingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessagingViewHolder(LayoutInflater.from(ctx).inflate(R.layout.chatting_message_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagingViewHolder holder, int position) {
        if (chatData.get(position).isRecieved()) {
            holder.chattingName.setText(chatData.get(position).getRecieverName());
        } else if (chatData.get(position).isSent()) {
            holder.chattingName.setText(chatData.get(position).getSenderName());
        }
        try {
            if (chatData.get(position).getMessageCreator().equals(FirebaseAuth.getInstance().getUid())) {
                holder.chattingName.setText("Me");
                holder.chatting_card.setCardBackgroundColor(ctx.getResources().getColor(R.color.material_background));
                holder.chattingName.setTextColor(ctx.getResources().getColor(R.color.white));
                holder.chattingMessage.setTextColor(ctx.getResources().getColor(R.color.white));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.chatting_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.timeTextMessege.getVisibility() == View.VISIBLE) {
                    holder.timeTextMessege.setVisibility(View.GONE);
                    if (chatData.get(position).getMessageCreator().equals(FirebaseAuth.getInstance().getUid())) {
                        holder.chatting_card.setCardBackgroundColor(ctx.getResources().getColor(R.color.material_background));
                    } else {
                        holder.chatting_card.setCardBackgroundColor(ctx.getResources().getColor(R.color.white));
                    }
                } else {
                    holder.chatting_card.setCardBackgroundColor(ctx.getResources().getColor(R.color.grey_300));
                    holder.timeTextMessege.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.chattingMessage.setText(chatData.get(position).getMessage());
        try {
            Date d1 = Calendar.getInstance().getTime();
            Date d2 = chatData.get(position).getMessageTime();
            Log.d("Current Time", d1.getTime() + "");
            long diff = d1.getTime() - d2.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            Log.d("Time", diffDays + " " + diffHours);
            String placeHolder = "";
            if (diffMinutes <= 1) {
                placeHolder = "Just Now";
            } else if (diffMinutes > 1 && diffHours == 0) {
                placeHolder = diffMinutes + " Minutes Ago!";
            } else if (diffHours > 0 && diffDays == 0) {
                placeHolder = diffHours + " Hours Ago!";
            } else if (diffDays > 0) {
                placeHolder = diffDays + " Days Ago!";
            }
            holder.timeTextMessege.setText(placeHolder);
        } catch (Exception e) {
            holder.timeTextMessege.setText("");
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MessagingViewHolder extends RecyclerView.ViewHolder {
        TextView chattingName, chattingMessage, timeTextMessege;
        CardView chatting_card;
        public MessagingViewHolder(@NonNull View itemView) {
            super(itemView);
            chattingName = itemView.findViewById(R.id.chattingName);
            chattingMessage = itemView.findViewById(R.id.chattingMessage);
            timeTextMessege = itemView.findViewById(R.id.timeTextMessege);
            chatting_card = itemView.findViewById(R.id.chatting_card);

        }
    }
}
