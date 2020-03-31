package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.updateOrderToSuccess;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToWechatPayActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ToWechatPayActivity";
    Button pay_money_wx;
    private ImageView mLly_attbarimageview;
    private TextView wechatpay_text;
    private double mMRealpaymoney;
    private String mMOrderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_wechat_pay);
        Bundle extras = getIntent().getExtras();
        mMRealpaymoney = extras.getDouble("mRealpaymoney");
        mMOrderCode = extras.getString("mOrderCode");
        initView();
    }

    private void initView() {
        pay_money_wx = findViewById(R.id.pay_money_wx);
        wechatpay_text = findViewById(R.id.wechatpay_text);
        mLly_attbarimageview = findViewById(R.id.lly_attbarimageview);

        pay_money_wx.setOnClickListener(this);
        mLly_attbarimageview.setOnClickListener(this);
        wechatpay_text.setText(mMRealpaymoney + "");
        //mMRealpaymoney
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_money_wx:

//                final IWXAPI mMsgApi = WXAPIFactory.createWXAPI(this, HttpRetrifitUtils.vxAPPID);
                // 将该app注册到微信
//                mMsgApi.registerApp(HttpRetrifitUtils.vxAPPID);
//                PayReq request = new PayReq();

//                request.appId = body.getAppId();//微信开放平台审核通过的应用
//                request.partnerId = body.getParterId();//商户号  1497835842 parterId
//                request.prepayId = body.getPrepayId();//预支付交易会话ID
//                request.sign = body.getSign();//签名
//                request.packageValue = "Sign=WXPay";//扩展字段
//                request.nonceStr = body.getNoncestr();//随机字符串
//                request.timeStamp = body.getTimestamp();//时间戳

//                mMsgApi.sendReq(request);

                //6.支付成功回调接口
                Call<updateOrderToSuccess> updateOrderToSuccessCall
                        = HttpManager.getInstance().getHttpClient3().updateOrderToSuccess(mMOrderCode);
                updateOrderToSuccessCall.enqueue(new Callback<updateOrderToSuccess>() {
                    @Override
                    public void onResponse(Call<updateOrderToSuccess> call, Response<updateOrderToSuccess> response) {
                        if (response.body() != null) {

                            if (response.body().isStatus()) {
                                LUtils.d(TAG, response.body().getMsg());


                                Intent intent = new Intent(ToWechatPayActivity.this, PaySuccessActivity.class);
                                Bundle b = new Bundle();
                                b.putString("CardInfocade", mMOrderCode);
                                intent.putExtras(b);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<updateOrderToSuccess> call, Throwable t) {

                    }
                });
                break;
            case R.id.lly_attbarimageview:
                finish();
                break;
        }
    }
}
