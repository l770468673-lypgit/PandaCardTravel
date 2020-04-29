package com.xlu.widgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;

import android.util.Log;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to setup RecyclerView for the "long press to swipe" gesture
 * TODO: Add vertical support
 *
 * @author carl
 */
@TargetApi(14)
public class SwipeGestureHelper implements View.OnTouchListener {

    private static final String TAG = "SwipeGestureHelper";
    public static final float DEFAULT_SCALE_OFFSET = 0.1F;
    public static final long DEFAULT_ANIMATION_DURATION = 50;
    public static final float DEFAULT_SWIPE_THRESHOLD_RATIO = 0.4F;
    public static final float DEFAULT_SWIPE_THRESHOLD_SPEED_DP_PER_SECOND = 800F;

    private RecyclerView mRecyclerView;
    private boolean mLongPressInAction;
    private final GestureDetectorCompat mGestureDetector;
    private float mTouchStartX;
    private float mTouchStartY;
    private int mActivePointerIndex;
    private VelocityTracker mVelocityTracker;
    private SwipeGestureAdapter mSwipeGestureAdapter;

    private View mSelectedView;
    private int mSelectedAdapterPos;
    private AnimatorHolder mSelectedAnimatorHolder;
    private AnimatorHolder mPrevAnimatorHolder;
    private AnimatorHolder mNextAnimatorHolder;
    private float mDy;
    private float mSwipeThresholdRatio = DEFAULT_SWIPE_THRESHOLD_RATIO;
    private float mSwipeThresholdSpeedDpPerSecond = DEFAULT_SWIPE_THRESHOLD_SPEED_DP_PER_SECOND;
    private OnSwipeListener mOnSwipeListener;
    private float mScaleAnimationOffset = DEFAULT_SCALE_OFFSET;
    private long mScaleAnimationDuration = DEFAULT_ANIMATION_DURATION;
    private long mOutAnimationDuration = DEFAULT_ANIMATION_DURATION;
    private long mRecoverAnimationDuration = DEFAULT_ANIMATION_DURATION;

    private List<Animator> mRunningAnimators = new ArrayList<>();

    public SwipeGestureHelper(Context context) {
        mGestureDetector = new GestureDetectorCompat(context, new LongPressGestureListener());
        mGestureDetector.setIsLongpressEnabled(false);
        mLongPressInAction = false;
        mSelectedAnimatorHolder = new AnimatorHolder();
        mPrevAnimatorHolder = new AnimatorHolder();
        mNextAnimatorHolder = new AnimatorHolder();
    }

    public float getSwipeThresholdRatio() {
        return mSwipeThresholdRatio;
    }

    /**
     * Sets the ratio threshold which determines if a swipe is successful, default is 0.4F.
     * Speed takes precedence over dragging distance in determine if a swipe is successful.
     *
     * @param threshold the ratio between the distance user has dragged the item view and the
     *                  height/width of parent
     */
    public void setSwipeThresholdRatio(float threshold) {
        this.mSwipeThresholdRatio = threshold;
    }

    public float getSwipeThresholdSpeedDpPerSecond() {
        return mSwipeThresholdSpeedDpPerSecond;
    }

    /**
     * Sets the speed threshold which determines if a swipe is successful, default is 800F.
     * Speed takes precedence over dragging distance in determine if a swipe is successful.
     *
     * @param swipeThresholdSpeedDpPerSecond the threshold speed of user's swipe in DP per second
     */
    public void setSwipeThresholdSpeedDpPerSecond(float swipeThresholdSpeedDpPerSecond) {
        mSwipeThresholdSpeedDpPerSecond = swipeThresholdSpeedDpPerSecond;
    }

    public float getScaleAnimationOffset() {
        return mScaleAnimationOffset;
    }

    /**
     * Sets the magnitude of change in scale for the scale animation when user long presses an item
     * view. The selected item view will be scaled up to 1.0F + scaleAnimationOffset, other item
     * views will be scaled down to 1.0F - scaleAnimationOffset.
     *
     * @param scaleAnimationOffset the magnitude of change in scale for scale animations
     */
    public void setScaleAnimationOffset(float scaleAnimationOffset) {
        mScaleAnimationOffset = scaleAnimationOffset;
    }

    public long getScaleAnimationDuration() {
        return mScaleAnimationDuration;
    }

    /**
     * Sets the duration of scale animation
     *
     * @param scaleAnimationDuration the animation duration
     */
    public void setScaleAnimationDuration(long scaleAnimationDuration) {
        mScaleAnimationDuration = scaleAnimationDuration;
    }

    public long getOutAnimationDuration() {
        return mOutAnimationDuration;
    }

    /**
     * Sets the duration of out animation when a swipe is successful and the selected view is
     * transitioning out of sight
     *
     * @param outAnimationDuration the animation duration
     */
    public void setOutAnimationDuration(long outAnimationDuration) {
        mOutAnimationDuration = outAnimationDuration;
    }

    public long getRecoverAnimationDuration() {
        return mRecoverAnimationDuration;
    }


    /**
     * Sets the duration of recover animation when views are recovering into their original scale
     * and position when user's finger lifts
     *
     * @param recoverAnimationDuration the animation duration
     */
    public void setRecoverAnimationDuration(long recoverAnimationDuration) {
        mRecoverAnimationDuration = recoverAnimationDuration;
    }

    public void setOnSwipeListener(OnSwipeListener listener) {
        this.mOnSwipeListener = listener;
    }

    /**
     * Sets an {@link SwipeGestureAdapter} to tell SwipeGestureHelper if item views at given
     * adapter position should be swiped at all, if it returns false, not haptic activity_feedback nor
     * swipe animation will be played even when user long-presses this item view.
     *
     * @param swipeGestureAdapter the {@link SwipeGestureAdapter} to set
     */
    public void setSwipeGestureAdapter(SwipeGestureAdapter swipeGestureAdapter) {
        mSwipeGestureAdapter = swipeGestureAdapter;
    }

    public void attachToRecyclerView(RecyclerView rv, ViewOnTouchDelegate delegate) {
        if (delegate != null) {
            delegate.addOnTouchListener(this);
        } else {
            rv.setOnTouchListener(this);
        }
        rv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                resetViewProperties(view);
                view.clearAnimation();
            }
        });
        this.mRecyclerView = rv;
    }

    private void recoverViews() {
        recoverAnimatorHolder(mSelectedAnimatorHolder);
        recoverAnimatorHolder(mPrevAnimatorHolder);
        recoverAnimatorHolder(mNextAnimatorHolder);
    }

    private void recoverAnimatorHolder(AnimatorHolder holder) {
        View v = holder.getView();
        if (v != null) {
            float currentScale = v.getScaleX();

            float translationY = v.getTranslationY();
            List<Animator> recoverAnimatorList = makeScaleAnimatorList(v, currentScale, 1.0f,
                    mRecoverAnimationDuration);
            if (translationY != 0) {
                ObjectAnimator translationYAnimator =
                        ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, translationY, 0);
                translationYAnimator.setDuration(mRecoverAnimationDuration);
                recoverAnimatorList.add(translationYAnimator);
            }

            AnimatorSet recoverAnimatorSet = new AnimatorSet();
            recoverAnimatorSet.playTogether(recoverAnimatorList);
            holder.playAnimator(recoverAnimatorSet);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        if (mLongPressInAction) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {
                    mLongPressInAction = false;

                    mVelocityTracker.computeCurrentVelocity(1000);
                    float pixelPerSecondY = VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                            mActivePointerIndex);
                    float density = v.getContext().getResources().getDisplayMetrics().density;
                    float dpPerSecondY = pixelPerSecondY / density;

                    float ratioY = mDy / (float) mRecyclerView.getHeight();

                    Log.d(TAG, String.format("Up event: dpPerSecondY: %f, ratioY: %f",
                            dpPerSecondY, ratioY));
                    if (Math.abs(dpPerSecondY) >= mSwipeThresholdSpeedDpPerSecond) {
                        onSwipe(mRecyclerView, mSelectedAdapterPos, mDy);
                    } else if (Math.abs(ratioY) > mSwipeThresholdRatio) {
                        onSwipe(mRecyclerView, mSelectedAdapterPos, mDy);
                    } else {
                        recoverViews();
                    }
                    recycleVelocityTracker();
                    mDy = 0F;
                    // Intercept this event event if it's the last event of long press action
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    float x = e.getX();
                    float y = e.getY();
                    mDy = y - mTouchStartY;
                    mSelectedView.setTranslationY(mDy);
                    mVelocityTracker.addMovement(e);
                    break;
                }
            }
        }

//        Log.d(TAG, "onTouch: " + e);
        return mLongPressInAction || mRunningAnimators.size() > 0;
    }

    private void onSwipe(final RecyclerView recyclerView, final int adapterPos, final float dy) {
        Log.d(TAG, String.format("onSwipe: %s, %d, %f", recyclerView, adapterPos, dy));
        playOutAnimation(mSelectedAnimatorHolder, recyclerView, (int) dy,
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        recoverAnimatorHolder(mPrevAnimatorHolder);
                        recoverAnimatorHolder(mNextAnimatorHolder);

                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipe(recyclerView, adapterPos, dy);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }

    private void resetAllChildrenProperties(RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            resetViewProperties(child);
        }
    }

    private void resetViewProperties(View child) {
        child.setTranslationX(0);
        child.setTranslationY(0);
        child.setScaleX(1.F);
        child.setScaleY(1.F);
    }

    private void playOutAnimation(AnimatorHolder holder, View parent, int direction,
                                  Animator.AnimatorListener listener) {
        View selectedView = holder.getView();
        float fromY = selectedView.getTranslationY();
        float toY;
        if (direction > 0) {
            toY = fromY + (parent.getBottom() - selectedView.getTop());
        } else {
            toY = fromY - selectedView.getBottom();
        }
        ObjectAnimator outAnimation = ObjectAnimator.ofFloat(selectedView, View.TRANSLATION_Y,
                fromY, toY);
        if (listener != null) {
            outAnimation.addListener(listener);
        }
        outAnimation.setDuration(mOutAnimationDuration);
        holder.playAnimator(outAnimation);
    }

    private AnimatorSet makeScaleAnimatorSet(View view, float fromScale, float toScale, long duration) {
        AnimatorSet animatorSet = new AnimatorSet();
        List<Animator> animators = makeScaleAnimatorList(view, fromScale, toScale, duration);
        animatorSet.playTogether(animators);
        return animatorSet;
    }

    private List<Animator> makeScaleAnimatorList(View view, float fromScale, float toScale, long duration) {
        List<Animator> result = new ArrayList<>();
        ValueAnimator scaleXAnimation = ObjectAnimator.ofFloat(view, View.SCALE_X,
                fromScale, toScale);
        scaleXAnimation.setInterpolator(new OvershootInterpolator());
        scaleXAnimation.setDuration(duration);
        result.add(scaleXAnimation);

        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(view, View.SCALE_Y,
                fromScale, toScale);
        scaleYAnimation.setInterpolator(new OvershootInterpolator());
        scaleYAnimation.setDuration(duration);
        result.add(scaleYAnimation);
        return result;
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public interface OnSwipeListener {
        void onSwipe(RecyclerView rv, int adapterPosition, float dy);
    }

    protected class AnimatorHolder implements Animator.AnimatorListener {
        private Animator mAnimator;
        private View mView;

        public View getView() {
            return mView;
        }

        public void setView(View view) {
            this.mView = view;
        }

        public void playAnimator(Animator animator) {
            animator.addListener(this);
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            animator.start();
            mAnimator = animator;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            mRunningAnimators.add(animation);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mRunningAnimators.remove(animation);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mRunningAnimators.remove(animation);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }

    private class LongPressGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            int pointerIndex = MotionEventCompat.getActionIndex(e);
            mActivePointerIndex = MotionEventCompat.getPointerId(e, pointerIndex);
            if (mRecyclerView != null) {
                RecyclerView rv = mRecyclerView;
                View v = rv.findChildViewUnder(e.getX(), e.getY());
                int adapterPos = rv.getChildAdapterPosition(v);
                if (mSwipeGestureAdapter != null) {
                    if (!mSwipeGestureAdapter.shouldSwipe(adapterPos)) {
                        return;
                    }
                }
                if (v != null) {
                    mLongPressInAction = true;
                    mTouchStartX = e.getX();
                    mTouchStartY = e.getY();

                    mSelectedView = v;
                    mSelectedAnimatorHolder.setView(mSelectedView);
                    if (mScaleAnimationDuration > 0) {
                        mSelectedAnimatorHolder.playAnimator(makeScaleAnimatorSet(mSelectedView,
                                1.0F, 1.0F + mScaleAnimationOffset, mScaleAnimationDuration));
                    }
                    mSelectedAdapterPos = adapterPos;
                    Log.d(TAG, "adapterPos: " + adapterPos);

                    RecyclerView.LayoutManager lm = rv.getLayoutManager();
                    View prevView = lm.findViewByPosition(adapterPos - 1);
                    if (prevView != null) {
                        mPrevAnimatorHolder.setView(prevView);
                        if (mScaleAnimationDuration > 0) {
                            mPrevAnimatorHolder.playAnimator(makeScaleAnimatorSet(prevView, 1.0f,
                                    1.0F - mScaleAnimationOffset, mScaleAnimationDuration));
                        }
                    }

                    View nextView = lm.findViewByPosition(adapterPos + 1);
                    if (nextView != null) {
                        mNextAnimatorHolder.setView(nextView);
                        if (mScaleAnimationDuration > 0) {
                            mNextAnimatorHolder.playAnimator(makeScaleAnimatorSet(nextView, 1.0f,
                                    1.0F - mScaleAnimationOffset, mScaleAnimationDuration));
                        }
                    }

                    Log.d(TAG, String.format("onLongPress: %s, %s, %s", prevView, mSelectedView, nextView));

                    recycleVelocityTracker();
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mRecyclerView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        }
    }
}
