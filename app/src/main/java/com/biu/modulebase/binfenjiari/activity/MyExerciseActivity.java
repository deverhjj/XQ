package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.MyExerciseFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {我的活动}
 * @Description:{描述}
 * @date 2016/4/15
 */
public class MyExerciseActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new MyExerciseFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "我的活动";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
