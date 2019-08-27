package com.teamjhj.donator_247blood.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.teamjhj.donator_247blood.Fragment.EmergencyRequestFragment;
import com.teamjhj.donator_247blood.Fragment.YourPostFragment;

public class RequestsTabAdapter extends FragmentStatePagerAdapter {
    int numOfTab;

    public RequestsTabAdapter(FragmentManager fm, int numOfTab) {
        super(fm);
        this.numOfTab = numOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new EmergencyRequestFragment();
        } else if (position == 1) {
            return new YourPostFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
