package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.biu.modulebase.binfenjiari.fragment.SubmitOrderFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {标题}
 * @Description:{描述}
 * @date 2016/6/6
 */
public class SubmitOrderActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new SubmitOrderFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "立即报名";
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
