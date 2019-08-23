package com.teamjhj.donator_247blood.Fragment;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.teamjhj.donator_247blood.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends DialogFragment {
    Button facebookSupportPage, facebookSupportGroup, officialSupportHelpline, facebookSupportMessengerGroup, aboutUs, aboutApp, logOut;

    public SupportFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_support, null);
        facebookSupportPage = v.findViewById(R.id.facebookSupportPage);
        facebookSupportPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://m.me/official.donator"));
                Objects.requireNonNull(getContext()).startActivity(intent);
            }
        });
        facebookSupportGroup = v.findViewById(R.id.facebookSupportGroup);
        facebookSupportGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/groups/official.donator/"));
                Objects.requireNonNull(getContext()).startActivity(intent);
            }
        });

        officialSupportHelpline = v.findViewById(R.id.officialSupportHelpline);
        officialSupportHelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Comming Soon", Toast.LENGTH_LONG).show();
            }
        });
        facebookSupportMessengerGroup = v.findViewById(R.id.facebookSupportMessengerGroup);
        facebookSupportMessengerGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://m.me/join/AbZtD16HXEboLDRX"));
                Objects.requireNonNull(getContext()).startActivity(intent);
            }
        });

        builder.setView(v);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_support, container, false);


        return v;
    }

}
