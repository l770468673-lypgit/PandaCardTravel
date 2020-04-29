package com.xlu.utils;


import android.content.Context;

import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by giant on 2017/7/21.
 */

public class ScreenUtil {
    /**
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(AppCompatActivity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int result = 0;
        int resourceId = activity.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
//        int screenHeight = dm.heightPixels - result;
        int screenHeight=dm.heightPixels;
        return screenHeight;
    }

    /**
     * dp转化dx
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
