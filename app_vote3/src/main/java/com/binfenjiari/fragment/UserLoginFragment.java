package com.binfenjiari.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.activity.AAMainVote3Activity;
import com.binfenjiari.activity.NavigationActivity;
import com.binfenjiari.activity.UserActivity;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Msgs;
import com.binfenjiari.utils.Uis;
import com.binfenjiari.utils.Views;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class UserLoginFragment extends UserFragment {
    private static final String TAG = UserLoginFragment.class.getSimpleName();
    private TextView mAccount;
    private TextView mPwd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_login_login, container, false);
    }

    @Override
    public void onInitView(View root) {
        mAccount = Views.find(root, R.id.login_phone);
        mPwd = Views.find(root, R.id.login_pwd);

        Views.find(root, R.id.btn_submit_login).setOnClickListener(this);
        Views.find(root, R.id.tv_forget_pwd).setOnClickListener(this);
        Views.find(root, R.id.tv_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_submit_login:
                login();
                break;

            case R.id.tv_forget_pwd:
                UserActivity.beginForgetPwd(this);
                break;

            case R.id.tv_register:
                UserActivity.beginRegister(this);
                break;
        }
    }

    private void login() {
        final String account = mAccount.getText().toString();
        final String pwd = mPwd.getText().toString();
        final boolean validAccount = !TextUtils.isEmpty(account);
        final boolean validPwd = !TextUtils.isEmpty(pwd);
        if (!validAccount || !validPwd) {
            if (!validAccount) {
                Msgs.shortToast(getContext(), "请输入账号");
            } else {
                Msgs.shortToast(getContext(), "请输入密码");
            }
            return;
        }
        Bundle args = new Bundle();
        args.putString(Constants.KEY_ACCOUNT, account);
        args.putString(Constants.KEY_PWD, pwd);
        presenter.login(args);
        //自动隐藏软键盘
        Uis.hideSoftInput(mPwd);
    }

    @Override
    public void onLoginSuccess() {
        Intent home = new Intent(getContext(), NavigationActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }
}
