package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.binfenjiari.fragment.BasejidiRankingFragment;
import com.binfenjiari.fragment.MineMovementFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{基地排行榜}
 * @date 2017/6/8
 */
public class BaseRankingListActivity extends BaseActivity {

    public final static int TYPE_LOGIN = 0;


    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon(null);
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        return BasejidiRankingFragment.newInstance();

    }

    @Override
    protected String getToolbarTitle() {
        return "基地排行";
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, BaseRankingListActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }


}
