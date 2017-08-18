package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CircleAllFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * Created by jhj_Plus on 2016/1/7.
 */
public class CircleAllActivity extends BaseActivity {
    private static final String TAG = "CircleAllActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        return new CircleAllFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "所有圈子";
    }

}
