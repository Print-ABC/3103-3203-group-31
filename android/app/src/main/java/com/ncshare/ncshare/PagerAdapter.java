package com.ncshare.ncshare;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.FriendListFragment;
import fragments.FriendPendingFragment;
import fragments.FriendSearchFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FriendListFragment tab1 = new FriendListFragment();
                return tab1;
            case 1:
                FriendSearchFragment tab2 = new FriendSearchFragment();
                return tab2;
            case 2:
                FriendPendingFragment tab3 = new FriendPendingFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}