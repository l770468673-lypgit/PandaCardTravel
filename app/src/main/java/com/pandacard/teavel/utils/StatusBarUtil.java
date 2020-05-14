package com.pandacard.teavel.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;

import com.pandacard.teavel.R;

public class StatusBarUtil {

    private static final int FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view;

    /**
     * 设置状态栏 Drawable,比如渐变色
     *
     * @param activity
     * @param drawableId
     */
    public static void setDrawable(Activity activity, @DrawableRes int drawableId) {
        Drawable drawable = activity.getResources().getDrawable(drawableId);
        setDrawable(activity, drawable);
    }

    /**
     * 设置状态栏 Drawable
     * @param activity
     * @param drawable
     */
    public static void setDrawable(Activity activity, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 去掉半透明
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 状态栏设置透明
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        // Activity 已经添加过了 statusBarView , 则修改背景颜色
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackground(drawable);
        } else { // 没有添加过，则添加一个 statusBarView
            // decorView 中添加一个与状态栏大小的 view
            decorView.addView(createStatusBarDrawableView(activity, drawable));
        }
        setRootView(activity);
    }
    /**
     * 生成一个和状态栏大小相同的矩形条,并设置背景为 drawable
     *
     * @param activity
     * @param drawable
     * @return
     */
    private static View createStatusBarDrawableView(Activity activity, Drawable drawable) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackground(drawable);
        statusBarView.setId(FAKE_STATUS_BAR_VIEW_ID);
        return statusBarView;
    }


    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }
    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
