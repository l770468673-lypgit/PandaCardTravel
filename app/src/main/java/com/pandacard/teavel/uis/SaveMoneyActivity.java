package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.StatusBarUtil;

public class SaveMoneyActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView mSave_imageview_back;
    private RadioButton mSave_radio_btn_recharge;
    private RadioButton mSave_radio_btn_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_money);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initView();
    }

    private void initView() {

        mSave_imageview_back = findViewById(R.id.save_imageview_back);
        mSave_radio_btn_recharge = findViewById(R.id.save_radio_btn_recharge);
        mSave_radio_btn_more = findViewById(R.id.save_radio_btn_more);

        mSave_imageview_back.setOnClickListener(this);
        mSave_radio_btn_recharge.setOnClickListener(this);
        mSave_radio_btn_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.save_imageview_back:
                finish();
                break;
            case R.id.save_radio_btn_recharge:
                Toast.makeText(this, "开发中", Toast.LENGTH_LONG).show();
                break;
            case R.id.save_radio_btn_more:

                Intent intent = new Intent(this, MoreDisposeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
