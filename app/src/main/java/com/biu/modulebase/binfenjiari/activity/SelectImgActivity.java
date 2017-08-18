package com.biu.modulebase.binfenjiari.activity;


import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.SelectImgFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/1/19
 */
public class SelectImgActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        return new SelectImgFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "相机胶卷";
    }


}
