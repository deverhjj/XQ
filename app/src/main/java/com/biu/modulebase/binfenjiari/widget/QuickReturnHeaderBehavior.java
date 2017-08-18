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
public class QuickReturnHeaderBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = "QuickReturnFooterBehr";

    private static final Interpolator DEFAULT_INTERPOLATOR=new FastOutSlowInInterpolator();

    private static final int DEFAULT_DURATION=150;

    private int mDySinceDirectionChanged;

    private boolean mIsHiding;

    private boolean mIsShowing;

    public QuickReturnHeaderBehavior() {
    }

    public QuickReturnHeaderBehavior(Context context, AttributeSet attrs) {
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
//            child.setAlpha(0.5f);
            show(child);
        }

    }


    private void show(final View header) {
        mIsShowing=true;

        ViewPropertyAnimator animator = header.animate().translationY(0)
                .setInterpolator(DEFAULT_INTERPOLATOR).setDuration(DEFAULT_DURATION);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                header.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsShowing=false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsShowing=false;
                if (!mIsHiding) {
                    hide(header);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }

    private void hide(final View header) {

        mIsHiding=true;

        ViewPropertyAnimator animator = header.animate().translationY(-header.getHeight())
                .setInterpolator(DEFAULT_INTERPOLATOR).setDuration(DEFAULT_DURATION);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsHiding=false;
                header.setVisibility(View.GONE);

//                Toolbar toolbar= (Toolbar) footer.findViewById(R.id.toolbar);
//                toolbar.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsHiding=false;
                if (!mIsShowing) {
                    show(header);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }

}
