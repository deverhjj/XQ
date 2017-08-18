package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.ReservationJidiFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {报名的活动}
 * @Description:{描述}
 * @date 2016/4/15
 */
public class ReservationJidiActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new ReservationJidiFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "预约的基地";
    }
}
