package com.teamjhj.donator_247blood.Activity;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.teamjhj.donator_247blood.Adapter.BottomTabAdapter;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.Fragment.BloodRequestDialog;
import com.teamjhj.donator_247blood.Fragment.ViewPendingHistoryDialog;
import com.teamjhj.donator_247blood.Helper.BottomTabViewPager;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.Services.BackgroundJobService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private final String DATE_FORMAT = "d/M/yyyy";
    ReadableBottomBar readableBottomBar;
    Handler mHandler;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private UserProfile userProfile;
    private int dayCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String mobile = bundle.getString("mobile");
            if (mobile.contains("Comments")) {
                startActivity(new Intent(this, NotificationActivity.class));
                //finish();
            } else if (mobile.contains("Messenger")) {
                startActivity(new Intent(this, MessengerActivity.class));
            } else if (mobile.contentEquals(FirebaseAuth.getInstance().getUid())) {
                BloodRequestDialog bloodRequestDialog = new BloodRequestDialog(mobile);
                bloodRequestDialog.setCancelable(false);
                bloodRequestDialog.show(getSupportFragmentManager(), "LiveRequest");
//                Uri u = Uri.parse("tel:" + mobile);
//
//                // Create the intent and set the data for the
//                // intent as the phone number.
//                Intent i = new Intent(Intent.ACTION_DIAL, u);
//
//                try {
//                    startActivity(i);
//                    // finish();
//                } catch (SecurityException s) {
//                    Toast.makeText(this, s.getLocalizedMessage(), Toast.LENGTH_LONG)
//                            .show();
//                }
            } else if (mobile.contains("EmergencyRequest")) {
                startActivity(new Intent(this, MyRequestActivity.class));
            } else if (mobile.contains("BloodFeedReq")) {
                Intent i = new Intent(this, MyRequestActivity.class);
                i.putExtra("tabChange", true);
                startActivity(i);
            }
//            else if(mobile.contains("PendingHistory"))
//            {
//                ViewPendingHistoryDialog viewPendingHistoryDialog=new ViewPendingHistoryDialog();
//                viewPendingHistoryDialog.show(getSupportFragmentManager(),"PendingHistory");
//            }
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                updateTokenToDatabase(newToken);
            }
        });
        initializeView();

//        Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
//        PeriodicWorkRequest periodicWorkRequest=new PeriodicWorkRequest.Builder(MessengerActiveTimeWorker.class,15,TimeUnit.MINUTES)
//                .setConstraints(constraints)
//                .build();
//        WorkManager.getInstance(this).enqueue(periodicWorkRequest);


        ComponentName componentName = new ComponentName(this, BackgroundJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        int result = jobScheduler.schedule(jobInfo);


        DatabaseReference historyQuery = FirebaseDatabase.getInstance().getReference("PendingDonationConfirmation").child(FirebaseAuth.getInstance().getUid());
        historyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ViewPendingHistoryDialog viewPendingHistoryDialog = new ViewPendingHistoryDialog();
                    viewPendingHistoryDialog.show(getSupportFragmentManager(), "PendingHistory");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initializeView() {
        readableBottomBar = findViewById(R.id.readableBottomBar);
        final BottomTabViewPager bottomTabViewPager = findViewById(R.id.bottomTabViewPager);
        BottomTabAdapter bottomTabAdapter = new BottomTabAdapter(getSupportFragmentManager(), 3);
        bottomTabViewPager.setAdapter(bottomTabAdapter);
        bottomTabViewPager.setPagingEnabled(false);
        readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                bottomTabViewPager.setCurrentItem(i);

            }
        });
        getUserProfile();
        bottomTabViewPager.setOffscreenPageLimit(3);


    }

    private void updateTokenToDatabase(final String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile");
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("token").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Token Updated", token);
                } else {
                    Log.e("Token Error", task.getException().getMessage());
                }

            }
        });
    }

    private void getUserProfile() {
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userProfile = dataSnapshot.getValue(UserProfile.class);
                    if (userProfile != null) {
                        AppData.setUserProfile(userProfile);
                        updateDonationDate(userProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void updateDonationDate(UserProfile userProfile) {
        int day, month, year;
        int userDay, userMonth, userYear;
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
        userDay = userProfile.getLastDonationDate();
        userMonth = userProfile.getLastDonationMonth();
        userYear = userProfile.getLastDonationYear();
        System.out.println(day + "-" + month + "-" + year);
        System.out.println(userDay + "-" + userMonth + "-" + userYear);
        dayCount = 0;
        String start = userDay + "/" + userMonth + "/" + userYear;
        String end = day + "/" + month + "/" + year;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            assert startDate != null;
            assert endDate != null;
            dayCount = (int) getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        updateUserLocation(dayCount);
    }

    private long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    private void updateUserLocation(final int dayCount) {
        locationListener = new LocationListener() {
            DatabaseReference availableDonner = FirebaseDatabase.getInstance().getReference("AvailableDonner");
            GeoFire geoFire = new GeoFire(availableDonner);

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {

                    if (userProfile.isAvailable()) {
                        try {
                            geoFire.setLocation(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()), new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if (error != null) {
                                        Toast.makeText(MainActivity.this, "Something Went Wrong!\nCheck Your Internet Or Contact Support", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                            availableDonner.child(FirebaseAuth.getInstance().getUid()).child("BloodGroup").setValue(userProfile.getBloodGroup());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        availableDonner.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).removeValue();
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60, 100, locationListener);
                    }

                }
            }
        };
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60, 100, locationListener);
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 100, locationListener);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Toast.makeText(this, "Please Allow Location Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}
