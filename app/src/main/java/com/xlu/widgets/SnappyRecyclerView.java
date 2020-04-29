package com.xlu.widgets;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Subclass of RecyclerView that delegates all fling logic to its layout manager
 *
 * @author carl
 */
public class SnappyRecyclerView extends RecyclerView {

    private static final String TAG = "SnappyRecyclerView";
    private int mScrollState;

    public SnappyRecyclerView(Context context) {
        this(context, null);
    }

    public SnappyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnappyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean result = super.onTouchEvent(e);

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP: {
                // IMPORTANT: Fix issue that if a tap happens during a fling, the fling animation
                // will be cancelled, resulting in scroller stopped at incorrect place (thus, no
                // snapping behaviour). This fix works because when the issue happens, it will
                // stop the animation as well as setting scroll state to SCROLL_STATE_IDLE,
                // and call onScrollStateChanged callback. We capture scroll state in
                // onScrollStateChanged, and if scroll state ever becomes SCROLL_STATE_IDLE during
                // an up event, we'll know that the animation has been stopped. So we call
                // fling() with 0 velocity to start the animation again, thus snapping to the
                // nearest possible snapping position defined in the scroller
                if (mScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    fling(0, 0);
                }
                break;
            }
        }

        return result;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        mScrollState = state;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        Log.d(TAG, String.format("fling vx: %d, vy: %d", velocityX, velocityY));
        LayoutManager lm = getLayoutManager();
        if (lm instanceof SnappyScrollCalculator) {
            SnappyScrollCalculator cal = (SnappyScrollCalculator) lm;
            int index = cal.computeScrollToItemIndex(velocityX, velocityY);
            smoothScrollToPosition(index);
            setFlag(index);
            return true;
        }
        return super.fling(velocityX, velocityY);
    }

    public void setFlag(int index) {
        if (mOnItemClickLitener != null)
            mOnItemClickLitener.onItemSelect(index);
    }

    public interface OnItemSelectLitener {
        void onItemSelect(int position);
    }

    public OnItemSelectLitener mOnItemClickLitener;

    public void setOnItemSelectLitener(OnItemSelectLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
