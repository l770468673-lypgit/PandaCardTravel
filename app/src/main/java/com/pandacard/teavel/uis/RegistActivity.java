package com.pandacard.teavel.uis;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.bases.BaseActivity;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;


public class RegistActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegistActivity";
    private TextView mChongzhinfc_textView;
    private ImageView mchongzhinfc_imageview_back;
    private Button mlogin_regist, mbtn_yanzhengma;
    private TimeCount mTimeCount;
    private EditText mreg_phonenum, mimgregpasswordyanzhengmg, mokregpassword, mre_okregpassword;
    private String mVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);

        initview();


    }

    class TimeCount extends CountDownTimer {

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
            mbtn_yanzhengma.setText("获取验证码");
            mbtn_yanzhengma.setClickable(true);


        }
    }

    private void initview() {
        mTimeCount = new TimeCount(60000, 1000);

        mChongzhinfc_textView = findViewById(R.id.chongzhinfc_textView);
        mchongzhinfc_imageview_back = findViewById(R.id.chongzhinfc_imageview_back);
        mbtn_yanzhengma = findViewById(R.id.btn_yanzhengma);
        mlogin_regist = findViewById(R.id.login_regist);

        mreg_phonenum = findViewById(R.id.reg_phonenum);
        mimgregpasswordyanzhengmg = findViewById(R.id.imgregpasswordyanzhengmg);
        mokregpassword = findViewById(R.id.okregpassword);
        mre_okregpassword = findViewById(R.id.re_okregpassword);

        mChongzhinfc_textView.setText(getResources().getText(R.string.regist_title_name));
        mlogin_regist.setOnClickListener(this);
        mbtn_yanzhengma.setOnClickListener(this);
        mchongzhinfc_imageview_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chongzhinfc_imageview_back:
                finish();
                break;
            case R.id.login_regist:
                if (mimgregpasswordyanzhengmg.getText().toString().trim().equals(mVerifyCode)) {
                    if (mokregpassword.getText().toString().trim().equals(mre_okregpassword.getText().toString().trim())) {

                        //                        Call<RegistBean> registBeanCall = HttpManager.getInstance().getHttpClient().toRegist("104", mreg_phonenum.getText().toString().trim(), mokregpassword.getText().toString().trim());
                        //
                        //                        registBeanCall.enqueue(new Callback<RegistBean>() {
                        //                            @Override
                        //                            public void onResponse(Call<RegistBean> call, Response<RegistBean> response) {
                        //                                RegistBean body = response.body();
                        //                                if (body != null) {
                        //                                    if (body.getErrorCode() == 0) {
                        //
                        //                                        ToastUtils.showToast(RegistActivity.this, body.getErrorMsg() + "请登录");
                        //                                        RequestManager.getInstance().register(mreg_phonenum.getText().toString().trim(), mokregpassword.getText().toString().trim(),
                        //                                                "", "", "", new HttpResponseListener() {
                        //                                                    @Override
                        //                                                    public void onResponseSuccess(int i, Object o) {
                        //                                                        LUtils.d(TAG, "code===" + i);
                        //                                                    }
                        //
                        //                                                    @Override
                        //                                                    public void onResponseError(Throwable throwable) {
                        //                                                        LUtils.d(TAG, "throwable===" + throwable.getMessage());
                        //                                                    }
                        //                                                });
                        //                                        finish();
                        //                                    } else {
                        //
                        //                                        ToastUtils.showToast(RegistActivity.this, body.getErrorMsg());
                        //                                    }
                        //                                }
                        //                            }
                        //
                        //                            @Override
                        //                            public void onFailure(Call<RegistBean> call, Throwable t) {
                        //
                        //                            }
                        //                        });

                    } else {
                        ToastUtils.showToast(this, "两次密码不一致");
                        LUtils.d(TAG, " ===" + mokregpassword.getText().toString().trim() + "===" + mre_okregpassword.getText().toString().trim());
                    }
                } else {
                    ToastUtils.showToast(RegistActivity.this, "验证码不正确");
                }
                break;
            case R.id.btn_yanzhengma:
                mbtn_yanzhengma.setClickable(false);
                mTimeCount.start();
//                Call<SecurityCode> securityCode =
//                        HttpManager.getInstance().getHttpClient().getSecurityCode("103", mreg_phonenum.getText().toString().trim());
//                securityCode.enqueue(new Callback<SecurityCode>() {
//                    @Override
//                    public void onResponse(Call<SecurityCode> call, Response<SecurityCode> response) {
//                        SecurityCode body = response.body();
//                        if (body != null) {
//                            if (body.getErrorCode() == 0) {
//                                mVerifyCode = body.getExtra().getVerifyCode();
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SecurityCode> call, Throwable t) {
//
//                    }
//                });

                break;
        }
    }
}
