package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.VoteRankFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by hasee on 2016/11/3.
 */

public class VoteRankActivity extends BaseActivity {
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title=getIntent().getStringExtra("title");


        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getFragment() {
        return new VoteRankFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return title;
    }
}
