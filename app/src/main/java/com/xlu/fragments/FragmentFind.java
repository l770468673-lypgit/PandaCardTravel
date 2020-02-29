package com.xlu.fragments;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.fragments.MainFrag_home;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.adapters.FindFragmentPageAdapter;
import com.xlu.po.MyEvent;
import com.xlu.uis.ActivitySelectCity;
import com.xlu.utils.Constance;


import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


//             MD5: CD:0E:C9:A1:57:C5:ED:D8:37:5E:BD:0F:82:2E:A0:F2
//           SHA1: 12:5F:06:8D:97:BE:1D:9C:12:0A:69:86:ED:AE:A1:27:66:EA:23:42
//           SHA256: E6:C5:B1:69:EA:A4:7B:D8:5E:AE:2A:DA:DA:23:3C:A1:5C:C1:D7:E0:6E:6B:D3:C4:D5:71:3A:DF:85:CE:BF:76
//           签名算法名称: SHA256withRSA
//           版本: 3


/**
 * Created by giant on 2017/8/3.
 */

public class FragmentFind extends Fragment implements View.OnClickListener {
    View view;
    private TextView tvTilteName;
    private TabLayout tabFind;
    private ViewPager vpFind;
    String[] names = {"线路", "酒店", "美食", "攻略", "门票"};//,"导览"};
    List<Fragment> fragments;
    FindFragmentPageAdapter adapter;
    TextView tvAllTitle;
    private final static int REQUEST_CODE_CITY = 23;

    public static Fragment newInstance(String mTrippic) {
        FragmentFind fragment = new FragmentFind();
        Bundle args = new Bundle();
        args.putString("MBananerpic", mTrippic);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, null);
        findView();
        Log.d("tag", "FragmentFind=====");
        tvAllTitle.setText(MyApplication.city);
        tvAllTitle.setVisibility(View.VISIBLE);
        tvAllTitle.setOnClickListener(this);
        fragments = new ArrayList<>();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Fragment f2 = new FragmentFindItem(Constance.XIANLU_TYPE);// 线路  1
        Fragment f3 = new FragmentFindItem(Constance.JIUDIAN_TYPE);//酒店  2
        Fragment f4 = new FragmentFindItem(Constance.MEISHI_TYPE);//美食  3
        Fragment f5 = new FragmentFindItem(Constance.GONGLUE_TYPE);//攻略  5
        Fragment f1 = new FragmentFindItem(Constance.TICKET_TYPE); //门票 0
//        FragmentFindItem f6 = new FragmentFindItem(Constance.DAOLAN_TYPE); //门票 0
        fragments.add(f2);
        fragments.add(f3);
        fragments.add(f4);
        fragments.add(f5);
        fragments.add(f1);
//        fragments.add(f6);
        adapter = new FindFragmentPageAdapter(getFragmentManager(), fragments, names, getActivity());
        vpFind.setAdapter(adapter);
        tabFind.setupWithViewPager(vpFind);
        tabFind.getTabAt(0).select(); //默认选中某项放在\加载viewpager之后
        return view;
    }


    private void findView() {
        tabFind = (TabLayout) view.findViewById(R.id.tab_find);
        vpFind = (ViewPager) view.findViewById(R.id.vp_findvvp);
        tvAllTitle = (TextView) view.findViewById(R.id.tv_all_title_name);
        tvAllTitle.setOnClickListener(this);


    }

    @Subscribe
    public void onEventMainThread(MyEvent e) {

        if (e.getId() == 1) {
            tvAllTitle.setText(MyApplication.city);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all_title_name:
                Intent intent = new Intent(getActivity(), ActivitySelectCity.class);
                startActivityForResult(intent, REQUEST_CODE_CITY);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CITY && resultCode == 10) {
            tvAllTitle.setText(data.getStringExtra("city"));
            adapter.notifyDataSetChanged();
            MyApplication.city = data.getStringExtra("city");
        }
    }


}
