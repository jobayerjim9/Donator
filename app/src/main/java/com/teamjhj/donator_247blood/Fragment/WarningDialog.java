package com.teamjhj.donator_247blood.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.teamjhj.donator_247blood.Activity.MyRequestActivity;
import com.teamjhj.donator_247blood.R;

import java.util.Objects;

public class WarningDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_warning_dialog, null);

        Button warningDissmissButton = v.findViewById(R.id.warningDissmissButton);
        Button viewReqWarning = v.findViewById(R.id.viewReqWarning);
        viewReqWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyRequestActivity.class));
                dismiss();
            }
        });
        warningDissmissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        builder.setView(v);
        return builder.create();
    }
}
