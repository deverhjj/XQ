package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.EventVoteDetailFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/5/4.
 */
public class EventVoteDetailActivity extends BaseActivity {
    private static final String TAG = "EventVoteDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        return new EventVoteDetailFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "投票详情";
    }
}
