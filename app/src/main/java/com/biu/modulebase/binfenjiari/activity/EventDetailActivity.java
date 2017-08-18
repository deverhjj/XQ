package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.EventDetailFragment;
import com.biu.modulebase.common.base.BaseActivity;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by jhj_Plus on 2016/4/25.
 */
public class EventDetailActivity extends BaseActivity {
    private static final String TAG = "EventDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        Intent intent=getIntent();
        String id=intent!=null?intent.getStringExtra(Constant.KEY_ID):"-1";
        int position = intent.getIntExtra("position",-1);
        return EventDetailFragment.newInstance(id,position);
    }

    @Override
    protected String getToolbarTitle() {
        return "活动详情";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult( requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}
