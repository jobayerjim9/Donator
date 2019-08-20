package com.teamjhj.donator_247blood;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewNonEmergencyRequestInfo extends DialogFragment {
    TextView nameReview, locationReview, mobileReview, bloodGroupReview, dateReview, reasonReview;
    Button editButtonReview, postButtonReview;
    private Context ctx;

    public ReviewNonEmergencyRequestInfo() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_review_non_emergency_request_info, null);
        nameReview = v.findViewById(R.id.nameReview);
        editButtonReview = v.findViewById(R.id.editButtonReview);
        postButtonReview = v.findViewById(R.id.postButtonReview);
        locationReview = v.findViewById(R.id.locationReview);
        mobileReview = v.findViewById(R.id.mobileReview);
        bloodGroupReview = v.findViewById(R.id.bloodGroupReview);
        dateReview = v.findViewById(R.id.dateReview);
        reasonReview = v.findViewById(R.id.reasonReview);
        nameReview.setText(AppData.getNonEmergencyInfo().getName());
        locationReview.setText(AppData.getNonEmergencyInfo().getLocation());
        mobileReview.setText(AppData.getNonEmergencyInfo().getPhone());
        bloodGroupReview.setText(AppData.getNonEmergencyInfo().getSelectedBloodGroup());
        String temp = AppData.getNonEmergencyInfo().getDate() + "-" + AppData.getNonEmergencyInfo().getMonth() + "-" + AppData.getNonEmergencyInfo().getYear();
        dateReview.setText(temp);

        reasonReview.setText(AppData.getNonEmergencyInfo().getReason());
        editButtonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        postButtonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });
        builder.setView(v);
        return builder.create();
    }


    private void uploadPost() {
        final ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Please Wait! Posting Your Post!");
        progressDialog.setCancelable(true);
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(AppData.getNonEmergencyInfo().getYear() + "").child(AppData.getNonEmergencyInfo().getMonth() + "").child(AppData.getNonEmergencyInfo().getDate() + "");
        databaseReference.push().setValue(AppData.getNonEmergencyInfo()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    BloodFeedFragment.getDataFromDatabase();
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Successfully Posted!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                    dismiss();


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
    }
}
