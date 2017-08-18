package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CommVoteDetailFragment2;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by hasee on 2016/11/3.
 */

public class CommDetailActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        return new CommVoteDetailFragment2();
    }

    @Override
    protected String getToolbarTitle() {
        return "投票";
    }
}
