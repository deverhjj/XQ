package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.binfenjiari.fragment.LoginForgetPwdFragment;
import com.binfenjiari.fragment.LoginLoginFragment;
import com.binfenjiari.fragment.LoginRegisterFragment;
import com.binfenjiari.fragment.UserLoginFragment;
import com.binfenjiari.fragment.presenter.UserPresenter;
import com.biu.modulebase.common.base.BaseActivity;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class LoginActivity extends BaseActivity {

    public final static int TYPE_LOGIN = 0;

    public final static int TYPE_REGISTER = 1;

    public final static int TYPE_FORGET_PWD = 2;

    @Override
    protected Fragment getFragment() {
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        if (type == TYPE_LOGIN) {
            UserLoginFragment v = new UserLoginFragment();
            UserPresenter p = new UserPresenter();
            v.bindPresenter(p);
            p.bindView(v);
            return v;
        } else if (type == TYPE_REGISTER) {
            return LoginRegisterFragment.newInstance();

        } else if (type == TYPE_FORGET_PWD) {
            return LoginForgetPwdFragment.newInstance();

        } else {
            return LoginLoginFragment.newInstance();
        }

    }

    @Override
    protected String getToolbarTitle() {
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        if (type == TYPE_LOGIN) {
            return "登录";
        } else if (type == TYPE_REGISTER) {
            return "注册";
        } else if (type == TYPE_FORGET_PWD) {
            return "忘记密码";
        } else {
            return "登录";
        }
    }

    public static void beginLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);

    }

    public static void beginRegister(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), LoginActivity.class);
        intent.putExtra("type", TYPE_REGISTER);
        fragment.startActivityForResult(intent, TYPE_REGISTER);

    }

    public static void beginForgetPwd(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), LoginActivity.class);
        intent.putExtra("type", TYPE_FORGET_PWD);
        fragment.startActivityForResult(intent, TYPE_FORGET_PWD);

    }

}
