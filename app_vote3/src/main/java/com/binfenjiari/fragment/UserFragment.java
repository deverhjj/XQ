package com.binfenjiari.fragment;

import com.binfenjiari.base.AppExp;
import com.binfenjiari.base.AppFragment;
import com.binfenjiari.fragment.contract.UserContract;
import com.binfenjiari.utils.Msgs;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/18
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class UserFragment extends AppFragment<UserContract.UserPresenter>
        implements UserContract.UserView
{
    private static final String TAG = UserFragment.class.getSimpleName();

    @Override
    public void showPostFailureUi(AppExp exp) {
        super.showPostFailureUi(exp);
        Msgs.shortToast(getContext(), exp.msg());
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailed(AppExp exp) {

    }

    @Override
    public void onCodeFailed(AppExp exp) {

    }

    @Override
    public void onCodeSuccess() {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailed(AppExp exp) {

    }

    @Override
    public void onFindPwdSuccess() {

    }

    @Override
    public void onFindPwdFailed(AppExp exp) {

    }
}
