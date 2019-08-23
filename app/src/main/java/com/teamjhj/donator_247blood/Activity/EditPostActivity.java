package com.teamjhj.donator_247blood.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.Fragment.BloodFeedFragment;
import com.teamjhj.donator_247blood.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class EditPostActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final int AUTOCOMPLETE_REQUEST_CODE = 1;
    String add;
    DatabaseReference post;
    NonEmergencyInfo nonEmergencyInfo;
    private Spinner spinnerProfile;
    private String bloodGroup;
    private Button dateProfile, timeProfile, locationProfile, saveButtonProfile;
    private TextInputLayout contactNumberProfile, reasonProfile;
    private int day = 0, month = 0, year = 0, hours, minute;
    private PlacesClient placesClient;
    private LocationListener locationListener;
    private LatLng pickedLocation;
    private List<Address> addresses;
    private LocationManager locationManager;
    private List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            nonEmergencyInfo = AppData.getEditPostInfo();
            hours = nonEmergencyInfo.getHour();
            minute = nonEmergencyInfo.getMinute();
            pickedLocation = new LatLng(nonEmergencyInfo.getLat(), nonEmergencyInfo.getLongt());
            add = nonEmergencyInfo.getLocation();


            spinnerProfile = findViewById(R.id.spinnerProfile);
            contactNumberProfile = findViewById(R.id.contactNumberProfile);
            reasonProfile = findViewById(R.id.reasonProfile);
            dateProfile = findViewById(R.id.dateProfile);
            timeProfile = findViewById(R.id.timeProfile);
            locationProfile = findViewById(R.id.locationProfile);
            saveButtonProfile = findViewById(R.id.saveButtonProfile);
            Objects.requireNonNull(contactNumberProfile.getEditText()).setText(nonEmergencyInfo.getPhone());
            Objects.requireNonNull(reasonProfile.getEditText()).setText(nonEmergencyInfo.getReason());
            String temp = nonEmergencyInfo.getHour() + " : " + nonEmergencyInfo.getMinute();
            timeProfile.setText(temp);
            temp = nonEmergencyInfo.getDate() + "/" + nonEmergencyInfo.getMonth() + "/" + nonEmergencyInfo.getYear();
            dateProfile.setText(temp);
            locationProfile.setText(nonEmergencyInfo.getLocation());
            dateProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePickerDialouge();
                }
            });
            timeProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePickerDialougue();
                }
            });
            locationProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(EditPostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditPostActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        Intent intent = new Autocomplete.IntentBuilder(
                                AutocompleteActivityMode.FULLSCREEN, fields)
                                .setCountry("bd")
                                .build(EditPostActivity.this);
                        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                    }
                }
            });
            saveButtonProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savePostToDatabase();
                }
            });
            setSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePostToDatabase() {
        post = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfo.getYear() + "").child(nonEmergencyInfo.getMonth() + "").child(nonEmergencyInfo.getDate() + "").child(nonEmergencyInfo.getKey());
        if (day == 0 && month == 0 && year == 0) {

            validate();
        } else {
            post.removeValue();
            nonEmergencyInfo.setDate(day);
            nonEmergencyInfo.setMonth(month);
            nonEmergencyInfo.setYear(year);
            post = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(year + "").child(month + "").child(day + "").child(nonEmergencyInfo.getKey());
            validate();
        }
    }

    private void validate() {

        if (!Objects.requireNonNull(contactNumberProfile.getEditText()).getText().toString().isEmpty()) {
            nonEmergencyInfo.setPhone(contactNumberProfile.getEditText().getText().toString());
        } else {
            Toast.makeText(EditPostActivity.this, "Contact Number Cannot Be Empty", Toast.LENGTH_LONG).show();
        }
        if (!Objects.requireNonNull(reasonProfile.getEditText()).getText().toString().isEmpty()) {
            nonEmergencyInfo.setReason(reasonProfile.getEditText().getText().toString());
        } else {
            Toast.makeText(EditPostActivity.this, "Reason Cannot Be Empty", Toast.LENGTH_LONG).show();
        }
        if (pickedLocation != null) {
            nonEmergencyInfo.setLat(pickedLocation.latitude);
            nonEmergencyInfo.setLongt(pickedLocation.longitude);
        }
        if (add != null) {
            nonEmergencyInfo.setLocation(add);
        }
        if (!Objects.requireNonNull(contactNumberProfile.getEditText()).getText().toString().isEmpty() || !Objects.requireNonNull(reasonProfile.getEditText()).getText().toString().isEmpty()) {
            nonEmergencyInfo.setHour(hours);
            nonEmergencyInfo.setMinute(minute);
            post.setValue(nonEmergencyInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        finish();
                        BloodFeedFragment.getDataFromDatabase();
                        Toast.makeText(EditPostActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        finish();
                        Toast.makeText(EditPostActivity.this, "Saved Not Successfully", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void setSpinner() {
        String compareValue = nonEmergencyInfo.getSelectedBloodGroup();
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bloodGroups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfile.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinnerProfile.setSelection(spinnerPosition);
        }
        spinnerProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroup = Objects.requireNonNull(adapter.getItem(position)).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.day = dayOfMonth;
        this.month = month + 1;
        this.year = year;
        String temp = dayOfMonth + "-" + month + "-" + year;
        dateProfile.setText(temp);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hours = hourOfDay;
        this.minute = minute;
        String temp = hourOfDay + " : " + minute;
        timeProfile.setText(temp);


    }

    private void showTimePickerDialougue() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void showDatePickerDialouge() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    // Start the autocomplete intent.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                add = place.getName();
                locationProfile.setText(place.getName());
                pickedLocation = new LatLng(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude);
                setPickedLocation(pickedLocation);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60, 30, locationListener);
            } else {
                Toast.makeText(this, "Please Allow Location Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setPickedLocation(LatLng pickedLocation) {
        this.pickedLocation = pickedLocation;

    }

}
