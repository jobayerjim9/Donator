package com.teamjhj.donator_247blood;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ServicesPageAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public ServicesPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new BloodFeedFragment();
        } else if (position == 1) {
            return new SupportFragment();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
