package com.biu.modulebase.binfenjiari.activity;


import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CircleFragment;
import com.biu.modulebase.binfenjiari.fragment.SearchResultPostFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {帖子搜索結果}
 * @Description:{描述}
 * @date 2016/6/5
 */
public class SearchResultPostActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        String title = getIntent().getStringExtra("title");
        String circleId = getIntent().getStringExtra(CircleFragment.EXTRA_CIRCLE_ID);
        return  SearchResultPostFragment.newInstance(title,circleId);
    }

    @Override
    protected String getToolbarTitle() {
        return "搜索结果";
    }
}
