package com.teamjhj.donator_247blood.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Activity.MyRequestActivity;
import com.teamjhj.donator_247blood.Adapter.DonnerListAdapter;
import com.teamjhj.donator_247blood.DataModel.AcceptingData;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.LiveBloodRequest;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.ApiClient;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.shadowfax.proswipebutton.ProSwipeButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * A simple {@link Fragment} subclass.
 */
public class DonnerListFragment extends Fragment {
    private final String DATE_FORMAT = "d/M/yyyy";
    LottieAnimationView animation_view2;
    DonnerListAdapter donnerListAdapter;
    private ArrayList<String> donnersKey;
    private ArrayList<UserProfile> donnersData = new ArrayList<>();
    private RecyclerView donnerListRecycler;
    private TextView noDonor;
    private ApiInterface apiInterface;
    private String reason, bloodGroup;
    LatLng location;
    int dayCount = 0;
    ArrayList<Integer> radiusData;
    Button viewRequestButton;
    ProSwipeButton proSwipeBtn;
    ImageView closeButtonDonnerList;
    public DonnerListFragment(String reason, String bloodGroup, LatLng location) {
        this.reason = reason;
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.radiusData = AppData.getRadiusList();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_donner_list, container, false);
        DatabaseReference liveRequest = FirebaseDatabase.getInstance().getReference("LiveRequest").child(FirebaseAuth.getInstance().getUid());
        DatabaseReference requestHistory = FirebaseDatabase.getInstance().getReference("RequestHistory").child("Emergency").child(FirebaseAuth.getInstance().getUid());
        proSwipeBtn = v.findViewById(R.id.awesome_btn);
        viewRequestButton = v.findViewById(R.id.viewRequestButton);
        closeButtonDonnerList = v.findViewById(R.id.closeButtonDonnerList);
        closeButtonDonnerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation_view2.cancelAnimation();
                SearchDonnerFragment.donnerListClose(1);

            }
        });
        viewRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyRequestActivity.class));
            }
        });
        proSwipeBtn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. Perform your async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // task success! show TICK icon in ProSwipeButton
                        liveRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    proSwipeBtn.showResultIcon(false);
                                    proSwipeBtn.setVisibility(View.INVISIBLE);
                                    viewRequestButton.setVisibility(View.VISIBLE);
                                    viewRequestButton.setText("View Pending Request!");
                                    UnsuccessfulDialog unsuccessfulDialog=new UnsuccessfulDialog("You Already Have A Pending Request! Close It For Request Again!");
                                    unsuccessfulDialog.show(getChildFragmentManager(),"UnsuccessfulDialog");


                                } else {
                                    for (int i = 0; i < donnersData.size(); i++) {
                                        sendNotification(donnersData.get(i));

                                    }
                                    Date date = Calendar.getInstance().getTime();
                                    LiveBloodRequest liveBloodRequest = new LiveBloodRequest(date, location.latitude, location.longitude, reason, bloodGroup);

                                    liveRequest.setValue(liveBloodRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //liveRequest.child("DonorsFound").child(FirebaseAuth.getInstance().getUid()).child("radius").setValue(radiusData.get(0));
                                    for (int i=0;i<donnersData.size();i++)
                                    {
                                        liveRequest.child("DonorsFound").child(donnersData.get(i).getUid()).child("radius").setValue(radiusData.get(i));
                                    }

                                    SuccessfulDialog successfulDialog=new SuccessfulDialog("Request Placed Successfully");
                                    successfulDialog.show(getChildFragmentManager(),"SuccessfulDialog");
                                            }
                                        }
                                    });
                                    proSwipeBtn.showResultIcon(true);
                                    proSwipeBtn.setVisibility(View.INVISIBLE);
                                    viewRequestButton.setVisibility(View.VISIBLE);

                                    // false if task failed
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }, 1000);
            }
        });
        donnerListRecycler = v.findViewById(R.id.donnerListRecycler);
        noDonor = v.findViewById(R.id.noDonor);
        donnerListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        animation_view2 = v.findViewById(R.id.animation_view2);
        donnerListAdapter = new DonnerListAdapter(getContext(), donnersData);
        donnerListRecycler.setAdapter(donnerListAdapter);

        TextView bloodGroupSearchDonor = v.findViewById(R.id.bloodGroupSearchDonor);
        bloodGroupSearchDonor.setText(bloodGroup);
        donnersKey = AppData.getDonners();
        new DonnerListTask().execute();


        return v;
    }

    private void moveToArchive() {
        DatabaseReference liveRequest = FirebaseDatabase.getInstance().getReference("LiveRequest").child(FirebaseAuth.getInstance().getUid());
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Cancelling Your Request!");
        progressDialog.show();
        liveRequest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LiveBloodRequest liveBloodRequest = dataSnapshot.getValue(LiveBloodRequest.class);
                if (liveBloodRequest != null) {
                    DatabaseReference archiveRef = FirebaseDatabase.getInstance().getReference("RequestArchive").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    String key = archiveRef.push().getKey();
                    archiveRef.child(key).setValue(liveBloodRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                liveRequest.child("DonorsFound").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            AcceptingData acceptingData = dataSnapshot1.getValue(AcceptingData.class);
                                            if (acceptingData != null) {
                                                if (acceptingData.isAccepted()) {
                                                    archiveRef.child(key).child("AcceptedDonor").child(Objects.requireNonNull(dataSnapshot1.getKey())).setValue(acceptingData);
                                                }
                                            }
                                        }
                                        liveRequest.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                    progressDialog.dismiss();
                                                    if (task.isSuccessful()) {
                                                        animation_view2.cancelAnimation();
                                                        SearchDonnerFragment.donnerListClose(1);
                                                        SuccessfulDialog successfulDialog=new SuccessfulDialog("Request Cancelled Successfully");
                                                        successfulDialog.show(getChildFragmentManager(),"SuccessfulDialog");

                                                    } else {
                                                        UnsuccessfulDialog unsuccessfulDialog=new UnsuccessfulDialog(task.getException().getLocalizedMessage());
                                                        unsuccessfulDialog.show(getChildFragmentManager(),"UnsuccessfulDialog");

                                                    }
                                                    //Toast.makeText(getContext(), "Request Cancelled Successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void sendNotification(UserProfile userProfile) {
        try {
            Date date = Calendar.getInstance().getTime();

            DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Notifications").child(userProfile.getUid());
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Log.e("UID", userProfile.getUid());
            //Log.e("Donor's Token",userProfile.getToken());
            String tempToken = AppData.getUserProfile().getToken();
            Log.e("MyToken", tempToken);
            String notificationMessage = AppData.getUserProfile().getName() + " Requesting For " + userProfile.getBloodGroup() + " Blood\nTap To Accept/View";
            NotificationData notificationData = new NotificationData(notificationMessage, "Nearby Blood Request!", AppData.getUserProfile().getUid());

            notificationData.setDate(date);
            notification.push().setValue(notificationData);
            NotificationSender notificationSender = new NotificationSender(userProfile.getToken(), notificationData);
            //NotificationSender notificationSender = new NotificationSender(tempToken, notificationData);
            Call<ResponseBody> bodyCall = apiInterface.sendNotification(notificationSender);
            bodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("Response Code", response.code() + "");
                    Log.e("Error MEssage", response.message());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    private class DonnerListTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (donnersKey.size() != 0) {
                for (int i = 0; i < donnersKey.size(); i++) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(donnersKey.get(i));
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                            if (userProfile != null) {
                                if (donnersData.size() != 20) {
                                    try {
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
                                        String start = userDay + "/" + userMonth + "/" + userYear;
                                        String end = day + "/" + month + "/" + year;
                                        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
                                        Date startDate, endDate;
                                        startDate = dateFormat.parse(start);
                                        endDate = dateFormat.parse(end);
                                        assert startDate != null;
                                        assert endDate != null;
                                        dayCount = (int) getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (dayCount > 90) {

                                        donnersData.add(userProfile);
                                        donnerListAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            } else {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        proSwipeBtn.setVisibility(View.INVISIBLE);
                        noDonor.setVisibility(View.VISIBLE);
                        animation_view2.setVisibility(View.VISIBLE);
                    }
                });


            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }


    }
}
