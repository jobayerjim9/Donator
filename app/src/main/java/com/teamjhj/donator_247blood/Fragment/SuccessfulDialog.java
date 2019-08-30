package com.teamjhj.donator_247blood.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.teamjhj.donator_247blood.R;

import java.util.Objects;

public class SuccessfulDialog extends DialogFragment {
    private Context ctx;
    private String message;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx=context;
    }

    public SuccessfulDialog(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_successful, null);
        TextView doneText=v.findViewById(R.id.doneText);
        Button okSuccessful=v.findViewById(R.id.okSuccessful);
        doneText.setText(message);
        okSuccessful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        builder.setView(v);
        return builder.create();

    }
}
