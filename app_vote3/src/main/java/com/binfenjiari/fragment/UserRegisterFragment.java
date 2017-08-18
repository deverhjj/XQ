package com.binfenjiari.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binfenjiari.R;
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
public class UserRegisterFragment extends UserFragment implements UserContract.UserView {
    private static final String TAG = UserRegisterFragment.class.getSimpleName();
    private TextView mAccount;
    private TextView mPwd;
    private TextView mVCode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onGenerateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_login_register, container, false);
    }

    @Override
    public void onInitView(View root) {
        mAccount = Views.find(root, R.id.register_phone);
        mPwd = Views.find(root, R.id.register_pwd);
        mVCode = Views.find(root, R.id.verification);

        Views.find(root, R.id.register_btn).setOnClickListener(this);
        Views.find(root, R.id.send_verification).setOnClickListener(this);
        Views.find(root, R.id.policy).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.register_btn:
                register();
                break;

            case R.id.send_verification:
                getVCode();
                break;

            case R.id.policy:

                break;
        }
    }


    private void getVCode() {

    }

    private void register() {
        final String account = mAccount.getText().toString();
        final String pwd = mPwd.getText().toString();
        final String vCode = mVCode.getText().toString();
        final boolean validAccount = !TextUtils.isEmpty(account);
        final boolean validPwd = !TextUtils.isEmpty(pwd);
        final boolean validVCode = !TextUtils.isEmpty(vCode);

        if (!validAccount || !validPwd || !validVCode) {
            if (!validAccount) {
                Msgs.shortToast(getContext(), "请输入账号");
            } else if (!validPwd) {
                Msgs.shortToast(getContext(), "请输入密码");
            } else {
                Msgs.shortToast(getContext(), "请输入验证码");
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
