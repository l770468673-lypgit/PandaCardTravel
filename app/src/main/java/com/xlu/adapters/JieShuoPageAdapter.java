package com.xlu.adapters;


import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class JieShuoPageAdapter extends PagerAdapter {
	private List<View> views;
	public JieShuoPageAdapter(List<View> views) {
		super();
		this.views = views;
	}

    @Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position));
		return views.get(position);
	}
	 @Override
     public void destroyItem(ViewGroup container, int position,
                             Object object) {
         container.removeView(views.get(position));  
     }  
}
