package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CardDetailFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * Created by jhj_Plus on 2016/1/13.
 */
public class CardDetailActivity extends BaseActivity {
    private static final String TAG = "CardDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        return new CardDetailFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "帖子详情";
    }

}
