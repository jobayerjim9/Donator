package com.teamjhj.donator_247blood.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Adapter.PendingHistoryAdapter;
import com.teamjhj.donator_247blood.DataModel.DonationHistory;
import com.teamjhj.donator_247blood.DataModel.PendingHistory;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPendingHistoryDialog extends DialogFragment {
    private Context ctx;
    private RecyclerView pendingHistoryRecycler;
    private Button closeButtonPending;
    private ArrayList<PendingHistory> pendingHistories = new ArrayList<>();
    private ArrayList<DonationHistory> donationHistories = new ArrayList<>();
    private PendingHistoryAdapter pendingHistoryAdapter;
    private ShimmerFrameLayout pendingHistoryShimmer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_view_pending_history, null);
        updateDate();
        pendingHistoryRecycler = v.findViewById(R.id.pendingHistoryRecycler);
        closeButtonPending = v.findViewById(R.id.closeButtonPending);
        pendingHistoryShimmer = v.findViewById(R.id.pendingHistoryShimmer);
        pendingHistoryRecycler.setLayoutManager(new LinearLayoutManager(ctx));
        pendingHistoryAdapter = new PendingHistoryAdapter(ctx, pendingHistories, donationHistories);
        pendingHistoryRecycler.setAdapter(pendingHistoryAdapter);
        closeButtonPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference("PendingDonationConfirmation").child(FirebaseAuth.getInstance().getUid());
        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingHistories.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    PendingHistory pendingHistory = dataSnapshot1.getValue(PendingHistory.class);
                    if (pendingHistory != null) {
                        pendingHistoryShimmer.stopShimmer();
                        pendingHistoryShimmer.setVisibility(View.GONE);

                        pendingHistory.setKey(dataSnapshot1.getKey());
                        pendingHistories.add(pendingHistory);
                        pendingHistoryAdapter.notifyDataSetChanged();
                    }
                }
                if (pendingHistories.isEmpty()) {
                    Toast.makeText(ctx, "No More Request Left!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        builder.setView(v);
        return builder.create();


    }

    private void updateDate() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("DonationHistory");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donationHistories.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DonationHistory donationHistory = dataSnapshot1.getValue(DonationHistory.class);
                    if (donationHistory != null) {
                        donationHistory.setKey(dataSnapshot1.getKey());
                        donationHistories.add(donationHistory);
                        pendingHistoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
