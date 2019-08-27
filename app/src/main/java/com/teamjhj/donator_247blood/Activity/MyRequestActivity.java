package com.teamjhj.donator_247blood.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.teamjhj.donator_247blood.Adapter.RequestsTabAdapter;
import com.teamjhj.donator_247blood.R;

public class MyRequestActivity extends AppCompatActivity {
    ViewPager requestsViewPager;
    TabLayout requestsTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request);
        requestsViewPager = findViewById(R.id.requestsViewPager);
        requestsTabLayout = findViewById(R.id.requestsTabLayout);
        RequestsTabAdapter requestsTabAdapter = new RequestsTabAdapter(getSupportFragmentManager(), requestsTabLayout.getTabCount());
        requestsViewPager.setAdapter(requestsTabAdapter);
        requestsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                requestsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        requestsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(requestsTabLayout));
        ImageView backButtonEmergency = findViewById(R.id.backButtonEmergency);
        backButtonEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        boolean changeTab = getIntent().getBooleanExtra("tabChange", false);
        if (changeTab) {

            requestsViewPager.setCurrentItem(1);
            TabLayout.Tab tab = requestsTabLayout.getTabAt(1);
            assert tab != null;
            tab.select();
        }
    }
}
