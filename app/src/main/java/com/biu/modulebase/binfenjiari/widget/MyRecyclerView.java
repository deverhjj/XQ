package com.biu.modulebase.binfenjiari.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.biu.modulebase.binfenjiari.util.LogUtil;

/**
 * Created by jhj_Plus on 2016/5/5.
 */
public class MyRecyclerView extends RecyclerView {
    private static final String TAG = "MyRecyclerView";

    private int mSelectedPos = -1;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public int getSelectedPos() {
        return mSelectedPos;
    }


    public void setSelectedPos(int childPos) {

        if (childPos!=mSelectedPos) {
            clearChildSelectedState();
        }

        View selectedChild = getChildAt(childPos);
        if (selectedChild != null) {
            if (mSelectedPos != childPos || !selectedChild.isSelected()) {
                selectedChild.setSelected(true);
            } else if (selectedChild.isSelected()) {
                selectedChild.setSelected(false);
            }
        }
        this.mSelectedPos = childPos;
    }

    void clearChildSelectedState() {
        if (mSelectedPos >= 0 || mSelectedPos < getAdapter().getItemCount()) {
            View selectedChild = getChildAt(mSelectedPos);
            if (selectedChild != null) {
                selectedChild.setSelected(false);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(new MyGridLayoutManager(getContext(),2,VERTICAL,false));
    }

    public class MyGridLayoutManager extends GridLayoutManager {

        public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                int defStyleRes)
        {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public MyGridLayoutManager(Context context, int spanCount) {
            this(context, spanCount,VERTICAL,false);
        }

        public MyGridLayoutManager(Context context, int spanCount, int orientation,
                boolean reverseLayout)
        {
            super(context, spanCount, orientation, reverseLayout);
            setAutoMeasureEnabled(true);
        }

        @Override
        public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec)
        {
            final int itemCount=getItemCount();
            final int childCount=getChildCount();

            if (itemCount > 0) {
                final int width = LayoutManager.chooseSize(widthSpec,
                        getPaddingLeft() + getPaddingRight(),
                        ViewCompat.getMinimumWidth(MyRecyclerView.this));
                if (childCount>0) {
                    final int height = LayoutManager.chooseSize(MeasureSpec.UNSPECIFIED,
                            getPaddingTop() + getPaddingBottom(), measureChildren());
                    setMeasuredDimension(width, height);
                    LogUtil.LogE(TAG, "onMeasure" + getChildCount() + ",,,," + getItemCount());
                } else {
                    setMeasuredDimension(width, Integer.MAX_VALUE);
                }
            } else {
               super.onMeasure(recycler,state,widthSpec,heightSpec);
            }
        }

        private int measureChildren() {
            final int itemCount = getItemCount();
            final int cloumnCount = itemCount % getSpanCount() == 0 ? itemCount / getSpanCount()
                    : itemCount / getSpanCount() + 1;
            //TODO 默认第一个ChildView的高度乘行数为最终RecyclerView高度
            int height = getChildAt(0).getMeasuredHeight() * cloumnCount;
            return height;
        }


        @Override
        public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
            super.setMeasuredDimension(childrenBounds, wSpec, hSpec);
            LogUtil.LogE(TAG,"setMeasuredDimension===>"+"width="+childrenBounds.width()+"," +
                    "height="+childrenBounds.height());
        }

        @Override
        public void onLayoutChildren(Recycler recycler, State state) {
            super.onLayoutChildren(recycler, state);
            LogUtil.LogE(TAG,"onLayoutChildren"+getChildCount()+",,,,"+getItemCount());
            getChildCount();
        }

    }




}
