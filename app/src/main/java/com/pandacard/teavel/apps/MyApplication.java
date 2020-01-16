package com.pandacard.teavel.apps;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mob.MobSDK;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;

public class MyApplication extends Application {

    private static String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        ShareUtil.initShared(this);
        HttpManager.getInstance();
    }

    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            LUtils.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

}
