package com.pandacard.teavel.apps;

import android.app.Application;

import com.mob.MobSDK;
import com.pandacard.teavel.https.HttpManager;
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
    //    private void checuAppUpdate() {
    //    Call<AppUpdate> appUpdateCall = HttpManager.getInstance().getHttpClient().upDateApp(Contants.ACTIONID102, "1");
    //        appUpdateCall.enqueue(new Callback<AppUpdate>() {
    //        @Override
    //        public void onResponse(Call<AppUpdate> call, Response<AppUpdate> response) {
    //            AppUpdate body = response.body();
    //            if (body != null) {
    //                int errorCode = body.getErrorCode();
    //                if (errorCode == 0) {
    //                    final AppUpdate.ExtraBean extra = body.getExtra();
    //                    String versionCode = extra.getVersionCode();
    //                    int localVersion = MyApplications.getLocalVersion(MainActivity.this);
    //                    int iversionCode = Integer.parseInt(versionCode);
    //                    LUtils.d(TAG, "localVersion==" + localVersion);
    //                    LUtils.d(TAG, "iversionCode==" + iversionCode);
    //                    if (iversionCode > localVersion) {
    //                        // 开始下载
    //                        LUtils.d(TAG, "开始下载");
    //                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    //                        builder.setTitle("发现新版本" + extra.getVersionCode())
    //                                .setMessage("是否升级？")
    //                                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
    //                                    @Override
    //                                    public void onClick(DialogInterface dialog, int which) {
    //                                        Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_LONG).show();
    //                                        new AppDownloadTask().execute(extra.getUrl(),
    //                                                extra.getApkFileName(), extra.getVersionCode());
    //                                    }
    //                                }).create().show();
    //
    //                    } else {
    //                        LUtils.d(TAG, "不下载");
    //                    }
    //                } else {
    //                    LUtils.d(TAG, "body==null");
    //                }
    //            } else {
    //                LUtils.d(TAG, "请求失败 ");
    //            }
    //        }
    //
    //        @Override
    //        public void onFailure(Call<AppUpdate> call, Throwable t) {
    //
    //        }
    //    });
    //}

}
