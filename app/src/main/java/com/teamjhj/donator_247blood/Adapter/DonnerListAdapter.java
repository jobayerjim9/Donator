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

import com.google.firebase.database.DatabaseReference;
import com.teamjhj.donator_247blood.Activity.ChatActivity;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DonnerListAdapter extends RecyclerView.Adapter<DonnerListAdapter.DonnerListViewHolder> {
    private Context ctx;
    private ArrayList<UserProfile> donnersData;
    private DatabaseReference location;
    private ArrayList<String> keys;

    public DonnerListAdapter(Context c, ArrayList<UserProfile> data) {
        ctx = c;
        donnersData = data;
        keys = AppData.getDonners();
    }

    @NonNull
    @Override
    public DonnerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DonnerListViewHolder(LayoutInflater.from(ctx).inflate(R.layout.donor_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DonnerListViewHolder holder, final int position) {
        try {
            String placeHolder;
            holder.nameOfDonner.setText(donnersData.get(position).getName());
            placeHolder = "Within " + AppData.getRadiusList().get(position) + " km";
            holder.distanceText.setText(placeHolder);
            holder.distanceBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            holder.distanceBar.setProgress(AppData.getRadiusList().get(position));
            holder.messageDonor.setOnClickListener(new View.OnClickListener() {
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
                    holder.callButton.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.callButton.setOnClickListener(new View.OnClickListener() {
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

//    private void getLocationData(int position, @NonNull final DonnerListViewHolder holder) {
//        location = FirebaseDatabase.getInstance().getReference("AvailableDonner");
//        GeoFire geoFire = new GeoFire(location);
//        geoFire.getLocation(keys.get(position), new LocationCallback() {
//            @Override
//            public void onLocationResult(String key, GeoLocation location) {
//                holder.donnerLocation.setText(getAddress(location.latitude, location.longitude));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }


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

    class DonnerListViewHolder extends RecyclerView.ViewHolder {
        TextView nameOfDonner, distanceText;
        ImageView callButton, messageDonor;
        ProgressBar distanceBar;

        public DonnerListViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfDonner = itemView.findViewById(R.id.nameOfDonner);
            distanceText = itemView.findViewById(R.id.distanceText);
            callButton = itemView.findViewById(R.id.callButton);
            distanceBar = itemView.findViewById(R.id.distanceBar);
            messageDonor = itemView.findViewById(R.id.messageDonor);
//            bloodGroupOfDonner = itemView.findViewById(R.id.bloodGroupOfDonner);
//            contactOfDonner = itemView.findViewById(R.id.contactOfDonner);
//            lastDonationDateDonner = itemView.findViewById(R.id.lastDonationDateDonner);
//            donnerLocation = itemView.findViewById(R.id.donnerLocation);
//            distanceDonor = itemView.findViewById(R.id.distanceDonor);
//            callButton = itemView.findViewById(R.id.callButton);
        }
    }
}
