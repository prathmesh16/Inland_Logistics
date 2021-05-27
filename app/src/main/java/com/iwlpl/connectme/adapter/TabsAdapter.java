package com.iwlpl.connectme.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iwlpl.connectme.fragment.PickupMasterListFragment;


public class TabsAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    Context context;

    public TabsAdapter(FragmentManager fm, int totalTabs, Context context) {
        super(fm);
        this.totalTabs = totalTabs;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        return new PickupMasterListFragment(position);
    }

    @Override
    public int getCount() {
        return totalTabs;
    }


}
