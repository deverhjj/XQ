package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.biu.modulebase.binfenjiari.fragment.CircleAllFragment;
import com.biu.modulebase.binfenjiari.fragment.CircleCreateFragment;
import com.biu.modulebase.common.base.BaseActivity;


/**
 * @author Lee
 * @Title: {创建圈子}
 * @Description:{描述}
 * @date 2016/1/14
 */
public class CircleCreateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        Intent intent=getIntent();
        String typesJsonString=intent!=null?intent.getStringExtra(CircleAllFragment.EXTRA_TYPES)
                :null;
        return CircleCreateFragment.newInstance(typesJsonString);
    }

    @Override
    protected String getToolbarTitle() {
        return "创建圈子";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CircleCreateFragment fragment = (CircleCreateFragment) getSupportFragmentManager().getFragments().get(0);
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
