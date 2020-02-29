package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.Find_tab_Adapter;
import com.pandacard.teavel.adapters.fragments.FragmentNotReceiv;
import com.pandacard.teavel.adapters.fragments.FragmentNotSend;
import com.pandacard.teavel.adapters.fragments.FragmentaALL;
import com.pandacard.teavel.adapters.fragments.FragmentaClosed;
import com.pandacard.teavel.adapters.fragments.FragmentaFinish;
import com.pandacard.teavel.adapters.fragments.FragmentnotPay;

import java.util.ArrayList;
import java.util.List;

public class OrderMy_Activity extends AppCompatActivity {

    private TabLayout mTab_ordermsg;
    private ViewPager mVp_ordermsg;
    private List<Fragment> mList_fragment;
    private List<String> mList_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_my);
        initView();
        initfragDte();

    }

    private void initfragDte() {
        FragmentaALL all = new FragmentaALL();
        FragmentnotPay notPay = new FragmentnotPay();
        FragmentNotSend NotSend = new FragmentNotSend();
        FragmentNotReceiv NotReceiv = new FragmentNotReceiv();
        FragmentaFinish Finish = new FragmentaFinish();
        FragmentaClosed Closed = new FragmentaClosed();

        //将fragment装进列表
        mList_fragment = new ArrayList<>();
        mList_fragment.add(all);
        mList_fragment.add(notPay);
        mList_fragment.add(NotSend);
        mList_fragment.add(NotReceiv);
        mList_fragment.add(Finish);
        mList_fragment.add(Closed);



        //将名称加载tab名字列表
        mList_title = new ArrayList<>();
        mList_title.add("全部");
        mList_title.add("未付款");
        mList_title.add("待发货");
        mList_title.add("待收货");
        mList_title.add("已完成");
        mList_title.add("已关闭");
        //设置TabLayout 的模式
        mTab_ordermsg.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        //为TabLayout添加tab名称
        mTab_ordermsg.addTab(mTab_ordermsg.newTab().setText(mList_title.get(0)));
        mTab_ordermsg.addTab(mTab_ordermsg.newTab().setText(mList_title.get(1)));
        mTab_ordermsg.addTab(mTab_ordermsg.newTab().setText(mList_title.get(2)));
        mTab_ordermsg.addTab(mTab_ordermsg.newTab().setText(mList_title.get(3)));
        Find_tab_Adapter fadapter = new Find_tab_Adapter(getSupportFragmentManager(), mList_fragment, mList_title);

        mVp_ordermsg.setAdapter(fadapter);
        mTab_ordermsg.setupWithViewPager(mVp_ordermsg);
    }

    private void initView() {
        mTab_ordermsg = findViewById(R.id.tab_ordermsg);
        mVp_ordermsg = findViewById(R.id.vp_ordermsg);
    }
}
