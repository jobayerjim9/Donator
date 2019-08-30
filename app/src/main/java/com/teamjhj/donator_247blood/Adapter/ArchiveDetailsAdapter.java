package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

public class ArchiveDetailsAdapter extends RecyclerView.Adapter<ArchiveDetailsAdapter.ArchiveDetailsViewHolder> {
    private ArrayList<UserProfile> userProfiles;
    private Context ctx;

    public ArchiveDetailsAdapter(ArrayList<UserProfile> userProfiles, Context ctx) {
        this.userProfiles = userProfiles;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ArchiveDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArchiveDetailsViewHolder(LayoutInflater.from(ctx).inflate(R.layout.accepted_donor_archive, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArchiveDetailsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    class ArchiveDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView nameArchive;
        ImageView callArchive, messageArchive;


        public ArchiveDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameArchive = itemView.findViewById(R.id.nameArchive);
            callArchive = itemView.findViewById(R.id.callArchive);
            messageArchive = itemView.findViewById(R.id.messageArchive);
        }
    }
}
