package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CircleFragment;
import com.biu.modulebase.binfenjiari.fragment.NoticeListFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * @author Lee
 * @Title: {公告管理列表}
 * @Description:{描述}
 * @date 2016/1/25
 */
public class NoticeListActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        Intent intent = getIntent();
        String circleId = intent != null ? intent.getStringExtra(CircleFragment.EXTRA_CIRCLE_ID)
                : "";
        return NoticeListFragment.newInstance(circleId);
    }

    @Override
    protected String getToolbarTitle() {
        return "公告";
    }

}
