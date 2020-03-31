package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.Find_tab_Adapter;
import com.pandacard.teavel.adapters.fragments.Frag_OneSelfCardOrde;
import com.pandacard.teavel.adapters.fragments.Frag_AllCardOrder;
import com.pandacard.teavel.adapters.fragments.Frag_SendOrderCard;
import com.pandacard.teavel.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class CardOrder_Activity extends AppCompatActivity {

    private TabLayout mTab_cardordermsg;
    private ViewPager mVp_cardordermsg;
    private List<Fragment> mFragments;
    private List<String> mStringList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_order_act);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initView();
        initfragmentDate();
    }

    private void initView() {

    }

    public void pageback_finish(View view) {
        finish();
    }
    private void initfragmentDate() {
        mTab_cardordermsg = findViewById(R.id.tab_cardordermsg);
        mVp_cardordermsg = findViewById(R.id.vp_cardordermsg);

        Frag_AllCardOrder ownBuy = new Frag_AllCardOrder();
        Frag_OneSelfCardOrde gotCard = new Frag_OneSelfCardOrde();
        Frag_SendOrderCard sendCard = new Frag_SendOrderCard();
        mFragments = new ArrayList<>();
        mFragments.add(ownBuy);
        mFragments.add(sendCard);
        mFragments.add(gotCard);

        mStringList = new ArrayList<>();
        mStringList.add("卡订单");
        mStringList.add("已自购");
        mStringList.add("已赠送");

        //设置TabLayout 的模式
        mTab_cardordermsg.setTabMode(TabLayout.MODE_FIXED);

        //为TabLayout添加tab名称
        mTab_cardordermsg.addTab(mTab_cardordermsg.newTab().setText(mStringList.get(0)));
        mTab_cardordermsg.addTab(mTab_cardordermsg.newTab().setText(mStringList.get(1)));
        mTab_cardordermsg.addTab(mTab_cardordermsg.newTab().setText(mStringList.get(2)));


        Find_tab_Adapter fadapter = new Find_tab_Adapter(getSupportFragmentManager(), mFragments, mStringList);

        mVp_cardordermsg.setAdapter(fadapter);
        mTab_cardordermsg.setupWithViewPager(mVp_cardordermsg);
    }
}
