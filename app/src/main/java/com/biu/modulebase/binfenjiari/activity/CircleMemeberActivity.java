package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CircleMemeberFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {圈子成员}
 * @Description:{描述}
 * @date 2016/5/4
 */
public class CircleMemeberActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new CircleMemeberFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "圈子成员";
    }

}
