package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.UserNameFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * Created by jhj_Plus on 2016/2/25.
 */
public class UserNameActivity extends BaseActivity {
    private static final String TAG = "UserNameActivity";

    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        Intent intent=getIntent();
        String oldUserName=intent!=null?intent.getStringExtra(UserNameFragment
                .EXTRA_OLD_USER_NAME):"";
        return UserNameFragment.getInstance(oldUserName);
    }

    @Override
    protected String getToolbarTitle() {
        return "更改用户名";
    }
}
