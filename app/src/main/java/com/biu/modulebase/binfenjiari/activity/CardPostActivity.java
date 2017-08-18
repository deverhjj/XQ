package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.biu.modulebase.binfenjiari.fragment.CardPostFragment;
import com.biu.modulebase.binfenjiari.fragment.CircleFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/1/13.
 */
public class CardPostActivity extends BaseActivity {
    private static final String TAG = "CardPostActivity";

    private CardPostFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
       Intent intent=getIntent();
        String circleId = intent != null ? intent.getStringExtra(CircleFragment.EXTRA_CIRCLE_ID)
                : "";
        fragment =CardPostFragment.newInstance(circleId);
        return fragment;
    }

    @Override
    protected String getToolbarTitle() {
        return "发贴";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CardPostFragment fragment = (CardPostFragment) getSupportFragmentManager().getFragments().get(0);
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
