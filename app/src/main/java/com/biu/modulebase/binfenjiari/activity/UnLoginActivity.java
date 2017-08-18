package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.biu.modulebase.binfenjiari.R;
import com.biu.modulebase.binfenjiari.fragment.UnLoginFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/5/3.
 */
public class UnLoginActivity extends BaseActivity {
    private static final String TAG = "UnLoginActivity";

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        ActionBar actionBar=getSupportActionBar();
//        if (actionBar!=null) {
//            actionBar.hide();
//        }
//        layout_app_bar.setVisibility(View.GONE);
//    }


    @Override
    protected void setActView() {
        setContentView(R.layout.activity_base_two);
        // setStatusBar();
        init();
    }

    protected void init() {
//        setTitle(null);
//        layout_app_bar = (AppBarLayout) findViewById(R.id.layout_app_bar);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        title = (TextView) findViewById(R.id.toolbar_title);
//        title.setText(getToolbarTitle());
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

