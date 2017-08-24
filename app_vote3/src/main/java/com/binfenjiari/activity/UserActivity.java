package com.binfenjiari.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.binfenjiari.base.MvpActivity;
import com.binfenjiari.fragment.UserFindPwdFragment;
import com.binfenjiari.fragment.UserFragment;
import com.binfenjiari.fragment.UserLoginFragment;
import com.binfenjiari.fragment.UserRegisterDoneFragment;
import com.binfenjiari.fragment.UserRegisterFragment;
import com.binfenjiari.fragment.presenter.UserPresenter;

/**
 * @author tangjin
 * @Title: {标题}
 * @Description:{描述}
 * @date 2017/6/8
 */
public class UserActivity extends MvpActivity<UserFragment, UserPresenter> {
    public final static int TYPE_LOGIN = 0;
    public final static int TYPE_REGISTER = 1;
    public final static int TYPE_FORGET_PWD = 2;

    public final static int TYPE_REGISTER_SUCCESS = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getToolbarTitle());
    }

    private String getToolbarTitle() {
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        if (type == TYPE_LOGIN) {
            return "登录";
        } else if (type == TYPE_REGISTER) {
            return "注册";
        } else if (type == TYPE_FORGET_PWD) {
            return "忘记密码";
        } else if (type == TYPE_REGISTER_SUCCESS) {
            return "注册成功";
        } else {
            return "登录";
        }
    }

    public static void beginLogin(Context context) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("type", TYPE_LOGIN);
        context.startActivity(intent);
    }

    public static void beginRegister(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), UserActivity.class);
        intent.putExtra("type", TYPE_REGISTER);
        fragment.startActivityForResult(intent, TYPE_REGISTER);
    }

    public static void beginRegisterSuccess(Context context, Bundle args) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("type", TYPE_REGISTER_SUCCESS);
        intent.putExtras(args);
        context.startActivity(intent);
    }

    public static void beginForgetPwd(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), UserActivity.class);
        intent.putExtra("type", TYPE_FORGET_PWD);
        fragment.startActivityForResult(intent, TYPE_FORGET_PWD);
    }

    @NonNull
    @Override
    public UserFragment mvpView() {
        int type = getIntent().getIntExtra("type", TYPE_LOGIN);
        UserFragment v = null;
        if (type == TYPE_LOGIN) {
            v = new UserLoginFragment();
        } else if (type == TYPE_REGISTER) {
            v = new UserRegisterFragment();
        } else if (type == TYPE_FORGET_PWD) {
            v = new UserFindPwdFragment();
        } else if (type == TYPE_REGISTER_SUCCESS) {
            v = new UserRegisterDoneFragment();
            v.setArguments(getIntent().getExtras());
        }
        return v;
    }

    @NonNull
    @Override
    public UserPresenter mvpPresenter() {
        return new UserPresenter();
    }
}
