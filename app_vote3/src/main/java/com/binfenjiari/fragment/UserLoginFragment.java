package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binfenjiari.R;
import com.binfenjiari.activity.LoginActivity;
import com.binfenjiari.base.AppExp;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.fragment.contract.UserContract;
import com.binfenjiari.utils.Constants;
import com.binfenjiari.utils.Msgs;
import com.binfenjiari.utils.Uis;
import com.binfenjiari.utils.Views;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class UserLoginFragment extends UserFragment
        implements UserContract.UserView
{
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
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_submit_login:
                login();
                break;

            case R.id.tv_forget_pwd:
                LoginActivity.beginForgetPwd(this);
                break;

            case R.id.tv_register:
                LoginActivity.beginRegister(this);
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
    public void showPostFailureUi(AppExp exp) {
        super.showPostFailureUi(exp);
        Msgs.shortToast(getContext(), exp.msg());
    }
}
