package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.ReportFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {举报界面}
 * @Description:{描述}
 * @date 2016/5/25
 */
public class ReportActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new ReportFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "举报";
    }
}
