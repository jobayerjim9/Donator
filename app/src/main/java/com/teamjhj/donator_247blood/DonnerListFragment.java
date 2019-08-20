package com.teamjhj.donator_247blood;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;
import com.teamjhj.donator_247blood.RestApi.ApiClient;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonnerListFragment extends Fragment {

    LottieAnimationView animation_view2;
    DonnerListAdapter donnerListAdapter;
    private ArrayList<String> donnersKey;
    private ArrayList<UserProfile> donnersData = new ArrayList<>();
    private RecyclerView donnerListRecycler;
    private TextView noDonor;
    private ApiInterface apiInterface;
    private String reason, bloodGroup;

    public DonnerListFragment(String reason, String bloodGroup) {
        this.reason = reason;
        this.bloodGroup = bloodGroup;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_donner_list, container, false);

        donnerListRecycler = v.findViewById(R.id.donnerListRecycler);
        noDonor = v.findViewById(R.id.noDonor);
        donnerListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        animation_view2 = v.findViewById(R.id.animation_view2);
        donnerListAdapter = new DonnerListAdapter(getContext(), donnersData);
        donnerListRecycler.setAdapter(donnerListAdapter);
        ImageView closeButtonDonnerList = v.findViewById(R.id.closeButtonDonnerList);
        closeButtonDonnerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation_view2.cancelAnimation();
                SearchDonnerFragment.donnerListClose(1);
            }
        });
        TextView bloodGroupSearchDonor = v.findViewById(R.id.bloodGroupSearchDonor);
        bloodGroupSearchDonor.setText(bloodGroup);
        donnersKey = AppData.getDonners();
        new DonnerListTask().execute();


        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void sendNotification(UserProfile userProfile) {
        try {
            DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Notifications").child(FirebaseAuth.getInstance().getUid());
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Log.e("UID", userProfile.getUid());
            //Log.e("Donor's Token",userProfile.getToken());
            String tempToken = AppData.getUserProfile().getToken();
            Log.e("MyToken", tempToken);
            String notificationMessage = AppData.getUserProfile().getName() + " Searching " + userProfile.getBloodGroup() + " Blood\nContact: " + AppData.getUserProfile().getMobileNumber() + "\nReason: " + reason;
            NotificationData notificationData = new NotificationData(notificationMessage, "Nearby Blood Request!", AppData.getUserProfile().getMobileNumber());
            Date date = Calendar.getInstance().getTime();
            notificationData.setDate(date);
            notification.push().setValue(notificationData);
            NotificationSender notificationSender = new NotificationSender(userProfile.getToken(), notificationData);
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
                                donnersData.add(userProfile);
                                donnerListAdapter.notifyDataSetChanged();
                                sendNotification(userProfile);
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
