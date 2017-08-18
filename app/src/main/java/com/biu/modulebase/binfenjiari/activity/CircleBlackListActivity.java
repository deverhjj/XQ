package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CircleBlackListFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {圈子黑名单}
 * @Description:{描述}
 * @date 2016/5/4
 */
public class CircleBlackListActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new CircleBlackListFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "黑名单";
    }

}
