package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.VoteSuccessFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by hasee on 2016/11/3.
 */

public class VoteSuccessActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        return new VoteSuccessFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "投票成功";
    }
}
