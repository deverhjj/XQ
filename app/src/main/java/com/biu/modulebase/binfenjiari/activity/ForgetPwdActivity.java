package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.fragment.ForgetPwFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/5/30
 */
public class ForgetPwdActivity extends BaseActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ActionBar actionBar=getSupportActionBar();
//        if (actionBar!=null) {
//            actionBar.hide();
//        }
//    }

    @Override
    protected void setActView() {
        setContentView(R.layout.activity_base_two);
        init();
    }

    protected void init() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment content = fm.findFragmentById(R.id.fragmentContainer);
        if (content == null) {
            content = getFragment();
            if (content!=null) {
                fm.beginTransaction().add(getFragmentContainerId(), content).commit();
            }
        }
    }

    @Override
    protected Fragment getFragment() {
        return new ForgetPwFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "";
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftKeybord();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
