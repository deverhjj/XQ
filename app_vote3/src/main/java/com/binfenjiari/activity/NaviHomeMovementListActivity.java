package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.binfenjiari.fragment.NaviHomeMovementFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{活动列表}
 * @date 2017/6/8
 */
public class NaviHomeMovementListActivity extends BaseActivity {

    public final static int TYPE_LOGIN = 0;


    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon(null);
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        return NaviHomeMovementFragment.newInstance();

    }

    @Override
    protected String getToolbarTitle() {
        return "热门活动";
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, NaviHomeMovementListActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }


}
