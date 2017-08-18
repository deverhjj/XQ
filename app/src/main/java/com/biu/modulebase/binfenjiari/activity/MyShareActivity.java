package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.MyShareFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {分享app}
 * @Description:{描述}
 * @date 2016/5/4
 */
public class MyShareActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new MyShareFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "邀请好友";
    }
}
