package com.pandacard.teavel.adapters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpCallback;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.GoodsInfoById;
import com.pandacard.teavel.uis.ByPandaActivity;
import com.pandacard.teavel.uis.CardActiviting;
import com.pandacard.teavel.uis.CardOrder_Activity;
import com.pandacard.teavel.uis.MineOrderDetal;
import com.pandacard.teavel.uis.MinePandaCards;
import com.pandacard.teavel.uis.OrderMy_Activity;
import com.pandacard.teavel.uis.ReaTravelActivity;
import com.pandacard.teavel.uis.RollActivity;
import com.pandacard.teavel.uis.WelcomeActivit;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit2.Call;


public class MainFrag_travelHome extends Fragment implements View.OnClickListener {
    private static String TAG = "MainFrag_mine";
    private RadioButton mFragment_travelhome_active;
    private RadioButton mFragment_home_recharge;
    private RadioButton mFragment_travelhome_discounts;
    private RadioButton mFragment_travelhome_useread;
    private RadioGroup mFragment_travelhome_rgroup;
    private String mStrIslogin;


    public MainFrag_travelHome() {
    }

    public static MainFrag_travelHome newInstance() {
        MainFrag_travelHome fragment = new MainFrag_travelHome();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mStrIslogin = ShareUtil.getString(HttpRetrifitUtils.APPISlOGIN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.mainfrag_travelhome, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {

        mFragment_travelhome_rgroup = inflate.findViewById(R.id.fragment_travelhome_rgroup);
        mFragment_travelhome_active = inflate.findViewById(R.id.fragment_travelhome_active);
        mFragment_home_recharge = inflate.findViewById(R.id.fragment_travelhome_recharge);
        mFragment_travelhome_discounts = inflate.findViewById(R.id.fragment_travelhome_discounts);
        mFragment_travelhome_useread = inflate.findViewById(R.id.fragment_travelhome_useread);

        mFragment_travelhome_active.setOnClickListener(this);
        mFragment_home_recharge.setOnClickListener(this);
        mFragment_travelhome_useread.setOnClickListener(this);
        mFragment_travelhome_discounts.setOnClickListener(this);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarUtil.setDrawable(getActivity(), R.drawable.mine_title_jianbian);
    }


    @Override
    public void onClick(View v) {
        if (mStrIslogin != null) {
            switch (v.getId()) {

                case R.id.fragment_travelhome_active:

                    Intent intarvel = new Intent(getActivity(), ReaTravelActivity.class);
                    startActivity(intarvel);
                    break;
                case R.id.fragment_travelhome_recharge:
//                    Intent in2 = new Intent(getActivity(), ByPandaActivity.class);
//                    Bundle b = new Bundle();
//                b.putString("mSbanna", mSbanna);
//                b.putString("mGoodsDesc", mGoodsDesc);
//                b.putString("mProductId", mProductId);
//                b.putDouble("mGoodsCostPrice", mGoodsCostPrice);
//                    in2.putExtras(b);
//                    startActivity(in2);

                    toWeChatproject();
                    break;

                case R.id.fragment_travelhome_discounts:
                    Intent intentorder = new Intent(getActivity(), CardOrder_Activity.class);
                    startActivity(intentorder);
                    break;
                case R.id.fragment_travelhome_useread:
                    Intent intent = new Intent(getActivity(), OrderMy_Activity.class);
                    startActivity(intent);

                    break;

                default:
                    break;
            }


        } else {
            ToastUtils.showToast(getActivity(), "请登录后再试");
        }
    }


    public void toWeChatproject() {
        String appId = "wx066b02355bf9f39b"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_ad3fcc78de0c"; // 小程序原始id
        req.path = "/pages/index/index";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        api.sendReq(req);
    }
}
