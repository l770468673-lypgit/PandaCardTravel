package com.xlu.adapters;


 import android.content.Context;


 import androidx.fragment.app.Fragment;
 import androidx.fragment.app.FragmentManager;
 import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by giant on 2017/8/3.
 */

public class FindFragmentPageAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT=6;
    private Context mContext;
    private List<Fragment> fts;
    private String[] names;

    public FindFragmentPageAdapter(FragmentManager fm, List<Fragment> lists, String[] names, Context mContext) {
        super(fm);
        this.mContext = mContext;
        this.fts=lists;
        this.names=names;
    }

    @Override
    public Fragment getItem(int position) {
        return  fts.get(position);

    }

    @Override
    public int getCount() {
        return fts.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return names[position];
    }
}
