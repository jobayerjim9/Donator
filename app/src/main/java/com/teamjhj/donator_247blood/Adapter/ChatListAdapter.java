package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.teamjhj.donator_247blood.Activity.ChatActivity;
import com.teamjhj.donator_247blood.DataModel.ChatData;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private ArrayList<ChatData> chatData;
    private Context ctx;

    public ChatListAdapter(ArrayList<ChatData> chatData, Context ctx) {
        this.chatData = chatData;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater.from(ctx).inflate(R.layout.messenger_people_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.nameMessenger.setText(chatData.get(position).getRecieverName());
        holder.messageMessenger.setText(chatData.get(position).getMessage());
        holder.messenger_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference setViewed = FirebaseDatabase.getInstance().getReference("MessengerChat").child(FirebaseAuth.getInstance().getUid()).child(chatData.get(position).getUid()).child(chatData.get(position).getMessegingKey());
                setViewed.child("viewed").setValue(true);
                holder.messenger_people.setCardBackgroundColor(ctx.getResources().getColor(R.color.browser_actions_bg_grey));
                holder.nameMessenger.setTextColor(ctx.getResources().getColor(R.color.black));
                holder.messageMessenger.setTextColor(ctx.getResources().getColor(R.color.black));
                Intent i = new Intent(ctx, ChatActivity.class);
                i.putExtra("Name", chatData.get(position).getRecieverName());
                i.putExtra("uid", chatData.get(position).getUid());
                ctx.startActivity(i);
            }
        });
        if (!chatData.get(position).isViewed()) {
            holder.messenger_people.setCardBackgroundColor(ctx.getResources().getColor(R.color.material_background));
            holder.nameMessenger.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.messageMessenger.setTextColor(ctx.getResources().getColor(R.color.white));
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("UserProfilePicture").child(chatData.get(position).getUid());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(holder.messengerProfilePic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ChatListViewHolder extends RecyclerView.ViewHolder {
        TextView nameMessenger, messageMessenger;
        CardView messenger_people;
        ImageView messengerProfilePic;
        ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            nameMessenger = itemView.findViewById(R.id.nameMessenger);
            messageMessenger = itemView.findViewById(R.id.messageMessenger);
            messenger_people = itemView.findViewById(R.id.messenger_people);
            messengerProfilePic = itemView.findViewById(R.id.messengerProfilePic);
        }
    }
}
