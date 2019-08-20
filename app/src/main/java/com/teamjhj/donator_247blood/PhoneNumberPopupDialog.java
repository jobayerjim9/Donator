package com.teamjhj.donator_247blood;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class PhoneNumberPopupDialog extends DialogFragment {
    private Context context;

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
                    mobileNumberValue = "+88" + mobileNumberValue;
                    AppData.setMobileNumber(mobileNumberValue);
                    startActivity(new Intent(getActivity(), SignUpActivity.class));
                    dismiss();
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
