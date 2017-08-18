package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.NotificationFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/4/20.
 */
public class NotificationActivity extends BaseActivity {
    private static final String TAG = "NotificationActivity";

    @Override
    protected Fragment getFragment() {
        return new NotificationFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "通知";
    }
}
