package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CommFreshDetailFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/4/19.
 */
public class CommFreshDetailActivity extends BaseActivity {
    private static final String TAG = "CommQADetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        return new CommFreshDetailFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "新鲜事详情";
    }
}
