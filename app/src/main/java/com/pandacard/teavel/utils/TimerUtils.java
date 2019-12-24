package com.pandacard.teavel.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

import com.pandacard.teavel.R;

/**
 * Android中Timer的方法，
 * 直接调用方法即可
 * 关于Timer方法的类
 * init
 * start
 * stop
 */
public class TimerUtils {

    public static Button mbtn_yanzhengma;
    public static TimeCount mMTimeCount;
    public static Context mContext;

    public static void initTimer(Context context, Button button, long countTime, long interval) {
        mMTimeCount = new TimeCount(countTime, interval);
        mbtn_yanzhengma = button;
        mContext = context;

    }

    public static class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mbtn_yanzhengma.setClickable(false);

            mbtn_yanzhengma.setText("(" + millisUntilFinished / 1000 + ") ");
        }

        @Override
        public void onFinish() {
            mbtn_yanzhengma.setText(mContext.getResources().getString(R.string.login_wx_querycode));
            mbtn_yanzhengma.setClickable(true);
        }
    }

    public static void TimerStart() {
        mMTimeCount.start();
    }

    public static void TimerStop(String string) {
        mbtn_yanzhengma.setClickable(true);
        mMTimeCount.cancel();
        mbtn_yanzhengma.setText(string);
    }
}
