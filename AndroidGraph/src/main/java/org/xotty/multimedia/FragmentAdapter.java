package org.xotty.multimedia;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;


public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mTitles;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mTitles.get(position);
    }

}
