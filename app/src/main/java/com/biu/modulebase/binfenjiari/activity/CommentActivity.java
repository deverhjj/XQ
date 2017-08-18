package com.biu.modulebase.binfenjiari.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CommentFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * Created by jhj_Plus on 2016/4/25.
 */
public class CommentActivity extends BaseActivity {
    private static final String TAG = "CommentActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNavigationIcon();
    }
    @Override
    protected Fragment getFragment() {
        return new CommentFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "评论";
    }
}
