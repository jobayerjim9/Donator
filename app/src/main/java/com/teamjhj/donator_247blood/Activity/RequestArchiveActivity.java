package com.teamjhj.donator_247blood.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.teamjhj.donator_247blood.Adapter.ArchiveTabAdapter;
import com.teamjhj.donator_247blood.R;

public class RequestArchiveActivity extends AppCompatActivity {
    private TabLayout archiveTabLayout;
    private ViewPager archiveViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_archive);
        archiveTabLayout = findViewById(R.id.archiveTabLayout);
        archiveViewPager = findViewById(R.id.archiveViewPager);
        ArchiveTabAdapter archiveTabAdapter = new ArchiveTabAdapter(getSupportFragmentManager(), archiveTabLayout.getTabCount());
        archiveViewPager.setAdapter(archiveTabAdapter);
        archiveTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                archiveViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        archiveViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(archiveTabLayout));

        ImageView archiveBackButton = findViewById(R.id.archiveBackButton);
        archiveBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
