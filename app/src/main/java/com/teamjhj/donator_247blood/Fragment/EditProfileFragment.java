package com.teamjhj.donator_247blood.Fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.util.Calendar;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    CheckBox availableCheckBox;
    DatabaseReference profile;
    Spinner spinnerEditProfile;
    RadioGroup publicButton, privateButton;
    private UserProfile userProfile;
    private String bloodGroup;
    private TextInputLayout nameEditProfile, mobileNumberEditProfile;
    private int day, month, year;
    private RadioGroup privacyNumberGroup;
    private String privacy = null;
    private Button saveButtonEditProfile;
    public EditProfileFragment() {
        day = 0;
        month = 0;
        year = 0;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        try {
            userProfile = AppData.getUserProfile();
            availableCheckBox = v.findViewById(R.id.availableCheckBox);
            spinnerEditProfile = v.findViewById(R.id.spinnerEditProfile);
            nameEditProfile = v.findViewById(R.id.nameEditProfile);
            privacyNumberGroup = v.findViewById(R.id.privacyNumberGroup);
            saveButtonEditProfile = v.findViewById(R.id.saveButtonEditProfile);

            saveButtonEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateData();
                }
            });
            mobileNumberEditProfile = v.findViewById(R.id.mobileNumberEditProfile);
            Objects.requireNonNull(nameEditProfile.getEditText()).setText(userProfile.getName());
            Objects.requireNonNull(mobileNumberEditProfile.getEditText()).setText(userProfile.getMobileNumber());
            mobileNumberEditProfile.setEnabled(false);
            setSpinner();
            privacyNumberGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.privateButton) {
                        privacy = "Private";
                    } else if (i == R.id.publicButton) {
                        privacy = "Public";
                    }
                }
            });

            profile = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

            if (userProfile.getPrivacy() != null) {
                if (userProfile.getPrivacy().contains("Public")) {
                    privacyNumberGroup.check(R.id.publicButton);
                } else if (userProfile.getPrivacy().contains("Private")) {
                    privacyNumberGroup.check(R.id.privateButton);
                }
            } else {
                profile.child("privacy").setValue("Public");
                privacyNumberGroup.check(R.id.publicButton);
            }

            try {
                if (userProfile.isAvailable()) {
                    availableCheckBox.setChecked(true);
                } else {
                    availableCheckBox.setChecked(false);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            availableCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        profile.child("available").setValue(true);
                    } else {
                        profile.child("available").setValue(false);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void updateData() {
        String name = nameEditProfile.getEditText().getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name Couldn't Be Empty", Toast.LENGTH_LONG).show();
        } else if (privacy == null) {
            Toast.makeText(getContext(), "Please Select A Privacy", Toast.LENGTH_LONG).show();
        } else {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Updating Your Profile!");
            progressDialog.show();
            profile.child("name").setValue(name);
            profile.child("privacy").setValue(privacy);
            profile.child("bloodGroup").setValue(bloodGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("ProfileUpdated", task.toString());
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Your Profile Updated Successfully!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    progressDialog.dismiss();
                }
            });

        }

    }

    private void setSpinner() {
        String compareValue = userProfile.getBloodGroup();
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.bloodGroups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditProfile.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinnerEditProfile.setSelection(spinnerPosition);
        }
        spinnerEditProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroup = Objects.requireNonNull(adapter.getItem(position)).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePickerDialouge() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(getContext()),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.day = dayOfMonth;
        this.month = month + 1;
        this.year = year;
        String temp = day + "/" + this.month + "/" + year;
    }
}
