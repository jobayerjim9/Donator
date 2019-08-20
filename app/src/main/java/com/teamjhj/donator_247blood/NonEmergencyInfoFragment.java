package com.teamjhj.donator_247blood;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;
import com.manojbhadane.QButton;

import java.util.Calendar;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class NonEmergencyInfoFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    static ImageView closeButtonDonnerList;
    static ScrollView scrollViewNonEmergency;
    private static FragmentManager fm;
    TextView datePickLabelNonEmergency, timePickLabelNonEmergency, nonEmergencyHeading;
    Button date_picker_future;
    NonEmergencyInfo nonEmergencyInfo;
    EditText bagsNonEmergency;
    int day, month, year, hours, minute;
    private Context ctx;

    public NonEmergencyInfoFragment() {
        day = 0;
        month = 0;
        year = 0;
        hours = -1;
        minute = -1;
        // Required empty public constructor
    }

    public static void closeReview(int n) {

        ReviewNonEmergencyRequestInfo fragment = (ReviewNonEmergencyRequestInfo) fm.findFragmentById(R.id.confirmationDialogue);
        FragmentTransaction donnerListFragment = fm.beginTransaction();

        if (fragment != null) {
            donnerListFragment.remove(fragment).commit();
        }
        scrollViewNonEmergency.setVisibility(View.VISIBLE);
        closeButtonDonnerList.setVisibility(View.VISIBLE);
        if (n == 1) {
            SearchDonnerFragment.donnerListClose(2);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_non__emergency__info, null);
        QButton reviewButton = v.findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReviewFrag();
            }
        });
        fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        date_picker_future = v.findViewById(R.id.date_picker_future);
        nonEmergencyHeading = v.findViewById(R.id.nonEmergencyHeading);
        scrollViewNonEmergency = v.findViewById(R.id.scrollViewNonEmergency);
        bagsNonEmergency = v.findViewById(R.id.bagsNonEmergency);
        datePickLabelNonEmergency = v.findViewById(R.id.datePickLabelNonEmergency);
        timePickLabelNonEmergency = v.findViewById(R.id.timePickLabelNonEmergency);
        date_picker_future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialouge();
            }
        });
        closeButtonDonnerList = v.findViewById(R.id.closeButtonDonnerList);
        closeButtonDonnerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(v);

        return builder.create();
    }


    private void loadReviewFrag() {
        String bag = bagsNonEmergency.getText().toString();
        if (day == 0 || month == 0 || year == 0) {
            Toast.makeText(getContext(), "Please Select A Date Before Review Your Request", Toast.LENGTH_LONG).show();
        } else if (bag.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter How Many Bags Need", Toast.LENGTH_LONG).show();
        } else {
            AppData.getNonEmergencyInfo().setName(AppData.getUserProfile().getName());
            AppData.getNonEmergencyInfo().setPhone(AppData.getUserProfile().getMobileNumber());
            AppData.getNonEmergencyInfo().setBags(bag);
//            scrollViewNonEmergency.setVisibility(View.INVISIBLE);
//            closeButtonDonnerList.setVisibility(View.GONE);
//            nonEmergencyHeading.setText("Please Review Your Provided Informations!");
//            FragmentTransaction fragmentTransaction = fm.beginTransaction();
//            fragmentTransaction.add(R.id.confirmationDialogue, new ReviewNonEmergencyRequestInfo());
//            fragmentTransaction.commit();

            ReviewNonEmergencyRequestInfo review = new ReviewNonEmergencyRequestInfo();
            review.setCancelable(false);
            review.show(getChildFragmentManager(), "Review");


        }
    }

    private void showTimePickerDialougue() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void showDatePickerDialouge() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(getContext()),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.day = dayOfMonth;
        this.month = month + 1;
        this.year = year;
        AppData.getNonEmergencyInfo().setDate(dayOfMonth);
        AppData.getNonEmergencyInfo().setMonth(month + 1);
        AppData.getNonEmergencyInfo().setYear(year);
        String temp = dayOfMonth + "-" + this.month + "-" + year;
        date_picker_future.setText(temp);
        temp = "Yor Picked Date: ";
        datePickLabelNonEmergency.setText(temp);


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hours = hourOfDay;
        this.minute = minute;
        AppData.getNonEmergencyInfo().setHour(hourOfDay);
        AppData.getNonEmergencyInfo().setMinute(minute);
        String temp = "Your Picked Time: ";
        timePickLabelNonEmergency.setText(temp);
        temp = hourOfDay + " : " + minute;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Resume", "Here");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Pause", "Here");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("onDetach", "Here");
    }
}
