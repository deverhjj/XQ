package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.AddMyInfoFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by hasee on 2016/11/3.
 */

public class AddMyInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        return new AddMyInfoFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "填写个人信息";
    }
}
