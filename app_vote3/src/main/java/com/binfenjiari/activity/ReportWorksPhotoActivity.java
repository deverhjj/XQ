package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.binfenjiari.fragment.LoginForgetPwdFragment;
import com.binfenjiari.fragment.LoginLoginFragment;
import com.binfenjiari.fragment.LoginRegisterFragment;
import com.binfenjiari.fragment.ReportWorksPhotoFragment;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class ReportWorksPhotoActivity extends BaseActivity {

    public final static int TYPE_LOGIN = 0;

    public final static int TYPE_REGISTER = 1;

    public final static int TYPE_FORGET_PWD = 2;

    @Override
    protected Fragment getFragment() {
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        return ReportWorksPhotoFragment.newInstance();

    }

    @Override
    protected String getToolbarTitle() {
        return "";
    }

    public static void beginActivity(Context context) {
        Intent intent = new Intent(context, ReportWorksPhotoActivity.class);
//        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }


}
