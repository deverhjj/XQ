package com.biu.modulebase.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.biu.modulebase.binfenjiari.fragment.CollectionFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author Lee
 * @Title: {收藏}
 * @Description:{描述}
 * @date 2016/4/15
 */
public class CollectionActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon();
        return new CollectionFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return "我的收藏";
    }

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, CollectionActivity.class));
    }
}
