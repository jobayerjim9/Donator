package com.teamjhj.donator_247blood.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.DonationHistory;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.util.Calendar;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    UserProfile userProfile;
    ProgressDialog progressDialog;
    CheckBox notDated;
    int tempYear;
    String privacy = null;
    RadioGroup privacyNumberGroup;
    private Spinner spinner;
    private Button signUpDet, signUpBackButton;
    private LinearLayout linearLayout1, linearLayout2;
    private TextInputLayout nameInputSignUp, ageInputSignUp;
    private int count = 0, y, d, m, pos = -1;
    private String nameInput, ageInput, bloodGroup;
    private TextView signUpTextView, donatorTextView2, donatorTextView, pickText;
    private Button datePickerButton;
    private boolean checkNotDonate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize();
    }

    private void initialize() {
        datePickerButton = findViewById(R.id.datePickerButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.linearLayout2);
        nameInputSignUp = findViewById(R.id.nameInputSignUp);
        ageInputSignUp = findViewById(R.id.ageInputSignUp);
        notDated = findViewById(R.id.notDated);
        pickText = findViewById(R.id.pickText);
        privacyNumberGroup = findViewById(R.id.privacyNumberGroup);
        privacyNumberGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.publicButton) {
                    privacy = "Public";
                } else if (i == R.id.privateButton) {
                    privacy = "Private";
                }
            }
        });
        signUpDet = findViewById(R.id.signUpDet);
        signUpBackButton = findViewById(R.id.signUpBackButton);
        setSpinner();

        notDated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    datePickerButton.setVisibility(View.GONE);
                    pickText.setVisibility(View.GONE);
                    checkNotDonate = true;
                    tempYear = y;
                    y = -1;


                } else {
                    y = tempYear;
                    datePickerButton.setVisibility(View.VISIBLE);
                    pickText.setVisibility(View.VISIBLE);
                }
            }
        });
        signUpBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout1.setVisibility(View.VISIBLE);
                signUpTextView.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);
                signUpBackButton.setVisibility(View.GONE);
                signUpDet.setText("Next");
                count--;
            }
        });

        signUpDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    if (Objects.requireNonNull(nameInputSignUp.getEditText()).getText().toString().trim().isEmpty()) {

                        nameInputSignUp.setErrorEnabled(true);
                        nameInputSignUp.setError("Please Enter Your Name");
                    } else if (privacy == null) {
                        Toast.makeText(SignUpActivity.this, "Please Select Your Privacy", Toast.LENGTH_LONG).show();
                    } else if(bloodGroup==null)
                    {
                        Toast.makeText(SignUpActivity.this, "Please Select Your Blood Group", Toast.LENGTH_LONG).show();
                    }
                    else {
                        nameInputSignUp.setErrorEnabled(false);
                        nameInput = nameInputSignUp.getEditText().getText().toString();
                        linearLayout1.setVisibility(View.GONE);
                        signUpTextView.setVisibility(View.GONE);
                        linearLayout2.setVisibility(View.VISIBLE);
                        signUpBackButton.setVisibility(View.VISIBLE);
                        signUpBackButton.setVisibility(View.VISIBLE);
                        signUpDet.setText("Finish");
                        count++;

                    }
                } else if (count == 1) {
                    TextView errorText = findViewById(R.id.errorText);
                    if (Objects.requireNonNull(ageInputSignUp.getEditText()).getText().toString().isEmpty()) {
                        ageInputSignUp.setErrorEnabled(true);
                        ageInputSignUp.setError("Please Enter Your Age");
                    } else if (y == 0) {
                        errorText.setText("Please Pick Your Last Donation Date/Check The Box If You Didn't Donated Yet!");
                    } else {
                        uploadUserProfile();
                        ageInputSignUp.setErrorEnabled(false);
                        errorText.setText("");
                        signUpBackButton.setVisibility(View.GONE);

                    }

                }
            }
        });
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialouge();
            }
        });

    }

    private void showDatePickerDialouge() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void uploadUserProfile() {
        int[] donationDate = {d, y, m};

        ageInput = Objects.requireNonNull(ageInputSignUp.getEditText()).getText().toString();
        if (nameInput.isEmpty() || y == 0 || bloodGroup.isEmpty() || ageInput.isEmpty()) {
            Toast.makeText(this, "Please Check Your Information Again!", Toast.LENGTH_LONG).show();
        } else {
            try {
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Creating Your Profile");
                progressDialog.show();
                userProfile = new UserProfile(nameInput, FirebaseAuth.getInstance().getUid(), bloodGroup, d, m, y, AppData.getMobileNumber(), privacy);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                databaseReference.setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (y != -1) {
                                DatabaseReference history = FirebaseDatabase.getInstance().getReference("UserProfile").child(FirebaseAuth.getInstance().getUid()).child("DonationHistory");
                                DonationHistory donationHistory = new DonationHistory("Not Specified!", d, m, y);
                                history.push().setValue(donationHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "Sign Up Successfully Completed", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                            finish();

                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Something Is Wrong! Check Internet Or Contact Support!", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpActivity.this, "Sign Up Successfully Completed", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            }


                        } else {
                            Toast.makeText(SignUpActivity.this, "Something Is Wrong! Check Internet Or Contact Support!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.d = dayOfMonth;
        this.m = month + 1;
        this.y = year;
        String date = "Last Donate At: " + d + "-" + m + "-" + y;
        datePickerButton.setText(date);
    }

    private void setSpinner() {
        spinner = findViewById(R.id.spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bloodGroups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    bloodGroup = Objects.requireNonNull(adapter.getItem(position)).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
