package com.teamjhj.donator_247blood.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Activity.SignUpActivity;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.util.Objects;

public class PhoneNumberPopupDialog extends DialogFragment {
    private Context context;
    boolean exist;
    public PhoneNumberPopupDialog() {
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.phone_number_popup_dialog, null);
        final TextInputLayout mobileNumberPopup = v.findViewById(R.id.mobileNumberPopup);
        Button doneButtonPopup = v.findViewById(R.id.doneButtonPopup);
        doneButtonPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumberValue = Objects.requireNonNull(mobileNumberPopup.getEditText()).getText().toString().trim();
                System.out.println("MOBILE 1 " + mobileNumberValue);
                if (mobileNumberValue.isEmpty()) {
                    mobileNumberPopup.setErrorEnabled(true);
                    mobileNumberPopup.setError("Please Enter Your Mobile Number");
                } else if (mobileNumberValue.contains("+")) {
                    mobileNumberPopup.setErrorEnabled(true);
                    mobileNumberPopup.setError("Please Enter Number Only. Country Code Doesn't Required!");
                } else if (mobileNumberValue.length() != 11) {
                    mobileNumberPopup.setErrorEnabled(true);
                    mobileNumberPopup.setError("Please Enter Valid Phone Number(11 Digit)");
                } else {
                    ProgressDialog progressDialog=new ProgressDialog(context);
                    progressDialog.setMessage("Verifying Your Mobile Number!");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    final String mobileNumber = "+88" + mobileNumberValue;
                    exist=false;
                    DatabaseReference profiles= FirebaseDatabase.getInstance().getReference("UserProfile");
                    profiles.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                            {
                                UserProfile userProfile=dataSnapshot1.getValue(UserProfile.class);
                                if(userProfile!=null)
                                {
                                    try {
                                        if (userProfile.getMobileNumber().equals(mobileNumber)) {
                                            exist = true;
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Mobile Number Already Exist!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if(!exist)
                            {
                                progressDialog.dismiss();
                                AppData.setMobileNumber(mobileNumber);
                                startActivity(new Intent(context, SignUpActivity.class));
                                dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
        builder.setView(v);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
