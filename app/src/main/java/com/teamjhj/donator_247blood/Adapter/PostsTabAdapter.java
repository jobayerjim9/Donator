package com.teamjhj.donator_247blood.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.teamjhj.donator_247blood.Fragment.PendingRequestFragment;
import com.teamjhj.donator_247blood.Fragment.SavedRequestFragment;
import com.teamjhj.donator_247blood.Fragment.YourPostFragment;

public class PostsTabAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public PostsTabAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PendingRequestFragment();
        } else if (position == 1) {
            return new YourPostFragment();
        } else if (position == 2) {
            return new SavedRequestFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
