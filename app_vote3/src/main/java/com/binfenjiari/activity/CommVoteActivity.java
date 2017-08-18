package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.binfenjiari.fragment.MineMovementFragment;
import com.biu.modulebase.binfenjiari.fragment.CommVoteFragment2;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class CommVoteActivity extends BaseActivity {

    public final static int TYPE_LOGIN = 0;


    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon(null);
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        return new CommVoteFragment2();

    }

    @Override
    protected String getToolbarTitle() {
        return "投票";
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, CommVoteActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }


}
