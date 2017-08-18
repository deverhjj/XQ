package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.SignatureFragment;
import com.biu.modulebase.binfenjiari.fragment.UserNameFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {个性签名}
 * @Description:{描述}
 * @date 2016/4/14
 */
public class SignatureActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        Intent intent=getIntent();
        String oldUserName=intent!=null?intent.getStringExtra(UserNameFragment
                .EXTRA_OLD_USER_NAME):"";
        return SignatureFragment.getInstance(oldUserName);
    }

    @Override
    protected String getToolbarTitle() {
        return "个性签名";
    }
}
