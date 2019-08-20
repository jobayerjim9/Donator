package com.teamjhj.donator_247blood.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.teamjhj.donator_247blood.R;

import java.util.Objects;

public class NewUpdateAvailableDialog extends DialogFragment {


    private Context ctx;

    public NewUpdateAvailableDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_update_dialog, null);
        Button updateButton = v.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.teamjhj.donator_247blood"));
                ctx.startActivity(intent);
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
}
