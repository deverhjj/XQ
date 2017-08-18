package com.biu.modulebase.binfenjiari.widget;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * Created by jhj_Plus on 2016/5/6.
 */
@SuppressWarnings("unused")
public class QuickReturnFooterBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = "QuickReturnFooterBehavior";

    private static final Interpolator DEFAULT_INTERPOLATOR=new FastOutSlowInInterpolator();

    private static final int DEFAULT_DURATION=300;

    private int mDySinceDirectionChanged;

    private boolean mIsHiding;

    private boolean mIsShowing;

    public QuickReturnFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
            View directTargetChild, View target, int nestedScrollAxes)
    {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL)!=0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target,
            int dx, int dy, int[] consumed)
    {
        if (dy > 0 && mDySinceDirectionChanged < 0 || dy < 0 && mDySinceDirectionChanged > 0) {
            child.animate().cancel();
            mDySinceDirectionChanged = 0;
        }

        mDySinceDirectionChanged += dy;

        if (mDySinceDirectionChanged > 0 && mDySinceDirectionChanged >= child.getHeight() &&
                child.getVisibility() == View.VISIBLE && !mIsHiding)
        {
            hide(child);
        } else if (mDySinceDirectionChanged < 0 && child.getVisibility() == View.GONE &&
                !mIsShowing)
        {
            show(child);
        }

    }


    private void show(final View footer) {

        if (mListener!=null) {
           mListener.onShowBefore();
        }

        mIsShowing=true;

        ViewPropertyAnimator animator = footer.animate().translationY(0)
                .setInterpolator(DEFAULT_INTERPOLATOR).setDuration(DEFAULT_DURATION);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                footer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsShowing=false;
                if (mListener!=null) {
                    mListener.onShowAfter();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsShowing=false;
                if (!mIsHiding) {
                    hide(footer);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }

    private void hide(final View footer) {

        if (mListener!=null) {
            mListener.onHideBefore();
        }

        mIsHiding=true;

        ViewPropertyAnimator animator = footer.animate().translationY(footer.getHeight())
                .setInterpolator(DEFAULT_INTERPOLATOR).setDuration(DEFAULT_DURATION);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsHiding=false;
                footer.setVisibility(View.GONE);
                if (mListener!=null) {
                    mListener.onHideAfter();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsHiding=false;
                if (!mIsShowing) {
                    show(footer);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }

    public void quickShow(View footer) {
        footer.setVisibility(View.VISIBLE);
        footer.setTranslationY(0);
    }

    public void quickHide(View footer) {
        footer.setTranslationY(footer.getHeight());
        footer.setVisibility(View.GONE);
    }


    private OnFooterViewVisibilityChangeListener mListener;
    public void setOnFooterViewVisibilityChangeListener(OnFooterViewVisibilityChangeListener listener) {
        mListener=listener;
    }

    public interface OnFooterViewVisibilityChangeListener {
        void onShowBefore();

        void onHideBefore();

        void onShowAfter();

        void onHideAfter();
    }

    public static class SimpleOnFooterViewVisibilityChangeListener
            implements OnFooterViewVisibilityChangeListener
    {

        @Override
        public void onShowBefore() {

        }

        @Override
        public void onHideBefore() {

        }

        @Override
        public void onShowAfter() {

        }

        @Override
        public void onHideAfter() {

        }
    }


}
