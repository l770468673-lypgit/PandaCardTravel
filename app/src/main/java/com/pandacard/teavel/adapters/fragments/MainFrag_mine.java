package com.pandacard.teavel.adapters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pandacard.teavel.R;
import com.pandacard.teavel.uis.LoginActivity;
import com.pandacard.teavel.uis.MineCarsDetal;
import com.pandacard.teavel.uis.WelcomeActivit;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;


public class MainFrag_mine extends Fragment implements View.OnClickListener {
    private static String TAG = "MainFrag_mine";
    private RadioButton mFragment_mine_active;
    private RadioButton mFragment_mine_useread;
    private RadioButton mFragment_mine_order;
    private RadioButton mFragment_mine_eid;
    private TextView mMine_rely_panda2;
    private ImageView mMine_rely_panda3;


    public MainFrag_mine() {
    }

    public static MainFrag_mine newInstance() {
        MainFrag_mine fragment = new MainFrag_mine();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main_frag_mine, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        mFragment_mine_active = inflate.findViewById(R.id.fragment_mine_active);
        mFragment_mine_useread = inflate.findViewById(R.id.fragment_mine_useread);
        mFragment_mine_order = inflate.findViewById(R.id.fragment_mine_order);
        mFragment_mine_eid = inflate.findViewById(R.id.fragment_mine_eid);
        mMine_rely_panda2 = inflate.findViewById(R.id.mine_rely_panda2);
        mMine_rely_panda3 = inflate.findViewById(R.id.mine_rely_panda3);

        mFragment_mine_active.setOnClickListener(this);
        mFragment_mine_useread.setOnClickListener(this);
        mFragment_mine_order.setOnClickListener(this);
        mFragment_mine_eid.setOnClickListener(this);

        mMine_rely_panda2.setOnClickListener(this);
        mMine_rely_panda3.setOnClickListener(this);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarUtil.setDrawable(getActivity(), R.drawable.mine_title_jianbian);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_mine_active:
            case R.id.fragment_mine_useread:
            case R.id.fragment_mine_order:
            case R.id.fragment_mine_eid:
                ToastUtils.showToast(getActivity(), "开发中");
                break;

            case R.id.mine_rely_panda3:
                Intent intentlog = new Intent(getActivity(), WelcomeActivit.class);
                startActivity(intentlog);
                ShareUtil.removekey(HttpRetrifitUtils.SERNAME_PHONE);
                ShareUtil.removekey(HttpRetrifitUtils.SERNAME_PASS);
                ShareUtil.removekey(HttpRetrifitUtils.WXLOGIN_UNID);
                ShareUtil.removekey(HttpRetrifitUtils.APPISlOGIN);
                getActivity().finish();
                break;
            case R.id.mine_rely_panda2:

                Intent intent = new Intent(getActivity(), MineCarsDetal.class);
                startActivity(intent);

                break;
        }
    }
}
