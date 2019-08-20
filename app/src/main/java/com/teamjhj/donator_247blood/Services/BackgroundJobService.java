package com.teamjhj.donator_247blood.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

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
                    Log.d("Background ", i + "");
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        Log.d("Background ", "Cancelled");
        jobCancelled = true;
        return true;
    }
}
