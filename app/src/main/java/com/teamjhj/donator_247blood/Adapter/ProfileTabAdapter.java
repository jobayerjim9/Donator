package com.teamjhj.donator_247blood.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.teamjhj.donator_247blood.Fragment.EditProfileFragment;
import com.teamjhj.donator_247blood.Fragment.HistoryFragment;

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
            return new EditProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
