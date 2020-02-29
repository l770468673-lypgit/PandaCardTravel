package com.xlu.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ScrollDisabledListView extends ListView {
	private int mPosition;

	public ScrollDisabledListView(Context context) {
		super(context);
	}

	public ScrollDisabledListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollDisabledListView(Context context, AttributeSet attrs,
                                  int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;
		if (actionMasked == MotionEvent.ACTION_DOWN) { // 记录手指按下时的位置
			mPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
			return super.dispatchTouchEvent(ev);
		}
		if (actionMasked == MotionEvent.ACTION_MOVE) {             // 最关键的地方，忽略MOVE 事件           
			// ListView onTouch获取不到MOVE事件所以不会发生滚动处理           
			return true;       
			} 
		// 手指抬起时         
		if (actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_CANCEL) {             
			// 手指按下与抬起都在同一个视图内，交给父控件处理，这是一个点击事件            
			if (pointToPosition((int) ev.getX(), (int) ev.getY()) == mPosition) {               
				super.dispatchTouchEvent(ev);             
				} else {               
					// 如果手指已经移出按下时的Item，说明是滚动行为，清理Item pressed状态              
					setPressed(false);                
					invalidate();                
					return true;           
					}       
			
		}
		
		return super.dispatchTouchEvent(ev); 
	}
	@Override  
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	            MeasureSpec.AT_MOST);  
	    super.onMeasure(widthMeasureSpec, expandSpec);  
	} 
	public void setListViewHeightBasedOnChildren(ScrollDisabledListView listView) { 
		 

		  // 获取ListView对应的Adapter

		  ListAdapter listAdapter = listView.getAdapter();

		  if (listAdapter == null) {

		   return;

		  }

		  int totalHeight = 0;

		  for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

		   View listItem = listAdapter.getView(i, null, listView);

		   listItem.measure(0, 0); // 计算子项View 的宽高

		   totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

		  }

		  ViewGroup.LayoutParams params = listView.getLayoutParams();

		  params.height = totalHeight
		    + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		  // listView.getDividerHeight()获取子项间分隔符占用的高度

		  // params.height最后得到整个ListView完整显示需要的高度

		  listView.setLayoutParams(params);

		 }



}
