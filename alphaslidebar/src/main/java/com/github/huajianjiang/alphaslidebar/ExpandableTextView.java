package com.github.huajianjiang.alphaslidebar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/9
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class ExpandableTextView extends AppCompatTextView {
    private static final String TAG = ExpandableTextView.class.getSimpleName();

    private int mOriginalMaxLines = Integer.MAX_VALUE;

    private OnExpandableStateChangeListener mExpandableListener;
    private OnExpandListener mExpandListener;
    private OnCollapseListener mCollapseListener;

    private boolean mAnimEnable;
    private ObjectAnimator mExpandAnim;
    private boolean mAnimating;
    private int mExpandedHeight;
    private int mCollpasedHeight;

    private boolean mExpandable;
    private boolean mExpanded;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mOriginalMaxLines = TextViewCompat.getMaxLines(this);
        TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, defStyleAttr,
                                               0);
        mAnimEnable = a.getBoolean(R.styleable.ExpandableTextView_animEnable, true);
        a.recycle();

        if (mAnimEnable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mExpandAnim = ObjectAnimator.ofInt(this, "Height", 0);
                mExpandAnim.setTarget(this);
                mExpandAnim.setDuration(3000);
                mExpandAnim.setInterpolator(new OvershootInterpolator());
                mExpandAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mAnimating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mAnimating = false;
                        mExpanded = !mExpanded;
                        if (mExpanded) {
                            setMaxLines(Integer.MAX_VALUE);
                        } else {
                            setMaxLines(mOriginalMaxLines);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mAnimating = false;
                        if (mExpanded) {
                            setMaxLines(Integer.MAX_VALUE);
                        } else {
                            setMaxLines(mOriginalMaxLines);
                        }
                    }

                });
            }
        }
    }

    public void setOnExpandableStateChangeListener(OnExpandableStateChangeListener listener)
    {
        mExpandableListener = listener;
    }

    public void setExpandListener(OnExpandListener expandListener)
    {
        mExpandListener = expandListener;
    }

    public void setCollapseListener(OnCollapseListener collapseListener)
    {
        mCollapseListener = collapseListener;
    }

    /**
     * get the original initial MaxLines
     * @return
     */
    public int getOriginalMaxLines() {
        return mOriginalMaxLines;
    }

    /**
     * toggle ExpandableTextView's expansion state
     */
    public void toggle(boolean force) {
        if (!mExpandable || mAnimating) return;
        if (mExpanded) {
            collapse(force);
        } else {
            expand(force);
        }
    }

    public void expand(boolean force) {
        if (!mExpandable || mAnimating || (mExpanded && !force)) return;
        mCollpasedHeight = getHeight();
        if (mAnimEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
            mExpandAnim != null)
        {
            Logger.e(TAG, "Start Expand>>>CollpasedHeight=" + mCollpasedHeight + "," +
                          "ExpandedHeight=" + mExpandedHeight);
            mExpandAnim.setIntValues(mCollpasedHeight, mExpandedHeight);
            mExpandAnim.start();
        } else {
            setMaxLines(Integer.MAX_VALUE);
            mExpanded = !mExpanded;
        }
        if (mExpandListener != null) {
            mExpandListener.onExpand(this);
        }
        if (force) {
            if (!mExpanded) {
                mExpanded = !mExpanded;
            }
        } else {
            mExpanded = !mExpanded;
        }
    }

    public void collapse(boolean force) {
        if (!mExpandable || mAnimating || (!mExpanded && !force)) return;
        mExpandedHeight = getHeight();
        if (mAnimEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
            mExpandAnim != null)
        {
            Logger.e(TAG, "Start Collapse>>>ExpandedHeight=" + mExpandedHeight + "," +
                          "CollpasedHeight=" + mCollpasedHeight);
            mExpandAnim.setIntValues(mExpandedHeight, mCollpasedHeight);
            mExpandAnim.start();
        } else {
            setMaxLines(mOriginalMaxLines);
            mExpanded = !mExpanded;
        }
        if (mCollapseListener != null) {
            mCollapseListener.onCollpase(this);
        }
        if (force) {
            if (mExpanded) {
                mExpanded = !mExpanded;
            }
        } else {
            mExpanded = !mExpanded;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Layout layout = getLayout();
        if (layout == null) return;

        final int lineCount = layout.getLineCount();
        final int originalMaxLines = mOriginalMaxLines;

        mExpandable = layout.getEllipsisCount(lineCount - 1) > 0 || lineCount > originalMaxLines;

        if (mExpandableListener != null) {
            mExpandableListener.onExpandableStateChanged(this, mExpandable);
        }

        //        if (e) {
        //            Logger.e(TAG, layout.getEllipsisStart(getLineCount() - 1) + ","+layout.getLineStart(getLineCount()-1));
        ////            getText().toString().repl
        //
        //        }
       // Logger.e(TAG, "onDraw==>");
    }

    public interface OnExpandableStateChangeListener {
        void onExpandableStateChanged(ExpandableTextView who, boolean expandable);
    }

    public interface OnExpandListener {
        void onExpand(ExpandableTextView who);
    }

    public interface OnCollapseListener {
        void onCollpase(ExpandableTextView who);
    }

}
