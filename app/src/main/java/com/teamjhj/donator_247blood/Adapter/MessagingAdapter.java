package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamjhj.donator_247blood.DataModel.ChatData;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

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
        holder.chattingMessage.setText(chatData.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    class MessagingViewHolder extends RecyclerView.ViewHolder {
        TextView chattingName, chattingMessage;

        public MessagingViewHolder(@NonNull View itemView) {
            super(itemView);
            chattingName = itemView.findViewById(R.id.chattingName);
            chattingMessage = itemView.findViewById(R.id.chattingMessage);

        }
    }
}
