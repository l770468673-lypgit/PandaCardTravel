package com.pandacard.teavel.uis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pandacard.teavel.MainActivity;
import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.LUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {

    private static final String TAG = "LoginActivity";
    private Button mlogin_loginedyt;
    List<String> mPermissionList = new ArrayList<>();
    private int PERMISSION_CODE = 1000;
    AlertDialog mPermissionDialog;
    private Platform mWechat;

    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            //            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };
    private TextView mLogin_phonenum_reg;
    private ImageView mlogin_will_wxlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initPermission();


    }

    private void initPermission() {
        mPermissionList.clear();
        //逐个判断是否还有未通过的权限
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) !=
                    PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限到mPermissionList中
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
        } else {
            //权限已经都通过了，可以将程序继续打开了
            initView();
        }


    }

    private void initView() {


        mlogin_loginedyt = findViewById(R.id.login_loginedyt);
        mLogin_phonenum_reg = findViewById(R.id.login_phonenum_reg);
        mlogin_will_wxlogin = findViewById(R.id.login_will_wxlogin);

        mlogin_loginedyt.setOnClickListener(this);
        mLogin_phonenum_reg.setOnClickListener(this);
        mlogin_will_wxlogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_loginedyt:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                break;
            case R.id.login_phonenum_reg:


                break;
            case R.id.login_will_wxlogin:
                mWechat = ShareSDK.getPlatform(Wechat.NAME);
                mWechat.setPlatformActionListener(this);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasPermissionDismiss = false;//有权限没有通过
        if (PERMISSION_CODE == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    LUtils.d(TAG, "grantResults[i]==" + i);
                    hasPermissionDismiss = true;
                    break;
                }
            }
        }
        if (hasPermissionDismiss) {//如果有没有被允许的权限
            showPermissionDialog();

        } else {
            //权限已经都通过了，可以将程序继续打开了
            initView();
        }


    }

    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + "com.estone.bank.estone_appsmartlock");
                            Intent intent = new Intent(Settings.
                                    ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                            LoginActivity.this.finish();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Iterator ite = hashMap.entrySet().iterator();
        //        mDialog2.dismiss();

        while (ite.hasNext()) {
            Map.Entry entry = (Map.Entry) ite.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            LUtils.d(TAG, key + "：-------------- " + value);
            LUtils.d(TAG, hashMap.toString());
        }
    }

    @Override
    public void onError(Platform platform, final int i, final Throwable throwable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // mDialog2.dismiss();
                //                mAnimaition.stop();
                //                iamge_loaddate_anim.setVisibility(View.GONE);
                LUtils.d(TAG, "微信登录失败!" + i + throwable);
                Toast.makeText(getBaseContext(), "微信登录失败!" + i + throwable, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCancel(Platform platform, final int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                mDialog2.dismiss();

                LUtils.d(TAG, "微信取消登录!" + i);
                Toast.makeText(getBaseContext(), "微信取消登录!" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
