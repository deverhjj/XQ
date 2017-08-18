package com.github.huajianjiang.alphaslidebar;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/9
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class AlphaSlideBarLayout extends FrameLayout {

    public AlphaSlideBarLayout(@NonNull Context context) {
        super(context);
    }

    public AlphaSlideBarLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AlphaSlideBarLayout(@NonNull Context context, @Nullable AttributeSet attrs,
            @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }
}
