package com.teamjhj.donator_247blood;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.teamjhj.donator_247blood.DataModel.ConfigData;
import com.teamjhj.donator_247blood.Fragment.NewUpdateAvailableDialog;

import java.util.Objects;

public class LoadingScreenActivity extends AppCompatActivity {
    private String version = "1.0.6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);


//        ConstraintLayout loadingLayout = findViewById(R.id.loadingLayout);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loadingLayout.getBackground();
//        animationDrawable.setEnterFadeDuration(1000);
//        animationDrawable.setExitFadeDuration(2000);
//        animationDrawable.start();


        final Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final DatabaseReference config = FirebaseDatabase.getInstance().getReference("ConfigFile");
                config.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ConfigData configData = dataSnapshot.getValue(ConfigData.class);
                        if (configData != null) {
                            if (configData.getVersion().equals(version)) {
                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    validate();
                                } else {
                                    startActivity(new Intent(LoadingScreenActivity.this, SignInActivity.class));
                                    finish();
                                }
                            } else {
                                NewUpdateAvailableDialog updateAvailableDialog = new NewUpdateAvailableDialog();
                                updateAvailableDialog.setCancelable(false);
                                updateAvailableDialog.show(getSupportFragmentManager(), "update");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//                startActivity(new Intent(LoadingScreenActivity.this, MainActivity.class));

            }
        };
        thread.start();
    }

    private void validate() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    startActivity(new Intent(LoadingScreenActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoadingScreenActivity.this, "Sign Up Doesn't Completed Properly! Do it Again!", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(LoadingScreenActivity.this, SignInActivity.class));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
