package com.teamjhj.donator_247blood.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamjhj.donator_247blood.DataModel.DonationHistory;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Objects;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context ctx;
    private ArrayList<DonationHistory> donationHistories;

    public HistoryAdapter(Context ctx, ArrayList<DonationHistory> donationHistories) {
        this.ctx = ctx;
        this.donationHistories = donationHistories;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(ctx).inflate(R.layout.history_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, final int position) {
        holder.hospitalClinic.setText(donationHistories.get(position).getHospitalName());
        String temp = donationHistories.get(position).getDay() + "/" + donationHistories.get(position).getMonth() + "/" + donationHistories.get(position).getYear();
        holder.dateHistory.setText(temp);
        holder.historyCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Delete History")
                        .setMessage("Are you sure you want to delete this history?")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("DonationHistory").child(donationHistories.get(position).getKey());
                                historyReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ctx, "Deleted Successfully! Swipe Down To Refresh!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })

                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return donationHistories.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView hospitalClinic, dateHistory;
        CardView historyCard;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            hospitalClinic = itemView.findViewById(R.id.hospitalClinic);
            dateHistory = itemView.findViewById(R.id.dateHistory);
            historyCard = itemView.findViewById(R.id.historyCard);
        }
    }
}
