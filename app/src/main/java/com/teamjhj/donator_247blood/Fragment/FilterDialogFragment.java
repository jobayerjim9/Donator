package com.teamjhj.donator_247blood.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.R;

import java.util.Objects;

public class FilterDialogFragment extends DialogFragment {
    private Context ctx;
    private Spinner sortSpinner;

    public FilterDialogFragment() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_filer_dialog, null);
        Button sortByNearbyLocation, sortByDate, doneButtonSort, backButtonSort;
        sortByNearbyLocation = v.findViewById(R.id.sortByNearbyLocation);
        sortByDate = v.findViewById(R.id.sortByDate);
        backButtonSort = v.findViewById(R.id.backButtonSort);
        sortSpinner = v.findViewById(R.id.sortSpinner);
        setSpinner();
        sortByNearbyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodFeedFragment.setFilter("location");
                BloodFeedFragment.getDataFromDatabase();
                dismiss();
            }
        });
        sortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodFeedFragment.setFilter("no");
                BloodFeedFragment.getDataFromDatabase();
                dismiss();
            }
        });

        backButtonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        builder.setView(v);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    private void setSpinner() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.bloodGroups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    String bloodGroup = Objects.requireNonNull(adapter.getItem(position)).toString();
                    BloodFeedFragment.setBloodGroup(bloodGroup);
                    BloodFeedFragment.sortByBloodGroup();
                    dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
