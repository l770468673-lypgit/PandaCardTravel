package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.generateOrder;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class confirmActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "confirmActivity";
    private TextView mOnfirm_num;
    private int mStringExtra;
    private Button mConfirm_button;
    private TextView mText_contmoney;
    private String mMProductId;
    private double mGoodsCostPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);

        Bundle extras = getIntent().getExtras();
        mStringExtra = extras.getInt(HttpRetrifitUtils.ACT_BUNUM, 0);
        mMProductId = extras.getString("mProductId");
        mGoodsCostPrice = extras.getDouble("mGoodsCostPrice");

        initView();
    }

    private void initView() {
        mText_contmoney = findViewById(R.id.text_contmoney);
        mOnfirm_num = findViewById(R.id.onfirm_num);

        mConfirm_button = findViewById(R.id.confirm_button);
        mOnfirm_num.setText(mStringExtra + "件");
        LUtils.d(TAG, "mOnfirm_num==ss" + mStringExtra);
        mConfirm_button.setOnClickListener(this);
        mText_contmoney.setText("合计 ：" + mStringExtra * mGoodsCostPrice + "");

    }


    private void ConfirmOrder(final Dialog dialog) {

        Call<generateOrder> generateOrderCall = HttpManager.getInstance().getHttpClient3().
                generateOrder(ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID),
                        mMProductId,
                        mGoodsCostPrice,
                        mStringExtra * mGoodsCostPrice,
                        mStringExtra * mGoodsCostPrice,
                        0.0,
                        "",
                        mStringExtra,
                        1);
        generateOrderCall.enqueue(new Callback<generateOrder>() {
            @Override
            public void onResponse(Call<generateOrder> call, Response<generateOrder> response) {
                if (response.body() != null) {
                    String orderCode = response.body().getData().getOrderCode();
                    LUtils.d(TAG, "orderCode id ==" + orderCode);
                    dialog.dismiss();
                    dialog.cancel();
                    Intent intentoredr = new Intent(confirmActivity.this,
                            PayMoneyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("orderCode", orderCode);
                    bundle.putDouble("realpaymoney", mStringExtra * mGoodsCostPrice);
                    intentoredr.putExtras(bundle);
                    startActivity(intentoredr);

                }
            }

            @Override
            public void onFailure(Call<generateOrder> call, Throwable t) {
                ToastUtils.showToast(confirmActivity.this,"请求失败 ,稍后再试 ");
                dialog.dismiss();
                dialog.cancel();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_button:
                //提交订单
                mConfirm_button.setClickable(false);
                ProgressDialog dialog = new ProgressDialog(this);
                // 设置进度条风格，风格为圆形，旋转的
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(true);
                dialog.show();
                ConfirmOrder(dialog);

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConfirm_button.setClickable(true);
    }
}
