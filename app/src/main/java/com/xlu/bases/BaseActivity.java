package com.xlu.bases;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.lidroid.xutils.ViewUtils;
import com.pandacard.teavel.R;
import com.xlu.po.MyEvent;
import com.xlu.widgets.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public abstract class BaseActivity extends FragmentActivity {

	protected Context context;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	// -------------------------Activity--------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		setContentView(initView());
		ViewUtils.inject(this);
		ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);

		super.onDestroy();
	}

	// ------------------------Abstract Method--------------------------------
	protected abstract int initView();
	
	 //----------------------------LoadFail------------------------------------
    @Nullable
    @BindView(R.id.load_fail)
    protected RelativeLayout rl_fail;
    @Nullable
    @BindView(R.id.load_fail_reload)
    protected Button btn_reload;
    @Nullable
    @BindView(R.id.load_fail_text)
    protected TextView tv_text;
    @Nullable
    @BindView(R.id.load_fail_progress)
    protected ProgressWheel pw_wheel;
    @Nullable
    @BindView(R.id.load_fail_icon)
    protected ImageView iv_icon;

    @Nullable
    public final void isLoading() {
        rl_fail.setVisibility(View.VISIBLE);
        pw_wheel.setVisibility(View.VISIBLE);
        btn_reload.setVisibility(View.GONE);
        tv_text.setVisibility(View.GONE);
        iv_icon.setVisibility(View.GONE);
    }

    @Nullable
    public final void isFinished() {
        rl_fail.setVisibility(View.GONE);
    }

    @Nullable
    public final void isFailed() {
        rl_fail.setVisibility(View.VISIBLE);
        pw_wheel.setVisibility(View.GONE);
        btn_reload.setVisibility(View.VISIBLE);
        tv_text.setVisibility(View.VISIBLE);
        iv_icon.setVisibility(View.VISIBLE);
    }

    @Optional
    @OnClick(R.id.load_fail_reload)
    public final void reload(View v) {
        refresh();
    }
    @Nullable
    protected void refresh() {
        startActivity(new Intent(this, this.getClass()));
        this.finish();
    }

	// -----------------------------ButterKnife------------------------------------------
	public void onClick(View v) {
	}


	// ---------------------------EventBus-------------------------------------
	@Subscribe
	public void onEvent(MyEvent e) {
	}

	@Subscribe
	public void onEventMainThread(MyEvent e) {
	}

	@Subscribe
	public void onEventBackgroundThread(MyEvent e) {
	}

	@Subscribe
	public void onEventAsync(MyEvent e) {
	}

}
