package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.AdMoreFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author chenbixin
 * @Title: {标题}
 * @Description:{描述}
 * @date ${2016/9/13}
 */
public class AdMoreActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        return new AdMoreFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "资讯";
    }
}
