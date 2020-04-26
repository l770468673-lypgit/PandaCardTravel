package com.pandacard.teavel.adapters.fragments;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.AllCardOrderAdapter;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.AddressBean;
import com.pandacard.teavel.https.beans.ReceiveCardbean;
import com.pandacard.teavel.https.beans.small_routine_bean.CardsByUserId;
import com.pandacard.teavel.https.beans.small_routine_bean.MyAddressByUserId;
import com.pandacard.teavel.https.beans.small_routine_bean.updateOrderCardStatusForGive;
import com.pandacard.teavel.uis.MyTransactionAddress;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.MyDialog;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.ToastUtils;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link Frag_AllCardOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_AllCardOrder extends Fragment implements View.OnClickListener, AllCardOrderAdapter.AdapterItenClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String TAG = "Frag_AllCardOrder";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //    private LinearLayout mLly_addressdetal;
    private MyDialog mMyDialogby;
    private MyDialog mMyDialogConfirm;
    private TextView mRelativeLayout;
    private TextView mDialog_cancle;
    private TextView mDialog_success;
    private TextView mTv_successname;
    private TextView mTv_succesphone;
    private TextView mTv_succesaddress;
    private TextView mTv_succescommit;
    private TextView mTv_succescamcle;
    private View mViewsuccess;
    private View mViewfirstdia;
    private String mSaddress_addname;
    private String mSaddress_addphone;
    private String mSaddress_addprovinces;
    private String mSaddress_addcountries;
    private String mSaddress_addcity;
    private String mSaddress_addaddressdetals;
    private AllCardOrderAdapter mAdapter;
    private List<CardsByUserId.CardListBean> mCardList;
    private String mOrderCode;
    private int ADDRESSREQUEST = 0x0777;
    private int ADDRESSRESULT = 0x0888;
    private AllUiHandler mHandler;


    public Frag_AllCardOrder() {
        // Required empty public constructor
    }

    private RecyclerView mAll_orderrecycle;
    private RelativeLayout informationnull;


    private ImageView iamge_loaddate_anim;
    private AnimationDrawable mAnimaition;
    private Button ownbuy, sendfriends;


    private EditText address_addname;
    EditText address_addphone;
    EditText address_addprovinces;
    EditText address_addcountries;
    EditText address_addcity;
    EditText address_addaddressdetals;

    class AllUiHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10001:
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                    informationnull.setVisibility(View.VISIBLE);
                    break;
                case 10002:
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                    informationnull.setVisibility(View.GONE);
                    break;

            }
        }
    }

    // TODO: Rename and change types and number of parameters
    public static Frag_AllCardOrder newInstance(String param1, String param2) {
        Frag_AllCardOrder fragment = new Frag_AllCardOrder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mHandler = new AllUiHandler();
        View inflate = inflater.inflate(R.layout.fragment_blank, container, false);
        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {

        mAll_orderrecycle = inflate.findViewById(R.id.all_orderrecycle);
        informationnull = inflate.findViewById(R.id.informationnull);
        iamge_loaddate_anim = inflate.findViewById(R.id.spaiamge_loaddate_anim);
        //        ownbuy = inflate.findViewById(R.id.ownbuy);
        //        sendfriends = inflate.findViewById(R.id.sendfriends);
        //        mLly_addressdetal = inflate.findViewById(R.id.lly_addressdetal);

        iamge_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);
        mAnimaition = (AnimationDrawable) iamge_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);
        mAll_orderrecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAnimaition.start();
        iamge_loaddate_anim.setVisibility(View.VISIBLE);


        mAdapter = new AllCardOrderAdapter(getActivity());
        mAll_orderrecycle.setAdapter(mAdapter);
        mAdapter.setOrderItenClick(this);
        //        、、===========


    }

    @Override
    public void onResume() {
        super.onResume();
        informationnull.setVisibility(View.GONE);
        initDate();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initDate() {
        iamge_loaddate_anim.setVisibility(View.VISIBLE);
        Call<CardsByUserId> cardsByUserId = HttpManager.getInstance().getHttpClient3().getCardsByUserId(
                ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID),
                HttpRetrifitUtils.STATE_NOTSEND);
        cardsByUserId.enqueue(new Callback<CardsByUserId>() {
            @Override
            public void onResponse(Call<CardsByUserId> call, Response<CardsByUserId> response) {
                if (response.body() != null) {

                    mCardList = response.body().getCardList();
                    if (mCardList != null) {
                        mAdapter.setCardList(mCardList);
                        mAdapter.notifyDataSetChanged();
                        mAnimaition.stop();

                        mHandler.sendEmptyMessageDelayed(10002, 200);
                    } else {
                        mHandler.sendEmptyMessageDelayed(10001, 200);

                    }

                }

            }

            @Override
            public void onFailure(Call<CardsByUserId> call, Throwable t) {

                mHandler.sendEmptyMessage(10001);
                //                mLly_addressdetal.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.confirmtv_succescommit:
                AddressBean addressBean = new AddressBean(mSaddress_addprovinces,
                        mSaddress_addcountries, mSaddress_addcity, mSaddress_addaddressdetals,
                        mSaddress_addphone, mSaddress_addname, ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID), mOrderCode);
                Call<CardsByUserId> cardsByUserIdCall = HttpManager.getInstance().getHttpClient3().AddressInfoForSelfPurchase(
                        addressBean.toString());
                cardsByUserIdCall.enqueue(new Callback<CardsByUserId>() {
                    @Override
                    public void onResponse(Call<CardsByUserId> call, Response<CardsByUserId> response) {
                        CardsByUserId body = response.body();
                        if (body != null) {
                            mMyDialogConfirm.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<CardsByUserId> call, Throwable t) {

                    }
                });
                break;
            case R.id.confirmtv_succescamcle:
                mMyDialogConfirm.dismiss();
                break;
            case R.id.relay_tpaddressact:
                mMyDialogby.dismiss();
                Intent Transac = new Intent(getActivity(), MyTransactionAddress.class);
                Bundle bundle = new Bundle();
                bundle.putString("MyTransactionAddress", "MyTransactionAddress");
                Transac.putExtras(bundle);
                startActivityForResult(Transac, ADDRESSREQUEST);
                break;
            case R.id.dialog_success:
                mSaddress_addname = address_addname.getText().toString();
                mSaddress_addphone = address_addphone.getText().toString();
                mSaddress_addprovinces = address_addprovinces.getText().toString();
                mSaddress_addcountries = address_addcountries.getText().toString();
                mSaddress_addcity = address_addcity.getText().toString();
                mSaddress_addaddressdetals = address_addaddressdetals.getText().toString();

                if (mSaddress_addname.length() > 0 &&
                        mSaddress_addphone.length() > 0 &&
                        mSaddress_addprovinces.length() > 0 &&
                        mSaddress_addcountries.length() > 0 &&
                        mSaddress_addcity.length() > 0 &&
                        mSaddress_addaddressdetals.length() > 0) {


                    mMyDialogby.dismiss();

                    OwnConfirmDialog();


                } else {
                    ToastUtils.showToast(getActivity(), "地址不能为空");
                }
                break;
            case R.id.dialog_cancle:
                mMyDialogby.dismiss();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESSREQUEST && resultCode == ADDRESSRESULT) {
            Bundle extras = data.getExtras();
            MyAddressByUserId.AddressListBean bean =
                    (MyAddressByUserId.AddressListBean) extras.getSerializable("addressListBean");
            OwnByAndSendDialog(bean);
        }
    }


    private void OwnConfirmDialog() {


        mViewsuccess = getLayoutInflater().inflate(R.layout.address_successdialog, null);

        mTv_successname = mViewsuccess.findViewById(R.id.tv_successname);
        mTv_succesphone = mViewsuccess.findViewById(R.id.tv_succesphone);
        mTv_succesaddress = mViewsuccess.findViewById(R.id.tv_succesaddress);
        mTv_succescommit = mViewsuccess.findViewById(R.id.confirmtv_succescommit);
        mTv_succescamcle = mViewsuccess.findViewById(R.id.confirmtv_succescamcle);
        mTv_succescommit.setOnClickListener(this);
        mTv_succescamcle.setOnClickListener(this);

        mMyDialogConfirm = new MyDialog(getActivity(), mViewsuccess);
        mMyDialogConfirm.setCancelable(true);
        mMyDialogConfirm.show();
        mTv_successname.setText(mSaddress_addname);
        mTv_succesphone.setText(mSaddress_addphone);
        mTv_succesaddress.setText(mSaddress_addprovinces + mSaddress_addcity
                + mSaddress_addcountries
                + mSaddress_addaddressdetals);

    }

    public void OwnByAndSendDialog(MyAddressByUserId.AddressListBean bean) {
        mViewfirstdia = getLayoutInflater().inflate(R.layout.addressdialog_layout, null);
        mMyDialogby = new MyDialog(getActivity(), mViewfirstdia);
        address_addname = mViewfirstdia.findViewById(R.id.address_addname);
        address_addphone = mViewfirstdia.findViewById(R.id.address_addphone);
        address_addprovinces = mViewfirstdia.findViewById(R.id.address_addprovinces);
        address_addcountries = mViewfirstdia.findViewById(R.id.address_addcountries);
        address_addcity = mViewfirstdia.findViewById(R.id.address_addcity);
        address_addaddressdetals = mViewfirstdia.findViewById(R.id.address_addaddressdetals);


        mRelativeLayout = mViewfirstdia.findViewById(R.id.relay_tpaddressact);
        mDialog_success = mViewfirstdia.findViewById(R.id.dialog_success);
        mDialog_cancle = mViewfirstdia.findViewById(R.id.dialog_cancle);

        if (bean != null) {
            address_addname.setText(bean.getReceiver());
            address_addphone.setText(bean.getPhoneNumber());
            address_addprovinces.setText(bean.getProvinces());
            address_addcountries.setText(bean.getRegion());
            address_addcity.setText(bean.getCountries());
            address_addaddressdetals.setText(bean.getDetailAddress());
        }
        mRelativeLayout.setOnClickListener(this);
        mDialog_success.setOnClickListener(this);
        mDialog_cancle.setOnClickListener(this);
        mMyDialogby.setCancelable(true);
        mMyDialogby.show();
    }

    @Override
    public void setClickBuy(int position) {
        //        mOrderCode = mCardList.get(position).getOrderCode();
        //        OwnByAndSendDialog(null);
        toWeChatproject();

    }

    @Override
    public void setClickSend(final int position) {
        //        CardsByUserId.CardListBean cardListBean = mCardList.get(position);
        //        ReceiveCardbean cardbean =
        //                new ReceiveCardbean(cardListBean.getId(), cardListBean.getOrderCode()
        //                        , cardListBean.getUserId(), "", ""
        //                        , cardListBean.getProName(), cardListBean.getProLogo(), "");
        //        LUtils.d(TAG, "cardbean====" + cardbean.toString());
        //        //  addressdialog
        //        OnekeyShare oks = new OnekeyShare();
        //        //         title标题，微信、QQ和QQ空间等平台使用
        //        //        oks.setTitle("分享");
        //        //         titleUrl QQ和QQ空间跳转链接
        //        //        oks.setTitleUrl("");
        //        //         text是分享文本，所有平台都需要这个字段
        //        oks.setText("/pages/receiveCard/receiveCard?data=" + cardbean.toString());
        //        //         imagePath是图片的本地路径，确保SDcard下面存在此张图片
        //        oks.setImagePath("/sdcard/test.jpg");
        //        //         url在微信、Facebook等平台中使用
        //        //                oks.setUrl("http://sharesdk.cn");
        //        oks.setCallback(new PlatformActionListener() {
        //            @Override
        //            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        //                checkOrderDetal(mCardList.get(position).getId());
        //                ToastUtils.showToast(getActivity(), "分享成功");
        //            }
        //
        //            @Override
        //            public void onError(Platform platform, int i, Throwable throwable) {
        //                ToastUtils.showToast(getActivity(), "分享失败");
        //            }
        //
        //            @Override
        //            public void onCancel(Platform platform, int i) {
        //                ToastUtils.showToast(getActivity(), "取消分享");
        //            }
        //        });
        //        oks.show(getActivity());
        toWeChatproject();

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


    private void checkOrderDetal(String id) {

        Call<updateOrderCardStatusForGive> updateOrderCardStatusForGiveCall =
                HttpManager.getInstance().getHttpClient3().updateOrderCardStatusForGive(id);
        updateOrderCardStatusForGiveCall.enqueue(new Callback<updateOrderCardStatusForGive>() {
            @Override
            public void onResponse(Call<updateOrderCardStatusForGive> call, Response<updateOrderCardStatusForGive> response) {

            }

            @Override
            public void onFailure(Call<updateOrderCardStatusForGive> call, Throwable t) {

            }
        });
    }
}
