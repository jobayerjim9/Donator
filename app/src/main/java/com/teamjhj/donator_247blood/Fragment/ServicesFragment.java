package com.teamjhj.donator_247blood.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.teamjhj.donator_247blood.Adapter.ServicesPageAdapter;
import com.teamjhj.donator_247blood.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment {
    private TabLayout servicesTabLayout;
    private ViewPager servicesViewPager;
    private int[] tabIcons = {
            R.drawable.ic_blood,
            R.drawable.ic_support
    };

    public ServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_services, container, false);
        servicesTabLayout = v.findViewById(R.id.servicesTabLayout);
        servicesViewPager = v.findViewById(R.id.servicesViewPager);
        initialize();
        return v;
    }

    private void initialize() {
        ServicesPageAdapter servicesPageAdapter = new ServicesPageAdapter(getFragmentManager(), servicesTabLayout.getTabCount());
        servicesViewPager.setAdapter(servicesPageAdapter);
        setUpIcon();
        servicesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                servicesViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        servicesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(servicesTabLayout));
    }

    private void setUpIcon() {
        Objects.requireNonNull(servicesTabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(servicesTabLayout.getTabAt(1)).setIcon(tabIcons[1]);

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
