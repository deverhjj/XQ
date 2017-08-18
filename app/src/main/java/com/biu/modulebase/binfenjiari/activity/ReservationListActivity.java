package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.ReservationListFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {预约-基地活动列表}
 * @Description:{描述}
 * @date 2016/5/26
 */
public class ReservationListActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new ReservationListFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "基地活动";
    }
}
