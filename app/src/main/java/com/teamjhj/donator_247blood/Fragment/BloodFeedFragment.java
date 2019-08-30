package com.teamjhj.donator_247blood.Fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Activity.PostsActivity;
import com.teamjhj.donator_247blood.Adapter.RequestAdapter;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class BloodFeedFragment extends Fragment {
    static ArrayList<NonEmergencyInfo> nonEmergencyInfos = new ArrayList<>();
    static ArrayList<NonEmergencyInfo> nonEmergencyInfosSaved = new ArrayList<>();
    static ArrayList<NonEmergencyInfo> nonEmergencyInfosOwnPost = new ArrayList<>();
    static Context ctx;
    static String filter, bloodGroup;
    static RequestAdapter requestAdapter;
    static FragmentManager fm;
    static private SwipeRefreshLayout swipeLayout;
    static private DatabaseReference databaseReference;
    static private int date, year, month;
    static private RecyclerView bloodRequestRecycler;
    private static double longitude = 0;
    private static double latitude = 0;
    private static TextView filterStatus;
    private static LottieAnimationView nothingFoundBloodFeed;
    private static TextView noPostTextView;
    ProgressDialog progressDialog;
    private Handler mHandler;
    private CardView sortButton;
    private Spinner sortSpinner;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private static ShimmerFrameLayout bloodFeedShimmer;
    public BloodFeedFragment() {
        filter = "no";
        // Required empty public constructor

    }

    public static void getDataFromDatabase() {
        if (filter.equals("no")) {
            filterStatus.setText("Earliest Date");
        } else if (filter.equals("location")) {
            filterStatus.setText("Nearby Location");
        }
        nothingFoundBloodFeed.setVisibility(View.GONE);
        noPostTextView.setVisibility(View.GONE);
        bloodFeedShimmer.startShimmer();
        bloodFeedShimmer.setVisibility(View.VISIBLE);
        nonEmergencyInfos.clear();
        nonEmergencyInfosSaved.clear();
        nonEmergencyInfosOwnPost.clear();
        // bloodRequestRecycler.setAdapter(requestAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nonEmergencyInfos.clear();
                nonEmergencyInfosSaved.clear();
                nonEmergencyInfosOwnPost.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (Integer.parseInt(Objects.requireNonNull(dataSnapshot1.getKey())) >= month) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            if (Integer.parseInt(dataSnapshot1.getKey()) == month) {
                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                    if (Integer.parseInt(Objects.requireNonNull(dataSnapshot2.getKey())) >= date) {
                                        NonEmergencyInfo nonEmergencyInfo = dataSnapshot3.getValue(NonEmergencyInfo.class);
                                        if (nonEmergencyInfo != null) {
                                            bloodFeedShimmer.stopShimmer();
                                            bloodFeedShimmer.setVisibility(View.GONE);
                                            nonEmergencyInfo.setKey(dataSnapshot3.getKey());
                                            addData(nonEmergencyInfo);
                                        }
                                    }
                                }
                            } else {
                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                    NonEmergencyInfo nonEmergencyInfo = dataSnapshot3.getValue(NonEmergencyInfo.class);
                                    if (nonEmergencyInfo != null) {
                                        addData(nonEmergencyInfo);
                                    }
                                }
                            }
                        }

                    }

                }
                if (nonEmergencyInfosSaved.isEmpty()) {
                    PostsActivity.updateData(nonEmergencyInfosSaved);
                }
                if (nonEmergencyInfosOwnPost.size() == 0) {
                    YourPostFragment.refreshRecycler(nonEmergencyInfosOwnPost, ctx);
                }
                if (nonEmergencyInfos.size()==0)
                {
                    bloodFeedShimmer.stopShimmer();
                    bloodFeedShimmer.setVisibility(View.GONE);
                    nothingFoundBloodFeed.setVisibility(View.VISIBLE);
                    noPostTextView.setVisibility(View.VISIBLE);
                    requestAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private static void addData(final NonEmergencyInfo nonEmergencyInfo) {
        try {
            if (!nonEmergencyInfo.getUid().equals(FirebaseAuth.getInstance().getUid()) && !nonEmergencyInfo.isClosed()) {
                DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfo.getYear() + "").child(nonEmergencyInfo.getMonth() + "").child(nonEmergencyInfo.getDate() + "").child(nonEmergencyInfo.getKey()).child("LikedPeople").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                postReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            nonEmergencyInfosSaved.add(nonEmergencyInfo);
                            PostsActivity.updateData(nonEmergencyInfosSaved);

                        }

//                        else {
//                            PostsActivity.updateData(nonEmergencyInfosSaved);
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(!nonEmergencyInfo.isClosed()) {
                    nonEmergencyInfos.add(nonEmergencyInfo);
                }
                if (filter.contains("location")) {
                    sortByLocation();
                } else if (filter.contains("Blood")) {
                    sortByBloodGroup();
                } else {
                    filterStatus.setText("Earliest Date");
                    if (nonEmergencyInfos.isEmpty()) {
                        bloodFeedShimmer.stopShimmer();
                        bloodFeedShimmer.setVisibility(View.GONE);
                        nothingFoundBloodFeed.setVisibility(View.VISIBLE);
                        noPostTextView.setVisibility(View.VISIBLE);
                    } else {
                        nothingFoundBloodFeed.setVisibility(View.GONE);
                        noPostTextView.setVisibility(View.GONE);
                        requestAdapter.notifyDataSetChanged();
                    }
                }

                AppData.setNonEmergencyInfos(nonEmergencyInfos);
            } else if (nonEmergencyInfo.getUid().equals(FirebaseAuth.getInstance().getUid()) && !nonEmergencyInfo.isClosed()) {
                nonEmergencyInfosOwnPost.add(nonEmergencyInfo);
                YourPostFragment.refreshRecycler(nonEmergencyInfosOwnPost, ctx);
            }


            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
            }
            if (nonEmergencyInfos.isEmpty()) {
                nothingFoundBloodFeed.setVisibility(View.VISIBLE);
                noPostTextView.setVisibility(View.VISIBLE);
                bloodFeedShimmer.stopShimmer();
                bloodFeedShimmer.setVisibility(View.GONE);
            }
//            if (nonEmergencyInfosOwnPost.isEmpty()) {
//                YourPostFragment.refreshRecycler(new ArrayList<NonEmergencyInfo>(), ctx);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void sortByBloodGroup() {
        ArrayList<NonEmergencyInfo> filteredNonEmergency = new ArrayList<>();
        filterStatus.setText("Blood Group" + "(" + bloodGroup + ")");
        filter = "Blood";
        //RequestAdapter requestAdapter = new RequestAdapter(ctx, filteredNonEmergency);
        // bloodRequestRecycler.setAdapter(requestAdapter);

        for (int i = 0; i < nonEmergencyInfos.size(); i++) {
            if (nonEmergencyInfos.get(i).getSelectedBloodGroup().equals(bloodGroup)) {
                filteredNonEmergency.add(nonEmergencyInfos.get(i));
            }
        }
        nonEmergencyInfos.clear();
        nonEmergencyInfos.addAll(filteredNonEmergency);
        requestAdapter.notifyDataSetChanged();
        if (nonEmergencyInfos.isEmpty()) {
            nothingFoundBloodFeed.setVisibility(View.VISIBLE);
            noPostTextView.setVisibility(View.VISIBLE);
            bloodFeedShimmer.stopShimmer();
            bloodFeedShimmer.setVisibility(View.GONE);
        } else {
            bloodFeedShimmer.stopShimmer();
            bloodFeedShimmer.setVisibility(View.GONE);
            nothingFoundBloodFeed.setVisibility(View.GONE);
            noPostTextView.setVisibility(View.GONE);
            requestAdapter.notifyDataSetChanged();

        }

    }

    public static void sortByLocation() {
        double longitude = 0, latitude = 0;

        if (getLatitude() == 0 && getLongitude() == 0) {
            try {
                SharedPreferences pref = PreferenceManager
                        .getDefaultSharedPreferences(ctx);
                longitude = Double.parseDouble(pref.getString("longitude", null));
                latitude = Double.parseDouble(pref.getString("latitude", null));
            } catch (Exception e) {
                latitude = getLatitude();
                longitude = getLongitude();
                e.printStackTrace();
            }
        } else {
            latitude = getLatitude();
            longitude = getLongitude();
        }
        filter = "location";

        if (longitude != 0 && latitude != 0) {
            // filter = "location";
            for (int i = 0; i < nonEmergencyInfos.size(); i++) {
                nonEmergencyInfos.get(i).setDistamceFromUser(distance(latitude, longitude, nonEmergencyInfos.get(i).getLat(), nonEmergencyInfos.get(i).getLongt()));
            }
            Collections.sort(nonEmergencyInfos, new Comparator<NonEmergencyInfo>() {
                @Override
                public int compare(NonEmergencyInfo nonEmergencyInfo, NonEmergencyInfo t1) {
                    return Double.compare(nonEmergencyInfo.getDistamceFromUser(), t1.getDistamceFromUser());
                }
            });
            filterStatus.setText("Nearby Location");
            if (nonEmergencyInfos.isEmpty()) {
                nothingFoundBloodFeed.setVisibility(View.VISIBLE);
                noPostTextView.setVisibility(View.VISIBLE);
                bloodFeedShimmer.stopShimmer();
                bloodFeedShimmer.setVisibility(View.GONE);
            } else {
                nothingFoundBloodFeed.setVisibility(View.GONE);
                noPostTextView.setVisibility(View.GONE);
                bloodFeedShimmer.stopShimmer();
                bloodFeedShimmer.setVisibility(View.GONE);

            }
            requestAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(ctx, "Please Wait! Getting Yout Current Location!", Toast.LENGTH_LONG).show();
        }
    }

    public static double getLongitude() {
        return longitude;
    }



    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String getFilter() {
        return filter;
    }

    public static void setFilter(String filter) {
        BloodFeedFragment.filter = filter;
    }

    public static String getBloodGroup() {
        return bloodGroup;
    }

    public static void setBloodGroup(String bloodGroup) {
        BloodFeedFragment.bloodGroup = bloodGroup;
    }

    //    private final Runnable m_Runnable = new Runnable()
//    {
//        public void run()
//
//        {
//
//            BloodFeedFragment.getDataFromDatabase();
//            mHandler.postDelayed(m_Runnable,60000);
//        }
//
//    };

    private void checkEmpty() {

    }

    public void setLongitude(double longitude) {
        BloodFeedFragment.longitude = longitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        BloodFeedFragment.latitude = latitude;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 50, locationListener);
            } else {
                Toast.makeText(getContext(), "Please Allow Location Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ctx = getContext();
        initPlace();
        View v = inflater.inflate(R.layout.fragment_blood_feed, container, false);
        fm = getChildFragmentManager();
        date = Calendar.getInstance().get(Calendar.DATE);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        bloodRequestRecycler = v.findViewById(R.id.bloodRequestRecycler);
        bloodFeedShimmer = v.findViewById(R.id.bloodFeedShimmer);
        bloodFeedShimmer.startShimmer();
        bloodRequestRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        requestAdapter = new RequestAdapter(ctx, nonEmergencyInfos);
        bloodRequestRecycler.setAdapter(requestAdapter);
        bloodRequestRecycler.setHasFixedSize(true);
        //bloodRequestRecycler.
        databaseReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(year + "");
        sortButton = v.findViewById(R.id.sortButton);
        swipeLayout = v.findViewById(R.id.swipeLayout);

        sortSpinner = v.findViewById(R.id.sortSpinner);


        filterStatus = v.findViewById(R.id.filterStatus);
        nothingFoundBloodFeed = v.findViewById(R.id.nothingFoundBloodFeed);
        noPostTextView = v.findViewById(R.id.noPostTextView);
        noPostTextView.setVisibility(View.GONE);
        nothingFoundBloodFeed.setVisibility(View.GONE);

        filterStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noPostTextView.setVisibility(View.GONE);
                nothingFoundBloodFeed.setVisibility(View.GONE);
                FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
                filterDialogFragment.show(getChildFragmentManager(), "filterDialog");

            }
        });
        // retieveDataFromFirebase();

        getDataFromDatabase();

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);

                getDataFromDatabase();
            }
        });
        this.mHandler = new Handler();
//        m_Runnable.run();
        return v;
    }

    private void initPlace() {
        Places.initialize(Objects.requireNonNull(getContext()), "AIzaSyAjt-nsOSRT-l8UYDNAe7zzr7molPPCb4Y");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setLatitude(location.getLatitude());
                setLongitude(location.getLongitude());
                try {

                    SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("longitude", location.getLongitude() + "");
                    editor.putString("latitude", location.getLatitude() + "");
                    editor.apply();
                    editor.commit();
                    Log.d("Location Saved","Saved");
                    requestAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
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
                Toast.makeText(getContext(), "Please Enable Location", Toast.LENGTH_LONG).show();
            }
        };
        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 50, locationListener);
        }
    }
}
