package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.CommVoteDetailFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/4/18.
 */
public class CommVoteDetailActivity extends BaseActivity {
    private static final String TAG = "CommVoteDetailActivity";

    public static final int TYPE_VOTE_TEXT=0;

    public static final int TYPE_VOTE_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        Intent intent=getIntent();
        String id =intent.getStringExtra(Constant.KEY_ID);
        return CommVoteDetailFragment.newInstance(id);
    }

    @Override
    protected String getToolbarTitle() {
        return "投票详情";
    }
}
