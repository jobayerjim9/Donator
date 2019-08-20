package com.teamjhj.donator_247blood;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BottomTabAdapter extends FragmentPagerAdapter {
    private int numOfTab;

    BottomTabAdapter(FragmentManager fm, int numOfTab) {
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