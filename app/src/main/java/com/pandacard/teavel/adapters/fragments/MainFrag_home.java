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
import com.pandacard.teavel.uis.LoginActivity;
import com.pandacard.teavel.uis.MainActivity;
import com.pandacard.teavel.uis.NFCActivity;
import com.pandacard.teavel.uis.SaveMoneyActivity;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainFrag_home extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static String TAG = "MainFrag_home";
    private ViewPager mFragment_home_vvp;
    private List<ImageView> mImageViews;
    private Myadapter mMyadapter;
    private LinearLayout mFragment_home_points;
    // 标记前一个小圆点的位置
    private int prePosition = 0;
    private RadioGroup mfragment_home__rgroup;
    private TextView mFragment_home_login;
    private RadioButton mfragment_home_active, mfragment_home_recharge, mfragment_home_useread, mfragment_home_discounts;


    public MainFrag_home() {
    }

    public static MainFrag_home newInstance() {
        MainFrag_home fragment = new MainFrag_home();
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
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main_frag_home, container, false);
        initView(inflate);
        mImageViews = loadImg();


        return inflate;
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
        mFragment_home_points = inflate.findViewById(R.id.fragment_home_points);
        mFragment_home_login = inflate.findViewById(R.id.fragment_home_login);

        mfragment_home__rgroup = inflate.findViewById(R.id.fragment_home__rgroup);

        mfragment_home_active = inflate.findViewById(R.id.fragment_home_active);
        mfragment_home_recharge = inflate.findViewById(R.id.fragment_home_recharge);
        mfragment_home_useread = inflate.findViewById(R.id.fragment_home_useread);
        mfragment_home_discounts = inflate.findViewById(R.id.fragment_home_discounts);


        mfragment_home_active.setOnClickListener(this);
        mFragment_home_login.setOnClickListener(this);
        mfragment_home_recharge.setOnClickListener(this);
        mfragment_home_useread.setOnClickListener(this);
        mfragment_home_discounts.setOnClickListener(this);


        mMyadapter = new Myadapter(getActivity());
        mFragment_home_vvp.setAdapter(mMyadapter);


        mFragment_home_vvp.addOnPageChangeListener(this);
    }

    public List<ImageView> loadImg() {
        List<String> mList = new ArrayList<>();
        mList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575455923333&di=f7785f33467b0330962e6393edc59a32&imgtype=0&src=http%3A%2F%2Fpic30.nipic.com%2F20130625%2F7447430_153500063000_2.jpg");
        mList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575455923333&di=f7785f33467b0330962e6393edc59a32&imgtype=0&src=http%3A%2F%2Fpic30.nipic.com%2F20130625%2F7447430_153500063000_2.jpg");
        mList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575455923333&di=f7785f33467b0330962e6393edc59a32&imgtype=0&src=http%3A%2F%2Fpic30.nipic.com%2F20130625%2F7447430_153500063000_2.jpg");

        List<ImageView> imgList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            //Glide加载网络图片
            Glide.with(getActivity())
                    .load(mList.get(i))
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
            mFragment_home_points.addView(point);

        }
        // 设置默认的小圆点
        mFragment_home_points.getChildAt(0).setBackgroundResource(R.mipmap.ic_launcher);
        mMyadapter.setList(imgList);
        return imgList;

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
        mFragment_home_points.getChildAt(position).setBackgroundResource(
                R.mipmap.ic_launcher);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_home_active:
            case R.id.fragment_home_useread:
            case R.id.fragment_home_discounts:
                ToastUtils.showToast(getActivity(), "开发中");
                break;
            case R.id.fragment_home_recharge:
                Intent intent = new Intent(getActivity(), NFCActivity.class);
                startActivityForResult(intent, ParamConst.READ_CARD_INFO_CODE);
                break;
            case R.id.fragment_home_login:
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
                break;

        }
    }


}
