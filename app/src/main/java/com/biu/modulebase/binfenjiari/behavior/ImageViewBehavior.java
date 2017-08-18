package com.biu.modulebase.binfenjiari.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.widget.CircleImageView;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/4/14
 */
public class ImageViewBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private boolean mIsAvatarShown = true;

    public ImageViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
         if (dependency instanceof AppBarLayout) {
            // If we're depending on an AppBarLayout we will show/hide it automatically
            updateViewVisibility(parent, (AppBarLayout) dependency, child);
        }
        return false;
    }

    private boolean updateViewVisibility(CoordinatorLayout parent,
                                        AppBarLayout appBarLayout, CircleImageView child) {
        final CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.getAnchorId() != appBarLayout.getId()) {
            // The anchor ID doesn't match the dependency, so we won't automatically
            return false;
        }
        int scrollRange= appBarLayout.getTotalScrollRange();
        int childTop =child.getTop();

        if(childTop>(scrollRange/2)){
            mIsAvatarShown = true;
            child.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }else{
            mIsAvatarShown = false;
            child.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency.getId() == R.id.header;
    }

}
