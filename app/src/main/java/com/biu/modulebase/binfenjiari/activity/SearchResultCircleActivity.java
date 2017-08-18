package com.biu.modulebase.binfenjiari.activity;


import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.CircleItemFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {圈子搜索結果}
 * @Description:{描述}
 * @date 2016/6/5
 */
public class SearchResultCircleActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        String title = getIntent().getStringExtra("title");
        String searchTag = getIntent().getStringExtra(Constant.SEARCH_TAG);
        return  CircleItemFragment.newInstance(CircleItemFragment.TYPE_SEARCH,searchTag,title);
    }

    @Override
    protected String getToolbarTitle() {
        return "搜索结果";
    }
}
