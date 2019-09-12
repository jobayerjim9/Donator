package com.teamjhj.donator_247blood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamjhj.donator_247blood.Adapter.NearbyPlaceAdapter;
import com.teamjhj.donator_247blood.DataModel.LocationData;
import com.teamjhj.donator_247blood.DataModel.NearbyPlaceData;
import com.teamjhj.donator_247blood.DataModel.NearbyRawData;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;
import com.teamjhj.donator_247blood.RestApi.NearbyPlaceApi;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyPlaceResultActivity extends AppCompatActivity {

    private double lat,lon;
    private String searchCriteria;
    private ArrayList<NearbyPlaceData> nearbyPlaceData=new ArrayList<>();
    private NearbyPlaceAdapter nearbyPlaceAdapter;
    private RecyclerView nearbyPlaceRecycler;
    ShimmerFrameLayout nearbyShimmer;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_place_result);
        searchCriteria=getIntent().getStringExtra("search");
        Toolbar notificationToolbar = findViewById(R.id.nearbyResultToolbar);
        notificationToolbar.setTitle("Nearby "+searchCriteria);
        setSupportActionBar(notificationToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        notificationToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        notificationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Log.d("SearchCriteria",searchCriteria);

        nearbyPlaceRecycler=findViewById(R.id.nearbyPlaceRecycler);
        nearbyShimmer=findViewById(R.id.nearbyShimmer);
        nearbyShimmer.startShimmer();
        //nearbyPlaceRecycler.setNestedScrollingEnabled(false);
        nearbyPlaceRecycler.setHasFixedSize(true);
        nearbyPlaceRecycler.setLayoutManager(new LinearLayoutManager(this));
        nearbyPlaceAdapter=new NearbyPlaceAdapter(this,nearbyPlaceData);
        nearbyPlaceRecycler.setAdapter(nearbyPlaceAdapter);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    if(searchCriteria!=null)
                    {
                        getNearbyPlace(location.getLatitude(),location.getLongitude());
                    }
                }
            }
        });


    }


    private void getNearbyPlace(double lat,double lon)
    {
        ApiInterface apiInterface= NearbyPlaceApi.getClient().create(ApiInterface.class);
        if (lat!=0 && lon!=0) {
            Call<NearbyRawData> nearbyData = apiInterface.getNearbyPlace(lat + "," + lon, searchCriteria);
            nearbyData.enqueue(new Callback<NearbyRawData>() {
                @Override
                public void onResponse(Call<NearbyRawData> call, Response<NearbyRawData> response) {
                    //List<NearbyPlaceData> data = response.body();
                    NearbyRawData data=response.body();
                    if(data!=null)
                    {
                        nearbyShimmer.stopShimmer();
                        nearbyShimmer.setVisibility(View.GONE);
                        nearbyPlaceData.addAll(data.getResults());
                        nearbyPlaceAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<NearbyRawData> call, Throwable t) {
                    t.printStackTrace();
                    nearbyShimmer.stopShimmer();
                    nearbyShimmer.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            Toast.makeText(this, "Error Getting Location", Toast.LENGTH_SHORT).show();
        }

    }
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
