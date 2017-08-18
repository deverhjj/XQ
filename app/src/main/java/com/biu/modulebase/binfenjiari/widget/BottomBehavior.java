package com.biu.modulebase.binfenjiari.widget;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jhj_Plus on 2016/5/10.
 */
public class BottomBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = "BottomBehavior";

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {

        child.layout(parent.getLeft(), parent.getTop()+100,
                parent.getRight(), parent.getBottom()-200);

        return true;
    }
}
