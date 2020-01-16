package com.pandacard.teavel.uis;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.Main_frag_ViewPagerAdapter;
import com.pandacard.teavel.adapters.fragments.MainFrag_home;
import com.pandacard.teavel.adapters.fragments.MainFrag_mine;
import com.pandacard.teavel.adapters.fragments.MainFrag_shop;
import com.pandacard.teavel.adapters.fragments.MainFrag_travel;
import com.pandacard.teavel.apps.MyApplication;
import com.pandacard.teavel.bases.BaseActivity;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.AppUpdate;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private static String TAG = "MainActivity";
    private Button mBtntobus;
    private RadioGroup mMain_rgroup;
    private RadioButton mMain_frag_home;
    private RadioButton mMain_frag_travel;
    private RadioButton mMain_frag_shop;
    private RadioButton mMain_frag_mine;
    private ViewPager mMain_frag_viewpager;
    private ArrayList<Fragment> mMFragmentList;
    private Main_frag_ViewPagerAdapter mViewPagerAdapter;
    private String mMShoppic;
    private String mMTrippic;
    private String mMBananerpic;
    private String mAppIsLogin;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mManager;
    private Intent mIntentNotifi;
    private String mDownloadedAppPath;
    private Notification mBuild;
    private static final int NO_3 = 0x3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Bundle bundle = getIntent().getExtras();
        mMShoppic = bundle.getString("mShop");
        mMTrippic = bundle.getString("mTrip");
        mMBananerpic = bundle.getString("mBananer");


        LUtils.d(TAG, "mAppIsLogin==========" + mAppIsLogin);
        initView();

        initPagerDate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAppIsLogin = ShareUtil.getString(HttpRetrifitUtils.APPISlOGIN);
    }

    private void initPagerDate() {

        mMFragmentList = new ArrayList<>();
        mMFragmentList.add(MainFrag_home.newInstance(mMBananerpic));
        mMFragmentList.add(MainFrag_travel.newInstance(mMTrippic));
        mMFragmentList.add(MainFrag_shop.newInstance(mMShoppic));
        mMFragmentList.add(MainFrag_mine.newInstance());
        mViewPagerAdapter = new Main_frag_ViewPagerAdapter(getSupportFragmentManager());

        mMain_frag_viewpager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.setList(mMFragmentList);
        LUtils.d(TAG, "mMFragmentList.size==" + mMFragmentList.size());
        //系统默认选中第一个,但是系统选中第一个不执行onNavigationItemSelected(MenuItem)方法,
        // 如果要求刚进入页面就执行clickTabOne()方法,则手动调用选中第一个
        mMain_frag_viewpager.addOnPageChangeListener(this);
    }

    private void initView() {

        mMain_rgroup = findViewById(R.id.main_rgroup);
        mMain_frag_viewpager = findViewById(R.id.main_frag_viewpager);

        mMain_frag_home = findViewById(R.id.main_frag_home);
        mMain_frag_travel = findViewById(R.id.main_frag_travel);
        mMain_frag_shop = findViewById(R.id.main_frag_shop);
        mMain_frag_mine = findViewById(R.id.main_frag_mine);
        mBtntobus = findViewById(R.id.btntobus);

        mBtntobus.setOnClickListener(this);
        mMain_rgroup.setOnCheckedChangeListener(this);

        mMain_rgroup.getChildAt(0).performClick();//模拟点击第一个RB
        checuAppUpdate();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LUtils.d(TAG, "onKeyDown: event -- " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //finish();
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                break;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btntobus:

                if (mAppIsLogin != null) {

                    Intent intent = new Intent(this, SaveMoneyActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(this, "请登录后再试");
                }

                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_frag_home:
                mMain_frag_viewpager.setCurrentItem(0);
                break;
            case R.id.main_frag_travel:
                mMain_frag_viewpager.setCurrentItem(1);
                break;
            case R.id.main_frag_shop:
                mMain_frag_viewpager.setCurrentItem(2);
                break;
            case R.id.main_frag_mine:
                mMain_frag_viewpager.setCurrentItem(3);
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mMain_rgroup.check(R.id.main_frag_home);
                break;
            case 1:
                mMain_rgroup.check(R.id.main_frag_travel);
                break;
            case 2:
                mMain_rgroup.check(R.id.main_frag_shop);
                break;
            case 3:
                mMain_rgroup.check(R.id.main_frag_mine);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void checuAppUpdate() {

        Call<AppUpdate> appUpdateCall = HttpManager.getInstance().getHttpClient().upDateApp("");
        appUpdateCall.enqueue(new Callback<AppUpdate>() {
            @Override
            public void onResponse(Call<AppUpdate> call, Response<AppUpdate> response) {
                AppUpdate body = response.body();
                if (body != null) {
                    int errorCode = body.getErrorCode();
                    if (errorCode == 0) {
                        final AppUpdate.ExtraBean extra = body.getExtra();
                        String versionCode = extra.getVersionCode();
                        int localVersion = MyApplication.getLocalVersion(MainActivity.this);
                        int iversionCode = Integer.parseInt(versionCode);
                        LUtils.d(TAG, "localVersion==" + localVersion);
                        LUtils.d(TAG, "iversionCode==" + iversionCode);
                        if (iversionCode > localVersion) {
                            // 开始下载
                            LUtils.d(TAG, "开始下载");
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("发现新版本" + extra.getVersionCode())
                                    .setMessage("是否升级？")
                                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_LONG).show();
                                            new AppDownloadTask().execute(extra.getUrl(),
                                                    extra.getApkFileName(), extra.getVersionCode());
                                        }
                                    }).create().show();

                        } else {
                            LUtils.d(TAG, "不下载");
                        }
                    } else {
                        LUtils.d(TAG, "body==null");
                    }
                } else {
                    LUtils.d(TAG, "请求失败 ");
                }
            }

            @Override
            public void onFailure(Call<AppUpdate> call, Throwable t) {

            }
        });
    }

    public class AppDownloadTask extends AsyncTask<String, Integer, Boolean> {
        private int mTotalBytes;


        //11//onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 初始化 NotificationCompat ；
            mBuilder = new NotificationCompat.Builder(MainActivity.this);
            mBuilder.setSmallIcon(R.mipmap.pandaapplogo);
            mBuilder.setContentTitle("下载中....");
            mBuilder.setContentText("正在下载");
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //            mManager.notify(NO_3, mBuilder.build());
            mBuilder.setProgress(100, 0, false);

            mIntentNotifi = new Intent(Intent.ACTION_VIEW);
            mIntentNotifi.setAction(Intent.ACTION_VIEW);
            mIntentNotifi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntentNotifi.addCategory(Intent.CATEGORY_DEFAULT);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            InputStream is = null;
            OutputStream output = null;

            Log.e(TAG, "params[0]:" + params[0]);
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
                    Log.e(TAG, "connection failed:" + params[0]);
                    return Boolean.FALSE;
                }
                mTotalBytes = conn.getContentLength();
                Log.e(TAG, "mTotalBytes===:" + mTotalBytes);
                is = conn.getInputStream();
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
                if (dir.exists()) {
                    File[] files = dir.listFiles();
                    for (File f : files) {
                        f.delete();
                    }
                } else {
                    dir.mkdirs();
                    Log.d(TAG, "create dir:" + dir.getAbsolutePath());
                }
                mDownloadedAppPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + params[1] + "_" + params[2] + ".apk";
                File file = new File(mDownloadedAppPath);
                Log.d(TAG, "mDownloadedAppPath ===" + mDownloadedAppPath);
                file.createNewFile();

                output = new FileOutputStream(file);
                byte[] buffer = new byte[(int) (1024 * 1024)];
                int current;
                int downloaded = 0;

                int downloaded_M = 0;
                while ((current = is.read(buffer)) != -1) {
                    output.write(buffer, 0, current);
                    downloaded += current;
                    if ((downloaded / (512 * 1024) > downloaded_M)) {
                        downloaded_M++;
                        publishProgress(downloaded);
                    }
                }
                output.flush();
                Log.d(TAG, "new app size:" + mTotalBytes + ", downloaded file length:" + file.length());
                return mTotalBytes == file.length() ? Boolean.TRUE : Boolean.FALSE;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Exception:", e);
                return Boolean.FALSE;
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    output = null;
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    is = null;
                }

            }
        }


        //2 onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mBuilder.setContentTitle("下载完成");
            mBuilder.setContentText("点击安装");
            mBuilder.setProgress(100, 100, false);
            if (aBoolean) {
                silentInstallApk(mDownloadedAppPath);
                //点击安装代码块
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            LUtils.d(TAG, "download package:" + values[0]);
            LUtils.d(TAG, "onProgressUpdate ---mTotalBytes --------  ==" + mTotalBytes);
            float percent = (float) values[0] / (float) mTotalBytes;
            float progress = (float) Math.floor(percent * 100);
            LUtils.d(TAG, "download package:progress  ==" + (int) progress);
            mBuilder.setProgress(100, (int) progress, false);
            mBuilder.setContentText("下载进度：" + (int) progress + "%");
            //  mBuilder.setContentIntent(resultPendingIntent);
            mManager.notify(NO_3, mBuilder.build());


        }
    }

    private void silentInstallApk(String apkPath) {
        Log.i(TAG, "silentInstallApk: " + apkPath);
        File file = new File(apkPath);
        if (!file.exists()) {
            Log.e(TAG, "silentInstallApk File doesn't exist:" + apkPath);
            return;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MainActivity.this, "com.pandacard.teavel.provider", file);
            mIntentNotifi.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        mIntentNotifi.setDataAndType(uri, "application/vnd.android.package-archive");
        PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, mIntentNotifi, 0);
        mBuild = mBuilder.setContentIntent(pi).build();
        mManager.notify(NO_3, mBuild);

    }


}
