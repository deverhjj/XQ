package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CircleCreatedMyFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * 我的圈子
 *
 */
public class CircleCreatedMyActivity extends BaseActivity {

    private static final String TAG = "CircleCreatedMyActivity";

    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new CircleCreatedMyFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "我的圈子";
    }


}
