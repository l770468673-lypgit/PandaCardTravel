package com.pandacard.teavel.adapters.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.pandacard.teavel.ParamConst;
import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.Myadapter;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.cardsbean;
import com.pandacard.teavel.uis.ByPandaActivity;
import com.pandacard.teavel.uis.CardActiviting;
import com.pandacard.teavel.uis.LoginActivity;
import com.pandacard.teavel.uis.MainActivity;
import com.pandacard.teavel.uis.NFCActivity;
import com.pandacard.teavel.uis.RightsActivity;
import com.pandacard.teavel.uis.SaveMoneyActivity;
import com.pandacard.teavel.uis.eIDActivity;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;
import com.pandacard.teavel.utils.UserByteUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFrag_home extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static String TAG = "MainFrag_home";
    private ViewPager mFragment_home_vvp;
    private List<ImageView> mImageViews;
    private Myadapter mMyadapter;
    //    private LinearLayout mFragment_home_points;

    // 标记前一个小圆点的位置
    private int prePosition = 0;
    private RadioGroup mfragment_home__rgroup;
    private TextView mFragment_home_login;
    private RadioButton mfragment_home_active, mfragment_home_recharge, mfragment_home_useread, mfragment_home_discounts;
    private String mAppIsLogin;
    private TextView mLly_isbindcard;
    private int[] mIntslist;
    private List<ImageView> mMlis;


    public MainFrag_home() {
    }

    public static MainFrag_home newInstance(String MBananerpic) {
        MainFrag_home fragment = new MainFrag_home();
        Bundle args = new Bundle();
        args.putString("MBananerpic", MBananerpic);
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
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main_frag_home, container, false);
        mMlis = new ArrayList<>();

        initView(inflate);
        //        String mBananerpic = getArguments().getString("MBananerpic");
        //        loadImg(mBananerpic);

        mIntslist = loadLocalPic();

        createVVp();


        return inflate;
    }

    private void createVVp() {
        for (int i = 0; i < mIntslist.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(mIntslist[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mMlis.add(imageView);
        }


        mMyadapter.setList(mMlis);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarUtil.setDrawable(getActivity(), R.drawable.mine_title_jianbian);
        loadporerColor();
    }

    private void loadporerColor() {
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.login_back));
        }
    }

    private void initView(View inflate) {

        mFragment_home_vvp = inflate.findViewById(R.id.fragment_home_vvp);
        mLly_isbindcard = inflate.findViewById(R.id.lly_isbindcard);
        //        mFragment_home_points = inflate.findViewById(R.id.fragment_home_points);
        mFragment_home_login = inflate.findViewById(R.id.fragment_home_login);

        mfragment_home__rgroup = inflate.findViewById(R.id.fragment_home__rgroup);

        mfragment_home_active = inflate.findViewById(R.id.fragment_home_active);
        mfragment_home_recharge = inflate.findViewById(R.id.fragment_home_recharge);
        mfragment_home_useread = inflate.findViewById(R.id.fragment_home_useread);
        mfragment_home_discounts = inflate.findViewById(R.id.fragment_home_discounts);


        mfragment_home_active.setOnClickListener(this);
        mFragment_home_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
            }
        });
        mfragment_home_recharge.setOnClickListener(this);
        mfragment_home_useread.setOnClickListener(this);
        mfragment_home_discounts.setOnClickListener(this);


        mMyadapter = new Myadapter(getActivity());
        mFragment_home_vvp.setAdapter(mMyadapter);


        mFragment_home_vvp.addOnPageChangeListener(this);
    }

    public List<String> spliteStrWithBlank(String msg) {
        List<String> mlist = new ArrayList<>();

        String[] s = msg.split(";");
        for (int i = 0; i < s.length; i++) {
            if (s[i].length() > 0) {
                mlist.add(s[i]);
            }
        }
        return mlist;
    }


    private void loadcardsDate() {
        if (ShareUtil.getString(HttpRetrifitUtils.SERNAME_PHONE) != null) {
            Call<cardsbean> cards = HttpManager.getInstance().getHttpClient().getCards(ShareUtil.getString(HttpRetrifitUtils.SERNAME_PHONE));

            cards.enqueue(new Callback<cardsbean>() {
                @Override
                public void onResponse(Call<cardsbean> call, Response<cardsbean> response) {
                    if (response.body() != null) {
                        cardsbean body = response.body();
                        if (body.getCode() == 1) {
                            String cards1 = body.getExtra().getCards();
                            List<String> mStrings = UserByteUtils.spliteStrWithBlank(cards1);
                            if (mStrings.size() > 0) {
                                ShareUtil.putString(HttpRetrifitUtils.DEFAULTCARDISBIND, mStrings.get(0));
                                mLly_isbindcard.setText(R.string.cardactive_cardsisbind);
                            } else {
                                mLly_isbindcard.setText(R.string.cardactive_cardsnotbind);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<cardsbean> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadcardsDate();

        mAppIsLogin = ShareUtil.getString(HttpRetrifitUtils.APPISlOGIN);
        if (ShareUtil.getString(HttpRetrifitUtils.APPISlOGIN) != null) {
            mFragment_home_login.setText(R.string.login_islogin);
            mFragment_home_login.setClickable(false);
        }
    }

    public int[] loadLocalPic() {

        return new int[]{R.mipmap.banner_2, R.mipmap.banner_3, R.mipmap.banner_4};
    }

    public void loadImg(String mBananerpic) {

        if (mBananerpic != null) {
            List<String> mList = spliteStrWithBlank(mBananerpic);

            List<ImageView> imgList = new ArrayList<>();
            for (int i = 0; i < mList.size(); i++) {
                LUtils.d(TAG, "-----------" + mList.get(i));
                ImageView imageView = new ImageView(getActivity());

                Glide.with(getActivity())
                        .load(mList.get(i).trim())
                        .into(imageView);

                //设置imageview占满整个ViewPager
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imgList.add(imageView);

                // 创建小圆点
                View point = new View(getActivity());
                point.setBackgroundResource(R.drawable.icon_dot_pink);
                // 此处40是指40个px.并不是40dp.
                // LayoutParams params = new LayoutParams(40, 40);
                DisplayMetrics metrics = new DisplayMetrics();
                // TypedValue.applyDimension:
                float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                        30, metrics);
                float height = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX, 30, metrics);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) width, (int) height);
                point.setLayoutParams(params);
                params.leftMargin = 5;
                //                mFragment_home_points.addView(point);
                //
            }
            // 设置默认的小圆点
            //            mFragment_home_points.getChildAt(0).setBackgroundResource(R.mipmap.ic_launcher);
            //            mMyadapter.setList(imgList);

        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onPageScrolled(int position, float offset, int distance) {
        LUtils.i(TAG, "offset=" + offset + ",distance=" + distance);

    }

    @Override
    public void onPageSelected(int position) {

        // 实现小圆点的不同状态切换效果
        //        mFragment_home_points.getChildAt(position).setBackgroundResource(
        //                R.mipmap.ic_launcher);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (mAppIsLogin != null) {

            switch (v.getId()) {
                case R.id.fragment_home_active:
                    Intent in = new Intent(getActivity(), eIDActivity.class);
                    startActivity(in);
                    break;
                case R.id.fragment_home_useread:
                    Intent in2 = new Intent(getActivity(), ByPandaActivity.class);
                    startActivity(in2);
                    break;
                case R.id.fragment_home_discounts:
                    Intent inrights = new Intent(getActivity(), RightsActivity.class);
                    startActivity(inrights);
                    break;
                case R.id.fragment_home_recharge:
                    Intent intent = new Intent(getActivity(), NFCActivity.class);
                    startActivityForResult(intent, ParamConst.READ_CARD_INFO_CODE);
                    break;
            }

        } else {
            ToastUtils.showToast(getActivity(), "请登录后再试");

        }

    }


}
