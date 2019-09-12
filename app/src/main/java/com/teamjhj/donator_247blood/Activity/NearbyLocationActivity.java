package com.teamjhj.donator_247blood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.teamjhj.donator_247blood.R;

public class NearbyLocationActivity extends AppCompatActivity {
    private double lat,lon;
    private LocationManager locationManager;
    private LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_location);
        Toolbar notificationToolbar = findViewById(R.id.nearbyToolbar);
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
        Button nearbyHospital=findViewById(R.id.nearbyHospital);
        nearbyHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NearbyLocationActivity.this,NearbyPlaceResultActivity.class);
                intent.putExtra("search","Hospital");
                startActivity(intent);
            }
        });
        Button nearbyBloodBank=findViewById(R.id.nearbyBloodBank);
        nearbyBloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NearbyLocationActivity.this,NearbyPlaceResultActivity.class);
                intent.putExtra("search","Blood Bank");
                startActivity(intent);
            }
        });
        Button nearbyClinics=findViewById(R.id.nearbyClinics);
        nearbyClinics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NearbyLocationActivity.this,NearbyPlaceResultActivity.class);
                intent.putExtra("search","Clinic");
                startActivity(intent);
            }
        });
        Button nearbyPoliceStation=findViewById(R.id.nearbyPoliceStation);
        nearbyPoliceStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NearbyLocationActivity.this,NearbyPlaceResultActivity.class);
                intent.putExtra("search","Police Station");
                startActivity(intent);
            }
        });
        Button nearbyPharmacy=findViewById(R.id.nearbyPharmacy);
        nearbyPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NearbyLocationActivity.this,NearbyPlaceResultActivity.class);
                intent.putExtra("search","Pharmacy");
                startActivity(intent);
            }
        });
        Button nearbyNgo=findViewById(R.id.nearbyNgo);
        nearbyNgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NearbyLocationActivity.this,NearbyPlaceResultActivity.class);
                intent.putExtra("search","NGO");
                startActivity(intent);
            }
        });

    }

}
