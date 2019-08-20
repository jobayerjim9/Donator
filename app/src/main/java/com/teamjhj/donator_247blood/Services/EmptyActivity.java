package com.teamjhj.donator_247blood.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.teamjhj.donator_247blood.Activity.NotificationActivity;
import com.teamjhj.donator_247blood.R;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String mobile = bundle.getString("mobile");
            if (mobile.contains("+88")) {
                Uri u = Uri.parse("tel:" + mobile);

                // Create the intent and set the data for the
                // intent as the phone number.
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try {
                    startActivity(i);
                    finish();
                } catch (SecurityException s) {
                    Toast.makeText(this, s.getLocalizedMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                startActivity(new Intent(this, NotificationActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
}
