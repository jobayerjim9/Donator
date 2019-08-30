package com.teamjhj.donator_247blood.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Activity.AboutUsActivity;
import com.teamjhj.donator_247blood.Activity.AppIntoActivity;
import com.teamjhj.donator_247blood.Activity.SignInActivity;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import pro.midev.expandedmenulibrary.ExpandedMenuClickListener;
import pro.midev.expandedmenulibrary.ExpandedMenuItem;
import pro.midev.expandedmenulibrary.ExpandedMenuView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDonnerFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    static ExpandedMenuView expMenu;
    static ImageView logoImage;
    private static NestedScrollView searchDonnerScrollView;
    private static FrameLayout searchDonnerFrameLayout;
    private static FragmentManager fm;
    private static TextView searchDonorLabel;
    private static CardView searchDonorMessage, searchCardView;
    private final int AUTOCOMPLETE_REQUEST_CODE = 1;
    String add;
    AutocompleteSupportFragment place_autocomplete_fragment;
    //private TextView locationLabel;
    private Button pickupLocation, searchDonner;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private List<Address> addresses;
    private LatLng pickedLocation;
    private int pos;
    private String selectedBloodGroup, currentButtonName;
    private RadioGroup radioGroup;
    private RadioButton currentButton;
    private TextInputLayout reasonInput;
    private int radius = 0;
    private ArrayList<String> donners;
    private ArrayList<Integer> radiusList;
    private FrameLayout searchingAnimationFragment;
    private NonEmergencyInfo nonEmergencyInfo;
    private PlacesClient placesClient;
    private List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS);
    private ProgressDialog progressDialog;
    private LottieAnimationView blood_transfusion;
    private Button datePickerSearchDonor;
    private EditText bagsSearchDonor;
    int day,month,year;
    public SearchDonnerFragment() {
        day = 0;
        month = 0;
        year = 0;
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search_donner, container, false);
        add = null;
        initPlace();
        fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //locationLabellocationLabel = view.findViewById(R.id.locationLabel);
        pickupLocation = view.findViewById(R.id.pickLocation);
        datePickerSearchDonor = view.findViewById(R.id.datePickerSearchDonor);
        radioGroup = view.findViewById(R.id.radioGroup);
        reasonInput = view.findViewById(R.id.reasonInput);
        bagsSearchDonor = view.findViewById(R.id.bagsSearchDonor);
        expMenu = view.findViewById(R.id.expMenu);
        searchDonorLabel = view.findViewById(R.id.searchDonorLabel);
        searchDonnerScrollView = view.findViewById(R.id.searchDonnerScrollView);
        logoImage = view.findViewById(R.id.logoImage);
        blood_transfusion = view.findViewById(R.id.blood_transfusion);
        searchCardView = view.findViewById(R.id.searchCardView);
        searchDonorMessage = view.findViewById(R.id.searchDonorMessage);
        searchDonnerFrameLayout = view.findViewById(R.id.searchDonnerFrameLayout);
        searchingAnimationFragment = view.findViewById(R.id.searchingAnimationFragment);
        pickupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    Intent intent = new Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN, fields)
                            .setCountry("bd")
                            .build(Objects.requireNonNull(getContext()));
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                }

            }
        });
        datePickerSearchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialouge();
            }
        });
        expMenu.setIcons(
                new ExpandedMenuItem(R.drawable.ic_sign_out, "Sign Out"),
                new ExpandedMenuItem(R.drawable.ic_share, "Share"),
                new ExpandedMenuItem(R.drawable.ic_about, "About Us"),
                new ExpandedMenuItem(R.drawable.ic_support, "Intro")


        );
        expMenu.setOnItemClickListener(new ExpandedMenuClickListener() {
            @Override
            public void onItemClick(int i) {
                if (i == 0) {
                    FirebaseAuth.getInstance().signOut();
                    getContext().startActivity(new Intent(getActivity(), SignInActivity.class));
                    getActivity().finish();
                } else if (i == 1) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Join Donator Community " + "https://play.google.com/store/apps/details?id=com.teamjhj.donator_247blood";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Welcome To Join");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } else if (i == 2) {
                    getContext().startActivity(new Intent(getActivity(), AboutUsActivity.class));
                } else if (i == 3) {
                    startActivity(new Intent(getContext(), AppIntoActivity.class));
                }
            }
        });
        Spinner selectBloodGroup = view.findViewById(R.id.selectBloodGroup);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.bloodGroups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectBloodGroup.setAdapter(adapter);
        selectBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    pos = position;
                    selectedBloodGroup = Objects.requireNonNull(adapter.getItem(position)).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                searchDonnerScrollView.setVisibility(View.VISIBLE);
                searchDonorLabel.setVisibility(View.VISIBLE);
                searchDonorMessage.setVisibility(View.GONE);
                blood_transfusion.setVisibility(View.GONE);

                if (i == R.id.nonEmmargencyButton) {
                    datePickerSearchDonor.setVisibility(View.VISIBLE);
                    bagsSearchDonor.setVisibility(View.VISIBLE);
                    searchDonner.setText("Post To Blood Feed");
                } else if (i == R.id.emmargencyButton) {
                    datePickerSearchDonor.setVisibility(View.GONE);
                    bagsSearchDonor.setVisibility(View.GONE);

                    DatabaseReference checkReq = FirebaseDatabase.getInstance().getReference("LiveRequest").child(FirebaseAuth.getInstance().getUid());
                    checkReq.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                WarningDialog warningDialog = new WarningDialog();
                                warningDialog.setCancelable(false);
                                warningDialog.show(getChildFragmentManager(), "Warning!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    searchDonner.setText("Search Donor");
                }
            }
        });
        searchDonner = view.findViewById(R.id.searchDonner);
        searchDonner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Objects.requireNonNull(getActivity()));
                currentButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioGroup.getCheckedRadioButtonId() == R.id.emmargencyButton) {
                    currentButtonName = "Emergency";

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.nonEmmargencyButton) {
                    currentButtonName = "Non-Emergency";

                }
                validateInfo();
            }
        });
        return view;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void donnerListClose(int n) {
        if (n == 1) {
            DonnerListFragment fragment = (DonnerListFragment) fm.findFragmentById(R.id.searchDonnerFrameLayout);
            FragmentTransaction donnerListFragment = fm.beginTransaction();
            donnerListFragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            if (fragment != null) {
                donnerListFragment.remove(fragment).commit();
            }

            searchDonnerScrollView.setVisibility(View.VISIBLE);
            searchDonnerFrameLayout.setVisibility(View.GONE);
            searchCardView.setVisibility(View.VISIBLE);
            expMenu.setVisibility(View.VISIBLE);
            searchDonorLabel.setVisibility(View.VISIBLE);
            logoImage.setVisibility(View.VISIBLE);
        }
//        else if (n == 2) {
//            NonEmergencyInfoFragment fragment = (NonEmergencyInfoFragment) fm.findFragmentById(R.id.searchDonnerFrameLayout);
//            FragmentTransaction donnerListFragment = fm.beginTransaction();
//
//            if (fragment != null) {
//                donnerListFragment.remove(fragment).commit();
//            }
//
//            searchDonnerScrollView.setVisibility(View.VISIBLE);
//            searchDonnerFrameLayout.setVisibility(View.GONE);
//        }


    }



    @Override
    public void onPause() {
        super.onPause();
    }
    private void validateInfo() {

        System.out.println(pickedLocation + "-" + selectedBloodGroup + "-" + currentButtonName + "-" + reasonInput.getEditText().getText().toString());
        if (pickedLocation == null) {
            Toast.makeText(getContext(), "Pick The Location Of The Hospital Where Blood Need", Toast.LENGTH_LONG).show();
        }
        else if(selectedBloodGroup==null)
        {
            Toast.makeText(getContext(), "Select Your Desired Blood Group", Toast.LENGTH_SHORT).show();
        }
        else if(reasonInput.getEditText().getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), " Enter Why You Need Blood!\n Example:- Dengue", Toast.LENGTH_SHORT).show();
        }
        else {
            if (currentButtonName.equals("Emergency")) {
                Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                searchDonnerScrollView.setVisibility(View.GONE);
                searchingAnimationFragment.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.searchingAnimationFragment, new SearchingAnimationFragment());
                fragmentTransaction.commit();
                radius = 0;
                donners = new ArrayList<>();
                radiusList = new ArrayList<>();
                searchCardView.setVisibility(View.GONE);
                expMenu.setVisibility(View.GONE);
                searchDonorLabel.setVisibility(View.GONE);
                logoImage.setVisibility(View.GONE);
                searchNearbyDonner();
            } else {
                String bags=bagsSearchDonor.getText().toString();
                if(bags.isEmpty())
                {
                    Toast.makeText(getContext(), "Enter How Many Bags Of Blood You Need", Toast.LENGTH_SHORT).show();
                }
                else if(day==0 || month ==0 || year==0)
                {
                    Toast.makeText(getContext(), "Pick A Date!", Toast.LENGTH_SHORT).show();
                }
                else {
                    nonEmergencyInfo = new NonEmergencyInfo(add, selectedBloodGroup, reasonInput.getEditText().getText().toString(), pickedLocation.latitude, pickedLocation.longitude, FirebaseAuth.getInstance().getUid());
                    nonEmergencyInfo.setDate(day);
                    nonEmergencyInfo.setMonth(month);
                    nonEmergencyInfo.setYear(year);
                    nonEmergencyInfo.setPhone(AppData.getUserProfile().getMobileNumber());
                    nonEmergencyInfo.setName(AppData.getUserProfile().getName());
                    nonEmergencyInfo.setBags(bags);
                    uploadPost();
                    Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        }
    }
    private void uploadPost() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait! Posting Your Post!");
        progressDialog.setCancelable(true);
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfo.getYear() + "").child(nonEmergencyInfo.getMonth() + "").child(nonEmergencyInfo.getDate() + "");
        databaseReference.push().setValue(nonEmergencyInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    BloodFeedFragment.getDataFromDatabase();
                    SuccessfulDialog successfulDialog=new SuccessfulDialog("Successfully Posted!");
                    successfulDialog.show(getChildFragmentManager(),"SuccessfulDialog");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                UnsuccessfulDialog unsuccessfulDialog=new UnsuccessfulDialog(e.getLocalizedMessage());
                unsuccessfulDialog.show(getChildFragmentManager(),"UnsuccessfulDialog");
                progressDialog.dismiss();
            }
        });

    }
    private void searchNearbyDonner() {

        final DatabaseReference availableDonner = FirebaseDatabase.getInstance().getReference("AvailableDonner");
        GeoFire geoFire = new GeoFire(availableDonner);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickedLocation.latitude, pickedLocation.longitude), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {
                DatabaseReference select = availableDonner.child(key).child("BloodGroup");
                select.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            String blood = dataSnapshot.getValue(String.class);
                            Log.e("BloodGroupSearchDonor", blood);
                            if (blood.contains(selectedBloodGroup)) {
                                if (!donners.toString().contains(key)) {
                                    if (!Objects.equals(FirebaseAuth.getInstance().getUid(), key)) {
                                        donners.add(key);
                                        radiusList.add(radius);
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (donners.size() <= 40 && radius <= 25) {
                    radius++;
                    searchNearbyDonner();
                } else {
                    AppData.setDonners(donners);
                    AppData.setRadiusList(radiusList);
                    getDonnerList();

                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Toast.makeText(getContext(), "Your Internet Is Too Slow!", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getDonnerList() {

        SearchingAnimationFragment fragment = (SearchingAnimationFragment) fm.findFragmentById(R.id.searchingAnimationFragment);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.remove(fragment).commit();
        }

        searchingAnimationFragment.setVisibility(View.GONE);
        searchDonnerFrameLayout.setVisibility(View.VISIBLE);

        FragmentTransaction donnerListFragment = fm.beginTransaction();
        donnerListFragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        donnerListFragment.add(R.id.searchDonnerFrameLayout, new DonnerListFragment(reasonInput.getEditText().getText().toString(), selectedBloodGroup, pickedLocation));
        donnerListFragment.commit();


    }

    public void setPickedLocation(LatLng pickedLocation) {
        this.pickedLocation = pickedLocation;

    }

    private void initPlace() {
        Places.initialize(Objects.requireNonNull(getContext()), "AIzaSyAjt-nsOSRT-l8UYDNAe7zzr7molPPCb4Y");
        placesClient = Places.createClient(getContext());
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    Geocoder geocoder = new Geocoder(getContext());
                    pickedLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                        pickupLocation.setText(addresses.get(0).getAddressLine(0));
                        add = addresses.get(0).getAddressLine(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                try {
                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 100, locationListener);
                    }
                    Toast.makeText(getContext(), "Please Enable Location", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 100, locationListener);
        }


// Start the autocomplete intent.
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                add = place.getName();
                pickupLocation.setText(place.getName());
                pickedLocation = new LatLng(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude);
                setPickedLocation(pickedLocation);
                // locationLabel.setText("Your Picked Location:");
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
    }
    private void showDatePickerDialouge() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(getContext()),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.day = dayOfMonth;
        this.month = month + 1;
        this.year = year;
        String temp = dayOfMonth + "-" + this.month + "-" + year;
        datePickerSearchDonor.setText(temp);



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60, 30, locationListener);
            } else {
                Toast.makeText(getContext(), "Please Allow Location Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}
