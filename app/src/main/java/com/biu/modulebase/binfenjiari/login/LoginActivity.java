package com.biu.modulebase.binfenjiari.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;

import com.biu.modulebase.common.base.BaseActivity;
import com.biu.modulebase.binfenjiari.fragment.UnLoginFragment;

/**
 * Created by jhj_Plus on 2016/5/3.
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "UnLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null) {
            actionBar.hide();
        }
    }

    @Override
    protected Fragment getFragment() {
        return new UnLoginFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return null;
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

