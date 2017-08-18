package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.SettingFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/4/14
 */
public class SettingActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        return new SettingFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "设置";
    }

    public void doPositiveClick() {
        SettingFragment fragment = (SettingFragment) getSupportFragmentManager().getFragments().get(0);
        fragment.clearCache();
    }

    public void doNegativeClick() {

    }
}
