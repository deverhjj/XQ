package com.biu.modulebase.binfenjiari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.datastructs.Constant;
import com.biu.modulebase.binfenjiari.fragment.JidiDetailFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/4/25.
 */
public class JidiDetailActivity extends BaseActivity {
    private static final String TAG = "JidiDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }

    @Override
    protected Fragment getFragment() {
        Intent intent=getIntent();
        int position =intent != null ? intent.getIntExtra(Constant.KEY_POSITION,-1) : -1;
        String id=intent!=null?intent.getStringExtra(Constant.KEY_ID):"-1";
        return JidiDetailFragment.newInstance(position,id);
    }

    @Override
    protected String getToolbarTitle() {
        return "基地详情";
    }
}
