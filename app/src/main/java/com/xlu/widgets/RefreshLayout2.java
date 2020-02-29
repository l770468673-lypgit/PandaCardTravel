package com.xlu.widgets;

/**
 * Created by Loushui on 2015/10/28.
 */

import android.content.Context;
import android.os.Handler;
 import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pandacard.teavel.R;



/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 * 
 * @author mrsimple
 */
public class RefreshLayout2 extends SwipeRefreshLayout implements
		AbsListView.OnScrollListener {

	// 滑动到最下面时的上拉操作
	private int mTouchSlop;

	private ListView mListView;
	private OnLoadListener mOnLoadListener;
	private View mListViewFooter;

	// 一起用于滑动到底部时判断是上拉还是下拉
	private int mYDown;
	private int mLastY;

	// 是否在加载中 ( 上拉加载更多 )
	private boolean isLoading = false;

	/**
	 * @param context
	 */
	public RefreshLayout2(Context context) {
		this(context, null);
	}

	public RefreshLayout2(Context context, AttributeSet attrs) {
		super(context, attrs);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mListViewFooter = LayoutInflater.from(context).inflate(
				R.layout.listview_footer, null, false);

		this.setColorSchemeResources(R.color.google_blue, R.color.google_green,
				R.color.google_red, R.color.google_yellow);
	}

	public void autoRefresh() {
		this.postDelayed(new Runnable() {

			@Override
			public void run() {
				setRefreshing(true); // 直接调用是没有用的
			}
		}, 1);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// 初始化ListView对象
		if (mListView == null) {
			getListView();
		}
	}

	/**
	 * 获取ListView对象
	 */
	private void getListView() {
		int childs = getChildCount();
		if (childs > 0) {
			View childView = getChildAt(0);
			if (childView instanceof ListView) {
				mListView = (ListView) childView;
				// 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
				mListView.setOnScrollListener(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// 按下
			mYDown = (int) event.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			// 移动
			mLastY = (int) event.getRawY();
			break;

		case MotionEvent.ACTION_UP:
			// 抬起
			if (canLoad()) {
				loadData();
			}
			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	/**
	 * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
	 * 
	 * @return
	 */
	private boolean canLoad() {
		return isBottom() && !isLoading && isPullUp() && !isRefreshing();
	}

	/**
	 * 判断是否到了最底部
	 */
	private boolean isBottom() {

		if (mListView != null && mListView.getAdapter() != null) {
			return mListView.getLastVisiblePosition() == (mListView
					.getAdapter().getCount() - 1);
		}
		return false;
	}

	/**
	 * 是否是上拉操作
	 * 
	 * @return
	 */
	private boolean isPullUp() {
		return (mYDown - mLastY) >= mTouchSlop;
	}

	/**
	 * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
	 */
	private void loadData() {
		if (mOnLoadListener != null) {
			// 设置状态
			setLoading(true);
			mOnLoadListener.onLoad();
		}
	}

	/**
	 * @param loading
	 */
	public void setLoading(boolean loading) {
		isLoading = loading;
		if (isLoading) {
			// mListView.addFooterView(mListViewFooter);
		} else {

			new Handler() {
				public void handleMessage(android.os.Message msg) {
					// mListView.removeFooterView(mListViewFooter);
					mYDown = 0;
					mLastY = 0;

					// ((BaseAdapter) ((HeaderViewListAdapter) mListView
					// .getAdapter()).getWrappedAdapter())
					// .notifyDataSetChanged();

					((BaseAdapter) mListView.getAdapter())
							.notifyDataSetChanged();

				};
			}.sendEmptyMessage(0);
		}
	}

	/**
	 * @param loadListener
	 */
	public void setOnLoadListener(OnLoadListener loadListener) {
		mOnLoadListener = loadListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 滚动时到了最底部也可以加载更多
		if (canLoad()) {
			loadData();
		}
	}

	/**
	 * 加载更多的监听器
	 * 
	 * @author mrsimple
	 */
	public static interface OnLoadListener {
		public void onLoad();
	}
}
