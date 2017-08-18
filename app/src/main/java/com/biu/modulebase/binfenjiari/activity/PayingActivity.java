package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.PayingFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/5/26
 */
public class PayingActivity extends BaseActivity {

    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new PayingFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "支付订单";
    }

}
