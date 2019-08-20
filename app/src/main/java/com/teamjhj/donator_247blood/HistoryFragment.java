package com.teamjhj.donator_247blood;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    String hospitalNameString;
    DonationHistory donationHistory;
    LinearLayout addDonationInfoLayout, headingTableHistory;
    ArrayList<DonationHistory> donationHistories;
    SwipeRefreshLayout swipeHistory;
    private TextView historyHeading, noHistoryText;
    private TextInputLayout hospitalNameInput;
    private RecyclerView historyRecycler;
    private Button addDonationInfo, pickDateHistory, todayButttonHistory, addButtonHistory, backButtonHistory;
    private int day, month, year;
    private LottieAnimationView nothingFoundHistory;

    public HistoryFragment() {
        day = 0;
        month = 0;
        year = 0;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        historyHeading = v.findViewById(R.id.historyHeading);
        historyRecycler = v.findViewById(R.id.historyRecycler);
        historyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        addDonationInfo = v.findViewById(R.id.addDonationInfo);
        addDonationInfoLayout = v.findViewById(R.id.addDonationInfoLayout);
        nothingFoundHistory = v.findViewById(R.id.nothingFoundHistory);
        nothingFoundHistory.setVisibility(View.GONE);
        pickDateHistory = v.findViewById(R.id.pickDateHistory);
        todayButttonHistory = v.findViewById(R.id.todayButttonHistory);
        addButtonHistory = v.findViewById(R.id.addButtonHistory);
        noHistoryText = v.findViewById(R.id.noHistoryText);
        hospitalNameInput = v.findViewById(R.id.hospitalNameInput);
        backButtonHistory = v.findViewById(R.id.backButtonHistory);
        headingTableHistory = v.findViewById(R.id.headingTableHistory);
        swipeHistory = v.findViewById(R.id.swipeHistory);
        swipeHistory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeHistory.setColorScheme(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);
                getHistoryData();
            }

        });

        addDonationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyRecycler.setVisibility(View.GONE);
                addDonationInfo.setVisibility(View.GONE);
                headingTableHistory.setVisibility(View.GONE);
                addDonationInfoLayout.setVisibility(View.VISIBLE);
                noHistoryText.setVisibility(View.GONE);
                nothingFoundHistory.setVisibility(View.GONE);
            }
        });
        pickDateHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialouge();
            }
        });
        todayButttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH) + 1;
                year = c.get(Calendar.YEAR);
                validate();
            }
        });
        addButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        backButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyRecycler.setVisibility(View.VISIBLE);
                addDonationInfo.setVisibility(View.VISIBLE);
                headingTableHistory.setVisibility(View.VISIBLE);
                addDonationInfoLayout.setVisibility(View.GONE);
                if (donationHistories.isEmpty()) {
                    noHistoryText.setVisibility(View.VISIBLE);
                    nothingFoundHistory.setVisibility(View.VISIBLE);
                    headingTableHistory.setVisibility(View.GONE);
                } else {
                    headingTableHistory.setVisibility(View.VISIBLE);
                    noHistoryText.setVisibility(View.GONE);
                    nothingFoundHistory.setVisibility(View.GONE);
                }
            }
        });
        getHistoryData();
        return v;
    }

    private void getHistoryData() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("DonationHistory");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donationHistories = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DonationHistory donationHistory = dataSnapshot1.getValue(DonationHistory.class);
                    if (donationHistory != null) {
                        donationHistory.setKey(dataSnapshot1.getKey());
                        donationHistories.add(donationHistory);
                    }
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), donationHistories);
                historyRecycler.setAdapter(historyAdapter);
                if (donationHistories.isEmpty()) {
                    noHistoryText.setVisibility(View.VISIBLE);
                    nothingFoundHistory.setVisibility(View.VISIBLE);
                    headingTableHistory.setVisibility(View.GONE);
                } else {
                    headingTableHistory.setVisibility(View.VISIBLE);
                    noHistoryText.setVisibility(View.GONE);
                    nothingFoundHistory.setVisibility(View.GONE);
                }
                if (swipeHistory.isRefreshing()) {
                    swipeHistory.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validate() {
        hospitalNameString = Objects.requireNonNull(hospitalNameInput.getEditText()).getText().toString().trim();
        if (hospitalNameString.isEmpty()) {
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Please Enter Hospital/Clinic Name", Toast.LENGTH_LONG).show();
        } else if (day == 0 || month == 0 || year == 0) {
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Please Select Date", Toast.LENGTH_LONG).show();
        } else {
            donationHistory = new DonationHistory(hospitalNameString, day, month, year);
            addDonationInfoToDatabase();
        }
    }

    private void addDonationInfoToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("DonationHistory");
        databaseReference.push().setValue(donationHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
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
                            Toast.makeText(getContext(), "Hi", Toast.LENGTH_LONG).show();
                            DatabaseReference profile = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                            profile.child("lastDonationDate").setValue(day);
                            profile.child("lastDonationMonth").setValue(month);
                            profile.child("lastDonationYear").setValue(year);

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                    historyRecycler.setVisibility(View.VISIBLE);
                    addDonationInfo.setVisibility(View.VISIBLE);
                    headingTableHistory.setVisibility(View.VISIBLE);
                    addDonationInfoLayout.setVisibility(View.GONE);
                } else {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Network Or Server Error. Please Try Again Later", Toast.LENGTH_LONG).show();
                }
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
        pickDateHistory.setText(temp);
    }

}
