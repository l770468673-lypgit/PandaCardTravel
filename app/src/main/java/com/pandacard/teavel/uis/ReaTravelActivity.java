package com.pandacard.teavel.uis;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.adapters.FindFragmentPageAdapter;
import com.xlu.fragments.FragmentFindItem;
import com.xlu.po.MyEvent;
import com.xlu.uis.ActivitySelectCity;
import com.xlu.utils.Constance;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class ReaTravelActivity extends AppCompatActivity implements View.OnClickListener {
    View view;
    private TextView tvTilteName;
    private TabLayout tabFind;
    private ViewPager vpFind;
    String[] names = {"线路", "酒店", "美食", "攻略", "门票"};//,"导览"};
    List<Fragment> fragments;
    FindFragmentPageAdapter adapter;
    TextView tvAllTitle;
    private final static int REQUEST_CODE_CITY = 23;
    private FindFragmentPageAdapter mMAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rea_travel);

        initView();

    }

    private void findView() {
        tabFind = (TabLayout) findViewById(R.id.tab_find);
        vpFind = (ViewPager) findViewById(R.id.vp_findvvp);
        tvAllTitle = (TextView) findViewById(R.id.tv_all_title_name);
        tvAllTitle.setOnClickListener(this);


    }

    private void initView() {

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

        mMAdapter = new FindFragmentPageAdapter(getSupportFragmentManager(), fragments, names, this);
        vpFind.setAdapter(mMAdapter);
        tabFind.setupWithViewPager(vpFind);
        tabFind.getTabAt(0).select(); //默认选中某项放在\加载viewpager之后
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
                Intent intent = new Intent(this, ActivitySelectCity.class);
                startActivityForResult(intent, REQUEST_CODE_CITY);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CITY && resultCode == 10) {
            tvAllTitle.setText(data.getStringExtra("city"));

            MyApplication.city = data.getStringExtra("city");
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
