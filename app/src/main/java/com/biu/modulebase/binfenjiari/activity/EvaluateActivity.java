package com.biu.modulebase.binfenjiari.activity;

import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.biu.modulebase.binfenjiari.fragment.EvaluateFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {评价}
 * @Description:{描述}
 * @date 2016/5/23
 */
public class EvaluateActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new EvaluateFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "评价";
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
