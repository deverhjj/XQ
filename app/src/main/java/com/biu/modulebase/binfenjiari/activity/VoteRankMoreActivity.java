package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.VoteRankMoreFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{描述}
 * @date ${2016/9/13}
 */
public class VoteRankMoreActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        return new VoteRankMoreFragment();
    }

    @Override
    protected String getToolbarTitle() {
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        return title;
    }
}
