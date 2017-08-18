package com.binfenjiari.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/5/14
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class SquareFrameLayout extends FrameLayout {
    private static final String TAG = SquareFrameLayout.class.getSimpleName();

    public SquareFrameLayout(@NonNull Context context) {
        super(context);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                             @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Logger.e(TAG, "onMeasure===>w" + MeasureSpec.toString(widthMeasureSpec) + ",h=" +
//                      MeasureSpec.toString(heightMeasureSpec));

        super.onMeasure(widthMeasureSpec, MeasureSpec
                .makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec),
                        MeasureSpec.getMode(widthMeasureSpec)));
//        Logger.e(TAG, "w=" + getMeasuredWidth() + "h=" + getMeasuredHeight());
    }
}
