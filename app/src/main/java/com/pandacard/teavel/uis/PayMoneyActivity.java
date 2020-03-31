package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.OrderInfoByCode;
import com.pandacard.teavel.https.beans.small_routine_bean.updateOrderToClose;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "PayMoneyActivity";
    private String mOrderCode;
    private TextView mOrderdetal_onfirm_num;
    private TextView mOrderdetal_onfirm_order;
    private TextView mOrderdetal_onfirm_ordertime;
    private TextView mOrderdetal_onfirm_paytime;
    private TextView mOrderdetal_onfirm_payway;
    private TextView mConfirm_button;

    private double mRealpaymoney;
    private ImageView mLly_attbarimageview;
    private TextView mConcle_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_money);

        Bundle intent = getIntent().getExtras();
        mOrderCode = intent.getString("orderCode");
        mRealpaymoney = intent.getDouble("realpaymoney");

        initView();
        LUtils.d(TAG, "mOrderCode==" + mOrderCode);

    }


    private void loaOrderDate(String orderCode) {
        Call<OrderInfoByCode> orderInfoByCode =
                HttpManager.getInstance().getHttpClient3().getOrderInfoByCode(orderCode);
        orderInfoByCode.enqueue(new Callback<OrderInfoByCode>() {
            @Override
            public void onResponse(Call<OrderInfoByCode> call, Response<OrderInfoByCode> response) {
                LUtils.d(TAG, "onResponse  ");
                if (response.body() != null) {
                    OrderInfoByCode createTime = response.body();
                    String createTime1 = createTime.getOrderInfo().getCreateTime();

                    int orderState = response.body().getOrderInfo().getOrderState();
                    LUtils.d(TAG, "createTime==" + createTime1);
                    LUtils.d(TAG, "orderState==" + orderState);
                    mOrderdetal_onfirm_ordertime.setText(createTime1 + "");
                    if (orderState == 0) {
                        mOrderdetal_onfirm_paytime.setText("待付款");
                    } else if (orderState == 1) {
                        mOrderdetal_onfirm_paytime.setText("已付款");
                    } else if (orderState == 2) {
                        mOrderdetal_onfirm_paytime.setText("待收货");
                    } else if (orderState == 3) {
                        mOrderdetal_onfirm_paytime.setText("已完成");
                    } else if (orderState == 4) {
                        mOrderdetal_onfirm_paytime.setText("已失效");
                    } else if (orderState == 5) {
                        mOrderdetal_onfirm_paytime.setText("已评论");
                    } else if (orderState == 6) {
                        mOrderdetal_onfirm_paytime.setText("支付失败");
                    } else if (orderState == 7) {
                        mOrderdetal_onfirm_paytime.setText("（申请退款）退款中");
                    } else if (orderState == 8) {
                        mOrderdetal_onfirm_paytime.setText("退款成功");
                    } else if (orderState == 9) {
                        mOrderdetal_onfirm_paytime.setText(" 退款失败");
                    }

                } else {
                    LUtils.d(TAG, "response.body()==null==");
                }
            }

            @Override
            public void onFailure(Call<OrderInfoByCode> call, Throwable t) {
                LUtils.d(TAG, "onFailure  " + t);
            }
        });

    }

    private void initView() {
        mLly_attbarimageview = findViewById(R.id.lly_attbarimageview);
        mOrderdetal_onfirm_num = findViewById(R.id.orderdetal_onfirm_num);
        mOrderdetal_onfirm_order = findViewById(R.id.orderdetal_onfirm_order);
        mOrderdetal_onfirm_ordertime = findViewById(R.id.orderdetal_onfirm_ordertime);
        mOrderdetal_onfirm_paytime = findViewById(R.id.orderdetal_onfirm_paytime);
        mOrderdetal_onfirm_payway = findViewById(R.id.orderdetal_onfirm_payway);
        mConfirm_button = findViewById(R.id.confirm_button);
        mConcle_button = findViewById(R.id.concle_button);
        mLly_attbarimageview.setOnClickListener(this);
        mConcle_button.setOnClickListener(this);
        mConfirm_button.setOnClickListener(this);
        mOrderdetal_onfirm_num.setText(mRealpaymoney + "");
        mOrderdetal_onfirm_order.setText(mOrderCode);

        loaOrderDate(mOrderCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_button:
                Intent inpay = new Intent(this, ToWechatPayActivity.class);
                Bundle b = new Bundle();
                b.putDouble("mRealpaymoney", mRealpaymoney);
                b.putString("mOrderCode", mOrderCode);
                inpay.putExtras(b);
                startActivity(inpay);
                break;
            case R.id.concle_button:
                concleOrder();

                break;
            case R.id.lly_attbarimageview:
                finish();
                break;
        }
    }

    private void concleOrder() {
        Call<updateOrderToClose> updateOrderToCloseCall
                = HttpManager.getInstance().getHttpClient3().updateOrderToClose(mOrderCode);
        updateOrderToCloseCall.enqueue(new Callback<updateOrderToClose>() {
            @Override
            public void onResponse(Call<updateOrderToClose> call, Response<updateOrderToClose> response) {

                if (response.body() != null) {
                    updateOrderToClose body = response.body();
                    boolean status = body.isStatus();
                    if (status) {
                        ToastUtils.showToast(PayMoneyActivity.this, body.getMsg());
                        finish();
                        Intent intent = new Intent(PayMoneyActivity.this, ByPandaActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<updateOrderToClose> call, Throwable t) {

            }
        });
    }
}
