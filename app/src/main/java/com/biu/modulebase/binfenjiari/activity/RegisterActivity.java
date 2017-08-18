package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.RegisterFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * Created by jhj_Plus on 2015/12/7.
 */
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";

    @Override
    protected Fragment getFragment() {
        return new RegisterFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "";
    }

}
