package com.teamjhj.donator_247blood.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.teamjhj.donator_247blood.Fragment.BloodFeedRequestArchiveFragment;
import com.teamjhj.donator_247blood.Fragment.EmergencyRequestArchiveFragment;

public class ArchiveTabAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public ArchiveTabAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new EmergencyRequestArchiveFragment();
        } else if (position == 1) {
            return new BloodFeedRequestArchiveFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
