package com.pandacard.teavel.uis;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pandacard.teavel.R;
import com.pandacard.teavel.bases.AppStatus;
import com.pandacard.teavel.bases.BaseActivity;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.ResourcesBean;
import com.pandacard.teavel.https.beans.pandaInfo;
import com.pandacard.teavel.utils.HttpRetrifitUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivit extends BaseActivity {

    public final Runnable toSplashActivity = new Runnable() {
        @Override
        public void run() {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(WelcomeActivit.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            bundle.putString("mShop", mShop);
            bundle.putString("mTrip", mTrip);
            bundle.putString("mBananer", mBananer);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    };


    private WelcomeHandler mHandler;
    private ImageView mWelcomeimg;
    private String mBananer;
    private String mShop;
    private String mTrip;
    public List<String> mlist;


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
        mHandler = new WelcomeHandler();
        mWelcomeimg = findViewById(R.id.welcomeimg);
        AppStatus.APP_STATUS = AppStatus.APP_STATUS_NORMAL; // App正常的启动，设置App的启动状态为正常启动
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        Call<pandaInfo> resourcesBeanCall = HttpManager.getInstance().getHttpClient().toGetInfo("");
        resourcesBeanCall.enqueue(new Callback<pandaInfo>() {
            @Override
            public void onResponse(Call<pandaInfo> call, Response<pandaInfo> response) {
                if (response.body() != null) {
                    pandaInfo.ExtraBean extra = response.body().getExtra();
                    mBananer = extra.getBananer();
                    mShop = extra.getShop();
                    mTrip = extra.getTrip();


                    final String welcome = extra.getWelcome();

//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Glide.with(WelcomeActivit.this).load(welcome).into(mWelcomeimg);
//                        }
//                    });

                }
            }

            @Override
            public void onFailure(Call<pandaInfo> call, Throwable t) {

            }
        });


        mHandler.postDelayed(toSplashActivity, 4000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
