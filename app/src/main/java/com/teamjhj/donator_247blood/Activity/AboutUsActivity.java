package com.teamjhj.donator_247blood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.teamjhj.donator_247blood.R;

import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity {
    private ImageView facebookSupportPage,messangerSupport,facebookGroupSupport,emailSupport,googlePlaySupport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about_us);
            Toolbar toolbar2;
            toolbar2 = findViewById(R.id.toolbar2);
            facebookSupportPage = findViewById(R.id.facebookSupportPage);
            messangerSupport = findViewById(R.id.messangerSupport);
            emailSupport = findViewById(R.id.emailSupport);
            googlePlaySupport = findViewById(R.id.googlePlaySupport);
            facebookGroupSupport = findViewById(R.id.facebookGroupSupport);
            setSupportActionBar(toolbar2);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar2.setNavigationIcon(R.drawable.ic_back);
            toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            facebookSupportPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://m.me/official.donator"));
                    startActivity(intent);
                }
            });
            messangerSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://m.me/join/AbZtD16HXEboLDRX"));
                    startActivity(intent);
                }
            });
            facebookGroupSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://www.facebook.com/groups/official.donator/"));
                    startActivity(intent);
                }
            });
            emailSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "visionaries.bd@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Donator Help");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Write Your Message Here.....\n\nSupport Token:- " + FirebaseAuth.getInstance().getUid());
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
            googlePlaySupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.teamjhj.donator_247blood"));
                    startActivity(intent);
                }
            });
        }
        catch (OutOfMemoryError e)
        {
            Toast.makeText(this, "You don't have memory to see high resolution pictures!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
