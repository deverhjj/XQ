package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.CommVoteDetail2Fragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/4/18.
 */
public class CommVoteDetail2Activity extends BaseActivity {
    private static final String TAG = "CommVoteDetailActivity";

    public static final int TYPE_VOTE_TEXT=0;

    public static final int TYPE_VOTE_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        Intent intent=getIntent();
        String id =intent.getStringExtra(Constant.KEY_ID);
        return CommVoteDetail2Fragment.newInstance(id);
    }

    @Override
    protected String getToolbarTitle() {
        return "投票图文详情";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
