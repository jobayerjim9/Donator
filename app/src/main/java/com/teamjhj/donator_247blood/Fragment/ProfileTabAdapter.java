package com.teamjhj.donator_247blood.Fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ProfileTabAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public ProfileTabAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HistoryFragment();
        } else if (position == 1) {
            return new PostFragment();
        } else if (position == 2) {
            return new EditProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
