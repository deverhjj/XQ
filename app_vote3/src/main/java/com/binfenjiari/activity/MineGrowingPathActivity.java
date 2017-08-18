package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.binfenjiari.fragment.MineGrowingPathFragment;
import com.binfenjiari.fragment.MineIntegralFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class MineGrowingPathActivity extends BaseActivity {

    public final static int TYPE_LOGIN = 0;


    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon(null);
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        return MineGrowingPathFragment.newInstance();

    }

    @Override
    protected String getToolbarTitle() {
        return "成长足迹";
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, MineGrowingPathActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }


}
