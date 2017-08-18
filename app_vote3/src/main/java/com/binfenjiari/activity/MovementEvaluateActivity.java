package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.binfenjiari.fragment.MineIntegralFragment;
import com.binfenjiari.fragment.MovementEvaluateFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {活动评价}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class MovementEvaluateActivity extends BaseActivity {

    public final static int TYPE_LOGIN = 0;


    @Override
    protected Fragment getFragment() {
        setBackNavigationIcon(null);
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        return MovementEvaluateFragment.newInstance();

    }

    @Override
    protected String getToolbarTitle() {
        return "评价";
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, MovementEvaluateActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }


}
