package com.pandacard.teavel.uis;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.pandacard.teavel.R;
import com.pandacard.teavel.bases.BaseActivity;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.ResourcesBean;
import com.pandacard.teavel.https.beans.SecurityCode;
import com.pandacard.teavel.https.beans.resetPass;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.TimerUtils;
import com.pandacard.teavel.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegistActivity";
    private TextView mChongzhinfc_textView;
    private ImageView mchongzhinfc_imageview_back;
    private Button mlogin_regist, mbtn_yanzhengma;
    private EditText mreg_phonenum, mimgregpasswordyanzhengmg, mokregpassword, mre_okregpassword;

    private int mAnInt;
    private static String sVcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        // 1是重置密码
        // 2 是注册号码
        Bundle extras = getIntent().getExtras();
        mAnInt = extras.getInt(HttpRetrifitUtils.ACT_TITLENAME);

        initview(mAnInt);
    }
    private void initview(int anInt) {
        mChongzhinfc_textView = findViewById(R.id.chongzhinfc_textView);
        mchongzhinfc_imageview_back = findViewById(R.id.chongzhinfc_imageview_back);
        mbtn_yanzhengma = findViewById(R.id.btn_yanzhengma);
        mlogin_regist = findViewById(R.id.login_regist);
        mreg_phonenum = findViewById(R.id.reg_phonenum);
        mimgregpasswordyanzhengmg = findViewById(R.id.imgregpasswordyanzhengmg);
        mokregpassword = findViewById(R.id.okregpassword);
        mre_okregpassword = findViewById(R.id.re_okregpassword);
        if (anInt == 1) {
            mChongzhinfc_textView.setText(getResources().getText(R.string.reset_title_name));
        } else {
            mChongzhinfc_textView.setText(getResources().getText(R.string.regist_title_name));
        }
        mlogin_regist.setOnClickListener(this);
        mbtn_yanzhengma.setOnClickListener(this);
        mchongzhinfc_imageview_back.setOnClickListener(this);
        TimerUtils.initTimer(this, mbtn_yanzhengma, 60000, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chongzhinfc_imageview_back:
                finish();
                break;
            case R.id.login_regist:
                if (mimgregpasswordyanzhengmg.getText().toString().trim().equals(sVcode)) {
                    LUtils.d(TAG, mokregpassword.getText().toString().trim());
                    LUtils.d(TAG, mre_okregpassword.getText().toString().trim());
                    //判断第一个字符是否为“数”
                    if (mreg_phonenum.getText().toString().trim().startsWith("1")) {
                        if (mokregpassword.getText().toString().trim().equals(mre_okregpassword.getText().toString().trim()) &&
                                mokregpassword.getText().toString().trim().length() >= 6) {
                            // 1是重置密码
                            // 2 是注册号码
                            if (mAnInt == 2) {
                                HttpRetrifitUtils.toPhoneRegist(mreg_phonenum.getText().toString().trim(),
                                        mokregpassword.getText().toString().trim(), RegistActivity.this);
                            } else {
                                HttpRetrifitUtils.ResetPassword(mreg_phonenum.getText().toString().trim(),
                                        mokregpassword.getText().toString().trim(),RegistActivity.this);
                            }
                        } else {
                            ToastUtils.showToast(this, "密码不正确");
                            LUtils.d(TAG, " ===" + mokregpassword.getText().toString().trim() + "===" + mre_okregpassword.getText().toString().trim());
                        }
                    } else {
                        ToastUtils.showToast(this, "手机号码不正确");
                    }
                } else {
                    ToastUtils.showToast(RegistActivity.this, "验证码不正确");
                }
                break;
            case R.id.btn_yanzhengma:
                if (mreg_phonenum.getText().toString().trim().length() == 11 && mreg_phonenum.getText().toString().trim().startsWith("1")) {
                    TimerUtils.TimerStart();
                    sVcode = null;
                  getSMSCode(mreg_phonenum.getText().toString().trim(), RegistActivity.this);

                } else {
                    ToastUtils.showToast(this, "检查手机号码是否正确");
                }
                break;
        }
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
