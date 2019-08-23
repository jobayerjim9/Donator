package com.teamjhj.donator_247blood.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class BackgroundJobService extends JobService {
    int i = 1;
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (jobCancelled) {
                        return;
                    }
                    updateMessengerActive();
                    Log.d("BackgroundJobService ", i + "");
                    i++;
                    try {
                        Thread.sleep(180000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        return true;

    }

    private void updateMessengerActive() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MessengerChat").child(FirebaseAuth.getInstance().getUid());
        Date date = Calendar.getInstance().getTime();
        databaseReference.child("date").setValue(date);

    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        Log.d("Background ", "Cancelled");
        jobCancelled = true;
        return true;
    }


}
