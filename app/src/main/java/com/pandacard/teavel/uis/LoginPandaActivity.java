package com.pandacard.teavel.uis;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pandacard.teavel.R;
import com.pandacard.teavel.bases.BasePandaActivity;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.Mobilesbean;
import com.pandacard.teavel.https.beans.SecurityCode;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.KeyboardUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.TimerUtils;
import com.pandacard.teavel.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPandaActivity extends BasePandaActivity implements View.OnClickListener, PlatformActionListener {

    private static final String TAG = "LoginActivity";
    private Button mlogin_logineddenglu;
    List<String> mPermissionList = new ArrayList<>();
    private int PERMISSION_CODE = 1000;
    AlertDialog mPermissionDialog;
    private cn.sharesdk.framework.Platform mWechat;

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
    private TextView mForgetpass;
    private ImageView mlogin_will_wxlogin;
    private ImageView mPassvisvilit;

    private boolean editflagView = true;
    private EditText mLogin_password;
    private RelativeLayout mRel_login_editphone;
    private RelativeLayout mRel_wxlogin_editphone;
    private loginHandler mHandler;
    public static int WX_LOGIN_SETPHONE = 23001;
    private TextView mChongzhinfc_textView;
    private ImageView mChongzhinfc_imageview_back;
    private Button mBtn_yanzhengma;
    private EditText mWx_login_phonenum;
    private EditText mWx_login_password;
    private Button mWx_login_loginedyt;

    private EditText mLogin_phonenum;
    private   String sVcode;


    class loginHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 23001:
                    LUtils.d(TAG, "微信登录成功");
                    mRel_wxlogin_editphone.setVisibility(View.VISIBLE);
                    mRel_login_editphone.setVisibility(View.GONE);
                    mRel_login_editphone.setClickable(false);
                    break;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        mWechat = ShareSDK.getPlatform(Wechat.NAME);
        mWechat.setPlatformActionListener(this);
        mHandler = new loginHandler();
        loadporerColor();
        initPermission();
    }

    private void loadporerColor() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.login_back));
        }
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

        mlogin_logineddenglu = findViewById(R.id.login_logindenglu);

        mLogin_password = findViewById(R.id.login_password);
        mLogin_phonenum = findViewById(R.id.login_phonenum);

        mLogin_phonenum_reg = findViewById(R.id.login_phonenum_reg);
        mlogin_will_wxlogin = findViewById(R.id.login_will_wxlogin);

        mPassvisvilit = findViewById(R.id.passvisvilit);
        mRel_login_editphone = findViewById(R.id.rel_login_editphone);
        mRel_wxlogin_editphone = findViewById(R.id.rel_wxlogin_editphone);

        mChongzhinfc_textView = findViewById(R.id.chongzhinfc_textView);
        mBtn_yanzhengma = findViewById(R.id.btn_yanzhengma);

        mWx_login_phonenum = findViewById(R.id.wx_login_phonenum);
        mWx_login_password = findViewById(R.id.wx_login_password);
        mWx_login_loginedyt = findViewById(R.id.wx_login_logindenglu);

        mForgetpass = findViewById(R.id.forgetpass);


        mChongzhinfc_imageview_back = findViewById(R.id.chongzhinfc_imageview_back);

        TimerUtils.initTimer(this, mBtn_yanzhengma, 60000, 1000);
        mlogin_logineddenglu.setOnClickListener(this);
        mBtn_yanzhengma.setOnClickListener(this);
        mLogin_phonenum_reg.setOnClickListener(this);
        mlogin_will_wxlogin.setOnClickListener(this);
        mWx_login_loginedyt.setOnClickListener(this);
        mPassvisvilit.setOnClickListener(this);
        mlogin_logineddenglu.setClickable(true);
        mForgetpass.setOnClickListener(this);
        mChongzhinfc_imageview_back.setOnClickListener(this);
        mChongzhinfc_textView.setText(getResources().getText(R.string.login_wx_phonebind));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_logindenglu:

                if (mLogin_phonenum.getText().toString().trim().length() == 11 && mLogin_password.getText().toString().trim().length() > 0) {
                    HttpRetrifitUtils.toLogin(mLogin_phonenum.getText().toString().trim(),
                            mLogin_password.getText().toString().trim(), LoginPandaActivity.this);
                    HttpRetrifitUtils.toWchatReg(mLogin_phonenum.getText().toString().trim(),
                            mLogin_password.getText().toString().trim());
                } else {
                    ToastUtils.showToast(this, "请检查参数是否正确");
                }
                break;
            case R.id.login_phonenum_reg:
                Intent inreg = new Intent(this, RegistPandaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(HttpRetrifitUtils.ACT_TITLENAME, 2);// 2 是注册
                inreg.putExtras(bundle);
                startActivity(inreg);
                startActivity(inreg);
                finish();
                break;
            case R.id.wx_login_logindenglu:
                if (ShareUtil.getString(HttpRetrifitUtils.WXLOGIN_UNID) != null
                        && mWx_login_phonenum.getText().toString().trim().length() == 11//) {
                        && sVcode.equals(mWx_login_password.getText().toString().trim())) {
                    HttpRetrifitUtils.WX_Regist(ShareUtil.getString(HttpRetrifitUtils.WXLOGIN_UNID),
                            mWx_login_phonenum.getText().toString().trim(), LoginPandaActivity.this);
                } else {
                    ToastUtils.showToast(this, "请检查参数");
                }

                break;
            case R.id.btn_yanzhengma:
                if (mWx_login_phonenum.getText().toString().trim().length() == 11) {

                    TimerUtils.TimerStart();
                    getSMSCode(mWx_login_phonenum.getText().toString().trim(), LoginPandaActivity.this);

                } else {
                    ToastUtils.showToast(this, R.string.login_wx_editokphone);
                }

                break;
            case R.id.chongzhinfc_imageview_back:
                mRel_wxlogin_editphone.setVisibility(View.GONE);
                mRel_login_editphone.setVisibility(View.VISIBLE);
                mRel_login_editphone.setClickable(true);
                TimerUtils.TimerStop(getResources().getString(R.string.login_wx_querycode));
                KeyboardUtils.hideKeyboard(this);
                break;
            case R.id.passvisvilit:
                if (!editflagView) {
                    mLogin_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mLogin_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                editflagView = !editflagView;
                mLogin_password.postInvalidate();
                CharSequence text = mLogin_password.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
                break;
            case R.id.login_will_wxlogin:
                LUtils.d(TAG, "：----login_will_wxlogin---------- ");

                mWechat.showUser(null);
                LUtils.d(TAG, "：----login_will_wxlogin-------mWechat--- ");
                break;
            case R.id.forgetpass:
                Intent inreset = new Intent(this, RegistPandaActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt(HttpRetrifitUtils.ACT_TITLENAME, 1);// 1 是重置密码
                inreset.putExtras(bundle2);
                startActivity(inreset);
                finish();
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
            //权限已经都通过
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
                            LoginPandaActivity.this.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                            LoginPandaActivity.this.finish();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();

    }

    @Override
    public void onComplete(cn.sharesdk.framework.Platform platform, int i, HashMap<String, Object> hashMap) {
        Iterator ite = hashMap.entrySet().iterator();

        while (ite.hasNext()) {
            Map.Entry entry = (Map.Entry) ite.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (entry.getKey().equals("unionid")) {
                LUtils.d(TAG, key + "：-----unionid--------- " + entry.getValue());
                ShareUtil.putString(HttpRetrifitUtils.WXLOGIN_UNID, (String) entry.getValue());
                getMobiles((String) entry.getValue());
            }
            if (entry.getKey().equals("headimgurl")) {
                LUtils.d(TAG, key + "：-----headimgurl--------- " + entry.getValue());
            }
        }
    }

    @Override
    public void onError(cn.sharesdk.framework.Platform platform, final int i, final Throwable throwable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LUtils.d(TAG, "微信登录失败!" + i + throwable);
                ToastUtils.showToast(getBaseContext(), "微信登录失败!" + i + throwable);
            }
        });
    }

    public void getMobiles(final String uid) {
        Call<Mobilesbean> mobiles = HttpManager.getInstance().getHttpClient().getMobiles(uid);
        mobiles.enqueue(new Callback<Mobilesbean>() {
            @Override
            public void onResponse(Call<Mobilesbean> call, Response<Mobilesbean> response) {
                if (response.body() != null) {
                    String mobiles1 = response.body().getExtra().getMobiles();

                    if (mobiles1.length() < 1) {
                        mHandler.sendEmptyMessage(WX_LOGIN_SETPHONE);
                    } else {
                        HttpRetrifitUtils.toWXlogin(uid, LoginPandaActivity.this);
                        ShareUtil.putString(HttpRetrifitUtils.SERNAME_PHONE, mobiles1);
                    }
                }
            }

            @Override
            public void onFailure(Call<Mobilesbean> call, Throwable t) {

            }
        });

    }

    @Override
    public void onCancel(cn.sharesdk.framework.Platform platform, final int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                mDialog2.dismiss();
                LUtils.d(TAG, "微信取消登录!" + i);
                ToastUtils.showToast(getBaseContext(), "微信取消登录!" + i);

            }
        });
    }

    /**
     * 获取验证码
     */
    public  void getSMSCode(String phone, final Activity context) {
        Call<SecurityCode> securityCode =
                HttpManager.getInstance().getHttpClient().toSMSCode(phone);
        securityCode.enqueue(new Callback<SecurityCode>() {
            @Override
            public void onResponse(Call<SecurityCode> call, Response<SecurityCode> response) {
                if (response.body() != null) {
                    sVcode = response.body().getExtra().getVcode();

                }
            }

            @Override
            public void onFailure(Call<SecurityCode> call, Throwable t) {
            }
        });



    }

}
