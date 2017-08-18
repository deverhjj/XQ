package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.VoteMoreFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by hasee on 2016/11/3.
 */

public class VoteMoreActivity extends BaseActivity {

    @Override
    protected Fragment getFragment() {
        return new VoteMoreFragment();
    }

    @Override
    protected String getToolbarTitle() {
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        return title;
    }
}
