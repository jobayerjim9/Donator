package com.teamjhj.donator_247blood.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.astritveliu.boom.Boom;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.teamjhj.donator_247blood.DataModel.DistanceApiData;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;
import com.teamjhj.donator_247blood.RestApi.GoogleDistanceAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BloodFeedFragment extends Fragment {
    static ArrayList<NonEmergencyInfo> nonEmergencyInfos = new ArrayList<>();
    static ArrayList<NonEmergencyInfo> nonEmergencyInfosBackup = new ArrayList<>();
    static ArrayList<NonEmergencyInfo> nonEmergencyInfosSaved = new ArrayList<>();
    static ArrayList<NonEmergencyInfo> nonEmergencyInfosOwnPost = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
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
    static DistanceApiData distanceApiData;
    static FragmentActivity activity;
    public BloodFeedFragment() {
        filter = "location";

        // Required empty public constructor

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ctx = context;


        activity=getActivity();
        initPlace();
        View v = inflater.inflate(R.layout.blood_feed_new, container, false);
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
        //bloodRequestRecycler.setNestedScrollingEnabled(false);
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
        new Boom(filterStatus);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (nonEmergencyInfosBackup.size()==0)
                {
                    bloodFeedShimmer.stopShimmer();
                    bloodFeedShimmer.setVisibility(View.GONE);
                    nothingFoundBloodFeed.setVisibility(View.VISIBLE);
                    noPostTextView.setVisibility(View.VISIBLE);
                    requestAdapter.notifyDataSetChanged();
                }
            }
        }, 30000);
        return v;
    }
    public static void getDataFromDatabase() {
        i=0;
        if (filter.equals("date")) {
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
        nonEmergencyInfosBackup.clear();
        // bloodRequestRecycler.setAdapter(requestAdapter);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nonEmergencyInfos.clear();
                nonEmergencyInfosSaved.clear();
                nonEmergencyInfosOwnPost.clear();
                nonEmergencyInfosBackup.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (Integer.parseInt(Objects.requireNonNull(dataSnapshot1.getKey())) >= month) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            if (Integer.parseInt(dataSnapshot1.getKey()) == month) {
                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                    if (Integer.parseInt(Objects.requireNonNull(dataSnapshot2.getKey())) >= date) {
                                        NonEmergencyInfo nonEmergencyInfo = dataSnapshot3.getValue(NonEmergencyInfo.class);
                                        if (nonEmergencyInfo != null) {
                                            nonEmergencyInfo.setKey(dataSnapshot3.getKey());
                                            addData(nonEmergencyInfo);

                                            //nonEmergencyInfos.get(i).setDistamceFromUser(distance(latitude, longitude, nonEmergencyInfos.get(i).getLat(), nonEmergencyInfos.get(i).getLongt()));
                                            //updateDistanceData(nonEmergencyInfo);
                                        }
                                    }
                                }
                            } else {
                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                    NonEmergencyInfo nonEmergencyInfo = dataSnapshot3.getValue(NonEmergencyInfo.class);
                                    if (nonEmergencyInfo != null) {
//                                        DatabaseReference checkAccep=
                                        nonEmergencyInfo.setKey(dataSnapshot3.getKey());
                                        addData(nonEmergencyInfo);
                                        //updateDistanceData(nonEmergencyInfo);
                                        //addData(nonEmergencyInfo);
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
                //sortByLocation();
                Log.d("How Many Times?","Fuck");

//                if (nonEmergencyInfos.size() == 0) {
//                    bloodFeedShimmer.stopShimmer();
//                    bloodFeedShimmer.setVisibility(View.GONE);
//                    nothingFoundBloodFeed.setVisibility(View.VISIBLE);
//                    noPostTextView.setVisibility(View.VISIBLE);
//                    requestAdapter.notifyDataSetChanged();
//                }

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
                if (!nonEmergencyInfo.isClosed()) {
                    //nonEmergencyInfos.add(nonEmergencyInfo);
                    nonEmergencyInfosBackup.add(nonEmergencyInfo);
                    Log.e("Executed","Again"+nonEmergencyInfo.getKey());
                    sortByLocation();
                    //AppData.setNonEmergencyInfos(nonEmergencyInfos);
                }

            } else if (nonEmergencyInfo.getUid().equals(FirebaseAuth.getInstance().getUid()) && !nonEmergencyInfo.isClosed()) {
                nonEmergencyInfosOwnPost.add(nonEmergencyInfo);
                YourPostFragment.refreshRecycler(nonEmergencyInfosOwnPost, ctx);
            }


            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
            }
            if (nonEmergencyInfosBackup.isEmpty()) {
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

    int clickCount = 0;

    public static void sortByBloodGroup() {
        ArrayList<NonEmergencyInfo> filteredNonEmergency = new ArrayList<>();
        filterStatus.setText("Blood Group" + "(" + bloodGroup + ")");
        filter = "Blood";
        //RequestAdapter requestAdapter = new RequestAdapter(ctx, filteredNonEmergency);
        // bloodRequestRecycler.setAdapter(requestAdapter);

        for (int i = 0; i < nonEmergencyInfosBackup.size(); i++) {
            if (nonEmergencyInfosBackup.get(i).getSelectedBloodGroup().equals(bloodGroup)) {
                filteredNonEmergency.add(nonEmergencyInfosBackup.get(i));
            }
        }
        nonEmergencyInfos.clear();
        nonEmergencyInfos.addAll(filteredNonEmergency);
        requestAdapter=new RequestAdapter(ctx,nonEmergencyInfos);
        bloodRequestRecycler.setAdapter(requestAdapter);
        if (nonEmergencyInfos.isEmpty()) {
            // getDataFromDatabase();
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
    private static int i=0;
    public static void sortByLocation() {
        if(i==nonEmergencyInfosBackup.size())
        {

            Log.e("Index",i+"");
            Log.e("Size",nonEmergencyInfosBackup.size()+"");
            return;
        }
        else if(getLongitude()!=0 && getLatitude()!=0) {
                final int index=i;
                ApiInterface apiInterface = GoogleDistanceAPI.getClient().create(ApiInterface.class);
                Call<DistanceApiData> distanceData = apiInterface.getDistance(latitude + "," + longitude, nonEmergencyInfosBackup.get(index).getLat() + "," + nonEmergencyInfosBackup.get(index).getLongt());
                distanceData.enqueue(new Callback<DistanceApiData>() {


                    @Override
                    public void onResponse(Call<DistanceApiData> call, final Response<DistanceApiData> response) {

                        if(response.isSuccessful() && call.isExecuted())
                        {
                            DistanceApiData distanceApiData=response.body();
                            if (distanceApiData!=null) {

                                try {
                                    double distance = distanceApiData.getRows().get(0).getElements().get(0).getDistance().getValue() / 1000;
                                    Log.e("Distance", distance + " Index " + index);
                                    nonEmergencyInfosBackup.get(index).setDistamceFromUser(distance);
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                            }
                            if(index<nonEmergencyInfosBackup.size()) {
                                if(index==nonEmergencyInfosBackup.size()-1)
                                {



                                    Log.e("Last Index",index+"");
                                    if (filter.contains("location"))
                                    {
                                        nonEmergencyInfos.clear();
                                        nonEmergencyInfos.addAll(nonEmergencyInfosBackup);
                                        requestAdapter=new RequestAdapter(ctx,nonEmergencyInfos);
                                        bloodRequestRecycler.setAdapter(requestAdapter);
                                        Collections.sort(nonEmergencyInfos, new Comparator<NonEmergencyInfo>() {
                                            @Override
                                            public int compare(NonEmergencyInfo nonEmergencyInfo, NonEmergencyInfo t1) {
                                                return Double.compare(nonEmergencyInfo.getDistamceFromUser(), t1.getDistamceFromUser());
                                            }
                                        });
                                        filterStatus.setText("Nearby Location");
                                    }
                                    else if (filter.contains("Blood")) {nonEmergencyInfos.clear();

                                        sortByBloodGroup();
                                    } else if(filter.contains("date")) {
                                        nonEmergencyInfos.clear();
                                        nonEmergencyInfos.addAll(nonEmergencyInfosBackup);
                                        requestAdapter=new RequestAdapter(ctx,nonEmergencyInfos);
                                        bloodRequestRecycler.setAdapter(requestAdapter);
                                        filterStatus.setText("Earliest Date");
                                    }
                                    if (nonEmergencyInfosBackup.isEmpty()) {
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

                                    return;
                                }
                                else {
                                    i = index;
                                    i++;
                                    sortByLocation();
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<DistanceApiData> call, Throwable t) {

                    }
                });

        }
    }

    public static double getLongitude() {
        return longitude;
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
        //getDataFromDatabase();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        try {
                            setLatitude(location.getLatitude());
                            setLongitude(location.getLongitude());
                            getDataFromDatabase();
                            // requestAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 50, locationListener);
            } else {
                Toast.makeText(getContext(), "Please Allow Location Permission", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void initPlace() {
//        Places.initialize(Objects.requireNonNull(getContext()),getString(R.string.place_api_key));
//
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                setLatitude(location.getLatitude());
//                setLongitude(location.getLongitude());
//                try {
//
//                    SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putString("longitude", location.getLongitude() + "");
//                    editor.putString("latitude", location.getLatitude() + "");
//                    editor.apply();
//                    editor.commit();
//                    Log.d("Location Saved","Saved");
//                    getDataFromDatabase();
//                   // requestAdapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                try {
//                    Toast.makeText(context, "Please Enable Location", Toast.LENGTH_LONG).show();
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        };
//        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    try {
                        setLatitude(location.getLatitude());
                        setLongitude(location.getLongitude());
                        getDataFromDatabase();
                        // requestAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 50, locationListener);
        }
    }

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
