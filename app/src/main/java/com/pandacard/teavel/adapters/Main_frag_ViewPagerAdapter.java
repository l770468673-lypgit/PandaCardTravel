package com.pandacard.teavel.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Main_frag_ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;

    public Main_frag_ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }


    public void setList(ArrayList<Fragment> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }


}