package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.queryUserOrderCardById;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardOrderDetalActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mOrderdetal_onfirm_num;
    private TextView mOrderdetal_onfirm_order;
    private TextView mOrderdetal_onfirm_ordertime;
    private TextView mOrderdetal_onfirm_paytime;
    private TextView mOrderdetal_onfirm_payway;
    private ImageView mLly_attbarimageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_order_detal);
        initView();
    }

    private void initView() {
        mOrderdetal_onfirm_num = findViewById(R.id.cardorderdetal_onfirm_num);
        mLly_attbarimageview = findViewById(R.id.lly_attbarimageview);
        mOrderdetal_onfirm_order = findViewById(R.id.cardorderdetal_onfirm_order);
        mOrderdetal_onfirm_ordertime = findViewById(R.id.cardorderdetal_onfirm_ordertime);
        mOrderdetal_onfirm_paytime = findViewById(R.id.cardorderdetal_onfirm_paytime);
        mOrderdetal_onfirm_payway = findViewById(R.id.cardorderdetal_onfirm_payway);
        mLly_attbarimageview.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        String cardInfocade = extras.getString("CardInfocade");
        if (cardInfocade != null) {
            initDate(cardInfocade);
        }
    }

    private void initDate(String cardInfocade) {

        Call<queryUserOrderCardById> queryUserOrderCardByIdCall =
                HttpManager.getInstance().getHttpClient3().queryUserOrderCardById(cardInfocade);
        queryUserOrderCardByIdCall.enqueue(new Callback<queryUserOrderCardById>() {
            @Override
            public void onResponse(Call<queryUserOrderCardById> call, Response<queryUserOrderCardById> response) {
                if (response.body() != null) {
                    queryUserOrderCardById body = response.body();
                    queryUserOrderCardById.CardInfoBean cardInfo = body.getCardInfo();
                    mOrderdetal_onfirm_num.setText(cardInfo.getProName());
                    mOrderdetal_onfirm_order.setText(cardInfo.getOrderCode());
                    mOrderdetal_onfirm_ordertime.setText(cardInfo.getCreateTime());
                    mOrderdetal_onfirm_paytime.setText(cardInfo.getLastUpdateTime());

                    mOrderdetal_onfirm_payway.setText("微信支付");

                }

            }

            @Override
            public void onFailure(Call<queryUserOrderCardById> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_attbarimageview:
                finish();
                break;
        }
    }
}
