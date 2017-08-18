package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CircleManageFragment;
import com.biu.modulebase.common.base.BaseActivity;

public class CircleManageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment()
    {
        Intent intent=getIntent();
        String  circleId= intent!=null? intent.getStringExtra(
                CircleManageFragment.KEY_CIRCLE_ID) :null;
        return  CircleManageFragment.newInstance(circleId);
    }

    @Override
    protected String getToolbarTitle() {
        return "圈子管理";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CircleManageFragment fragment = (CircleManageFragment) getSupportFragmentManager().getFragments().get(0);
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
