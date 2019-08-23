package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamjhj.donator_247blood.Activity.ChatActivity;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AcceptedDonorAdapter extends RecyclerView.Adapter<AcceptedDonorAdapter.AcceptedDonorViewHolder> {
    private Context ctx;
    private ArrayList<UserProfile> donnersData;
    private ArrayList<Integer> radius;

    public AcceptedDonorAdapter(Context ctx, ArrayList<UserProfile> donnersData, ArrayList<Integer> radius) {
        this.ctx = ctx;
        this.donnersData = donnersData;
        this.radius = radius;
    }

    @NonNull
    @Override
    public AcceptedDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AcceptedDonorViewHolder(LayoutInflater.from(ctx).inflate(R.layout.accepted_donor_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedDonorViewHolder holder, int position) {
        try {
            String placeHolder;
            holder.nameOfDonnerAccepted.setText(donnersData.get(position).getName());
            placeHolder = "Within " + radius.get(position) + " km";
            holder.distanceTextAccepted.setText(placeHolder);
            holder.distanceBarAccepted.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            holder.distanceBarAccepted.setProgress(radius.get(position));
            holder.messageDonorAccepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ctx, ChatActivity.class);
                    i.putExtra("Name", donnersData.get(position).getName());
                    i.putExtra("uid", donnersData.get(position).getUid());
                    ctx.startActivity(i);
                }
            });
//            holder.bloodGroupOfDonner.setText(donnersData.get(position).getBloodGroup());
//            holder.contactOfDonner.setText(donnersData.get(position).getMobileNumber());
//            placeHolder = donnersData.get(position).getLastDonationDate() + "-" + donnersData.get(position).getLastDonationMonth() + "-" + donnersData.get(position).getLastDonationYear();
//            holder.lastDonationDateDonner.setText(placeHolder);
//            placeHolder = "Around " + AppData.getRadiusList().get(position) + " km";
//            holder.distanceDonor.setText(placeHolder);
//            placeHolder = "Call " + donnersData.get(position).getName();
//            holder.callButton.setText(placeHolder);
//            getLocationData(position, holder);
            try {
                if (donnersData.get(position).getPrivacy().contains("Private")) {
                    holder.callButtonAccepted.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.callButtonAccepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri u = Uri.parse("tel:" + donnersData.get(position).getMobileNumber());

                    // Create the intent and set the data for the
                    // intent as the phone number.
                    Intent i = new Intent(Intent.ACTION_DIAL, u);

                    try {
                        ctx.startActivity(i);
                    } catch (SecurityException s) {

                        Toast.makeText(ctx, s.getLocalizedMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return donnersData.size();
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + ", " + obj.getLocality() + "-" + obj.getPostalCode();


            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();
            return add;
            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return "Error Loading location!";
    }

    class AcceptedDonorViewHolder extends RecyclerView.ViewHolder {
        TextView nameOfDonnerAccepted, distanceTextAccepted;
        ProgressBar distanceBarAccepted;
        ImageView callButtonAccepted, messageDonorAccepted;

        AcceptedDonorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfDonnerAccepted = itemView.findViewById(R.id.nameOfDonnerAccepted);
            distanceTextAccepted = itemView.findViewById(R.id.distanceTextAccepted);
            distanceBarAccepted = itemView.findViewById(R.id.distanceBarAccepted);
            callButtonAccepted = itemView.findViewById(R.id.callButtonAccepted);
            messageDonorAccepted = itemView.findViewById(R.id.messageDonorAccepted);
        }
    }
}
