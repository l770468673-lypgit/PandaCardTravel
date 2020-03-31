package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.StatusBarUtil;

public class RollActivity extends AppCompatActivity {

    private TextView mChongzhinfc_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initView();

    }

    private void initView() {
        mChongzhinfc_textView = findViewById(R.id.chongzhinfc_textView);
        mChongzhinfc_textView.setText(R.string.fragment_mine_minecardwallet);
    }
}
