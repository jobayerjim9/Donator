package com.teamjhj.donator_247blood.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.teamjhj.donator_247blood.Fragment.BloodFeedFragment;
import com.teamjhj.donator_247blood.Fragment.ProfileFragment;
import com.teamjhj.donator_247blood.Fragment.SearchDonnerFragment;

public class BottomTabAdapter extends FragmentPagerAdapter {
    private int numOfTab;

    public BottomTabAdapter(FragmentManager fm, int numOfTab) {
        super(fm);
        this.numOfTab = numOfTab;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new SearchDonnerFragment();
        } else if (i == 1) {
            return new BloodFeedFragment();
        } else if (i == 2) {
            return new ProfileFragment();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}