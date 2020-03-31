package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.ToastUtils;

public class PaySuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTocardorder;
    private TextView mTocarddetal;
    private TextView mToorderdetal;
    private String mCardInfocade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        initview();
    }

    private void initview() {
        mTocardorder = findViewById(R.id.tocardorder);
        mTocarddetal = findViewById(R.id.tocarddetal);
        mToorderdetal = findViewById(R.id.toorderdetal);

        mTocardorder.setOnClickListener(this);
        mToorderdetal.setOnClickListener(this);
        mTocarddetal.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        mCardInfocade = extras.getString("CardInfocade");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tocardorder:
                Intent intent = new Intent(this, CardOrder_Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tocarddetal:
//                Intent intent2 = new Intent(this, CardOrderDetalActivity.class);
//                Bundle b = new Bundle();
//                b.putString("CardInfocade", mCardInfocade);
//                intent2.putExtras(b);
//                startActivity(intent2);
                ToastUtils.showToast(this, "暂无");
                break;
            case R.id.toorderdetal:
                Intent intent3 = new Intent(this, OrderMy_Activity.class);

                startActivity(intent3);
                finish();
                break;
        }
    }
}
