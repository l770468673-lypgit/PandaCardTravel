package com.xlu.widgets;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.xlu.myinterface.Pullable;


public class SuperListView extends ListView implements Pullable {

	private LayoutInflater inflater;
	private View view;
	private View view1;
	private TextView tvRefresh;
	private ImageView ivPic;
	private ImageView ivLoading;
	private TextView tvFail;
	private TextView tvJianyi;
	private ObjectAnimator rotate;

	public SuperListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.load_fail, null);
		view1 = inflater.inflate(R.layout.loading, null);
		tvRefresh = (TextView) view.findViewById(R.id.tv_refresh);
		tvFail = (TextView) view.findViewById(R.id.tv_fail);
		tvJianyi = (TextView) view.findViewById(R.id.tv_fail_jianyi);
		ivPic = (ImageView) view.findViewById(R.id.iv_fail);
		ivLoading = (ImageView) view1.findViewById(R.id.iv_loading);
		rotate = new ObjectAnimator().ofFloat(ivLoading, "rotation", 0F,360);
		rotate.setRepeatCount(ValueAnimator.INFINITE);
		rotate.setDuration(1000);
		// TODO Auto-generated constructor stub
	}

	public void loading(){
		removeHeaderView(view);
		removeHeaderView(view1);
		removeFooterView(view);
		removeFooterView(view1);
		setAdapter(null);
		addHeaderView(view1,null,false);
		setAdapter(null);
		rotate.start();
	}
	
	public void loadSuccess(){
		rotate.end();
		removeHeaderView(view);
		removeHeaderView(view1);
		removeFooterView(view);
		removeFooterView(view1);
	}

	public void setTvRefresh(int visible) {
		this.tvRefresh.setVisibility(visible);
	}

	public void loadFail(int resId, String str1, String str2, String str3, OnClickListener listener){
		rotate.end();
		removeHeaderView(view);
		removeHeaderView(view1);
		removeFooterView(view);
		removeFooterView(view1);
		setAdapter(null);
		addHeaderView(view,null,false);
		setAdapter(null);
		tvRefresh.setOnClickListener(listener);
		tvFail.setText(str1);
		tvJianyi.setText(str2);
	}

	@Override
	public boolean canPullDown() {
		if (getCount() == 0)
		{
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0)!=null&&getChildAt(0).getTop() >= 0)
		{
			// 滑到顶部了
			return true;
		} else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getCount() == 0)
		{
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
					getLastVisiblePosition()
							- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return true;
		}
		return false;
	}
}
