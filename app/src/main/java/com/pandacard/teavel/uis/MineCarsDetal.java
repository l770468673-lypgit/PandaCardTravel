package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.StatusBarUtil;

public class MineCarsDetal extends AppCompatActivity implements View.OnClickListener {


    private ImageView mAttbarimageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_cars_detal);

        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initview();

    }

    private void initview() {
        mAttbarimageview = findViewById(R.id.mincardattbarimageview);


        mAttbarimageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mincardattbarimageview:
                finish();
                break;
        }
    }
}
