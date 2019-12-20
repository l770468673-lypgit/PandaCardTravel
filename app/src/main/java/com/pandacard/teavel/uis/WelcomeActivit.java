package com.pandacard.teavel.uis;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.pandacard.teavel.R;
import com.pandacard.teavel.bases.AppStatus;
import com.pandacard.teavel.bases.BaseActivity;

public class WelcomeActivit extends BaseActivity {

    public Runnable toSplashActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(WelcomeActivit.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    };
    private WelcomeHandler mHandler;

    class WelcomeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        AppStatus.APP_STATUS = AppStatus.APP_STATUS_NORMAL; // App正常的启动，设置App的启动状态为正常启动
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mHandler = new WelcomeHandler();
        mHandler.postDelayed(toSplashActivity, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
