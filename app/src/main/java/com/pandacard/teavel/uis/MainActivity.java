package com.pandacard.teavel.uis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.pandacard.teavel.ParamConst;
import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.Main_frag_ViewPagerAdapter;
import com.pandacard.teavel.adapters.fragments.MainFrag_home;
import com.pandacard.teavel.adapters.fragments.MainFrag_mine;
import com.pandacard.teavel.adapters.fragments.MainFrag_shop;
import com.pandacard.teavel.adapters.fragments.MainFrag_travel;
import com.pandacard.teavel.utils.LUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private static String TAG = "MainActivity";
    private Button mBtntobus;
    private RadioGroup mMain_rgroup;
    private RadioButton mMain_frag_home;
    private RadioButton mMain_frag_travel;
    private RadioButton mMain_frag_shop;
    private RadioButton mMain_frag_mine;
    private ViewPager mMain_frag_viewpager;
    private ArrayList<Fragment> mMFragmentList;
    private Main_frag_ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initView();

        initPagerDate();
    }

    private void initPagerDate() {

        mMFragmentList = new ArrayList<>();
        mMFragmentList.add(MainFrag_home.newInstance());
        mMFragmentList.add(MainFrag_travel.newInstance());
        mMFragmentList.add(MainFrag_shop.newInstance());
        mMFragmentList.add(MainFrag_mine.newInstance());
        mViewPagerAdapter = new Main_frag_ViewPagerAdapter(getSupportFragmentManager());

        mMain_frag_viewpager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.setList(mMFragmentList);
        LUtils.d(TAG, "mMFragmentList.size==" + mMFragmentList.size());
        //系统默认选中第一个,但是系统选中第一个不执行onNavigationItemSelected(MenuItem)方法,
        // 如果要求刚进入页面就执行clickTabOne()方法,则手动调用选中第一个
        mMain_frag_viewpager.addOnPageChangeListener(this);
    }

    private void initView() {

        mMain_rgroup = findViewById(R.id.main_rgroup);
        mMain_frag_viewpager = findViewById(R.id.main_frag_viewpager);

        mMain_frag_home = findViewById(R.id.main_frag_home);
        mMain_frag_travel = findViewById(R.id.main_frag_travel);
        mMain_frag_shop = findViewById(R.id.main_frag_shop);
        mMain_frag_mine = findViewById(R.id.main_frag_mine);
        mBtntobus = findViewById(R.id.btntobus);

        mBtntobus.setOnClickListener(this);
        mMain_rgroup.setOnCheckedChangeListener(this);

        mMain_rgroup.getChildAt(0).performClick();//模拟点击第一个RB

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btntobus:
                Intent intent = new Intent(this, SaveMoneyActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_frag_home:
                mMain_frag_viewpager.setCurrentItem(0);
                break;
            case R.id.main_frag_travel:
                mMain_frag_viewpager.setCurrentItem(1);
                break;
            case R.id.main_frag_shop:
                mMain_frag_viewpager.setCurrentItem(2);
                break;
            case R.id.main_frag_mine:
                mMain_frag_viewpager.setCurrentItem(3);
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mMain_rgroup.check(R.id.main_frag_home);
                break;
            case 1:
                mMain_rgroup.check(R.id.main_frag_travel);
                break;
            case 2:
                mMain_rgroup.check(R.id.main_frag_shop);
                break;
            case 3:
                mMain_rgroup.check(R.id.main_frag_mine);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
