package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.StatusBarUtil;

public class confirmActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mOnfirm_num;
    private int mStringExtra;
    private Button mConfirm_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initView();
    }

    private void initView() {

        mStringExtra = getIntent().getIntExtra(HttpRetrifitUtils.ACT_BUNUM, 0);
        mOnfirm_num = findViewById(R.id.onfirm_num);
        mConfirm_button = findViewById(R.id.confirm_button);
        mOnfirm_num.setText(mStringExtra + "件");
        mConfirm_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_button:

                break;
        }
    }
}
