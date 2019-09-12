package com.teamjhj.donator_247blood.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.DataModel.ConfigData;
import com.teamjhj.donator_247blood.Fragment.NewUpdateAvailableDialog;
import com.teamjhj.donator_247blood.R;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

public class LoadingScreenActivity extends AppCompatActivity {
    private String version = "1.4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        try {
            NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(this).setButtonColor(R.color.material_background).build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
                                    if (getIntent().getExtras() != null) {
                                        Bundle bundle = getIntent().getExtras();
                                        Log.e("FucckingBundle",bundle.toString());
                                        String tag = bundle.getString("mobile");
                                        Intent i=new Intent(LoadingScreenActivity.this,MainActivity.class);
                                        i.putExtra("mobile",tag);
                                        startActivity(i);
                                        finish();
                                    }
                                    else {
                                        Log.e("Failed To Get Jim","Try New Logic Fucking Dumb");
                                        validate();
                                    }
                                } else {
                                    //startActivity(new Intent(LoadingScreenActivity.this, SignInActivity.class));
                                    startActivity(new Intent(LoadingScreenActivity.this,AppIntoActivity.class));
                                    finish();
                                }
                            } else {
                                try {
                                    NewUpdateAvailableDialog updateAvailableDialog = new NewUpdateAvailableDialog();
                                    updateAvailableDialog.setCancelable(false);
                                    updateAvailableDialog.show(getSupportFragmentManager(), "update");
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
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
