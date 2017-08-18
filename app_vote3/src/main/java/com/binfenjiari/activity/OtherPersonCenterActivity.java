package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.binfenjiari.fragment.MineIntegralFragment;
import com.binfenjiari.fragment.OtherPersonCenterFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class OtherPersonCenterActivity extends BaseActivity {

    public final static int TYPE_LOGIN = 0;


    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon(null);
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        return OtherPersonCenterFragment.newInstance();

    }

    @Override
    protected String getToolbarTitle() {
        return "刘小牛";
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, OtherPersonCenterActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }


}
