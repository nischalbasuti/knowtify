package com.hydratech19gmail.notify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaelse on 30-07-2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
    }
    public void addFragments(Fragment fragment, String tabTitle){
            this.fragments.add(fragment);
            this.tabTitles.add(tabTitle);
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
