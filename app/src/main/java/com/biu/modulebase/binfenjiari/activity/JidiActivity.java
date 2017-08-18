package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.JidiFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/4/21.
 */
public class JidiActivity extends BaseActivity {
    private static final String TAG = "JidiActivity";

    @Override
    protected Fragment getFragment() {
        return new JidiFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }
}
