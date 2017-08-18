package com.biu.modulebase.binfenjiari.activity;


import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.SearchResultEventFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/5
 */
public class SearchResultEventActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new SearchResultEventFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "搜索结果";
    }
}
