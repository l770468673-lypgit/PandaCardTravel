
package com.xlu.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pandacard.teavel.R;


public class StarView extends RelativeLayout {
	private int counter = 5;
	private int drawable_defoult;
	private int drawble_hight;
	private int drawable_half;
	private float star;
	private Context context;
	private float child_width;
	private boolean supportHalf;
	private LinearLayout foregroundStarView;
	private LinearLayout backgroundStarView;
	
	private  Boolean seletedable;

	public final int DECIMAL = 0;

	public final int HALF = 1;
	public final int WHOLE = 2;

	private int mPrecision;

	public StarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	

	public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.starView, defStyleAttr, 0);
		drawable_defoult = typedArray.getResourceId(
				R.styleable.starView_defoultImage, android.R.color.black);
		drawble_hight = typedArray.getResourceId(
				R.styleable.starView_hightImage, android.R.color.white);
		drawable_half = typedArray.getResourceId(
				R.styleable.starView_half_star, android.R.color.white);
		counter = typedArray.getInteger(R.styleable.starView_counterAll, 5);
		child_width = typedArray.getDimension(R.styleable.starView_childWidth,
				0);
		supportHalf = typedArray.getBoolean(R.styleable.starView_support_half,
				false);
		mPrecision = typedArray.getInt(R.styleable.starView_precision, 0);
		
		seletedable= typedArray.getBoolean(R.styleable.starView_seletedable,false);
		addChild();
	}

	private void addChild() {
		foregroundStarView = createStarViewWithImage(drawble_hight);
		backgroundStarView = createStarViewWithImage(drawable_defoult);
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addView(backgroundStarView, params);
		this.addView(foregroundStarView, params);
	}

	public StarView(Context context) {
		this(context, null);
		this.context = context;
	}

	private LinearLayout createStarViewWithImage(int drawbleId) {
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				(int) child_width, (int) child_width);
		for (int i = 0; i < counter; i++) {
			ImageView imageview = new ImageView(context);
			imageview.setImageResource(drawbleId);
			linearLayout.addView(imageview, param);
		}
		return linearLayout;
	}

	
	/**
	 * 
	 * @Title: setStar
	 * @Description:
	 * @param star
	 */
	public void setStar(float star) {
		this.star = star;
		LayoutParams params = new LayoutParams(
				(int) (star * child_width), LayoutParams.MATCH_PARENT);
		foregroundStarView.setLayoutParams(params);
		this.invalidate();
	}

	public float getStar(){
		return star;
	}
	
	
	
	
	
	private void updateView(int width) {
		LayoutParams params = new LayoutParams(
				width, LayoutParams.MATCH_PARENT);
		foregroundStarView.setLayoutParams(params);
		this.invalidate();
	}

	/**
	 * 
	 * @Title: setStartViewWidth
	 * @Description:
	 * @param width
	 */
	private void setStartViewWidth(float width) {
		int viewWidth = this.getWidth();
		if (width <= 0) {
			width = 0f;
		} else if (width > viewWidth) {
			width = (float) viewWidth;
		}
		switch (mPrecision) {
		case DECIMAL:
			star=width/viewWidth;
			updateView((int)width);
			break;
		case HALF:
			int    half = viewWidth / (counter*2);
			if (width<(half/3)) {
				setStar(0);
			}else{
			int  a=	(int) (width / half);
			
				setStar((float)a/2);
			}
			break;
		case WHOLE:
			int num = viewWidth / counter;
			if (width<(num/3)) {
				setStar(0);
			}else{
				setStar(((int)width / num)+1);
			}
			break;
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			setStartViewWidth( event.getX());
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return seletedable;
	}



	public Boolean getSeletedable() {
		return seletedable;
	}



	public void setSeletedable(Boolean seletedable) {
		this.seletedable = seletedable;
	}
	
	
	
	
}
