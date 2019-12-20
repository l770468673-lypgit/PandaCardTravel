package com.pandacard.teavel.uis;

import android.os.Bundle;

import com.pandacard.teavel.R;
import com.pandacard.teavel.bases.BaseActivity;
import com.pandacard.teavel.utils.StatusBarUtil;

public class MoreDisposeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_dispose);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
    }
}
