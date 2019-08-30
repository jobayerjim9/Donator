package com.teamjhj.donator_247blood.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.DonationHistory;
import com.teamjhj.donator_247blood.DataModel.PendingHistory;
import com.teamjhj.donator_247blood.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PendingHistoryAdapter extends RecyclerView.Adapter<PendingHistoryAdapter.PendingHistoryViewHolder> {
    private final String DATE_FORMAT = "d/M/yyyy";
    private Context ctx;
    private ArrayList<PendingHistory> pendingHistories;
    private ArrayList<DonationHistory> donationHistories;

    public PendingHistoryAdapter(Context ctx, ArrayList<PendingHistory> pendingHistories, ArrayList<DonationHistory> donationHistories) {
        this.ctx = ctx;
        this.pendingHistories = pendingHistories;
        this.donationHistories = donationHistories;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public PendingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PendingHistoryViewHolder(LayoutInflater.from(ctx).inflate(R.layout.pending_history_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PendingHistoryViewHolder holder, int position) {
        holder.namePending.setText(pendingHistories.get(position).getName());
        String placeHolder;
        if (pendingHistories.get(position).getDate().getYear() < 1900) {
            placeHolder = pendingHistories.get(position).getDate().getDate() + "-" + (pendingHistories.get(position).getDate().getMonth() + 1) + "-" + (pendingHistories.get(position).getDate().getYear() + 1900);
        } else {
            placeHolder = pendingHistories.get(position).getDate().getDate() + "-" + pendingHistories.get(position).getDate().getMonth() + "-" + pendingHistories.get(position).getDate().getYear();
        }
        holder.datePending.setText(placeHolder);
        holder.acceptPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(position);
            }
        });
        holder.declinePending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference removeValue = FirebaseDatabase.getInstance().getReference("PendingDonationConfirmation").child(FirebaseAuth.getInstance().getUid()).child(pendingHistories.get(position).getKey());
                removeValue.removeValue();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingHistories.size();
    }

    private void validate(int position) {
        ProgressDialog progressDialog=new ProgressDialog(ctx);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Adding Your Donation History");
        progressDialog.show();
        int dayCount = 0;
        int day, month, year;
        if (pendingHistories.get(position).getDate().getYear() < 1900) {
            day = pendingHistories.get(position).getDate().getDate();
            month = pendingHistories.get(position).getDate().getMonth() + 1;
            year = pendingHistories.get(position).getDate().getYear() + 1900;
        } else {
            day = pendingHistories.get(position).getDate().getDate();
            month = pendingHistories.get(position).getDate().getMonth();
            year = pendingHistories.get(position).getDate().getYear();
        }


        boolean valid = true;
        for (int i = 0; i < donationHistories.size(); i++) {
            int userDay, userMonth, userYear;
            userDay = donationHistories.get(i).getDay();
            userMonth = donationHistories.get(i).getMonth();
            userYear = donationHistories.get(i).getYear();
            System.out.println(day + "-" + month + "-" + year);
            System.out.println(userDay + "-" + userMonth + "-" + userYear);
            String start = userDay + "/" + userMonth + "/" + userYear;
            String end = day + "/" + month + "/" + year;
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            Date startDate, endDate;
            try {
                startDate = dateFormat.parse(start);
                endDate = dateFormat.parse(end);
                assert startDate != null;
                assert endDate != null;
                dayCount = (int) getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dayCount < 0) {
                try {
                    startDate = dateFormat.parse(end);
                    endDate = dateFormat.parse(start);
                    assert startDate != null;
                    assert endDate != null;
                    dayCount = (int) getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Day ount", dayCount + "");
            if (dayCount <= 90) {
                valid = false;
            }
        }
        if (valid) {
            DonationHistory donationHistory = new DonationHistory(pendingHistories.get(position).getName(), day, month, year);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("DonationHistory");
            databaseReference.push().setValue(donationHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        int d, m, y;
                        d = AppData.getUserProfile().getLastDonationDate();
                        m = AppData.getUserProfile().getLastDonationMonth();
                        y = AppData.getUserProfile().getLastDonationYear();
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        try {
                            String date = m + "/" + d + "/" + y;
                            Date lastDonationDate = formatter.parse(date);
                            date = month + "/" + day + "/" + year;
                            Date nowDate = formatter.parse(date);
                            assert lastDonationDate != null;
                            assert nowDate != null;
                            if (lastDonationDate.compareTo(nowDate) < 0) {
                                DatabaseReference profile = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                                profile.child("lastDonationDate").setValue(day);
                                profile.child("lastDonationMonth").setValue(month);
                                profile.child("lastDonationYear").setValue(year);

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DatabaseReference removePendingHistory = FirebaseDatabase.getInstance().getReference("PendingDonationConfirmation").child(FirebaseAuth.getInstance().getUid());
                        removePendingHistory.removeValue();

                        Toast.makeText(ctx, "Added Successfully", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(ctx.getApplicationContext(), "Network Or Server Error. Please Try Again Later", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(ctx, "You Cannot Donate Twice In 90 Days!", Toast.LENGTH_LONG).show();
        }
    }

    private long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    class PendingHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView namePending, datePending;
        ImageView acceptPending, declinePending;

        public PendingHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            namePending = itemView.findViewById(R.id.namePending);
            datePending = itemView.findViewById(R.id.datePending);
            acceptPending = itemView.findViewById(R.id.acceptPending);
            declinePending = itemView.findViewById(R.id.declinePending);


        }
    }

}
