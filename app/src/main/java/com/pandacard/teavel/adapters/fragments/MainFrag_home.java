package com.pandacard.teavel.adapters.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.Myadapter;
import com.pandacard.teavel.utils.LUtils;

import java.util.ArrayList;
import java.util.List;

public class MainFrag_home extends Fragment implements ViewPager.OnPageChangeListener {
    private static String TAG = "MainFrag_home";
    private ViewPager mFragment_home_vvp;
    private List<ImageView> mImageViews;
    private Myadapter mMyadapter;
    private LinearLayout mFragment_home_points;
    // 标记前一个小圆点的位置
    private int prePosition = 0;


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


    private void initView(View inflate) {

        mFragment_home_vvp = inflate.findViewById(R.id.fragment_home_vvp);
        mFragment_home_points = inflate.findViewById(R.id.fragment_home_points);

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

    // 当页面滚动的时候调用的方法
    // offset:0--1:百分比
    // distance:移动过的真实的距离值.

    @Override
    public void onPageScrolled(int position, float offset, int distance) {
        LUtils.i(TAG, "offset=" + offset + ",distance=" + distance);

    }

    @Override
    public void onPageSelected(int position) {

        // 实现小圆点的不同状态切换效果
        mFragment_home_points.getChildAt(position).setBackgroundResource(
                R.mipmap.ic_launcher);

//        prePosition = position;
////         设置底部小点选中状态
//        mFragment_home_points.getChildAt(prePosition).setBackgroundResource(R.drawable.icon_dot_pink);


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
